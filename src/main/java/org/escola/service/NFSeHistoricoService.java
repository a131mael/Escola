package org.escola.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.escola.model.Configuracao;
import org.escola.model.NotaFiscal;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Importa pro banco local o historico de NFS-e da empresa direto da API de
 * distribuicao do governo (ADN -- Ambiente de Dados Nacional), via consulta
 * por NSU (numero sequencial unico, paginacao incremental). Diferente da
 * NFSeNacionalService (que so sabe das notas emitidas por ESTE sistema), essa
 * API devolve TODAS as notas da empresa, nao importa quem emitiu -- inclusive
 * as emitidas manualmente pelo Emissor Nacional Web e as antigas do sistema
 * da prefeitura (Atende.net), desde que Palhoca seja aderente ao ADN (que e).
 *
 * Motivo de existir: o relatorio mensal precisa do historico completo, e o
 * governo pode expurgar dados antigos depois de 1-2 anos -- entao salvar
 * tudo localmente (uma vez so, aqui) e o jeito de nao perder esse historico.
 */
@Stateless
public class NFSeHistoricoService {

	private static final String ADN_BASE_URL = "https://adn.nfse.gov.br/contribuintes";
	private static final int MAX_PAGINAS = 2000; // seguranca contra loop infinito

	@Inject
	private NotaFiscalService notaFiscalService;

	public static class ResultadoImportacao {
		public int novasImportadas;
		public int jaExistiam;
		public int ignoradasNaoNFSe;
		public int ignoradasNaoSomosPrestador;
		public String erro;
	}

	private boolean cnpjEmitenteNosso(String cnpjEmitente, Configuracao config) {
		if (cnpjEmitente == null || cnpjEmitente.isEmpty()) {
			return false;
		}
		String cnpjNosso = config.getCnpj().replaceAll("[^0-9]", "");
		return cnpjEmitente.equals(cnpjNosso);
	}

	public ResultadoImportacao importarHistorico(Configuracao config) {
		ResultadoImportacao resultado = new ResultadoImportacao();
		System.out.println("[NFSE-HIST] importarHistorico iniciado");
		try {
			SSLContext sslContext = criarSslContext(config);
			long nsu = 0;
			for (int pagina = 0; pagina < MAX_PAGINAS; pagina++) {
				// cnpjConsulta precisa ser enviado explicitamente mesmo em autoconsulta --
				// sem ele a API rejeita com StatusProcessamento=REJEICAO e Erros vazio,
				// sem nenhum detalhe do motivo (confirmado empiricamente).
				String cnpjLimpo = config.getCnpj().replaceAll("[^0-9]", "");
				String url = ADN_BASE_URL + "/DFe/" + nsu + "?lote=true&cnpjConsulta=" + cnpjLimpo;
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				if (conn instanceof HttpsURLConnection) {
					((HttpsURLConnection) conn).setSSLSocketFactory(sslContext.getSocketFactory());
				}
				conn.setConnectTimeout(20000);
				conn.setReadTimeout(30000);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				int status = conn.getResponseCode();
				InputStream respStream = (status >= 200 && status < 300) ? conn.getInputStream()
						: conn.getErrorStream();
				String respBody = lerStream(respStream);

				System.out.println("[NFSE-HIST] pagina=" + pagina + " nsu=" + nsu + " status=" + status);
				if (status == 404) {
					break; // NENHUM_DOCUMENTO_LOCALIZADO -- acabou o historico
				}
				if (status != 200) {
					resultado.erro = "HTTP " + status + ": " + respBody;
					System.out.println("[NFSE-HIST] ERRO: " + resultado.erro);
					break;
				}

				JSONObject json = new JSONObject(respBody);
				JSONArray loteDFe = json.optJSONArray("LoteDFe");
				System.out.println("[NFSE-HIST] itens no lote=" + (loteDFe != null ? loteDFe.length() : 0));
				if (loteDFe == null || loteDFe.length() == 0) {
					break;
				}

				long maiorNsuDoLote = nsu;
				for (int i = 0; i < loteDFe.length(); i++) {
					JSONObject item = loteDFe.getJSONObject(i);
					long nsuItem = item.optLong("NSU", maiorNsuDoLote);
					if (nsuItem > maiorNsuDoLote) {
						maiorNsuDoLote = nsuItem;
					}
					if (!"NFSE".equals(item.optString("TipoDocumento", ""))) {
						resultado.ignoradasNaoNFSe++;
						continue;
					}
					String chaveAcesso = item.optString("ChaveAcesso", "");
					if (chaveAcesso.isEmpty() || notaFiscalService.existeComChaveAcesso(chaveAcesso)) {
						resultado.jaExistiam++;
						continue;
					}
					String arquivoXmlB64 = item.optString("ArquivoXml", "");
					if (arquivoXmlB64.isEmpty()) {
						continue;
					}
					String xml = gunzipBase64(arquivoXmlB64);
					// a consulta por CNPJ traz tanto as notas que a empresa EMITIU (receita)
					// quanto as que RECEBEU de outros prestadores (despesas) -- so importa
					// pro relatorio de faturamento as que a empresa e o prestador (emit).
					String cnpjEmitente = extrair(xml, "<emit><CNPJ>(\\d+)</CNPJ>");
					if (!cnpjEmitenteNosso(cnpjEmitente, config)) {
						resultado.ignoradasNaoSomosPrestador++;
						continue;
					}
					NotaFiscal nota = montarNotaFiscalDoXml(xml, chaveAcesso, config.getNfseAmbiente());
					if (nota != null) {
						notaFiscalService.save(nota);
						resultado.novasImportadas++;
					}
				}

				if (maiorNsuDoLote <= nsu) {
					break; // nao avancou, evita loop infinito
				}
				nsu = maiorNsuDoLote;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultado.erro = e.toString();
		}
		System.out.println("[NFSE-HIST] finalizado: novas=" + resultado.novasImportadas + " jaExistiam="
				+ resultado.jaExistiam + " naoNFSe=" + resultado.ignoradasNaoNFSe + " naoSomosPrestador="
				+ resultado.ignoradasNaoSomosPrestador + " erro=" + resultado.erro);
		return resultado;
	}

	private NotaFiscal montarNotaFiscalDoXml(String xml, String chaveAcesso, String ambiente) {
		String numero = extrair(xml, "<nNFSe>(\\d+)</nNFSe>");
		String valorStr = extrair(xml, "<vLiq>([\\d.]+)</vLiq>");
		String dhProc = extrair(xml, "<dhProc>([^<]+)</dhProc>");
		String nomeTomador = extrair(xml, "<toma>.*?<xNome>([^<]+)</xNome>");

		if (numero.isEmpty() && valorStr.isEmpty()) {
			return null; // nao parece uma NFS-e valida, pula
		}

		NotaFiscal nota = new NotaFiscal();
		nota.setChaveAcesso(chaveAcesso);
		nota.setNumero(numero);
		nota.setStatus("AUTORIZADA");
		nota.setOrigem("HISTORICO_ADN");
		nota.setAmbiente(ambiente);
		nota.setXmlNfseRetorno(xml);
		if (!valorStr.isEmpty()) {
			try {
				nota.setValor(Double.parseDouble(valorStr));
			} catch (NumberFormatException e) {
				// deixa valor nulo
			}
		}
		if (!nomeTomador.isEmpty()) {
			nota.setNomeTomador(nomeTomador);
		}
		Date data = parseDhProc(dhProc);
		nota.setDataEnvio(data);
		nota.setDataAutorizacao(data);
		return nota;
	}

	private Date parseDhProc(String dhProc) {
		if (dhProc == null || dhProc.isEmpty()) {
			return new Date();
		}
		try {
			// formato: 2026-11-10T17:37:24-03:00
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
			return sdf.parse(dhProc);
		} catch (Exception e) {
			return new Date();
		}
	}

	private String extrair(String xml, String regex) {
		Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(xml);
		return m.find() ? m.group(1) : "";
	}

	private String gunzipBase64(String base64) throws Exception {
		byte[] comprimido = java.util.Base64.getDecoder().decode(base64);
		try (java.util.zip.GZIPInputStream gzis = new java.util.zip.GZIPInputStream(
				new ByteArrayInputStream(comprimido))) {
			return lerStream(gzis);
		}
	}

	private String lerStream(InputStream is) throws Exception {
		if (is == null) {
			return "";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n;
		while ((n = is.read(buffer)) != -1) {
			baos.write(buffer, 0, n);
		}
		return baos.toString("UTF-8");
	}

	/** Mesma raiz que faltava no cacerts pro host do Sefin Nacional -- ver
	 * NFSeNacionalService.GLOBALSIGN_ROOT_R46_PEM pro detalhe completo. */
	private static final String GLOBALSIGN_ROOT_R46_PEM = "-----BEGIN CERTIFICATE-----\n"
			+ "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\n"
			+ "-----END CERTIFICATE-----\n";

	private SSLContext criarSslContext(Configuracao config) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		try (FileInputStream fis = new FileInputStream(config.getCertificadoPath())) {
			keyStore.load(fis, config.getCertificadoSenha().toCharArray());
		}
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, config.getCertificadoSenha().toCharArray());

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null, null);
		int i = 0;
		javax.net.ssl.TrustManagerFactory tmfPadrao = javax.net.ssl.TrustManagerFactory
				.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
		tmfPadrao.init((KeyStore) null);
		for (javax.net.ssl.TrustManager tm : tmfPadrao.getTrustManagers()) {
			if (tm instanceof javax.net.ssl.X509TrustManager) {
				for (java.security.cert.X509Certificate issuer : ((javax.net.ssl.X509TrustManager) tm)
						.getAcceptedIssuers()) {
					trustStore.setCertificateEntry("padrao-" + (i++), issuer);
				}
			}
		}
		java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
		java.security.cert.X509Certificate globalSignRootR46 = (java.security.cert.X509Certificate) cf
				.generateCertificate(new ByteArrayInputStream(GLOBALSIGN_ROOT_R46_PEM.getBytes(StandardCharsets.UTF_8)));
		trustStore.setCertificateEntry("globalsign-root-r46", globalSignRootR46);

		javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory
				.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
		return sslContext;
	}
}

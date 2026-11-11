package org.escola.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import java.util.ArrayList;
import java.util.List;

import org.escola.model.Configuracao;
import org.escola.model.PixRecebido;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Consulta a situacao de boletos via API de Cobranca Bancaria V3 do Sicoob
 * (https://developers.sicoob.com.br). Em ambiente de producao usa OAuth2
 * client_credentials com mTLS (mesmo certificado A1 usado na DPS/NFSe); em
 * sandbox usa as credenciais publicas de homologacao do Sicoob.
 *
 * Copia adaptada de org.escolar.service.SicoobBoletoService (sistema da
 * Favo de Mel) -- so a parte de consulta de boleto e Pix recebidos, sem o
 * pagamento de Pix (nao usado no Adonai).
 */
@Stateless
public class SicoobBoletoService {

	private static final String SANDBOX_BASE_URL = "https://sandbox.sicoob.com.br/sicoob/sandbox";
	private static final String SANDBOX_CLIENT_ID = "9b5e603e428cc477a2841e2683c92d21";
	private static final String SANDBOX_TOKEN = "1301865f-c6bc-38f3-9f49-666dbcfc59c3";
	private static final String PRODUCAO_BASE_URL = "https://api.sicoob.com.br";
	private static final String TOKEN_URL = "https://auth.sicoob.com.br/auth/realms/cooperado/protocol/openid-connect/token";

	/**
	 * Retorna a descricao da situacao do boleto (ex: "Em Aberto", "Liquidado",
	 * "Baixado"). Lanca exception com detalhes da resposta HTTP em caso de erro.
	 */
	public String consultarSituacaoBoleto(Configuracao config, long nossoNumero) throws Exception {
		boolean producao = "producao".equalsIgnoreCase(config.getSicoobAmbiente());

		String baseUrl = producao ? PRODUCAO_BASE_URL : SANDBOX_BASE_URL;
		String clientId = producao ? config.getSicoobClientId() : SANDBOX_CLIENT_ID;
		SSLContext sslContext = producao ? criarSslContext(config) : null;
		String token = producao ? obterToken(config, sslContext) : SANDBOX_TOKEN;

		long nossoNumeroComDv = calcularNossoNumeroComDv(config, nossoNumero);
		String url = baseUrl + "/cobranca-bancaria/v3/boletos?numeroCliente="
				+ urlEncode(config.getSicoobNumeroContrato())
				+ "&codigoModalidade=" + urlEncode(config.getSicoobCodigoModalidade())
				+ "&nossoNumero=" + nossoNumeroComDv;

		HttpURLConnection conn = abrirConexao(url, sslContext);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		conn.setRequestProperty("client_id", clientId);
		conn.setRequestProperty("Accept", "application/json");

		String body = lerResposta(conn);
		int httpStatus = conn.getResponseCode();
		if (httpStatus != 200) {
			if (body != null && body.contains("5002")) {
				return "Boleto não enviado";
			}
			throw new Exception("HTTP " + httpStatus + ": " + body);
		}

		return extrairSituacao(body);
	}

	private String obterToken(Configuracao config, SSLContext sslContext) throws Exception {
		return obterTokenComScope(config, sslContext, "boletos_consulta", 15000);
	}

	private HttpURLConnection abrirConexao(String urlStr, SSLContext sslContext) throws IOException {
		return abrirConexao(urlStr, sslContext, 15000, 15000);
	}

	private HttpURLConnection abrirConexao(String urlStr, SSLContext sslContext, int connectTimeout, int readTimeout)
			throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
		if (sslContext != null && conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) conn).setSSLSocketFactory(sslContext.getSocketFactory());
		}
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		return conn;
	}

	private SSLContext criarSslContext(Configuracao config) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		try (FileInputStream fis = new FileInputStream(config.getCertificadoPath())) {
			keyStore.load(fis, config.getCertificadoSenha().toCharArray());
		}

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, config.getCertificadoSenha().toCharArray());

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
		return sslContext;
	}

	private String lerResposta(HttpURLConnection conn) throws IOException {
		int status = conn.getResponseCode();
		InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
		if (is == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				sb.append(linha);
			}
		}
		return sb.toString();
	}

	/**
	 * Sicoob armazena o nosso numero como (nossoNumero * 10 + DV). O DV usa o
	 * algoritmo: concatena agencia(4) + beneficiario(10) + nossoNumero(7), aplica
	 * pesos [3,1,9,7,...] mod 11; DV=0 se resto 0 ou 1, senao DV=11-resto.
	 */
	private long calcularNossoNumeroComDv(Configuracao config, long nossoNumero) {
		String agencia = String.format("%04d", Long.parseLong(
				config.getSicoobCodigoAgencia() == null ? "0" : config.getSicoobCodigoAgencia().trim()));
		String beneficiario = String.format("%010d", Long.parseLong(
				config.getSicoobNumeroContrato() == null ? "0" : config.getSicoobNumeroContrato().trim()));
		String nn = String.format("%07d", nossoNumero);
		String s = agencia + beneficiario + nn;
		int[] pesos = { 3, 1, 9, 7, 3, 1, 9, 7, 3, 1, 9, 7, 3, 1, 9, 7, 3, 1, 9, 7, 3 };
		int soma = 0;
		for (int i = 0; i < s.length(); i++) {
			soma += Character.getNumericValue(s.charAt(i)) * pesos[i];
		}
		int resto = soma % 11;
		int dv = (resto == 0 || resto == 1) ? 0 : (11 - resto);
		return nossoNumero * 10 + dv;
	}

	/**
	 * Consulta Pix recebidos no mes/ano via API Sicoob Pix V2.
	 * Retorna lista vazia (nunca lanca excecao) para nao travar a tela caso a API esteja fora.
	 */
	private static volatile List<PixRecebido> pixRecebidosCache = null;
	private static volatile long pixRecebidosCacheTs = 0;
	private static volatile String pixRecebidosCacheKey = "";
	private static final long PIX_CACHE_TTL_MS = 5 * 60 * 1000L;

	public List<PixRecebido> consultarPixRecebidos(Configuracao config, int mes, int ano) {
		String cacheKey = mes + "/" + ano;
		long agora = System.currentTimeMillis();
		if (pixRecebidosCache != null
				&& cacheKey.equals(pixRecebidosCacheKey)
				&& (agora - pixRecebidosCacheTs) < PIX_CACHE_TTL_MS) {
			return pixRecebidosCache;
		}

		List<PixRecebido> lista = new ArrayList<>();
		try {
			if (!"producao".equalsIgnoreCase(config.getSicoobAmbiente())) {
				return lista;
			}
			SSLContext sslContext = criarSslContext(config);
			String clientId = config.getSicoobPixClientId() != null
					? config.getSicoobPixClientId() : config.getSicoobClientId();

			Configuracao pixConfig = new Configuracao();
			pixConfig.setCertificadoPath(config.getCertificadoPath());
			pixConfig.setCertificadoSenha(config.getCertificadoSenha());
			pixConfig.setSicoobClientId(clientId);
			pixConfig.setSicoobAmbiente(config.getSicoobAmbiente());
			String token = obterTokenComScope(pixConfig, sslContext, "pix.read", 20000);

			java.util.Calendar calInicio = java.util.Calendar.getInstance();
			calInicio.set(ano, mes - 1, 1, 0, 0, 0);
			calInicio.set(java.util.Calendar.MILLISECOND, 0);

			java.util.Calendar calFim = java.util.Calendar.getInstance();
			calFim.set(ano, mes - 1, 1, 23, 59, 59);
			calFim.set(java.util.Calendar.DAY_OF_MONTH, calFim.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));

			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
			String inicio = sdf.format(calInicio.getTime());
			String fim = sdf.format(calFim.getTime());

			String url = PRODUCAO_BASE_URL + "/pix/api/v2/pix?inicio=" + urlEncode(inicio)
					+ "&fim=" + urlEncode(fim) + "&paginacao.itensPorPagina=100&paginacao.paginaAtual=0";

			HttpURLConnection conn = abrirConexao(url, sslContext, 15000, 60000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + token);
			conn.setRequestProperty("client_id", clientId);
			conn.setRequestProperty("Accept", "application/json");

			String body = lerResposta(conn);
			int status = conn.getResponseCode();
			if (status != 200) {
				return pixRecebidosCache != null && cacheKey.equals(pixRecebidosCacheKey) ? pixRecebidosCache : lista;
			}

			JSONObject json = new JSONObject(body);
			JSONArray pix = json.optJSONArray("pix");
			if (pix != null) {
				for (int i = 0; i < pix.length(); i++) {
					JSONObject p = pix.getJSONObject(i);
					PixRecebido pr = new PixRecebido();
					pr.setEndToEndId(p.optString("endToEndId", ""));
					pr.setTxid(p.optString("txid", ""));
					pr.setValor(formatarValorPix(p.optString("valor", "")));
					pr.setHorario(formatarHorarioPix(p.optString("horario", "")));
					pr.setInfoPagador(p.optString("infoPagador", ""));
					JSONObject pagador = p.optJSONObject("pagador");
					if (pagador != null) {
						pr.setNomePagador(pagador.optString("nome", ""));
						pr.setCpfPagador(formatarDocumentoPix(pagador.optString("cpf", pagador.optString("cnpj", ""))));
					}
					lista.add(pr);
				}
			}
			pixRecebidosCache = lista;
			pixRecebidosCacheKey = cacheKey;
			pixRecebidosCacheTs = System.currentTimeMillis();
		} catch (Exception e) {
			System.err.println("[PIX-RECEBIDOS] Falha ao consultar: " + e.getMessage());
			if (pixRecebidosCache != null && cacheKey.equals(pixRecebidosCacheKey)) {
				return pixRecebidosCache;
			}
		}
		return lista;
	}

	private String formatarDocumentoPix(String doc) {
		if (doc == null)
			return "";
		String d = doc.replaceAll("\\D", "");
		if (d.length() == 11)
			return d.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
		if (d.length() == 14)
			return d.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
		return doc;
	}

	private String formatarValorPix(String valor) {
		if (valor == null || valor.isEmpty())
			return "";
		try {
			java.math.BigDecimal bd = new java.math.BigDecimal(valor);
			java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("pt", "BR"));
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			return nf.format(bd);
		} catch (Exception e) {
			return valor;
		}
	}

	private String formatarHorarioPix(String iso) {
		if (iso == null || iso.isEmpty())
			return "";
		try {
			java.time.ZonedDateTime zdt = java.time.ZonedDateTime.parse(iso);
			java.time.ZonedDateTime br = zdt.withZoneSameInstant(java.time.ZoneId.of("America/Sao_Paulo"));
			String[] dias = { "domingo", "segunda-feira", "terça-feira", "quarta-feira", "quinta-feira",
					"sexta-feira", "sábado" };
			String diaSemana = dias[br.getDayOfWeek().getValue() % 7];
			return String.format("%s %02d/%02d/%04d %02d:%02d",
					diaSemana, br.getDayOfMonth(), br.getMonthValue(), br.getYear(),
					br.getHour(), br.getMinute());
		} catch (Exception e) {
			return iso;
		}
	}

	private String obterTokenComScope(Configuracao config, SSLContext sslContext, String scope, int timeout)
			throws Exception {
		HttpURLConnection conn = abrirConexao(TOKEN_URL, sslContext, timeout, timeout);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);

		String corpo = "grant_type=client_credentials"
				+ "&client_id=" + urlEncode(config.getSicoobClientId())
				+ "&scope=" + urlEncode(scope);

		try (OutputStream os = conn.getOutputStream()) {
			os.write(corpo.getBytes(StandardCharsets.UTF_8));
		}

		String body = lerResposta(conn);
		int status = conn.getResponseCode();
		if (status != 200) {
			throw new Exception("Falha ao obter token Sicoob - HTTP " + status + ": " + body);
		}
		return new JSONObject(body).getString("access_token");
	}

	private String urlEncode(String valor) throws IOException {
		return URLEncoder.encode(valor == null ? "" : valor, "UTF-8");
	}

	/**
	 * O schema exato do retorno varia (lista "resultado" ou objeto unico);
	 * procura por chaves de situacao conhecidas e, na ausencia, devolve o JSON
	 * bruto para diagnostico/ajuste posterior.
	 */
	private String extrairSituacao(String body) {
		try {
			JSONObject json = new JSONObject(body);
			JSONObject alvo = json;

			if (json.has("resultado")) {
				Object resultado = json.get("resultado");
				if (resultado instanceof JSONArray) {
					JSONArray arr = (JSONArray) resultado;
					if (arr.length() == 0) {
						return "Boleto não encontrado";
					}
					alvo = arr.getJSONObject(0);
				} else if (resultado instanceof JSONObject) {
					alvo = (JSONObject) resultado;
				}
			}

			for (String chave : new String[] { "situacao", "situacaoBoleto", "descricaoSituacao", "status" }) {
				if (alvo.has(chave)) {
					Object valor = alvo.get(chave);
					if (valor instanceof JSONObject) {
						JSONObject sit = (JSONObject) valor;
						if (sit.has("descricao")) {
							return sit.getString("descricao");
						}
						return sit.toString();
					}
					return String.valueOf(valor);
				}
			}

			return "Resposta Sicoob: " + body;
		} catch (Exception e) {
			return "Resposta Sicoob: " + body;
		}
	}
}

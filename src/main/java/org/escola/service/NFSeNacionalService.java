package org.escola.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.escola.model.Configuracao;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Emite NFS-e pelo Sistema Nacional de NFS-e (ADN -- Ambiente de Dados
 * Nacional, gov.br/nfse), substituindo a antiga integracao via Atende.net
 * (Palhoca). Monta e assina a DPS (Declaracao de Prestacao de Servico)
 * conforme leiaute oficial v1.01, e envia via mTLS com o mesmo certificado
 * A1 ja usado no Sicoob.
 *
 * Fontes oficiais usadas pra montar isso: ANEXO I (leiaute DPS v1.01) e os
 * XSDs em gov.br/nfse/.../documentacao-tecnica. cTribNac "080101" = Ensino
 * regular pre-escolar, fundamental e medio (LC 116/2003, item 08).
 */
@Stateless
public class NFSeNacionalService {

	private static final String SANDBOX_BASE_URL = "https://sefin.producaorestrita.nfse.gov.br/SefinNacional";
	private static final String PRODUCAO_BASE_URL = "https://sefin.nfse.gov.br/SefinNacional";
	private static final String NS_NFSE = "http://www.sped.fazenda.gov.br/nfse";
	private static final String NS_DSIG = "http://www.w3.org/2000/09/xmldsig#";

	/** Codigo de tributacao nacional pra ensino regular (pre-escolar, fundamental, medio) */
	private static final String CTRIBNAC_ENSINO = "080101";
	/** NBS correspondente a ensino pre-escolar/fundamental/medio (confirmado em nota real emitida p/ Adonai) */
	private static final String CNBS_ENSINO = "122012000";

	public static class ResultadoEmissao {
		public boolean sucesso;
		public String chaveAcesso;
		public String numero;
		public String xmlRetorno;
		public String motivoRejeicao;
	}

	/**
	 * Monta, assina e envia a DPS pro sistema nacional. nDps deve ser unico por
	 * serie (ex: usar o id da NotaFiscal no banco).
	 */
	public ResultadoEmissao emitir(Configuracao config, long nDps, String nomeTomador, String cpfTomador,
			double valorServico, String descricaoServico) throws Exception {
		boolean producao = "producao".equalsIgnoreCase(config.getNfseAmbiente());
		String baseUrl = producao ? PRODUCAO_BASE_URL : SANDBOX_BASE_URL;

		String idDps = montarIdDps(config, nDps);
		Document doc = montarDps(config, idDps, producao, nomeTomador, cpfTomador, valorServico, descricaoServico);
		assinarDocumento(doc, config, idDps);

		String xmlAssinado = documentoParaString(doc);

		SSLContext sslContext = criarSslContext(config);
		String url = baseUrl + "/nfse";

		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		if (conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) conn).setSSLSocketFactory(sslContext.getSocketFactory());
		}
		conn.setConnectTimeout(20000);
		conn.setReadTimeout(30000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setDoOutput(true);

		JSONObject corpo = new JSONObject();
		corpo.put("dpsXmlGZipB64", gzipBase64(xmlAssinado));

		try (OutputStream os = conn.getOutputStream()) {
			os.write(corpo.toString().getBytes(StandardCharsets.UTF_8));
		}

		int status = conn.getResponseCode();
		InputStream respStream = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
		String respBody = lerStream(respStream);

		ResultadoEmissao resultado = new ResultadoEmissao();
		JSONObject json;
		try {
			json = new JSONObject(respBody);
		} catch (Exception e) {
			resultado.sucesso = false;
			resultado.xmlRetorno = respBody;
			resultado.motivoRejeicao = "HTTP " + status + " (resposta nao-JSON): " + respBody;
			return resultado;
		}
		if (status == 201) {
			resultado.sucesso = true;
			resultado.chaveAcesso = optStringCI(json, "chaveAcesso");
			String nfseXmlGZipB64 = optStringCI(json, "nfseXmlGZipB64");
			resultado.xmlRetorno = nfseXmlGZipB64.isEmpty() ? respBody : gunzipBase64(nfseXmlGZipB64);
			java.util.regex.Matcher m = java.util.regex.Pattern.compile("<nNFSe>(\\d+)</nNFSe>")
					.matcher(resultado.xmlRetorno);
			resultado.numero = m.find() ? m.group(1) : "";
		} else {
			resultado.sucesso = false;
			resultado.xmlRetorno = respBody;
			StringBuilder motivo = new StringBuilder("HTTP " + status);
			org.json.JSONArray erros = json.optJSONArray("erros");
			if (erros != null) {
				for (int i = 0; i < erros.length(); i++) {
					JSONObject erro = erros.getJSONObject(i);
					String codigo = optStringCI(erro, "codigo");
					String descricao = optStringCI(erro, "descricao");
					String complemento = optStringCI(erro, "complemento");
					motivo.append(" | ").append(codigo).append(": ").append(descricao);
					if (!complemento.isEmpty()) {
						motivo.append(" (").append(complemento).append(")");
					}
				}
			}
			resultado.motivoRejeicao = motivo.toString();
		}
		return resultado;
	}

	/** A API as vezes documenta campos em minusculo (swagger) mas retorna em
	 * PascalCase em runtime (ou vice-versa) -- busca sem diferenciar maiuscula. */
	private String optStringCI(JSONObject obj, String nomeCampo) {
		for (String chave : obj.keySet()) {
			if (chave.equalsIgnoreCase(nomeCampo)) {
				return obj.optString(chave, "");
			}
		}
		return "";
	}

	private String montarIdDps(Configuracao config, long nDps) {
		String codMun = config.getCodigoMunicipio(); // 7 digitos, ex: 4211900
		String cnpj = config.getCnpj().replaceAll("[^0-9]", "");
		String serie = "00001";
		String numero = String.format("%015d", nDps);
		return "DPS" + codMun + "2" + cnpj + serie + numero;
	}

	private Document montarDps(Configuracao config, String idDps, boolean producao, String nomeTomador,
			String cpfTomador, double valorServico, String descricaoServico) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();

		Element dps = doc.createElementNS(NS_NFSE, "DPS");
		dps.setAttribute("versao", "1.01");
		doc.appendChild(dps);

		Element infDps = doc.createElementNS(NS_NFSE, "infDPS");
		infDps.setAttribute("Id", idDps);
		infDps.setIdAttribute("Id", true);
		dps.appendChild(infDps);

		// TSDateTimeUTC exige offset numerico (+hh:mm/-hh:mm) -- 'Z' literal nao bate no pattern do XSD
		SimpleDateFormat sdfUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		sdfUtc.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		SimpleDateFormat sdfData = new SimpleDateFormat("yyyy-MM-dd");

		add(doc, infDps, "tpAmb", producao ? "1" : "2");
		add(doc, infDps, "dhEmi", sdfUtc.format(new Date()));
		add(doc, infDps, "verAplic", "EscolaAdonai1.0");
		add(doc, infDps, "serie", "00001");
		add(doc, infDps, "nDPS", String.valueOf(Long.parseLong(idDps.substring(30))));
		add(doc, infDps, "dCompet", sdfData.format(new Date()));
		add(doc, infDps, "tpEmit", "1"); // 1 = Prestador
		// finNFSe foi removido do schema atual de producao (confirmado contra XML real
		// emitido); substituido por cMotivoEmisTI/chNFSeRej, ambos opcionais e nao
		// usados aqui (so se aplicam a emissao por TI ou substituicao de nota)
		add(doc, infDps, "cLocEmi", config.getCodigoMunicipio());

		// prest
		Element prest = doc.createElementNS(NS_NFSE, "prest");
		infDps.appendChild(prest);
		add(doc, prest, "CNPJ", config.getCnpj().replaceAll("[^0-9]", ""));
		if (config.getInscricaoMunicipal() != null && !config.getInscricaoMunicipal().isEmpty()) {
			add(doc, prest, "IM", config.getInscricaoMunicipal());
		}
		// xNome NAO vai aqui -- regra de negocio: quando o emitente da DPS e o proprio
		// prestador (tpEmit=1), o nome nao deve ser informado (e redundante, o
		// cadastro nacional ja sabe o nome pelo CNPJ). Confirmado via E0121 + XML real.
		Element regTrib = doc.createElementNS(NS_NFSE, "regTrib");
		prest.appendChild(regTrib);
		add(doc, regTrib, "opSimpNac", "3"); // 3 = Optante ME/EPP Simples Nacional
		add(doc, regTrib, "regApTribSN", "1"); // 1 = Regime de apuracao dos tributos federais e municipal pelo SN
		add(doc, regTrib, "regEspTrib", "0"); // 0 = Nenhum regime especial

		// toma
		Element toma = doc.createElementNS(NS_NFSE, "toma");
		infDps.appendChild(toma);
		String cpfLimpo = cpfTomador != null ? cpfTomador.replaceAll("[^0-9]", "") : "";
		add(doc, toma, "CPF", cpfLimpo);
		add(doc, toma, "xNome", nomeTomador != null ? nomeTomador : "");

		// serv
		Element serv = doc.createElementNS(NS_NFSE, "serv");
		infDps.appendChild(serv);
		Element locPrest = doc.createElementNS(NS_NFSE, "locPrest");
		serv.appendChild(locPrest);
		add(doc, locPrest, "cLocPrestacao", config.getCodigoMunicipio());
		Element cServ = doc.createElementNS(NS_NFSE, "cServ");
		serv.appendChild(cServ);
		add(doc, cServ, "cTribNac", CTRIBNAC_ENSINO);
		add(doc, cServ, "xDescServ", descricaoServico != null ? descricaoServico : "Mensalidade escolar");
		add(doc, cServ, "cNBS", CNBS_ENSINO);

		// valores
		Element valores = doc.createElementNS(NS_NFSE, "valores");
		infDps.appendChild(valores);
		Element vServPrest = doc.createElementNS(NS_NFSE, "vServPrest");
		valores.appendChild(vServPrest);
		add(doc, vServPrest, "vServ", String.format(java.util.Locale.US, "%.2f", valorServico));
		Element trib = doc.createElementNS(NS_NFSE, "trib");
		valores.appendChild(trib);
		Element tribMun = doc.createElementNS(NS_NFSE, "tribMun");
		trib.appendChild(tribMun);
		add(doc, tribMun, "tribISSQN", "1"); // 1 = Operacao tributavel
		add(doc, tribMun, "tpRetISSQN", "1"); // 1 = Nao retido
		Element totTrib = doc.createElementNS(NS_NFSE, "totTrib");
		trib.appendChild(totTrib);
		// pTotTribSN = aliquota aproximada total de tributos pelo Simples Nacional (Lei
		// 12.741/2012, so informativo). Confirmado contra NFS-e real emitida p/ Adonai
		// (13.45%) -- varia com o faturamento acumulado dos ultimos 12 meses, entao o
		// valor precisa ser revisado periodicamente com o contador.
		add(doc, totTrib, "pTotTribSN", "13.45");

		return doc;
	}

	private void add(Document doc, Element pai, String nome, String valor) {
		Element el = doc.createElementNS(NS_NFSE, nome);
		el.setTextContent(valor);
		pai.appendChild(el);
	}

	private void assinarDocumento(Document doc, Configuracao config, String idDps) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		try (FileInputStream fis = new FileInputStream(config.getCertificadoPath())) {
			keyStore.load(fis, config.getCertificadoSenha().toCharArray());
		}
		String alias = Collections.list(keyStore.aliases()).get(0);
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, config.getCertificadoSenha().toCharArray());
		X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);

		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

		Transform envelopedTransform = fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14nTransform = fac.newTransform(CanonicalizationMethod.INCLUSIVE, (TransformParameterSpec) null);

		DigestMethod digestMethod = fac.newDigestMethod(DigestMethod.SHA256, null);
		Reference ref = fac.newReference("#" + idDps, digestMethod, List.of(envelopedTransform, c14nTransform), null,
				null);

		CanonicalizationMethod cm = fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
				(C14NMethodParameterSpec) null);
		SignatureMethod sm = fac.newSignatureMethod(SignatureMethod.RSA_SHA256, null);
		SignedInfo si = fac.newSignedInfo(cm, sm, List.of(ref));

		KeyInfoFactory kif = fac.getKeyInfoFactory();
		X509Data x509Data = kif.newX509Data(List.of(cert));
		KeyInfo ki = kif.newKeyInfo(List.of(x509Data));

		XMLSignature signature = fac.newXMLSignature(si, ki);

		Element dpsEl = doc.getDocumentElement();
		DOMSignContext dsc = new DOMSignContext(privateKey, dpsEl);
		dsc.setDefaultNamespacePrefix("");
		signature.sign(dsc);
	}

	private String documentoParaString(Document doc) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		StreamResult result = new StreamResult(new java.io.StringWriter());
		transformer.transform(new DOMSource(doc), result);
		return result.getWriter().toString();
	}

	private String gzipBase64(String texto) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (java.util.zip.GZIPOutputStream gzos = new java.util.zip.GZIPOutputStream(baos)) {
			gzos.write(texto.getBytes(StandardCharsets.UTF_8));
		}
		return java.util.Base64.getEncoder().encodeToString(baos.toByteArray());
	}

	private String gunzipBase64(String base64) throws IOException {
		byte[] comprimido = java.util.Base64.getDecoder().decode(base64);
		try (java.util.zip.GZIPInputStream gzis = new java.util.zip.GZIPInputStream(
				new ByteArrayInputStream(comprimido))) {
			return lerStream(gzis);
		}
	}

	private String lerStream(InputStream is) throws IOException {
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

	/** Raiz "GlobalSign Root R46" -- emite a cadeia do servidor do Sefin Nacional
	 * (via SERPRO), mas essa raiz (de 2019/2025) nao esta no cacerts padrao do
	 * OpenJDK 11.0.8 usado no container, causando PKIX path building failed.
	 * Adicionada explicitamente aqui, so pra essa conexao -- nao mexe no
	 * truststore global da JVM. */
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
				for (X509Certificate issuer : ((javax.net.ssl.X509TrustManager) tm).getAcceptedIssuers()) {
					trustStore.setCertificateEntry("padrao-" + (i++), issuer);
				}
			}
		}
		java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
		X509Certificate globalSignRootR46 = (X509Certificate) cf.generateCertificate(
				new ByteArrayInputStream(GLOBALSIGN_ROOT_R46_PEM.getBytes(StandardCharsets.UTF_8)));
		trustStore.setCertificateEntry("globalsign-root-r46", globalSignRootR46);

		javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory
				.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
		return sslContext;
	}
}

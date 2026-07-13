package org.escola.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Stateless;

import org.escola.util.MensagemWA;

@Stateless
public class EvolutionApiService implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String BASE_URL = "http://192.168.15.19:8083";
	private static final String INSTANCE = "adonai";
	private static final String API_KEY = "adonai2024";

	// cache numNorm → JID real (pode ser @lid ou @s.whatsapp.net), populado ao enviar mensagens
	private static final ConcurrentHashMap<String, String> jidCache = new ConcurrentHashMap<>();

	public static final String PODE_COBRAR = "PODE_COBRAR";
	public static final String NAO_COBRAR = "NAO_COBRAR";
	public static final String INDECISO = "INDECISO";

	private static final String MSG_COBRANCA_1 =
		"Olá %s! Identificamos que o boleto com vencimento em %s encontra-se em aberto. " +
		"Caso precise, podemos reenviar o boleto para facilitar o pagamento. " +
		"Se já efetuou o pagamento, por favor desconsidere esta mensagem. " +
		"Qualquer dúvida estamos à disposição!";

	private static final String MSG_COBRANCA_2 =
		"Prezado(a) %s, informamos que o boleto referente ao mês de %s ainda consta em aberto em nosso sistema. " +
		"É importante manter os pagamentos em dia para garantir a continuidade dos serviços prestados. " +
		"Caso já tenha efetuado o pagamento, pedimos que envie o comprovante para regularização. " +
		"Em caso de dúvidas ou para negociação, entre em contato conosco. Agradecemos a atenção.";

	private static final List<String> KEYWORDS_PAGO = Arrays.asList(
		"paguei", "comprovante", "transferi", "pix", "acordo", "combinado",
		"pago", "quitei", "enviei", "mandei", "já paguei", "efetuei"
	);

	public boolean enviarMensagem(String numero, String texto) {
		try {
			String numNorm = normalizarTelefone(numero);
			if (numNorm == null) return false;

			URL url = new URL(BASE_URL + "/message/sendText/" + INSTANCE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("apikey", API_KEY);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(15000);

			String body = "{\"number\":\"" + numNorm + "\",\"textMessage\":{\"text\":" + escapeJson(texto) + "}}";
			try (OutputStream os = conn.getOutputStream()) {
				os.write(body.getBytes("UTF-8"));
			}

			int code = conn.getResponseCode();
			if (code >= 200 && code < 300) {
				// captura o JID real usado (pode ser @lid) para usar nas consultas futuras
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) sb.append(line);
					br.close();
					String jid = extractJsonString(sb.toString(), "remoteJid");
					if (jid != null && !jid.isEmpty()) jidCache.put(numNorm, jid);
				} catch (Exception ignored) {}
				return true;
			}
			return false;
		} catch (Exception e) {
			System.err.println("EvolutionApiService.enviarMensagem erro: " + e.getMessage());
			return false;
		}
	}

	public String analisarStatusCobranca(List<String> telefones) {
		for (String tel : telefones) {
			if (tel == null || tel.trim().isEmpty()) continue;
			String trimmed = tel.trim().toLowerCase();
			if (trimmed.startsWith("x") || trimmed.equals("x")) continue;

			String numNorm = normalizarTelefone(tel);
			if (numNorm == null) continue;

			String msgs = buscarMensagensRecentes(numNorm);
			if (msgs != null) {
				String msgsLower = msgs.toLowerCase();
				for (String keyword : KEYWORDS_PAGO) {
					if (msgsLower.contains(keyword)) {
						return NAO_COBRAR;
					}
				}
				return PODE_COBRAR;
			}
		}
		return INDECISO;
	}

	public String resolverJid(String numero) {
		try {
			String numNorm = normalizarTelefone(numero);
			if (numNorm == null) return null;
			// se já enviamos uma mensagem antes, temos o JID real (pode ser @lid)
			if (jidCache.containsKey(numNorm)) return jidCache.get(numNorm);

			URL url = new URL(BASE_URL + "/chat/whatsappNumbers/" + INSTANCE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("apikey", API_KEY);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setConnectTimeout(8000);
			conn.setReadTimeout(10000);

			String body = "{\"numbers\":[\"" + numNorm + "\"]}";
			try (OutputStream os = conn.getOutputStream()) {
				os.write(body.getBytes("UTF-8"));
			}

			if (conn.getResponseCode() != 200) return numNorm + "@s.whatsapp.net";

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) sb.append(line);
			br.close();

			String resp = sb.toString();
			// extrai "jid":"XXXXX@s.whatsapp.net"
			String jidKey = "\"jid\":\"";
			int idx = resp.indexOf(jidKey);
			if (idx >= 0) {
				int start = idx + jidKey.length();
				int end = resp.indexOf("\"", start);
				if (end > start) return resp.substring(start, end);
			}
			return numNorm + "@s.whatsapp.net";
		} catch (Exception e) {
			return normalizarTelefone(numero) + "@s.whatsapp.net";
		}
	}

	private String buscarMensagensRecentes(String numero) {
		try {
			String jid = resolverJid(numero);
			if (jid == null) return null;

			URL url = new URL(BASE_URL + "/chat/findMessages/" + INSTANCE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("apikey", API_KEY);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(15000);

			String body = "{\"where\":{\"key\":{\"remoteJid\":\"" + jid + "\"}},\"limit\":30}";
			try (OutputStream os = conn.getOutputStream()) {
				os.write(body.getBytes("UTF-8"));
			}

			int code = conn.getResponseCode();
			if (code != 200) return null;

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) sb.append(line);
			br.close();

			String resp = sb.toString();
			if (resp != null && (resp.contains("\"conversation\"") || resp.contains("\"text\""))) {
				return resp;
			}
			return null;
		} catch (Exception e) {
			System.err.println("EvolutionApiService.buscarMensagensRecentes erro: " + e.getMessage());
			return null;
		}
	}

	public List<MensagemWA> buscarMensagens(String numero) {
		return buscarMensagensSince(numero, 0L);
	}

	public List<MensagemWA> buscarMensagensSince(String numero, long sinceTimestamp) {
		List<MensagemWA> result = new ArrayList<MensagemWA>();
		try {
			// Build list of JIDs to try: resolved + alternate Brazilian format (8 vs 9 digit)
			List<String> jidsToTry = new ArrayList<String>();
			if (numero != null && !numero.trim().isEmpty()) {
				String jid = resolverJid(numero);
				if (jid != null) jidsToTry.add(jid);
				// Also try alternate format: Brazilian 9-digit → 8-digit and vice-versa
				String numNorm = normalizarTelefone(numero);
				if (numNorm != null && numNorm.startsWith("55") && numNorm.length() == 13) {
					// 55 + 2 area + 9 + 8digits → try without the leading 9
					String alt = "55" + numNorm.substring(2, 4) + numNorm.substring(5) + "@s.whatsapp.net";
					if (!jidsToTry.contains(alt)) jidsToTry.add(alt);
				} else if (numNorm != null && numNorm.startsWith("55") && numNorm.length() == 12) {
					// 55 + 2 area + 8digits → try with leading 9
					String alt = "55" + numNorm.substring(2, 4) + "9" + numNorm.substring(4) + "@s.whatsapp.net";
					if (!jidsToTry.contains(alt)) jidsToTry.add(alt);
				}
			}

			for (String jid : jidsToTry) {
				// usa dot-notation "key.remoteJid" — formato correto para Evolution API v1.8.x
				// $gt não é suportado via HTTP nesta versão; deduplicação por messageId resolve incrementalidade
				String body = "{\"where\":{\"key.remoteJid\":\"" + jid + "\"},\"limit\":0}";
				List<MensagemWA> found = fetchMensagens(body);
				result.addAll(found);
				if (!found.isEmpty()) break;
			}
		} catch (Exception e) {
			System.err.println("EvolutionApiService.buscarMensagens erro: " + e.getMessage());
		}
		return result;
	}

	private List<MensagemWA> fetchMensagens(String body) {
		try {
			URL url = new URL(BASE_URL + "/chat/findMessages/" + INSTANCE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("apikey", API_KEY);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(15000);

			try (OutputStream os = conn.getOutputStream()) {
				os.write(body.getBytes("UTF-8"));
			}

			if (conn.getResponseCode() != 200) return new ArrayList<MensagemWA>();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) sb.append(line);
			br.close();

			return parseMensagens(sb.toString());
		} catch (Exception e) {
			return new ArrayList<MensagemWA>();
		}
	}

	private List<MensagemWA> parseMensagens(String json) {
		List<MensagemWA> result = new ArrayList<MensagemWA>();
		if (json == null || json.trim().isEmpty()) return result;

		// Walk the JSON character-by-character to extract each top-level object,
		// tracking brace depth so we handle any field ordering or nested objects.
		int i = 0;
		int len = json.length();

		// skip to first '[' or '{'
		while (i < len && json.charAt(i) != '[' && json.charAt(i) != '{') i++;

		// if it's a bare object (not wrapped in array), wrap it virtually
		boolean isArray = i < len && json.charAt(i) == '[';
		if (isArray) i++; // skip '['

		while (i < len) {
			// skip whitespace / commas between elements
			while (i < len && (json.charAt(i) == ',' || json.charAt(i) == ' '
					|| json.charAt(i) == '\n' || json.charAt(i) == '\r'
					|| json.charAt(i) == '\t')) i++;

			if (i >= len || json.charAt(i) == ']') break;
			if (json.charAt(i) != '{') { i++; continue; }

			// found start of a top-level element — find its end
			int start = i;
			int depth = 0;
			boolean inStr = false;
			char prev = 0;
			while (i < len) {
				char c = json.charAt(i);
				if (inStr) {
					if (c == '"' && prev != '\\') inStr = false;
				} else {
					if (c == '"') inStr = true;
					else if (c == '{') depth++;
					else if (c == '}') { depth--; if (depth == 0) { i++; break; } }
				}
				prev = (c == '\\' && prev == '\\') ? 0 : c;
				i++;
			}

			String elem = json.substring(start, i);
			parsearElemento(elem, result);
		}
		return result;
	}

	private void parsearElemento(String elem, List<MensagemWA> result) {
		boolean fromMe = false;
		int fromMeIdx = elem.indexOf("\"fromMe\":");
		if (fromMeIdx >= 0) {
			int v = fromMeIdx + 9;
			while (v < elem.length() && elem.charAt(v) == ' ') v++;
			fromMe = elem.startsWith("true", v);
		}

		String messageId = extractJsonString(elem, "id");
		String pushName = extractJsonString(elem, "pushName");

		long waTimestamp = 0;
		int tsIdx = elem.indexOf("\"messageTimestamp\":");
		if (tsIdx >= 0) {
			int v = tsIdx + 19;
			while (v < elem.length() && (elem.charAt(v) == ' ' || elem.charAt(v) == '{')) v++;
			// handle {"low":N,"high":0} format
			if (v < elem.length() && elem.charAt(v) != '"') {
				int lowIdx = elem.indexOf("\"low\":", tsIdx);
				if (lowIdx >= 0 && lowIdx < tsIdx + 60) {
					int lv = lowIdx + 6;
					while (lv < elem.length() && elem.charAt(lv) == ' ') lv++;
					int end = lv;
					while (end < elem.length() && (Character.isDigit(elem.charAt(end)) || elem.charAt(end) == '-')) end++;
					if (end > lv) try { waTimestamp = Long.parseLong(elem.substring(lv, end)); } catch (Exception ignored) {}
				} else {
					int end = v;
					while (end < elem.length() && Character.isDigit(elem.charAt(end))) end++;
					if (end > v) try { waTimestamp = Long.parseLong(elem.substring(v, end)); } catch (Exception ignored) {}
				}
			}
		}

		// Extract text from known message types
		String text = extractJsonString(elem, "conversation");
		if (text == null) text = extractJsonString(elem, "text");
		if (text == null) text = extractJsonString(elem, "caption");

		// Media types without text — show placeholder
		if (text == null || text.trim().isEmpty()) {
			if (elem.contains("\"imageMessage\"")) text = "[Imagem]";
			else if (elem.contains("\"videoMessage\"")) text = "[Vídeo]";
			else if (elem.contains("\"audioMessage\"") || elem.contains("\"pttMessage\"")) text = "[Áudio]";
			else if (elem.contains("\"documentMessage\"")) {
				String title = extractJsonString(elem, "title");
				text = title != null && !title.isEmpty() ? "[Documento: " + title + "]" : "[Documento]";
			}
			else if (elem.contains("\"stickerMessage\"")) text = "[Sticker]";
			else if (elem.contains("\"reactionMessage\"")) return; // ignora reações
			else if (elem.contains("\"contactMessage\"") || elem.contains("\"contactsArrayMessage\"")) text = "[Contato]";
			else if (elem.contains("\"locationMessage\"")) text = "[Localização]";
		}

		if (text != null && !text.trim().isEmpty()) {
			result.add(new MensagemWA(text, fromMe, waTimestamp, messageId, pushName));
		}
	}

	private String extractJsonString(String json, String key) {
		String search = "\"" + key + "\":\"";
		int idx = json.indexOf(search);
		if (idx < 0) return null;
		int start = idx + search.length();
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < json.length(); i++) {
			char c = json.charAt(i);
			if (c == '\\' && i + 1 < json.length()) {
				char next = json.charAt(i + 1);
				if (next == '"') { sb.append('"'); i++; }
				else if (next == 'n') { sb.append('\n'); i++; }
				else if (next == '\\') { sb.append('\\'); i++; }
				else sb.append(c);
			} else if (c == '"') {
				break;
			} else {
				sb.append(c);
			}
		}
		return sb.length() > 0 ? sb.toString() : null;
	}

	public String getMsgCobranca1(String nomeResponsavel, String mes) {
		return String.format(MSG_COBRANCA_1, nomeResponsavel, mes);
	}

	public String getMsgCobranca2(String nomeResponsavel, String mes) {
		return String.format(MSG_COBRANCA_2, nomeResponsavel, mes);
	}

	public String normalizarTelefone(String tel) {
		if (tel == null) return null;
		String num = tel.replaceAll("[^0-9]", "");
		if (num.isEmpty()) return null;
		if (num.startsWith("55") && num.length() >= 12) return num;
		if (num.length() == 11) return "55" + num;       // DDD + 9 dígitos
		if (num.length() == 10) return "55" + num;       // DDD + 8 dígitos
		if (num.length() == 9)  return "5548" + num;     // sem DDD, 9 dígitos → DDD 48
		if (num.length() == 8)  return "5548" + num;     // sem DDD, 8 dígitos → DDD 48
		return null;
	}

	private String escapeJson(String s) {
		if (s == null) return "\"\"";
		return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
			.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t") + "\"";
	}
}

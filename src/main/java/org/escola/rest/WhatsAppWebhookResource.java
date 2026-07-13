package org.escola.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.escola.model.Aluno;
import org.escola.service.AlunoService;
import org.escola.service.MensagemWhatsAppService;

@Stateless
@Path("/webhook")
public class WhatsAppWebhookResource {

	@Inject
	private AlunoService alunoService;

	@Inject
	private MensagemWhatsAppService mensagemWhatsAppService;

	@POST
	@Path("/whatsapp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response receberMensagem(String payload) {
		try {
			if (payload == null || payload.trim().isEmpty()) return Response.ok("{}").build();

			String event = extractJsonString(payload, "event");
			if (!"messages.upsert".equals(event)) return Response.ok("{}").build();

			boolean fromMe = payload.contains("\"fromMe\":true");
			if (fromMe) return Response.ok("{}").build();

			String pushName = extractJsonString(payload, "pushName");
			if (pushName == null || pushName.trim().isEmpty()) return Response.ok("{}").build();

			String messageId = extractJsonString(payload, "id");
			long ts = extractTimestamp(payload, "messageTimestamp");

			String text = extractJsonString(payload, "conversation");
			if (text == null) text = extractJsonString(payload, "text");
			if (text == null) text = extractJsonString(payload, "caption");
			if (text == null) {
				if (payload.contains("\"imageMessage\"")) text = "[Imagem]";
				else if (payload.contains("\"videoMessage\"")) text = "[Vídeo]";
				else if (payload.contains("\"audioMessage\"") || payload.contains("\"pttMessage\"")) text = "[Áudio]";
				else if (payload.contains("\"documentMessage\"")) text = "[Documento]";
				else if (payload.contains("\"stickerMessage\"")) text = "[Sticker]";
				else if (payload.contains("\"reactionMessage\"")) return Response.ok("{}").build();
				else text = "[Mensagem]";
			}

			List<Aluno> alunos = alunoService.findAlunoPorNomeContato(pushName);
			if (alunos != null && !alunos.isEmpty()) {
				Aluno aluno = alunos.get(0);
				String telefone = getPrimTelefoneValido(aluno);
				if (telefone != null) {
					mensagemWhatsAppService.salvarRecebida(aluno, telefone, messageId, text, ts);
				}
			}
		} catch (Exception e) {
			System.err.println("WhatsAppWebhookResource erro: " + e.getMessage());
		}
		return Response.ok("{}").build();
	}

	private String getPrimTelefoneValido(Aluno a) {
		String[] tels = {a.getContatoTelefone1(), a.getContatoTelefone2(),
						 a.getContatoTelefone3(), a.getContatoTelefone4()};
		for (String t : tels) {
			if (t != null && !t.trim().isEmpty() && !t.trim().toLowerCase().startsWith("x")) return t;
		}
		return null;
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

	private long extractTimestamp(String json, String key) {
		String search = "\"" + key + "\":";
		int idx = json.indexOf(search);
		if (idx < 0) return System.currentTimeMillis() / 1000L;
		int v = idx + search.length();
		while (v < json.length() && json.charAt(v) == ' ') v++;
		if (v < json.length() && json.charAt(v) == '"') {
			v++;
			int end = v;
			while (end < json.length() && json.charAt(end) != '"') end++;
			try { return Long.parseLong(json.substring(v, end)); } catch (Exception e) { /* fallthrough */ }
		}
		int end = v;
		while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
		try { return Long.parseLong(json.substring(v, end)); } catch (Exception e) { /* fallthrough */ }
		return System.currentTimeMillis() / 1000L;
	}
}

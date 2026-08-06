package org.escola.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

import org.escola.model.MensagemWhatsApp;
import org.escola.service.EvolutionApiService;
import org.escola.service.EvolutionApiService.MidiaResult;
import org.escola.service.MensagemWhatsAppService;

@Stateless
@Path("/whatsapp")
public class WhatsAppMidiaResource {

	@Inject
	private EvolutionApiService evolutionApiService;

	@Inject
	private MensagemWhatsAppService mensagemWhatsAppService;

	@GET
	@Path("/midia")
	public Response getMidia(@QueryParam("id") String messageId,
			@QueryParam("tel") String telefone,
			@QueryParam("fromMe") boolean fromMe) {

		if (messageId == null || messageId.trim().isEmpty() || telefone == null || telefone.trim().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		// 1. já temos salvo no banco (caminho normal pra mensagens novas) - nunca expira
		MensagemWhatsApp salva = mensagemWhatsAppService.buscarPorMessageId(messageId);
		if (salva != null && salva.getMidia() != null) {
			return responder(salva.getMidia(), salva.getMidiaMimetype());
		}

		// 2. mensagem antiga sem mídia salva ainda - tenta buscar ao vivo na Evolution API
		MidiaResult midia = evolutionApiService.buscarMidia(messageId, telefone, fromMe);
		if (midia == null || midia.getBytes() == null) {
			return Response.status(Response.Status.NOT_FOUND)
				.entity("Mídia indisponível (link do WhatsApp pode ter expirado).")
				.build();
		}

		// backfill: já que conseguimos agora, salva pra não depender de buscar de novo depois
		if (salva != null) {
			mensagemWhatsAppService.salvarMidia(salva.getId(), midia.getBytes(), midia.getMimetype());
		}

		return responder(midia.getBytes(), midia.getMimetype());
	}

	private Response responder(byte[] bytes, String mimetype) {
		CacheControl cc = new CacheControl();
		cc.setMaxAge(86400);
		cc.setPrivate(true);

		return Response.ok(bytes)
			.type(mimetype != null ? mimetype : "application/octet-stream")
			.cacheControl(cc)
			.build();
	}
}

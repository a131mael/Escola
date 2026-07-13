package org.escola.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

import org.escola.service.EvolutionApiService;
import org.escola.service.EvolutionApiService.MidiaResult;

@Stateless
@Path("/whatsapp")
public class WhatsAppMidiaResource {

	@Inject
	private EvolutionApiService evolutionApiService;

	@GET
	@Path("/midia")
	public Response getMidia(@QueryParam("id") String messageId,
			@QueryParam("tel") String telefone,
			@QueryParam("fromMe") boolean fromMe) {

		if (messageId == null || messageId.trim().isEmpty() || telefone == null || telefone.trim().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		MidiaResult midia = evolutionApiService.buscarMidia(messageId, telefone, fromMe);
		if (midia == null || midia.getBytes() == null) {
			return Response.status(Response.Status.NOT_FOUND)
				.entity("Mídia indisponível (link do WhatsApp pode ter expirado).")
				.build();
		}

		CacheControl cc = new CacheControl();
		cc.setMaxAge(86400);
		cc.setPrivate(true);

		String mimetype = midia.getMimetype() != null ? midia.getMimetype() : "application/octet-stream";
		return Response.ok(midia.getBytes())
			.type(mimetype)
			.cacheControl(cc)
			.build();
	}
}

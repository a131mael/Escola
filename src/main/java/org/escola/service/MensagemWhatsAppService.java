package org.escola.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.escola.model.Aluno;
import org.escola.model.MensagemWhatsApp;
import org.escola.util.MensagemWA;

@Stateless
public class MensagemWhatsAppService implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;

	@javax.inject.Inject
	private EvolutionApiService evolutionApiService;

	public List<MensagemWA> carregarDosBanco(Aluno aluno, String telefone) {
		if (aluno == null || telefone == null) return new ArrayList<MensagemWA>();
		String telNorm = telefone.replaceAll("[^0-9]", "");

		TypedQuery<MensagemWhatsApp> q = em.createQuery(
			"SELECT m FROM MensagemWhatsApp m WHERE m.aluno.id = :alunoId AND m.telefone = :tel ORDER BY m.waTimestamp ASC",
			MensagemWhatsApp.class);
		q.setParameter("alunoId", aluno.getId());
		q.setParameter("tel", telNorm);

		List<MensagemWA> result = new ArrayList<MensagemWA>();
		for (MensagemWhatsApp m : q.getResultList()) {
			result.add(new MensagemWA(m.getTexto(), m.isFromMe(), m.getWaTimestamp(), m.getMessageId()));
		}
		return result;
	}

	public long ultimoTimestamp(Aluno aluno, String telefone) {
		if (aluno == null || telefone == null) return 0L;
		String telNorm = telefone.replaceAll("[^0-9]", "");

		try {
			Long ts = em.createQuery(
				"SELECT MAX(m.waTimestamp) FROM MensagemWhatsApp m WHERE m.aluno.id = :alunoId AND m.telefone = :tel",
				Long.class)
				.setParameter("alunoId", aluno.getId())
				.setParameter("tel", telNorm)
				.getSingleResult();
			return ts != null ? ts : 0L;
		} catch (Exception e) {
			return 0L;
		}
	}

	public void salvarRecebida(Aluno aluno, String telefone, String messageId, String texto, long waTimestamp) {
		salvarRecebida(aluno, telefone, messageId, texto, waTimestamp, null, null);
	}

	public void salvarRecebida(Aluno aluno, String telefone, String messageId, String texto, long waTimestamp,
			byte[] midia, String midiaMimetype) {
		if (aluno == null || telefone == null || texto == null) return;
		String telNorm = telefone.replaceAll("[^0-9]", "");
		if (telNorm.isEmpty()) return;
		if (messageId != null && !messageId.isEmpty()) {
			Long count = em.createQuery("SELECT COUNT(m) FROM MensagemWhatsApp m WHERE m.messageId = :mid", Long.class)
				.setParameter("mid", messageId).getSingleResult();
			if (count > 0) return;
		}
		MensagemWhatsApp msg = new MensagemWhatsApp(aluno, telNorm, messageId, texto, false, waTimestamp);
		msg.setMidia(midia);
		msg.setMidiaMimetype(midiaMimetype);
		em.persist(msg);
		em.flush();
	}

	public MensagemWhatsApp buscarPorMessageId(String messageId) {
		if (messageId == null || messageId.trim().isEmpty()) return null;
		try {
			return em.createQuery("SELECT m FROM MensagemWhatsApp m WHERE m.messageId = :mid", MensagemWhatsApp.class)
				.setParameter("mid", messageId)
				.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public void salvarMidia(Long msgId, byte[] midia, String midiaMimetype) {
		if (msgId == null || midia == null) return;
		MensagemWhatsApp msg = em.find(MensagemWhatsApp.class, msgId);
		if (msg == null) return;
		msg.setMidia(midia);
		msg.setMidiaMimetype(midiaMimetype);
		em.merge(msg);
		em.flush();
	}

	public void salvarCobrancaEnviada(Aluno aluno, String telefone, String texto) {
		if (aluno == null || telefone == null || texto == null) return;
		String telNorm = telefone.replaceAll("[^0-9]", "");
		if (telNorm.isEmpty()) return;
		long ts = System.currentTimeMillis() / 1000L;
		MensagemWhatsApp msg = new MensagemWhatsApp(aluno, telNorm, null, texto, true, ts);
		em.persist(msg);
		em.flush();
	}

	public void salvarNovas(Aluno aluno, String telefone, List<MensagemWA> novas) {
		if (aluno == null || telefone == null || novas == null || novas.isEmpty()) return;
		String telNorm = telefone.replaceAll("[^0-9]", "");

		for (MensagemWA wa : novas) {
			// Skip if messageId already exists
			if (wa.getMessageId() != null && !wa.getMessageId().isEmpty()) {
				Long count = em.createQuery(
					"SELECT COUNT(m) FROM MensagemWhatsApp m WHERE m.aluno.id = :alunoId AND m.messageId = :mid",
					Long.class)
					.setParameter("alunoId", aluno.getId())
					.setParameter("mid", wa.getMessageId())
					.getSingleResult();
				if (count > 0) continue;
			}

			MensagemWhatsApp msg = new MensagemWhatsApp(
				aluno, telNorm, wa.getMessageId(), wa.getText(), wa.isFromMe(), wa.getTimestamp());

			if (wa.isMidia() && wa.getMessageId() != null && !wa.getMessageId().isEmpty()) {
				try {
					EvolutionApiService.MidiaResult midia = evolutionApiService.buscarMidia(
						wa.getMessageId(), telNorm, wa.isFromMe());
					if (midia != null && midia.getBytes() != null) {
						msg.setMidia(midia.getBytes());
						msg.setMidiaMimetype(midia.getMimetype());
					}
				} catch (Exception e) {
					System.err.println("salvarNovas: falha ao baixar mídia " + wa.getMessageId() + ": " + e.getMessage());
				}
			}

			em.persist(msg);
		}
		em.flush();
	}
}

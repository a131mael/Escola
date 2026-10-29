package org.escola.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.escola.model.Aluno;
import org.escola.model.EventoArrecadacao;
import org.escola.model.EventoParcela;
import org.escola.model.EventoParticipante;
import org.escola.util.Service;

@Stateless
public class EventoParticipanteService extends Service {

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	@Inject
	private AlunoService alunoService;

	// Garante que todo aluno da turma tenha um EventoParticipante (com a 1a
	// parcela) pro evento informado. Idempotente -- chamado toda vez que a
	// aba da turma e aberta, cobre aluno matriculado depois do evento criado.
	public List<EventoParticipante> findOuCriarParticipantes(EventoArrecadacao evento, Long idTurma) {
		List<Aluno> alunosDaTurma = alunoService.findAlunoTurmaBytTurma(idTurma);

		for (Aluno aluno : alunosDaTurma) {
			if (findByEventoEAluno(evento.getId(), aluno.getId()) == null) {
				criarParticipante(evento, aluno);
			}
		}

		List<EventoParticipante> participantes = findByEventoETurma(evento.getId(), idTurma);
		Collections.sort(participantes, new Comparator<EventoParticipante>() {
			public int compare(EventoParticipante a, EventoParticipante b) {
				String nomeA = a.getAluno().getNomeAluno() == null ? "" : a.getAluno().getNomeAluno();
				String nomeB = b.getAluno().getNomeAluno() == null ? "" : b.getAluno().getNomeAluno();
				return nomeA.compareToIgnoreCase(nomeB);
			}
		});
		return participantes;
	}

	private EventoParticipante criarParticipante(EventoArrecadacao evento, Aluno aluno) {
		EventoParticipante participante = new EventoParticipante();
		participante.setEvento(evento);
		participante.setAluno(aluno);
		participante.setNumeroParcelas(1);
		em.persist(participante);

		EventoParcela parcela = new EventoParcela();
		parcela.setParticipante(participante);
		parcela.setNumero(1);
		parcela.setValor(evento.getValorUnitario());
		em.persist(parcela);

		return participante;
	}

	public EventoParticipante findByEventoEAluno(Long idEvento, Long idAluno) {
		try {
			Query query = em.createQuery(
					"SELECT p FROM EventoParticipante p WHERE p.evento.id = :idEvento AND p.aluno.id = :idAluno");
			query.setParameter("idEvento", idEvento);
			query.setParameter("idAluno", idAluno);
			return (EventoParticipante) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EventoParticipante> findByEventoETurma(Long idEvento, Long idTurma) {
		Query query = em.createQuery("SELECT p FROM EventoParticipante p, AlunoTurma at "
				+ "WHERE p.evento.id = :idEvento AND at.aluno.id = p.aluno.id AND at.turma.id = :idTurma");
		query.setParameter("idEvento", idEvento);
		query.setParameter("idTurma", idTurma);
		try {
			return query.getResultList();
		} catch (NoResultException nre) {
			return new ArrayList<>();
		}
	}

	public void confirmar(EventoParticipante participante, Integer quantidade) {
		participante.setConfirmado(true);
		if (participante.getEvento().isPorQuantidade()) {
			participante.setQuantidadeConfirmada(quantidade == null || quantidade < 1 ? 1 : quantidade);
			recalcularParcelasNaoPagas(participante);
		}
		em.merge(participante);
	}

	public void atualizarNumeroParcelas(EventoParticipante participante, int novoNumero) {
		if (novoNumero < 1) {
			novoNumero = 1;
		}
		List<EventoParcela> parcelas = participante.getParcelas();

		int pagas = 0;
		for (EventoParcela p : parcelas) {
			if (p.isPago()) {
				pagas++;
			}
		}
		if (novoNumero < pagas) {
			// nao da pra ter menos parcelas do que ja foi pago
			novoNumero = pagas;
		}

		// remove parcelas nao pagas em excesso (do final pra comecar)
		for (int i = parcelas.size() - 1; i >= 0 && parcelas.size() > novoNumero; i--) {
			EventoParcela p = parcelas.get(i);
			if (!p.isPago()) {
				parcelas.remove(i);
				em.remove(em.contains(p) ? p : em.merge(p));
			}
		}
		// adiciona parcelas novas (nao pagas) se faltar
		int proximoNumero = parcelas.size() + 1;
		while (parcelas.size() < novoNumero) {
			EventoParcela nova = new EventoParcela();
			nova.setParticipante(participante);
			nova.setNumero(proximoNumero++);
			em.persist(nova);
			parcelas.add(nova);
		}

		participante.setNumeroParcelas(novoNumero);
		em.merge(participante);
		recalcularParcelasNaoPagas(participante);
	}

	// Redivide o valor restante (total do participante menos o que ja foi
	// pago) igualmente entre as parcelas ainda nao pagas.
	private void recalcularParcelasNaoPagas(EventoParticipante participante) {
		double quantidade = 1;
		if (participante.getEvento().isPorQuantidade() && participante.getQuantidadeConfirmada() != null) {
			quantidade = participante.getQuantidadeConfirmada();
		}
		double total = participante.getEvento().getValorUnitario() * quantidade;

		double jaPago = 0;
		int naoPagas = 0;
		for (EventoParcela p : participante.getParcelas()) {
			if (p.isPago()) {
				jaPago += p.getValorPago();
			} else {
				naoPagas++;
			}
		}

		if (naoPagas == 0) {
			return;
		}

		double restante = Math.max(total - jaPago, 0);
		double valorPorParcela = restante / naoPagas;
		for (EventoParcela p : participante.getParcelas()) {
			if (!p.isPago()) {
				p.setValor(valorPorParcela);
				em.merge(p);
			}
		}
	}

}

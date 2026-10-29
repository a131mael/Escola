package org.escola.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.escola.model.EventoArrecadacao;
import org.escola.model.EventoParcela;
import org.escola.model.EventoParticipante;
import org.escola.model.EventoResumoTurma;
import org.escola.model.Turma;
import org.escola.util.Service;

@Stateless
public class EventoParcelaService extends Service {

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	@Inject
	private EventoParticipanteService participanteService;

	@Inject
	private TurmaService turmaService;

	public void baixar(EventoParcela parcela) {
		parcela.setValorPago(parcela.getValor());
		parcela.setDataPagamento(new Date());
		em.merge(parcela);
	}

	public void estornar(EventoParcela parcela) {
		parcela.setValorPago(null);
		parcela.setDataPagamento(null);
		em.merge(parcela);
	}

	public List<EventoResumoTurma> resumoPorEvento(EventoArrecadacao evento) {
		List<EventoResumoTurma> resumos = new ArrayList<>();

		List<Turma> turmas;
		if (evento.isTodasTurmas()) {
			turmas = turmaService.findAll();
		} else {
			turmas = new ArrayList<>();
			turmas.add(evento.getTurma());
		}

		for (Turma turma : turmas) {
			List<EventoParticipante> participantes = participanteService.findByEventoETurma(evento.getId(),
					turma.getId());
			if (participantes.isEmpty()) {
				continue;
			}

			EventoResumoTurma resumo = new EventoResumoTurma();
			resumo.setNomeTurma(turma.getNome());
			resumo.setTotalParticipantes(participantes.size());

			int confirmados = 0;
			int pagos = 0;
			double esperado = 0;
			double arrecadado = 0;

			for (EventoParticipante participante : participantes) {
				if (Boolean.TRUE.equals(participante.getConfirmado())) {
					confirmados++;
				}
				boolean todasPagas = !participante.getParcelas().isEmpty();
				for (EventoParcela parcela : participante.getParcelas()) {
					esperado += parcela.getValor();
					if (parcela.isPago()) {
						arrecadado += parcela.getValorPago();
					} else {
						todasPagas = false;
					}
				}
				if (todasPagas) {
					pagos++;
				}
			}

			resumo.setConfirmados(confirmados);
			resumo.setPagos(pagos);
			resumo.setTotalEsperado(esperado);
			resumo.setTotalArrecadado(arrecadado);
			resumos.add(resumo);
		}

		return resumos;
	}

	public EventoResumoTurma resumoGeral(List<EventoResumoTurma> resumosPorTurma) {
		EventoResumoTurma geral = new EventoResumoTurma();
		geral.setNomeTurma("Geral");
		for (EventoResumoTurma r : resumosPorTurma) {
			geral.setTotalParticipantes(geral.getTotalParticipantes() + r.getTotalParticipantes());
			geral.setConfirmados(geral.getConfirmados() + r.getConfirmados());
			geral.setPagos(geral.getPagos() + r.getPagos());
			geral.setTotalEsperado(geral.getTotalEsperado() + r.getTotalEsperado());
			geral.setTotalArrecadado(geral.getTotalArrecadado() + r.getTotalArrecadado());
		}
		return geral;
	}

}

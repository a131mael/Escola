package org.escola.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.escola.enums.Serie;
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
		em.flush();
	}

	public void estornar(EventoParcela parcela) {
		parcela.setValorPago(null);
		parcela.setDataPagamento(null);
		em.merge(parcela);
		em.flush();
	}

	public List<EventoResumoTurma> resumoPorEvento(EventoArrecadacao evento) {
		List<EventoResumoTurma> resumos = new ArrayList<>();

		List<Turma> turmas;
		if (evento.isTodasTurmas()) {
			turmas = turmaService.findAll();
			// Maternal/Jardim/Pre: so 1 linha por serie (ver EventoArrecadacaoDetalheController.carregar()).
			Map<Serie, Turma> representantePorSerieBaixa = new LinkedHashMap<>();
			List<Turma> semDuplicataBaixa = new ArrayList<>();
			for (Turma t : turmas) {
				if (t.getSerie().ordinal() <= Serie.PRE.ordinal()) {
					Turma existente = representantePorSerieBaixa.get(t.getSerie());
					if (existente == null || t.getId() < existente.getId()) {
						representantePorSerieBaixa.put(t.getSerie(), t);
					}
				} else {
					semDuplicataBaixa.add(t);
				}
			}
			semDuplicataBaixa.addAll(representantePorSerieBaixa.values());
			turmas = semDuplicataBaixa;
			turmas.sort(Comparator.comparingInt((Turma t) -> t.getSerie().ordinal())
					.thenComparing(Turma::getNome));
		} else {
			turmas = new ArrayList<>();
			turmas.add(evento.getTurma());
		}

		for (Turma turma : turmas) {
			List<EventoParticipante> participantes = participanteService.findByEventoETurma(evento.getId(), turma);
			if (participantes.isEmpty()) {
				continue;
			}

			EventoResumoTurma resumo = new EventoResumoTurma();
			resumo.setNomeTurma(turma.getSerie().ordinal() <= Serie.PRE.ordinal() ? turma.getSerie().getName()
					: turma.getNome() + " (" + turma.getPeriodo().getName() + ")");
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

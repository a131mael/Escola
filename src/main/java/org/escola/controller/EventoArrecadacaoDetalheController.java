package org.escola.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.model.EventoArrecadacao;
import org.escola.model.EventoParcela;
import org.escola.model.EventoParticipante;
import org.escola.model.EventoResumoTurma;
import org.escola.model.Turma;
import org.escola.service.EventoArrecadacaoService;
import org.escola.service.EventoParcelaService;
import org.escola.service.EventoParticipanteService;
import org.escola.service.TurmaService;
import org.escola.util.PixBRCodeUtil;

@Named
@ViewScoped
public class EventoArrecadacaoDetalheController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EventoArrecadacaoService eventoArrecadacaoService;

	@Inject
	private EventoParticipanteService participanteService;

	@Inject
	private EventoParcelaService parcelaService;

	@Inject
	private TurmaService turmaService;

	private Long id;

	private EventoArrecadacao evento;

	private List<Turma> turmas;

	private Map<Long, List<EventoParticipante>> participantesPorTurma = new HashMap<>();

	private List<EventoResumoTurma> resumoPorTurma;

	private EventoResumoTurma resumoGeral;

	private String pixCopiaECola;

	public void carregar() {
		if (id == null) {
			return;
		}
		evento = eventoArrecadacaoService.findById(id);
		if (evento == null) {
			return;
		}

		if (evento.isTodasTurmas()) {
			turmas = turmaService.findAll();
		} else {
			turmas = new ArrayList<>();
			if (evento.getTurma() != null) {
				turmas.add(evento.getTurma());
			}
		}

		atualizarResumo();
	}

	public List<EventoParticipante> getParticipantes(Turma turma) {
		if (turma == null) {
			return new ArrayList<>();
		}
		List<EventoParticipante> participantes = participantesPorTurma.get(turma.getId());
		if (participantes == null) {
			participantes = participanteService.findOuCriarParticipantes(evento, turma.getId());
			participantesPorTurma.put(turma.getId(), participantes);
		}
		return participantes;
	}

	public void confirmar(EventoParticipante participante) {
		participanteService.confirmar(participante, participante.getQuantidadeConfirmada());
		atualizarResumo();
	}

	public void atualizarNumeroParcelas(EventoParticipante participante) {
		participanteService.atualizarNumeroParcelas(participante, participante.getNumeroParcelas());
		atualizarResumo();
	}

	public void baixarParcela(EventoParcela parcela) {
		parcelaService.baixar(parcela);
		atualizarResumo();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pagamento registrado"));
	}

	public void estornarParcela(EventoParcela parcela) {
		parcelaService.estornar(parcela);
		atualizarResumo();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Baixa desfeita"));
	}

	private void atualizarResumo() {
		resumoPorTurma = parcelaService.resumoPorEvento(evento);
		resumoGeral = parcelaService.resumoGeral(resumoPorTurma);
	}

	public void gerarPix() {
		pixCopiaECola = PixBRCodeUtil.gerarBRCode(evento.getChavePix(), evento.getNomeRecebedor(),
				evento.getCidadeRecebedor(), evento.getValorUnitario());
	}

	public boolean isPago(EventoParcela parcela) {
		return parcela.isPago();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventoArrecadacao getEvento() {
		return evento;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public List<EventoResumoTurma> getResumoPorTurma() {
		return resumoPorTurma;
	}

	public EventoResumoTurma getResumoGeral() {
		return resumoGeral;
	}

	public List<EventoResumoTurma> getResumoGeralLista() {
		List<EventoResumoTurma> lista = new ArrayList<>();
		if (resumoGeral != null) {
			lista.add(resumoGeral);
		}
		return lista;
	}

	public String getPixCopiaECola() {
		return pixCopiaECola;
	}

}

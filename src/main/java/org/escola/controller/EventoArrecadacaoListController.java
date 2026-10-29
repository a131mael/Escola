package org.escola.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.model.EventoArrecadacao;
import org.escola.model.Turma;
import org.escola.service.EventoArrecadacaoService;
import org.escola.service.TurmaService;

@Named
@ViewScoped
public class EventoArrecadacaoListController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EventoArrecadacaoService eventoArrecadacaoService;

	@Inject
	private TurmaService turmaService;

	private List<EventoArrecadacao> listaEventos;

	private List<Turma> turmasDisponiveis;

	private EventoArrecadacao novoEvento;

	private Long turmaIdSelecionada;

	@PostConstruct
	public void init() {
		listaEventos = eventoArrecadacaoService.findAll();
		turmasDisponiveis = turmaService.findAll();
		prepararNovoEvento();
	}

	public void prepararNovoEvento() {
		novoEvento = new EventoArrecadacao();
		novoEvento.setTodasTurmas(true);
		novoEvento.setChavePix("7d2f5c98-708b-4682-bf22-69ef6e1a013c");
		novoEvento.setNomeRecebedor("Colegio Adonai");
		novoEvento.setCidadeRecebedor("Palhoca");
		turmaIdSelecionada = null;
	}

	public void salvarNovoEvento() {
		if (!novoEvento.isTodasTurmas() && turmaIdSelecionada != null) {
			novoEvento.setTurma(turmaService.findById(turmaIdSelecionada));
		} else {
			novoEvento.setTurma(null);
		}
		eventoArrecadacaoService.save(novoEvento);
		listaEventos = eventoArrecadacaoService.findAll();
		prepararNovoEvento();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Evento criado"));
	}

	public void remover(EventoArrecadacao evento) {
		eventoArrecadacaoService.remover(evento.getId());
		listaEventos = eventoArrecadacaoService.findAll();
	}

	public List<EventoArrecadacao> getListaEventos() {
		return listaEventos;
	}

	public List<Turma> getTurmasDisponiveis() {
		return turmasDisponiveis;
	}

	public EventoArrecadacao getNovoEvento() {
		return novoEvento;
	}

	public void setNovoEvento(EventoArrecadacao novoEvento) {
		this.novoEvento = novoEvento;
	}

	public Long getTurmaIdSelecionada() {
		return turmaIdSelecionada;
	}

	public void setTurmaIdSelecionada(Long turmaIdSelecionada) {
		this.turmaIdSelecionada = turmaIdSelecionada;
	}

}

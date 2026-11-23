package org.escola.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.enums.Serie;
import org.escola.model.EventoArrecadacao;
import org.escola.model.EventoParcela;
import org.escola.model.EventoParticipante;
import org.escola.model.EventoResumoTurma;
import org.escola.model.Turma;
import org.escola.service.AlunoService;
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

	@Inject
	private AlunoService alunoService;

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
			// Maternal/Jardim/Pre nao sao vinculados por AlunoTurma -- decide
			// pelo cadastro do aluno (so a serie) direto nesses casos.
			turmas.removeIf(t -> t.getSerie().ordinal() <= Serie.PRE.ordinal()
					? alunoService.findAlunoPorSerie(t.getSerie()).isEmpty()
					: t.getTotalAlunos() == 0);
			// Maternal/Jardim/Pre: so 1 aba por serie (nao distingue periodo
			// nem duplicatas de turma, tipo "Maternal" e "Maternal II").
			java.util.Map<Serie, Turma> representantePorSerieBaixa = new java.util.LinkedHashMap<>();
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
			if (evento.getTurma() != null) {
				turmas.add(evento.getTurma());
			}
		}

		atualizarResumo();
		gerarPix();
	}

	public List<EventoParticipante> getParticipantes(Turma turma) {
		if (turma == null) {
			return new ArrayList<>();
		}
		List<EventoParticipante> participantes = participantesPorTurma.get(turma.getId());
		if (participantes == null) {
			participantes = participanteService.findOuCriarParticipantes(evento, turma);
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

	public String getPixQrCodeBase64() {
		return PixBRCodeUtil.gerarQrCodeBase64(pixCopiaECola);
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

	// Maternal/Jardim/Pre: 1 aba por serie, sem mostrar periodo (ver carregar()).
	public String getTituloTurma(Turma turma) {
		Serie serie = turma.getSerie();
		if (serie.ordinal() <= Serie.PRE.ordinal()) {
			return serie.getName();
		}
		// Do 6o ano pra cima so existe 1 turma por serie, entao o periodo
		// (Manha/Tarde) e redundante no titulo da aba -- so atrapalha o layout.
		if (serie.ordinal() >= Serie.SEXTO_ANO.ordinal() && serie.ordinal() <= Serie.NONO_ANO.ordinal()) {
			return turma.getNome();
		}
		return turma.getNome() + " (" + turma.getPeriodo().getName() + ")";
	}

}

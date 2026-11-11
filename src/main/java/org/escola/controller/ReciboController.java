package org.escola.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.model.Aluno;
import org.escola.model.ContratoAluno;
import org.escola.service.AlunoService;
import org.escola.util.CurrencyWriter;
import org.escola.util.FileDownload;
import org.escola.util.ImpressoesUtils;
import org.escola.util.Util;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class ReciboController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AlunoService alunoService;

	@Inject
	private org.escola.service.ConfiguracaoService configuracaoService;

	private Aluno crianca;

	public Aluno getCrianca() {
		return crianca;
	}

	public void setCrianca(Aluno crianca) {
		this.crianca = crianca;
	}

	public List<Aluno> completeCrianca(String texto) {
		return alunoService.findByNomeContendo(texto);
	}

	public void onCriancaSelecionada(org.primefaces.event.SelectEvent event) {
		Aluno aluno = (Aluno) event.getObject();
		// a entidade que vem da sugestao do autocomplete nao tem os contratos
		// inicializados (lazy) -- busca de novo, ja com tudo carregado, igual
		// o findById ja faz em outras telas.
		aluno = alunoService.findById(aluno.getId());
		crianca = aluno;
		int anoAtual = configuracaoService.getConfiguracao().getAnoLetivo();
		ContratoAluno contrato = aluno.getContratoVigente(anoAtual);
		if (contrato == null) {
			// sem contrato ativo no ano atual -- usa o ultimo contrato (mais
			// recente) que a crianca tiver, mesmo que de ano anterior
			contrato = ultimoContrato(aluno);
		}
		if (contrato != null) {
			nomeResponsavel = contrato.getNomeResponsavel();
			cpf = contrato.getCpfResponsavel();
		}
	}

	private String nomeResponsavel;

	private String cpf;

	private String valor;

	private String item;

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	private ContratoAluno ultimoContrato(Aluno aluno) {
		ContratoAluno ultimo = null;
		if (aluno.getContratos() != null) {
			for (ContratoAluno c : aluno.getContratos()) {
				if (ultimo == null || (c.getDataCriacaoContrato() != null && ultimo.getDataCriacaoContrato() != null
						&& c.getDataCriacaoContrato().after(ultimo.getDataCriacaoContrato()))) {
					ultimo = c;
				}
			}
		}
		return ultimo;
	}

	private HashMap<String, String> montarRecibo(double valorNumerico) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("reciboresponsavel", nomeResponsavel != null ? nomeResponsavel : "");
		trocas.put("recibocpf", cpf != null ? cpf : "");
		trocas.put("recibovalor", "R$ " + Util.formatarDouble2Decimais(valorNumerico).replace(".", ","));
		trocas.put("recibovalorextenso", new CurrencyWriter().write(BigDecimal.valueOf(valorNumerico)));
		trocas.put("reciboitem", item != null ? item : "");
		trocas.put("recibodata", dataExtenso);

		if (crianca != null) {
			trocas.put("reciboaluno", crianca.getNomeAluno() != null ? crianca.getNomeAluno() : "");
			trocas.put("reciboserie", crianca.getSerie() != null ? crianca.getSerie().getName() : "");
			trocas.put("reciboperiodo", crianca.getPeriodo() != null ? crianca.getPeriodo().getName() : "");
		} else {
			trocas.put("reciboaluno", "");
			trocas.put("reciboserie", "");
			trocas.put("reciboperiodo", "");
		}

		return trocas;
	}

	public StreamedContent gerarRecibo() throws IOException {
		if (nomeResponsavel == null || nomeResponsavel.trim().isEmpty() || valor == null
				|| valor.trim().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new javax.faces.application.FacesMessage(javax.faces.application.FacesMessage.SEVERITY_ERROR,
							"Preencha ao menos o nome e o valor", null));
			return null;
		}

		double valorNumerico;
		try {
			valorNumerico = Double.parseDouble(valor.replace(".", "").replace(",", "."));
		} catch (NumberFormatException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new javax.faces.application.FacesMessage(javax.faces.application.FacesMessage.SEVERITY_ERROR,
							"Valor inválido", null));
			return null;
		}

		String nomeArquivo = "recibo" + System.currentTimeMillis();
		ImpressoesUtils.imprimirInformacoesAluno("modeloRecibo.docx", montarRecibo(valorNumerico), nomeArquivo);
		nomeArquivo += ".doc";

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + File.separator
				+ nomeArquivo;
		InputStream stream = new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, "recibo.doc");
	}

}

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.escola.controller.recado;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.auth.AuthController;
import org.escola.enums.BimestreEnum;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.enums.TipoDestinatario;
import org.escola.enums.TipoMembro;
import org.escola.model.Aluno;
import org.escola.model.HistoricoAluno;
import org.escola.model.Member;
import org.escola.model.Recado;
import org.escola.model.RecadoDestinatario;
import org.escola.service.RecadoService;
import org.escola.util.FileDownload;
import org.escola.util.ImpressoesUtils;
import org.escola.util.Util;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Named
@ViewScoped
public class RecadoController  extends AuthController implements Serializable {

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Recado recado;
	
	@Produces
	@Named
	private List<Recado> recados;
	
	@Inject
	private RecadoService recadoService;

	@Named
	private BimestreEnum bimestreSel;
	@Named
	private DisciplinaEnum disciplinaSel;

	private LazyDataModel<Recado> lazyListDataModel;
	
	private UploadedFile file;
	byte[] bts = null;
	
	public LazyDataModel<Recado> getLazyDataModel() {
		if (lazyListDataModel == null) {

			lazyListDataModel = new LazyDataModel<Recado>() {

				@Override
				public Recado getRowData(String rowKey) {
					return recadoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Recado al) {
					return al.getId();
				}

				@Override
				public List<Recado> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					filtros.put("removido", false);
					if (filtros.containsKey("periodo")) {
						filtros.put("periodo", filtros.get("periodo").equals("MANHA") ? PerioddoEnum.MANHA
								: filtros.get("periodo").equals("TARDE") ? PerioddoEnum.TARDE : PerioddoEnum.INTEGRAL);
					}

					if (filtros.containsKey("serie")) {
						if (filtros.get("serie").equals(Serie.JARDIM_I.toString())) {
							filtros.put("serie", Serie.JARDIM_I);
						} else if (filtros.get("serie").equals(Serie.JARDIM_II.toString())) {
							filtros.put("serie", Serie.JARDIM_II);
						} else if (filtros.get("serie").equals(Serie.MATERNAL.toString())) {
							filtros.put("serie", Serie.MATERNAL);
						} else if (filtros.get("serie").equals(Serie.PRE.toString())) {
							filtros.put("serie", Serie.PRE);
						} else if (filtros.get("serie").equals(Serie.PRIMEIRO_ANO.toString())) {
							filtros.put("serie", Serie.PRIMEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.SEGUNDO_ANO.toString())) {
							filtros.put("serie", Serie.SEGUNDO_ANO);
						} else if (filtros.get("serie").equals(Serie.TERCEIRO_ANO.toString())) {
							filtros.put("serie", Serie.TERCEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUARTO_ANO.toString())) {
							filtros.put("serie", Serie.QUARTO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUINTO_ANO.toString())) {
							filtros.put("serie", Serie.QUINTO_ANO);
						}

					}
					
					filtros.put("removido", false);

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Recado> ol = recadoService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModel.setRowCount((int) recadoService.count(filtros));
						return ol;
					}

					this.setRowCount((int) recadoService.count(filtros));
					return null;

				}
			};
			lazyListDataModel.setRowCount((int) recadoService.count(null));

		}

		return lazyListDataModel;

	}

		
	@PostConstruct
	private void init() {
		if (getRecado() == null) {
			Object obj = Util.getAtributoSessao("recado");
			if (obj != null) {
				setRecado((Recado) obj);
			} else {
				setRecado(new Recado());
			}
		}
	}
	
	public List<RecadoDestinatario> getMemberRespondeu(){
		return recadoService.getMemberRespondeu(recado);
	}
	
	public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
	
	public int getPercentualProfessoresLeram(){
		System.out.println("Total de professores resp : " +recadoService.getMemberRespondeu(recado,TipoMembro.PROFESSOR).size());
		System.out.println("Total de professores : " + recadoService.getTotalProfessores());
		
		System.out.println("%% : " + (recadoService.getMemberRespondeu(recado,TipoMembro.PROFESSOR).size()*100 / recadoService.getTotalProfessores()));
		
		return (recadoService.getMemberRespondeu(recado,TipoMembro.PROFESSOR).size()*100 / recadoService.getTotalProfessores());
	}

	public int getPercentualResponsaveisLeram(){
		return ((recadoService.getMemberRespondeu(recado,TipoMembro.ALUNO)).size()*100 /  (recadoService.getTotalResponsaveis(recado)));
	}

	
	public boolean estaEmUmaTurma(long idAluno){
		//boolean estaNaTurma =alunoService.estaEmUmaTUrma(idAluno);
	//	return estaNaTurma;
		return true;
	}

	private Float maior(Float float1, Float float2) {
		if(Float.isNaN(float1)){
			return float2;
		}
		if(Float.isNaN(float2)){
			return float1;
		}
		
		return float1 > float2 ? float1 : float2;
	}

	private String mostraNotas(Float nota) {
		if (nota == null || nota == 0 || Float.isNaN(nota)) {
			return "";
		} else {
			DecimalFormat df = new DecimalFormat("0.##");
			String dx = df.format(nota);
			
			dx = dx.replace(",", ".");
			
			return String.valueOf((Math.round(Float.parseFloat(dx) / 0.5) * 0.5)) ;
		}
	}

	public boolean isRecadoSelecionado() {
		if(recado == null){
			return false;
		}
		return recado.getId() != null ? true : false;
	}
	
	private Float media(Float... notas) {
		int qtade = 0;
		float sum = 0;
		for (Float f : notas) {
			sum += f;
			qtade++;
		}
		return qtade > 0 ? sum / qtade : 0;
	}
	
	/*public float getNota(DisciplinaEnum disciplina, BimestreEnum  bimestre){
		return alunoService.getNota(aluno.getId(), disciplina, bimestre, false);
	}
*/
		public boolean renderDisciplina(int ordinal) {
		if (getDisciplinaSel() == null) {
			return true;
		}

		return ordinal == getDisciplinaSel().ordinal();
	}
	
	
	public HashMap<String, String> montarAtestadoFrequencia(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonaiturma", aluno.getSerie().getName());
		trocas.put("adonaiperiodo", aluno.getPeriodo().getName());
		trocas.put("adonaidata", dataExtenso);

		return trocas;
	}

	public HashMap<String, String> montarAtestadoMatricula(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonaiturma", aluno.getSerie().getName());
		trocas.put("adonaiperiodo", aluno.getPeriodo().getName());
		trocas.put("adonaidata", dataExtenso);

		return trocas;
	}

	public HashMap<String, String> montarAtestadoNegativoDebito(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonaiturma", aluno.getSerie().getName());
		trocas.put("adonaiperiodo", aluno.getPeriodo().getName());
		trocas.put("adonaidata", dataExtenso);
		trocas.put("adonaicpfresponsavel", aluno.getContratoVigente().getCpfResponsavel());
		trocas.put("adonainomeresponsavel", aluno.getContratoVigente().getNomeResponsavel());

		return trocas;
	}

	public HashMap<String, String> montarAtestadoVaga(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonaiturma", aluno.getSerie().getName());
		trocas.put("adonaiperiodo", aluno.getPeriodo().getName());
		trocas.put("adonaidata", dataExtenso);

		return trocas;
	}
	
	private HistoricoAluno getHistorico(List<HistoricoAluno> historicos, Serie serie){
		for(HistoricoAluno historico : historicos){
			if(historico.getSerie() != null && historico.getSerie().equals(serie)){
				return historico;
			}
		}
		return null;
	}
	
	
	public HashMap<String, String> montarContrato(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());
		Calendar dataLim = Calendar.getInstance();
		dataLim.add(Calendar.MONTH, 1);
		String dataLimiteExtenso = formatador.format(dataLim.getTime());

		DateFormat formatadorAno = DateFormat.getDateInstance(DateFormat.YEAR_FIELD, new Locale("pt", "BR"));
		String ano = formatadorAno.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonaiurma", aluno.getSerie().getName());
		trocas.put("adonaieriodo", aluno.getPeriodo().getName());
		trocas.put("adonaidata", dataExtenso);
		trocas.put("adonaiano", ano);
		trocas.put("adonaidatalimtevaga", dataLimiteExtenso);

		return trocas;
	}

	public void onRowSelect(SelectEvent event) {
		Recado al = (Recado) event.getObject();
		FacesMessage msg = new FacesMessage("Recado Selecionado");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselect(UnselectEvent event) {
		Aluno al = (Aluno) event.getObject();
		FacesMessage msg = new FacesMessage("Recado Descelecionado");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/*public boolean isAlunoSelecionado() {
		if(aluno == null){
			return false;
		}
		return aluno.getId() != null ? true : false;
	}*/

	public StreamedContent imprimirContratoAdonai(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if(aluno != null && aluno.getId() != null){
			nomeArquivo =aluno.getId() + "b";
		//	ImpressoesUtils.imprimirInformacoesAluno(aluno, "mb1.docx", montarBoletim(aluno), nomeArquivo);
			nomeArquivo += ".doc";
		}else{
			nomeArquivo ="mb1.docx";
		}
		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+nomeArquivo;
		
		InputStream stream =  new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	/*public StreamedContent imprimirContratoAdonai() throws IOException {
		return imprimirContratoAdonai(aluno);
	}*/

	public StreamedContent imprimirAtestadoFrequencia(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if(aluno != null && aluno.getId() != null){
			nomeArquivo = aluno.getId() + "c";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloAtestadoFrequencia2017.docx",
					montarAtestadoFrequencia(aluno), nomeArquivo);			
			nomeArquivo += ".doc";
		}else{
			nomeArquivo = "modeloAtestadoFrequencia2017.docx";
		}
		
		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+ nomeArquivo;
		InputStream stream =  new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	/*public StreamedContent imprimirAtestadoFrequencia() throws IOException {
		return imprimirAtestadoFrequencia(aluno);
	}*/

	public StreamedContent imprimirAtestadoMatricula(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if(aluno != null && aluno.getId() != null){
			nomeArquivo =aluno.getId() + "d";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloAtestadoMatricula2017.docx",
					montarAtestadoMatricula(aluno), nomeArquivo);
			
			nomeArquivo += ".doc";
		}else{
			nomeArquivo ="modeloAtestadoMatricula2017.docx";
		}
		
		
		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+ nomeArquivo;
		InputStream stream =  new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	/*public StreamedContent imprimirAtestadoMatricula() throws IOException {
		return imprimirAtestadoMatricula(aluno);
	}*/

	public StreamedContent imprimirNegativoDebito(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if(aluno != null && aluno.getId() != null){
			nomeArquivo =aluno.getId() + "f";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloNegativoDebito2017.docx",
					montarAtestadoNegativoDebito(aluno), nomeArquivo);
			
			nomeArquivo +=".doc";
		}else{
			nomeArquivo = "modeloNegativoDebito2017.docx";
		}

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+nomeArquivo;
		InputStream stream =  new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	/*public StreamedContent imprimirNegativoDebito() throws IOException {
		return imprimirNegativoDebito(aluno);
	}
*/
	public StreamedContent imprimirContrato(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if(aluno != null && aluno.getId() != null){
			 nomeArquivo =aluno.getId() + "g"; 
			 ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloContrato2017.docx", montarContrato(aluno),nomeArquivo);
			 nomeArquivo+= ".doc";
		}else{
			nomeArquivo = "modeloContrato2017.docx";
		}
		
		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+nomeArquivo;
		InputStream stream =  new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	/*public StreamedContent imprimirContrato() throws IOException {
		return imprimirContrato(aluno);
	}*/

	public StreamedContent imprimirAtestadoVaga(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if(aluno != null && aluno.getId() != null){
			nomeArquivo =aluno.getId() + "h";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloAtestadoVaga2017.docx", montarAtestadoVaga(aluno),nomeArquivo);
			nomeArquivo +=".doc";
		}else{
			nomeArquivo = "modeloAtestadoVaga2017.docx";
		}
		
		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+nomeArquivo;
		InputStream stream =  new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}
/*
	public StreamedContent imprimirAtestadoVaga() throws IOException {
		return imprimirAtestadoVaga(aluno);
	}
	*/
	public StreamedContent imprimirHistorico(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if(aluno != null && aluno.getId() != null){
			nomeArquivo =aluno.getId() + "k";
		//	ImpressoesUtils.imprimirInformacoesAluno(aluno, "historicoEscolar2017.docx", montarHistorico(aluno),nomeArquivo);
			nomeArquivo +=".doc";
		}else{
			nomeArquivo = "historicoEscolar2017.docx";
		}
		
		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+nomeArquivo;
		InputStream stream =  new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	/*public StreamedContent imprimirHistorico() throws IOException {
		return imprimirHistorico(aluno);
	}*/

	public List<Recado> getAlunos() {

		return recadoService.findAll();
	}

	public String salvar() {
		Recado rec = getRecado();
		if(file != null){
			rec.setFilePergunta(bts);
		}
		rec.setAprovado(true);
		rec.setIdQuemPostou(getLoggedUser().getId());
		recadoService.save(rec);
		
		Util.removeAtributoSessao("recado");
		return "index";
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		file = event.getFile();
		bts = file.getContents();
        //application code
    }
	
	public String voltar() {
		return "index";
	}

	public String editar(Long idprof) {
		setRecado(recadoService.findById(idprof));
		Util.addAtributoSessao("recado", getRecado());
		return "cadastrar";
	}
	
	public String editarHistorico(HistoricoAluno historicoAluno) {
		//this.historicoAluno = historicoAluno;
		return "";
	}
	
	public String visualizar(Recado aluno) {
		//popularAlunoAvaliacao(aluno);
		
		setRecado(recadoService.findById(aluno.getId()));
		Util.addAtributoSessao("recado", aluno);
		
		return "cadastrar";
	}

	public String editar() {
		return editar(getRecado().getId());
	}

	public String remover(Long idTurma) {
		recadoService.remover(idTurma);
		return "index";
	}
	
	public String restaurar(Long idTurma) {
	//	recadoService.restaurar(idTurma);
		return "index";
	}

	public String adicionarNovo() {
		Util.removeAtributoSessao("recado");
		return "cadastrar";
	}
	
	/*public void adicionarHistorico(HistoricoAluno historico){
		historico.setAluno(aluno);
		alunoService.saveHistorico(historico);	
	}*/
	
	public String cadastrarNovo() {
		return "exibirAluno";
	}

	public Recado getRecado() {
		return recado;
	}

	public void setAluno(Recado recado) {
		this.setRecado(recado);
	}

	
	public DisciplinaEnum getDisciplinaSel() {
		return disciplinaSel;
	}

	public void setDisciplinaSel(DisciplinaEnum disciplinaSel) {
		this.disciplinaSel = disciplinaSel;
	}

	public BimestreEnum getBimestreSel() {
		return bimestreSel;
	}

	public void setBimestreSel(BimestreEnum bimestreSel) {
		this.bimestreSel = bimestreSel;
	}

	public BimestreEnum getPrimeiroBimestre(){
		return BimestreEnum.PRIMEIRO_BIMESTRE;
	}
	
	public BimestreEnum getSegundoBimestre(){
		return BimestreEnum.SEGUNDO_BIMESTRE;
	}
	
	public BimestreEnum getTerceiroBimestre(){
		return BimestreEnum.TERCEIRO_BIMESTRE;
	}
	
	public BimestreEnum getQuartoBimestre(){
		return BimestreEnum.QUARTO_BIMESTRE;
	}
	
	public DisciplinaEnum getPortugues(){
		return DisciplinaEnum.PORTUGUES;
	}
	
	public DisciplinaEnum getMatematica(){
		return DisciplinaEnum.MATEMATICA;
	}
	
	public DisciplinaEnum getHistoria(){
		return DisciplinaEnum.HISTORIA;
	}
	
	public DisciplinaEnum getIngles(){
		return DisciplinaEnum.INGLES;
	}
	
	public DisciplinaEnum getEDFisica(){
		return DisciplinaEnum.EDUCACAO_FISICA;
	}
	
	public DisciplinaEnum getGeografia(){
		return DisciplinaEnum.GEOGRAFIA;
	}
	
	public DisciplinaEnum getCiencias(){
		return DisciplinaEnum.CIENCIAS;
	}
	
	public DisciplinaEnum getFormacaoCrista(){
		return DisciplinaEnum.FORMACAO_CRISTA;
	}
	
	public DisciplinaEnum getArtes(){
		return DisciplinaEnum.ARTES;
	}


	public void setRecado(Recado recado) {
		this.recado = recado;
	}


	public UploadedFile getFile() {
		return file;
	}


	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
}

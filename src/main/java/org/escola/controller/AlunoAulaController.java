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
package org.escola.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.auth.AuthController;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.Serie;
import org.escola.enums.TipoMembro;
import org.escola.model.Aluno;
import org.escola.model.AlunoAula;
import org.escola.model.Aula;
import org.escola.model.Member;
import org.escola.model.Turma;
import org.escola.service.AlunoService;
import org.escola.service.AulaService;
import org.escola.service.ConfiguracaoService;
import org.escola.service.TurmaService;
import org.escola.util.Util;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;

@Named
@ViewScoped
public class AlunoAulaController extends AuthController implements Serializable {

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private AlunoAula alunoAula;

	@Inject
	private AulaService aulaService;

	@Inject
	private AlunoService alunoService;

	@Inject
	private ConfiguracaoService configuracaoService;

	@Inject
	private TurmaService turmaService;

	private LazyDataModel<AlunoAula> lazyListDataModelAlunoAula;
	
	private LazyDataModel<Aluno> lazyListDataModelChamada;

	private Boolean mostrarListagem = false;

	private DashboardModel model;

	private DashboardModel model2;

	private TreeNode root;

	private String selectedNode;

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@PostConstruct
	private void init() {
		if (alunoAula == null) {
			Object obj = Util.getAtributoSessao("alunoAula");
			if (obj != null) {
				alunoAula = (AlunoAula) obj;
			}
		}
		// MODEL 1
		setModel(new DefaultDashboardModel());
		DashboardColumn column1 = new DefaultDashboardColumn();
		DashboardColumn column2 = new DefaultDashboardColumn();
		DashboardColumn column3 = new DefaultDashboardColumn();
		DashboardColumn column4 = new DefaultDashboardColumn();
		DashboardColumn column5 = new DefaultDashboardColumn();

		column1.addWidget("portugues");
		column2.addWidget("matematica");
		column1.addWidget("ingles");
		column4.addWidget("edfisica");
		column2.addWidget("formacao");

		if (getLoggedUser().getInfantil() != null && getLoggedUser().getInfantil()) {
			if (alunoService.findById(Long.valueOf(getLoggedUser().getIdCrianca1())).getSerie()
					.equals(Serie.JARDIM_I)) {
				column1.addWidget("culinaria");
				column3.addWidget("livro");
				column3.addWidget("brincadeiras");
				column3.addWidget("historiaemcena");
				
			} else {
				column1.addWidget("letrar");
				column2.addWidget("numerar");
				column3.addWidget("investigar");
				column4.addWidget("desenhar");
				column3.addWidget("livro");
				column3.addWidget("brincadeiras");
			}

		} else {
			column3.addWidget("historia");
			column4.addWidget("geografia");
			column1.addWidget("ciencias");
			column3.addWidget("artes");
			column2.addWidget("filosofia");

		}

		getModel().addColumn(column1);
		getModel().addColumn(column2);
		getModel().addColumn(column3);
		getModel().addColumn(column4);
		getModel().addColumn(column5);

		// MODEL 2
		setModel2(new DefaultDashboardModel());
		DashboardColumn mes1 = new DefaultDashboardColumn();
		DashboardColumn mes2 = new DefaultDashboardColumn();
		DashboardColumn mes3 = new DefaultDashboardColumn();
		DashboardColumn mes4 = new DefaultDashboardColumn();
		DashboardColumn mes5 = new DefaultDashboardColumn();

		mes1.addWidget("semana1");
		mes1.addWidget("semana2");
		mes1.addWidget("semana3");
		mes1.addWidget("semana4");

		mes2.addWidget("semana5");
		mes2.addWidget("semana6");
		mes2.addWidget("semana7");
		mes2.addWidget("semana8");

		mes3.addWidget("semana9");
		mes3.addWidget("semana10");
		mes3.addWidget("semana11");
		mes3.addWidget("semana12");
		mes3.addWidget("semana13");

		mes4.addWidget("semana14");
		mes4.addWidget("semana15");
		mes4.addWidget("semana16");
		mes4.addWidget("semana17");

		mes5.addWidget("semana18");
		mes5.addWidget("semana19");
		mes5.addWidget("semana20");
		mes5.addWidget("semana21");
		mes5.addWidget("semana22");

		getModel2().addColumn(mes1);
		getModel2().addColumn(mes2);
		getModel2().addColumn(mes3);
		getModel2().addColumn(mes4);
		getModel2().addColumn(mes5);
	}

	public void onRowSelect(SelectEvent event) {

		Util.addAtributoSessao("alunoAula", (AlunoAula) event.getObject());
		setAlunoAula((AlunoAula) event.getObject());
	}

	public void onRowUnselect(UnselectEvent event) {
		// setAlunoAula(null);
	}

	public boolean exibirColunaDisciplina() {
		int obj = (int) Util.getAtributoSessao("tipoListagem");
		if (obj == 0) {
			return false;
		} else {
			return true;
		}
	}

	public String entrarDisciplina(String disciplina) {
		DisciplinaEnum de = DisciplinaEnum.values()[Integer.valueOf(disciplina)];
		Util.addAtributoSessao("disciplina", de);
		Util.addAtributoSessao("tipoListagem", 0);
		Util.addAtributoSessao("alunoAula", aulaService.getPrimeiraAula(de, getLoggedUser().getIdCrianca1()));

		return "entrar";
	}
	
	

	public String entrarSemana(String disciplina) {
		int semana = Integer.valueOf(disciplina);

		Util.addAtributoSessao("semana", semana);
		Util.addAtributoSessao("tipoListagem", 1);
		Util.addAtributoSessao("alunoAula",
				aulaService.getPrimeiraAulaNaoAssistida(semana, getLoggedUser().getIdCrianca1()));
		return "entrar";
	}

	public DisciplinaEnum getDisciplina() {
		DisciplinaEnum disc = DisciplinaEnum.ARTES;
		Object obj = Util.getAtributoSessao("disciplina");
		if (obj != null) {
			disc = (DisciplinaEnum) obj;
		}
		return disc;

	}

	public int getSemana() {
		int semana = 0;
		Object obj = Util.getAtributoSessao("semana");
		if (obj != null) {
			semana = (int) obj;
		}
		return semana;
	}

	public Double getMedia(Aula aula) {
		return 0D;
	}

	public String salvar() {

		// aulaService.save(getAula(), null);

		return "index";
	}

	public String concluirAula(AlunoAula al) {
		if (al != null) {
			al.setAssistiu(true);
			aulaService.salvarAlunoAula(alunoAula);
		}

		return "";
	}

	public String concluirAula() {
		if (alunoAula == null) {
			alunoAula = (AlunoAula) Util.getAtributoSessao("alunoAula");
		}
		alunoAula.setAssistiu(true);
		aulaService.salvarAlunoAula(alunoAula);
		getRootForce();
		return "";
	}

	public String proximaAula() {
		if (alunoAula == null) {
			alunoAula = (AlunoAula) Util.getAtributoSessao("alunoAula");
		}
		if(alunoAula != null){
			alunoAula.setAssistiu(true);
		}
		aulaService.salvarAlunoAula(alunoAula);
		String dataAula = dateFormat.format(alunoAula.getAula().getDataAula());
		Long idAluno = alunoAula.getAluno().getId();
		Integer ordem = alunoAula.getAula().getOrdem();
		AlunoAula proxima = null;
		if(Util.getAtributoSessao("tipoListagem")!= null && (int)Util.getAtributoSessao("tipoListagem")==1){
			proxima = aulaService.getPrimeiraAulaNaoAssistida(getSemana(), String.valueOf(idAluno));
		}else{
			proxima = aulaService.getPrimeiraAulaNaoAssistida(getDisciplina(), String.valueOf(idAluno));
		}
		
		Util.addAtributoSessao("alunoAula", proxima);
		setAlunoAula(proxima);
		
		
		if(Util.getAtributoSessao("tipoListagem")!= null && (int)Util.getAtributoSessao("tipoListagem")==1){
			root = criaArvoreAulas(true, alunoAula.getAula().getDataAula());
		}
		return "";
	}

	public boolean podeEditar(Aula aula) {
		if (getLoggedUser().getTipoMembro().equals(TipoMembro.ADMIM)) {
			return true;
		} else {

			return true;
		}
	}

	public boolean exibirSerie() {
		if (getLoggedUser().getTipoMembro().equals(TipoMembro.ADMIM)) {
			return true;
		} else {
			if (getLoggedUser().getTipoMembro().equals(TipoMembro.PROFESSOR)) {
				Set<Serie> series = getSeries(getLoggedUser());
				if (series.size() > 1) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		}
	}

	public Set<Serie> getSeries(Member member) {
		List<Turma> turmas = null;
		if (member.getProfessor() != null) {
			turmas = turmaService.findAll(member.getProfessor().getId());
		} else {
			turmas = turmaService.findAll();
		}
		Set<Serie> series = new HashSet<>();
		for (Turma t : turmas) {
			series.add(t.getSerie());
		}
		return series;
	}

	public Set<Serie> getSeries() {

		return getSeries(getLoggedUser());
	}

	/*
	 * public String salvarAlunoAula(AlunoAula alunoAula){
	 * aulaService.save(alunoAula); return "index"; }
	 */

	public String voltar() {
		return "index";
	}

	/*
	 * public Set<Aula> getAvaliacoes(){
	 * 
	 * return aulaService.findAll(); return
	 * aulaService.findAll(getLoggedUser()); }
	 */

	/*
	 * public List<Aula> getAvaliacoes(DisciplinaEnum disciplina, Serie serie,
	 * BimestreEnum bimestre){
	 * 
	 * return aulaService.findAulaby(disciplina, bimestre, serie); }
	 */

	public String remover(Long idTurma) {
		aulaService.remover(idTurma);
		return "index";
	}

	public String adicionarNovo() {
		Util.removeAtributoSessao("alunoAula");
		return "cadastrar";
	}

	public AlunoAula getAlunoAula() {
		if (alunoAula == null) {
			alunoAula = aulaService.getPrimeiraAula(getDisciplina(), getLoggedUser().getIdCrianca1());
		}
		Util.addAtributoSessao("alunoAula", alunoAula);
		return alunoAula;
	}

	public Boolean getMostrarListagem() {
		return mostrarListagem;
	}

	public void setMostrarListagem(Boolean mostrarListagem) {
		this.mostrarListagem = mostrarListagem;
	}

	public void setMostrarListagem() {
		if (this.mostrarListagem) {
			this.mostrarListagem = false;
		} else {
			this.mostrarListagem = true;
		}
	}

	public LazyDataModel<AlunoAula> getLazyListDataModelAlunoAula() {
		int tipo = (int) Util.getAtributoSessao("tipoListagem");
		if (tipo == 0) {
			return getLazyListDataModelAlunoAulaDisciplina();
		} else {
			return getLazyListDataModelAlunoAulaSemana();
		}
	}

	public LazyDataModel<AlunoAula> getLazyListDataModelAlunoAulaDisciplina() {
		if (lazyListDataModelAlunoAula == null) {

			lazyListDataModelAlunoAula = new LazyDataModel<AlunoAula>() {

				@Override
				public AlunoAula getRowData(String rowKey) {
					return aulaService.findAlunoAulaById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(AlunoAula al) {
					return al.getId();
				}

				@Override
				public List<AlunoAula> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);

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
					if (filtros.containsKey("disciplina")) {
						if (filtros.get("disciplina").equals(DisciplinaEnum.ARTES.toString())) {
							filtros.put("disciplina", DisciplinaEnum.ARTES);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.CIENCIAS.toString())) {
							filtros.put("disciplina", DisciplinaEnum.CIENCIAS);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.EDUCACAO_FISICA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.EDUCACAO_FISICA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.FORMACAO_CRISTA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.FORMACAO_CRISTA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.GEOGRAFIA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.GEOGRAFIA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.HISTORIA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.HISTORIA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.MATEMATICA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.MATEMATICA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.PORTUGUES.toString())) {
							filtros.put("disciplina", DisciplinaEnum.PORTUGUES);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.INGLES.toString())) {
							filtros.put("disciplina", DisciplinaEnum.INGLES);
						}

					}

					filtros.put("aluno", Long.parseLong(getLoggedUser().getIdCrianca1()));
					filtros.put("aula.disciplina", getDisciplina());
					filtros.put("aula.visible", true);

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<AlunoAula> ol = aulaService.findAlunoAula(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModelAlunoAula.setRowCount((int) aulaService.countAlunoAula(filtros));
						return ol;
					}

					this.setRowCount((int) aulaService.countAlunoAula(filtros));
					return null;

				}
			};
			Map<String, Object> filtros = new HashMap<String, Object>();

			filtros.put("aluno", Long.parseLong(getLoggedUser().getIdCrianca1()));
			filtros.put("aula.disciplina", getDisciplina());
			filtros.put("aula.visible", true);

			lazyListDataModelAlunoAula.setRowCount((int) aulaService.countAlunoAula(filtros));

		}

		return lazyListDataModelAlunoAula;

	}

	public LazyDataModel<AlunoAula> getLazyListDataModelAlunoAulaSemana() {
		if (lazyListDataModelAlunoAula == null) {

			lazyListDataModelAlunoAula = new LazyDataModel<AlunoAula>() {

				@Override
				public AlunoAula getRowData(String rowKey) {
					return aulaService.findAlunoAulaById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(AlunoAula al) {
					return al.getId();
				}

				@Override
				public List<AlunoAula> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);

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
					if (filtros.containsKey("disciplina")) {
						if (filtros.get("disciplina").equals(DisciplinaEnum.ARTES.toString())) {
							filtros.put("disciplina", DisciplinaEnum.ARTES);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.CIENCIAS.toString())) {
							filtros.put("disciplina", DisciplinaEnum.CIENCIAS);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.EDUCACAO_FISICA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.EDUCACAO_FISICA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.FORMACAO_CRISTA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.FORMACAO_CRISTA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.GEOGRAFIA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.GEOGRAFIA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.HISTORIA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.HISTORIA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.MATEMATICA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.MATEMATICA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.PORTUGUES.toString())) {
							filtros.put("disciplina", DisciplinaEnum.PORTUGUES);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.INGLES.toString())) {
							filtros.put("disciplina", DisciplinaEnum.INGLES);
						}

					}

					filtros.put("aluno", Long.parseLong(getLoggedUser().getIdCrianca1()));
					filtros.put("aula.semana", getSemana());
					filtros.put("aula.visible", true);

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<AlunoAula> ol = aulaService.findAlunoAula(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModelAlunoAula.setRowCount((int) aulaService.countAlunoAula(filtros));
						return ol;
					}

					this.setRowCount((int) aulaService.countAlunoAula(filtros));
					return null;

				}
			};
			Map<String, Object> filtros = new HashMap<String, Object>();

			filtros.put("aluno", Long.parseLong(getLoggedUser().getIdCrianca1()));
			filtros.put("aula.semana", getSemana());
			filtros.put("aula.visible", true);

			lazyListDataModelAlunoAula.setRowCount((int) aulaService.countAlunoAula(filtros));

		}

		return lazyListDataModelAlunoAula;

	}

	public void setLazyListDataModelAlunoAula(LazyDataModel<AlunoAula> lazyListDataModelAlunoAula) {
		this.lazyListDataModelAlunoAula = lazyListDataModelAlunoAula;
	}

	public void setAlunoAula(AlunoAula alunoAula) {
		this.alunoAula = alunoAula;
	}

	public DashboardModel getModel() {
		return model;
	}

	public void setModel(DashboardModel model) {
		this.model = model;
	}

	public DashboardModel getModel2() {
		return model2;
	}

	public void setModel2(DashboardModel model2) {
		this.model2 = model2;
	}

	public TreeNode getRoot() {
		if (root == null) {
			root = criaArvoreAulas();
		}
		return root;
	}

	public TreeNode getRootForce() {
		root = criaArvoreAulas();
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode criaArvoreAulas() {

		return criaArvoreAulas(false, null);

	}

	public TreeNode criaArvoreAulas(boolean expandir, Date data) {
		int tipo = (int) Util.getAtributoSessao("tipoListagem");
		

		AlunoAula rootAA = new AlunoAula();
		TreeNode root = new DefaultTreeNode(rootAA, null);
		try {

			Calendar c = Calendar.getInstance();
			Date segunda = null;
			

			if(tipo == 0){
				segunda =  new SimpleDateFormat("yyyy-MM-dd").parse("2020-08-03");
			}else{
				segunda = new SimpleDateFormat("yyyy-MM-dd").parse(getLimitInferiorSemana(getSemana()));
			}
			
			c.setTime(segunda);
			Date terca = getProximoDia(c);
			Date quarta = getProximoDia(c);
			Date quinta = getProximoDia(c);
			Date sexta = getProximoDia(c);

			TreeNode nodeSegunda = null;
			TreeNode nodeTerca = null;
			TreeNode nodeQuarta = null;
			TreeNode nodeQuinta = null;
			TreeNode nodeSexta = null;

			if (tipo == 0) {
				Map<String, Object> filtros = new HashMap<String, Object>();
				filtros.put("aluno", Long.parseLong(getLoggedUser().getIdCrianca1()));
				filtros.put("aula.disciplina", getDisciplina());
				filtros.put("aula.visible", true);

				List<AlunoAula> aulasDisciplina = aulaService.findAlunoAula(0, 0, null, null, filtros);
				for(AlunoAula aulas :aulasDisciplina){
					TreeNode aula = new DefaultTreeNode(aulas, root);
				}
				
			} else {
				 nodeSegunda = montarNode(segunda, root);
				 nodeTerca = montarNode(terca, root);
				 nodeQuarta = montarNode(quarta, root);
				 nodeQuinta = montarNode(quinta, root);
				 nodeSexta = montarNode(sexta, root);
			}
			
			if (expandir) {
				expandirNodo(data, segunda, nodeSegunda);
				expandirNodo(data, terca, nodeTerca);
				expandirNodo(data, quarta, nodeQuarta);
				expandirNodo(data, quinta, nodeQuinta);
				expandirNodo(data, sexta, nodeSexta);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return root;
	}

	private void expandirNodo(Date data, Date segunda, TreeNode nodeSegunda) {
		if (data != null) {
			if (data.compareTo(segunda) == 0) {
				if (nodeSegunda != null) {
					expandirNode(nodeSegunda);
				}
			}
		}
	}

	private void expandirNode(TreeNode node) {
		node.setExpanded(true);
	}

	private Date getProximoDia(Calendar c) {
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	private TreeNode montarNode(Date dataNode, TreeNode root) {
		Date hj = new Date();
		AlunoAula alunoAula = new AlunoAula();
		TreeNode dia2 = null;
		String dataAulaExtenso = dateFormat.format(dataNode);
		alunoAula.setAula(new Aula(dataAulaExtenso));
		if (dataNode.before(hj)) {
			dia2 = new DefaultTreeNode(alunoAula, root);
			List<AlunoAula> aulasTerca = aulaService.findAlunoAulaby(Long.parseLong(getLoggedUser().getIdCrianca1()),
					dataAulaExtenso);
			if (aulasTerca != null && aulasTerca.size() > 0) {
				for (AlunoAula al : aulasTerca) {
					TreeNode aula = new DefaultTreeNode(al, dia2);
				}
			}
		}
		return dia2;
	}

	private TreeNode montarNodeDisciplina(Date dataNode, TreeNode root) {
		Date hj = new Date();
		AlunoAula alunoAula = new AlunoAula();
		TreeNode dia2 = null;
		String dataAulaExtenso = dateFormat.format(dataNode);
		alunoAula.setAula(new Aula(dataAulaExtenso));
		if (dataNode.before(hj)) {
			dia2 = new DefaultTreeNode(alunoAula, root);
			Map<String, Object> filtros = new HashMap<String, Object>();
			filtros.put("aluno", Long.parseLong(getLoggedUser().getIdCrianca1()));
			filtros.put("aula.disciplina", getDisciplina());
			filtros.put("aula.visible", true);
			filtros.put("aula.dataAula", dataAulaExtenso);

			List<AlunoAula> aulasTerca = aulaService.findAlunoAula(0, 0, null, null, filtros);

			if (aulasTerca != null && aulasTerca.size() > 0) {
				for (AlunoAula al : aulasTerca) {
					TreeNode aula = new DefaultTreeNode(al, dia2);
				}
			}
		}
		return dia2;
	}

	public boolean ultimaAulaDodia(AlunoAula al) {
		AlunoAula al2 = aulaService.getUltimaAula(dateFormat.format(al.getAula().getDataAula()),
				getLoggedUser().getIdCrianca1());
		if (al2.getId().equals(al.getId())) {
			return true;
		}
		return false;
	}

	public void onNodeSelect(NodeSelectEvent event) {
		AlunoAula al2 = (AlunoAula) event.getTreeNode().getData();
		if (al2.getAssistiu() != null && al2.getAssistiu()) {
			Util.addAtributoSessao("alunoAula", al2);
			setAlunoAula(al2);

		} else {
			AlunoAula al = null;
			if (al2.getAula().getDataAula() != null) {
				al = aulaService.getPrimeiraAulaNaoAssistida(dateFormat.format(al2.getAula().getDataAula()),
						getLoggedUser().getIdCrianca1());
			} else {
				al = aulaService.getPrimeiraAulaNaoAssistida(al2.getAula().getTitulo(),
						getLoggedUser().getIdCrianca1());
			}

			Util.addAtributoSessao("alunoAula", al);
			setAlunoAula(al);
		}

		/*
		 * if (true) { FacesMessage message = new
		 * FacesMessage(FacesMessage.SEVERITY_INFO, "ATEÇÃO",
		 * "Você deve assistir primeiro a aula: " + al.getAula().getTitulo());
		 * FacesContext.getCurrentInstance().addMessage(null, message); }
		 */
	}

	private String getLimitSuperiorSemana(int i) {
		String date = "";
		switch (i) {

		case 1:
			date = "2020-08-07";
			break;

		case 2:
			date = "2020-08-14";
			break;

		case 3:
			date = "2020-08-21";
			break;

		case 4:
			date = "2020-08-27";
			break;
		case 5:
			date = "2020-09-04";
			break;
		case 6:
			date = "2020-09-11";
			break;
		case 7:
			date = "2020-09-18";
			break;
		case 8:
			date = "2020-09-25";
			break;
		case 9:
			date = "2020-10-02";
			break;
		case 10:
			date = "2020-10-09";
			break;
		case 11:
			date = "2020-10-16";
			break;
		case 12:
			date = "2020-10-23";
			break;
		case 13:
			date = "2020-10-30";
			break;
		case 14:
			date = "2020-11-06";
			break;
		case 15:
			date = "2020-11-13";
			break;
		case 16:
			date = "2020-11-20";
			break;
		case 17:
			date = "2020-11-27";
			break;
		case 18:
			date = "2020-12-04";
			break;
		case 19:
			date = "2020-12-10";
			break;
		case 20:
			date = "2020-12-18";
			break;
		case 21:
			date = "2020-12-25";
			break;
		case 22:
			date = "2021-01-01";
			break;

		default:
			break;
		}
		return date;
	}

	private String getLimitInferiorSemana(int i) {
		String date = "";
		switch (i) {

		case 1:
			date = "2020-08-03";
			break;

		case 2:
			date = "2020-08-10";
			break;

		case 3:
			date = "2020-08-17";
			break;

		case 4:
			date = "2020-08-24";
			break;
		case 5:
			date = "2020-08-31";
			break;
		case 6:
			date = "2020-09-07";
			break;
		case 7:
			date = "2020-09-14";
			break;
		case 8:
			date = "2020-09-21";
			break;
		case 9:
			date = "2020-09-28";
			break;
		case 10:
			date = "2020-10-05";
			break;
		case 11:
			date = "2020-10-12";
			break;
		case 12:
			date = "2020-10-19";
			break;
		case 13:
			date = "2020-10-26";
			break;
		case 14:
			date = "2020-11-02";
			break;
		case 15:
			date = "2020-11-09";
			break;
		case 16:
			date = "2020-11-16";
			break;
		case 17:
			date = "2020-11-23";
			break;
		case 18:
			date = "2020-11-30";
			break;
		case 19:
			date = "2020-12-07";
			break;
		case 20:
			date = "2020-12-14";
			break;
		case 21:
			date = "2020-12-21";
			break;
		case 22:
			date = "2020-12-28";
			break;

		default:
			break;
		}
		return date;
	}

	public String getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(String selectedNode) {
		this.selectedNode = selectedNode;
	}

	public LazyDataModel<Aluno> getLazyListDataModelChamada() {
		if (lazyListDataModelChamada == null) {

			lazyListDataModelChamada = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);

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
					if (filtros.containsKey("disciplina")) {
						if (filtros.get("disciplina").equals(DisciplinaEnum.ARTES.toString())) {
							filtros.put("disciplina", DisciplinaEnum.ARTES);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.CIENCIAS.toString())) {
							filtros.put("disciplina", DisciplinaEnum.CIENCIAS);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.EDUCACAO_FISICA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.EDUCACAO_FISICA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.FORMACAO_CRISTA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.FORMACAO_CRISTA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.GEOGRAFIA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.GEOGRAFIA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.HISTORIA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.HISTORIA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.MATEMATICA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.MATEMATICA);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.PORTUGUES.toString())) {
							filtros.put("disciplina", DisciplinaEnum.PORTUGUES);
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.INGLES.toString())) {
							filtros.put("disciplina", DisciplinaEnum.INGLES);
						}

					}

					filtros.put("serie", getSeries());

					List<Aluno> ol = alunoService.findAll((Serie)getSeries().toArray()[0]);

					if (ol != null && ol.size() > 0) {
						lazyListDataModelChamada.setRowCount(1);
						return ol;
					}

					this.setRowCount(1);
					return null;

				}
			};
			lazyListDataModelChamada.setRowCount(1);

		}

		return lazyListDataModelChamada;
	}

	public void setLazyListDataModelChamada(LazyDataModel<Aluno> lazyListDataModelChamada) {
		this.lazyListDataModelChamada = lazyListDataModelChamada;
	}


}

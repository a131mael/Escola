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
import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.enums.TipoMembro;
import org.escola.model.Aula;
import org.escola.model.Member;
import org.escola.model.Turma;
import org.escola.service.AulaService;
import org.escola.service.ConfiguracaoService;
import org.escola.service.TurmaService;
import org.escola.util.Util;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named
@ViewScoped
public class AulaController extends AuthController implements Serializable {

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Aula aula;

	@Inject
	private AulaService aulaService;

	@Inject
	private ConfiguracaoService configuracaoService;

	@Inject
	private TurmaService turmaService;

	private LazyDataModel<Aula> lazyListDataModel;

	private Boolean mostrarListagem = false;

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@PostConstruct
	private void init() {
		if (getAula() == null) {
			Object obj = Util.getAtributoSessao("aula");
			if (obj != null) {
				setAula((Aula) obj);
			} else {
				setAula(new Aula());
			}
		}
	}

	public void saveOrder(Aula aula) {
		aulaService.save(aula, null);
	}

	public void onRowSelect(SelectEvent event) {
		aula = (Aula) event.getObject();
	}

	public void onRowUnselect(UnselectEvent event) {
		aula = null;
	}

	public boolean isAulaSelecionada() {
		if (getAula() == null) {
			return false;
		}
		return getAula().getId() != null ? true : false;
	}

	public LazyDataModel<Aula> getLazyDataModel() {
		if (lazyListDataModel == null) {

			lazyListDataModel = new LazyDataModel<Aula>() {

				@Override
				public Aula getRowData(String rowKey) {
					return aulaService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aula al) {
					return al.getId();
				}

				@Override
				public List<Aula> load(int first, int pageSize, String order, SortOrder so, Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					try {
						if (filtros.containsKey("dataAula")) {
							Date data = null;
							data = new SimpleDateFormat("dd/MM/yyyy").parse((String) filtros.get("dataAula"));
							filtros.put("dataAula", data);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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
						} else if (filtros.get("serie").equals(Serie.SEXTO_ANO.toString())) {
							filtros.put("serie", Serie.SEXTO_ANO);
						} else if (filtros.get("serie").equals(Serie.SETIMO_ANO.toString())) {
							filtros.put("serie", Serie.SETIMO_ANO);
						} else if (filtros.get("serie").equals(Serie.OITAVO_ANO.toString())) {
							filtros.put("serie", Serie.OITAVO_ANO);
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
						} else if (filtros.get("disciplina").equals(DisciplinaEnum.ESPANHOL.toString())) {
							filtros.put("disciplina", DisciplinaEnum.ESPANHOL);
						}

						else if (filtros.get("disciplina").equals(DisciplinaEnum.CULINARIA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.CULINARIA);
						}
						
						else if (filtros.get("disciplina").equals(DisciplinaEnum.LIVRO_EXPERIENCIAS.toString())) {
							filtros.put("disciplina", DisciplinaEnum.LIVRO_EXPERIENCIAS);
						}
						
						else if (filtros.get("disciplina").equals(DisciplinaEnum.BRINCADEIRAS_E_ARTE.toString())) {
							filtros.put("disciplina", DisciplinaEnum.BRINCADEIRAS_E_ARTE);
						}
						
						else if (filtros.get("disciplina").equals(DisciplinaEnum.LETRAR.toString())) {
							filtros.put("disciplina", DisciplinaEnum.LETRAR);
						}

						
						else if (filtros.get("disciplina").equals(DisciplinaEnum.NUMERAR.toString())) {
							filtros.put("disciplina", DisciplinaEnum.NUMERAR);
						}
						
						else if (filtros.get("disciplina").equals(DisciplinaEnum.INVESTIGAR.toString())) {
							filtros.put("disciplina", DisciplinaEnum.INVESTIGAR);
						}
						
						else if (filtros.get("disciplina").equals(DisciplinaEnum.DESENHAR.toString())) {
							filtros.put("disciplina", DisciplinaEnum.DESENHAR);
						}
						
						else if (filtros.get("disciplina").equals(DisciplinaEnum.HISTORIA_EM_CENA.toString())) {
							filtros.put("disciplina", DisciplinaEnum.HISTORIA_EM_CENA);
						}

					}

					/* filtros.put("removido", false); */

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";
					if (getSeriesLoggedUser() != null && getSeriesLoggedUser().size() == 1) {
						filtros.put("serie", getSeriesLoggedUser().get(0));
					}

					List<Aula> ol = aulaService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModel.setRowCount((int) aulaService.count(filtros));
						return ol;
					}

					this.setRowCount((int) aulaService.count(filtros));
					return null;

				}
			};
			lazyListDataModel.setRowCount((int) aulaService.count(null));

		}

		return lazyListDataModel;

	}

	public List<Serie> getSeriesLoggedUser() {
		if (getLoggedUser().getProfessor() != null) {
			return turmaService.findAllSeries(getLoggedUser().getProfessor().getId());
		} else {
			return null;
		}
	}

	public Double getMedia(Aula aula) {
		return 0D;
	}

	public String salvar() {

		aulaService.save(getAula(), null);

		return "index";
	}

	public boolean podeEditar(Aula aula) {
		if (getLoggedUser().getTipoMembro().equals(TipoMembro.ADMIM)) {
			return true;
		} else {

			return true;
		}
	}

	public boolean exibirSerie() {
		/*
		 * if(getLoggedUser().getTipoMembro().equals(TipoMembro.ADMIM)){ return
		 * true; }else {
		 * if(getLoggedUser().getTipoMembro().equals(TipoMembro.PROFESSOR)){
		 * Set<Serie> series = getSeries(getLoggedUser()); if(series.size() >
		 * 1){ return true; }else{ return false; } } return true; }
		 */
		return true;
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

	public String editar(Long idprof) {
		setAula(aulaService.findById(idprof));
		Util.addAtributoSessao("aula", getAula());
		return "cadastrar";
	}

	public String editar() {
		return editar(getAula().getId());
	}

	public String remover(Long idTurma) {
		aulaService.remover(idTurma);
		return "index";
	}

	public String remover() {
		return remover(getAula().getId());
	}

	public String adicionarNovo() {
		Util.removeAtributoSessao("aula");
		return "cadastrar";
	}

	public Aula getAula() {
		return aula;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
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

}

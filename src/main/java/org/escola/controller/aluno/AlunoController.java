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
package org.escola.controller.aluno;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import org.escola.controller.OfficeDOCUtil;
import org.escola.enums.BimestreEnum;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.model.Aluno;
import org.escola.model.AlunoAvaliacao;
import org.escola.model.HistoricoAluno;
import org.escola.model.Professor;
import org.escola.service.AlunoService;
import org.escola.service.AvaliacaoService;
import org.escola.util.Constant;
import org.escola.util.CurrencyWriter;
import org.escola.util.FileDownload;
import org.escola.util.ImpressoesUtils;
import org.escola.util.Util;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class AlunoController implements Serializable {

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Aluno aluno;

	@Produces
	@Named
	private HistoricoAluno historicoAluno;

	@Produces
	@Named
	private List<Aluno> alunos;

	@Inject
	private AlunoService alunoService;

	@Inject
	private AvaliacaoService avaliacaoService;

	private OfficeDOCUtil officeDOCUtil;
	CurrencyWriter cw;

	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoPortugues;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoIngles;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoMatematica;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoHistoria;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoEDFisica;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoGeografia;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoCiencias;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoFormacaoCrista;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoArtes;

	@Named
	private BimestreEnum bimestreSel;
	@Named
	private DisciplinaEnum disciplinaSel;

	private LazyDataModel<Aluno> lazyListDataModel;
	
	private LazyDataModel<Aluno> lazyListDataModelExAlunos;
	
	private LazyDataModel<Aluno> lazyListDataModelUltimoAnoLetivo;

	public LazyDataModel<Aluno> getLazyDataModel() {
		if (lazyListDataModel == null) {

			lazyListDataModel = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,
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

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Aluno> ol = alunoService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModel.setRowCount((int) alunoService.count(filtros));
						return ol;
					}

					this.setRowCount((int) alunoService.count(filtros));
					return null;

				}
			};
			lazyListDataModel.setRowCount((int) alunoService.count(null));

		}

		return lazyListDataModel;

	}

	public LazyDataModel<Aluno> getLazyDataModelExAlunos() {
		if (lazyListDataModelExAlunos == null) {

			lazyListDataModelExAlunos = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					filtros.put("removido" , true);
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

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Aluno> ol = alunoService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModelExAlunos.setRowCount((int) alunoService.count(filtros));
						return ol;
					}

					this.setRowCount((int) alunoService.count(filtros));
					return null;

				}
			};
			lazyListDataModelExAlunos.setRowCount((int) alunoService.count(null));

		}

		return lazyListDataModelExAlunos;

	}

	public LazyDataModel<Aluno> getLazyDataModelUltimoAnoLetivo() {
		if (lazyListDataModelUltimoAnoLetivo == null) {

			lazyListDataModelUltimoAnoLetivo = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					
					filtros.put("anoLetivo" , Constant.anoLetivoAtual -1);
					
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

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Aluno> ol = alunoService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModelUltimoAnoLetivo.setRowCount((int) alunoService.count(filtros));
						return ol;
					}

					this.setRowCount((int) alunoService.count(filtros));
					return null;

				}
			};
			lazyListDataModelUltimoAnoLetivo.setRowCount((int) alunoService.count(null));

		}

		return lazyListDataModelUltimoAnoLetivo;

	}
	
	@PostConstruct
	private void init() {
		if (aluno == null) {
			Object obj = Util.getAtributoSessao("aluno");
			if (obj != null) {
				aluno = (Aluno) obj;
			} else {
				aluno = new Aluno();
			}
		}
		if(alunoAvaliacaoIngles == null){
			Object obj = Util.getAtributoSessao("alunoAvaliacaoIngles");
			if (obj != null) {
				alunoAvaliacaoIngles =  (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoIngles = new LinkedHashMap<>();;
			}
		}
		if(alunoAvaliacaoArtes == null){
			Object obj = Util.getAtributoSessao("alunoAvaliacaoArtes");
			if (obj != null) {
				alunoAvaliacaoArtes =  (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoArtes = new LinkedHashMap<>();;
			}
		}
		if(alunoAvaliacaoCiencias == null){
			Object obj = Util.getAtributoSessao("alunoAvaliacaoCiencias");
			if (obj != null) {
				alunoAvaliacaoCiencias =  (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoCiencias = new LinkedHashMap<>();;
			}
		}
		if(alunoAvaliacaoEDFisica == null){
			Object obj = Util.getAtributoSessao("alunoAvaliacaoEDFisica");
			if (obj != null) {
				alunoAvaliacaoEDFisica =  (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoEDFisica = new LinkedHashMap<>();;
			}
		}
		if(alunoAvaliacaoFormacaoCrista == null){
			Object obj = Util.getAtributoSessao("alunoAvaliacaoFormacaoCrista");
			if (obj != null) {
				alunoAvaliacaoFormacaoCrista =  (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoFormacaoCrista = new LinkedHashMap<>();;
			}
		}
		if(alunoAvaliacaoGeografia == null){
			Object obj = Util.getAtributoSessao("alunoAvaliacaoGeografia");
			if (obj != null) {
				alunoAvaliacaoGeografia =  (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoGeografia = new LinkedHashMap<>();;
			}
		}
		if(alunoAvaliacaoHistoria == null){
			Object obj = Util.getAtributoSessao("alunoAvaliacaoHistoria");
			if (obj != null) {
				alunoAvaliacaoHistoria =  (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoHistoria = new LinkedHashMap<>();;
			}
		}
		
		if(alunoAvaliacaoMatematica == null){
			Object obj = Util.getAtributoSessao("alunoAvaliacaoMatematica");
			if (obj != null) {
				alunoAvaliacaoMatematica =  (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoMatematica = new LinkedHashMap<>();;
			}
		}
		
		if(alunoAvaliacaoPortugues == null){
			Object obj = Util.getAtributoSessao("alunoAvaliacaoPortugues");
			if (obj != null) {
				alunoAvaliacaoPortugues =  (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoPortugues = new LinkedHashMap<>();;
			}
		}
		
		if (historicoAluno == null) {
			historicoAluno = new HistoricoAluno();
		}
		officeDOCUtil = new OfficeDOCUtil();
		cw = new CurrencyWriter();
	}
	
	public boolean estaEmUmaTurma(long idAluno){
		boolean estaNaTurma =alunoService.estaEmUmaTUrma(idAluno);
		return estaNaTurma;
	}

	private Float maior(Float float1, Float float2) {
		return float1 > float2 ? float1 : float2;
	}

	private String mostraNotas(Float nota) {
		if (nota == null || nota == 0) {
			return "";
		} else {
			return String.valueOf(Math.round(nota * 100) / 100);
		}
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
	
	public float getNota(DisciplinaEnum disciplina, BimestreEnum  bimestre){
		return alunoService.getNota(aluno.getId(), disciplina, bimestre, false);
	}

	public List<HistoricoAluno> getHistoricoAluno(Aluno aluno){
		return alunoService.getHistoricoAluno(aluno);
	}
	
	public void popularAlunoAvaliacao(Aluno aluno) {
		setAlunoAvaliacaoPortugues(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.PORTUGUES,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoIngles(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.INGLES,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoEDFisica(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null,
				DisciplinaEnum.EDUCACAO_FISICA, this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoGeografia(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.GEOGRAFIA,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoHistoria(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.HISTORIA,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoMatematica(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null,
				DisciplinaEnum.MATEMATICA, this.getBimestreSel(), aluno.getSerie()));

		setAlunoAvaliacaoCiencias(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.CIENCIAS,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoFormacaoCrista(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null,
				DisciplinaEnum.FORMACAO_CRISTA, this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoArtes(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.ARTES,
				this.getBimestreSel(), aluno.getSerie()));
	}

	public boolean renderDisciplina(int ordinal) {
		if (getDisciplinaSel() == null) {
			return true;
		}

		return ordinal == getDisciplinaSel().ordinal();
	}
	
	public void saveAvaliacaoAluno(AlunoAvaliacao alav) {
		avaliacaoService.saveAlunoAvaliacao(alav);
	} 

	public void saveAvaliacaoAluno(Long idAluAv, Float nota) {
		avaliacaoService.saveAlunoAvaliacao(idAluAv, nota);
	}

	private HashMap<String, String> montarBoletim(Aluno aluno) {
		Professor prof = alunoService.getProfessor(aluno.getId());
		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("#nomeAluno", aluno.getNomeAluno());
		trocas.put("#nomeProfessor", prof.getNome());
		trocas.put("#turma", aluno.getSerie().getName());

		float notaPortuguesPrimeiroBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float notaPortuguesSegundoBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float notaPortuguesTerceiroBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float notaPortuguesQuartoBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float notaPortuguesPrimeiroRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float notaPortuguesSegundoRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float notaPortuguesTerceiroRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float notaPortuguesQuartoRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaPortuguesRecFinal = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, true, true);

		// PORTUGUES
		trocas.put("#np1", mostraNotas(notaPortuguesPrimeiroBimestre));
		trocas.put("#np2", mostraNotas(notaPortuguesSegundoBimestre));
		trocas.put("#np3", mostraNotas(notaPortuguesTerceiroBimestre));
		trocas.put("#np4", mostraNotas(notaPortuguesQuartoBimestre));

		// rec
		trocas.put("#npr1", mostraNotas(notaPortuguesPrimeiroRec));
		trocas.put("#npr2", mostraNotas(notaPortuguesSegundoRec));
		trocas.put("#npr3", mostraNotas(notaPortuguesTerceiroRec));
		trocas.put("#npr4", mostraNotas(notaPortuguesQuartoRec));
		// mediaFinal
		trocas.put("#mp1", mostraNotas(maior(notaPortuguesPrimeiroBimestre, notaPortuguesPrimeiroRec)));
		trocas.put("#mp2", mostraNotas(maior(notaPortuguesSegundoRec, notaPortuguesSegundoBimestre)));
		trocas.put("#mp3", mostraNotas(maior(notaPortuguesTerceiroRec, notaPortuguesTerceiroBimestre)));
		trocas.put("#mp4", mostraNotas(maior(notaPortuguesQuartoRec, notaPortuguesQuartoBimestre)));
		// Final do ano
		trocas.put("#npF",
				mostraNotas(media(maior(notaPortuguesPrimeiroBimestre, notaPortuguesPrimeiroRec),
						maior(notaPortuguesSegundoRec, notaPortuguesSegundoBimestre),
						maior(notaPortuguesTerceiroRec, notaPortuguesTerceiroBimestre),
						maior(notaPortuguesQuartoRec, notaPortuguesQuartoBimestre))));
		trocas.put("#nprf", mostraNotas(notaPortuguesRecFinal));

		// Matematica
		float notaMTM1Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float notaMTM2Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float notaMTM3Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float notaMTM4Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float notaMTM1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float notaMTM2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float notaMTM3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float notaMTM4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.QUARTO_BIMESTRE,
				true);
		float notaMtmRecFinal = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, true, true);

		trocas.put("#nm1", mostraNotas(notaMTM1Bimestre));
		trocas.put("#nm2", mostraNotas(notaMTM2Bimestre));
		trocas.put("#nm3", mostraNotas(notaMTM3Bimestre));
		trocas.put("#nm4", mostraNotas(notaMTM4Bimestre));
		// rec
		trocas.put("#nmr1", mostraNotas(notaMTM1Rec));
		trocas.put("#nmr2", mostraNotas(notaMTM2Rec));
		trocas.put("#nmr3", mostraNotas(notaMTM3Rec));
		trocas.put("#nmr4", mostraNotas(notaMTM4Rec));
		// mediaFinal
		trocas.put("#mm1", mostraNotas(maior(notaMTM1Bimestre, notaMTM1Rec)));
		trocas.put("#mm2", mostraNotas(maior(notaMTM2Bimestre, notaMTM2Rec)));
		trocas.put("#mm3", mostraNotas(maior(notaMTM3Bimestre, notaMTM3Rec)));
		trocas.put("#mm4", mostraNotas(maior(notaMTM4Bimestre, notaMTM4Rec)));
		// Final do ano
		trocas.put("#nmF", mostraNotas(media(maior(notaMTM1Bimestre, notaMTM1Rec), maior(notaMTM2Bimestre, notaMTM2Rec),
				maior(notaMTM3Bimestre, notaMTM3Rec), maior(notaMTM4Bimestre, notaMTM4Rec))));
		trocas.put("#nmrf", mostraNotas(notaMtmRecFinal));

		// Ingles
		float nota1BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.SEGUNDO_BIMESTRE,
				true);
		float nota3RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.QUARTO_BIMESTRE,
				true);
		float notaRecFinalIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, true, true);

		trocas.put("#ni1", mostraNotas(nota1BimestreIngles));
		trocas.put("#ni2", mostraNotas(nota2BimestreIngles));
		trocas.put("#ni3", mostraNotas(nota3BimestreIngles));
		trocas.put("#ni4", mostraNotas(nota4BimestreIngles));
		// rec
		trocas.put("#nir1", mostraNotas(nota1RecIngles));
		trocas.put("#nir2", mostraNotas(nota2RecIngles));
		trocas.put("#nir3", mostraNotas(nota3RecIngles));
		trocas.put("#nir4", mostraNotas(nota4RecIngles));
		// mediaFinal
		trocas.put("#mi1", mostraNotas(maior(nota1RecIngles, nota1BimestreIngles)));
		trocas.put("#mi2", mostraNotas(maior(nota2RecIngles, nota2BimestreIngles)));
		trocas.put("#mi3", mostraNotas(maior(nota3RecIngles, nota3BimestreIngles)));
		trocas.put("#mi4", mostraNotas(maior(nota4RecIngles, nota4BimestreIngles)));
		// Final do ano
		trocas.put("#niF",
				mostraNotas(media(maior(nota1RecIngles, nota1BimestreIngles),
						maior(nota2RecIngles, nota2BimestreIngles), maior(nota3RecIngles, nota3BimestreIngles),
						maior(nota4RecIngles, nota4BimestreIngles))));
		trocas.put("#nirf", mostraNotas(notaRecFinalIngles));

		// EdFisica
		float nota1BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, true, true);

		trocas.put("#ne1", mostraNotas(nota1BimestreEdFisica));
		trocas.put("#ne2", mostraNotas(nota2BimestreEdFisica));
		trocas.put("#ne3", mostraNotas(nota3BimestreEdFisica));
		trocas.put("#ne4", mostraNotas(nota4BimestreEdFisica));
		// rec
		trocas.put("#ner1", mostraNotas(nota1RecEdFisica));
		trocas.put("#ner2", mostraNotas(nota2RecEdFisica));
		trocas.put("#ner3", mostraNotas(nota3RecEdFisica));
		trocas.put("#ner4", mostraNotas(nota4RecEdFisica));
		// mediaFinal
		trocas.put("#me1", mostraNotas(maior(nota1BimestreEdFisica, nota1RecEdFisica)));
		trocas.put("#me2", mostraNotas(maior(nota2BimestreEdFisica, nota2RecEdFisica)));
		trocas.put("#me3", mostraNotas(maior(nota3BimestreEdFisica, nota3RecEdFisica)));
		trocas.put("#me4", mostraNotas(maior(nota4BimestreEdFisica, nota4RecEdFisica)));
		// Final do ano
		trocas.put("#neF",
				mostraNotas(media(maior(nota1BimestreEdFisica, nota1RecEdFisica),
						maior(nota2BimestreEdFisica, nota2RecEdFisica), maior(nota3BimestreEdFisica, nota3RecEdFisica),
						maior(nota4BimestreEdFisica, nota4RecEdFisica))));
		trocas.put("#nerf", mostraNotas(notaRecFinalEdFisica));

		// Geofrafia
		float nota1BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, true, true);

		trocas.put("#ng1", mostraNotas(nota1BimestreGeografia));
		trocas.put("#ng2", mostraNotas(nota2BimestreGeografia));
		trocas.put("#ng3", mostraNotas(nota3BimestreGeografia));
		trocas.put("#ng4", mostraNotas(nota4BimestreGeografia));
		// rec
		trocas.put("#ngr1", mostraNotas(nota1RecGeografia));
		trocas.put("#ngr2", mostraNotas(nota2RecGeografia));
		trocas.put("#ngr3", mostraNotas(nota3RecGeografia));
		trocas.put("#ngr4", mostraNotas(nota4RecGeografia));
		// mediaFinal
		trocas.put("#mg1", mostraNotas(maior(nota1RecGeografia, nota1BimestreGeografia)));
		trocas.put("#mg2", mostraNotas(maior(nota2RecGeografia, nota2BimestreGeografia)));
		trocas.put("#mg3", mostraNotas(maior(nota3RecGeografia, nota3BimestreGeografia)));
		trocas.put("#mg4", mostraNotas(maior(nota4RecGeografia, nota4BimestreGeografia)));
		// Final do ano
		trocas.put("#ngF",
				mostraNotas(media(maior(nota1RecGeografia, nota1BimestreGeografia),
						maior(nota2RecGeografia, nota2BimestreGeografia),
						maior(nota3RecGeografia, nota3BimestreGeografia),
						maior(nota4RecGeografia, nota4BimestreGeografia))));
		trocas.put("#ngrf", mostraNotas(notaRecFinalGeografia));

		// Historia
		float nota1BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, true, true);

		trocas.put("#nh1", mostraNotas(nota1BimestreHistoria));
		trocas.put("#nh2", mostraNotas(nota2BimestreHistoria));
		trocas.put("#nh3", mostraNotas(nota3BimestreHistoria));
		trocas.put("#nh4", mostraNotas(nota4BimestreHistoria));
		// rec
		trocas.put("#nhr1", mostraNotas(nota1RecHistoria));
		trocas.put("#nhr2", mostraNotas(nota2RecHistoria));
		trocas.put("#nhr3", mostraNotas(nota3RecHistoria));
		trocas.put("#nhr4", mostraNotas(nota4RecHistoria));
		// mediaFinal
		trocas.put("#mh1", mostraNotas(maior(nota1RecHistoria, nota1BimestreHistoria)));
		trocas.put("#mh2", mostraNotas(maior(nota2RecHistoria, nota2BimestreHistoria)));
		trocas.put("#mh3", mostraNotas(maior(nota3RecHistoria, nota3BimestreHistoria)));
		trocas.put("#mh4", mostraNotas(maior(nota4RecHistoria, nota4BimestreHistoria)));
		// Final do ano
		trocas.put("#nhF",
				mostraNotas(media(maior(nota1RecHistoria, nota1BimestreHistoria),
						maior(nota2RecHistoria, nota2BimestreHistoria), maior(nota3RecHistoria, nota3BimestreHistoria),
						maior(nota4RecHistoria, nota4BimestreHistoria))));
		trocas.put("#nhrf", mostraNotas(notaRecFinalHistoria));

		// Ciencias
		float nota1BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalCiencia = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, true, true);

		trocas.put("#nc1", mostraNotas(nota1BimestreCiencias));
		trocas.put("#nc2", mostraNotas(nota2BimestreCiencias));
		trocas.put("#nc3", mostraNotas(nota3BimestreCiencias));
		trocas.put("#nc4", mostraNotas(nota4BimestreCiencias));
		// rec
		trocas.put("#ncr1", mostraNotas(nota1RecCiencias));
		trocas.put("#ncr2", mostraNotas(nota2RecCiencias));
		trocas.put("#ncr3", mostraNotas(nota3RecCiencias));
		trocas.put("#ncr4", mostraNotas(nota4RecCiencias));
		// mediaFinal
		trocas.put("#mc1", mostraNotas(maior(nota1RecCiencias, nota1BimestreCiencias)));
		trocas.put("#mc2", mostraNotas(maior(nota2RecCiencias, nota2BimestreCiencias)));
		trocas.put("#mc3", mostraNotas(maior(nota3RecCiencias, nota3BimestreCiencias)));
		trocas.put("#mc4", mostraNotas(maior(nota4RecCiencias, nota4BimestreCiencias)));
		// Final do ano
		trocas.put("#ncF",
				mostraNotas(media(maior(nota1RecCiencias, nota1BimestreCiencias),
						maior(nota2RecCiencias, nota2BimestreCiencias), maior(nota3RecCiencias, nota3BimestreCiencias),
						maior(nota4RecCiencias, nota4BimestreCiencias))));
		trocas.put("#ncrf", mostraNotas(notaRecFinalHistoria));

		// Formacao Crista
		float nota1BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, true, true);

		trocas.put("#nf1", mostraNotas(nota1BimestreFormaCrista));
		trocas.put("#nf2", mostraNotas(nota2BimestreFormaCrista));
		trocas.put("#nf3", mostraNotas(nota3BimestreFormaCrista));
		trocas.put("#nf4", mostraNotas(nota4BimestreFormaCrista));
		// rec
		trocas.put("#nfr1", mostraNotas(nota1RecFormaCrista));
		trocas.put("#nfr2", mostraNotas(nota2RecFormaCrista));
		trocas.put("#nfr3", mostraNotas(nota3RecFormaCrista));
		trocas.put("#nfr4", mostraNotas(nota4RecFormaCrista));
		// mediaFinal
		trocas.put("#mf1", mostraNotas(maior(nota1RecFormaCrista, nota1BimestreFormaCrista)));
		trocas.put("#mf2", mostraNotas(maior(nota2RecFormaCrista, nota2BimestreFormaCrista)));
		trocas.put("#mf3", mostraNotas(maior(nota3RecFormaCrista, nota3BimestreFormaCrista)));
		trocas.put("#mf4", mostraNotas(maior(nota4RecFormaCrista, nota4BimestreFormaCrista)));
		// Final do ano
		trocas.put("#nfF",
				mostraNotas(media(maior(nota1RecFormaCrista, nota1BimestreFormaCrista),
						maior(nota2RecFormaCrista, nota2BimestreFormaCrista),
						maior(nota3RecFormaCrista, nota3BimestreFormaCrista),
						maior(nota4RecFormaCrista, nota4BimestreFormaCrista))));
		trocas.put("#nfrf", mostraNotas(notaRecFinalFormaCrista));

		// Artes
		float nota1BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.PRIMEIRO_BIMESTRE,
				true);
		float nota2RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.SEGUNDO_BIMESTRE,
				true);
		float nota3RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.TERCEIRO_BIMESTRE,
				true);
		float nota4RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.QUARTO_BIMESTRE,
				true);
		float notaRecFinalArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, true, true);

		trocas.put("#na1", mostraNotas(nota1BimestreArtes));
		trocas.put("#na2", mostraNotas(nota2BimestreArtes));
		trocas.put("#na3", mostraNotas(nota3BimestreArtes));
		trocas.put("#na4", mostraNotas(nota4BimestreArtes));
		// rec
		trocas.put("#nar1", mostraNotas(nota1RecArtes));
		trocas.put("#nar2", mostraNotas(nota2RecArtes));
		trocas.put("#nar3", mostraNotas(nota3RecArtes));
		trocas.put("#nar4", mostraNotas(nota4RecArtes));
		// mediaFinal
		trocas.put("#ma1", mostraNotas(maior(nota1RecArtes, nota1BimestreArtes)));
		trocas.put("#ma2", mostraNotas(maior(nota2RecArtes, nota2BimestreArtes)));
		trocas.put("#ma3", mostraNotas(maior(nota3RecArtes, nota3BimestreArtes)));
		trocas.put("#ma4", mostraNotas(maior(nota4RecArtes, nota4BimestreArtes)));
		// Final do ano
		trocas.put("#naF",
				mostraNotas(media(maior(nota1RecArtes, nota1BimestreArtes), maior(nota2RecArtes, nota2BimestreArtes),
						maior(nota3RecArtes, nota3BimestreArtes), maior(nota4RecArtes, nota4BimestreArtes))));
		trocas.put("#narf", mostraNotas(notaRecFinalArtes));

		return trocas;
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
		trocas.put("adonaicpfresponsavel", aluno.getCpfResponsavel());
		trocas.put("adonainomeresponsavel", aluno.getNomeResponsavel());

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
		Aluno al = (Aluno) event.getObject();
		FacesMessage msg = new FacesMessage("Car Selected");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselect(UnselectEvent event) {
		Aluno al = (Aluno) event.getObject();
		FacesMessage msg = new FacesMessage("Car Unselected");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public boolean isAlunoSelecionado() {
		return aluno.getId() != null ? true : false;
	}

	public StreamedContent imprimirContratoAdonai(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if(aluno != null && aluno.getId() != null){
			nomeArquivo =aluno.getId() + "b";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "mb1.docx", montarBoletim(aluno), nomeArquivo);
			nomeArquivo += ".doc";
		}else{
			nomeArquivo ="mb1.docx";
		}
		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+nomeArquivo;
		
		InputStream stream =  new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	public StreamedContent imprimirContratoAdonai() throws IOException {
		return imprimirContratoAdonai(aluno);
	}

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

	public StreamedContent imprimirAtestadoFrequencia() throws IOException {
		return imprimirAtestadoFrequencia(aluno);
	}

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

	public StreamedContent imprimirAtestadoMatricula() throws IOException {
		return imprimirAtestadoMatricula(aluno);
	}

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

	public StreamedContent imprimirNegativoDebito() throws IOException {
		return imprimirNegativoDebito(aluno);
	}

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

	public StreamedContent imprimirContrato() throws IOException {
		return imprimirContrato(aluno);
	}

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

	public StreamedContent imprimirAtestadoVaga() throws IOException {
		return imprimirAtestadoVaga(aluno);
	}

	public List<Aluno> getAlunos() {

		return alunoService.findAll();
	}

	public String salvar() {
		alunoService.save(aluno);
		Util.removeAtributoSessao("aluno");
		return "index";
	}

	public String voltar() {
		return "index";
	}

	public String editar(Long idprof) {
		aluno = alunoService.findById(idprof);
		Util.addAtributoSessao("aluno", aluno);
		return "cadastrar";
	}
	
	public String visualizar(Aluno aluno) {
		popularAlunoAvaliacao(aluno);
		Util.addAtributoSessao("alunoAvaliacaoIngles", alunoAvaliacaoIngles);
		Util.addAtributoSessao("alunoAvaliacaoArtes", alunoAvaliacaoArtes);
		Util.addAtributoSessao("alunoAvaliacaoCiencias", alunoAvaliacaoCiencias);
		Util.addAtributoSessao("alunoAvaliacaoEdFisica", alunoAvaliacaoEDFisica);
		Util.addAtributoSessao("alunoAvaliacaoFormacaoCrista", alunoAvaliacaoFormacaoCrista);
		Util.addAtributoSessao("alunoAvaliacaoGeografia", alunoAvaliacaoGeografia);
		Util.addAtributoSessao("alunoAvaliacaoHistoria", alunoAvaliacaoHistoria);
		Util.addAtributoSessao("alunoAvaliacaoMatematica", alunoAvaliacaoMatematica);
		Util.addAtributoSessao("alunoAvaliacaoPortugues", alunoAvaliacaoPortugues);
		
		aluno = alunoService.findById(aluno.getId());
		Util.addAtributoSessao("aluno", aluno);
		
		return "cadastrar";
	}

	public String editar() {
		return editar(aluno.getId());
	}

	public String remover(Long idTurma) {
		alunoService.remover(idTurma);
		return "index";
	}
	
	public String restaurar(Long idTurma) {
		alunoService.restaurar(idTurma);
		return "index";
	}

	public String adicionarNovo() {
		return "cadastrar";
	}
	
	public void adicionarHistorico(HistoricoAluno historico){
		historico.setAluno(aluno);
		alunoService.saveHistorico(historico);	
	}
	
	public void removerHistorico(long idHistorico){
		alunoService.removerHistorico(idHistorico);
	}

	public String cadastrarNovo() {

		return "exibirAluno";
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoPortugues() {
		return alunoAvaliacaoPortugues;
	}

	public void setAlunoAvaliacaoPortugues(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoPortugues) {
		this.alunoAvaliacaoPortugues = alunoAvaliacaoPortugues;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoIngles() {
		return alunoAvaliacaoIngles;
	}

	public void setAlunoAvaliacaoIngles(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoIngles) {
		this.alunoAvaliacaoIngles = alunoAvaliacaoIngles;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoMatematica() {
		return alunoAvaliacaoMatematica;
	}

	public void setAlunoAvaliacaoMatematica(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoMatematica) {
		this.alunoAvaliacaoMatematica = alunoAvaliacaoMatematica;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoHistoria() {
		return alunoAvaliacaoHistoria;
	}

	public void setAlunoAvaliacaoHistoria(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoHistoria) {
		this.alunoAvaliacaoHistoria = alunoAvaliacaoHistoria;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoEDFisica() {
		return alunoAvaliacaoEDFisica;
	}

	public void setAlunoAvaliacaoEDFisica(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoEDFisica) {
		this.alunoAvaliacaoEDFisica = alunoAvaliacaoEDFisica;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoGeografia() {
		return alunoAvaliacaoGeografia;
	}

	public void setAlunoAvaliacaoGeografia(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoGeografia) {
		this.alunoAvaliacaoGeografia = alunoAvaliacaoGeografia;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoCiencias() {
		return alunoAvaliacaoCiencias;
	}

	public void setAlunoAvaliacaoCiencias(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoCiencias) {
		this.alunoAvaliacaoCiencias = alunoAvaliacaoCiencias;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoFormacaoCrista() {
		return alunoAvaliacaoFormacaoCrista;
	}

	public void setAlunoAvaliacaoFormacaoCrista(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoFormacaoCrista) {
		this.alunoAvaliacaoFormacaoCrista = alunoAvaliacaoFormacaoCrista;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoArtes() {
		return alunoAvaliacaoArtes;
	}

	public void setAlunoAvaliacaoArtes(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoArtes) {
		this.alunoAvaliacaoArtes = alunoAvaliacaoArtes;
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
	
}

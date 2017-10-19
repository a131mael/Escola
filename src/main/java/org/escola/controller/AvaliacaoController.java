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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.auth.AuthController;
import org.escola.enums.BimestreEnum;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.enums.TipoMembro;
import org.escola.model.Aluno;
import org.escola.model.AlunoAvaliacao;
import org.escola.model.Avaliacao;
import org.escola.model.Member;
import org.escola.model.Turma;
import org.escola.service.AvaliacaoService;
import org.escola.service.ConfiguracaoService;
import org.escola.service.TurmaService;
import org.escola.util.Util;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
@Named
@ViewScoped
public class AvaliacaoController extends AuthController implements Serializable{

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	 private Avaliacao avaliacao;
	
	@Inject
    private AvaliacaoService avaliacaoService;
	
	@Inject
    private ConfiguracaoService configuracaoService;
	
	@Inject
    private TurmaService turmaService;
	
	private LazyDataModel<Avaliacao> lazyListDataModel;
	
	@PostConstruct
	private void init() {
		if(avaliacao == null){
			Object obj = Util.getAtributoSessao("avaliacao");
			if(obj != null){
				avaliacao = (Avaliacao) obj;
			}else{
				avaliacao = new Avaliacao();
			}
		}
	}
	
	public LazyDataModel<Avaliacao> getLazyDataModel() {
		if (lazyListDataModel == null) {

			lazyListDataModel = new LazyDataModel<Avaliacao>() {

				@Override
				public Avaliacao getRowData(String rowKey) {
					return avaliacaoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Avaliacao al) {
					return al.getId();
				}

				@Override
				public List<Avaliacao> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					
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
					
					filtros.put("bimestre", configuracaoService.getConfiguracao().getBimestre());
					filtros.put("professor", getLoggedUser().getProfessor());
					
					/*filtros.put("removido", false);*/

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Avaliacao> ol = avaliacaoService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModel.setRowCount((int) avaliacaoService.count(filtros));
						return ol;
					}

					this.setRowCount((int) avaliacaoService.count(filtros));
					return null;

				}
			};
			lazyListDataModel.setRowCount((int) avaliacaoService.count(null));

		}

		return lazyListDataModel;

	}
	
	public Double getMedia(Avaliacao avaliacao){
		return avaliacaoService.getMedia(avaliacao);
	}
	
	public String salvar(){
		if(avaliacao.getBimestre() == null){
			avaliacao.setBimestre(configuracaoService.getConfiguracao().getBimestre());
		}
		
		if(avaliacao.getSerie() == null){
			avaliacao.setSerie(getSeries().iterator().next());
		}
		
		if(getLoggedUser().getProfessor() != null){
			avaliacaoService.save(avaliacao,getLoggedUser().getProfessor().getId());
		}else{
			avaliacaoService.save(avaliacao, null);
		}
		return "index";
	}
	
	public boolean podeEditar(Avaliacao avaliacao){
		if(getLoggedUser().getTipoMembro().equals(TipoMembro.ADMIM)){
			return true;
		}else {
			if(getLoggedUser().getTipoMembro().equals(TipoMembro.PROFESSOR)){
				return avaliacao.getBimestre().ordinal()>=configuracaoService.getConfiguracao().getBimestre().ordinal();
			}
			return true;
		}	
	}
	
	public boolean exibirSerie(){
		if(getLoggedUser().getTipoMembro().equals(TipoMembro.ADMIM)){
			return true;
		}else {
			if(getLoggedUser().getTipoMembro().equals(TipoMembro.PROFESSOR)){
				Set<Serie> series = getSeries(getLoggedUser());
				if(series.size() > 1){
					return true;
				}else{
					return false;
				}
			}
			return true;
		}
	}
	

	public Set<Serie> getSeries(Member member) {
		List<Turma> turmas = null;
		if(member.getProfessor() != null){
			turmas = turmaService.findAll(member.getProfessor().getId());
		}else{
			turmas = turmaService.findAll();
		}
		Set<Serie> series = new HashSet<>();
		for(Turma t : turmas){
			series.add(t.getSerie());
		}
		return series;
	}
	public Set<Serie> getSeries() {
		
		return getSeries(getLoggedUser());
	}
	
	
	public String salvarAlunoAvaliacao(AlunoAvaliacao alunoAvaliacao){
		avaliacaoService.save(alunoAvaliacao);
		return "index";
	}
	
	
	public String voltar(){
		return "index";
	}
	
	public Set<Avaliacao> getAvaliacoes(){
		
		/*return avaliacaoService.findAll();*/
		return avaliacaoService.findAll(getLoggedUser());
	}
	
	public List<Avaliacao> getAvaliacoes(DisciplinaEnum disciplina, Serie serie,  BimestreEnum bimestre){
		
		return avaliacaoService.findAvaliacaoby(disciplina, bimestre, serie);
	}

	public String editar(Long idprof){
		avaliacao = avaliacaoService.findById(idprof);
		Util.addAtributoSessao("avaliacao", avaliacao);
		return "cadastrar";
	}	
	
	public String remover(Long idTurma){
		avaliacaoService.remover(idTurma);
		return "index";
	}
	
	public String adicionarNovo(){
		Util.removeAtributoSessao("avaliacao");
		return "cadastrar";
	}
	
}

///*
// * JBoss, Home of Professional Open Source
// * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
// * contributors by the @authors tag. See the copyright.txt in the
// * distribution for a full listing of individual contributors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.escola.controller;
//
//import java.io.Serializable;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import javax.annotation.PostConstruct;
//import javax.enterprise.inject.Produces;
//import javax.faces.view.ViewScoped;
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import org.escola.auth.AuthController;
//import org.escola.enums.BimestreEnum;
//import org.escola.enums.DisciplinaEnum;
//import org.escola.enums.Serie;
//import org.escola.enums.TipoMembro;
//import org.escola.model.AlunoAvaliacao;
//import org.escola.model.Avaliacao;
//import org.escola.model.Member;
//import org.escola.model.Turma;
//import org.escola.service.AvaliacaoService;
//import org.escola.service.ConfiguracaoService;
//import org.escola.service.TurmaService;
//import org.escola.util.Util;
//@Named
//@ViewScoped
//public class AvaliacaoControllerOriginal extends AuthController implements Serializable{
//
//	/****/
//	private static final long serialVersionUID = 1L;
//
//	@Produces
//	@Named
//	 private Avaliacao avaliacao;
//	
//	@Inject
//    private AvaliacaoService avaliacaoService;
//	
//	@Inject
//    private ConfiguracaoService configuracaoService;
//	
//	@Inject
//    private TurmaService turmaService;
//	
//	@PostConstruct
//	private void init() {
//		if(avaliacao == null){
//			Object obj = Util.getAtributoSessao("avaliacao");
//			if(obj != null){
//				avaliacao = (Avaliacao) obj;
//			}else{
//				avaliacao = new Avaliacao();
//			}
//		}
//	}
//	
//	public Double getMedia(Avaliacao avaliacao){
//		return avaliacaoService.getMedia(avaliacao);
//	}
//	
//	public String salvar(){
//		if(avaliacao.getBimestre() == null){
//			avaliacao.setBimestre(configuracaoService.getConfiguracao().getBimestre());
//		}
//		
//		if(avaliacao.getSerie() == null){
//			avaliacao.setSerie(getSeries().iterator().next());
//		}
//		
//		if(getLoggedUser().getProfessor() != null){
//			avaliacaoService.save(avaliacao,getLoggedUser().getProfessor().getId());
//		}else{
//			avaliacaoService.save(avaliacao, null);
//		}
//		return "index";
//	}
//	
//	public boolean podeEditar(Avaliacao avaliacao){
//		if(getLoggedUser().getTipoMembro().equals(TipoMembro.ADMIM)){
//			return true;
//		}else {
//			if(getLoggedUser().getTipoMembro().equals(TipoMembro.PROFESSOR)){
//				return avaliacao.getBimestre().ordinal()>=configuracaoService.getConfiguracao().getBimestre().ordinal();
//			}
//			return true;
//		}	
//	}
//	
//	public boolean exibirSerie(){
//		if(getLoggedUser().getTipoMembro().equals(TipoMembro.ADMIM)){
//			return true;
//		}else {
//			if(getLoggedUser().getTipoMembro().equals(TipoMembro.PROFESSOR)){
//				Set<Serie> series = getSeries(getLoggedUser());
//				if(series.size() > 1){
//					return true;
//				}else{
//					return false;
//				}
//			}
//			return true;
//		}
//	}
//	
//
//	public Set<Serie> getSeries(Member member) {
//		List<Turma> turmas = null;
//		if(member.getProfessor() != null){
//			turmas = turmaService.findAll(member.getProfessor().getId());
//		}else{
//			turmas = turmaService.findAll();
//		}
//		Set<Serie> series = new HashSet<>();
//		for(Turma t : turmas){
//			series.add(t.getSerie());
//		}
//		return series;
//	}
//	public Set<Serie> getSeries() {
//		
//		return getSeries(getLoggedUser());
//	}
//	
//	
//	public String salvarAlunoAvaliacao(AlunoAvaliacao alunoAvaliacao){
//		avaliacaoService.save(alunoAvaliacao);
//		return "index";
//	}
//	
//	
//	public String voltar(){
//		return "index";
//	}
//	
//	public Set<Avaliacao> getAvaliacoes(){
//		
//		/*return avaliacaoService.findAll();*/
//		return avaliacaoService.findAll(getLoggedUser());
//	}
//	
//	public List<Avaliacao> getAvaliacoes(DisciplinaEnum disciplina, Serie serie,  BimestreEnum bimestre){
//		
//		return avaliacaoService.findAvaliacaoby(disciplina, bimestre, serie);
//	}
//
//	public String editar(Long idprof){
//		avaliacao = avaliacaoService.findById(idprof);
//		Util.addAtributoSessao("avaliacao", avaliacao);
//		return "cadastrar";
//	}	
//	
//	public String remover(Long idTurma){
//		avaliacaoService.remover(idTurma);
//		return "index";
//	}
//	
//	public String adicionarNovo(){
//		Util.removeAtributoSessao("avaliacao");
//		return "cadastrar";
//	}
//	
//}

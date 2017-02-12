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
package org.escola.controller.professor;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.escola.auth.AuthController;
import org.escola.model.Aluno;
import org.escola.model.Member;
import org.escola.model.Professor;
import org.escola.model.Turma;
import org.escola.service.AlunoService;
import org.escola.service.ProfessorService;
import org.escola.util.Util;

import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class ProfessorController extends AuthController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Professor professor;
	
	@Produces
	@Named
	private List<Professor> professores;
	
	@Inject
    private ProfessorService professorService;
	
	@Inject
    private AlunoService alunoService;

	@PostConstruct
	private void init() {
		if(professor == null){
			Object objectSessao = Util.getAtributoSessao("professor");
			if(objectSessao != null){
				professor = (Professor) objectSessao;
				Util.removeAtributoSessao("professor");
			}else{
				Member m = new Member();
				professor = new Professor();
				professor.setMember(m);
			}
		}
	}

	public List<Professor> getProfessores(){
		
		return professorService.findAll();
	}
	
	public List<Aluno> getAlunosDoProfessor(){
		List<Turma> turmasDosProfessor = professorService.findTurmaByProfessor(getLoggedUser().getId());
		return alunoService.findAlunoTurmaBytTurma(turmasDosProfessor);
	}
	
	public String salvar(){
		professor.setInicio(null);
		professorService.save(professor);
		return "index";
	}
	
	public String voltar(){
		return "index";
	}
	
	public String editar(Long idprof){
		professor = professorService.findById(idprof);
		Util.addAtributoSessao("professor", professor);
		return "cadastrar";
	}	
	
	public String remover(Long idTurma){
		professorService.remover(idTurma);
		return "index";
	}
	
	public String adicionarNovo(){
		return "cadastrar";
	}
	
	public String cadastrarNovo(){
		
		return "exibir";
	}
	
}

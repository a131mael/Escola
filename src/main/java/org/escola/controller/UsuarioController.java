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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.escola.model.Member;
import org.escola.service.UsuarioService;
import org.escola.util.Util;

import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class UsuarioController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Member usuario;
	
	@Inject
    private UsuarioService usuarioService;
	
	@PostConstruct
	private void init() {
		if(usuario == null){
			Object objectSessao = Util.getAtributoSessao("usuario");
			if(objectSessao != null){
				usuario = (Member) objectSessao;
				Util.removeAtributoSessao("usuario");
			}else{
				usuario = new Member();
			}
		}
	}

	public Member getUsuario() {
		return usuario;
	}

	public void setUsuario(Member usuario) {
		this.usuario = usuario;
	}

	public List<Member> getUsuarios(){
		
		return usuarioService.findAll();
	}

	
	public String salvar(){
		usuarioService.save(usuario);
		return "index";
	}
	
	public String voltar(){
		return "index";
	}
	
	public String editar(Long idprof){
		usuario = usuarioService.findById(idprof);
		Util.addAtributoSessao("usuario", usuario);
		return "cadastrar";
	}	
	
	public String remover(Long idTurma){
		usuarioService.remover(idTurma);
		return "index";
	}
	
	public String adicionarNovo(){
		return "cadastrar";
	}
	
	public String cadastrarNovo(){
		
		return "exibir";
	}
	
}

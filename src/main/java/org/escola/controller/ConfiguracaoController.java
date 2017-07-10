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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.model.Configuracao;
import org.escola.service.ConfiguracaoService;

@Named
@ViewScoped
public class ConfiguracaoController implements Serializable{

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Configuracao configuracao;
		
	@Inject
    private ConfiguracaoService configuracaoService;

	
	@PostConstruct
	private void init() {
		List<Configuracao> confs =configuracaoService.findAll(); 
		
		if(confs == null || confs.isEmpty()){
			configuracao = new Configuracao();
		}else{
			configuracao = confs.get(0);
		}
	}

	public String salvar(){
		configuracaoService.save(configuracao);
		return "index";
	}
	
	public Configuracao getConfiguracao() {
		return configuracao;
	}


	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}


}

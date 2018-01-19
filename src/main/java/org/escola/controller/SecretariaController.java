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

import javax.enterprise.inject.Model;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.escola.util.UtilFinalizarAnoLetivo;

@Model
@ViewScoped
public class SecretariaController {

	@Inject
	private UtilFinalizarAnoLetivo finalizarAnoLetivo;

	public void finalizarAnoLetivo() {
		int quantidade = finalizarAnoLetivo.getAlunosAlunoLetivoAtual().size();
		int gerados = 0;
		int quantidadeNoLote = 300;
		int inicio = 0;
		while (inicio <= quantidade) {
			finalizarAnoLetivo.finalizar(inicio,quantidadeNoLote);
			inicio +=quantidadeNoLote +1;
		}
		finalizarAnoLetivo.alterarConfiguracao();
	}

	public String linkAlunos() {
		return "listagemAlunos";
	}

	public String linkProfessores() {
		return "listagemProfessores";
	}

	public String linkCalendario() {
		return "listagemCalendario";
	}

	public String linkTurmas() {
		return "listagemTurmas";
	}

}

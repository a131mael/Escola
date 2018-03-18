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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Model;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.escola.model.Aluno;
import org.escola.service.AlunoService;
import org.escola.service.ConfiguracaoService;
import org.escola.util.CompactadorZip;
import org.escola.util.FileDownload;
import org.escola.util.FileUtils;
import org.escola.util.UtilFinalizarAnoLetivo;
import org.primefaces.model.StreamedContent;

@Model
@ViewScoped
public class SecretariaController {

	@Inject
	private UtilFinalizarAnoLetivo finalizarAnoLetivo;
	
	@Inject
	private ConfiguracaoService configuracaoService;
	
	@Inject
	private AlunoService alunoService;

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

	
	public StreamedContent gerarCNAB240_TodosAlunos() {
		try {
			Map<String,Object> parametros = new HashMap<>();
			parametros.put("removido", false);
			List<Aluno> todosAlunos = alunoService.findAll(parametros);
			long sequencialArquivo = configuracaoService.getSequencialArquivo() ;
			String pasta = ""+System.currentTimeMillis();
			String caminhoFinalPasta = System.getProperty("java.io.tmpdir") + pasta;
			CompactadorZip.createDir(caminhoFinalPasta);
			
			for(Aluno al : todosAlunos){
				String nomeArquivo = caminhoFinalPasta +System.getProperty("file.separator")+ "CNAB240_" + al.getCodigo() + ".txt";
				InputStream stream = FileUtils.gerarCNB240(sequencialArquivo+"", nomeArquivo, al);
				configuracaoService.incrementaSequencialArquivoCNAB();
				sequencialArquivo++;
				
				FileUtils.inputStreamToFile(stream, nomeArquivo);
			}
			
			String arquivoSaida = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+"escolartodasCriancasCNAB.zip";
			CompactadorZip.compactarParaZip(arquivoSaida, caminhoFinalPasta);
			
			InputStream stream2 =  new FileInputStream(arquivoSaida);
			return FileDownload.getContentDoc(stream2, "escolartodasCriancasCNAB.zip");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

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

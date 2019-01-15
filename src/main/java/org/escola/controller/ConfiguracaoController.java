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
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.model.Boleto;
import org.escola.model.Configuracao;
import org.escola.model.ContratoAluno;
import org.escola.service.AlunoService;
import org.escola.service.ConfiguracaoService;
import org.escola.util.CompactadorZip;
import org.escola.util.FileDownload;
import org.escola.util.FileUtils;
import org.escola.util.UtilFinalizarAnoLetivo;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class ConfiguracaoController implements Serializable{

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Configuracao configuracao;
	
	private int mesGerarCNAB;
	private int anohistorico;
	
	@Inject
    private ConfiguracaoService configuracaoService;

	@Inject
	private UtilFinalizarAnoLetivo finalizarAnoLetivo;
	
	@Inject
	private AlunoService alunoService;

	@PostConstruct
	private void init() {
		List<Configuracao> confs =configuracaoService.findAll(); 
		
		if(confs == null || confs.isEmpty()){
			configuracao = new Configuracao();
		}else{
			configuracao = confs.get(0);
		}
	}
	
	public void gerarHistoricoAno(int ano){
		int quantidade = alunoService.findAll().size();
		int quantidadeNoLote = 50;
		int inicio = 0;
		while (inicio <= quantidade) {
			finalizarAnoLetivo.gerarHistorico(inicio,quantidadeNoLote,ano);
			inicio +=quantidadeNoLote +1;
		}
	}
	
	
	
	public StreamedContent gerarCNABDoMES(int mes) {
		try {
			Calendar calendario = Calendar.getInstance();

			StringBuilder sb = new StringBuilder();
			sb.append(calendario.get(Calendar.YEAR));
			sb.append(calendario.get(Calendar.MONTH));
			sb.append(calendario.get(Calendar.DAY_OF_MONTH));

			List<Boleto> boletos = configuracaoService.findBoletosMes(mesGerarCNAB, configuracao.getAnoRematricula());

			String caminhoFinalPasta = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + sb;
			CompactadorZip.createDir(caminhoFinalPasta);

			for (Boleto b : boletos) {
				ContratoAluno ca = configuracaoService.findContrato(b.getId()); 
				
				if(ca.getCpfResponsavel() != null && !ca.getCpfResponsavel().equalsIgnoreCase("")){
					if(ca.getNomeResponsavel() != null && !ca.getNomeResponsavel().equalsIgnoreCase("")){
						if(ca.getEndereco() != null && !ca.getEndereco().equalsIgnoreCase("")){
							if(ca.getCep() == null || ca.getCep().equalsIgnoreCase("")){
								ca.setCep("88132700");	
							}
							if(ca.getBairro() == null || ca.getBairro().equalsIgnoreCase("")){
								ca.setBairro("Bela Vista");
							}
							if(ca.getCidade() == null || ca.getCidade().equalsIgnoreCase("")){
								ca.setCidade("Palhoca");
							}
							
							InputStream stream = gerarCNB240(ca, mesGerarCNAB, caminhoFinalPasta);
							FileUtils.inputStreamToFile(stream, b.getNossoNumero()+"");
						}
					}
				}
			}

			String arquivoSaida = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + sb + "CNAB240.zip";
			CompactadorZip.compactarParaZip(arquivoSaida, caminhoFinalPasta);

			InputStream stream2 = new FileInputStream(arquivoSaida);
			return FileDownload.getContentDoc(stream2, "escolarCNABSdoMes" +sb+".zip");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	public InputStream gerarCNB240(ContratoAluno contrato, int mes, String caminhoArquivo) {
		try {
			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";

			InputStream stream = FileUtils.gerarCNB240(sequencialArquivo, contrato, mes, caminhoArquivo);
			configuracaoService.incrementaSequencialArquivoCNAB();

			return stream;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public int getMesGerarCNAB() {
		return mesGerarCNAB;
	}

	public void setMesGerarCNAB(int mesGerarCNAB) {
		this.mesGerarCNAB = mesGerarCNAB;
	}

	public int getAnohistorico() {
		return anohistorico;
	}

	public void setAnohistorico(int anohistorico) {
		this.anohistorico = anohistorico;
	}


}

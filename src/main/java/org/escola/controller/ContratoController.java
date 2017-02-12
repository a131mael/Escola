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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.escola.model.Contrato;
import org.escola.model.ContratoAdonai;
import org.escola.model.ContratoFretamento;
import org.escola.util.CurrencyWriter;

@Model
@ViewScoped
public class ContratoController {

	@Produces
	@Named
	private Contrato contrato;

	@Produces
	@Named
	private ContratoFretamento contratoFretamento;
	
	@Produces
	@Named
	private ContratoAdonai contratoAdonai;

	private OfficeDOCUtil officeDOCUtil;
	CurrencyWriter cw;

	@PostConstruct
	private void init() {
		contrato = new Contrato();
		contratoFretamento = new ContratoFretamento();
		contratoAdonai = new ContratoAdonai();
		officeDOCUtil = new OfficeDOCUtil();
		cw = new CurrencyWriter();
	}

	public void imprimir() throws IOException {
		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("#CONTRATANTENOME", contrato.getContratanteNome());
		trocas.put("#CONTRATANTERG", contrato.getContratanteRG());
		trocas.put("#CONTRATANTECPF", contrato.getContratanteCPF());
		trocas.put("#CONTRATANTERUA", contrato.getContratanteRua());

		trocas.put("#TRANSPORTADONOME", contrato.getTransportadoNome());
		trocas.put("#TRANSPORTADORUA", contrato.getTransportadoEndereco());
		trocas.put("#TRANSPORTADOESCOLA", contrato.getTransportadoEscola());

		trocas.put("#DADOSGERAISHORARIO1", contrato.getHorario1());
		trocas.put("#DADOSGERAISHORARIO2", contrato.getHorario2());
		trocas.put("#DADOSGERAISMES1", contrato.getMes1());
		trocas.put("#DADOSGERAISMES2", contrato.getMes2());
		trocas.put("#DADOSGERAISPARCELAS", contrato.getParcelas());
		BigDecimal valorTotal = (new BigDecimal(contrato.getValorTotal())).multiply(((new BigDecimal(contrato.getParcelas()))));
		trocas.put("#DADOSGERAISTOTAL", valorTotal.toString());
		contrato.setValorTotal(contrato.getValorTotal().replace(",", "."));
		trocas.put("#DADOSGERAISTOTALEXTENSO", cw.write(valorTotal));
		trocas.put("#DADOSGERAISQTADEPARCELAS", contrato.getParcelas());
		trocas.put("#DADOSGERAISEXTENSOPARCELA", cw.write(new BigDecimal(contrato.getValorTotal())));
		trocas.put("#DADOSGERAISPARCELA", new BigDecimal(contrato.getValorTotal()).toString());/// valor da parcela
		
		trocas.put("#REMOVER", contrato.getTipo());
		
		officeDOCUtil.editDoc("MODELO1-1.doc", trocas, contrato.getContratanteCPF());
		try {

			String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+contrato.getContratanteCPF() + ".doc";
			Process pro = Runtime.getRuntime().exec("cmd.exe /c  " + caminho);
//			pro.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void imprimirContratoFretamento() throws IOException {
		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("#nomecontrante", contratoFretamento.getContratanteNome().toUpperCase()	);
		String nomeContratante = contratoFretamento.getContratanteNome();
		
		if(nomeContratante.length() < 55){
			int espacos = 30 - nomeContratante.length();;
			//nomeContratante = adicionaEspaco(nomeContratante,espacos);
			nomeContratante = adicionaEspacoAtras(nomeContratante, espacos);
		}
		System.out.println("NOMECONTRATANTE :" + nomeContratante +":x");
		trocas.put("#NOMEDOCONTRATANTE", nomeContratante);
		trocas.put("#RGCONTRATANTE", contratoFretamento.getContratanteRG());
		trocas.put("#cpfCONTRATANTE", contratoFretamento.getContratanteCPF());
		trocas.put("#CPFDOCONTRATANTE", adicionaEspacoAtras(contratoFretamento.getContratanteCPF(),10));
		trocas.put("#ENDEREÇOCONTRATANTE", contratoFretamento.getContratanteEndereco().toUpperCase());

		trocas.put("#ORIGEM", contratoFretamento.getOrigem().toUpperCase());
		trocas.put("#DESTINO", contratoFretamento.getDestino().toUpperCase());

		trocas.put("#DATAEHORARIOSAIDA", contratoFretamento.getHorario1());
		trocas.put("#DATADESAIDA", contratoFretamento.getHorario1());
		trocas.put("#DATAEHORARIORETORNO", contratoFretamento.getHorario2());
		
		trocas.put("#VALORINTEGRAL",  "R$ " + contratoFretamento.getValorTotal()+ ",00");
		BigDecimal valorTotal = new BigDecimal(contratoFretamento.getValorTotal());
		BigDecimal trintaPorcento = (valorTotal.multiply(new BigDecimal(0.3))).setScale(0, RoundingMode.UP);
		trocas.put("#30%DOVALOR",  "R$ " + trintaPorcento.toString() + ",00");
		trocas.put("#70%DOVALOR", "R$ " + valorTotal.subtract(trintaPorcento).toString() + ",00");	
		trocas.put("#DADOSGERAISEXTENSOPARCELA", cw.write(valorTotal));
		
		trocas.put("#ONIBUS1", contratoFretamento.getCarro1().toString());
		trocas.put("#ONIBUS2", contratoFretamento.getCarro2().toString());
		trocas.put("#ONIBUS3", contratoFretamento.getCarro3().toString());
		trocas.put("#ONIBUS4", contratoFretamento.getCarro4().toString());
		trocas.put("#ONIBUS5", contratoFretamento.getCarro5().toString());
		
		officeDOCUtil.editDoc("MODELO_CONTRATO_FRETE.doc", trocas, contratoFretamento.getContratanteCPF());
		try {

			String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+contratoFretamento.getContratanteCPF() + ".doc";
			Process pro = Runtime.getRuntime().exec("cmd.exe /c  " + caminho);
//			pro.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String adicionaEspaco(String nomeContratante, int tamanho) {
		StringBuilder sb = new StringBuilder(nomeContratante);
		StringBuilder sbBranco = new StringBuilder();
		for(int i=0; i<tamanho; i++){
			sbBranco.append(" ");
		}
		return sbBranco.append(sb).toString();
	}
	
	private String adicionaEspacoAtras(String nomeContratante, int tamanho) {
		StringBuilder sb = new StringBuilder(nomeContratante);
		StringBuilder sbBranco = new StringBuilder();
		for(int i=0; i<tamanho; i++){
			sbBranco.append(" ");
		}
		return sb.append(sbBranco).toString();
	}

	public void imprimirContratoAdonai() throws IOException {
		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("#NOMERESPOSAVEL", contrato.getContratanteNome());
		trocas.put("#RGRESPONSAVEL", contrato.getContratanteRG());
		trocas.put("#CPFRESPONSAVEL", contrato.getContratanteCPF());
		
		trocas.put("#NF1", contrato.getContratanteRua());
		trocas.put("#NF2", contrato.getContratanteRua());
		trocas.put("#NF3", contrato.getContratanteRua());
		trocas.put("#NF4", contrato.getContratanteRua());
		
		trocas.put("#N1", contrato.getContratanteRua());
		trocas.put("#N2", contrato.getContratanteRua());
		trocas.put("#N3", contrato.getContratanteRua());
		trocas.put("#N4", contrato.getContratanteRua());

		
		trocas.put("#P1", contrato.getContratanteRua());
		trocas.put("#P2", contrato.getContratanteRua());
		trocas.put("#P3", contrato.getContratanteRua());
		
		trocas.put("#ANUIDADE", contrato.getTransportadoNome());
		
		trocas.put("#NUMEROPARCELAS", contrato.getTransportadoNome());
		
		trocas.put("#VALORMENSAL", contrato.getTransportadoEndereco());
		trocas.put("#MESESAPAGAR", contrato.getTransportadoEscola());
		

		trocas.put("#HOJEDIA", contrato.getHorario1());
		trocas.put("#HOJEMES", contrato.getHorario1());
		trocas.put("#HOJEANO", contrato.getHorario1());
		
		officeDOCUtil.editDoc("ModeloAdonai.doc", trocas, contratoFretamento.getContratanteCPF());
		try {

			String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+contratoFretamento.getContratanteCPF() + ".doc";
			Process pro = Runtime.getRuntime().exec("cmd.exe /c  " + caminho);
//			pro.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	
	public String irContratoEscolar(){
		return "contratoEscolar";
	}
	
	public String irContratoFretamento(){
		return "contratoFretamento";
	}
	
	public String irContratoAdonai(){
		return "contratoAdonai";
	}

	public ContratoFretamento getContratoFretamento() {
		return contratoFretamento;
	}

	public void setContratoFretamento(ContratoFretamento contratoFretamento) {
		this.contratoFretamento = contratoFretamento;
	}

}

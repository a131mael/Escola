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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.enums.StatusBoletoEnum;
import org.escola.model.Aluno;
import org.escola.model.Boleto;
import org.escola.service.AlunoService;
import org.escola.service.FinanceiroEscolaService;
import org.escola.util.Formatador;
import org.escola.util.Util;
import org.escola.util.Verificador;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named
@ViewScoped
public class FinanceiroController implements Serializable{

	/****/
	private static final long serialVersionUID = 1L;

	@Inject
	private FinanceiroEscolaService financeiroService;
	
	@Inject
	private AlunoService alunoService;

	private LazyDataModel<Boleto> lazyListDataModel;
		
	private List<Aluno> alunosEcontrados;
	
	private String nomeAluno;
	
	private String nomeResponsavel;
	
	private String cpfResponsavel;
	
	private String nDocumento;

	@Named
	private Aluno alunoBaixaSelecionado;

	@PostConstruct
	private void init() {
	}
	
	public void buscarAluno(String nome, String nomeResponsavel,String cpf, String nDocumento){
		alunosEcontrados = alunoService.findAluno(nome,nomeResponsavel,cpf, nDocumento);	
	}
	
	public void buscarAluno(){
		buscarAluno(nomeAluno, nomeResponsavel, cpfResponsavel, nDocumento);
	}
	
	public List<Boleto> getBoletos(int mes){
		return financeiroService.getBoletoMes(mes);
	}

	public void baixarBoleto(Boleto boleto){
		Double valorFinal = Verificador.getValorFinal(boleto);
		boleto.setValorPago(valorFinal);
		boleto.setDataPagamento(new Date());
		boleto.setBaixaManual(true);
		financeiroService.save(boleto);
	}
	
	public boolean isBoletoPago(Boleto boleto){
		return Verificador.getStatusEnum(boleto).equals(StatusBoletoEnum.PAGO) || isRemovido(boleto);
	}
	
	public boolean isRemovido(Boleto boleto){
		/*if(boleto.getCancelado() != null && boleto.getCancelado()){
			return true;
		}*/
		return false;
	}
	
	
	public String formatarData(Date data){
		return Formatador.formataData(data);	
	}
	
	public String verificaValorFinalBoleto(Boleto boleto){
		return Util.formatarDouble2Decimais(Verificador.getValorFinal(boleto)) ;	
	}
	
	public String verificaJurosMultaFinalBoleto(Boleto boleto){
		return Util.formatarDouble2Decimais(Verificador.getJurosMulta(boleto)) ;	
	}
	
	public LazyDataModel<Boleto> getLazyDataModel(int mes) {
		if (lazyListDataModel == null) {

			lazyListDataModel = new LazyDataModel<Boleto>() {

				@Override
				public Boleto getRowData(String rowKey) {
					return financeiroService.findBoletoByID(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Boleto al) {
					return al.getId();
				}

				@Override
				public List<Boleto> load(int first, int pageSize, String order, SortOrder so,
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
					
					filtros.put("removido", false);

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Boleto> ol = financeiroService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModel.setRowCount((int) financeiroService.count(filtros));
						return ol;
					}

					this.setRowCount((int) financeiroService.count(filtros));
					return null;

				}
			};
			lazyListDataModel.setRowCount((int) financeiroService.count(null));

		}

		return lazyListDataModel;

	}

	public String marcarLinha(Aluno a) {
		String cor = "";
		if(a == null){
			return "";
		}
		
		if(a.getRemovido() != null && a.getRemovido()){
			cor = "marcarLinhaVermelho";
		}else{
			cor = "marcarLinha";
		}
		
		/*cor = "marcarLinhaVermelho";
		cor = "marcarLinhaVerde";
		cor = "marcarLinhaAmarelo";
		cor = "marcarLinha"
		*/
		return cor;
	}
	
	public double getPrevisto(int mes){
		return financeiroService.getPrevisto(mes);
	}
	
	public double getPago(int mes){
		return financeiroService.getPago(mes);
	}

	public double getDiferenca(int mes){
		return getPrevisto(mes) - getPago(mes);
	}


	public List<Aluno> getAlunosEcontrados() {
		return alunosEcontrados;
	}

	public void setAlunosEcontrados(List<Aluno> alunosEcontrados) {
		this.alunosEcontrados = alunosEcontrados;
	}

	public Aluno getAlunoBaixaSelecionado() {
		return alunoBaixaSelecionado;
	}

	public void setAlunoBaixaSelecionado(Aluno alunoBaixaSelecionado) {
		this.alunoBaixaSelecionado = alunoBaixaSelecionado;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getCpfResponsavel() {
		return cpfResponsavel;
	}

	public void setCpfResponsavel(String cpfResponsavel) {
		this.cpfResponsavel = cpfResponsavel;
	}

	public String getnDocumento() {
		return nDocumento;
	}

	public void setnDocumento(String nDocumento) {
		this.nDocumento = nDocumento;
	}
	
}

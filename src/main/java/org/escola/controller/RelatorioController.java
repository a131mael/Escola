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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.model.Configuracao;
import org.escola.service.ConfiguracaoService;
import org.escola.service.FinanceiroEscolaService;
import org.escola.service.RelatorioService;
import org.escola.util.Util;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.escola.service.AlunoService;
import org.escola.model.Aluno;

@Named
@ViewScoped
public class RelatorioController implements Serializable{

	/****/
	private static final long serialVersionUID = 1L;
		
	@Inject
    private RelatorioService relatorioService;
	
	@Inject
	private ConfiguracaoService configuracaoService;
	
	@Inject
	private AlunoService alunoService;
	
	@Inject
	private FinanceiroEscolaService financeiroService;
	
	private LazyDataModel<Aluno> lazyListDataModelQuantidade;
	private LazyDataModel<Aluno> lazyListDataModelMes;
	
	private List<Aluno> devedores = new ArrayList<Aluno>();
	
	private Configuracao configuracao;
	
	private Integer anoSelecionado;
	private int quantidadeAtrasados;
	private int mesAtrasado;
	
	private Aluno aluno;
	
	private Integer mesSelecionadoRelatorio;
	
	@PostConstruct
	private void init() {
		configuracao = configuracaoService.getConfiguracao();
		
		Object obj = Util.getAtributoSessao("aluno");
		if (obj != null) {
		//	setAluno((Aluno) obj);
		}
		Object obj2 = Util.getAtributoSessao("anoSelecionado");
		if (obj2 != null) {
			anoSelecionado = (Integer) obj2;
		}else{
			anoSelecionado = configuracao.getAnoLetivo();
		}

		Object obj3 = Util.getAtributoSessao("mesSelecionadoRelatorio");
		if (obj3 != null) {
			mesSelecionadoRelatorio = (Integer) obj3;
		} else {
			Calendar c = Calendar.getInstance();
			
			mesSelecionadoRelatorio = c.get(Calendar.MONTH)+1;
		}
	}
	
	private Serie maternal;
	private Serie jardimI;
	private Serie jardimII;
	private Serie pre;
	private Serie primeiro;
	private Serie segundo;
	private Serie terceiro;
	private Serie quarto;
	private Serie quinto;
	
	private PerioddoEnum manha;
	private PerioddoEnum tarde;
	private PerioddoEnum integral;
	
	private int mesSelecionado = 1;

	public double getNotasEnviadas(int mes){
		return relatorioService.getTotalNotasEmitidas(mes);
	}
	
	public List<String> getResponsaveisNotasEnviadas(int mes){
		return relatorioService.getResponsaveisNotasEnviadas(mes);
	}
	
	public List<String> getResponsaveisNotasEnviadas(){
		return relatorioService.getResponsaveisNotasEnviadas(mesSelecionado);
	}
	
	public String getTotalCriancasDevendo(int mesBusca) {
		return String.valueOf(financeiroService.getQuantidadeBoletosAtrasadosPorMes(mesBusca,anoSelecionado));
	}
	
	public String getQuantidadeAtrasadosPorQuantidade(int quantidade) {
		return String.valueOf(financeiroService.getQuantidadeBoletosAtrasadosPorQuantidade(quantidade,anoSelecionado));
	}
	
	public String getQuantidadeBoletosAtrasados() {
		return String.valueOf(financeiroService.getQuantidadeBoletosAtrasados(anoSelecionado));
	}
	
	public String getValorTotalBoletosAtrasados() {
		return String.valueOf(financeiroService.getValorTotalAtrasado(anoSelecionado));
	}
	
	public String getValorBoletosAtrasadosPorMes(int mes) {
		
		return String.valueOf(financeiroService.getValorBoletosAtrasadosPorMes(mes,anoSelecionado));
	}
	
	public String getNomeDevedor(int posicao){
		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("quantidadeDevedores", 30);
		filtros.put("anoSelecionado", anoSelecionado);
		if(devedores == null || devedores.isEmpty()){
			devedores = financeiroService.findMaioresDevedores(filtros);
		}
		if(devedores.size() <= posicao){
			return "";
		}
		
		return devedores.get(posicao).getNomeAluno();
	}
	
	public String verDevedor(int posicao) {
		if(devedores != null){
			setAluno(alunoService.findById(devedores.get(posicao).getId()));
			Util.addAtributoSessao("aluno", getAluno());
		}
		return "verdevedor";
	}
	
	public String getValorDevidoDevedor(int posicao){
		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("quantidadeDevedores", 30);
		filtros.put("anoSelecionado", anoSelecionado);
		if(devedores == null || devedores.isEmpty()){
			devedores = financeiroService.findMaioresDevedores(filtros);
		}
		if(devedores.size() <= posicao){
			return "";
		}
		return Util.formatarDouble2Decimais(devedores.get(posicao).getTotalABerto());
	}
	
	
	public long getTotalAlunos(){
		Map<String, Object> filtros = new HashMap<>();
		filtros.put("anoLetivo", configuracao.getAnoLetivo());
		return relatorioService.count(null);
	}

	public long getTotalAlunosManha(){
		Map<String, Object> filtros = new HashMap<>();
		filtros.put("periodo", PerioddoEnum.MANHA);
		filtros.put("anoLetivo", configuracao.getAnoLetivo());
		return relatorioService.count(filtros);
	}
	
	public long getTotalAlunosTarde(){
		Map<String, Object> filtros = new HashMap<>();
		filtros.put("periodo", PerioddoEnum.TARDE);
		filtros.put("anoLetivo", configuracao.getAnoLetivo());
		return relatorioService.count(filtros);
	}
	
	public long getTotalAlunosIntegral(){
		Map<String, Object> filtros = new HashMap<>();
		filtros.put("periodo", PerioddoEnum.INTEGRAL);
		filtros.put("anoLetivo", configuracao.getAnoLetivo());
		return relatorioService.count(filtros);
	}

	public long getTotalAlunos(Serie serie, PerioddoEnum periodo){
		Map<String, Object> filtros = new HashMap<>();
		filtros.put("serie", getSerie(serie));
		filtros.put("periodo", getPeriodo(periodo));
		filtros.put("anoLetivo", configuracao.getAnoLetivo());
		return relatorioService.count(filtros);
	}
	
	public long getTotalAlunos(Serie serie){
		Map<String, Object> filtros = new HashMap<>();
		filtros.put("serie", getSerie(serie));
		filtros.put("anoLetivo", configuracao.getAnoLetivo());
		return relatorioService.count(filtros);
	}
	
	public String rotaMes(int mes){
		setMesAtrasado(mes);
		Util.addAtributoSessao("mesAtrasado", getMesAtrasado());
		Util.addAtributoSessao("anoSelecionado", anoSelecionado);
		return "listagemAlunosAtrasadosMes";
	}
	
	public String rotaQuantidade(int quantidade){
		setQuantidadeAtrasados(quantidade);
		Util.addAtributoSessao("quantidadeAtrasados", getQuantidadeAtrasados());
		Util.addAtributoSessao("anoSelecionado", anoSelecionado);
		return "listagemAlunosAtrasadosQuantidade";
	}

	//TODO nao pergunte =) converter para o server funcionar
	private Serie getSerie(Serie serie){
		return serie;
	}
	//TODO nao pergunte =) converter para o server funcionar
	private PerioddoEnum getPeriodo(PerioddoEnum periodo){
		return periodo;
	}
	public Serie getMaternal() {
		return Serie.MATERNAL;
	}

	public void setMaternal(Serie maternal) {
		this.maternal = maternal;
	}

	public Serie getJardimI() {
		return Serie.JARDIM_I;
	}

	public void setJardimI(Serie jardimI) {
		this.jardimI = jardimI;
	}

	public Serie getJardimII() {
		return Serie.JARDIM_II;
	}

	public void setJardimII(Serie jardimII) {
		this.jardimII = jardimII;
	}

	public Serie getPre() {
		return Serie.PRE;
	}

	public void setPre(Serie pre) {
		this.pre = pre;
	}

	public Serie getPrimeiro() {
		return Serie.PRIMEIRO_ANO;
	}

	public void setPrimeiro(Serie primeiro) {
		this.primeiro = primeiro;
	}

	public Serie getSegundo() {
		return Serie.SEGUNDO_ANO;
	}

	public void setSegundo(Serie segundo) {
		this.segundo = segundo;
	}

	public Serie getTerceiro() {
		return Serie.TERCEIRO_ANO;
	}

	public void setTerceiro(Serie terceiro) {
		this.terceiro = terceiro;
	}

	public Serie getQuarto() {
		return Serie.QUARTO_ANO;
	}

	public void setQuarto(Serie quarto) {
		this.quarto = quarto;
	}

	public Serie getQuinto() {
		return Serie.QUINTO_ANO;
	}

	public void setQuinto(Serie quinto) {
		this.quinto = quinto;
	}

	public PerioddoEnum getManha() {
		return PerioddoEnum.MANHA;
	}

	public void setManha(PerioddoEnum manha) {
		this.manha = manha;
	}

	public PerioddoEnum getTarde() {
		return PerioddoEnum.TARDE;
	}

	public void setTarde(PerioddoEnum tarde) {
		this.tarde = tarde;
	}

	public PerioddoEnum getIntegral() {
		return PerioddoEnum.INTEGRAL;
	}

	public void setIntegral(PerioddoEnum integral) {
		this.integral = integral;
	}

	public int getMesSelecionado() {
		return mesSelecionado;
	}

	public void setMesSelecionado(int mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}

	public Integer getAnoSelecionado() {
		return anoSelecionado;
	}

	public void setAnoSelecionado(Integer anoSelecionado) {
		this.anoSelecionado = anoSelecionado;
	}

	public int getQuantidadeAtrasados() {
		return quantidadeAtrasados;
	}

	public void setQuantidadeAtrasados(int quantidadeAtrasados) {
		this.quantidadeAtrasados = quantidadeAtrasados;
	}

	public int getMesAtrasado() {
		return mesAtrasado;
	}

	public void setMesAtrasado(int mesAtrasado) {
		this.mesAtrasado = mesAtrasado;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	
	public LazyDataModel<Aluno> getLazyDataModelMes() {
		if (lazyListDataModelMes == null) {

			lazyListDataModelMes = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,
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

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";
					
					Object obj = Util.getAtributoSessao("mesAtrasado");
					if (obj != null) {
						mesAtrasado = (int) obj;
					}
					filtros.put("mesAtrasado", mesAtrasado);
					filtros.put("anoSelecionado", anoSelecionado);

					List<Aluno> ol = financeiroService.findAlunoMes(first, pageSize, orderByParam, orderParam, filtros);
					if (ol != null && ol.size() > 0) {
						lazyListDataModelMes.setRowCount(Integer.parseInt(getTotalCriancasDevendo(mesAtrasado)));
						return ol;
					}

					this.setRowCount(Integer.parseInt(getTotalCriancasDevendo(mesAtrasado)));
					return null;
				}
			};
			lazyListDataModelMes.setRowCount(Integer.parseInt(getTotalCriancasDevendo(mesAtrasado)));
		}
		return lazyListDataModelMes;
	}
	
	public LazyDataModel<Aluno> getLazyDataModelQuantidade() {
		if (lazyListDataModelQuantidade == null) {

			lazyListDataModelQuantidade = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so, Map<String, Object> where) {

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

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";
					
					Object obj = Util.getAtributoSessao("quantidadeAtrasados");
					if (obj != null) {
						quantidadeAtrasados = (int) obj;
					}
					filtros.put("quantidadeAtrasados", quantidadeAtrasados);
					filtros.put("anoSelecionado", anoSelecionado);

					List<Aluno> ol = financeiroService.findAlunoQuantidade(first, pageSize, orderByParam, orderParam, filtros);
					if (ol != null && ol.size() > 0) {
						lazyListDataModelQuantidade.setRowCount((Integer.valueOf(getQuantidadeAtrasadosPorQuantidade(quantidadeAtrasados))));
						return ol;
					}

					this.setRowCount(Integer.valueOf(getQuantidadeAtrasadosPorQuantidade(quantidadeAtrasados)));
					return null;
				}
			};
			lazyListDataModelQuantidade.setRowCount(Integer.valueOf(getQuantidadeAtrasadosPorQuantidade(quantidadeAtrasados)));
		}
		return lazyListDataModelQuantidade;
	}
	
	public String editar(Long idprof) {
		setAluno(alunoService.findById(idprof));
		Util.addAtributoSessao("aluno", getAluno());
		return "cadastrar";
	}
	
	public String editar() {
		return editar(getAluno().getId());
	}
	
	public boolean isAlunoSelecionado() {
		if (getAluno() == null) {
			return false;
		}
		return getAluno().getId() != null ? true : false;
	}
	
	public void onRowSelect(SelectEvent event) {
		Aluno al = (Aluno) event.getObject();
		FacesMessage msg = new FacesMessage("Car Selected");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public Integer getMesSelecionadoRelatorio() {
		return mesSelecionadoRelatorio;
	}

	public void setMesSelecionadoRelatorio(Integer mesSelecionadoRelatorio) {
		Util.addAtributoSessao("mesSelecionadoRelatorio", mesSelecionadoRelatorio);
		this.mesSelecionadoRelatorio = mesSelecionadoRelatorio;
	}
}

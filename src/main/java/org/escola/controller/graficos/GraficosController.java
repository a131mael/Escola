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
package org.escola.controller.graficos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.controller.RelatorioController;
import org.escola.enums.BairroEnum;
import org.escola.enums.PerioddoEnum;
import org.escola.model.Aluno;
import org.escola.model.Faturamento;
import org.escola.service.AlunoService;
import org.escola.service.ConfiguracaoService;
import org.escola.service.ExtratoBancarioService;
import org.escola.service.FaturamentoService;
import org.escola.service.FinanceiroEscolaService;
import org.escola.service.TurmaService;
import org.escola.util.Util;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BubbleChartModel;
import org.primefaces.model.chart.BubbleChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.aaf.base.importacao.extrato.ExtratoGruposPagamentoRecebimentoAdonaiEnum;
import br.com.aaf.base.importacao.extrato.ExtratoGruposPagamentoRecebimentoEnum;
import br.com.aaf.base.importacao.extrato.ExtratoTiposEntradaSaidaEnum;

@Named
@ViewScoped
public class GraficosController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LineChartModel lineModel1;
	private BubbleChartModel lineModelAlunosEscola;

	private PieChartModel pieModelAlunosEscola;

	private PieChartModel pieModelAlunosIdade;

	private PieChartModel pieModelAlunosBairro;

	private PieChartModel pieModelAlunosPeriodo;

	private PieChartModel pieModelAlunosTroca;

	private PieChartModel pieModelRecebimentoGruposMes;

	private PieChartModel pieModelGastosGrupoMes;
	
	private String faturamentoAtual;
	
	private String quantidadeContratos;
	
	private String quantidadeAlunos;

	@Inject
	private FaturamentoService faturamentoService;

	@Inject
	private TurmaService carroService;

	@Inject
	private AlunoService alunoService;
	
	@Inject
	private FinanceiroEscolaService financeiroService;
	
	@Inject
	private RelatorioController relatorioController;

	@Inject
	private ConfiguracaoService configuracaoService;
	
	@Inject
	private ExtratoBancarioService extratoBancarioService;

	private double totalReceitaGrupoMes = 0d;
	private double totalGastoGrupoMes = 0d;
	
	
	@PostConstruct
	public void init() {
		createPieModel1AlunoBairro();
		createPieModel1AlunoPeriodo();
		createPieModelRecebimentosGrupoMes();
		createPieModelGastosMesGrupo();
	}
	
	public void remontarGraficosComMes(){
		createPieModelRecebimentosGrupoMes();
		createPieModelGastosMesGrupo();
	}
	
	private void createPieModelRecebimentosGrupoMes() {
		setTotalReceitaGrupoMes(0d);
		pieModelRecebimentoGruposMes = new PieChartModel();
		pieModelRecebimentoGruposMes.setTitle("Recebimento Por Grupo ");
		pieModelRecebimentoGruposMes.setLegendPosition("s");
		pieModelRecebimentoGruposMes.setLegendCols(12);
		pieModelRecebimentoGruposMes.setShadow(false);

		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("ano", getRelatorioController().getAnoSelecionado());
		filtros.put("mes", getRelatorioController().getMesSelecionadoRelatorio());
		filtros.put("tipoEntradaSaida", ExtratoTiposEntradaSaidaEnum.ENTRADA);

		try {
			for (int i = 0; i < ExtratoGruposPagamentoRecebimentoAdonaiEnum.values().length; i++) {
				filtros.put("grupoRecebimento", ExtratoGruposPagamentoRecebimentoAdonaiEnum.values()[i]);
				double quantidade = (double) extratoBancarioService.count(filtros);
				if (quantidade > 0) {
					pieModelRecebimentoGruposMes.set(ExtratoGruposPagamentoRecebimentoAdonaiEnum.values()[i].getNome(),	quantidade);
					setTotalReceitaGrupoMes(getTotalReceitaGrupoMes() + quantidade);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void itemSelect(ItemSelectEvent event) {
		Set<Entry<String, Number>> objetoGrafico = pieModelGastosGrupoMes.getData().entrySet();

		// Set<Map.Entry<String, Double>> objetoGrafico =
		// pieModelGastosGrupoMes.getData().entrySet().toArray()[event.getItemIndex()];
		Entry<String, Number> value = (Entry<String, Number>) objetoGrafico.toArray()[event.getItemIndex()];
		System.out.println(value.getKey());
		System.out.println(value.getValue());
	}

	public List<Entry<String, Number>> getGastosPorGrupo() {
		Set<Entry<String, Number>> objetoGrafico = pieModelGastosGrupoMes.getData().entrySet();
		List<Entry<String, Number>> lista = new ArrayList<Map.Entry<String,Number>>(objetoGrafico);
		Collections.sort(lista, new Comparator<Entry<String, Number>>() {  
            @Override  
            public int compare(Entry<String, Number> p1, Entry<String, Number> p2) {  
            	if((p1.getValue() == p2.getValue())){
            		return 0;
            	}else if(p1.getValue().doubleValue() > p2.getValue().doubleValue()){
            		return -1;
            	}else{
            		return 1;
            	}
            }  
     });  
		
		return lista;
	}

	public List<Entry<String, Number>> getReceitasPorGrupo() {
		Set<Entry<String, Number>> objetoGrafico = pieModelRecebimentoGruposMes.getData().entrySet();
		List<Entry<String, Number>> lista = new ArrayList<Map.Entry<String,Number>>(objetoGrafico);
		Collections.sort(lista, new Comparator<Entry<String, Number>>() {  
            @Override  
            public int compare(Entry<String, Number> p1, Entry<String, Number> p2) {  
            	if((p1.getValue() == p2.getValue())){
            		return 0;
            	}else if(p1.getValue().doubleValue() > p2.getValue().doubleValue()){
            		return -1;
            	}else{
            		return 1;
            	}
            }  
     });  
		
		return lista;
	}
	
	private void createPieModelGastosMesGrupo() {
		setTotalGastoGrupoMes(0D);
		pieModelGastosGrupoMes = new PieChartModel();
		pieModelGastosGrupoMes.setTitle("Gastos Por Grupo ");
		pieModelGastosGrupoMes.setLegendPosition("s");
		pieModelGastosGrupoMes.setLegendCols(12);
		pieModelGastosGrupoMes.setShadow(false);

		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("ano", getRelatorioController().getAnoSelecionado());
		filtros.put("mes", getRelatorioController().getMesSelecionadoRelatorio());
		filtros.put("tipoEntradaSaida", ExtratoTiposEntradaSaidaEnum.SAIDA);

		try {
			for (int i = 0; i < ExtratoGruposPagamentoRecebimentoAdonaiEnum.values().length; i++) {
				filtros.put("grupoRecebimento", ExtratoGruposPagamentoRecebimentoAdonaiEnum.values()[i]);
				double quantidade = (double) extratoBancarioService.count(filtros);
				if (quantidade > 0) {
					pieModelGastosGrupoMes.set(ExtratoGruposPagamentoRecebimentoAdonaiEnum.values()[i].getNome(), quantidade);
					setTotalGastoGrupoMes(getTotalGastoGrupoMes() + quantidade);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createPieModel1AlunoBairro() {
		pieModelAlunosBairro = new PieChartModel();
		pieModelAlunosBairro.setTitle("Crianças por Bairro");
		pieModelAlunosBairro.setLegendPosition("s");
		pieModelAlunosBairro.setLegendCols(12);
		pieModelAlunosBairro.setShadow(false);

		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("anoLetivo", configuracaoService.getConfiguracao().getAnoLetivo());
		filtros.put("removido", false);
		try {
			for (int i = 0; i < BairroEnum.values().length; i++) {
				filtros.put("bairroAluno", BairroEnum.values()[i]);
				int quantidade = (int) alunoService.count(filtros);
				if (quantidade > 0) {
					pieModelAlunosBairro.set(BairroEnum.values()[i].getName(), quantidade);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createPieModel1AlunoPeriodo() {
		pieModelAlunosPeriodo = new PieChartModel();
		pieModelAlunosPeriodo.setTitle("Crianças por Periodo");
		pieModelAlunosPeriodo.setLegendPosition("s");
		pieModelAlunosPeriodo.setLegendCols(12);
		pieModelAlunosPeriodo.setShadow(false);

		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("anoLetivo", configuracaoService.getConfiguracao().getAnoLetivo());
		filtros.put("removido", false);
		try {
			for (int i = 0; i < PerioddoEnum.values().length; i++) {
				filtros.put("periodo", PerioddoEnum.values()[i]);
				int quantidade = (int) alunoService.count(filtros);
				if (quantidade > 0) {
					pieModelAlunosPeriodo.set(PerioddoEnum.values()[i].getName(), quantidade);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createLineModels() {
		lineModel1.setTitle("Faturamento");
		lineModel1.setLegendPosition("e");
		Axis yAxis = lineModel1.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(15000);

		// grafico criancaPor escola bubble
		lineModelAlunosEscola.setTitle("Crianças por Escola");
		lineModelAlunosEscola.getAxis(AxisType.X).setLabel("Escolas");
		Axis yAxis2 = lineModelAlunosEscola.getAxis(AxisType.Y);
		yAxis2.setMin(0);
		yAxis2.setMax(50);
		yAxis2.setLabel("Quantidade");
	}

	public LineChartModel getLineModel1() {
		return lineModel1;
	}

	public void setLineModel1(LineChartModel lineModel1) {
		this.lineModel1 = lineModel1;
	}

	public BubbleChartModel getLineModelAlunosEscola() {
		return lineModelAlunosEscola;
	}

	public void setLineModelAlunosEscola(BubbleChartModel lineModelAlunosEscola) {
		this.lineModelAlunosEscola = lineModelAlunosEscola;
	}

	public String getFaturamentoAtual() {
		Calendar c = Calendar.getInstance();
		return String.valueOf(financeiroService.getPrevisto(c.get(Calendar.MONTH)+1));
	}
	
	public String getFaturamentoAtual(Long mes) {
		return String.valueOf(financeiroService.getPrevisto(mes.intValue()));
	}

	public void setFaturamentoAtual(String faturamentoAtual) {
		this.faturamentoAtual = faturamentoAtual;
	}

	public PieChartModel getPieModelAlunosEscola() {
		return pieModelAlunosEscola;
	}

	public void setPieModelAlunosEscola(PieChartModel pieModelAlunosEscola) {
		this.pieModelAlunosEscola = pieModelAlunosEscola;
	}

	public PieChartModel getPieModelAlunosBairro() {
		return pieModelAlunosBairro;
	}

	public void setPieModelAlunosBairro(PieChartModel pieModelAlunosBairro) {
		this.pieModelAlunosBairro = pieModelAlunosBairro;
	}

	public PieChartModel getPieModelAlunosPeriodo() {
		return pieModelAlunosPeriodo;
	}

	public void setPieModelAlunosPeriodo(PieChartModel pieModelAlunosPeriodo) {
		this.pieModelAlunosPeriodo = pieModelAlunosPeriodo;
	}

	public PieChartModel getPieModelAlunosTroca() {
		return pieModelAlunosTroca;
	}

	public void setPieModelAlunosTroca(PieChartModel pieModelAlunosTroca) {
		this.pieModelAlunosTroca = pieModelAlunosTroca;
	}

	public PieChartModel getPieModelAlunosIdade() {
		return pieModelAlunosIdade;
	}

	public void setPieModelAlunosIdade(PieChartModel pieModelAlunosIdade) {
		this.pieModelAlunosIdade = pieModelAlunosIdade;
	}

	public String getQuantidadeAlunos() {
		return String.valueOf(alunoService.findAlunoDoAnoLetivo().size());
	}

	public void setQuantidadeAlunos(String quantidadeAlunos) {
		this.quantidadeAlunos = quantidadeAlunos;
	}

	public String getQuantidadeContratos() {
		Calendar c = Calendar.getInstance();
		return String.valueOf(financeiroService.countContratos(c.get(Calendar.MONTH)+1));
	}

	public void setQuantidadeContratos(String quantidadeContratos) {
		this.quantidadeContratos = quantidadeContratos;
	}

	public double getTotalReceitaGrupoMes() {
		return totalReceitaGrupoMes;
	}

	public void setTotalReceitaGrupoMes(double totalReceitaGrupoMes) {
		this.totalReceitaGrupoMes = totalReceitaGrupoMes;
	}

	public double getTotalGastoGrupoMes() {
		return totalGastoGrupoMes;
	}

	public void setTotalGastoGrupoMes(double totalGastoGrupoMes) {
		this.totalGastoGrupoMes = totalGastoGrupoMes;
	}

	public RelatorioController getRelatorioController() {
		return relatorioController;
	}

	public void setRelatorioController(RelatorioController relatorioController) {
		this.relatorioController = relatorioController;
	}

}

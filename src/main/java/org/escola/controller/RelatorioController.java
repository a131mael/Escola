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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.model.Configuracao;
import org.escola.service.ConfiguracaoService;
import org.escola.service.RelatorioService;

@Named
@ViewScoped
public class RelatorioController implements Serializable{

	/****/
	private static final long serialVersionUID = 1L;
		
	@Inject
    private RelatorioService relatorioService;
	
	@Inject
	private ConfiguracaoService configuracaoService;
	
	private Configuracao configuracao;
	
	@PostConstruct
	private void init() {
		configuracao = configuracaoService.getConfiguracao();
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
	
	
}

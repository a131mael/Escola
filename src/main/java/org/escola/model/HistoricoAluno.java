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
package org.escola.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class HistoricoAluno implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    /**DADOS DO ALUNO*/
    @Size(min = 1, max = 250)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String nomeAluno;

    @ManyToOne
    private Aluno aluno;
    
    private String escola;
    
    private int ano;
    
    private float notaPortugues;
    
    private float notaMatematica;
    
    private float notaHistoria;
    
    private float notaIngles;
    
    private float notaEdFisica;
    
    private float notaGeografia;
    
    private float notaCiencias;
    
    private float notaformacaoCrista;
    
    private float notaArtes;
    
    
    private Serie serie;

    private PerioddoEnum periodo;

	public String getEscola() {
		return escola;
	}

	public void setEscola(String escola) {
		this.escola = escola;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public float getNotaPortugues() {
		return notaPortugues;
	}

	public void setNotaPortugues(float notaPortugues) {
		this.notaPortugues = notaPortugues;
	}

	public float getNotaMatematica() {
		return notaMatematica;
	}

	public void setNotaMatematica(float notaMatematica) {
		this.notaMatematica = notaMatematica;
	}

	public float getNotaHistoria() {
		return notaHistoria;
	}

	public void setNotaHistoria(float notaHistoria) {
		this.notaHistoria = notaHistoria;
	}

	public float getNotaIngles() {
		return notaIngles;
	}

	public void setNotaIngles(float notaIngles) {
		this.notaIngles = notaIngles;
	}

	public float getNotaEdFisica() {
		return notaEdFisica;
	}

	public void setNotaEdFisica(float notaEdFisica) {
		this.notaEdFisica = notaEdFisica;
	}

	public float getNotaGeografia() {
		return notaGeografia;
	}

	public void setNotaGeografia(float notaGeografia) {
		this.notaGeografia = notaGeografia;
	}

	public float getNotaCiencias() {
		return notaCiencias;
	}

	public void setNotaCiencias(float notaCiencias) {
		this.notaCiencias = notaCiencias;
	}

	public float getNotaformacaoCrista() {
		return notaformacaoCrista;
	}

	public void setNotaformacaoCrista(float notaformacaoCrista) {
		this.notaformacaoCrista = notaformacaoCrista;
	}

	public float getNotaArtes() {
		return notaArtes;
	}

	public void setNotaArtes(float notaArtes) {
		this.notaArtes = notaArtes;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public PerioddoEnum getPeriodo() {
		return periodo;
	}

	public void setPeriodo(PerioddoEnum periodo) {
		this.periodo = periodo;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}
    
}

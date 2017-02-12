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
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.escola.enums.BimestreEnum;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.Serie;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "id") )
public class Avaliacao implements Serializable {

	@Id
	@GeneratedValue(generator = "GENERATE_avaliacao", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "GENERATE_avaliacao", sequenceName = "Avaliacao_pk_seq", allocationSize = 1)
	private Long id;

	/** DADOS DO ALUNO */
	@NotNull
	@Size(min = 1, max = 250)
	private String nome;
	
	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AlunoAvaliacao> avaliacoes;
	
	@ManyToOne
	private Professor professor;
	
	@Column
	private int peso = 1;
	
	@Column
	private DisciplinaEnum disciplina;
	
	@Column
	private Serie serie;
	
	@Column
	private BimestreEnum bimestre;
	
	@Column
	private Date data;
	
	@Override
	public String toString() {
		return nome;
	}
	@Column
	private boolean trabalho;
	@Column
	private boolean prova;
	@Column
	private boolean bimestral;
	@Column
	private boolean recuperacao;
	@Column
	private boolean provaFinal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	public DisciplinaEnum getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(DisciplinaEnum disciplina) {
		this.disciplina = disciplina;
	}

	public BimestreEnum getBimestre() {
		return bimestre;
	}

	public void setBimestre(BimestreEnum bimestre) {
		this.bimestre = bimestre;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public boolean isTrabalho() {
		return trabalho;
	}

	public void setTrabalho(boolean trabalho) {
		this.trabalho = trabalho;
	}

	public boolean isProva() {
		return prova;
	}

	public void setProva(boolean prova) {
		this.prova = prova;
	}

	public boolean isBimestral() {
		return bimestral;
	}

	public void setBimestral(boolean bimestral) {
		this.bimestral = bimestral;
	}

	public boolean isRecuperacao() {
		return recuperacao;
	}

	public void setRecuperacao(boolean recuperacao) {
		this.recuperacao = recuperacao;
	}

	public List<AlunoAvaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(List<AlunoAvaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public boolean isProvaFinal() {
		return provaFinal;
	}

	public void setProvaFinal(boolean provaFinal) {
		this.provaFinal = provaFinal;
	}

}

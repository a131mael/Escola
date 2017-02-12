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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.escola.enums.EspecialidadeEnum;
import org.escola.enums.TipoMembro;


@Entity
public class Professor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "GENERATE_professor", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "GENERATE_professor", sequenceName = "Professor_pk_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    private Member member;
    
    @Column
    private boolean ativo;
    
    @Column
    private String codigo;
    
    @Column
    private EspecialidadeEnum especialidade;
    
    @OneToMany
    private List<Avaliacao> avaliacoes;
    
    @NotNull
    @Size(min = 1, max = 250)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String nome;
    
    private String email;
    
    @Column
    private TipoMembro tipoMembro;

    @Column
    private String telefone1;

    @Column
    private String telefone2;
    
    @Column
    private String telefone3;

    @Column
    private String login ;
    
    @Column
    private Date nascimento;
    
    @Column
    private Date inicio;
    
    @Column
    private String senha ;
    
    @Column
    private String endereco ;
    
    private String salario ;
    
    @Column
    private String cpf ;
    
    @Column
    private String rg ;

    @OneToMany
    private List<ProfessorTurma> turmas;
    
	public List<ProfessorTurma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<ProfessorTurma> turmas) {
		this.turmas = turmas;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public TipoMembro getTipoMembro() {
		return tipoMembro;
	}

	public void setTipoMembro(TipoMembro tipoMembro) {
		this.tipoMembro = tipoMembro;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getTelefone3() {
		return telefone3;
	}

	public void setTelefone3(String telefone3) {
		this.telefone3 = telefone3;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getSalario() {
		return salario;
	}

	public void setSalario(String salario) {
		this.salario = salario;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}    
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Professor other = (Professor) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (tipoMembro == null) {
			if (other.tipoMembro != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;

	}

	public EspecialidadeEnum getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(EspecialidadeEnum especialidade) {
		this.especialidade = especialidade;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(List<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

		
}

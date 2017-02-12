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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.escola.enums.TipoMembro;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email") )
public class Member implements Serializable {

	@Id
	@GeneratedValue(generator = "GENERATE_member", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "GENERATE_member", sequenceName = "Member_pk_seq", allocationSize = 1)
	private Long id;

	@NotNull
	@Size(min = 1, max = 25)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	private String name;

	@Column
	private String email;

	@Column
	private TipoMembro tipoMembro;

	@Column
	private String login;

	@Column
	private String senha;

	@OneToOne(mappedBy = "member")
	private Professor professor;
	
	@OneToOne(mappedBy = "member")
	private Aluno aluno;

	@Column(name = "phone_number")
	private String phoneNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public TipoMembro getTipoMembro() {
		return tipoMembro;
	}

	public void setTipoMembro(TipoMembro tipoMembro) {
		this.tipoMembro = tipoMembro;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
}

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Contrato implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 250)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String contratanteNome;
    
    @NotNull
    @Size(min = 1, max = 20)
    private String contratanteCPF;

    @NotNull
    @Size(min = 1, max = 20)
    private String contratanteRG;
    
    @NotNull
    @Size(min = 1, max = 250)
    private String contratanteRua;
    
    @NotNull
    @Size(min = 1, max = 250)
    private String transportadoNome;
    
    @NotNull
    @Size(min = 1, max = 250)
    private String transportadoEndereco;
    
    @NotNull
    @Size(min = 1, max = 250)
    private String transportadoEscola;
    
    @NotNull
    @Size(min = 1, max = 10)
    private String horario1;
    
    @NotNull
    @Size(min = 1, max = 10)
    private String horario2;
    
    @NotNull
    @Size(min = 1, max = 30)
    private String mes1;
    
    @NotNull
    @Size(min = 1, max = 30)
    private String mes2;
    
    @NotNull
    @Size(min = 1, max = 5)
    private String parcelas;
    
    @NotNull
    @Size(min = 1, max = 20)
    private String valorTotal;
    
    
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @Size(min = 10, max = 12)
    @Digits(fraction = 0, integer = 12)
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Size(min = 1, max = 20)
    private String tipo;
    
    private Date dataCriacao;
    
    public String getContratanteNome() {
		return contratanteNome;
	}

	public void setContratanteNome(String contratanteNome) {
		this.contratanteNome = contratanteNome;
	}

	public String getContratanteCPF() {
		return contratanteCPF;
	}

	public void setContratanteCPF(String contratanteCPF) {
		this.contratanteCPF = contratanteCPF;
	}

	public String getContratanteRG() {
		return contratanteRG;
	}

	public void setContratanteRG(String contratanteRG) {
		this.contratanteRG = contratanteRG;
	}

	public String getContratanteRua() {
		return contratanteRua;
	}

	public void setContratanteRua(String contratanteRua) {
		this.contratanteRua = contratanteRua;
	}

	public String getTransportadoNome() {
		return transportadoNome;
	}

	public void setTransportadoNome(String transportadoNome) {
		this.transportadoNome = transportadoNome;
	}

	public String getTransportadoEndereco() {
		return transportadoEndereco;
	}

	public void setTransportadoEndereco(String transportadoEndereco) {
		this.transportadoEndereco = transportadoEndereco;
	}

	public String getTransportadoEscola() {
		return transportadoEscola;
	}

	public void setTransportadoEscola(String transportadoEscola) {
		this.transportadoEscola = transportadoEscola;
	}

	public String getHorario1() {
		return horario1;
	}

	public void setHorario1(String horario1) {
		this.horario1 = horario1;
	}

	public String getHorario2() {
		return horario2;
	}

	public void setHorario2(String horario2) {
		this.horario2 = horario2;
	}

	public String getMes1() {
		return mes1;
	}

	public void setMes1(String mes1) {
		this.mes1 = mes1;
	}

	public String getMes2() {
		return mes2;
	}

	public void setMes2(String mes2) {
		this.mes2 = mes2;
	}

	public String getParcelas() {
		return parcelas;
	}

	public void setParcelas(String parcelas) {
		this.parcelas = parcelas;
	}

	public String getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}

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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "contratanteCPF"))
public class ContratoFretamento implements Serializable {

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
    private String contratanteEndereco;
    
    @NotNull
    @Size(min = 1, max = 550)
    private String origem;
    
    @NotNull
    @Size(min = 1, max = 550)
    private String destino;
    
    @NotNull
    @Size(min = 1, max = 50)
    private String horario1;
    
    @NotNull
    @Size(min = 1, max = 50)
    private String horario2;
    
    @NotNull
    @Size(min = 1, max = 20)
    private String valorTotal;

    @NotNull
    @Size(min = 1, max = 20)
    private String telefone1;
    
    
    @NotNull
    @Size(min = 1, max = 20)
    private String telefon2;
    
    private Date dataCriacao;
    
    private Boolean carro1;
    
    private Boolean carro2 ;
    
    private Boolean carro3;
    
    private Boolean carro4;
    
    private Boolean carro5;
    
    
    public String getContratanteEndereco() {
		return contratanteEndereco;
	}

	public void setContratanteEndereco(String contratanteEndereco) {
		this.contratanteEndereco = contratanteEndereco;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getTelefon2() {
		return telefon2;
	}

	public void setTelefon2(String telefon2) {
		this.telefon2 = telefon2;
	}

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


	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Boolean getCarro1() {
		return carro1;
	}

	public void setCarro1(Boolean carro1) {
		this.carro1 = carro1;
	}

	public Boolean getCarro2() {
		return carro2;
	}

	public void setCarro2(Boolean carro2) {
		this.carro2 = carro2;
	}

	public Boolean getCarro3() {
		return carro3;
	}

	public void setCarro3(Boolean carro3) {
		this.carro3 = carro3;
	}

	public Boolean getCarro4() {
		return carro4;
	}

	public void setCarro4(Boolean carro4) {
		this.carro4 = carro4;
	}

	public Boolean getCarro5() {
		return carro5;
	}

	public void setCarro5(Boolean carro5) {
		this.carro5 = carro5;
	}
	
}

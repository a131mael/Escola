package org.escola.enums;

public enum Sexo {

	MASCULINO("Masculino"),
	
	FEMININO("Feminino");
	
	
	
	private String tipo;
	
	Sexo(String tipo){
		this.tipo = tipo;
	}

	public String getName() {
		return tipo;
	}

}

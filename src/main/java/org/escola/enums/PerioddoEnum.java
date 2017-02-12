package org.escola.enums;

public enum PerioddoEnum {

	MANHA("Manha"),
	
	TARDE("Tarde"),
	
	INTEGRAL("Integral");
	
	private String tipo;
	
	PerioddoEnum(String tipo){
		this.tipo = tipo;
	}

	public String getName() {
		return tipo;
	}

}

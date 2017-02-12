package org.escola.enums;

public enum EspecialidadeEnum {

	PRINCIPAL("Principal"),
	
	ED_FISICA("Educação Fisica"),
	
	INGLES("Inglês");
	
	
	private String tipo;
	
	EspecialidadeEnum(String tipo){
		this.tipo = tipo;
	}

	public String getName() {
		return tipo;
	}

}

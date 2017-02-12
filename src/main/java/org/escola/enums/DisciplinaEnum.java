package org.escola.enums;

public enum DisciplinaEnum {

	PORTUGUES("Português"),
	
	MATEMATICA("Matemática"),
	
	HISTORIA("História"),
	
	INGLES("Inglês"),
	
	EDUCACAO_FISICA("Educação Física"),
	
	GEOGRAFIA("Geografia"),
	
	CIENCIAS("Ciências"),
	
	FORMACAO_CRISTA("Formação cristã"),
	
	ARTES("Artes");
	
	
	
	private String name;
	
	DisciplinaEnum(String name){
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

}

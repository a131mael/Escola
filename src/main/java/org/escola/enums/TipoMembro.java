package org.escola.enums;

public enum TipoMembro {

	SECRETARIA("Secretaria"),
	
	PROFESSOR("Professor"),
	
	ADMIM("Administrador"),
	
	ALUNO("Aluno");
	
	private String tipo;
	
	TipoMembro(String tipo){
		this.tipo = tipo;
	}

	public String getName() {
		
		return tipo;
	}

}

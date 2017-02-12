package org.escola.enums;

public enum TipoEventoEnum {

	ANIVERSARIO("Aniversário"),
	
	FERIADO("Feriado"),
	
	FESTA("Festa"),
	
	PAGAMENTO("Pagamento"),
	
	RECESSO("RECESSO"),

	OUTRO("Outro");
	
	
	private String tipo;
	
	TipoEventoEnum(String tipo){
		this.tipo = tipo;
	}

	public String getName() {
		return tipo;
	}

}

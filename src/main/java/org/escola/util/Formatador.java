package org.escola.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatador {

	public static String formataData(Date data){
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(data);
	}
	
	public static Date formatDateSomenteDiaMesAno(Date data){
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date dataFormatada = null;
		try {
			dataFormatada = formato.parse(formataData(data));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataFormatada;
	}
	
	public static String valorFormatado(Double valorF){
		BigDecimal valor = new BigDecimal (valorF);  
		NumberFormat nf = NumberFormat.getCurrencyInstance();  
		String formatado = nf.format (valor);
		return formatado;
	}
	
}

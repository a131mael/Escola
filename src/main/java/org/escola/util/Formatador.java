package org.escola.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.aaf.financeiro.util.OfficeUtil;
import org.escola.model.Boleto;

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
	
	public static CharSequence getMes(Date vencimento) {
		Calendar c = Calendar.getInstance();
		int mes = c.get(Calendar.MONTH);
		String mesString = "";
		switch (mes) {
		case 0:
			mesString = "Janeiro";
			break;

		case 1:
			mesString = "Fevereiro";
			break;

		case 2:
			mesString = "Março";
			break;

		case 3:
			mesString = "Abril";
			break;

		case 4:
			mesString = "Maio";
			break;

		case 5:
			mesString = "Junho";
			break;

		case 6:
			mesString = "Julho";
			break;

		case 7:
			mesString = "Agosto";
			break;

		case 8:
			mesString = "Setembro";
			break;
		case 9:
			mesString = "Outubro";
			break;
		case 10:
			mesString = "Novembro";
			break;
		case 11:
			mesString = "Dezembro";
			break;

		default:
			break;
		}

		return mesString;
	}

	public static List<org.aaf.financeiro.model.Boleto> getBoletosFinanceiro(List<Boleto> boletos) {
		List<org.aaf.financeiro.model.Boleto> boletosFinanceiro = new ArrayList<>();
		if(boletos!= null){
			for(Boleto boleto : boletos){
				org.aaf.financeiro.model.Boleto boletoFinanceiro = new org.aaf.financeiro.model.Boleto();
				boletoFinanceiro.setEmissao(boleto.getEmissao());
				boletoFinanceiro.setId(boleto.getId());
				boletoFinanceiro.setValorNominal(boleto.getValorNominal());
				boletoFinanceiro.setVencimento(boleto.getVencimento());
				boletoFinanceiro.setNossoNumero(String.valueOf(boleto.getNossoNumero()));
				boletoFinanceiro.setDataPagamento(OfficeUtil.retornaDataSomenteNumeros(boleto.getDataPagamento()));
				boletoFinanceiro.setValorPago(boleto.getValorPago());
				boletosFinanceiro.add(boletoFinanceiro);
			}
		}
		return boletosFinanceiro;
	}
	
}

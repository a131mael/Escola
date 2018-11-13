package org.escola.util;

import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;

import org.escola.enums.StatusBoletoEnum;
import org.escola.model.Boleto;

public class Verificador {

	public static Double getValorFinal(Boleto boleto) {
		if(getStatusEnum(boleto).equals(StatusBoletoEnum.ATRASADO)){
			return boleto.getValorNominal() + getJurosMulta(boleto);
		}else{
			return boleto.getValorNominal() - getDesconto(boleto);
		}
	}

	public static Double getDesconto(Boleto boleto) {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 1);
		if (boleto.getVencimento().compareTo(tomorrow.getTime()) == 1) {
			return 20d;
		} else {
			return 0d;
		}

	}

	public static String getDescontoString(Boleto boleto) {
		return Util.formatarDouble2Decimais(getDesconto(boleto));
	}

	public static Double getJurosMulta(org.escola.model.Boleto boleto) {
		if(getStatusEnum(boleto).equals(StatusBoletoEnum.CANCELADO) ||  getStatusEnum(boleto).equals(StatusBoletoEnum.PAGO) ){
			return 0D;
			
			}else{
				Calendar tomorrow = Calendar.getInstance();
				tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 1);
				double multa = boleto.getValorNominal() * 0.02;
				long diasVencimento = Util.diferencaEntreDatas(tomorrow.getTime(), boleto.getVencimento());
				if (diasVencimento > 0) {
					double juros = (diasVencimento / 2); // juros de 50 centavos
					return multa + juros;

				} else {
					return 0D;
				}

		}
	
	}

	public static String getStatus(org.escola.model.Boleto boleto) {
		return getStatusEnum(boleto).getName();
	}
	
	public static StatusBoletoEnum getStatusEnum(org.escola.model.Boleto boleto) {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) );
		tomorrow.set(Calendar.HOUR, 0);
		tomorrow.set(Calendar.MINUTE, 0);
		
		Date vencimento = Formatador.formatDateSomenteDiaMesAno(boleto.getVencimento());
		Date amanha = Formatador.formatDateSomenteDiaMesAno(tomorrow.getTime());
		long diasVencimento = Util.diferencaEntreDatas(amanha, vencimento);
		
		if (pago(boleto)) {
			return StatusBoletoEnum.PAGO;
		}else if(baixado(boleto) || cancelado(boleto)){	
			return StatusBoletoEnum.CANCELADO;
		} else if (diasVencimento > 3) {
			return StatusBoletoEnum.ATRASADO;
		} else if (diasVencimento == 0) {
			return StatusBoletoEnum.VENCE_HOJE;
		} else {
			return StatusBoletoEnum.A_VENCER;
		}
	}

	
	
	private static boolean pago(org.escola.model.Boleto boleto) {
		if (boleto.getDataPagamento() != null) {
			Date vencimento = Formatador.formatDateSomenteDiaMesAno(boleto.getVencimento());
			Date pagamento = Formatador.formatDateSomenteDiaMesAno(boleto.getDataPagamento());
			if (vencimento.after(pagamento) || vencimento.equals(pagamento)) {
				if (boleto.getValorNominal() -20 <= (boleto.getValorPago())) {
					return true;
				}
			}else if(boleto.getValorNominal() -20 <= boleto.getValorPago()){
				return true;
			}
		}
		return false;
	}
	
	private static boolean baixado(org.escola.model.Boleto boleto) {
		if(boleto.getBaixaGerada() != null && boleto.getBaixaGerada()){
			return true;
		}
		if(boleto.getBaixaManual() != null && boleto.getBaixaManual()){
			return true;
		}
		return false;
	}
	
	private static boolean cancelado(org.escola.model.Boleto boleto) {
		if(boleto.getCancelado() != null && boleto.getCancelado()){
			return true;
		}
		return false;
	}

	public static boolean isCPF(String CPF) {
		if(CPF == null){
			return false;
		}
		if(CPF.length() != 11){
			return false;
		}
		
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222")
				|| CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
				|| CPF.equals("99999999999") || (CPF.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48); // converte no respectivo caractere
											// numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			// Verifica se os digitos calculados conferem com os digitos
			// informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}
}

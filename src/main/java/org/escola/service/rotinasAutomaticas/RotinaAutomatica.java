/*
d * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.escola.service.rotinasAutomaticas;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class RotinaAutomatica {

	@Inject
	private CNAB240 cnab240;


	// Rotina que roda as 5:30 minutos nos domingos e na quinta
	@Schedule(minute = "30", hour = "5", dayOfWeek="Sun,Thu", month="*", persistent = false)
	public void automaticTimeout() {
		
		/*try {
			System.out.println("Gerando CNAB DE ALUNOS AINDA NAO ENVIADOs Escolar");
			cnab240.gerarCNABAlunos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			cnab240.gerarBaixaBoletosPagos();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	@Schedule(minute = "*/5", hour = "*", month="*", persistent = false)
	public void gerarArquivos() {
		try {
			System.out.println("Gerando Arquivo de Baixa Cancelados ");
			cnab240.gerarBaixaBoletoAlunosCancelados();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void enviarSPC() {
		/*try {
			System.out.println("ENVIANDO SPC ");
			//ClienteWebServiceSPC clienteWebServiceSPC = new ClienteWebServiceSPC();
			//clienteWebServiceSPC.enviarSPC();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void importarCNABPagmentos() {
		/*try {
			System.out.println("Gerando Arquivo de Baixa Pagos ");
			cnab240.importarPagamentosCNAB240();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	@Schedule(minute = "4", hour = "15", dayOfWeek="Tue", month="*", persistent = false)
	public void enviandoEmailBoletoAtrasado() {
		/*new Thread() {
			  @Override
		        public void run() {
				  try {
						System.out.println("Enviando Email boleto Atrasado ");
						EnviadorEmail email = new EnviadorEmail();
						email.enviarEmailBoletoAtrasado(CONSTANTES.emailFinanceiro,CONSTANTES.senhaEmailFinanceiro);
					} catch (Exception e) {
					//	e.printStackTrace();
					}	  
			  }
			
		}.start();*/
	}

	// Rotina que executa as 3:30 da manha.
	@Schedule(minute = "30", hour = "3",dayOfMonth="2", month="*" , persistent = false)
	public void enviarEmail() {
		/*new Thread() {
			  @Override
		        public void run() {
				  System.out.println("Enviando email do mes atual ");
					EnviadorEmail email = new EnviadorEmail();
					email.enviarEmailBoletosMesAtual();		  
			  }
		}.start();*/
	}
}

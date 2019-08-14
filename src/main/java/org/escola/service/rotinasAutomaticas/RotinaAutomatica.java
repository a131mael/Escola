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
//
//
//	// Rotina que roda as 5:30 minutos nos domingos e na quinta
//	public void automaticTimeout() {
//		
//		try {
//			//System.out.println("Gerando CNAB DE ALUNOS AINDA NAO ENVIADOs Escola");
//		//	cnab240.gerarCNABAlunos();
//			//cnab240.gerarCNABAlunos(3);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			//cnab240.gerarBaixaBoletosPagos();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
/*	public void gerarArquivos() {
		try {
			System.out.println("Gerando Arquivo de Baixa Cancelados ");
			cnab240.gerarBaixaBoletoAlunosCancelados();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
//	
//	public void enviarSPC() {
//		/*try {
//			System.out.println("ENVIANDO SPC ");
//			//ClienteWebServiceSPC clienteWebServiceSPC = new ClienteWebServiceSPC();
//			//clienteWebServiceSPC.enviarSPC();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}*/
//	}
//	
//	
//	public void importarCNABPagmentos() {
//		/*try {
//			System.out.println("Gerando Arquivo de Baixa Pagos ");
//			cnab240.importarPagamentosCNAB240();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}*/
//	}
//	
//	public void enviandoEmailBoletoAtrasado() {
//		/*new Thread() {
//			  @Override
//		        public void run() {
//				  try {
//						System.out.println("Enviando Email boleto Atrasado ");
//						EnviadorEmail email = new EnviadorEmail();
//						email.enviarEmailBoletoAtrasado(CONSTANTES.emailFinanceiro,CONSTANTES.senhaEmailFinanceiro);
//					} catch (Exception e) {
//					//	e.printStackTrace();
//					}	  
//			  }
//			
//		}.start();*/
//	}
//
//	// Rotina que executa as 3:30 da manha.
//	public void enviarEmail() {
//		new Thread() {
//			  @Override
//		        public void run() {
//				  System.out.println("Enviando email do mes atual ");
//			//		EnviadorEmail email = new EnviadorEmail();
//			//		email.enviarEmailBoletosMesAtual();		  
//			  }
//		}.start();
//	}*/
}

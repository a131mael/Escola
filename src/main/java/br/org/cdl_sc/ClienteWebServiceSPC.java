package br.org.cdl_sc;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;

import javax.ejb.Stateless;

import br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS;
import br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor;
import br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorContato;
import br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco;
import br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSPCWS;
import br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault;
import br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSService_PortType;
import br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSService_ServiceLocator;

@Stateless
public class ClienteWebServiceSPC {

	public void enviarSPC() {
		try {

			RegistroSpcScWSService_ServiceLocator locator = new RegistroSpcScWSService_ServiceLocator();
			
			RegistroSpcScWSService_PortType proxy = locator.getRegistroSpcSc();
			Calendar dataCompra = Calendar.getInstance();
			Calendar dataVencimento = Calendar.getInstance();
			
			ConsumidorEndereco consumidorEndereco = new ConsumidorEndereco();
			consumidorEndereco.setBairro("Bela Vista");
			consumidorEndereco.setCep("88132700");
			consumidorEndereco.setCidade("Palhoça");
			consumidorEndereco.setUf("sc");
			consumidorEndereco.setNumero(2001);
			consumidorEndereco.setLogradouro("Rua jose cosme pamplona ");
			consumidorEndereco.setComplemento("casa");
			
			ConsumidorContato consumidorContato = new ConsumidorContato();
			consumidorContato.setDddCelular("48");
			consumidorContato.setDddFixo("48");
			consumidorContato.setNumeroCelular("999484089");
			consumidorContato.setNumeroFixo("32424194");

			Consumidor consumidor = new Consumidor();
			consumidor.setCpfCnpj("06660604952");
			consumidor.setNome("Abimael Aldevino Fidencio");
			consumidor.setSexo("Masculino");
			consumidor.setNomeMae("Marlete Maria da Silva Fidencio");
			consumidor.setConsumidorEndereco(consumidorEndereco);
			consumidor.setConsumidorContato(consumidorContato);
			
			
			RegistroSPCWS registroSPCWS = new RegistroSPCWS();
			registroSPCWS.setConsumidor(consumidor);
			registroSPCWS.setMotivoRegistro("Comprador");
			registroSPCWS.setMotivoRegistro("Comprador");
			registroSPCWS.setValor(new BigDecimal("2"));
			registroSPCWS.setCampanha(false);
			registroSPCWS.setContrato("12345");
			registroSPCWS.setDataCompra(dataCompra);
			registroSPCWS.setDataVencimento(dataVencimento);
			registroSPCWS.setParcela(1);

			ConfirmacaoRegistroWS confirmacaoRegistroWS = proxy.cadastrarRegistroSPC(registroSPCWS);
			System.out.println(confirmacaoRegistroWS.getMensagem());
		} catch (RegistroSpcScWSFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}

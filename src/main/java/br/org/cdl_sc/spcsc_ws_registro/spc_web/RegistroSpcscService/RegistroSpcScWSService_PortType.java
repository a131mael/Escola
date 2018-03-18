/**
 * RegistroSpcScWSService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public interface RegistroSpcScWSService_PortType extends java.rmi.Remote {
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS cadastrarRegistroCheque(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroChequeWS registroChequeWS) throws java.rmi.RemoteException, br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault;
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS cadastrarRegistroSPC(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSPCWS registroSPCWS) throws java.rmi.RemoteException, br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault;
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS cancelarRegistroSPC(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.CancelamentoSPCWS cancelamentoSPCWS) throws java.rmi.RemoteException, br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault;
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroGeralWS[] solicitarRegistrosAtivo(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.FiltroRegistrosAtivoWS filtroRegistrosAtivoWS) throws java.rmi.RemoteException, br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault;
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS cancelarRegistroCheque(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.CancelamentoChequeWS cancelamentoChequeWS) throws java.rmi.RemoteException, br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault;
}

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class RegistroSpcScWSServiceProxy implements RegistroSpcScWSService_PortType {
  private String _endpoint = null;
  private RegistroSpcScWSService_PortType registroSpcScWSService_PortType = null;
  
  public RegistroSpcScWSServiceProxy() {
    _initRegistroSpcScWSServiceProxy();
  }
  
  public RegistroSpcScWSServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initRegistroSpcScWSServiceProxy();
  }
  
  private void _initRegistroSpcScWSServiceProxy() {
    try {
      registroSpcScWSService_PortType = (new RegistroSpcScWSService_ServiceLocator()).getRegistroSpcSc();
      if (registroSpcScWSService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)registroSpcScWSService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)registroSpcScWSService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (registroSpcScWSService_PortType != null)
      ((javax.xml.rpc.Stub)registroSpcScWSService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public RegistroSpcScWSService_PortType getRegistroSpcScWSService_PortType() {
    if (registroSpcScWSService_PortType == null)
      _initRegistroSpcScWSServiceProxy();
    return registroSpcScWSService_PortType;
  }
  
  public ConfirmacaoRegistroWS cadastrarRegistroCheque(RegistroChequeWS registroChequeWS) throws java.rmi.RemoteException,RegistroSpcScWSFault{
    if (registroSpcScWSService_PortType == null)
      _initRegistroSpcScWSServiceProxy();
    return registroSpcScWSService_PortType.cadastrarRegistroCheque(registroChequeWS);
  }
  
  public ConfirmacaoRegistroWS cadastrarRegistroSPC(RegistroSPCWS registroSPCWS) throws java.rmi.RemoteException,RegistroSpcScWSFault{
    if (registroSpcScWSService_PortType == null)
      _initRegistroSpcScWSServiceProxy();
    return registroSpcScWSService_PortType.cadastrarRegistroSPC(registroSPCWS);
  }
  
  public ConfirmacaoCancelamentoRegistroWS cancelarRegistroSPC(CancelamentoSPCWS cancelamentoSPCWS) throws java.rmi.RemoteException,RegistroSpcScWSFault{
    if (registroSpcScWSService_PortType == null)
      _initRegistroSpcScWSServiceProxy();
    return registroSpcScWSService_PortType.cancelarRegistroSPC(cancelamentoSPCWS);
  }
  
  public RegistroGeralWS[] solicitarRegistrosAtivo(FiltroRegistrosAtivoWS filtroRegistrosAtivoWS) throws java.rmi.RemoteException,RegistroSpcScWSFault{
    if (registroSpcScWSService_PortType == null)
      _initRegistroSpcScWSServiceProxy();
    return registroSpcScWSService_PortType.solicitarRegistrosAtivo(filtroRegistrosAtivoWS);
  }
  
  public ConfirmacaoCancelamentoRegistroWS cancelarRegistroCheque(CancelamentoChequeWS cancelamentoChequeWS) throws java.rmi.RemoteException,RegistroSpcScWSFault{
    if (registroSpcScWSService_PortType == null)
      _initRegistroSpcScWSServiceProxy();
    return registroSpcScWSService_PortType.cancelarRegistroCheque(cancelamentoChequeWS);
  }
  
  
}
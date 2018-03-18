/**
 * RegistroSpcScWSService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class RegistroSpcScWSService_ServiceLocator extends org.apache.axis.client.Service implements RegistroSpcScWSService_Service {

    public RegistroSpcScWSService_ServiceLocator() {
    }


    public RegistroSpcScWSService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RegistroSpcScWSService_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RegistroSpcSc
    private java.lang.String RegistroSpcSc_address = "https://spcscpreproducao-ws.cdl-sc.org.br/spc-web/RegistroSpcScWSService";

    public java.lang.String getRegistroSpcScAddress() {
        return RegistroSpcSc_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegistroSpcScWSDDServiceName = "RegistroSpcSc";

    public java.lang.String getRegistroSpcScWSDDServiceName() {
        return RegistroSpcScWSDDServiceName;
    }

    public void setRegistroSpcScWSDDServiceName(java.lang.String name) {
        RegistroSpcScWSDDServiceName = name;
    }

    public RegistroSpcScWSService_PortType getRegistroSpcSc() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegistroSpcSc_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegistroSpcSc(endpoint);
    }

    public RegistroSpcScWSService_PortType getRegistroSpcSc(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            RegistroSpcScWSServiceSoapBindingStub _stub = new RegistroSpcScWSServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getRegistroSpcScWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRegistroSpcScEndpointAddress(java.lang.String address) {
        RegistroSpcSc_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (RegistroSpcScWSService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                RegistroSpcScWSServiceSoapBindingStub _stub = new RegistroSpcScWSServiceSoapBindingStub(new java.net.URL(RegistroSpcSc_address), this);
                _stub.setPortName(getRegistroSpcScWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("RegistroSpcSc".equals(inputPortName)) {
            return getRegistroSpcSc();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "RegistroSpcScWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "RegistroSpcSc"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RegistroSpcSc".equals(portName)) {
            setRegistroSpcScEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

/**
 * RegistroSpcScWSServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class RegistroSpcScWSServiceSoapBindingStub extends org.apache.axis.client.Stub
		implements br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSService_PortType {
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector cachedSerQNames = new java.util.Vector();
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	static org.apache.axis.description.OperationDesc[] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[5];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("CadastrarRegistroCheque");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "registroChequeWS"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"registroChequeWS"),
				br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroChequeWS.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "confirmacaoRegistroWS"));
		oper.setReturnClass(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "resultado"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				"br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault",
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				true));
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("CadastrarRegistroSPC");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "registroSPCWS"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"registroSPCWS"),
				br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSPCWS.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "confirmacaoRegistroWS"));
		oper.setReturnClass(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "resultado"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				"br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault",
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				true));
		_operations[1] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("CancelarRegistroSPC");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "cancelamentoSPCWS"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"cancelamentoSPCWS"),
				br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.CancelamentoSPCWS.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"confirmacaoCancelamentoRegistroWS"));
		oper.setReturnClass(
				br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "resultado"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				"br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault",
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				true));
		_operations[2] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("SolicitarRegistrosAtivo");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName("", "filtroRegistrosAtivoWS"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"filtroRegistrosAtivoWS"),
				br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.FiltroRegistrosAtivoWS.class, false,
				false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "registroGeralWS"));
		oper.setReturnClass(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroGeralWS[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "resultado"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				"br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault",
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				true));
		_operations[3] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("CancelarRegistroCheque");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "cancelamentoChequeWS"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"cancelamentoChequeWS"),
				br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.CancelamentoChequeWS.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"confirmacaoCancelamentoRegistroWS"));
		oper.setReturnClass(
				br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "resultado"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				"br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault",
				new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
						"RegistroSpcScWSFault"),
				true));
		_operations[4] = oper;

	}

	public RegistroSpcScWSServiceSoapBindingStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public RegistroSpcScWSServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service)
			throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public RegistroSpcScWSServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
		java.lang.Class cls;
		javax.xml.namespace.QName qName;
		javax.xml.namespace.QName qName2;
		java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
		java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
		java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
		java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
		java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
		java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
		java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
		java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
		java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
		java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"cancelamentoChequeWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.CancelamentoChequeWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"cancelamentoSPCWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.CancelamentoSPCWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"confirmacaoCancelamentoRegistroWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"confirmacaoRegistroWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"consumidor");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"consumidorContato");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorContato.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"consumidorEndereco");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"filtroRegistrosAtivoWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.FiltroRegistrosAtivoWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"protestoDocumentoWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoDocumentoWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"protestoWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"registroBoletoWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroBoletoWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"registroChequeWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroChequeWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"registroGeralWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroGeralWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"RegistroSpcScWSFault");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService",
				"registroSPCWS");
		cachedSerQNames.add(qName);
		cls = br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSPCWS.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

	}

	protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
		try {
			org.apache.axis.client.Call _call = super._createCall();
			_call.setUsername("1355");
			_call.setPassword("AD3210");
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			java.util.Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				java.lang.String key = (java.lang.String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setEncodingStyle(null);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
						javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames.get(i);
						java.lang.Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							java.lang.Class sf = (java.lang.Class) cachedSerFactories.get(i);
							java.lang.Class df = (java.lang.Class) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories
									.get(i);
							org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories
									.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (java.lang.Throwable _t) {
			throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
		}
	}

	public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS cadastrarRegistroCheque(
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroChequeWS registroChequeWS)
			throws java.rmi.RemoteException,
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "CadastrarRegistroCheque"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { registroChequeWS });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS) _resp;
				} catch (java.lang.Exception _exception) {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS) org.apache.axis.utils.JavaUtils
							.convert(_resp,
									br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) {
					throw (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS cadastrarRegistroSPC(
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSPCWS registroSPCWS)
			throws java.rmi.RemoteException,
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "CadastrarRegistroSPC"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { registroSPCWS });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS) _resp;
				} catch (java.lang.Exception _exception) {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS) org.apache.axis.utils.JavaUtils
							.convert(_resp,
									br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoRegistroWS.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) {
					throw (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS cancelarRegistroSPC(
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.CancelamentoSPCWS cancelamentoSPCWS)
			throws java.rmi.RemoteException,
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "CancelarRegistroSPC"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { cancelamentoSPCWS });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS) _resp;
				} catch (java.lang.Exception _exception) {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS) org.apache.axis.utils.JavaUtils
							.convert(_resp,
									br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) {
					throw (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroGeralWS[] solicitarRegistrosAtivo(
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.FiltroRegistrosAtivoWS filtroRegistrosAtivoWS)
			throws java.rmi.RemoteException,
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[3]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "SolicitarRegistrosAtivo"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { filtroRegistrosAtivoWS });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroGeralWS[]) _resp;
				} catch (java.lang.Exception _exception) {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroGeralWS[]) org.apache.axis.utils.JavaUtils
							.convert(_resp,
									br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroGeralWS[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) {
					throw (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS cancelarRegistroCheque(
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.CancelamentoChequeWS cancelamentoChequeWS)
			throws java.rmi.RemoteException,
			br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[4]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "CancelarRegistroCheque"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { cancelamentoChequeWS });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS) _resp;
				} catch (java.lang.Exception _exception) {
					return (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS) org.apache.axis.utils.JavaUtils
							.convert(_resp,
									br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConfirmacaoCancelamentoRegistroWS.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) {
					throw (br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroSpcScWSFault) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

}

/**
 * CancelamentoSPCWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class CancelamentoSPCWS  implements java.io.Serializable {
    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor;

    private java.lang.String contrato;

    private int parcela;

    private java.lang.String motivoCancelamento;

    public CancelamentoSPCWS() {
    }

    public CancelamentoSPCWS(
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor,
           java.lang.String contrato,
           int parcela,
           java.lang.String motivoCancelamento) {
           this.consumidor = consumidor;
           this.contrato = contrato;
           this.parcela = parcela;
           this.motivoCancelamento = motivoCancelamento;
    }


    /**
     * Gets the consumidor value for this CancelamentoSPCWS.
     * 
     * @return consumidor
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor getConsumidor() {
        return consumidor;
    }


    /**
     * Sets the consumidor value for this CancelamentoSPCWS.
     * 
     * @param consumidor
     */
    public void setConsumidor(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor) {
        this.consumidor = consumidor;
    }


    /**
     * Gets the contrato value for this CancelamentoSPCWS.
     * 
     * @return contrato
     */
    public java.lang.String getContrato() {
        return contrato;
    }


    /**
     * Sets the contrato value for this CancelamentoSPCWS.
     * 
     * @param contrato
     */
    public void setContrato(java.lang.String contrato) {
        this.contrato = contrato;
    }


    /**
     * Gets the parcela value for this CancelamentoSPCWS.
     * 
     * @return parcela
     */
    public int getParcela() {
        return parcela;
    }


    /**
     * Sets the parcela value for this CancelamentoSPCWS.
     * 
     * @param parcela
     */
    public void setParcela(int parcela) {
        this.parcela = parcela;
    }


    /**
     * Gets the motivoCancelamento value for this CancelamentoSPCWS.
     * 
     * @return motivoCancelamento
     */
    public java.lang.String getMotivoCancelamento() {
        return motivoCancelamento;
    }


    /**
     * Sets the motivoCancelamento value for this CancelamentoSPCWS.
     * 
     * @param motivoCancelamento
     */
    public void setMotivoCancelamento(java.lang.String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CancelamentoSPCWS)) return false;
        CancelamentoSPCWS other = (CancelamentoSPCWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.consumidor==null && other.getConsumidor()==null) || 
             (this.consumidor!=null &&
              this.consumidor.equals(other.getConsumidor()))) &&
            ((this.contrato==null && other.getContrato()==null) || 
             (this.contrato!=null &&
              this.contrato.equals(other.getContrato()))) &&
            this.parcela == other.getParcela() &&
            ((this.motivoCancelamento==null && other.getMotivoCancelamento()==null) || 
             (this.motivoCancelamento!=null &&
              this.motivoCancelamento.equals(other.getMotivoCancelamento())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getConsumidor() != null) {
            _hashCode += getConsumidor().hashCode();
        }
        if (getContrato() != null) {
            _hashCode += getContrato().hashCode();
        }
        _hashCode += getParcela();
        if (getMotivoCancelamento() != null) {
            _hashCode += getMotivoCancelamento().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CancelamentoSPCWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "cancelamentoSPCWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consumidor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "consumidor"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "consumidor"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contrato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parcela");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parcela"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivoCancelamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "motivoCancelamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

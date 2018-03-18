/**
 * ConfirmacaoCancelamentoRegistroWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class ConfirmacaoCancelamentoRegistroWS  implements java.io.Serializable {
    private boolean erro;

    private java.lang.String mensagem;

    public ConfirmacaoCancelamentoRegistroWS() {
    }

    public ConfirmacaoCancelamentoRegistroWS(
           boolean erro,
           java.lang.String mensagem) {
           this.erro = erro;
           this.mensagem = mensagem;
    }


    /**
     * Gets the erro value for this ConfirmacaoCancelamentoRegistroWS.
     * 
     * @return erro
     */
    public boolean isErro() {
        return erro;
    }


    /**
     * Sets the erro value for this ConfirmacaoCancelamentoRegistroWS.
     * 
     * @param erro
     */
    public void setErro(boolean erro) {
        this.erro = erro;
    }


    /**
     * Gets the mensagem value for this ConfirmacaoCancelamentoRegistroWS.
     * 
     * @return mensagem
     */
    public java.lang.String getMensagem() {
        return mensagem;
    }


    /**
     * Sets the mensagem value for this ConfirmacaoCancelamentoRegistroWS.
     * 
     * @param mensagem
     */
    public void setMensagem(java.lang.String mensagem) {
        this.mensagem = mensagem;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConfirmacaoCancelamentoRegistroWS)) return false;
        ConfirmacaoCancelamentoRegistroWS other = (ConfirmacaoCancelamentoRegistroWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.erro == other.isErro() &&
            ((this.mensagem==null && other.getMensagem()==null) || 
             (this.mensagem!=null &&
              this.mensagem.equals(other.getMensagem())));
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
        _hashCode += (isErro() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMensagem() != null) {
            _hashCode += getMensagem().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConfirmacaoCancelamentoRegistroWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "confirmacaoCancelamentoRegistroWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("erro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "erro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensagem");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mensagem"));
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

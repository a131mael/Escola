/**
 * CancelamentoChequeWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class CancelamentoChequeWS  implements java.io.Serializable {
    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor;

    private java.lang.String numero;

    private java.lang.String motivoCancelamento;

    private java.lang.String contaCorrente;

    private int banco;

    private java.lang.String agencia;

    public CancelamentoChequeWS() {
    }

    public CancelamentoChequeWS(
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor,
           java.lang.String numero,
           java.lang.String motivoCancelamento,
           java.lang.String contaCorrente,
           int banco,
           java.lang.String agencia) {
           this.consumidor = consumidor;
           this.numero = numero;
           this.motivoCancelamento = motivoCancelamento;
           this.contaCorrente = contaCorrente;
           this.banco = banco;
           this.agencia = agencia;
    }


    /**
     * Gets the consumidor value for this CancelamentoChequeWS.
     * 
     * @return consumidor
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor getConsumidor() {
        return consumidor;
    }


    /**
     * Sets the consumidor value for this CancelamentoChequeWS.
     * 
     * @param consumidor
     */
    public void setConsumidor(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor) {
        this.consumidor = consumidor;
    }


    /**
     * Gets the numero value for this CancelamentoChequeWS.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this CancelamentoChequeWS.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the motivoCancelamento value for this CancelamentoChequeWS.
     * 
     * @return motivoCancelamento
     */
    public java.lang.String getMotivoCancelamento() {
        return motivoCancelamento;
    }


    /**
     * Sets the motivoCancelamento value for this CancelamentoChequeWS.
     * 
     * @param motivoCancelamento
     */
    public void setMotivoCancelamento(java.lang.String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }


    /**
     * Gets the contaCorrente value for this CancelamentoChequeWS.
     * 
     * @return contaCorrente
     */
    public java.lang.String getContaCorrente() {
        return contaCorrente;
    }


    /**
     * Sets the contaCorrente value for this CancelamentoChequeWS.
     * 
     * @param contaCorrente
     */
    public void setContaCorrente(java.lang.String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }


    /**
     * Gets the banco value for this CancelamentoChequeWS.
     * 
     * @return banco
     */
    public int getBanco() {
        return banco;
    }


    /**
     * Sets the banco value for this CancelamentoChequeWS.
     * 
     * @param banco
     */
    public void setBanco(int banco) {
        this.banco = banco;
    }


    /**
     * Gets the agencia value for this CancelamentoChequeWS.
     * 
     * @return agencia
     */
    public java.lang.String getAgencia() {
        return agencia;
    }


    /**
     * Sets the agencia value for this CancelamentoChequeWS.
     * 
     * @param agencia
     */
    public void setAgencia(java.lang.String agencia) {
        this.agencia = agencia;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CancelamentoChequeWS)) return false;
        CancelamentoChequeWS other = (CancelamentoChequeWS) obj;
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
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.motivoCancelamento==null && other.getMotivoCancelamento()==null) || 
             (this.motivoCancelamento!=null &&
              this.motivoCancelamento.equals(other.getMotivoCancelamento()))) &&
            ((this.contaCorrente==null && other.getContaCorrente()==null) || 
             (this.contaCorrente!=null &&
              this.contaCorrente.equals(other.getContaCorrente()))) &&
            this.banco == other.getBanco() &&
            ((this.agencia==null && other.getAgencia()==null) || 
             (this.agencia!=null &&
              this.agencia.equals(other.getAgencia())));
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
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getMotivoCancelamento() != null) {
            _hashCode += getMotivoCancelamento().hashCode();
        }
        if (getContaCorrente() != null) {
            _hashCode += getContaCorrente().hashCode();
        }
        _hashCode += getBanco();
        if (getAgencia() != null) {
            _hashCode += getAgencia().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CancelamentoChequeWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "cancelamentoChequeWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consumidor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "consumidor"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "consumidor"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivoCancelamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "motivoCancelamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contaCorrente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contaCorrente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("banco");
        elemField.setXmlName(new javax.xml.namespace.QName("", "banco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("agencia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "agencia"));
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

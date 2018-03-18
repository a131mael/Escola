/**
 * RegistroBoletoWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class RegistroBoletoWS  implements java.io.Serializable {
    private java.math.BigDecimal valorMulta;

    private java.math.BigDecimal valorJuros;

    private java.math.BigDecimal valor;

    private java.util.Calendar dataVencimento;

    private int dddCelular;

    private int celular;

    public RegistroBoletoWS() {
    }

    public RegistroBoletoWS(
           java.math.BigDecimal valorMulta,
           java.math.BigDecimal valorJuros,
           java.math.BigDecimal valor,
           java.util.Calendar dataVencimento,
           int dddCelular,
           int celular) {
           this.valorMulta = valorMulta;
           this.valorJuros = valorJuros;
           this.valor = valor;
           this.dataVencimento = dataVencimento;
           this.dddCelular = dddCelular;
           this.celular = celular;
    }


    /**
     * Gets the valorMulta value for this RegistroBoletoWS.
     * 
     * @return valorMulta
     */
    public java.math.BigDecimal getValorMulta() {
        return valorMulta;
    }


    /**
     * Sets the valorMulta value for this RegistroBoletoWS.
     * 
     * @param valorMulta
     */
    public void setValorMulta(java.math.BigDecimal valorMulta) {
        this.valorMulta = valorMulta;
    }


    /**
     * Gets the valorJuros value for this RegistroBoletoWS.
     * 
     * @return valorJuros
     */
    public java.math.BigDecimal getValorJuros() {
        return valorJuros;
    }


    /**
     * Sets the valorJuros value for this RegistroBoletoWS.
     * 
     * @param valorJuros
     */
    public void setValorJuros(java.math.BigDecimal valorJuros) {
        this.valorJuros = valorJuros;
    }


    /**
     * Gets the valor value for this RegistroBoletoWS.
     * 
     * @return valor
     */
    public java.math.BigDecimal getValor() {
        return valor;
    }


    /**
     * Sets the valor value for this RegistroBoletoWS.
     * 
     * @param valor
     */
    public void setValor(java.math.BigDecimal valor) {
        this.valor = valor;
    }


    /**
     * Gets the dataVencimento value for this RegistroBoletoWS.
     * 
     * @return dataVencimento
     */
    public java.util.Calendar getDataVencimento() {
        return dataVencimento;
    }


    /**
     * Sets the dataVencimento value for this RegistroBoletoWS.
     * 
     * @param dataVencimento
     */
    public void setDataVencimento(java.util.Calendar dataVencimento) {
        this.dataVencimento = dataVencimento;
    }


    /**
     * Gets the dddCelular value for this RegistroBoletoWS.
     * 
     * @return dddCelular
     */
    public int getDddCelular() {
        return dddCelular;
    }


    /**
     * Sets the dddCelular value for this RegistroBoletoWS.
     * 
     * @param dddCelular
     */
    public void setDddCelular(int dddCelular) {
        this.dddCelular = dddCelular;
    }


    /**
     * Gets the celular value for this RegistroBoletoWS.
     * 
     * @return celular
     */
    public int getCelular() {
        return celular;
    }


    /**
     * Sets the celular value for this RegistroBoletoWS.
     * 
     * @param celular
     */
    public void setCelular(int celular) {
        this.celular = celular;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistroBoletoWS)) return false;
        RegistroBoletoWS other = (RegistroBoletoWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.valorMulta==null && other.getValorMulta()==null) || 
             (this.valorMulta!=null &&
              this.valorMulta.equals(other.getValorMulta()))) &&
            ((this.valorJuros==null && other.getValorJuros()==null) || 
             (this.valorJuros!=null &&
              this.valorJuros.equals(other.getValorJuros()))) &&
            ((this.valor==null && other.getValor()==null) || 
             (this.valor!=null &&
              this.valor.equals(other.getValor()))) &&
            ((this.dataVencimento==null && other.getDataVencimento()==null) || 
             (this.dataVencimento!=null &&
              this.dataVencimento.equals(other.getDataVencimento()))) &&
            this.dddCelular == other.getDddCelular() &&
            this.celular == other.getCelular();
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
        if (getValorMulta() != null) {
            _hashCode += getValorMulta().hashCode();
        }
        if (getValorJuros() != null) {
            _hashCode += getValorJuros().hashCode();
        }
        if (getValor() != null) {
            _hashCode += getValor().hashCode();
        }
        if (getDataVencimento() != null) {
            _hashCode += getDataVencimento().hashCode();
        }
        _hashCode += getDddCelular();
        _hashCode += getCelular();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistroBoletoWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "registroBoletoWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valorMulta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "valorMulta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valorJuros");
        elemField.setXmlName(new javax.xml.namespace.QName("", "valorJuros"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "valor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataVencimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataVencimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dddCelular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dddCelular"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("celular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "celular"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

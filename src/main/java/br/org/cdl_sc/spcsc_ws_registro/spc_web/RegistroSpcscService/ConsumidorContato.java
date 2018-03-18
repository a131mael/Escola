/**
 * ConsumidorContato.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class ConsumidorContato  implements java.io.Serializable {
    private java.lang.String dddFixo;

    private java.lang.String numeroFixo;

    private java.lang.String dddCelular;

    private java.lang.String numeroCelular;

    public ConsumidorContato() {
    }

    public ConsumidorContato(
           java.lang.String dddFixo,
           java.lang.String numeroFixo,
           java.lang.String dddCelular,
           java.lang.String numeroCelular) {
           this.dddFixo = dddFixo;
           this.numeroFixo = numeroFixo;
           this.dddCelular = dddCelular;
           this.numeroCelular = numeroCelular;
    }


    /**
     * Gets the dddFixo value for this ConsumidorContato.
     * 
     * @return dddFixo
     */
    public java.lang.String getDddFixo() {
        return dddFixo;
    }


    /**
     * Sets the dddFixo value for this ConsumidorContato.
     * 
     * @param dddFixo
     */
    public void setDddFixo(java.lang.String dddFixo) {
        this.dddFixo = dddFixo;
    }


    /**
     * Gets the numeroFixo value for this ConsumidorContato.
     * 
     * @return numeroFixo
     */
    public java.lang.String getNumeroFixo() {
        return numeroFixo;
    }


    /**
     * Sets the numeroFixo value for this ConsumidorContato.
     * 
     * @param numeroFixo
     */
    public void setNumeroFixo(java.lang.String numeroFixo) {
        this.numeroFixo = numeroFixo;
    }


    /**
     * Gets the dddCelular value for this ConsumidorContato.
     * 
     * @return dddCelular
     */
    public java.lang.String getDddCelular() {
        return dddCelular;
    }


    /**
     * Sets the dddCelular value for this ConsumidorContato.
     * 
     * @param dddCelular
     */
    public void setDddCelular(java.lang.String dddCelular) {
        this.dddCelular = dddCelular;
    }


    /**
     * Gets the numeroCelular value for this ConsumidorContato.
     * 
     * @return numeroCelular
     */
    public java.lang.String getNumeroCelular() {
        return numeroCelular;
    }


    /**
     * Sets the numeroCelular value for this ConsumidorContato.
     * 
     * @param numeroCelular
     */
    public void setNumeroCelular(java.lang.String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsumidorContato)) return false;
        ConsumidorContato other = (ConsumidorContato) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dddFixo==null && other.getDddFixo()==null) || 
             (this.dddFixo!=null &&
              this.dddFixo.equals(other.getDddFixo()))) &&
            ((this.numeroFixo==null && other.getNumeroFixo()==null) || 
             (this.numeroFixo!=null &&
              this.numeroFixo.equals(other.getNumeroFixo()))) &&
            ((this.dddCelular==null && other.getDddCelular()==null) || 
             (this.dddCelular!=null &&
              this.dddCelular.equals(other.getDddCelular()))) &&
            ((this.numeroCelular==null && other.getNumeroCelular()==null) || 
             (this.numeroCelular!=null &&
              this.numeroCelular.equals(other.getNumeroCelular())));
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
        if (getDddFixo() != null) {
            _hashCode += getDddFixo().hashCode();
        }
        if (getNumeroFixo() != null) {
            _hashCode += getNumeroFixo().hashCode();
        }
        if (getDddCelular() != null) {
            _hashCode += getDddCelular().hashCode();
        }
        if (getNumeroCelular() != null) {
            _hashCode += getNumeroCelular().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsumidorContato.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "consumidorContato"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dddFixo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dddFixo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroFixo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroFixo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dddCelular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dddCelular"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroCelular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroCelular"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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

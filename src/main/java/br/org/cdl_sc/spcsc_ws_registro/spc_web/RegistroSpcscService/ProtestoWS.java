/**
 * ProtestoWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class ProtestoWS  implements java.io.Serializable {
    private java.lang.String comarca;

    private java.math.BigDecimal valor;

    private java.lang.String especie;

    private java.util.Calendar dataVencimento;

    private java.lang.String numero;

    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco consumidorEndereco;

    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoDocumentoWS[] protestoDocumentosWS;

    public ProtestoWS() {
    }

    public ProtestoWS(
           java.lang.String comarca,
           java.math.BigDecimal valor,
           java.lang.String especie,
           java.util.Calendar dataVencimento,
           java.lang.String numero,
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco consumidorEndereco,
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoDocumentoWS[] protestoDocumentosWS) {
           this.comarca = comarca;
           this.valor = valor;
           this.especie = especie;
           this.dataVencimento = dataVencimento;
           this.numero = numero;
           this.consumidorEndereco = consumidorEndereco;
           this.protestoDocumentosWS = protestoDocumentosWS;
    }


    /**
     * Gets the comarca value for this ProtestoWS.
     * 
     * @return comarca
     */
    public java.lang.String getComarca() {
        return comarca;
    }


    /**
     * Sets the comarca value for this ProtestoWS.
     * 
     * @param comarca
     */
    public void setComarca(java.lang.String comarca) {
        this.comarca = comarca;
    }


    /**
     * Gets the valor value for this ProtestoWS.
     * 
     * @return valor
     */
    public java.math.BigDecimal getValor() {
        return valor;
    }


    /**
     * Sets the valor value for this ProtestoWS.
     * 
     * @param valor
     */
    public void setValor(java.math.BigDecimal valor) {
        this.valor = valor;
    }


    /**
     * Gets the especie value for this ProtestoWS.
     * 
     * @return especie
     */
    public java.lang.String getEspecie() {
        return especie;
    }


    /**
     * Sets the especie value for this ProtestoWS.
     * 
     * @param especie
     */
    public void setEspecie(java.lang.String especie) {
        this.especie = especie;
    }


    /**
     * Gets the dataVencimento value for this ProtestoWS.
     * 
     * @return dataVencimento
     */
    public java.util.Calendar getDataVencimento() {
        return dataVencimento;
    }


    /**
     * Sets the dataVencimento value for this ProtestoWS.
     * 
     * @param dataVencimento
     */
    public void setDataVencimento(java.util.Calendar dataVencimento) {
        this.dataVencimento = dataVencimento;
    }


    /**
     * Gets the numero value for this ProtestoWS.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this ProtestoWS.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the consumidorEndereco value for this ProtestoWS.
     * 
     * @return consumidorEndereco
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco getConsumidorEndereco() {
        return consumidorEndereco;
    }


    /**
     * Sets the consumidorEndereco value for this ProtestoWS.
     * 
     * @param consumidorEndereco
     */
    public void setConsumidorEndereco(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco consumidorEndereco) {
        this.consumidorEndereco = consumidorEndereco;
    }


    /**
     * Gets the protestoDocumentosWS value for this ProtestoWS.
     * 
     * @return protestoDocumentosWS
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoDocumentoWS[] getProtestoDocumentosWS() {
        return protestoDocumentosWS;
    }


    /**
     * Sets the protestoDocumentosWS value for this ProtestoWS.
     * 
     * @param protestoDocumentosWS
     */
    public void setProtestoDocumentosWS(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoDocumentoWS[] protestoDocumentosWS) {
        this.protestoDocumentosWS = protestoDocumentosWS;
    }

    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoDocumentoWS getProtestoDocumentosWS(int i) {
        return this.protestoDocumentosWS[i];
    }

    public void setProtestoDocumentosWS(int i, br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoDocumentoWS _value) {
        this.protestoDocumentosWS[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProtestoWS)) return false;
        ProtestoWS other = (ProtestoWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.comarca==null && other.getComarca()==null) || 
             (this.comarca!=null &&
              this.comarca.equals(other.getComarca()))) &&
            ((this.valor==null && other.getValor()==null) || 
             (this.valor!=null &&
              this.valor.equals(other.getValor()))) &&
            ((this.especie==null && other.getEspecie()==null) || 
             (this.especie!=null &&
              this.especie.equals(other.getEspecie()))) &&
            ((this.dataVencimento==null && other.getDataVencimento()==null) || 
             (this.dataVencimento!=null &&
              this.dataVencimento.equals(other.getDataVencimento()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.consumidorEndereco==null && other.getConsumidorEndereco()==null) || 
             (this.consumidorEndereco!=null &&
              this.consumidorEndereco.equals(other.getConsumidorEndereco()))) &&
            ((this.protestoDocumentosWS==null && other.getProtestoDocumentosWS()==null) || 
             (this.protestoDocumentosWS!=null &&
              java.util.Arrays.equals(this.protestoDocumentosWS, other.getProtestoDocumentosWS())));
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
        if (getComarca() != null) {
            _hashCode += getComarca().hashCode();
        }
        if (getValor() != null) {
            _hashCode += getValor().hashCode();
        }
        if (getEspecie() != null) {
            _hashCode += getEspecie().hashCode();
        }
        if (getDataVencimento() != null) {
            _hashCode += getDataVencimento().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getConsumidorEndereco() != null) {
            _hashCode += getConsumidorEndereco().hashCode();
        }
        if (getProtestoDocumentosWS() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProtestoDocumentosWS());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProtestoDocumentosWS(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProtestoWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "protestoWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comarca");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comarca"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "valor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("especie");
        elemField.setXmlName(new javax.xml.namespace.QName("", "especie"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataVencimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataVencimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consumidorEndereco");
        elemField.setXmlName(new javax.xml.namespace.QName("", "consumidorEndereco"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "consumidorEndereco"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protestoDocumentosWS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "protestoDocumentosWS"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "protestoDocumentoWS"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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

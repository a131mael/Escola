/**
 * RegistroGeralWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class RegistroGeralWS  implements java.io.Serializable {
    private java.lang.String entidade;

    private java.lang.String associado;

    private java.util.Calendar dataRegistro;

    private java.util.Calendar dataCompra;

    private java.util.Calendar dataVencimento;

    private java.lang.String protesto;

    private java.lang.String tipo;

    private java.math.BigDecimal valor;

    private java.lang.String contrato;

    public RegistroGeralWS() {
    }

    public RegistroGeralWS(
           java.lang.String entidade,
           java.lang.String associado,
           java.util.Calendar dataRegistro,
           java.util.Calendar dataCompra,
           java.util.Calendar dataVencimento,
           java.lang.String protesto,
           java.lang.String tipo,
           java.math.BigDecimal valor,
           java.lang.String contrato) {
           this.entidade = entidade;
           this.associado = associado;
           this.dataRegistro = dataRegistro;
           this.dataCompra = dataCompra;
           this.dataVencimento = dataVencimento;
           this.protesto = protesto;
           this.tipo = tipo;
           this.valor = valor;
           this.contrato = contrato;
    }


    /**
     * Gets the entidade value for this RegistroGeralWS.
     * 
     * @return entidade
     */
    public java.lang.String getEntidade() {
        return entidade;
    }


    /**
     * Sets the entidade value for this RegistroGeralWS.
     * 
     * @param entidade
     */
    public void setEntidade(java.lang.String entidade) {
        this.entidade = entidade;
    }


    /**
     * Gets the associado value for this RegistroGeralWS.
     * 
     * @return associado
     */
    public java.lang.String getAssociado() {
        return associado;
    }


    /**
     * Sets the associado value for this RegistroGeralWS.
     * 
     * @param associado
     */
    public void setAssociado(java.lang.String associado) {
        this.associado = associado;
    }


    /**
     * Gets the dataRegistro value for this RegistroGeralWS.
     * 
     * @return dataRegistro
     */
    public java.util.Calendar getDataRegistro() {
        return dataRegistro;
    }


    /**
     * Sets the dataRegistro value for this RegistroGeralWS.
     * 
     * @param dataRegistro
     */
    public void setDataRegistro(java.util.Calendar dataRegistro) {
        this.dataRegistro = dataRegistro;
    }


    /**
     * Gets the dataCompra value for this RegistroGeralWS.
     * 
     * @return dataCompra
     */
    public java.util.Calendar getDataCompra() {
        return dataCompra;
    }


    /**
     * Sets the dataCompra value for this RegistroGeralWS.
     * 
     * @param dataCompra
     */
    public void setDataCompra(java.util.Calendar dataCompra) {
        this.dataCompra = dataCompra;
    }


    /**
     * Gets the dataVencimento value for this RegistroGeralWS.
     * 
     * @return dataVencimento
     */
    public java.util.Calendar getDataVencimento() {
        return dataVencimento;
    }


    /**
     * Sets the dataVencimento value for this RegistroGeralWS.
     * 
     * @param dataVencimento
     */
    public void setDataVencimento(java.util.Calendar dataVencimento) {
        this.dataVencimento = dataVencimento;
    }


    /**
     * Gets the protesto value for this RegistroGeralWS.
     * 
     * @return protesto
     */
    public java.lang.String getProtesto() {
        return protesto;
    }


    /**
     * Sets the protesto value for this RegistroGeralWS.
     * 
     * @param protesto
     */
    public void setProtesto(java.lang.String protesto) {
        this.protesto = protesto;
    }


    /**
     * Gets the tipo value for this RegistroGeralWS.
     * 
     * @return tipo
     */
    public java.lang.String getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this RegistroGeralWS.
     * 
     * @param tipo
     */
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the valor value for this RegistroGeralWS.
     * 
     * @return valor
     */
    public java.math.BigDecimal getValor() {
        return valor;
    }


    /**
     * Sets the valor value for this RegistroGeralWS.
     * 
     * @param valor
     */
    public void setValor(java.math.BigDecimal valor) {
        this.valor = valor;
    }


    /**
     * Gets the contrato value for this RegistroGeralWS.
     * 
     * @return contrato
     */
    public java.lang.String getContrato() {
        return contrato;
    }


    /**
     * Sets the contrato value for this RegistroGeralWS.
     * 
     * @param contrato
     */
    public void setContrato(java.lang.String contrato) {
        this.contrato = contrato;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistroGeralWS)) return false;
        RegistroGeralWS other = (RegistroGeralWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.entidade==null && other.getEntidade()==null) || 
             (this.entidade!=null &&
              this.entidade.equals(other.getEntidade()))) &&
            ((this.associado==null && other.getAssociado()==null) || 
             (this.associado!=null &&
              this.associado.equals(other.getAssociado()))) &&
            ((this.dataRegistro==null && other.getDataRegistro()==null) || 
             (this.dataRegistro!=null &&
              this.dataRegistro.equals(other.getDataRegistro()))) &&
            ((this.dataCompra==null && other.getDataCompra()==null) || 
             (this.dataCompra!=null &&
              this.dataCompra.equals(other.getDataCompra()))) &&
            ((this.dataVencimento==null && other.getDataVencimento()==null) || 
             (this.dataVencimento!=null &&
              this.dataVencimento.equals(other.getDataVencimento()))) &&
            ((this.protesto==null && other.getProtesto()==null) || 
             (this.protesto!=null &&
              this.protesto.equals(other.getProtesto()))) &&
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo()))) &&
            ((this.valor==null && other.getValor()==null) || 
             (this.valor!=null &&
              this.valor.equals(other.getValor()))) &&
            ((this.contrato==null && other.getContrato()==null) || 
             (this.contrato!=null &&
              this.contrato.equals(other.getContrato())));
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
        if (getEntidade() != null) {
            _hashCode += getEntidade().hashCode();
        }
        if (getAssociado() != null) {
            _hashCode += getAssociado().hashCode();
        }
        if (getDataRegistro() != null) {
            _hashCode += getDataRegistro().hashCode();
        }
        if (getDataCompra() != null) {
            _hashCode += getDataCompra().hashCode();
        }
        if (getDataVencimento() != null) {
            _hashCode += getDataVencimento().hashCode();
        }
        if (getProtesto() != null) {
            _hashCode += getProtesto().hashCode();
        }
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        if (getValor() != null) {
            _hashCode += getValor().hashCode();
        }
        if (getContrato() != null) {
            _hashCode += getContrato().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistroGeralWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "registroGeralWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("associado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "associado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCompra");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataCompra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataVencimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataVencimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protesto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "protesto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo"));
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
        elemField.setFieldName("contrato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contrato"));
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

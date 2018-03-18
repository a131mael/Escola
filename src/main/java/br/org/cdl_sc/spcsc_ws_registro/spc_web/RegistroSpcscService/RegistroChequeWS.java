/**
 * RegistroChequeWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class RegistroChequeWS  implements java.io.Serializable {
    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor;

    private int banco;

    private java.lang.String agencia;

    private java.lang.String contaCorrente;

    private java.lang.String alineaDevolucao;

    private java.util.Calendar dataVencimento;

    private java.util.Calendar dataEmissao;

    private int numero;

    private java.math.BigDecimal valor;

    private boolean campanha;

    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoWS protestoWS;

    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroBoletoWS registroBoletoWS;

    public RegistroChequeWS() {
    }

    public RegistroChequeWS(
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor,
           int banco,
           java.lang.String agencia,
           java.lang.String contaCorrente,
           java.lang.String alineaDevolucao,
           java.util.Calendar dataVencimento,
           java.util.Calendar dataEmissao,
           int numero,
           java.math.BigDecimal valor,
           boolean campanha,
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoWS protestoWS,
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroBoletoWS registroBoletoWS) {
           this.consumidor = consumidor;
           this.banco = banco;
           this.agencia = agencia;
           this.contaCorrente = contaCorrente;
           this.alineaDevolucao = alineaDevolucao;
           this.dataVencimento = dataVencimento;
           this.dataEmissao = dataEmissao;
           this.numero = numero;
           this.valor = valor;
           this.campanha = campanha;
           this.protestoWS = protestoWS;
           this.registroBoletoWS = registroBoletoWS;
    }


    /**
     * Gets the consumidor value for this RegistroChequeWS.
     * 
     * @return consumidor
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor getConsumidor() {
        return consumidor;
    }


    /**
     * Sets the consumidor value for this RegistroChequeWS.
     * 
     * @param consumidor
     */
    public void setConsumidor(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor) {
        this.consumidor = consumidor;
    }


    /**
     * Gets the banco value for this RegistroChequeWS.
     * 
     * @return banco
     */
    public int getBanco() {
        return banco;
    }


    /**
     * Sets the banco value for this RegistroChequeWS.
     * 
     * @param banco
     */
    public void setBanco(int banco) {
        this.banco = banco;
    }


    /**
     * Gets the agencia value for this RegistroChequeWS.
     * 
     * @return agencia
     */
    public java.lang.String getAgencia() {
        return agencia;
    }


    /**
     * Sets the agencia value for this RegistroChequeWS.
     * 
     * @param agencia
     */
    public void setAgencia(java.lang.String agencia) {
        this.agencia = agencia;
    }


    /**
     * Gets the contaCorrente value for this RegistroChequeWS.
     * 
     * @return contaCorrente
     */
    public java.lang.String getContaCorrente() {
        return contaCorrente;
    }


    /**
     * Sets the contaCorrente value for this RegistroChequeWS.
     * 
     * @param contaCorrente
     */
    public void setContaCorrente(java.lang.String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }


    /**
     * Gets the alineaDevolucao value for this RegistroChequeWS.
     * 
     * @return alineaDevolucao
     */
    public java.lang.String getAlineaDevolucao() {
        return alineaDevolucao;
    }


    /**
     * Sets the alineaDevolucao value for this RegistroChequeWS.
     * 
     * @param alineaDevolucao
     */
    public void setAlineaDevolucao(java.lang.String alineaDevolucao) {
        this.alineaDevolucao = alineaDevolucao;
    }


    /**
     * Gets the dataVencimento value for this RegistroChequeWS.
     * 
     * @return dataVencimento
     */
    public java.util.Calendar getDataVencimento() {
        return dataVencimento;
    }


    /**
     * Sets the dataVencimento value for this RegistroChequeWS.
     * 
     * @param dataVencimento
     */
    public void setDataVencimento(java.util.Calendar dataVencimento) {
        this.dataVencimento = dataVencimento;
    }


    /**
     * Gets the dataEmissao value for this RegistroChequeWS.
     * 
     * @return dataEmissao
     */
    public java.util.Calendar getDataEmissao() {
        return dataEmissao;
    }


    /**
     * Sets the dataEmissao value for this RegistroChequeWS.
     * 
     * @param dataEmissao
     */
    public void setDataEmissao(java.util.Calendar dataEmissao) {
        this.dataEmissao = dataEmissao;
    }


    /**
     * Gets the numero value for this RegistroChequeWS.
     * 
     * @return numero
     */
    public int getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this RegistroChequeWS.
     * 
     * @param numero
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }


    /**
     * Gets the valor value for this RegistroChequeWS.
     * 
     * @return valor
     */
    public java.math.BigDecimal getValor() {
        return valor;
    }


    /**
     * Sets the valor value for this RegistroChequeWS.
     * 
     * @param valor
     */
    public void setValor(java.math.BigDecimal valor) {
        this.valor = valor;
    }


    /**
     * Gets the campanha value for this RegistroChequeWS.
     * 
     * @return campanha
     */
    public boolean isCampanha() {
        return campanha;
    }


    /**
     * Sets the campanha value for this RegistroChequeWS.
     * 
     * @param campanha
     */
    public void setCampanha(boolean campanha) {
        this.campanha = campanha;
    }


    /**
     * Gets the protestoWS value for this RegistroChequeWS.
     * 
     * @return protestoWS
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoWS getProtestoWS() {
        return protestoWS;
    }


    /**
     * Sets the protestoWS value for this RegistroChequeWS.
     * 
     * @param protestoWS
     */
    public void setProtestoWS(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoWS protestoWS) {
        this.protestoWS = protestoWS;
    }


    /**
     * Gets the registroBoletoWS value for this RegistroChequeWS.
     * 
     * @return registroBoletoWS
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroBoletoWS getRegistroBoletoWS() {
        return registroBoletoWS;
    }


    /**
     * Sets the registroBoletoWS value for this RegistroChequeWS.
     * 
     * @param registroBoletoWS
     */
    public void setRegistroBoletoWS(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroBoletoWS registroBoletoWS) {
        this.registroBoletoWS = registroBoletoWS;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistroChequeWS)) return false;
        RegistroChequeWS other = (RegistroChequeWS) obj;
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
            this.banco == other.getBanco() &&
            ((this.agencia==null && other.getAgencia()==null) || 
             (this.agencia!=null &&
              this.agencia.equals(other.getAgencia()))) &&
            ((this.contaCorrente==null && other.getContaCorrente()==null) || 
             (this.contaCorrente!=null &&
              this.contaCorrente.equals(other.getContaCorrente()))) &&
            ((this.alineaDevolucao==null && other.getAlineaDevolucao()==null) || 
             (this.alineaDevolucao!=null &&
              this.alineaDevolucao.equals(other.getAlineaDevolucao()))) &&
            ((this.dataVencimento==null && other.getDataVencimento()==null) || 
             (this.dataVencimento!=null &&
              this.dataVencimento.equals(other.getDataVencimento()))) &&
            ((this.dataEmissao==null && other.getDataEmissao()==null) || 
             (this.dataEmissao!=null &&
              this.dataEmissao.equals(other.getDataEmissao()))) &&
            this.numero == other.getNumero() &&
            ((this.valor==null && other.getValor()==null) || 
             (this.valor!=null &&
              this.valor.equals(other.getValor()))) &&
            this.campanha == other.isCampanha() &&
            ((this.protestoWS==null && other.getProtestoWS()==null) || 
             (this.protestoWS!=null &&
              this.protestoWS.equals(other.getProtestoWS()))) &&
            ((this.registroBoletoWS==null && other.getRegistroBoletoWS()==null) || 
             (this.registroBoletoWS!=null &&
              this.registroBoletoWS.equals(other.getRegistroBoletoWS())));
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
        _hashCode += getBanco();
        if (getAgencia() != null) {
            _hashCode += getAgencia().hashCode();
        }
        if (getContaCorrente() != null) {
            _hashCode += getContaCorrente().hashCode();
        }
        if (getAlineaDevolucao() != null) {
            _hashCode += getAlineaDevolucao().hashCode();
        }
        if (getDataVencimento() != null) {
            _hashCode += getDataVencimento().hashCode();
        }
        if (getDataEmissao() != null) {
            _hashCode += getDataEmissao().hashCode();
        }
        _hashCode += getNumero();
        if (getValor() != null) {
            _hashCode += getValor().hashCode();
        }
        _hashCode += (isCampanha() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getProtestoWS() != null) {
            _hashCode += getProtestoWS().hashCode();
        }
        if (getRegistroBoletoWS() != null) {
            _hashCode += getRegistroBoletoWS().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistroChequeWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "registroChequeWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consumidor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "consumidor"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "consumidor"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contaCorrente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contaCorrente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alineaDevolucao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "alineaDevolucao"));
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
        elemField.setFieldName("dataEmissao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataEmissao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "valor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campanha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "campanha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protestoWS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "protestoWS"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "protestoWS"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registroBoletoWS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registroBoletoWS"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "registroBoletoWS"));
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

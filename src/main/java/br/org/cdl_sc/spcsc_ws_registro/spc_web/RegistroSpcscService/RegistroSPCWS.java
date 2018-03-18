/**
 * RegistroSPCWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class RegistroSPCWS  implements java.io.Serializable {
    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor;

    private java.lang.String contrato;

    private java.lang.String motivoRegistro;

    private java.util.Calendar dataVencimento;

    private java.util.Calendar dataCompra;

    private int parcela;

    private java.math.BigDecimal valor;

    private boolean campanha;

    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoWS protestoWS;

    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroBoletoWS registroBoletoWS;

    public RegistroSPCWS() {
    }

    public RegistroSPCWS(
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor,
           java.lang.String contrato,
           java.lang.String motivoRegistro,
           java.util.Calendar dataVencimento,
           java.util.Calendar dataCompra,
           int parcela,
           java.math.BigDecimal valor,
           boolean campanha,
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoWS protestoWS,
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroBoletoWS registroBoletoWS) {
           this.consumidor = consumidor;
           this.contrato = contrato;
           this.motivoRegistro = motivoRegistro;
           this.dataVencimento = dataVencimento;
           this.dataCompra = dataCompra;
           this.parcela = parcela;
           this.valor = valor;
           this.campanha = campanha;
           this.protestoWS = protestoWS;
           this.registroBoletoWS = registroBoletoWS;
    }


    /**
     * Gets the consumidor value for this RegistroSPCWS.
     * 
     * @return consumidor
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor getConsumidor() {
        return consumidor;
    }


    /**
     * Sets the consumidor value for this RegistroSPCWS.
     * 
     * @param consumidor
     */
    public void setConsumidor(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor) {
        this.consumidor = consumidor;
    }


    /**
     * Gets the contrato value for this RegistroSPCWS.
     * 
     * @return contrato
     */
    public java.lang.String getContrato() {
        return contrato;
    }


    /**
     * Sets the contrato value for this RegistroSPCWS.
     * 
     * @param contrato
     */
    public void setContrato(java.lang.String contrato) {
        this.contrato = contrato;
    }


    /**
     * Gets the motivoRegistro value for this RegistroSPCWS.
     * 
     * @return motivoRegistro
     */
    public java.lang.String getMotivoRegistro() {
        return motivoRegistro;
    }


    /**
     * Sets the motivoRegistro value for this RegistroSPCWS.
     * 
     * @param motivoRegistro
     */
    public void setMotivoRegistro(java.lang.String motivoRegistro) {
        this.motivoRegistro = motivoRegistro;
    }


    /**
     * Gets the dataVencimento value for this RegistroSPCWS.
     * 
     * @return dataVencimento
     */
    public java.util.Calendar getDataVencimento() {
        return dataVencimento;
    }


    /**
     * Sets the dataVencimento value for this RegistroSPCWS.
     * 
     * @param dataVencimento
     */
    public void setDataVencimento(java.util.Calendar dataVencimento) {
        this.dataVencimento = dataVencimento;
    }


    /**
     * Gets the dataCompra value for this RegistroSPCWS.
     * 
     * @return dataCompra
     */
    public java.util.Calendar getDataCompra() {
        return dataCompra;
    }


    /**
     * Sets the dataCompra value for this RegistroSPCWS.
     * 
     * @param dataCompra
     */
    public void setDataCompra(java.util.Calendar dataCompra) {
        this.dataCompra = dataCompra;
    }


    /**
     * Gets the parcela value for this RegistroSPCWS.
     * 
     * @return parcela
     */
    public int getParcela() {
        return parcela;
    }


    /**
     * Sets the parcela value for this RegistroSPCWS.
     * 
     * @param parcela
     */
    public void setParcela(int parcela) {
        this.parcela = parcela;
    }


    /**
     * Gets the valor value for this RegistroSPCWS.
     * 
     * @return valor
     */
    public java.math.BigDecimal getValor() {
        return valor;
    }


    /**
     * Sets the valor value for this RegistroSPCWS.
     * 
     * @param valor
     */
    public void setValor(java.math.BigDecimal valor) {
        this.valor = valor;
    }


    /**
     * Gets the campanha value for this RegistroSPCWS.
     * 
     * @return campanha
     */
    public boolean isCampanha() {
        return campanha;
    }


    /**
     * Sets the campanha value for this RegistroSPCWS.
     * 
     * @param campanha
     */
    public void setCampanha(boolean campanha) {
        this.campanha = campanha;
    }


    /**
     * Gets the protestoWS value for this RegistroSPCWS.
     * 
     * @return protestoWS
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoWS getProtestoWS() {
        return protestoWS;
    }


    /**
     * Sets the protestoWS value for this RegistroSPCWS.
     * 
     * @param protestoWS
     */
    public void setProtestoWS(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ProtestoWS protestoWS) {
        this.protestoWS = protestoWS;
    }


    /**
     * Gets the registroBoletoWS value for this RegistroSPCWS.
     * 
     * @return registroBoletoWS
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroBoletoWS getRegistroBoletoWS() {
        return registroBoletoWS;
    }


    /**
     * Sets the registroBoletoWS value for this RegistroSPCWS.
     * 
     * @param registroBoletoWS
     */
    public void setRegistroBoletoWS(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.RegistroBoletoWS registroBoletoWS) {
        this.registroBoletoWS = registroBoletoWS;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistroSPCWS)) return false;
        RegistroSPCWS other = (RegistroSPCWS) obj;
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
            ((this.motivoRegistro==null && other.getMotivoRegistro()==null) || 
             (this.motivoRegistro!=null &&
              this.motivoRegistro.equals(other.getMotivoRegistro()))) &&
            ((this.dataVencimento==null && other.getDataVencimento()==null) || 
             (this.dataVencimento!=null &&
              this.dataVencimento.equals(other.getDataVencimento()))) &&
            ((this.dataCompra==null && other.getDataCompra()==null) || 
             (this.dataCompra!=null &&
              this.dataCompra.equals(other.getDataCompra()))) &&
            this.parcela == other.getParcela() &&
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
        if (getContrato() != null) {
            _hashCode += getContrato().hashCode();
        }
        if (getMotivoRegistro() != null) {
            _hashCode += getMotivoRegistro().hashCode();
        }
        if (getDataVencimento() != null) {
            _hashCode += getDataVencimento().hashCode();
        }
        if (getDataCompra() != null) {
            _hashCode += getDataCompra().hashCode();
        }
        _hashCode += getParcela();
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
        new org.apache.axis.description.TypeDesc(RegistroSPCWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "registroSPCWS"));
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
        elemField.setFieldName("motivoRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "motivoRegistro"));
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
        elemField.setFieldName("dataCompra");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataCompra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parcela");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parcela"));
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

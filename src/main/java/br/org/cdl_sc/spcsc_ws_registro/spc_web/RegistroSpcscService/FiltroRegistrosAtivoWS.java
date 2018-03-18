/**
 * FiltroRegistrosAtivoWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class FiltroRegistrosAtivoWS  implements java.io.Serializable {
    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor;

    private java.lang.String protocolo;

    public FiltroRegistrosAtivoWS() {
    }

    public FiltroRegistrosAtivoWS(
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor,
           java.lang.String protocolo) {
           this.consumidor = consumidor;
           this.protocolo = protocolo;
    }


    /**
     * Gets the consumidor value for this FiltroRegistrosAtivoWS.
     * 
     * @return consumidor
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor getConsumidor() {
        return consumidor;
    }


    /**
     * Sets the consumidor value for this FiltroRegistrosAtivoWS.
     * 
     * @param consumidor
     */
    public void setConsumidor(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.Consumidor consumidor) {
        this.consumidor = consumidor;
    }


    /**
     * Gets the protocolo value for this FiltroRegistrosAtivoWS.
     * 
     * @return protocolo
     */
    public java.lang.String getProtocolo() {
        return protocolo;
    }


    /**
     * Sets the protocolo value for this FiltroRegistrosAtivoWS.
     * 
     * @param protocolo
     */
    public void setProtocolo(java.lang.String protocolo) {
        this.protocolo = protocolo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FiltroRegistrosAtivoWS)) return false;
        FiltroRegistrosAtivoWS other = (FiltroRegistrosAtivoWS) obj;
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
            ((this.protocolo==null && other.getProtocolo()==null) || 
             (this.protocolo!=null &&
              this.protocolo.equals(other.getProtocolo())));
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
        if (getProtocolo() != null) {
            _hashCode += getProtocolo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FiltroRegistrosAtivoWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "filtroRegistrosAtivoWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consumidor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "consumidor"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "consumidor"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protocolo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "protocolo"));
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

/**
 * RegistroSpcScWSFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class RegistroSpcScWSFault  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private java.lang.String faultString1;

    private java.lang.String faultCode1;

    public RegistroSpcScWSFault() {
    }

    public RegistroSpcScWSFault(
           java.lang.String faultString1,
           java.lang.String faultCode1) {
        this.faultString1 = faultString1;
        this.faultCode1 = faultCode1;
    }


    /**
     * Gets the faultString1 value for this RegistroSpcScWSFault.
     * 
     * @return faultString1
     */
    public java.lang.String getFaultString1() {
        return faultString1;
    }


    /**
     * Sets the faultString1 value for this RegistroSpcScWSFault.
     * 
     * @param faultString1
     */
    public void setFaultString1(java.lang.String faultString1) {
        this.faultString1 = faultString1;
    }


    /**
     * Gets the faultCode1 value for this RegistroSpcScWSFault.
     * 
     * @return faultCode1
     */
    public java.lang.String getFaultCode1() {
        return faultCode1;
    }


    /**
     * Sets the faultCode1 value for this RegistroSpcScWSFault.
     * 
     * @param faultCode1
     */
    public void setFaultCode1(java.lang.String faultCode1) {
        this.faultCode1 = faultCode1;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistroSpcScWSFault)) return false;
        RegistroSpcScWSFault other = (RegistroSpcScWSFault) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.faultString1==null && other.getFaultString1()==null) || 
             (this.faultString1!=null &&
              this.faultString1.equals(other.getFaultString1()))) &&
            ((this.faultCode1==null && other.getFaultCode1()==null) || 
             (this.faultCode1!=null &&
              this.faultCode1.equals(other.getFaultCode1())));
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
        if (getFaultString1() != null) {
            _hashCode += getFaultString1().hashCode();
        }
        if (getFaultCode1() != null) {
            _hashCode += getFaultCode1().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistroSpcScWSFault.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "RegistroSpcScWSFault"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("faultString1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "faultString"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("faultCode1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "faultCode"));
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}

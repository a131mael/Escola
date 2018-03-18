/**
 * Consumidor.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService;

public class Consumidor  implements java.io.Serializable {
    private java.lang.String cpfCnpj;

    private java.lang.String nome;

    private java.lang.String sexo;

    private java.lang.String rg;

    private java.lang.String ufRg;

    private java.lang.String email;

    private java.util.Calendar dataNascimento;

    private java.lang.String nomeConjuge;

    private java.util.Calendar dataNascimentoConjuge;

    private java.lang.String estadoCivil;

    private java.lang.String nomePai;

    private java.lang.String nomeMae;

    private java.lang.String localTrabalho;

    private java.lang.String nacionalidade;

    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco consumidorEndereco;

    private br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorContato consumidorContato;

    private java.util.Calendar dataFundacao;

    private java.lang.String nomeFantasia;

    private java.lang.String naturalidade;

    public Consumidor() {
    }

    public Consumidor(
           java.lang.String cpfCnpj,
           java.lang.String nome,
           java.lang.String sexo,
           java.lang.String rg,
           java.lang.String ufRg,
           java.lang.String email,
           java.util.Calendar dataNascimento,
           java.lang.String nomeConjuge,
           java.util.Calendar dataNascimentoConjuge,
           java.lang.String estadoCivil,
           java.lang.String nomePai,
           java.lang.String nomeMae,
           java.lang.String localTrabalho,
           java.lang.String nacionalidade,
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco consumidorEndereco,
           br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorContato consumidorContato,
           java.util.Calendar dataFundacao,
           java.lang.String nomeFantasia,
           java.lang.String naturalidade) {
           this.cpfCnpj = cpfCnpj;
           this.nome = nome;
           this.sexo = sexo;
           this.rg = rg;
           this.ufRg = ufRg;
           this.email = email;
           this.dataNascimento = dataNascimento;
           this.nomeConjuge = nomeConjuge;
           this.dataNascimentoConjuge = dataNascimentoConjuge;
           this.estadoCivil = estadoCivil;
           this.nomePai = nomePai;
           this.nomeMae = nomeMae;
           this.localTrabalho = localTrabalho;
           this.nacionalidade = nacionalidade;
           this.consumidorEndereco = consumidorEndereco;
           this.consumidorContato = consumidorContato;
           this.dataFundacao = dataFundacao;
           this.nomeFantasia = nomeFantasia;
           this.naturalidade = naturalidade;
    }


    /**
     * Gets the cpfCnpj value for this Consumidor.
     * 
     * @return cpfCnpj
     */
    public java.lang.String getCpfCnpj() {
        return cpfCnpj;
    }


    /**
     * Sets the cpfCnpj value for this Consumidor.
     * 
     * @param cpfCnpj
     */
    public void setCpfCnpj(java.lang.String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }


    /**
     * Gets the nome value for this Consumidor.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this Consumidor.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the sexo value for this Consumidor.
     * 
     * @return sexo
     */
    public java.lang.String getSexo() {
        return sexo;
    }


    /**
     * Sets the sexo value for this Consumidor.
     * 
     * @param sexo
     */
    public void setSexo(java.lang.String sexo) {
        this.sexo = sexo;
    }


    /**
     * Gets the rg value for this Consumidor.
     * 
     * @return rg
     */
    public java.lang.String getRg() {
        return rg;
    }


    /**
     * Sets the rg value for this Consumidor.
     * 
     * @param rg
     */
    public void setRg(java.lang.String rg) {
        this.rg = rg;
    }


    /**
     * Gets the ufRg value for this Consumidor.
     * 
     * @return ufRg
     */
    public java.lang.String getUfRg() {
        return ufRg;
    }


    /**
     * Sets the ufRg value for this Consumidor.
     * 
     * @param ufRg
     */
    public void setUfRg(java.lang.String ufRg) {
        this.ufRg = ufRg;
    }


    /**
     * Gets the email value for this Consumidor.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this Consumidor.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the dataNascimento value for this Consumidor.
     * 
     * @return dataNascimento
     */
    public java.util.Calendar getDataNascimento() {
        return dataNascimento;
    }


    /**
     * Sets the dataNascimento value for this Consumidor.
     * 
     * @param dataNascimento
     */
    public void setDataNascimento(java.util.Calendar dataNascimento) {
        this.dataNascimento = dataNascimento;
    }


    /**
     * Gets the nomeConjuge value for this Consumidor.
     * 
     * @return nomeConjuge
     */
    public java.lang.String getNomeConjuge() {
        return nomeConjuge;
    }


    /**
     * Sets the nomeConjuge value for this Consumidor.
     * 
     * @param nomeConjuge
     */
    public void setNomeConjuge(java.lang.String nomeConjuge) {
        this.nomeConjuge = nomeConjuge;
    }


    /**
     * Gets the dataNascimentoConjuge value for this Consumidor.
     * 
     * @return dataNascimentoConjuge
     */
    public java.util.Calendar getDataNascimentoConjuge() {
        return dataNascimentoConjuge;
    }


    /**
     * Sets the dataNascimentoConjuge value for this Consumidor.
     * 
     * @param dataNascimentoConjuge
     */
    public void setDataNascimentoConjuge(java.util.Calendar dataNascimentoConjuge) {
        this.dataNascimentoConjuge = dataNascimentoConjuge;
    }


    /**
     * Gets the estadoCivil value for this Consumidor.
     * 
     * @return estadoCivil
     */
    public java.lang.String getEstadoCivil() {
        return estadoCivil;
    }


    /**
     * Sets the estadoCivil value for this Consumidor.
     * 
     * @param estadoCivil
     */
    public void setEstadoCivil(java.lang.String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }


    /**
     * Gets the nomePai value for this Consumidor.
     * 
     * @return nomePai
     */
    public java.lang.String getNomePai() {
        return nomePai;
    }


    /**
     * Sets the nomePai value for this Consumidor.
     * 
     * @param nomePai
     */
    public void setNomePai(java.lang.String nomePai) {
        this.nomePai = nomePai;
    }


    /**
     * Gets the nomeMae value for this Consumidor.
     * 
     * @return nomeMae
     */
    public java.lang.String getNomeMae() {
        return nomeMae;
    }


    /**
     * Sets the nomeMae value for this Consumidor.
     * 
     * @param nomeMae
     */
    public void setNomeMae(java.lang.String nomeMae) {
        this.nomeMae = nomeMae;
    }


    /**
     * Gets the localTrabalho value for this Consumidor.
     * 
     * @return localTrabalho
     */
    public java.lang.String getLocalTrabalho() {
        return localTrabalho;
    }


    /**
     * Sets the localTrabalho value for this Consumidor.
     * 
     * @param localTrabalho
     */
    public void setLocalTrabalho(java.lang.String localTrabalho) {
        this.localTrabalho = localTrabalho;
    }


    /**
     * Gets the nacionalidade value for this Consumidor.
     * 
     * @return nacionalidade
     */
    public java.lang.String getNacionalidade() {
        return nacionalidade;
    }


    /**
     * Sets the nacionalidade value for this Consumidor.
     * 
     * @param nacionalidade
     */
    public void setNacionalidade(java.lang.String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }


    /**
     * Gets the consumidorEndereco value for this Consumidor.
     * 
     * @return consumidorEndereco
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco getConsumidorEndereco() {
        return consumidorEndereco;
    }


    /**
     * Sets the consumidorEndereco value for this Consumidor.
     * 
     * @param consumidorEndereco
     */
    public void setConsumidorEndereco(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorEndereco consumidorEndereco) {
        this.consumidorEndereco = consumidorEndereco;
    }


    /**
     * Gets the consumidorContato value for this Consumidor.
     * 
     * @return consumidorContato
     */
    public br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorContato getConsumidorContato() {
        return consumidorContato;
    }


    /**
     * Sets the consumidorContato value for this Consumidor.
     * 
     * @param consumidorContato
     */
    public void setConsumidorContato(br.org.cdl_sc.spcsc_ws_registro.spc_web.RegistroSpcscService.ConsumidorContato consumidorContato) {
        this.consumidorContato = consumidorContato;
    }


    /**
     * Gets the dataFundacao value for this Consumidor.
     * 
     * @return dataFundacao
     */
    public java.util.Calendar getDataFundacao() {
        return dataFundacao;
    }


    /**
     * Sets the dataFundacao value for this Consumidor.
     * 
     * @param dataFundacao
     */
    public void setDataFundacao(java.util.Calendar dataFundacao) {
        this.dataFundacao = dataFundacao;
    }


    /**
     * Gets the nomeFantasia value for this Consumidor.
     * 
     * @return nomeFantasia
     */
    public java.lang.String getNomeFantasia() {
        return nomeFantasia;
    }


    /**
     * Sets the nomeFantasia value for this Consumidor.
     * 
     * @param nomeFantasia
     */
    public void setNomeFantasia(java.lang.String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }


    /**
     * Gets the naturalidade value for this Consumidor.
     * 
     * @return naturalidade
     */
    public java.lang.String getNaturalidade() {
        return naturalidade;
    }


    /**
     * Sets the naturalidade value for this Consumidor.
     * 
     * @param naturalidade
     */
    public void setNaturalidade(java.lang.String naturalidade) {
        this.naturalidade = naturalidade;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Consumidor)) return false;
        Consumidor other = (Consumidor) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cpfCnpj==null && other.getCpfCnpj()==null) || 
             (this.cpfCnpj!=null &&
              this.cpfCnpj.equals(other.getCpfCnpj()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            ((this.sexo==null && other.getSexo()==null) || 
             (this.sexo!=null &&
              this.sexo.equals(other.getSexo()))) &&
            ((this.rg==null && other.getRg()==null) || 
             (this.rg!=null &&
              this.rg.equals(other.getRg()))) &&
            ((this.ufRg==null && other.getUfRg()==null) || 
             (this.ufRg!=null &&
              this.ufRg.equals(other.getUfRg()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.dataNascimento==null && other.getDataNascimento()==null) || 
             (this.dataNascimento!=null &&
              this.dataNascimento.equals(other.getDataNascimento()))) &&
            ((this.nomeConjuge==null && other.getNomeConjuge()==null) || 
             (this.nomeConjuge!=null &&
              this.nomeConjuge.equals(other.getNomeConjuge()))) &&
            ((this.dataNascimentoConjuge==null && other.getDataNascimentoConjuge()==null) || 
             (this.dataNascimentoConjuge!=null &&
              this.dataNascimentoConjuge.equals(other.getDataNascimentoConjuge()))) &&
            ((this.estadoCivil==null && other.getEstadoCivil()==null) || 
             (this.estadoCivil!=null &&
              this.estadoCivil.equals(other.getEstadoCivil()))) &&
            ((this.nomePai==null && other.getNomePai()==null) || 
             (this.nomePai!=null &&
              this.nomePai.equals(other.getNomePai()))) &&
            ((this.nomeMae==null && other.getNomeMae()==null) || 
             (this.nomeMae!=null &&
              this.nomeMae.equals(other.getNomeMae()))) &&
            ((this.localTrabalho==null && other.getLocalTrabalho()==null) || 
             (this.localTrabalho!=null &&
              this.localTrabalho.equals(other.getLocalTrabalho()))) &&
            ((this.nacionalidade==null && other.getNacionalidade()==null) || 
             (this.nacionalidade!=null &&
              this.nacionalidade.equals(other.getNacionalidade()))) &&
            ((this.consumidorEndereco==null && other.getConsumidorEndereco()==null) || 
             (this.consumidorEndereco!=null &&
              this.consumidorEndereco.equals(other.getConsumidorEndereco()))) &&
            ((this.consumidorContato==null && other.getConsumidorContato()==null) || 
             (this.consumidorContato!=null &&
              this.consumidorContato.equals(other.getConsumidorContato()))) &&
            ((this.dataFundacao==null && other.getDataFundacao()==null) || 
             (this.dataFundacao!=null &&
              this.dataFundacao.equals(other.getDataFundacao()))) &&
            ((this.nomeFantasia==null && other.getNomeFantasia()==null) || 
             (this.nomeFantasia!=null &&
              this.nomeFantasia.equals(other.getNomeFantasia()))) &&
            ((this.naturalidade==null && other.getNaturalidade()==null) || 
             (this.naturalidade!=null &&
              this.naturalidade.equals(other.getNaturalidade())));
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
        if (getCpfCnpj() != null) {
            _hashCode += getCpfCnpj().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        if (getSexo() != null) {
            _hashCode += getSexo().hashCode();
        }
        if (getRg() != null) {
            _hashCode += getRg().hashCode();
        }
        if (getUfRg() != null) {
            _hashCode += getUfRg().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getDataNascimento() != null) {
            _hashCode += getDataNascimento().hashCode();
        }
        if (getNomeConjuge() != null) {
            _hashCode += getNomeConjuge().hashCode();
        }
        if (getDataNascimentoConjuge() != null) {
            _hashCode += getDataNascimentoConjuge().hashCode();
        }
        if (getEstadoCivil() != null) {
            _hashCode += getEstadoCivil().hashCode();
        }
        if (getNomePai() != null) {
            _hashCode += getNomePai().hashCode();
        }
        if (getNomeMae() != null) {
            _hashCode += getNomeMae().hashCode();
        }
        if (getLocalTrabalho() != null) {
            _hashCode += getLocalTrabalho().hashCode();
        }
        if (getNacionalidade() != null) {
            _hashCode += getNacionalidade().hashCode();
        }
        if (getConsumidorEndereco() != null) {
            _hashCode += getConsumidorEndereco().hashCode();
        }
        if (getConsumidorContato() != null) {
            _hashCode += getConsumidorContato().hashCode();
        }
        if (getDataFundacao() != null) {
            _hashCode += getDataFundacao().hashCode();
        }
        if (getNomeFantasia() != null) {
            _hashCode += getNomeFantasia().hashCode();
        }
        if (getNaturalidade() != null) {
            _hashCode += getNaturalidade().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Consumidor.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "consumidor"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cpfCnpj");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cpfCnpj"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sexo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sexo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ufRg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ufRg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataNascimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataNascimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeConjuge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomeConjuge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataNascimentoConjuge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataNascimentoConjuge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estadoCivil");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estadoCivil"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomePai");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomePai"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeMae");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomeMae"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localTrabalho");
        elemField.setXmlName(new javax.xml.namespace.QName("", "localTrabalho"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nacionalidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nacionalidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("consumidorContato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "consumidorContato"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://spcsc-ws-registro.cdl-sc.org.br/spc-web/RegistroSpcscService", "consumidorContato"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataFundacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataFundacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeFantasia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomeFantasia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("naturalidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "naturalidade"));
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

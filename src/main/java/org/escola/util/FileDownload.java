/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.escola.util;

import java.io.InputStream;

import javax.ejb.LocalBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author abimael
 */
@ManagedBean(name = "FileDownload")
@ViewScoped
@LocalBean
public class FileDownload {

    private StreamedContent file;

    public FileDownload(InputStream stream) {        
        file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
    }
    
    public FileDownload(String caminhoArquivo) {         /*"/resources/demo/images/optimus.jpg"*/
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(caminhoArquivo);
        file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
    }
 
    public static StreamedContent getContentDoc(InputStream stream, String nomeArquivoSaida){
    	return new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", nomeArquivoSaida);
    }
    
    public StreamedContent getFile() {
        return file;
    }
}

package org.escola.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class WriteDocx {

	public static void main(String[] args) throws Exception {
        /*int count = 0;
        XWPFDocument document = new XWPFDocument();
        XWPFDocument docx = new XWPFDocument(new FileInputStream("MODELO.docx"));
        XWPFWordExtractor we = new XWPFWordExtractor(docx);
        
        String text = we.getText() ;
        if(text.contains("ESCOLAR")){
            text = text.replace("ESCOLAR", "XXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println(text);
        }
        char[] c = text.toCharArray();
        for(int i= 0; i < c.length;i++){

            if(c[i] == '\n'){
                count ++;
            }
        }
        System.out.println(c[0]);
        StringTokenizer st = new StringTokenizer(text,"\n");

        XWPFParagraph para = document.createParagraph();
        para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = para.createRun();
        run.setBold(true);
        run.setFontSize(36);
        run.setText("Apache POI works well!");

        List<XWPFParagraph>paragraphs = new ArrayList<XWPFParagraph>();
        List<XWPFRun>runs = new ArrayList<XWPFRun>();
        int k = 0;
        for(k=0;k<count+1;k++){
            paragraphs.add(document.createParagraph());
        }
        k=0;
        while(st.hasMoreElements()){
            paragraphs.get(k).setAlignment(ParagraphAlignment.LEFT);
            paragraphs.get(k).setSpacingAfter(0);
            paragraphs.get(k).setSpacingBefore(0);
            run = paragraphs.get(k).createRun();
            run.setText(st.nextElement().toString());
            k++;
        }

        document.write(new FileOutputStream("MODELO.docx"));*/
		//editDocX2("MODELO1.doc");
		OfficeDOCUtil docUtil= new OfficeDOCUtil();
		
		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("#CONTRATANTENOME", "contrato.getContratanteNome()");
		trocas.put("#CONTRATANTERG", "contrato.getContratanteRG()");
		trocas.put("#CONTRATANTECPF", "contrato.getContratanteCPF()");
		trocas.put("#CONTRATANTERUA", "contrato.getContratanteRua()");
		
		trocas.put("#TRANSPORTADONOME", "contrato.getTransportadoNome()");
		trocas.put("#TRANSPORTADORUA", "contrato.getTransportadoEndereco()");
		trocas.put("#TRANSPORTADOESCOLA", "contrato.getTransportadoEscola()");

		
		trocas.put("#DADOSGERAISHORARIO1", "contrato.getHorario1()");
		trocas.put("#DADOSGERAISHORARIO2", "contrato.getHorario2()");
		trocas.put("#DADOSGERAISMES1", "contrato.getMes1()");
		trocas.put("#DADOSGERAISMES2", "contrato.getMes2()");
		trocas.put("#DADOSGERAISPARCELAS", "contrato.getParcelas()");
		trocas.put("#DADOSGERAISTOTAL", "contrato.getValorTotal()");
		trocas.put("#DADOSGERAISTOTALEXTENSO", "valor por extenso");
		trocas.put("#DADOSGERAISQTADEPARCELAS", "contrato.getParcelas()");
		trocas.put("#DADOSGERAISPARCELA", "calcular");///valor da parcela
		trocas.put("#DADOSGERAISPARCELAEXTENSO", "gerar");
		//docUtil.editDoc("MODELO1.doc", trocas);
    }          

	public static void editDOCX(String modelFile){
		XWPFDocument doc = null;
		try {
			doc = new XWPFDocument(OPCPackage.open(modelFile));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		for (XWPFParagraph p : doc.getParagraphs()) {
		    List<XWPFRun> runs = p.getRuns();
		    if (runs != null) {
		        for (XWPFRun r : runs) {
		            String text = r.getText(0);
		            if (text != null && text.contains("ESCOLAR")) {
		                text = text.replace("ESCOLAR", "xxxxxxxxxxxxxxxxxxxxxxxxxx");
		                r.setText(text, 0);
		            }
		        }
		    }
		}
		for (XWPFTable tbl : doc.getTables()) {
		   for (XWPFTableRow row : tbl.getRows()) {
		      for (XWPFTableCell cell : row.getTableCells()) {
		         for (XWPFParagraph p : cell.getParagraphs()) {
		            for (XWPFRun r : p.getRuns()) {
		              String text = r.getText(0);
		              if (text.contains("ESCOLAR")) {
		                text = text.replace("ESCOLAR", "xxxxxxxxxxx");
		                r.setText(text);
		              }
		            }
		         }
		      }
		   }
		}
		try {
			doc.write(new FileOutputStream("output.doc"));
			doc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void editDocX2(String endereco){
		try
		{
		    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(endereco));
		    HWPFDocument doc = new HWPFDocument(fs);
		    OutputStream writer = new FileOutputStream("saida.docx");

		    doc.getRange().replaceText("ESCOLAR", "xxxxxxxxxx");
		    doc.write(writer);
		    writer.close();
		}
		catch(Exception e) 
		{
		    e.printStackTrace();
		}
	}
	
}
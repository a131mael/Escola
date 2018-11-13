package org.escola.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.escola.model.Aluno;
import org.escola.model.Boleto;
import org.escola.util.Formatador;
import org.escola.util.Verificador;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

public class OfficePDFUtil {

	/**
	 * @param args	
	 * @throws DocumentException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {

		// TODO Auto-generated method stub

		// Listing 1. Instantiation of document object
		/*
		 * Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		 * 
		 * String temp = System.getProperty("java.io.tmpdir");
		 * 
		 * // Listing 2. Creation of PdfWriter object PdfWriter writer =
		 * PdfWriter.getInstance(document, new FileOutputStream(temp +
		 * File.separator + "ITextTest.pdf"));
		 * 
		 * document.open();
		 * 
		 * // Listing 3. Creation of paragraph object Anchor anchorTarget = new
		 * Anchor("First page of the document.");
		 * anchorTarget.setName("BackToTop"); Paragraph paragraph1 = new
		 * Paragraph();
		 * 
		 * paragraph1.setSpacingBefore(50);
		 * 
		 * paragraph1.add(anchorTarget); document.add(paragraph1);
		 * 
		 * document .add(new Paragraph(
		 * "Some more text on the first page with different color and font type."
		 * , FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, new
		 * CMYKColor(0, 255, 0, 0))));
		 * 
		 * // Listing 4. Creation of chapter object Paragraph title1 = new
		 * Paragraph("Chapter 1", FontFactory.getFont( FontFactory.HELVETICA,
		 * 18, Font.BOLDITALIC, new CMYKColor(0, 255, 255, 17))); Chapter
		 * chapter1 = new Chapter(title1, 1); chapter1.setNumberDepth(0);
		 * 
		 * // Listing 5. Creation of section object Paragraph title11 = new
		 * Paragraph("This is Section 1 in Chapter 1",
		 * FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new
		 * CMYKColor(0, 255, 255, 17))); Section section1 =
		 * chapter1.addSection(title11); Paragraph someSectionText = new
		 * Paragraph( "This text comes as part of section 1 of chapter 1.");
		 * section1.add(someSectionText); someSectionText = new Paragraph(
		 * "Following is a 3 X 2 table."); section1.add(someSectionText);
		 * 
		 * // Listing 6. Creation of table object PdfPTable t = new
		 * PdfPTable(3);
		 * 
		 * t.setSpacingBefore(25); t.setSpacingAfter(25); PdfPCell c1 = new
		 * PdfPCell(new Phrase("Header1")); t.addCell(c1); PdfPCell c2 = new
		 * PdfPCell(new Phrase("Header2")); t.addCell(c2); PdfPCell c3 = new
		 * PdfPCell(new Phrase("Header3")); t.addCell(c3); t.addCell("1.1");
		 * t.addCell("1.2"); t.addCell("1.3"); section1.add(t);
		 * 
		 * // Listing 7. Creation of list object List l = new List(true, false,
		 * 10); l.add(new ListItem("First item of list")); l.add(new ListItem(
		 * "Second item of list")); section1.add(l);
		 * 
		 * // Listing 8. Adding image to the main document
		 * 
		 * //Image image2 = Image.getInstance("IBMLogo.bmp");
		 * //image2.scaleAbsolute(120f, 120f); //section1.add(image2);
		 * 
		 * // Listing 9. Adding Anchor to the main document. Paragraph title2 =
		 * new Paragraph("Using Anchor", FontFactory.getFont(
		 * FontFactory.HELVETICA, 16, Font.BOLD, new CMYKColor(0, 255, 0, 0)));
		 * section1.add(title2);
		 * 
		 * title2.setSpacingBefore(5000); Anchor anchor2 = new Anchor(
		 * "Back To Top"); anchor2.setReference("#BackToTop");
		 * 
		 * section1.add(anchor2);
		 * 
		 * 
		 * // Listing 10. Addition of a chapter to the main document
		 * document.add(chapter1); document.close();
		 */

	}

	public static void geraPDF(String nomeArquivo,byte[] pdfByteArray){
		
		try {
			String temp = System.getProperty("java.io.tmpdir");
		FileOutputStream out;
			out = new FileOutputStream(temp + File.separator + nomeArquivo);
			out.write(pdfByteArray);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void criaPDFDevedores(List<Aluno> alunos, String nomeArquivo) throws DocumentException, IOException{
		Document document = new Document(PageSize.A4.rotate(),  10f, 10f, 10f, 0f);
		PdfWriter writer = PdfWriter.getInstance(document,	new FileOutputStream(nomeArquivo));
		document.open();
		
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());
		
		document.add(new Paragraph("Lista de Devedores ADONAI gerada no dia :" + dataExtenso ));
		
		List<String> colunas = new ArrayList<>();
		colunas.add("Nome");
		colunas.add("CPF");
		colunas.add("Telefone");
		colunas.add("Nome Criança");
		colunas.add("Num Contrato");
		colunas.add("Entrada");
		colunas.add("Vencimento");
		colunas.add("Valor total");
		Table tabela = criaTabela(colunas);
		
		for(Aluno aluno : alunos){
			//tabela.addCell(criaCell(aluno.getNomeResponsavel(),6));
		//	tabela.addCell(criaCell(aluno.getCpfResponsavel(),6));
			String telefones = "";
			
			telefones+= getTelefones(aluno);
			tabela.addCell(criaCell(telefones,7));
			
			tabela.addCell(criaCell(aluno.getNomeAluno(),6));
			Double valorTotalDevido = 0D;
			String numeroBoleto = "";
			String vencimento = "";
			String entrada = "";
			/*for(org.escola.model.Boleto boleto : aluno.getBoletos()){
				if(boleto.getAtrasado() != null && boleto.getAtrasado()){
					numeroBoleto +=aluno.getCodigo() + "-" + boleto.getNossoNumero() + "\n"; 
					entrada += Formatador.formataData(boleto.getEmissao()) + "\n";
					vencimento += Formatador.formataData(boleto.getVencimento()) + "\n";
					valorTotalDevido += Verificador.getValorFinal(boleto);
				}
			}*/
			tabela.addCell(criaCell(numeroBoleto,6));
			tabela.addCell(criaCell(entrada,6));
			tabela.addCell(criaCell(vencimento,6));
			tabela.addCell(criaCell("R$ " + valorTotalDevido+"",7));
		}
		
		document.add(tabela);
		document.add(new Paragraph("Quantidade Crianças : " + alunos.size()));
		document.add(new Paragraph("Quantidade de boletos : " + getQuantidadeBoletos(alunos)));
		document.add(new Paragraph("Valor total : " + Formatador.valorFormatado(getValorTotal(alunos))));
		
		document.close();
	}
	
	private static Double getValorTotal(List<Aluno> alunos) {
		Double quantidade = 0D;
		for(Aluno al : alunos){
			/*for(Boleto b : al.getBoletos()){
				if(b.getAtrasado() != null && b.getAtrasado()){
					quantidade+=  Verificador.getValorFinal(b);
				}
			}*/
		}
		return quantidade;
	}

	private static int getQuantidadeBoletos(List<Aluno> alunos) {
		int quantidade = 0;
		for(Aluno al : alunos){
			/*for(Boleto b : al.getBoletos()){
				if(b.getAtrasado() != null && b.getAtrasado()){
					quantidade+= 1;
				}
			}*/
		}
		return quantidade;
	}

	private static String getTelefones(Aluno aluno) {
		String telefones = "";
		if(aluno.getTelefone() != null && !aluno.getTelefone().equalsIgnoreCase("")){
			telefones+=aluno.getTelefoneCelularMae()+ " / ";
		}if(aluno.getTelefoneEmpresaTrabalhaMae() != null && !aluno.getTelefoneEmpresaTrabalhaMae().equalsIgnoreCase("")){
			telefones+=aluno.getTelefoneEmpresaTrabalhaMae()+ " \n";
		}if(aluno.getTelefoneCelularPai() != null && !aluno.getTelefoneCelularPai().equalsIgnoreCase("")){
			telefones+=aluno.getTelefoneCelularPai()+ " / ";
		}if(aluno.getTelefoneEmpresaTrabalhaPai() != null && !aluno.getTelefoneEmpresaTrabalhaPai().equalsIgnoreCase("")){
			telefones+=aluno.getTelefoneEmpresaTrabalhaPai()+ " \n";
		}if(aluno.getTelefoneResidencialPai() != null && !aluno.getTelefoneResidencialPai().equalsIgnoreCase("")){
			telefones+=aluno.getTelefoneResidencialPai()+ " / ";
		}if(aluno.getTelefoneResidencialMae()!= null){
			telefones+=aluno.getTelefoneResidencialPai();
		}
		
		return telefones;
	}

	public static Cell criaCell(String string, float size) throws IOException, BadElementException {
		Font colfont = new Font(Font.HELVETICA, size); //you can change Font size 
		 
		Cell cell = null;
		if (string != null && "".equals(string)) {
	            return new Cell();
	        }else{
	        	cell = new Cell(new Phrase(string, colfont));
	        }
	        return cell;
	    }
	
	public static Table criaTabela(List<String> colunas) throws BadElementException, IOException{
		Table t = new Table(colunas.size());
		t.setBorderColor(new Color(220, 255, 100));
		t.setPadding(1);
		t.setSpacing(1);
		t.setBorderWidth(2);
		t.setWidth(100);
		for(String coluna : colunas){
			Cell c1 = criaCell(coluna, 12);
			c1.setHeader(true);
			t.addCell(c1);
		}
		t.endHeaders();
		
		return t;
	}
	
}

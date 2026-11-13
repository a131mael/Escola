package org.escola.controller;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
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
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
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

	/** Declaração de Pagamentos (comprovante pra Imposto de Renda) gerada direto em PDF
	 * com iText, sem depender de template .docx/LibreOffice. `fraseSubstituicao` já vem
	 * pronta de quem chamou (varia se o contrato está 100% quitado ou só em dia). */
	public static byte[] gerarDeclaracaoPagamentos(String nomeResponsavel, String cpfResponsavel, int ano,
			String valorPagoFormatado, int parcelasPagas, String valorParcelaFormatado, String nomeAluno,
			String dataExtenso, String fraseSubstituicao, String caminhoAssinatura) throws DocumentException {
		Document document = new Document(PageSize.A4, 60f, 60f, 50f, 50f);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.open();

		Font fonteCabecalhoBold = new Font(Font.HELVETICA, 11, Font.BOLD);
		Font fonteCabecalho = new Font(Font.HELVETICA, 10, Font.NORMAL);
		Font fonteTitulo = new Font(Font.HELVETICA, 14, Font.BOLD);
		Font fonteCorpo = new Font(Font.HELVETICA, 12, Font.NORMAL);

		Paragraph nomeEscola = new Paragraph("COLÉGIO ADONAI", fonteCabecalhoBold);
		nomeEscola.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(nomeEscola);

		String[] linhasCabecalho = {
				"Centro Educacional Adonai",
				"Estado de Santa Catarina",
				"MUNICÍPIO DE PALHOÇA",
				"PARECER Nº 571 / 2013",
				"Endereço: Rua José Cosme Pamplona nº 2001",
				"Bela Vista – Palhoça",
				"Fone: (48) 3242-4194 / 3093-0042",
		};
		for (String linha : linhasCabecalho) {
			Paragraph p = new Paragraph(linha, fonteCabecalho);
			p.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(p);
		}

		Paragraph titulo = new Paragraph("DECLARAÇÃO DE PAGAMENTOS", fonteTitulo);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		titulo.setSpacingBefore(24f);
		titulo.setSpacingAfter(24f);
		document.add(titulo);

		String corpoTexto = "Centro Educacional Adonai CNPJ 14.395.954/0001-55, com endereço na Rua José Cosme "
				+ "Pamplona nº 2001 – Bela Vista - Palhoça, vem, através desta, declarar que " + nomeResponsavel
				+ ", CPF: " + cpfResponsavel + ", efetuou o pagamento de mensalidades referentes ao Contrato de "
				+ "Prestação de Serviços de Educação Escolar para o ano letivo de " + ano + ", no valor total pago "
				+ "de R$ " + valorPagoFormatado + " (referente a " + parcelasPagas + " parcela(s) de R$ "
				+ valorParcelaFormatado + "), cujo beneficiário foi o aluno " + nomeAluno + ".";
		Paragraph corpo = new Paragraph(corpoTexto, fonteCorpo);
		corpo.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		corpo.setLeading(18f);
		corpo.setSpacingAfter(16f);
		document.add(corpo);

		Paragraph fechamento = new Paragraph(fraseSubstituicao, fonteCorpo);
		fechamento.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		fechamento.setLeading(18f);
		fechamento.setSpacingAfter(40f);
		document.add(fechamento);

		Paragraph dataParagrafo = new Paragraph("Palhoça, " + dataExtenso + ".", fonteCorpo);
		dataParagrafo.setSpacingAfter(50f);
		document.add(dataParagrafo);

		boolean assinaturaAdicionada = false;
		if (caminhoAssinatura != null) {
			try {
				Image assinatura = Image.getInstance(caminhoAssinatura);
				assinatura.scaleToFit(180f, 70f);
				assinatura.setAlignment(Image.ALIGN_LEFT);
				document.add(assinatura);
				assinaturaAdicionada = true;
			} catch (IOException e) {
				// Sem a imagem, cai no fallback da linha em branco abaixo.
			}
		}
		if (!assinaturaAdicionada) {
			document.add(new Paragraph("________________________________", fonteCorpo));
		}
		document.add(new Paragraph("Secretaria Centro Educacional Adonai.", fonteCorpo));

		document.close();
		return baos.toByteArray();
	}

	/** Contrato de Acordo de Dívida gerado direto em PDF (iText), com as assinaturas
	 * da credora e das 2 testemunhas embutidas — mesmo padrão da Declaração de
	 * Pagamentos. Substitui o antigo modeloAcordoDivida.docx (metade dos campos
	 * ficava em branco lá). Texto das cláusulas replicado fielmente do modelo
	 * original, com os valores calculados já substituídos. */
	public static byte[] gerarContratoAcordoDivida(String nomeResponsavel, String cpfResponsavel, String endereco,
			String totalDividaFormatado, String valorParcelaFormatado, int numeroParcelas,
			String vencimentoPrimeiraParcela, String dataExtenso, String mesesDevendo,
			String caminhoAssinaturaCredora, String caminhoAssinaturaTestemunha1, String caminhoAssinaturaTestemunha2)
			throws DocumentException {
		Document document = new Document(PageSize.A4, 60f, 60f, 50f, 50f);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.open();

		Font fonteCabecalhoBold = new Font(Font.HELVETICA, 11, Font.BOLD);
		Font fonteCabecalho = new Font(Font.HELVETICA, 10, Font.NORMAL);
		Font fonteTitulo = new Font(Font.HELVETICA, 14, Font.BOLD);
		Font fonteSecao = new Font(Font.HELVETICA, 12, Font.BOLD);
		Font fonteCorpo = new Font(Font.HELVETICA, 11, Font.NORMAL);
		Font fonteCorpoBold = new Font(Font.HELVETICA, 11, Font.BOLD);

		Paragraph nomeEscola = new Paragraph("COLÉGIO ADONAI", fonteCabecalhoBold);
		nomeEscola.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(nomeEscola);
		String[] linhasCabecalho = {
				"Centro Educacional Adonai",
				"Endereço: Rua Manoel Joaquim de Souza, 97",
				"Bela Vista – Palhoça",
				"Fone: (48) 3242-4194 / 3093-0042",
		};
		for (String linha : linhasCabecalho) {
			Paragraph p = new Paragraph(linha, fonteCabecalho);
			p.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(p);
		}

		Paragraph titulo = new Paragraph("CONTRATO DE ACORDO DE DÍVIDA", fonteTitulo);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		titulo.setSpacingBefore(20f);
		titulo.setSpacingAfter(20f);
		document.add(titulo);

		document.add(secao("IDENTIFICAÇÃO DAS PARTES CONTRATANTES", fonteSecao));
		document.add(corpo("COLÉGIO ADONAI, inscrito no CNPJ nº 14.395.954/0001-55, com sede à Rua Manoel Joaquim "
				+ "de Souza, 94, Bela Vista, Palhoça, Santa Catarina, neste ato representado por Marlete Maria da "
				+ "Silva Fidêncio, doravante denominada simplesmente CREDORA.", fonteCorpo));
		document.add(corpo(nomeResponsavel + ", inscrito(a) no CPF sob o nº " + cpfResponsavel + ", residente e "
				+ "domiciliado(a) à " + (endereco != null ? endereco : "") + ", doravante denominado(a) simplesmente "
				+ "DEVEDOR(A).", fonteCorpo));
		document.add(corpo("As partes acima identificadas têm, entre si, justo e acertado o presente contrato de "
				+ "acordo de dívida, que se regerá pelas cláusulas e condições seguintes:", fonteCorpo));

		document.add(secao("CLÁUSULA PRIMEIRA - DO OBJETO DO ACORDO", fonteSecao));
		document.add(corpo("O presente contrato tem por objeto o reconhecimento e a quitação da dívida existente "
				+ "em nome do(a) DEVEDOR(A) perante o COLÉGIO ADONAI, referente a valores inadimplidos relativos a "
				+ "mensalidades escolares e outros encargos, conforme detalhado na Cláusula Segunda.", fonteCorpo));

		document.add(secao("CLÁUSULA SEGUNDA - DO RECONHECIMENTO DA DÍVIDA", fonteSecao));
		document.add(corpo("O(a) DEVEDOR(A) reconhece e confessa que deve ao COLÉGIO ADONAI o montante de R$ "
				+ totalDividaFormatado + ", referente a:", fonteCorpo));
		String rotuloMensalidades = "Mensalidades escolares em atraso"
				+ (mesesDevendo != null && !mesesDevendo.trim().isEmpty() ? " (" + mesesDevendo + ")" : "");
		document.add(corpo(rotuloMensalidades + ": R$ " + totalDividaFormatado, fonteCorpo));
		document.add(corpo("Taxas adicionais: R$ 0,00", fonteCorpo));
		document.add(corpo("Totalizando o valor de R$ " + totalDividaFormatado + " a ser quitado conforme "
				+ "condições estabelecidas neste contrato.", fonteCorpo));

		document.add(secao("CLÁUSULA TERCEIRA - DAS CONDIÇÕES DE PAGAMENTO", fonteSecao));
		document.add(corpo("As partes acordam que a dívida mencionada na Cláusula Segunda será paga pelo(a) "
				+ "DEVEDOR(A) ao COLÉGIO ADONAI nas seguintes condições:", fonteCorpo));
		document.add(corpo("Parcelamento: O valor total da dívida será dividido em " + numeroParcelas
				+ " parcelas iguais de R$ " + valorParcelaFormatado + ".", fonteCorpo));
		document.add(corpo("Data de vencimento: A primeira parcela deverá ser paga no dia "
				+ vencimentoPrimeiraParcela + ", e as parcelas subsequentes vencerão no mesmo dia dos meses "
				+ "seguintes.", fonteCorpo));
		document.add(corpo("Forma de pagamento: O pagamento será realizado através de Boleto bancário.", fonteCorpo));

		document.add(secao("CLÁUSULA QUARTA - DAS CONSEQUÊNCIAS DO INADIMPLEMENTO", fonteSecao));
		document.add(corpo("O não pagamento de qualquer parcela na data de vencimento implicará em:", fonteCorpo));
		document.add(corpo("Multa de 2% sobre o valor da parcela em atraso.", fonteCorpo));
		document.add(corpo("Juros de R$ 0,50 (cinquenta centavos) por boleto, por dia de atraso, até o efetivo "
				+ "pagamento.", fonteCorpo));
		document.add(corpo("Caso o(a) DEVEDOR(A) deixe de pagar 2 parcelas consecutivas ou intercaladas ou haja "
				+ "parcela com mais de 45 dias de atraso o presente acordo será considerado rescindido e o saldo "
				+ "total da dívida será imediatamente exigível, com os devidos acréscimos legais.", fonteCorpo));
		document.add(corpo("Caso o contrato seja rescindido por inadimplência o COLÉGIO ADONAI poderá realizar o "
				+ "protesto do saldo total da dívida imediatamente, com inclusão do nome do contratante nos órgãos "
				+ "de proteção ao crédito, conforme previsto na legislação vigente.", fonteCorpo));

		document.add(secao("CLÁUSULA QUINTA - DA QUITAÇÃO", fonteSecao));
		document.add(corpo("Após o pagamento integral da dívida conforme as condições estabelecidas neste "
				+ "contrato, o COLÉGIO ADONAI fornecerá ao(a) DEVEDOR(A) um termo de quitação total da dívida, não "
				+ "restando mais qualquer valor a ser pago relacionado ao objeto deste contrato.", fonteCorpo));

		document.add(secao("CLÁUSULA SEXTA - DA CONFIDENCIALIDADE", fonteSecao));
		document.add(corpo("As partes se comprometem a manter confidenciais as informações relacionadas ao "
				+ "presente contrato e à dívida aqui mencionada, não divulgando-as a terceiros, exceto quando "
				+ "exigido por lei ou decisão judicial.", fonteCorpo));

		document.add(secao("CLÁUSULA SÉTIMA - DO FORO", fonteSecao));
		document.add(corpo("As partes elegem o foro da Comarca de Palhoça, Estado de Santa Catarina, para dirimir "
				+ "quaisquer dúvidas ou litígios oriundos deste contrato, renunciando a qualquer outro, por mais "
				+ "privilegiado que seja.", fonteCorpo));

		document.add(corpo("E, por estarem de pleno acordo com as cláusulas deste contrato, as partes assinam o "
				+ "presente instrumento em duas vias de igual teor e forma, juntamente com duas testemunhas, para "
				+ "que produza seus devidos efeitos legais.", fonteCorpo));

		Paragraph dataParagrafo = new Paragraph("Palhoça, " + dataExtenso + ".", fonteCorpo);
		dataParagrafo.setSpacingBefore(10f);
		dataParagrafo.setSpacingAfter(30f);
		document.add(dataParagrafo);

		document.add(corpo("CREDORA:", fonteCorpoBold));
		adicionarAssinatura(document, caminhoAssinaturaCredora);
		document.add(corpo("Nome: Marlete Maria da Silva Fidêncio", fonteCorpo));
		document.add(corpo("Representante legal do Colégio Adonai", fonteCorpo));
		document.add(corpo("CNPJ: 14.395.954/0001-55", fonteCorpo));

		Paragraph devedorTitulo = corpo("DEVEDOR(A):", fonteCorpoBold);
		devedorTitulo.setSpacingBefore(24f);
		document.add(devedorTitulo);
		document.add(new Paragraph(" "));
		document.add(corpo("Nome: " + nomeResponsavel, fonteCorpo));
		document.add(corpo("CPF: " + cpfResponsavel, fonteCorpo));

		Paragraph testemunhasTitulo = corpo("TESTEMUNHAS:", fonteCorpoBold);
		testemunhasTitulo.setSpacingBefore(24f);
		document.add(testemunhasTitulo);

		adicionarAssinatura(document, caminhoAssinaturaTestemunha1);
		document.add(corpo("NOME: ABIMAEL ALDEVINO FIDENCIO", fonteCorpo));
		document.add(corpo("RG: 5052701", fonteCorpo));
		document.add(corpo("CPF: 066.606.049-52", fonteCorpo));

		adicionarAssinatura(document, caminhoAssinaturaTestemunha2);
		document.add(corpo("NOME: Bernardo Gonçalves Fidêncio", fonteCorpo));
		document.add(corpo("RG: 109.160.559-90", fonteCorpo));
		document.add(corpo("CPF: 109.160.559-90", fonteCorpo));

		document.close();
		return baos.toByteArray();
	}

	private static Paragraph secao(String texto, Font fonte) {
		Paragraph p = new Paragraph(texto, fonte);
		p.setSpacingBefore(14f);
		p.setSpacingAfter(6f);
		return p;
	}

	private static Paragraph corpo(String texto, Font fonte) {
		Paragraph p = new Paragraph(texto, fonte);
		p.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		p.setLeading(15f);
		p.setSpacingAfter(4f);
		return p;
	}

	private static void adicionarAssinatura(Document document, String caminhoAssinatura) {
		try {
			Image assinatura = Image.getInstance(caminhoAssinatura);
			assinatura.scaleToFit(170f, 65f);
			assinatura.setAlignment(Image.ALIGN_LEFT);
			assinatura.setSpacingBefore(6f);
			document.add(assinatura);
		} catch (Exception e) {
			// segue sem a imagem — ainda sobra a linha "Nome:/CPF:" pra identificar
		}
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

	private static final Font DANFSE_FONTE_MARCA = new Font(Font.HELVETICA, 16, Font.BOLD);
	private static final Font DANFSE_FONTE_MARCA_SUB = new Font(Font.HELVETICA, 6, Font.NORMAL, Color.GRAY);
	private static final Font DANFSE_FONTE_TITULO = new Font(Font.HELVETICA, 11, Font.BOLD);
	private static final Font DANFSE_FONTE_TOPO = new Font(Font.HELVETICA, 8, Font.NORMAL);
	private static final Font DANFSE_FONTE_SECAO = new Font(Font.HELVETICA, 8, Font.BOLD);
	private static final Font DANFSE_FONTE_LABEL = new Font(Font.HELVETICA, 6, Font.NORMAL, Color.DARK_GRAY);
	private static final Font DANFSE_FONTE_VALOR = new Font(Font.HELVETICA, 8, Font.NORMAL);
	private static final Font DANFSE_FONTE_VALOR_GRANDE = new Font(Font.HELVETICA, 11, Font.BOLD);
	private static final Color DANFSE_CINZA_SECAO = new Color(225, 225, 225);
	private static final Color DANFSE_CINZA_LINHA = new Color(245, 245, 245);

	/** Gera o DANFSe (Documento Auxiliar da NFS-e) em PDF a partir do XML da nota ja
	 * autorizada, guardado em NotaFiscal.xmlNfseRetorno. Construido na mao porque a
	 * API oficial do governo pra isso foi desativada em 03/08/2026 (NT 008/2026) --
	 * so o download manual avulso no portal continua funcionando. Layout seguindo
	 * de perto o DANFSe v2.0 oficial (mesmas secoes/campos, conferido contra uma
	 * nota real baixada do portal nacional). */
	public static byte[] gerarDanfse(String xmlNfse) throws Exception {
		com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4, 24f, 24f, 24f, 24f);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.open();

		String chaveAcesso = extrairDanfse(xmlNfse, "infNFSe Id=\"NFS(\\d+)\"");
		String numeroNFSe = extrairDanfse(xmlNfse, "<nNFSe>(\\d+)</nNFSe>");
		String dhProc = extrairDanfseData(extrairDanfse(xmlNfse, "<dhProc>([^<]+)</dhProc>"));
		String dCompetBruta = extrairDanfse(xmlNfse, "<dCompet>([^<]+)</dCompet>");
		String competencia = formataDataSimples(dCompetBruta);
		String xLocEmi = extrairDanfse(xmlNfse, "<xLocEmi>([^<]+)</xLocEmi>");
		String xTribNac = extrairDanfse(xmlNfse, "<xTribNac>([^<]+)</xTribNac>");
		String xNBSDesc = extrairDanfse(xmlNfse, "<xNBS>([^<]+)</xNBS>");
		String vLiq = extrairDanfse(xmlNfse, "<vLiq>([\\d.]+)</vLiq>");
		String ambGer = extrairDanfse(xmlNfse, "<ambGer>(\\d+)</ambGer>");

		String idDps = extrairDanfse(xmlNfse, "infDPS Id=\"(DPS[^\"]+)\"");
		String numDps = extrairDanfse(xmlNfse, "<nDPS>(\\d+)</nDPS>");
		String serieDps = extrairDanfse(xmlNfse, "<serie>([^<]+)</serie>");
		String dhEmiDps = extrairDanfseData(extrairDanfse(xmlNfse, "<dhEmi>([^<]+)</dhEmi>"));
		String tpAmb = extrairDanfse(xmlNfse, "<tpAmb>(\\d+)</tpAmb>");

		String emitCnpj = extrairDanfse(xmlNfse, "<emit><CNPJ>(\\d+)</CNPJ>");
		String emitNome = extrairDanfse(xmlNfse, "<emit>.*?<xNome>([^<]+)</xNome>");
		String emitLgr = extrairDanfse(xmlNfse, "<xLgr>([^<]+)</xLgr>");
		String emitNro = extrairDanfse(xmlNfse, "<nro>([^<]+)</nro>");
		String emitBairro = extrairDanfse(xmlNfse, "<xBairro>([^<]+)</xBairro>");
		String emitCep = extrairDanfse(xmlNfse, "<CEP>(\\d+)</CEP>");
		String emitFone = extrairDanfse(xmlNfse, "<emit>.*?<fone>([^<]+)</fone>");
		String emitEmail = extrairDanfse(xmlNfse, "<emit>.*?<email>([^<]+)</email>");
		String cMun = extrairDanfse(xmlNfse, "<cMun>(\\d+)</cMun>");
		String emitUF = extrairDanfse(xmlNfse, "<enderNac>.*?<UF>([^<]+)</UF>");

		String tomaCpf = extrairDanfse(xmlNfse, "<toma><CPF>(\\d+)</CPF>");
		String tomaCnpj = tomaCpf.isEmpty() ? extrairDanfse(xmlNfse, "<toma><CNPJ>(\\d+)</CNPJ>") : "";
		String tomaNome = extrairDanfse(xmlNfse, "<toma>.*?<xNome>([^<]+)</xNome>");

		String descServ = extrairDanfse(xmlNfse, "<xDescServ>([^<]+)</xDescServ>");
		String imPrest = extrairDanfse(xmlNfse, "<prest>.*?<IM>([^<]+)</IM>");
		String opSimpNac = extrairDanfse(xmlNfse, "<opSimpNac>(\\d+)</opSimpNac>");
		String cTribNac = extrairDanfse(xmlNfse, "<cTribNac>(\\d+)</cTribNac>");
		String cNBS = extrairDanfse(xmlNfse, "<cNBS>(\\d+)</cNBS>");
		String tribISSQN = extrairDanfse(xmlNfse, "<tribISSQN>(\\d+)</tribISSQN>");
		String tpRetISSQN = extrairDanfse(xmlNfse, "<tpRetISSQN>(\\d+)</tpRetISSQN>");
		String pTotTribSN = extrairDanfse(xmlNfse, "<pTotTribSN>([\\d.]+)</pTotTribSN>");

		// ---- Cabecalho ----
		PdfPTable cabecalho = new PdfPTable(new float[] { 2.3f, 3f, 2.3f });
		cabecalho.setWidthPercentage(100);
		PdfPCell celMarca = new PdfPCell();
		celMarca.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
		Paragraph marca = new Paragraph("NFSe", DANFSE_FONTE_MARCA);
		marca.add(new Phrase("\nNota Fiscal de\nServiço eletrônica", DANFSE_FONTE_MARCA_SUB));
		celMarca.addElement(marca);
		cabecalho.addCell(celMarca);

		PdfPCell celTitulo = new PdfPCell();
		celTitulo.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
		celTitulo.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
		Paragraph pTitulo = new Paragraph("DANFSe v2.0", DANFSE_FONTE_TITULO);
		pTitulo.setAlignment(Paragraph.ALIGN_CENTER);
		pTitulo.add(new Phrase("\nDocumento Auxiliar da NFS-e", new Font(Font.HELVETICA, 9, Font.NORMAL)));
		celTitulo.addElement(pTitulo);
		cabecalho.addCell(celTitulo);

		PdfPCell celInfoTopo = new PdfPCell();
		celInfoTopo.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
		celInfoTopo.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
		Paragraph pInfoTopo = new Paragraph("Município: " + (xLocEmi.isEmpty() ? "-" : xLocEmi), DANFSE_FONTE_TOPO);
		pInfoTopo.setAlignment(Paragraph.ALIGN_RIGHT);
		pInfoTopo.add(new Phrase("\nAmbiente Gerador: " + (ambGer.isEmpty() ? "-" : ambGer), DANFSE_FONTE_TOPO));
		pInfoTopo.add(new Phrase("\nTipo de Ambiente: " + (tpAmb.isEmpty() ? "-" : tpAmb), DANFSE_FONTE_TOPO));
		celInfoTopo.addElement(pInfoTopo);
		cabecalho.addCell(celInfoTopo);
		document.add(cabecalho);
		document.add(linhaFina());

		// ---- Chave de acesso + QR ----
		PdfPTable linhaChave = new PdfPTable(new float[] { 4f, 1f });
		linhaChave.setWidthPercentage(100);
		linhaChave.setSpacingBefore(4f);
		PdfPCell celChave = celulaSemBorda();
		Paragraph pChaveLabel = new Paragraph("CHAVE DE ACESSO DA NFS-e", DANFSE_FONTE_SECAO);
		celChave.addElement(pChaveLabel);
		celChave.addElement(new Paragraph(chaveAcesso, DANFSE_FONTE_VALOR_GRANDE));
		linhaChave.addCell(celChave);

		PdfPCell celQr = celulaSemBorda();
		celQr.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
		try {
			// Formato confirmado decodificando o QR de uma NFS-e real baixada do portal
			// nacional -- "tpc=1" parece ser o tipo de consulta (NFS-e).
			String urlConsultaQr = "https://www.nfse.gov.br/ConsultaPublica?tpc=1&chave=" + chaveAcesso;
			com.google.zxing.qrcode.QRCodeWriter qrWriter = new com.google.zxing.qrcode.QRCodeWriter();
			com.google.zxing.common.BitMatrix matrix = qrWriter.encode(urlConsultaQr,
					com.google.zxing.BarcodeFormat.QR_CODE, 110, 110);
			ByteArrayOutputStream qrBaos = new ByteArrayOutputStream();
			javax.imageio.ImageIO.write(com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(matrix),
					"png", qrBaos);
			Image qrImage = Image.getInstance(qrBaos.toByteArray());
			qrImage.scaleToFit(70f, 70f);
			celQr.addElement(qrImage);
		} catch (Exception e) {
			// sem QR code nao impede o resto do documento
		}
		linhaChave.addCell(celQr);
		document.add(linhaChave);

		Paragraph avisoAutenticidade = new Paragraph(
				"A autenticidade desta NFS-e pode ser verificada pela leitura deste código QR ou pela consulta "
						+ "da chave de acesso no portal nacional da NFS-e (www.nfse.gov.br/ConsultaPublica)",
				DANFSE_FONTE_LABEL);
		avisoAutenticidade.setSpacingAfter(4f);
		document.add(avisoAutenticidade);
		document.add(linhaFina());

		// ---- Dados gerais da nota ----
		document.add(tabelaCampos(3, new String[][] { { "NÚMERO DA NFS-e", numeroNFSe },
				{ "COMPETÊNCIA DA NFS-e", competencia }, { "DATA E HORA DA EMISSÃO DA NFS-e", dhProc } }));
		document.add(tabelaCampos(3, new String[][] { { "NÚMERO DA DPS", numDps },
				{ "SÉRIE DA DPS", serieDps }, { "DATA E HORA DA EMISSÃO DA DPS", dhEmiDps } }));
		document.add(tabelaCampos(3, new String[][] { { "EMITENTE DA NFS-e", "Prestador" },
				{ "SITUAÇÃO DA NFS-e", "NFS-e Gerada" }, { "FINALIDADE", "-" } }));

		// ---- Prestador ----
		document.add(barraSecao("PRESTADOR / FORNECEDOR"));
		String ibgeFormatado = cMun.length() == 7 ? cMun.substring(0, 2) + "." + cMun.substring(2) : cMun;
		String cepFormatado = emitCep.length() == 8
				? emitCep.substring(0, 2) + "." + emitCep.substring(2, 5) + "-" + emitCep.substring(5)
				: emitCep;
		document.add(tabelaCampos(3, new String[][] { { "CNPJ / CPF / NIF", Formatador.formataCnpj(emitCnpj) },
				{ "Indicador Municipal (Inscrição)", imPrest.isEmpty() ? "-" : imPrest },
				{ "Telefone", emitFone.isEmpty() ? "-" : emitFone } }));
		document.add(tabelaCampos(3, new String[][] { { "Nome / Nome Empresarial", emitNome },
				{ "Município / Sigla UF", xLocEmi + " / " + emitUF },
				{ "Código IBGE / CEP", ibgeFormatado + " / " + cepFormatado } }));
		String enderecoPrestador = emitLgr + (emitNro.isEmpty() ? "" : ", " + emitNro)
				+ (emitBairro.isEmpty() ? "" : ", " + emitBairro);
		document.add(tabelaCampos(2, new String[][] { { "Endereço", enderecoPrestador },
				{ "E-mail", emitEmail.isEmpty() ? "-" : emitEmail } }));
		String textoSimplesNacional = "3".equals(opSimpNac) ? "Optante - Microempresa ou Empresa de Pequeno Porte"
				: "1".equals(opSimpNac) ? "Não Optante" : "2".equals(opSimpNac) ? "Optante - MEI" : "-";
		document.add(tabelaCampos(2,
				new String[][] { { "Simples Nacional na Data de Competência", textoSimplesNacional },
						{ "Regime de Apuração Tributária pelo SN",
								"Regime de apuração dos tributos federais e municipal pelo Simples Nacional" } }));

		// ---- Tomador ----
		document.add(barraSecao("TOMADOR / ADQUIRENTE"));
		String docTomador = !tomaCpf.isEmpty() ? Formatador.formataCpf(tomaCpf)
				: (!tomaCnpj.isEmpty() ? Formatador.formataCnpj(tomaCnpj) : "-");
		document.add(tabelaCampos(3, new String[][] { { "CNPJ / CPF / NIF", docTomador },
				{ "Indicador Municipal (Inscrição)", "-" }, { "Telefone", "-" } }));
		document.add(tabelaCampos(2,
				new String[][] { { "Nome / Nome Empresarial", tomaNome.isEmpty() ? "-" : tomaNome },
						{ "Município / Sigla UF", "-" } }));

		// ---- Servico ----
		document.add(barraSecao("SERVIÇO PRESTADO"));
		String codTribFormatado = cTribNac.length() == 6
				? cTribNac.substring(0, 2) + "." + cTribNac.substring(2, 4) + "." + cTribNac.substring(4)
				: cTribNac;
		String codNbsFormatado = cNBS.length() == 9
				? cNBS.substring(0, 1) + "." + cNBS.substring(1, 5) + "." + cNBS.substring(5, 7) + "."
						+ cNBS.substring(7)
				: cNBS;
		document.add(tabelaCampos(3, new String[][] { { "Código de Tributação Nacional/Municipal", codTribFormatado },
				{ "Código da NBS", codNbsFormatado }, { "Local da Prestação / UF", xLocEmi + " / " + emitUF } }));
		document.add(paragrafoCampo(xTribNac.isEmpty() ? xNBSDesc : xTribNac));
		document.add(tabelaCampos(1, new String[][] { { "Descrição do Serviço", descServ } }));

		// ---- Tributacao ----
		document.add(barraSecao("TRIBUTAÇÃO MUNICIPAL (ISSQN)"));
		String textoTribISSQN = "1".equals(tribISSQN) ? "Operação Tributável"
				: "2".equals(tribISSQN) ? "Imunidade" : "3".equals(tribISSQN) ? "Exportação de Serviço" : "-";
		String textoRetISSQN = "1".equals(tpRetISSQN) ? "Não Retido"
				: "2".equals(tpRetISSQN) ? "Retido pelo Tomador" : "-";
		document.add(tabelaCampos(2, new String[][] { { "Tipo de Tributação do ISSQN", textoTribISSQN },
				{ "Município / Sigla UF de Incidência do ISSQN", xLocEmi + " / " + emitUF } }));
		document.add(tabelaCampos(2, new String[][] { { "Retenção do ISSQN", textoRetISSQN },
				{ "ISSQN Apurado", "-" } }));

		// ---- Valor total ----
		document.add(barraSecao("VALOR TOTAL DA NFS-e"));
		String valorFormatado = vLiq.isEmpty() ? "-" : "R$ " + vLiq.replace(".", ",");
		document.add(tabelaCampos(2, new String[][] { { "Valor da Operação / Serviço", valorFormatado },
				{ "Valor Líquido da NFS-e", valorFormatado } }));

		// ---- Informacoes complementares ----
		document.add(barraSecao("INFORMAÇÕES COMPLEMENTARES"));
		String totaisAproximados = pTotTribSN.isEmpty() ? "Totais aproximados dos tributos não informados."
				: "Percentual aproximado dos tributos (Lei nº 12.741/2012), conforme regime do Simples Nacional: "
						+ pTotTribSN.replace(".", ",") + "%.";
		document.add(paragrafoCampo(totaisAproximados));

		Paragraph rodape = new Paragraph("Id. DPS: " + idDps, DANFSE_FONTE_LABEL);
		rodape.setSpacingBefore(10f);
		document.add(rodape);

		document.close();
		return baos.toByteArray();
	}

	private static PdfPCell celulaSemBorda() {
		PdfPCell cel = new PdfPCell();
		cel.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
		return cel;
	}

	private static com.lowagie.text.pdf.PdfPTable linhaFina() {
		com.lowagie.text.pdf.PdfPTable t = new com.lowagie.text.pdf.PdfPTable(1);
		t.setWidthPercentage(100);
		PdfPCell c = new PdfPCell();
		c.setBorder(com.lowagie.text.Rectangle.BOTTOM);
		c.setBorderColor(Color.BLACK);
		c.setFixedHeight(2f);
		t.addCell(c);
		return t;
	}

	private static com.lowagie.text.pdf.PdfPTable barraSecao(String titulo) {
		com.lowagie.text.pdf.PdfPTable t = new com.lowagie.text.pdf.PdfPTable(1);
		t.setWidthPercentage(100);
		t.setSpacingBefore(4f);
		PdfPCell c = new PdfPCell(new Phrase(titulo, DANFSE_FONTE_SECAO));
		c.setBackgroundColor(DANFSE_CINZA_SECAO);
		c.setPadding(3f);
		c.setBorder(com.lowagie.text.Rectangle.BOX);
		c.setBorderColor(Color.LIGHT_GRAY);
		t.addCell(c);
		return t;
	}

	/** Uma "linha" de campos lado a lado, cada um com label pequeno em cima e valor
	 * embaixo -- igual ao padrao visual do DANFSe oficial. */
	private static com.lowagie.text.pdf.PdfPTable tabelaCampos(int colunas, String[][] labelsEValores) {
		com.lowagie.text.pdf.PdfPTable t = new com.lowagie.text.pdf.PdfPTable(colunas);
		t.setWidthPercentage(100);
		for (String[] par : labelsEValores) {
			PdfPCell c = new PdfPCell();
			c.setBorder(com.lowagie.text.Rectangle.BOX);
			c.setBorderColor(Color.LIGHT_GRAY);
			c.setBackgroundColor(DANFSE_CINZA_LINHA);
			c.setPadding(3f);
			Paragraph p = new Paragraph(par[0], DANFSE_FONTE_LABEL);
			p.add(new Phrase("\n" + (par[1] == null || par[1].trim().isEmpty() ? "-" : par[1]), DANFSE_FONTE_VALOR));
			c.addElement(p);
			t.addCell(c);
		}
		return t;
	}

	private static Paragraph paragrafoCampo(String texto) {
		Paragraph p = new Paragraph(texto == null || texto.isEmpty() ? "-" : texto, DANFSE_FONTE_VALOR);
		p.setSpacingBefore(2f);
		p.setSpacingAfter(2f);
		return p;
	}

	private static String formataDataSimples(String dataIso) {
		// formato esperado: 2026-11-12
		if (dataIso == null || dataIso.length() < 10) {
			return dataIso;
		}
		return dataIso.substring(8, 10) + "/" + dataIso.substring(5, 7) + "/" + dataIso.substring(0, 4);
	}

	private static String extrairDanfseData(String dataHoraIso) {
		// formato esperado: 2026-11-12T09:24:06-03:00 -> 12/11/2026 09:24:06
		if (dataHoraIso == null || dataHoraIso.length() < 19) {
			return dataHoraIso;
		}
		String data = formataDataSimples(dataHoraIso.substring(0, 10));
		String hora = dataHoraIso.substring(11, 19);
		return data + " " + hora;
	}

	private static String extrairDanfse(String xml, String regex) {
		java.util.regex.Matcher m = java.util.regex.Pattern.compile(regex, java.util.regex.Pattern.DOTALL)
				.matcher(xml);
		return m.find() ? m.group(1) : "";
	}

}



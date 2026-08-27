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
	
}

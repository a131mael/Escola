package org.escola.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.IRunElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.escola.util.CompactadorZip;

public class OfficeDOCUtil {

	public void editDoc(String endereco, Map<String, String> trocas, String nomeArquivoSaida) throws IOException {
		OutputStream writer = null;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(
					new FileInputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath(endereco)));
			HWPFDocument doc = new HWPFDocument(fs);
			writer = new FileOutputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + File.separator
					+ nomeArquivoSaida + ".doc");

			// remove as clausulas nao utilizadas - Contrato escolar
			String idaEVolta = "CLAUSULA 6ª – O CONTRATANTE compromete-se a deixar o TRANSPORTADO pronto e aguardando pelo CONTRATADO no endereço e hora combinada, ou seja, na rua  #CONTRATANTERUA   as #DADOSGERAISHORARIO1,  não tolerando qualquer tipo de atraso ou mudança de endereço.";
			String ida = "CLAUSULA 6ª - O CONTRATADO SO SE RESPONSABILIZARA PELO TRANSPORTE DE IDA PARA A ESCOLA, O TRANSPORTE DE VOLTA DA ESCOLA È DE RESPONSABILIDADE DO CONTRATANTE.";
			String volta = "CLAUSULA 6ªB – O CONTRATADO SO SE RESPONSABILIZARA PELO TRANSPORTE DE VOLTA DA ESCOLA, O TRANSPORTE DE IDA PARA A ESCOLA È DE RESPONSABILIDADE DO CONTRATANTE.";

			String carro1 = " - Ônibus de 48 Lugares com ar condicionado.";
			String carro2 = "  - Ônibus de 46 Lugares com ar condicionado.";
			String carro3 = "  - Micro-Ônibus de 22 Lugares com ar condicionado.";
			String carro4 = "  - Micro-Ônibus de 20 Lugares sem ar condicionado.";
			String carro5 = "  - Van de 16 Lugares sem ar condicionado.";

			if (Boolean.parseBoolean(trocas.get("#ONIBUS1"))) {
				doc.getRange().replaceText("#ONIBUS1", carro1);
			} else {
				doc.getRange().replaceText("#ONIBUS1", "");
			}

			if (Boolean.parseBoolean(trocas.get("#ONIBUS2"))) {
				doc.getRange().replaceText("#ONIBUS2", carro2);
			} else {
				doc.getRange().replaceText("#ONIBUS2", "");
			}

			if (Boolean.parseBoolean(trocas.get("#ONIBUS3"))) {
				doc.getRange().replaceText("#ONIBUS3", carro3);
			} else {
				doc.getRange().replaceText("#ONIBUS3", "");
			}

			if (Boolean.parseBoolean(trocas.get("#ONIBUS4"))) {
				doc.getRange().replaceText("#ONIBUS4", carro4);
			} else {
				doc.getRange().replaceText("#ONIBUS4", "");
			}

			if (Boolean.parseBoolean(trocas.get("#ONIBUS5"))) {
				doc.getRange().replaceText("#ONIBUS5", carro5);
			} else {
				doc.getRange().replaceText("#ONIBUS5", "");
			}

			if (trocas.get("#REMOVER") != null) {

				switch (trocas.get("#REMOVER")) {
				case "1":
					doc.getRange().replaceText("#TIPOCONTRATO", idaEVolta);
					break;

				case "2":
					doc.getRange().replaceText("#TIPOCONTRATO", ida);
					break;

				case "3":
					doc.getRange().replaceText("#TIPOCONTRATO", volta);
					break;

				default:
					break;
				}

			}

			// faz o replace do que esta no map
			for (Map.Entry<String, String> entry : trocas.entrySet()) {
				doc.getRange().replaceText(entry.getKey(), entry.getValue());
			}

			doc.write(writer);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {

			}
		}
	}

	/** Gera um único arquivo combinando o documento principal (ex: contrato) com um
	 * documento anexo (ex: termo de consentimento), cada um com seu próprio mapa de
	 * substituições — o anexo entra como folha(s) separada(s) no final, após quebra de
	 * página, mantendo a assinatura própria do anexo. */
	public void editDoc2ComAnexo(String enderecoPrincipal, Map<String, String> trocasPrincipal,
			String enderecoAnexo, Map<String, String> trocasAnexo, String nomeArquivoSaida) throws IOException {
		editDoc2ComAnexos(enderecoPrincipal, trocasPrincipal,
				java.util.Collections.singletonList(enderecoAnexo), java.util.Collections.singletonList(trocasAnexo),
				nomeArquivoSaida);
	}

	/** Igual a editDoc2ComAnexo, mas aceita vários anexos — cada um entra como folha(s)
	 * separada(s), na ordem da lista, um atrás do outro, cada um com quebra de página
	 * antes. Usado por ex. quando o contrato precisa sair com o termo de consentimento
	 * de imagem e, se for o caso, o regimento escolar do segmento do aluno. */
	public void editDoc2ComAnexos(String enderecoPrincipal, Map<String, String> trocasPrincipal,
			List<String> enderecosAnexos, List<Map<String, String>> trocasAnexos, String nomeArquivoSaida) throws IOException {

		OutputStream writer = null;
		try {
			XWPFDocument principal = new XWPFDocument(
					new FileInputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath(enderecoPrincipal)));
			for (XWPFTable table : principal.getTables()) {
				for (XWPFTableRow linha : table.getRows()) {
					for (XWPFTableCell celula : linha.getTableCells()) {
						replaceParagrapfs(celula.getParagraphs(), trocasPrincipal);
					}
				}
			}
			replaceParagrapfs(principal.getParagraphs(), trocasPrincipal);

			for (int i = 0; i < enderecosAnexos.size(); i++) {
				XWPFDocument anexo = new XWPFDocument(new FileInputStream(
						FacesContext.getCurrentInstance().getExternalContext().getRealPath(enderecosAnexos.get(i))));
				replaceParagrapfs(anexo.getParagraphs(), trocasAnexos.get(i));

				// quebra de página antes de cada anexo
				principal.createParagraph().createRun().addBreak(org.apache.poi.xwpf.usermodel.BreakType.PAGE);

				for (XWPFParagraph origem : anexo.getParagraphs()) {
					XWPFParagraph destino = principal.createParagraph();
					copiarParagrafo(origem, destino);
				}
			}

			writer = new FileOutputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + File.separator
					+ nomeArquivoSaida + ".doc");
			principal.write(writer);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	private static void copiarParagrafo(XWPFParagraph origem, XWPFParagraph destino) {
		if (origem.getStyle() != null) {
			destino.setStyle(origem.getStyle());
		}
		if (origem.getAlignment() != null) {
			destino.setAlignment(origem.getAlignment());
		}
		destino.setIndentationLeft(origem.getIndentationLeft());
		destino.setSpacingAfter(origem.getSpacingAfter());
		for (XWPFRun origemRun : origem.getRuns()) {
			XWPFRun destinoRun = destino.createRun();
			destinoRun.setText(origemRun.text());
			destinoRun.setBold(origemRun.isBold());
			destinoRun.setItalic(origemRun.isItalic());
			if (origemRun.getFontSize() != -1) {
				destinoRun.setFontSize(origemRun.getFontSize());
			}
		}
	}

	public void editDoc2(String endereco, Map<String, String> trocas, String nomeArquivoSaida) throws IOException {

		OutputStream writer = null;

		try {
			XWPFDocument docx = new XWPFDocument(
					new FileInputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath(endereco)));

			writer = new FileOutputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + File.separator
					+ nomeArquivoSaida + ".doc");
			// faz o replace do que esta no map
			for (Map.Entry<String, String> entry : trocas.entrySet()) {
				for (XWPFTable table : docx.getTables()) {
					for (XWPFTableRow linha : table.getRows()) {
						for (XWPFTableCell celula : linha.getTableCells()) {
							replaceParagrapfs(celula.getParagraphs(), trocas);
						}
					}
				}
				replaceParagrapfs(docx.getParagraphs(), trocas);

			}

			docx.write(writer);

		} catch (OLE2NotOfficeXmlFileException ole2) {
			editDoc(endereco, trocas, nomeArquivoSaida);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {

			}
		}
	}

	/** Igual ao editDoc2, mas recebe caminhos absolutos em vez de resolver via
	 * FacesContext — necessário pra chamar fora de uma requisição JSF (ex: recurso
	 * REST usado pelo portal do responsável), onde FacesContext.getCurrentInstance()
	 * é nulo. */
	public void editDoc2CaminhoAbsoluto(String caminhoTemplateAbsoluto, Map<String, String> trocas,
			String caminhoSaidaAbsoluto) throws IOException {

		OutputStream writer = null;

		try {
			XWPFDocument docx = new XWPFDocument(new FileInputStream(caminhoTemplateAbsoluto));

			writer = new FileOutputStream(caminhoSaidaAbsoluto);
			for (Map.Entry<String, String> entry : trocas.entrySet()) {
				for (XWPFTable table : docx.getTables()) {
					for (XWPFTableRow linha : table.getRows()) {
						for (XWPFTableCell celula : linha.getTableCells()) {
							replaceParagrapfs(celula.getParagraphs(), trocas);
						}
					}
				}
				replaceParagrapfs(docx.getParagraphs(), trocas);
			}

			docx.write(writer);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {

			}
		}
	}

	public void editDocTemp(String endereco, Map<String, String> trocas, String nomeArquivoSaida) throws IOException {

		OutputStream writer = null;

		try {
			XWPFDocument docx = new XWPFDocument(
					new FileInputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath(endereco)));

			String caminhoFinalPasta = System.getProperty("java.io.tmpdir") +  System.getProperty("file.separator") +  nomeArquivoSaida + ".doc";
			writer = new FileOutputStream(caminhoFinalPasta);
			// faz o replace do que esta no map
			for (Map.Entry<String, String> entry : trocas.entrySet()) {
				for (XWPFTable table : docx.getTables()) {
					for (XWPFTableRow linha : table.getRows()) {
						for (XWPFTableCell celula : linha.getTableCells()) {
							replaceParagrapfs(celula.getParagraphs(), trocas);
						}
					}
				}
				replaceParagrapfs(docx.getParagraphs(), trocas);

			}

			docx.write(writer);

		} catch (OLE2NotOfficeXmlFileException ole2) {
			editDoc(endereco, trocas, nomeArquivoSaida);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Path caminho onde se encontra os docs(devem estar na mesma pasta)
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 **/
	public static void unionDocs(String path, String nomeArquivoSaida) throws FileNotFoundException, IOException {
		CompactadorZip.createDocFile(path + File.separator + nomeArquivoSaida);
		XWPFDocument docx = null;
		OutputStream writer = new FileOutputStream(path + File.separator + nomeArquivoSaida);
		try {
			for (String arquivo : CompactadorZip.getFiles(path)) {
				docx = new XWPFDocument(new FileInputStream(path + File.separator  + arquivo));
				docx.write(writer);
				docx.close();
			}
		} catch (OLE2NotOfficeXmlFileException ole2) {
			ole2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Path caminho onde se encontra os docs(devem estar na mesma pasta)
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 **/
	public static void unionDocs2(String path, String nomeArquivoSaida) throws FileNotFoundException, IOException {
		CompactadorZip.createDocFile(path + File.separator + nomeArquivoSaida);
		XWPFDocument docx = null;
		OutputStream writer = new FileOutputStream(path + File.separator + nomeArquivoSaida);
		try {
			for (String arquivo : CompactadorZip.getFiles(path)) {
				docx = new XWPFDocument(new FileInputStream(path + File.separator  + arquivo));
				docx.write(writer);
				writer.close();
				docx.close();
			}
		} catch (OLE2NotOfficeXmlFileException ole2) {
			ole2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	
	private static void replaceParagrapfs(List<XWPFParagraph> paragrafs, Map<String, String> trocas) {
		for (XWPFParagraph paragrafo : paragrafs) {
			for (Map.Entry<String, String> entry : trocas.entrySet()) {
				if (paragrafo.getText().contains(entry.getKey())) {
					replaceText(paragrafo, entry.getKey(), entry.getValue());
				}
			}
		}

	}

	private static void replaceText(XWPFParagraph p, String findText, String replaceText) {
		for (XWPFRun linha : p.getRuns()) {
			if (linha != null && linha.text() != null && findText != null && replaceText != null) {
				if(findText.equalsIgnoreCase(linha.text())){
					linha.setText(linha.text().replace(findText, replaceText), 0);
				}
			}
		}
	}

	private static void replaceParagrapfs2(List<XWPFParagraph> paragrafs, Map<String, String> trocas) {
		for (XWPFParagraph paragrafo : paragrafs) {
			for (XWPFRun linha : paragrafo.getRuns()) {
				trocas(linha, trocas);
			}
		}
	}

	private static void trocas(XWPFRun p, Map<String, String> trocas) {
		for (Map.Entry<String, String> entry : trocas.entrySet()) {
			if (p.text().contains(entry.getKey())) {
				p.setText(p.text().replace(entry.getKey(), entry.getValue()), 0);
			}
		}
	}

}

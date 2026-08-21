package org.escola.util;

import java.io.IOException;
import java.util.HashMap;

import org.escola.controller.OfficeDOCUtil;
import org.escola.model.Aluno;

public class ImpressoesUtils {

	private static OfficeDOCUtil officeDOCUtil = new OfficeDOCUtil();

	public static void gerarArquivoFisico(String modelo, HashMap<String, String> trocas, String nomeArquivoSaida) throws IOException {

		try {
			officeDOCUtil.editDoc2(modelo, trocas, nomeArquivoSaida);

			/*
			 * String caminho =
			 * FacesContext.getCurrentInstance().getExternalContext().
			 * getRealPath("/") + "\\"+nomeArquivoSaida + ".doc"; Process pro =
			 * Runtime.getRuntime().exec("cmd.exe /c  " + caminho);
			 * pro.waitFor(); System.out.println("ccc");
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void imprimirInformacoesAluno(Aluno aluno, String modelo, HashMap<String, String> trocas,
			String nomeArquivoSaida) throws IOException {

		try {
			officeDOCUtil.editDoc2(modelo, trocas, nomeArquivoSaida);

			/*
			 * String caminho =
			 * FacesContext.getCurrentInstance().getExternalContext().
			 * getRealPath("/") + "\\"+nomeArquivoSaida + ".doc"; Process pro =
			 * Runtime.getRuntime().exec("cmd.exe /c  " + caminho);
			 * pro.waitFor(); System.out.println("ccc");
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void imprimirInformacoesAluno(String modelo, HashMap<String, String> trocas, String nomeArquivoSaida) throws IOException {

		try {
			officeDOCUtil.editDoc2(modelo, trocas, nomeArquivoSaida);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Gera o documento principal (ex: contrato) já com o anexo (ex: termo de
	 * consentimento) como folha(s) separada(s) no final do mesmo arquivo. */
	public static void imprimirInformacoesAlunoComAnexo(String modeloPrincipal, HashMap<String, String> trocasPrincipal,
			String modeloAnexo, HashMap<String, String> trocasAnexo, String nomeArquivoSaida) throws IOException {

		try {
			officeDOCUtil.editDoc2ComAnexo(modeloPrincipal, trocasPrincipal, modeloAnexo, trocasAnexo, nomeArquivoSaida);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Igual, mas aceita vários anexos, um atrás do outro, na ordem da lista. */
	public static void imprimirInformacoesAlunoComAnexos(String modeloPrincipal, HashMap<String, String> trocasPrincipal,
			java.util.List<String> modelosAnexos, java.util.List<java.util.HashMap<String, String>> trocasAnexos,
			String nomeArquivoSaida) throws IOException {

		try {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			java.util.List<java.util.Map<String, String>> trocasAnexosMap = (java.util.List) trocasAnexos;
			officeDOCUtil.editDoc2ComAnexos(modeloPrincipal, trocasPrincipal, modelosAnexos, trocasAnexosMap, nomeArquivoSaida);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void imprimirInformacoesAlunoTemp(Aluno aluno, String modelo, HashMap<String, String> trocas,
			String nomeArquivoSaida) throws IOException {

		try {
			officeDOCUtil.editDocTemp(modelo, trocas, nomeArquivoSaida);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

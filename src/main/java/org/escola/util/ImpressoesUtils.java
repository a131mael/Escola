package org.escola.util;

import java.io.IOException;
import java.util.HashMap;

import org.escola.controller.OfficeDOCUtil;
import org.escola.model.Aluno;

public class ImpressoesUtils {

	private static OfficeDOCUtil officeDOCUtil = new OfficeDOCUtil();

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
	
	public static void imprimirInformacoesAlunoTemp(Aluno aluno, String modelo, HashMap<String, String> trocas,
			String nomeArquivoSaida) throws IOException {

		try {
			officeDOCUtil.editDocTemp(modelo, trocas, nomeArquivoSaida);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

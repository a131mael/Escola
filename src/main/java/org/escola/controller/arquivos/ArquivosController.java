package org.escola.controller.arquivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.escola.auth.AuthController;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class ArquivosController extends AuthController implements Serializable {

	private static final long serialVersionUID = 1L;

	// Pasta onde a impressora (scan-to-FTP, IP 192.168.15.200) deixa os PDFs escaneados,
	// organizados em subpastas por data (AAAA-MM-DD). Compartilhada entre Favo e Adonai -
	// os dois sistemas leem daqui, ninguém grava por aqui via aplicação.
	private static final String PASTA_SCAN = File.separator + "home" + File.separator + "servidor" + File.separator + "SCAN";

	private List<ArquivoEscaneado> arquivos;

	@PostConstruct
	public void init() {
		carregarArquivos();
	}

	private void carregarArquivos() {
		arquivos = new ArrayList<>();
		File raiz = new File(PASTA_SCAN);
		File[] pastasData = raiz.listFiles(File::isDirectory);
		if (pastasData == null) {
			return;
		}
		for (File pastaData : pastasData) {
			// só pastas no formato AAAA-MM-DD - pula BACKUPS_HD e qualquer outra coisa avulsa
			if (!pastaData.getName().matches("\\d{4}-\\d{2}-\\d{2}")) {
				continue;
			}
			File[] arquivosNaPasta = pastaData.listFiles(File::isFile);
			if (arquivosNaPasta == null) {
				continue;
			}
			for (File arquivo : arquivosNaPasta) {
				arquivos.add(new ArquivoEscaneado(pastaData.getName(), arquivo));
			}
		}
		arquivos.sort(Comparator.comparingLong((ArquivoEscaneado a) -> a.getArquivo().lastModified()).reversed());
	}

	public StreamedContent baixar(ArquivoEscaneado arquivoEscaneado) throws IOException {
		InputStream stream = new FileInputStream(arquivoEscaneado.getArquivo());
		return new DefaultStreamedContent(stream, "application/pdf", arquivoEscaneado.getNome());
	}

	public List<ArquivoEscaneado> getArquivos() {
		return arquivos;
	}

	public static class ArquivoEscaneado implements Serializable {

		private static final long serialVersionUID = 1L;

		private final String pasta;
		private final File arquivo;

		public ArquivoEscaneado(String pasta, File arquivo) {
			this.pasta = pasta;
			this.arquivo = arquivo;
		}

		public String getPasta() {
			return pasta;
		}

		public File getArquivo() {
			return arquivo;
		}

		public String getNome() {
			return arquivo.getName();
		}

		public Date getDataModificacao() {
			return new Date(arquivo.lastModified());
		}

		public long getTamanho() {
			return arquivo.length();
		}
	}
}

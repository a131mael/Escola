/*
d * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.escola.service.rotinasAutomaticas;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.aaf.financeiro.model.Pagador;
import org.aaf.financeiro.sicoob.util.CNAB240_REMESSA_SICOOB;
import org.aaf.financeiro.sicoob.util.CNAB240_RETORNO_SICOOB;
import org.aaf.financeiro.util.ImportadorArquivo;
import org.aaf.financeiro.util.OfficeUtil;
import org.aaf.financeiro.util.constantes.Constante;
import org.escola.enums.StatusBoletoEnum;
import org.escola.model.Aluno;
import org.escola.model.Boleto;
import org.escola.model.ContratoAluno;
import org.escola.service.ConfiguracaoService;
import org.escola.service.FinanceiroEscolaService;
import org.escola.util.Verificador;

/**
 *
 * @author martin
 */
@Stateless
@LocalBean
public class CNAB240 {

	@Inject
	private ConfiguracaoService configuracaoService;

	@Inject
	private FinanceiroEscolaService financeiroService;

	public byte[] gerarCNB240(int projeto, Aluno aluno, ContratoAluno contrato) {
		try {

			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";

			Pagador pagador = new Pagador();
			pagador.setBairro(contrato.getBairro());
			pagador.setCep(contrato.getCep());
			pagador.setCidade(contrato.getCidade() != null ? contrato.getCidade() : "PALHOCA");
			pagador.setCpfCNPJ(contrato.getCpfResponsavel());
			pagador.setEndereco(contrato.getEndereco());
			pagador.setNome(contrato.getNomeResponsavel());
			pagador.setNossoNumero(aluno.getCodigo());
			pagador.setUF("SC");
			pagador.setBoletos(aluno.getBoletosFinanceiro());
			CNAB240_REMESSA_SICOOB cnbRemessa = new CNAB240_REMESSA_SICOOB(projeto);
			byte[] arquivo = cnbRemessa.geraRemessa(pagador, sequencialArquivo);

			try {
				configuracaoService.incrementaSequencialArquivoCNAB();
				return arquivo;

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] gerarCNB240(int projeto, ContratoAluno contrato, Boleto b) {
		try {

			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";

			Pagador pagador = new Pagador();
			pagador.setBairro(contrato.getBairro());
			pagador.setCep(contrato.getCep());
			pagador.setCidade(contrato.getCidade() != null ? contrato.getCidade() : "PALHOCA");
			pagador.setCpfCNPJ(contrato.getCpfResponsavel());
			pagador.setEndereco(contrato.getEndereco());
			pagador.setNome(contrato.getNomeResponsavel());
			pagador.setNossoNumero(contrato.getNumero());
			pagador.setUF("SC");
			List<org.aaf.financeiro.model.Boleto> boletos = new ArrayList<>();
			boletos.add(b.getBoletoFinanceiro());
			pagador.setBoletos(boletos);
			CNAB240_REMESSA_SICOOB cnbRemessa = new CNAB240_REMESSA_SICOOB(projeto);
			byte[] arquivo = cnbRemessa.geraRemessa(pagador, sequencialArquivo);

			try {
				configuracaoService.incrementaSequencialArquivoCNAB();
				return arquivo;

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] gerarCNB240Baixa(int projeto, Boleto b) {
		try {

			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";

			Pagador pagador = new Pagador();
			pagador.setBairro(b.getContrato().getBairro());
			pagador.setCep(b.getContrato().getCep());
			pagador.setCidade(b.getContrato().getCidade() != null ? b.getContrato().getCidade() : "PALHOCA");
			pagador.setCpfCNPJ(b.getContrato().getCpfResponsavel());
			pagador.setEndereco(b.getContrato().getEndereco());
			pagador.setNome(b.getContrato().getNomeResponsavel());
			pagador.setNossoNumero(b.getContrato().getNumero());
			pagador.setUF("SC");
			ArrayList<Boleto> boletos = new ArrayList<>();
			boletos.add(b);
			pagador.setBoletos(getBoletosFinanceiro(boletos));
			CNAB240_REMESSA_SICOOB cnbRemessa = new CNAB240_REMESSA_SICOOB(projeto);
			byte[] arquivo = cnbRemessa.geraBaixa(pagador, sequencialArquivo);

			try {
				configuracaoService.incrementaSequencialArquivoCNAB();
				return arquivo;

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void gerarBaixaBoletosPagos(Boleto b, String path) {
		byte[] arquivo = gerarCNB240Baixa(CONSTANTES.projeto, b);
		String nomeArquivo = "CNAB240_" + b.getNossoNumero() + ".txt";
		ImportadorArquivo.geraArquivoFisico(arquivo, path + nomeArquivo);
		b.setBaixaGerada(true);
		financeiroService.save(b);
	}

	public void gerarBaixaBoletosCancelados(Boleto b, String path) {
		byte[] arquivo = gerarCNB240Baixa(CONSTANTES.projeto, b);
		String nomeArquivo = "CNAB240_" + b.getNossoNumero() + ".txt";
		ImportadorArquivo.geraArquivoFisico(arquivo, path + nomeArquivo);
		b.setBaixaGerada(true);
		b.setCancelado(true);
		b.setValorPago((double) 0);
		b.setCnabCanceladoEnviado(true);
		financeiroService.save(b);
	}

	public void gerarCNABAlunos() {
		List<Aluno> alunosSemCNABENVIADO = financeiroService.getAlunosCNABNaoEnviado();
		for (Aluno al : alunosSemCNABENVIADO) {
			for (ContratoAluno contrato : al.getContratosVigentes()) {
				byte[] arquivo = gerarCNB240(CONSTANTES.projeto, al, contrato);
				String nomeArquivo = "CNAB240_" + al.getCodigo() + System.currentTimeMillis() + ".txt";
				ImportadorArquivo.geraArquivoFisico(arquivo, CONSTANTES.PATH_ENVIAR_CNAB + nomeArquivo);
				financeiroService.saveCNABENviado(al);
			}
		}
	}

	public void gerarCNABAlunos(int quantidadeDeMeses) {
		List<Boleto> boletosNAOEnviados = financeiroService.getBoletosCNABNaoEnviado(quantidadeDeMeses);
		int contador = 1;
		for (Boleto b : boletosNAOEnviados) {
			if (enviarBoletoParaBanco(b, contador)) {
				contador++;
			}
		}
	}

	// Gerenciador (Raspberry Pi) -- quem de fato gera/envia CNAB pro banco pela
	// rotina automatica. O webapp Escola nao tem acesso ao disco dele, entao o
	// envio manual avulso manda o arquivo por SFTP direto pra pasta que o
	// Skyunix (cliente do banco) ja vigia la, em vez de escrever localmente
	// (a constante antiga PATH_ENVIAR_CNAB apontava pra um caminho que so
	// existe na Pi, nao no container do Escola -- nunca teria funcionado).
	// Credenciais NAO ficam no código (repo é público) -- configurar via
	// variavel de ambiente do container: GERENCIADOR_SSH_USUARIO e
	// GERENCIADOR_SSH_SENHA.
	private static final String GERENCIADOR_HOST = "192.168.15.10";
	private static final String GERENCIADOR_PASTA_OUTBOX = "/home/server/Skyunix/outbox/";

	private static String getConfig(String nome) {
		String valor = System.getenv(nome);
		if (valor == null || valor.isEmpty()) {
			valor = System.getProperty(nome);
		}
		return valor;
	}

	private void enviarArquivoParaGerenciador(byte[] conteudo, String nomeArquivo) {
		String usuario = getConfig("GERENCIADOR_SSH_USUARIO");
		String senha = getConfig("GERENCIADOR_SSH_SENHA");
		if (usuario == null || senha == null) {
			throw new RuntimeException(
					"Credencial do Gerenciador não configurada (env GERENCIADOR_SSH_USUARIO / GERENCIADOR_SSH_SENHA no container)");
		}

		Session session = null;
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(usuario, GERENCIADOR_HOST, 22);
			session.setPassword(senha);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(10000);

			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect(10000);
			sftp.cd(GERENCIADOR_PASTA_OUTBOX);
			sftp.put(new ByteArrayInputStream(conteudo), nomeArquivo);
		} catch (Exception e) {
			throw new RuntimeException("Falha ao enviar arquivo CNAB pro Gerenciador: " + e.getMessage(), e);
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

	/**
	 * Envia um único boleto pro banco (gera o arquivo de remessa CNAB240 e manda
	 * por SFTP pro Gerenciador, marcando como enviado) — usado pelo envio manual
	 * avulso/em lote a partir da tela de contrato.
	 * Retorna false sem gerar nada se o contrato não tiver os dados mínimos
	 * (CPF/nome/endereço do responsável) exigidos pela remessa.
	 */
	public boolean enviarBoletoParaBanco(Boleto b, int sequencialNoLote) {
		ContratoAluno ca = configuracaoService.findContrato(b.getId());

		if (ca.getCpfResponsavel() == null || ca.getCpfResponsavel().equalsIgnoreCase("")) {
			return false;
		}
		if (ca.getNomeResponsavel() == null || ca.getNomeResponsavel().equalsIgnoreCase("")) {
			return false;
		}
		if (ca.getEndereco() == null || ca.getEndereco().equalsIgnoreCase("")) {
			return false;
		}
		if (ca.getCep() == null || ca.getCep().equalsIgnoreCase("")) {
			ca.setCep("88132700");
		}
		if (ca.getBairro() == null || ca.getBairro().equalsIgnoreCase("")) {
			ca.setBairro("Bela Vista");
		}
		if (ca.getCidade() == null || ca.getCidade().equalsIgnoreCase("")) {
			ca.setCidade("Palhoca");
		}

		LocalDate data = LocalDate.now();
		StringBuilder sb = new StringBuilder();
		sb.append(data.getYear());
		sb.append(data.getMonthValue());
		sb.append(data.getDayOfMonth());

		byte[] arquivo = gerarCNB240(CONSTANTES.projeto, ca, b);
		String nomeArquivo = "COB_756_494960_" + sb + sequencialNoLote + ".REM";
		enviarArquivoParaGerenciador(arquivo, nomeArquivo);
		financeiroService.saveCNABENviado(b);
		return true;
	}

	public void gerarBaixaBoletosPagos() {
		List<Boleto> boletoParaBaixa = financeiroService.getBoletosParaBaixa();
		for (Boleto b : boletoParaBaixa) {
			gerarBaixaBoletosPagos(b, CONSTANTES.PATH_ENVIAR_BAIXA);
		}
	}

	public void gerarBaixaBoletoAlunosCancelados() {
		List<ContratoAluno> contratos = financeiroService.getAlunosRemovidos();
		for (ContratoAluno contrato : contratos) {
			for (Boleto b : contrato.getBoletos()) {
				gerarBaixaBoletosCancelados(b, CONSTANTES.PATH_ENVIAR_BAIXA_CANCELADOS);
			}
		}
	}

	private List<org.aaf.financeiro.model.Boleto> getBoletosFinanceiro(List<Boleto> boletos) {
		List<org.aaf.financeiro.model.Boleto> boletosFinanceiro = new ArrayList<>();
		if (boletos != null) {
			for (Boleto boleto : boletos) {
				org.aaf.financeiro.model.Boleto boletoFinanceiro = new org.aaf.financeiro.model.Boleto();
				boletoFinanceiro.setEmissao(boleto.getEmissao());
				boletoFinanceiro.setId(boleto.getId());
				boletoFinanceiro.setValorNominal(boleto.getValorNominal());
				boletoFinanceiro.setVencimento(boleto.getVencimento());
				boletoFinanceiro.setNossoNumero(String.valueOf(boleto.getNossoNumero()));
				boletoFinanceiro.setDataPagamento(OfficeUtil.retornaDataSomenteNumeros(boleto.getDataPagamento()));
				boletoFinanceiro.setValorPago(boleto.getValorPago());
				boletosFinanceiro.add(boletoFinanceiro);
			}
		}
		return boletosFinanceiro;
	}

	public void importarPagamentosCNAB240() {
		try {
			List<Pagador> boletosImportados = CNAB240_RETORNO_SICOOB
					.imporCNAB240(Constante.LOCAL_ARMAZENAMENTO_REMESSA);
			importarBoletos(boletosImportados, false);

		} catch (Exception e) {

		}
		try {
			ImportadorArquivo arquivo = new ImportadorArquivo();

			List<Pagador> boletosImportados = arquivo.importarExtratoBancarioConciliacao(2);
			importarBoletos(boletosImportados, true);

		} catch (Exception e) {

		}
	}

	public void importarBoletos(List<Pagador> boletosImportados, boolean extratoBancario) throws ParseException {
		for (Pagador pagador : boletosImportados) {
			org.aaf.financeiro.model.Boleto boletoCNAB = pagador.getBoletos().get(0);
			String numeroDocumento = boletoCNAB.getNossoNumero();
			if (numeroDocumento != null && !numeroDocumento.equalsIgnoreCase("") && !numeroDocumento.contains("-")) {
				try {
					numeroDocumento = numeroDocumento.trim().replace(" ", "").replace("/",
							"".replace("-", "").replace(".", ""));
					if (numeroDocumento.matches("^[0-9]*$")) {
						Long numeroDocumentoLong = Long.parseLong(numeroDocumento);
						org.escola.model.Boleto boleto = null;
						if (!extratoBancario) {
							numeroDocumentoLong -= 10000;
							boleto = financeiroService.findBoletoByID(numeroDocumentoLong);
						} else {
							String numeroDocumentoExtrato = String.valueOf(numeroDocumentoLong);
							boleto = financeiroService.findBoletoByNumeroModel(
									numeroDocumentoExtrato.substring(0, numeroDocumentoExtrato.length() - 1));
						}
						if (boleto != null) {
							if (!Verificador.getStatusEnum(boleto).equals(StatusBoletoEnum.PAGO)) {
								if (!(boletoCNAB.isDecurso() != null && boletoCNAB.isDecurso())) {
									boleto.setValorPago(boletoCNAB.getValorPago());
									boleto.setDataPagamento(OfficeUtil.retornaData(boletoCNAB.getDataPagamento()));
									boleto.setConciliacaoPorExtrato(extratoBancario);
									financeiroService.save(boleto);
									System.out.println("YESS, BOLETO PAGO");
								}
							}
						}
					}

				} catch (ClassCastException cce) {
					cce.printStackTrace();
				}
			}
		}
	}

}

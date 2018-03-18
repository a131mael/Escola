/*
d * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.escola.service.rotinasAutomaticas;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.aaf.financeiro.model.Pagador;
import org.aaf.financeiro.sicoob.util.CNAB240_REMESSA_SICOOB;
import org.aaf.financeiro.sicoob.util.CNAB240_RETORNO_SICOOB;
import org.aaf.financeiro.util.ImportadorArquivo;
import org.aaf.financeiro.util.OfficeUtil;
import org.aaf.financeiro.util.constantes.Constante;
import org.escola.enums.StatusBoletoEnum;
import org.escola.model.Aluno;
import org.escola.model.Boleto;
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

	public byte[] gerarCNB240(int projeto, Aluno aluno) {
		try {

			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";

			Pagador pagador = new Pagador();
			pagador.setBairro(aluno.getBairro());
			pagador.setCep(aluno.getCep());
			pagador.setCidade(aluno.getCidade() != null ? aluno.getCidade() : "PALHOCA");
			pagador.setCpfCNPJ(aluno.getCpfResponsavel());
			pagador.setEndereco(aluno.getEndereco());
			pagador.setNome(aluno.getNomeResponsavel());
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

	public byte[] gerarCNB240Baixa(int projeto, Boleto b) {
		try {

			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";

			Pagador pagador = new Pagador();
			pagador.setBairro(b.getPagador().getBairro());
			pagador.setCep(b.getPagador().getCep());
			pagador.setCidade(b.getPagador().getCidade() != null ? b.getPagador().getCidade() : "PALHOCA");
			pagador.setCpfCNPJ(b.getPagador().getCpfResponsavel());
			pagador.setEndereco(b.getPagador().getEndereco());
			pagador.setNome(b.getPagador().getNomeResponsavel());
			pagador.setNossoNumero(b.getPagador().getCodigo());
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
		financeiroService.save(b);
	}
	
	public void gerarCNABAlunos() {
		List<Aluno> alunosSemCNABENVIADO = financeiroService.getAlunosCNABNaoEnviado();
		for (Aluno al : alunosSemCNABENVIADO) {
			byte[] arquivo = gerarCNB240(CONSTANTES.projeto, al);
			String nomeArquivo = "CNAB240_" + al.getCodigo() + ".txt";
			ImportadorArquivo.geraArquivoFisico(arquivo, CONSTANTES.PATH_ENVIAR_CNAB + nomeArquivo);
			financeiroService.saveCNABENviado(al);
		}
	}

	public void gerarBaixaBoletosPagos() {
		List<Boleto> boletoParaBaixa = financeiroService.getBoletosParaBaixa();
		for (Boleto b : boletoParaBaixa) {
			gerarBaixaBoletosPagos(b,CONSTANTES.PATH_ENVIAR_BAIXA);
		}
	}

	public void gerarBaixaBoletoAlunosCancelados() {
		List<Aluno> alunosCancelados = financeiroService.getAlunosRemovidos();
		for (Aluno al : alunosCancelados) {
			for(Boleto b : al.getBoletos()){
				gerarBaixaBoletosCancelados(b,CONSTANTES.PATH_ENVIAR_BAIXA_CANCELADOS);
			}
		}
	}
	
	private List<org.aaf.financeiro.model.Boleto> getBoletosFinanceiro(List<Boleto> boletos){
		List<org.aaf.financeiro.model.Boleto> boletosFinanceiro = new ArrayList<>();
		if(boletos!= null){
			for(Boleto boleto : boletos){
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
			List<Pagador> boletosImportados = CNAB240_RETORNO_SICOOB.imporCNAB240(
					Constante.LOCAL_ARMAZENAMENTO_REMESSA + "adonai" + File.separator + "importando" + File.separator);
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
		for(Pagador pagador : boletosImportados){
			org.aaf.financeiro.model.Boleto
			boletoCNAB =pagador.getBoletos().get(0);
			String numeroDocumento = boletoCNAB.getNossoNumero();
			if(numeroDocumento != null && !numeroDocumento.equalsIgnoreCase("") && !numeroDocumento.contains("-")){
				try{
					numeroDocumento = numeroDocumento.trim().replace(" ", "").replace("/", "".replace("-", "").replace(".", ""));
					if(numeroDocumento.matches("^[0-9]*$")){
						Long numeroDocumentoLong = Long.parseLong(numeroDocumento);
						org.escola.model.Boleto boleto = null;
						if(!extratoBancario){
							numeroDocumentoLong-=10000;
							 boleto = financeiroService.findBoletoByID(numeroDocumentoLong);
						}else{
							String numeroDocumentoExtrato = String.valueOf(numeroDocumentoLong);
							boleto = financeiroService.findBoletoByNumeroModel(numeroDocumentoExtrato.substring(0, numeroDocumentoExtrato.length()-1));
						}
						if(boleto != null){
							if(!Verificador.getStatusEnum(boleto).equals(StatusBoletoEnum.PAGO)){
								boleto.setValorPago(boletoCNAB.getValorPago());
								boleto.setDataPagamento(OfficeUtil.retornaData(boletoCNAB.getDataPagamento()));
								boleto.setConciliacaoPorExtrato(extratoBancario);
								financeiroService.save(boleto);
								System.out.println("YESS, BOLETO PAGO");								
							}
						}
					}
					
				}catch (ClassCastException cce) {
					cce.printStackTrace();
				}
			}
		}
	}


}

package org.escola.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.model.Boleto;
import org.escola.model.Configuracao;
import org.escola.model.ContratoAluno;
import org.escola.service.AlunoService;
import org.escola.service.ConfiguracaoService;
import org.escola.util.CompactadorZip;
import org.escola.util.FileDownload;
import org.escola.util.FileUtils;
import org.escola.util.UtilFinalizarAnoLetivo;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class ConfiguracaoController implements Serializable{

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Configuracao configuracao;
	
	private int mesGerarCNAB;
	
	private int mesGerarCNABCancelamento;
	
	private int anohistorico;
	
	@Inject
    private ConfiguracaoService configuracaoService;

	@Inject
	private UtilFinalizarAnoLetivo finalizarAnoLetivo;
	
	@Inject
	private AlunoService alunoService;
	
	@Inject
	private org.escola.service.rotinasAutomaticas.CNAB240 cnab240;
	
	@PostConstruct
	private void init() {
		List<Configuracao> confs =configuracaoService.findAll(); 
		
		if(confs == null || confs.isEmpty()){
			configuracao = new Configuracao();
		}else{
			configuracao = confs.get(0);
		}
	}
	
	public void gerarHistoricoAno(int ano){
		int quantidade = alunoService.findAll().size();
		int quantidadeNoLote = 50;
		int inicio = 0;
		while (inicio <= quantidade) {
			finalizarAnoLetivo.gerarHistorico(inicio,quantidadeNoLote,ano);
			inicio +=quantidadeNoLote +1;
		}
	}
	
	public StreamedContent gerarCNABCancelamentoDoMES(int mes) {
		try {
			Calendar calendario = Calendar.getInstance();

			StringBuilder sb = new StringBuilder();
			sb.append(calendario.get(Calendar.YEAR));
			sb.append(calendario.get(Calendar.MONTH));
			
			sb.append(calendario.get(Calendar.DAY_OF_MONTH));

			List<Boleto> boletos = configuracaoService.findBoletosCanceladosMes(mesGerarCNABCancelamento, configuracao.getAnoRematricula());

			String caminhoFinalPasta = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + File.separator + sb+ File.separator;
			CompactadorZip.createDir(caminhoFinalPasta);

			for (Boleto b : boletos) {
				gerarCNB240Cancelamento(b, caminhoFinalPasta);
			}

			String arquivoSaida = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + File.separator + sb + "CNAB240.zip";
			CompactadorZip.compactarParaZip(arquivoSaida, caminhoFinalPasta);

			InputStream stream2 = new FileInputStream(arquivoSaida);
			return FileDownload.getContentDoc(stream2, "escolaCNABSdoMes" + " ___ " + mesGerarCNABCancelamento + "___" + sb+".zip");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void gerarCNB240Cancelamento(Boleto b, String caminhoArquivo) {
		try {
			cnab240.gerarBaixaBoletosCancelados(b, caminhoArquivo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public StreamedContent gerarCNABDoMES(int mes) {
		try {
			Calendar calendario = Calendar.getInstance();

			StringBuilder sb = new StringBuilder();
			sb.append(calendario.get(Calendar.YEAR));
			sb.append(calendario.get(Calendar.MONTH));
			sb.append(calendario.get(Calendar.DAY_OF_MONTH));

			List<Boleto> boletos = configuracaoService.findBoletosMes(mesGerarCNAB, configuracao.getAnoRematricula());

			String caminhoFinalPasta = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + File.separator + sb;
			CompactadorZip.createDir(caminhoFinalPasta);

			for (Boleto b : boletos) {
				ContratoAluno ca = configuracaoService.findContrato(b.getId()); 
				
				if(ca.getCpfResponsavel() != null && !ca.getCpfResponsavel().equalsIgnoreCase("")){
					if(ca.getNomeResponsavel() != null && !ca.getNomeResponsavel().equalsIgnoreCase("")){
						if(ca.getEndereco() != null && !ca.getEndereco().equalsIgnoreCase("")){
							if(ca.getCep() == null || ca.getCep().equalsIgnoreCase("")){
								ca.setCep("88132700");	
							}
							if(ca.getBairro() == null || ca.getBairro().equalsIgnoreCase("")){
								ca.setBairro("Bela Vista");
							}
							if(ca.getCidade() == null || ca.getCidade().equalsIgnoreCase("")){
								ca.setCidade("Palhoca");
							}
							
							InputStream stream = gerarCNB240(ca, mesGerarCNAB, caminhoFinalPasta);
							FileUtils.inputStreamToFile(stream, b.getNossoNumero()+"");
						}
					}
				}
				
				configuracaoService.mudarStatusParaCNABEnviado(b);
				
			}

			String arquivoSaida = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + File.separator + sb + "CNAB240.zip";
			CompactadorZip.compactarParaZip(arquivoSaida, caminhoFinalPasta);

			InputStream stream2 = new FileInputStream(arquivoSaida);
			return FileDownload.getContentDoc(stream2, "escolaCNABSdoMes" +sb+".zip");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	public InputStream gerarCNB240(ContratoAluno contrato, int mes, String caminhoArquivo) {
		try {
			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";

			InputStream stream = FileUtils.gerarCNB240(sequencialArquivo, contrato, mes, caminhoArquivo);
			configuracaoService.incrementaSequencialArquivoCNAB();

			return stream;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public String salvar(){
		configuracaoService.save(configuracao);
		return "index";
	}
	
	public Configuracao getConfiguracao() {
		return configuracao;
	}


	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}

	public int getMesGerarCNAB() {
		return mesGerarCNAB;
	}

	public void setMesGerarCNAB(int mesGerarCNAB) {
		this.mesGerarCNAB = mesGerarCNAB;
	}

	public int getAnohistorico() {
		return anohistorico;
	}

	public void setAnohistorico(int anohistorico) {
		this.anohistorico = anohistorico;
	}

	public int getMesGerarCNABCancelamento() {
		return mesGerarCNABCancelamento;
	}

	public void setMesGerarCNABCancelamento(int mesGerarCNABCancelamento) {
		this.mesGerarCNABCancelamento = mesGerarCNABCancelamento;
	}


}

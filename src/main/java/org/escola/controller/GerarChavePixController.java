package org.escola.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.model.ChavePixGerada;
import org.escola.model.Configuracao;
import org.escola.service.ChavePixGeradaService;
import org.escola.service.ConfiguracaoService;
import org.escola.util.PixBRCodeUtil;

@Named
@ViewScoped
public class GerarChavePixController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ChavePixGeradaService chavePixGeradaService;

	@Inject
	private ConfiguracaoService configuracaoService;

	private List<ChavePixGerada> listaGeradas;

	private ChavePixGerada novo;

	private ChavePixGerada selecionadaParaQr;

	@PostConstruct
	public void init() {
		listaGeradas = chavePixGeradaService.findAll();
		prepararNovo();
	}

	public void prepararNovo() {
		novo = new ChavePixGerada();
	}

	public void gerar() {
		try {
			if (novo.getValor() <= 0) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Informe um valor maior que zero", null));
				return;
			}

			Configuracao conf = configuracaoService.getConfiguracao();
			if (conf.getChavePix() == null || conf.getChavePix().trim().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Chave Pix não configurada", "Cadastre a chave Pix em Secretaria > Configuração primeiro"));
				return;
			}

			String payload = PixBRCodeUtil.gerarBRCode(conf.getChavePix(), conf.getNomeRecebedorPix(),
					conf.getCidadeRecebedorPix(), novo.getValor());
			novo.setPayload(payload);
			novo.setDataCriacao(new Date());

			chavePixGeradaService.salvar(novo);
			listaGeradas = chavePixGeradaService.findAll();
			prepararNovo();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Código Pix gerado"));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar", e.getMessage()));
		}
	}

	public void remover(ChavePixGerada chave) {
		chavePixGeradaService.remover(chave.getId());
		listaGeradas = chavePixGeradaService.findAll();
	}

	public void selecionarParaQr(ChavePixGerada chave) {
		this.selecionadaParaQr = chave;
	}

	public String getQrCodeBase64Selecionada() {
		if (selecionadaParaQr == null) {
			return null;
		}
		return PixBRCodeUtil.gerarQrCodeBase64(selecionadaParaQr.getPayload());
	}

	public ChavePixGerada getSelecionadaParaQr() {
		return selecionadaParaQr;
	}

	public List<ChavePixGerada> getListaGeradas() {
		return listaGeradas;
	}

	public ChavePixGerada getNovo() {
		return novo;
	}

	public void setNovo(ChavePixGerada novo) {
		this.novo = novo;
	}

}

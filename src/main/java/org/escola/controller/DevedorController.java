/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.escola.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.enums.PerioddoEnum;
import org.escola.enums.StatusBoletoEnum;
import org.escola.model.Aluno;
import org.escola.model.Boleto;
import org.escola.model.ContratoAluno;
import org.escola.model.Devedor;
import org.escola.service.DevedorService;
import org.escola.util.FileDownload;
import org.escola.util.ImpressoesUtils;
import org.escola.util.Util;
import org.escola.util.Verificador;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import com.lowagie.text.DocumentException;

@Named
@ViewScoped
public class DevedorController implements Serializable {

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Devedor devedor;

	@Produces
	@Named
	private List<Devedor> devedores;

	@Inject
	private DevedorService devedorService;

	private LazyDataModel<Aluno> lazyListDataModel;
	
	private LazyDataModel<ContratoAluno> lazyListDataModel2;
	
	private LazyDataModel<ContratoAluno> lazyListDataModel3;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	@Produces
	@Named
	private ContratoAluno contratoS;
	
	@PostConstruct
	private void init() {
		if (getDevedor() == null) {
			Object obj = Util.getAtributoSessao("devedor");
			if (obj != null) {
				setDevedor((Devedor) obj);
				Double total = 0D;
				for(Boleto b : devedor.getBoletos()){
				//+=b.getValor();
				}
				getDevedor().setValorTotal(total);
			} else {
				Boleto b = new Boleto();
				List<Boleto> bs = new ArrayList<>();
				bs.add(b);
				devedor = new Devedor();
				devedor.setBoletos(bs);
				devedor.setValorTotal(0D);
						
			}
		}
	}
	
	public Double getTotal(ContratoAluno ca) {
		Double total = 0D;
		for (Boleto b : ca.getBoletos()) {
			if (b.getAtrasado() != null && b.getAtrasado()) {
				total += Verificador.getValorFinal(b);
			}
		}

		return total;
	}

	
	public void buscar(){
		
	}
	
	public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         System.out.println(newValue);
        
    }
 
	public void onRowEdit(RowEditEvent event) {
	 System.out.println("a");
    }
	
	public StreamedContent gerarArquivoProtesto() {
		return gerarArquivoProtesto(getContratoS());
	}
	
	public StreamedContent gerarArquivoProtesto(ContratoAluno ca) {
		String nomeArquivo = "";
		if (ca != null && ca.getId() != null) {

			nomeArquivo = ca.getId() + "Z";
			String modeloArq = "modelo_protesto.docx";
			try {
				switch (ca.getBoletos().size()) {
				case 12:
					modeloArq = "modelo_protesto12.docx";
					break;
				case 11:
					modeloArq = "modelo_protesto11.docx";
					break;
				case 10:
					modeloArq = "modelo_protest10.docx";
					break;
				case 9:
					modeloArq = "modelo_protesto9.docx";
					break;
				case 8:
					modeloArq = "modelo_protesto8.docx";
					break;
				case 7:
					modeloArq = "modelo_protesto7.docx";
					break;
				case 6:
					modeloArq = "modelo_protesto6.docx";
					break;
				case 5:
					modeloArq = "modelo_protesto5.docx";
					break;
				case 4:
					modeloArq = "modelo_protesto4.docx";
					break;

				case 3:
					modeloArq = "modelo_protesto3.docx";
					break;

				case 2:
					modeloArq = "modelo_protesto2.docx";
					break;

				case 1:
					modeloArq = "modelo_protesto1.docx";
					break;

				default:
					break;
				}
				ImpressoesUtils.gerarArquivoFisico(modeloArq, montarArquivoProtesto(ca), nomeArquivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			nomeArquivo += ".doc";
		} else {
			nomeArquivo = "modelo_protesto.docx";
		}

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + nomeArquivo;
		InputStream stream = null;
		try {
			stream = new FileInputStream(caminho);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FileDownload.getContentDoc(stream, nomeArquivo);

	}
	
	public HashMap<String, String> montarArquivoProtesto(ContratoAluno contratoAluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.DATE_FIELD, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("nomeresponsavel", contratoAluno.getNomeResponsavel());
		trocas.put("nomemae", contratoAluno.getNomeMaeResponsavel());
		trocas.put("cpfresponsavel", contratoAluno.getCpfResponsavel());
		trocas.put("rgresponsavel", contratoAluno.getRgResponsavel());
		trocas.put("enderecoresponsavel", contratoAluno.getEndereco() + " - " + contratoAluno.getBairro() + " - "
				+ contratoAluno.getCidade() + " - " + contratoAluno.getCep() + " - " + "Santa Catarina");
		trocas.put("numerocontrato", contratoAluno.getNumero());
		trocas.put("datacontrato", formatador.format(contratoAluno.getDataCriacaoContrato()));
		trocas.put("valortotalcont",
				((contratoAluno.getNumeroParcelas() * contratoAluno.getValorMensal()) + "").replace(".", ","));
		trocas.put("numeroParcelas", contratoAluno.getNumeroParcelas() + "");
		trocas.put("valorparcela", (contratoAluno.getValorMensal() + "").replace(".", ","));
		trocas.put("datahoje", dataExtenso);

		double totalAberto = 0;
		for (int i = contratoAluno.getBoletos().size(); i > 0; i--) {
			if (i == 12) {
				trocas.put("dtvenctwo", formatador.format(contratoAluno.getBoletos().get(i - 1).getVencimento()));
				trocas.put("vloritwo", contratoAluno.getBoletos().get(i - 1).getValorNominal() + "");
				trocas.put("vlcoritow", verificaValorFinalBoleto(contratoAluno.getBoletos().get(i - 1)));
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stsdoze", situacao);
			} else if (i == 11) {
				trocas.put("dtvencelev", formatador.format(contratoAluno.getBoletos().get(i - 1).getVencimento()));
				trocas.put("valorOrigonze", contratoAluno.getBoletos().get(i - 1).getValorNominal() + "");
				trocas.put("vlcorriele", verificaValorFinalBoleto(contratoAluno.getBoletos().get(i - 1)));
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stsonze", situacao);
			} else if (i == 10) {
				trocas.put("dtvencten", formatador.format(contratoAluno.getBoletos().get(i - 1).getVencimento()));
				trocas.put("vloriten", contratoAluno.getBoletos().get(i - 1).getValorNominal() + "");
				trocas.put("vlcoriten", verificaValorFinalBoleto(contratoAluno.getBoletos().get(i - 1)));
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stsdez", situacao);
			} else {
				trocas.put("dataVenc" + i, formatador.format(contratoAluno.getBoletos().get(i - 1).getVencimento()));
				trocas.put("valorOrig" + i, contratoAluno.getBoletos().get(i - 1).getValorNominal() + "");
				trocas.put("valorCorri" + i, verificaValorFinalBoleto(contratoAluno.getBoletos().get(i - 1)));

			}

			if (i == 9) {
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stsnove", situacao);
			}
			if (i == 8) {
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stsoito", situacao);
			}
			if (i == 7) {
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stssete", situacao);
			}
			if (i == 6) {
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stsseis", situacao);
			}
			if (i == 5) {
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stscnco", situacao);
			}
			if (i == 4) {
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stsquatro", situacao);
			}
			if (i == 3) {
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("ststres", situacao);
			}
			if (i == 2) {
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stsdois", situacao);
			}
			if (i == 1) {
				String situacao = "ABERTO";
				if (Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1)).equals(StatusBoletoEnum.PAGO)
						|| Verificador.getStatusEnum(contratoAluno.getBoletos().get(i - 1))
								.equals(StatusBoletoEnum.CANCELADO)) {
					situacao = "QUITADO";
				} else {
					totalAberto += Verificador.getValorFinal(contratoAluno.getBoletos().get(i - 1));
				}
				trocas.put("stsum", situacao);
			}

		}
		trocas.put("totalemaberto", (totalAberto + "0").replace(".", ","));

		return trocas;
	}
	
	
	public void adicionarBoleto(){
		Boleto ultimo = new Boleto();
		if(devedor.getBoletos().size()>0){
			ultimo = devedor.getBoletos().get(devedor.getBoletos().size()-1);
		}
		Boleto boleto = new Boleto();
		/*boleto.setNumeroContrato(ultimo.getNumeroContrato());
		boleto.setDataGeracao(ultimo.getDataGeracao());
		boleto.setValor(ultimo.getValor());*/
		devedor.getBoletos().add(boleto);
	}
	
	public boolean isContratoSelecionado() {
		if (getContratoS() == null) {
			return false;
		}
		return getContratoS().getId() != null ? true : false;
	}
	
	public ContratoAluno getContratoS() {
		return contratoS;
	}

	public String verificaValorFinalBoleto(Boleto boleto) {
		return Util.formatarDouble2Decimais(Verificador.getValorFinal(boleto));
	}
	
	public StreamedContent imprimirDevedores() throws IOException, DocumentException {
		/*String nomeArquivo = "devedores.pdf";
		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+nomeArquivo;
		Map<String, Object> filtros = new LinkedHashMap();
		filtros.put("removido", false);
		List<Aluno> devedores = devedorService.find(0, 2000, "nomeResponsavel", "asc", filtros);
		
		LinkedHashSet<Aluno> aux = new LinkedHashSet();
		aux.addAll(devedores);
		
		List<Aluno> aux2 = new ArrayList<>();
		aux2.addAll(aux);
		OfficePDFUtil.criaPDFDevedores(aux2, caminho);
		
		InputStream stream =  new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
		*
		*/
		return null;
	}

	public LazyDataModel<ContratoAluno> getLazyDataModel2() {
		if (lazyListDataModel2 == null) {

			lazyListDataModel2 = new LazyDataModel<ContratoAluno>() {

				@Override
				public ContratoAluno getRowData(String rowKey) {
					return getDevedorService().findByIdContratoAluno(Long.valueOf(rowKey));
				}

				@Override
				public Long getRowKey(ContratoAluno al) {
					return al.getId();
				}

				@Override
				public List<ContratoAluno> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					if (filtros.containsKey("periodo")) {
						filtros.put("periodo", filtros.get("periodo").equals("MANHA") ? PerioddoEnum.MANHA
								: filtros.get("periodo").equals("TARDE") ? PerioddoEnum.TARDE : PerioddoEnum.INTEGRAL);
					}

					if (filtros.containsKey("enviadoParaCobrancaCDL")) {
						filtros.put("enviadoParaCobrancaCDL",
								filtros.get("enviadoParaCobrancaCDL").equals("Não") ? Boolean.FALSE : Boolean.TRUE);
					}

					if (filtros.containsKey("contratoTerminado")) {
						filtros.put("contratoTerminado",
								filtros.get("contratoTerminado").equals("Não") ? Boolean.FALSE : Boolean.TRUE);
					}

					if (filtros.containsKey("enviadoSPC")) {
						filtros.put("enviadoSPC",
								filtros.get("enviadoSPC").equals("Não") ? Boolean.FALSE : Boolean.TRUE);
					}
					filtros.put("protestado",Boolean.TRUE);
					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<ContratoAluno> ol = getDevedorService().findProtesto(first, pageSize, orderByParam, orderParam,
							filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModel2.setRowCount(ol.size()+pageSize+1);
						return ol;
					}

					this.setRowCount(ol.size()+pageSize+1);
					return null;

				}
			};
			lazyListDataModel2.setRowCount((int) getDevedorService().countContratoAluno(null));

		}

		return lazyListDataModel2;

	}

	public LazyDataModel<ContratoAluno> getLazyDataModel3() {
		if (lazyListDataModel3 == null) {

			lazyListDataModel3 = new LazyDataModel<ContratoAluno>() {

				@Override
				public ContratoAluno getRowData(String rowKey) {
					return getDevedorService().findByIdContratoAluno(Long.valueOf(rowKey));
				}

				@Override
				public Long getRowKey(ContratoAluno al) {
					return al.getId();
				}

				@Override
				public List<ContratoAluno> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					if (filtros.containsKey("periodo")) {
						filtros.put("periodo", filtros.get("periodo").equals("MANHA") ? PerioddoEnum.MANHA
								: filtros.get("periodo").equals("TARDE") ? PerioddoEnum.TARDE : PerioddoEnum.INTEGRAL);
					}

					if (filtros.containsKey("enviadoParaCobrancaCDL")) {
						filtros.put("enviadoParaCobrancaCDL",
								filtros.get("enviadoParaCobrancaCDL").equals("Não") ? Boolean.FALSE : Boolean.TRUE);
					}

					if (filtros.containsKey("contratoTerminado")) {
						filtros.put("contratoTerminado",
								filtros.get("contratoTerminado").equals("Não") ? Boolean.FALSE : Boolean.TRUE);
					}

					if (filtros.containsKey("enviadoSPC")) {
						filtros.put("enviadoSPC",
								filtros.get("enviadoSPC").equals("Não") ? Boolean.FALSE : Boolean.TRUE);
					}
					filtros.put("protestado",Boolean.TRUE);
					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<ContratoAluno> ol = getDevedorService().findProtestoEnviado(first, pageSize, orderByParam, orderParam,
							filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModel3.setRowCount(ol.size()+pageSize+1);
						return ol;
					}

					this.setRowCount(ol.size()+pageSize+1);
					return null;

				}
			};
			lazyListDataModel3.setRowCount((int) getDevedorService().countContratoAluno(null));

		}

		return lazyListDataModel3;

	}
	
	public LazyDataModel<Aluno> getLazyDataModel() {
		if (lazyListDataModel == null) {

			lazyListDataModel = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return getDevedorService().findById(Long.valueOf(rowKey));
				}

				@Override
				public Long getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					if (filtros.containsKey("periodo")) {
						filtros.put("periodo", filtros.get("periodo").equals("MANHA") ? PerioddoEnum.MANHA
								: filtros.get("periodo").equals("TARDE") ? PerioddoEnum.TARDE : PerioddoEnum.INTEGRAL);
					}

					if (filtros.containsKey("enviadoParaCobrancaCDL")) {
						filtros.put("enviadoParaCobrancaCDL",
								filtros.get("enviadoParaCobrancaCDL").equals("Não") ? Boolean.FALSE : Boolean.TRUE);
					}

					if (filtros.containsKey("contratoTerminado")) {
						filtros.put("contratoTerminado",
								filtros.get("contratoTerminado").equals("Não") ? Boolean.FALSE : Boolean.TRUE);
					}

					if (filtros.containsKey("enviadoSPC")) {
						filtros.put("enviadoSPC",
								filtros.get("enviadoSPC").equals("Não") ? Boolean.FALSE : Boolean.TRUE);
					}
					

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Aluno> ol = getDevedorService().findDevedor(dataInicio, dataFim);
					
					if (ol != null && ol.size() > 0) {
						lazyListDataModel.setRowCount((int) getDevedorService().count(filtros));
						return ol;
					}

					this.setRowCount((int) getDevedorService().count(filtros));
					return null;

				}
			};
			lazyListDataModel.setRowCount((int) getDevedorService().count(null));

		}

		return lazyListDataModel;

	}
	
	public String marcarLinha2(Aluno a) {
		String cor = "";
		
		if(a == null){
			return "";
		}
		
		if(a.getRemovido() != null && a.getRemovido()){
			cor = "marcarLinhaVermelho";
		}else{
			cor = "marcarLinha";
		}
		
		/*cor = "marcarLinhaVermelho";
		cor = "marcarLinhaVerde";
		cor = "marcarLinhaAmarelo";
		cor = "marcarLinha"
		*/
		return cor;
	}
	
	public boolean temcontato(String contato) {
		if (contato != null && !contato.equalsIgnoreCase("")) {
			return true;
		}
		return false;
	}
	
	public String marcarLinha(ContratoAluno a) {
		String cor = "";
		if(a != null){
			if (a.getPodeProtestarFinal() != null && a.getPodeProtestarFinal()) {
				cor = "marcarLinhaVerde";
				if (a.getEnviadoProtestoDefinitivo() != null && a.getEnviadoProtestoDefinitivo()) {
					cor = "marcarLinhaVermelho";
				}
			} else {
				if (a.getEnviadoProtestoDefinitivo() != null && a.getEnviadoProtestoDefinitivo()) {
					cor = "";
				}
				
				
				//TODO VER
			//	boolean estaNaTurma = alunoService.estaEmUmaTUrma(a.getId());
			//	if (!estaNaTurma) {
			//		cor = "marcarLinha";
			//	}
			}	
		}
		
		return cor;
	}
	
	public void saveObservavao(Aluno aluno){
		devedorService.saveObservacao(aluno);
	}
	
	public boolean isAlunoSelecionado() {
		return devedor.getId() != null ? true : false;
	}
	
	public String voltar() {
		return "index";
	}
	
	public String salvar() {
		//devedorService.save(devedor);
		Util.removeAtributoSessao("devedor");
		return "index";
	}

	public String editar(Long idprof) {
		//devedor = devedorService.findById(idprof);
		Util.addAtributoSessao("devedor", devedor);
		return "cadastrar";
	}
	
	public Double getTotal(Aluno devedor){
		Double total = 0D;
		if(devedor  != null){
			if(devedor.getContratoVigente() != null &&  !devedor.getContratoVigente().getBoletos().isEmpty()){
				for(Boleto b : devedor.getContratoVigente().getBoletos()){
					if(b.getAtrasado() != null && b.getAtrasado()){
						total += Verificador.getValorFinal(b);
					}
				}	
			}	
		}
		
		
		return total;
	}

	public String editar() {
		return editar(devedor.getId());
	}
	
	public String adicionarNovo() {
		return "cadastrar";
	}
	
	public String remover(Long idTurma) {
		getDevedorService().remover(idTurma);
		return "index";
	}

	public String remover(Boleto boleto) {
		devedor.getBoletos().remove(boleto);
		return "";
	}
	
	public double valorTotal(Aluno aluno){
		ContratoAluno contrato = aluno.getContratoVigente();
		if(contrato != null && contrato.getNumeroParcelas() != null){
			return contrato.getValorMensal()*contrato.getNumeroParcelas();
		}else{
			return 0;
		}
	}
	
	public void enviarProtesto(ContratoAluno ca) {
		devedorService.enviarParaProtesto(ca);
	}
	
	public void enviarPodeEnviarProtestoFina(ContratoAluno ca) {
		devedorService.enviarPodeEnviarProtestoFina(ca);
	}
	
	public void enviadoProtestoDefinitivo(ContratoAluno ca) {
		devedorService.enviadoProtestoDefinitivo(ca);
	}
	
	public void enviarPodeEnviarProtestoFina() {
		devedorService.enviarPodeEnviarProtestoFina(getContratoS());
	}
	
	public void enviadoProtestoDefinitivo() {
		devedorService.enviadoProtestoDefinitivo(getContratoS());
	}
	
	public boolean podeEnviar() {
		if(getContratoS() == null){
			return false;
		}
		return !Util.nullOrTrue(getContratoS().getPodeProtestarFinal());
	}
	
	public boolean podeEnviado() {
		if(getContratoS() == null){
			return false;
		}
		return Util.nullOrTrue(getContratoS().getPodeProtestarFinal()) && !Util.nullOrTrue(getContratoS().getEnviadoProtestoDefinitivo())  ;
	}
	

	private Float maior(Float float1, Float float2) {
		return float1 > float2 ? float1 : float2;
	}

	private String mostraNotas(Float nota) {
		if (nota == null || nota == 0) {
			return "";
		} else {
			return String.valueOf(Math.round(nota * 100) / 100);
		}
	}

	private Float media(Float... notas) {
		int qtade = 0;
		float sum = 0;
		for (Float f : notas) {
			sum += f;
			qtade++;
		}
		return qtade > 0 ? sum / qtade : 0;
	}

	public Devedor getDevedor() {
		return devedor;
	}

	public void setDevedor(Devedor devedor) {
		this.devedor = devedor;
	}

	public List<Devedor> getDevedores() {
		return devedores;
	}

	public void setDevedores(List<Devedor> devedores) {
		this.devedores = devedores;
	}

	public DevedorService getDevedorService() {
		return devedorService;
	}

	public void setDevedorService(DevedorService devedorService) {
		this.devedorService = devedorService;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setContratoS(ContratoAluno contratoS) {
		this.contratoS = contratoS;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
}

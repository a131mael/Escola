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
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.enums.PerioddoEnum;
import org.escola.model.Aluno;
import org.escola.model.Boleto;
import org.escola.model.Devedor;
import org.escola.service.DevedorService;
import org.escola.util.FileDownload;
import org.escola.util.ImpressoesUtils;
import org.escola.util.Util;
import org.escola.util.Verificador;
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
	
	public StreamedContent imprimirDevedores() throws IOException, DocumentException {
		String nomeArquivo = "devedores.pdf";
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
						filtros.put("enviadoParaCobrancaCDL", filtros.get("enviadoParaCobrancaCDL").equals("Não") ? Boolean.FALSE: Boolean.TRUE);
					}

					if (filtros.containsKey("contratoTerminado")) {
						filtros.put("contratoTerminado", filtros.get("contratoTerminado").equals("Não") ? Boolean.FALSE: Boolean.TRUE);
					}

					if (filtros.containsKey("enviadoSPC")) {
						filtros.put("enviadoSPC", filtros.get("enviadoSPC").equals("Não") ? Boolean.FALSE: Boolean.TRUE);
					}

					
					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Aluno> ol = getDevedorService().find(first, pageSize, orderByParam, orderParam, filtros);

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
	
	public String marcarLinha(Aluno a) {
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
			if(devedor.getBoletos() != null && !devedor.getBoletos().isEmpty()){
				for(Boleto b : devedor.getBoletos()){
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
		if(aluno != null && aluno.getNumeroParcelas() != null){
			return aluno.getValorMensal()*aluno.getNumeroParcelas();
		}else{
			return 0;
		}
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
	
}

package org.escola.controller.aluno;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.aaf.financeiro.model.Boleto;
import org.aaf.financeiro.model.Pagador;
import org.aaf.financeiro.sicoob.util.CNAB240_REMESSA_SICOOB;
import org.aaf.financeiro.sicoob.util.CNAB240_RETORNO_SICOOB;
import org.aaf.financeiro.sicoob.util.CNAB240_SICOOB;
import org.aaf.financeiro.util.ImportadorArquivo;
import org.aaf.financeiro.util.OfficeUtil;
import org.aaf.financeiro.util.constantes.Constante;
import org.apache.shiro.SecurityUtils;
import org.escola.controller.OfficeDOCUtil;
import org.escola.controller.OfficePDFUtil;
import org.escola.enums.BimestreEnum;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.enums.StatusBoletoEnum;
import org.escola.enums.TipoMembro;
import org.escola.model.Aluno;
import org.escola.model.AlunoAvaliacao;
import org.escola.model.Configuracao;
import org.escola.model.ContratoAluno;
import org.escola.model.HistoricoAluno;
import org.escola.model.Member;
import org.escola.model.Professor;
import org.escola.service.AlunoService;
import org.escola.service.AvaliacaoService;
import org.escola.service.ConfiguracaoService;
import org.escola.service.FinanceiroEscolaService;
import org.escola.service.rotinasAutomaticas.EnviadorEmail;
import org.escola.util.CompactadorZip;
import org.escola.util.CurrencyWriter;
import org.escola.util.FileDownload;
import org.escola.util.FileUtils;
import org.escola.util.Formatador;
import org.escola.util.ImpressoesUtils;
import org.escola.util.Util;
import org.escola.util.Verificador;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class AlunoController implements Serializable {

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Aluno aluno;

	@Produces
	@Named
	private HistoricoAluno historicoAluno;

	@Produces
	@Named
	private Boleto boleto;

	@Produces
	@Named
	private List<Aluno> alunos;

	@Inject
	private AlunoService alunoService;

	@Inject
	private ConfiguracaoService configuracaoService;

	@Inject
	private FinanceiroEscolaService financeiroEscolaService;

	@Inject
	private AvaliacaoService avaliacaoService;

	private OfficeDOCUtil officeDOCUtil;
	CurrencyWriter cw;

	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoPortugues;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoIngles;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoMatematica;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoHistoria;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoEDFisica;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoGeografia;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoCiencias;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoFormacaoCrista;
	private Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoArtes;
	private LazyDataModel<Boleto> lazyListDataModelBoletos;
	private LazyDataModel<Aluno> lazyListDataModelCanceladas;

	@Named
	private BimestreEnum bimestreSel;
	@Named
	private DisciplinaEnum disciplinaSel;

	private LazyDataModel<Aluno> lazyListDataModel;

	private LazyDataModel<Aluno> lazyListDataModelExAlunos;

	private LazyDataModel<Aluno> lazyListDataModelUltimoAnoLetivo;

	private boolean irmao1;

	private boolean irmao2;

	private boolean irmao3;

	private boolean irmao4;

	private Configuracao configuracao;

	@PostConstruct
	private void init() {
		if (aluno == null) {
			Object obj = Util.getAtributoSessao("aluno");
			if (obj != null) {
				aluno = (Aluno) obj;
				if (aluno.getIrmao1() != null
						&& (aluno.getIrmao1().getRemovido() == null || !aluno.getIrmao1().getRemovido())) {
					irmao1 = true;
				}
				if (aluno.getIrmao2() != null
						&& (aluno.getIrmao2().getRemovido() == null || !aluno.getIrmao2().getRemovido())) {
					irmao2 = true;
				}
				if (aluno.getIrmao3() != null
						&& (aluno.getIrmao3().getRemovido() == null || !aluno.getIrmao3().getRemovido())) {
					irmao3 = true;
				}
				if (aluno.getIrmao4() != null
						&& (aluno.getIrmao4().getRemovido() == null || !aluno.getIrmao4().getRemovido())) {
					irmao4 = true;
				}

			} else {
				aluno = new Aluno();
			}
		}

		if (alunoAvaliacaoIngles == null) {
			Object obj = Util.getAtributoSessao("alunoAvaliacaoIngles");
			if (obj != null) {
				alunoAvaliacaoIngles = (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoIngles = new LinkedHashMap<>();
				;
			}
		}
		if (alunoAvaliacaoArtes == null) {
			Object obj = Util.getAtributoSessao("alunoAvaliacaoArtes");
			if (obj != null) {
				alunoAvaliacaoArtes = (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoArtes = new LinkedHashMap<>();
				;
			}
		}
		if (alunoAvaliacaoCiencias == null) {
			Object obj = Util.getAtributoSessao("alunoAvaliacaoCiencias");
			if (obj != null) {
				alunoAvaliacaoCiencias = (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoCiencias = new LinkedHashMap<>();
				;
			}
		}
		if (alunoAvaliacaoEDFisica == null) {
			Object obj = Util.getAtributoSessao("alunoAvaliacaoEDFisica");
			if (obj != null) {
				alunoAvaliacaoEDFisica = (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoEDFisica = new LinkedHashMap<>();
				;
			}
		}
		if (alunoAvaliacaoFormacaoCrista == null) {
			Object obj = Util.getAtributoSessao("alunoAvaliacaoFormacaoCrista");
			if (obj != null) {
				alunoAvaliacaoFormacaoCrista = (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoFormacaoCrista = new LinkedHashMap<>();
				;
			}
		}
		if (alunoAvaliacaoGeografia == null) {
			Object obj = Util.getAtributoSessao("alunoAvaliacaoGeografia");
			if (obj != null) {
				alunoAvaliacaoGeografia = (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoGeografia = new LinkedHashMap<>();
				;
			}
		}
		if (alunoAvaliacaoHistoria == null) {
			Object obj = Util.getAtributoSessao("alunoAvaliacaoHistoria");
			if (obj != null) {
				alunoAvaliacaoHistoria = (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoHistoria = new LinkedHashMap<>();
				;
			}
		}

		if (alunoAvaliacaoMatematica == null) {
			Object obj = Util.getAtributoSessao("alunoAvaliacaoMatematica");
			if (obj != null) {
				alunoAvaliacaoMatematica = (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoMatematica = new LinkedHashMap<>();
				;
			}
		}

		if (alunoAvaliacaoPortugues == null) {
			Object obj = Util.getAtributoSessao("alunoAvaliacaoPortugues");
			if (obj != null) {
				alunoAvaliacaoPortugues = (Map<Aluno, List<AlunoAvaliacao>>) obj;
			} else {
				alunoAvaliacaoPortugues = new LinkedHashMap<>();
				;
			}
		}

		if (historicoAluno == null) {
			historicoAluno = new HistoricoAluno();
		}
		officeDOCUtil = new OfficeDOCUtil();
		cw = new CurrencyWriter();
		configuracao = configuracaoService.getConfiguracao();
	}

	public ContratoAluno getContrato() {
		Object obj = Util.getAtributoSessao("contrato");
		ContratoAluno contrato = null;
		if (obj != null) {
			contrato = (ContratoAluno) obj;
		}
		return contrato;
	}

	public LazyDataModel<Aluno> getLazyDataModelCanceladas() {
		if (lazyListDataModelCanceladas == null) {

			lazyListDataModelCanceladas = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.put("anoLetivo", configuracao.getAnoLetivo());

					filtros.putAll(where);
					filtros.put("removido", true);

					if (filtros.containsKey("nomeAluno")) {
						filtros.put("nomeAluno", ((String) filtros.get("nomeAluno")).toUpperCase());
					}

					if (filtros.containsKey("periodo")) {
						filtros.put("periodo", filtros.get("periodo").equals("MANHA") ? PerioddoEnum.MANHA
								: filtros.get("periodo").equals("TARDE") ? PerioddoEnum.TARDE : PerioddoEnum.INTEGRAL);
					}

					if (filtros.containsKey("serie")) {
						if (filtros.get("serie").equals(Serie.JARDIM_I.toString())) {
							filtros.put("serie", Serie.JARDIM_I);
						} else if (filtros.get("serie").equals(Serie.JARDIM_II.toString())) {
							filtros.put("serie", Serie.JARDIM_II);
						} else if (filtros.get("serie").equals(Serie.MATERNAL.toString())) {
							filtros.put("serie", Serie.MATERNAL);
						} else if (filtros.get("serie").equals(Serie.PRE.toString())) {
							filtros.put("serie", Serie.PRE);
						} else if (filtros.get("serie").equals(Serie.PRIMEIRO_ANO.toString())) {
							filtros.put("serie", Serie.PRIMEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.SEGUNDO_ANO.toString())) {
							filtros.put("serie", Serie.SEGUNDO_ANO);
						} else if (filtros.get("serie").equals(Serie.TERCEIRO_ANO.toString())) {
							filtros.put("serie", Serie.TERCEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUARTO_ANO.toString())) {
							filtros.put("serie", Serie.QUARTO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUINTO_ANO.toString())) {
							filtros.put("serie", Serie.QUINTO_ANO);
						}

					}

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Aluno> ol = alunoService.find(first, pageSize, orderByParam, orderParam, filtros);
					if (ol != null && ol.size() > 0) {
						long count = alunoService.count(filtros);
						lazyListDataModelCanceladas.setRowCount((int) count);

						// FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("tbl:total");
						// RequestContext.getCurrentInstance().update("tbl:total");

						return ol;
					}
					long count = alunoService.count(filtros);
					// FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("tbl:total");
					// RequestContext.getCurrentInstance().update("tbl:total");
					this.setRowCount((int) count);
					return null;

				}
			};
			long count = alunoService.count(null);
			// FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("tbl:total");
			// RequestContext.getCurrentInstance().update("tbl:total");
			lazyListDataModelCanceladas.setRowCount((int) count);

		}

		return lazyListDataModelCanceladas;

	}

	public LazyDataModel<Aluno> getLazyDataModel() {
		if (lazyListDataModel == null) {

			lazyListDataModel = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					filtros.put("removido", false);
					if (filtros.containsKey("periodo")) {
						filtros.put("periodo", filtros.get("periodo").equals("MANHA") ? PerioddoEnum.MANHA
								: filtros.get("periodo").equals("TARDE") ? PerioddoEnum.TARDE : PerioddoEnum.INTEGRAL);
					}

					if (filtros.containsKey("serie")) {
						if (filtros.get("serie").equals(Serie.JARDIM_I.toString())) {
							filtros.put("serie", Serie.JARDIM_I);
						} else if (filtros.get("serie").equals(Serie.JARDIM_II.toString())) {
							filtros.put("serie", Serie.JARDIM_II);
						} else if (filtros.get("serie").equals(Serie.MATERNAL.toString())) {
							filtros.put("serie", Serie.MATERNAL);
						} else if (filtros.get("serie").equals(Serie.PRE.toString())) {
							filtros.put("serie", Serie.PRE);
						} else if (filtros.get("serie").equals(Serie.PRIMEIRO_ANO.toString())) {
							filtros.put("serie", Serie.PRIMEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.SEGUNDO_ANO.toString())) {
							filtros.put("serie", Serie.SEGUNDO_ANO);
						} else if (filtros.get("serie").equals(Serie.TERCEIRO_ANO.toString())) {
							filtros.put("serie", Serie.TERCEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUARTO_ANO.toString())) {
							filtros.put("serie", Serie.QUARTO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUINTO_ANO.toString())) {
							filtros.put("serie", Serie.QUINTO_ANO);
						}

					}

					filtros.put("removido", false);
					filtros.put("anoLetivo", configuracaoService.getConfiguracao().getAnoLetivo());

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Aluno> ol = alunoService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModel.setRowCount((int) alunoService.count(filtros));
						return ol;
					}

					this.setRowCount((int) alunoService.count(filtros));
					return null;

				}
			};
			lazyListDataModel.setRowCount((int) alunoService.count(null));

		}

		return lazyListDataModel;

	}

	public LazyDataModel<Aluno> getLazyDataModelExAlunos() {
		if (lazyListDataModelExAlunos == null) {

			lazyListDataModelExAlunos = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);
					filtros.put("removido", true);
					if (filtros.containsKey("periodo")) {
						filtros.put("periodo", filtros.get("periodo").equals("MANHA") ? PerioddoEnum.MANHA
								: filtros.get("periodo").equals("TARDE") ? PerioddoEnum.TARDE : PerioddoEnum.INTEGRAL);
					}

					if (filtros.containsKey("serie")) {
						if (filtros.get("serie").equals(Serie.JARDIM_I.toString())) {
							filtros.put("serie", Serie.JARDIM_I);
						} else if (filtros.get("serie").equals(Serie.JARDIM_II.toString())) {
							filtros.put("serie", Serie.JARDIM_II);
						} else if (filtros.get("serie").equals(Serie.MATERNAL.toString())) {
							filtros.put("serie", Serie.MATERNAL);
						} else if (filtros.get("serie").equals(Serie.PRE.toString())) {
							filtros.put("serie", Serie.PRE);
						} else if (filtros.get("serie").equals(Serie.PRIMEIRO_ANO.toString())) {
							filtros.put("serie", Serie.PRIMEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.SEGUNDO_ANO.toString())) {
							filtros.put("serie", Serie.SEGUNDO_ANO);
						} else if (filtros.get("serie").equals(Serie.TERCEIRO_ANO.toString())) {
							filtros.put("serie", Serie.TERCEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUARTO_ANO.toString())) {
							filtros.put("serie", Serie.QUARTO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUINTO_ANO.toString())) {
							filtros.put("serie", Serie.QUINTO_ANO);
						}

					}

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Aluno> ol = alunoService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModelExAlunos.setRowCount((int) alunoService.count(filtros));
						return ol;
					}

					this.setRowCount((int) alunoService.count(filtros));
					return null;

				}
			};
			lazyListDataModelExAlunos.setRowCount((int) alunoService.count(null));

		}

		return lazyListDataModelExAlunos;

	}

	public void enviarEmailBoletoAtualEAtrasado(final Long idAluno) {
		new Thread() {
			@Override
			public void run() {
				EnviadorEmail email = new EnviadorEmail();
				email.enviarEmailBoletosMesAtualEAtrasados(idAluno);
			}
		}.start();
	}

	public LazyDataModel<Aluno> getLazyDataModelUltimoAnoLetivo() {
		if (lazyListDataModelUltimoAnoLetivo == null) {

			lazyListDataModelUltimoAnoLetivo = new LazyDataModel<Aluno>() {

				@Override
				public Aluno getRowData(String rowKey) {
					return alunoService.findById(Long.valueOf(rowKey));
				}

				@Override
				public Object getRowKey(Aluno al) {
					return al.getId();
				}

				@Override
				public List<Aluno> load(int first, int pageSize, String order, SortOrder so,
						Map<String, Object> where) {

					Map<String, Object> filtros = new HashMap<String, Object>();

					filtros.putAll(where);

					filtros.put("anoLetivo", configuracao.getAnoLetivo() - 1);

					if (filtros.containsKey("periodo")) {
						filtros.put("periodo", filtros.get("periodo").equals("MANHA") ? PerioddoEnum.MANHA
								: filtros.get("periodo").equals("TARDE") ? PerioddoEnum.TARDE : PerioddoEnum.INTEGRAL);
					}

					if (filtros.containsKey("serie")) {
						if (filtros.get("serie").equals(Serie.JARDIM_I.toString())) {
							filtros.put("serie", Serie.JARDIM_I);
						} else if (filtros.get("serie").equals(Serie.JARDIM_II.toString())) {
							filtros.put("serie", Serie.JARDIM_II);
						} else if (filtros.get("serie").equals(Serie.MATERNAL.toString())) {
							filtros.put("serie", Serie.MATERNAL);
						} else if (filtros.get("serie").equals(Serie.PRE.toString())) {
							filtros.put("serie", Serie.PRE);
						} else if (filtros.get("serie").equals(Serie.PRIMEIRO_ANO.toString())) {
							filtros.put("serie", Serie.PRIMEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.SEGUNDO_ANO.toString())) {
							filtros.put("serie", Serie.SEGUNDO_ANO);
						} else if (filtros.get("serie").equals(Serie.TERCEIRO_ANO.toString())) {
							filtros.put("serie", Serie.TERCEIRO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUARTO_ANO.toString())) {
							filtros.put("serie", Serie.QUARTO_ANO);
						} else if (filtros.get("serie").equals(Serie.QUINTO_ANO.toString())) {
							filtros.put("serie", Serie.QUINTO_ANO);
						}

					}

					String orderByParam = (order != null) ? order : "id";
					String orderParam = ("ASCENDING".equals(so.name())) ? "asc" : "desc";

					List<Aluno> ol = alunoService.find(first, pageSize, orderByParam, orderParam, filtros);

					if (ol != null && ol.size() > 0) {
						lazyListDataModelUltimoAnoLetivo.setRowCount((int) alunoService.count(filtros));
						return ol;
					}

					this.setRowCount((int) alunoService.count(filtros));
					return null;

				}
			};
			lazyListDataModelUltimoAnoLetivo.setRowCount((int) alunoService.count(null));

		}

		return lazyListDataModelUltimoAnoLetivo;

	}

	public boolean estaEmUmaTurma(long idAluno) {
		boolean estaNaTurma = alunoService.estaEmUmaTUrma(idAluno);
		return estaNaTurma;
	}

	/*public String marcarLinha(Long idAluno) {
		String cor = "";
		if (idAluno == null) {
			return "";
		}
		Aluno a = alunoService.findById(idAluno);
		if (a.getRematricular() != null && a.getRematricular()) {
			cor = "marcarLinhaVerde";
		} else {
			boolean estaNaTurma = alunoService.estaEmUmaTUrma(idAluno);
			if (!estaNaTurma) {
				cor = "marcarLinha";
			}
		}
		return cor;
	}*/
	
	public String marcarLinha(Aluno a) {
		String cor = "";
		if(a != null){
			if (a.getRematricular() != null && a.getRematricular()) {
				cor = "marcarLinhaVerde";
			} else {
				//TODO VER
			//	boolean estaNaTurma = alunoService.estaEmUmaTUrma(a.getId());
			//	if (!estaNaTurma) {
			//		cor = "marcarLinha";
			//	}
			}	
		}
		
		return cor;
	}

	private Float maior(Float float1, Float float2) {
		if (Float.isNaN(float1)) {
			return float2;
		}
		if (Float.isNaN(float2)) {
			return float1;
		}

		return float1 > float2 ? float1 : float2;
	}

	private String mostraNotas(Float nota) {
		if (nota == null || nota == 0 || Float.isNaN(nota)) {
			return "";
		} else {
			DecimalFormat df = new DecimalFormat("0.##");
			String dx = df.format(nota);

			dx = dx.replace(",", ".");

			return String.valueOf((Math.round(Float.parseFloat(dx) / 0.5) * 0.5));
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

	public float getNota(DisciplinaEnum disciplina, BimestreEnum bimestre) {
		return alunoService.getNota(aluno.getId(), disciplina, bimestre, false);
	}

	public List<HistoricoAluno> getHistoricoAluno(Aluno aluno) {
		return alunoService.getHistoricoAluno(aluno);
	}
	
	public boolean alunoCadastrado(Aluno al){
		if(aluno.getId() != null){
			return true;
		}else{
			return false;
		}
	}

	public void popularAlunoAvaliacao(Aluno aluno) {
		setAlunoAvaliacaoPortugues(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.PORTUGUES,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoIngles(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.INGLES,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoEDFisica(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null,
				DisciplinaEnum.EDUCACAO_FISICA, this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoGeografia(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.GEOGRAFIA,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoHistoria(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.HISTORIA,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoMatematica(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null,
				DisciplinaEnum.MATEMATICA, this.getBimestreSel(), aluno.getSerie()));

		setAlunoAvaliacaoCiencias(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.CIENCIAS,
				this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoFormacaoCrista(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null,
				DisciplinaEnum.FORMACAO_CRISTA, this.getBimestreSel(), aluno.getSerie()));
		setAlunoAvaliacaoArtes(avaliacaoService.findAlunoAvaliacaoMap(aluno.getId(), null, DisciplinaEnum.ARTES,
				this.getBimestreSel(), aluno.getSerie()));
	}

	public boolean renderDisciplina(int ordinal) {
		if (getDisciplinaSel() == null) {
			return true;
		}

		return ordinal == getDisciplinaSel().ordinal();
	}

	public void saveAvaliacaoAluno(AlunoAvaliacao alav) {
		avaliacaoService.saveAlunoAvaliacao(alav);
	}

	public void saveAvaliacaoAluno(Long idAluAv, Float nota) {
		avaliacaoService.saveAlunoAvaliacao(idAluAv, nota);
	}

	private HashMap<String, String> montarBoletim(Aluno aluno) {
		Professor prof = alunoService.getProfessor(aluno.getId());
		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("#nomeAluno", aluno.getNomeAluno());
		trocas.put("#nomeProfessor", prof.getNome());
		trocas.put("#turma", aluno.getSerie().getName());

		// FALTAS
		trocas.put("#f1", aluno.getFaltas1Bimestre() != null ? aluno.getFaltas1Bimestre().toString() : "");
		trocas.put("#f2", aluno.getFaltas2Bimestre() != null ? aluno.getFaltas2Bimestre().toString() : "");
		trocas.put("#f3", aluno.getFaltas3Bimestre() != null ? aluno.getFaltas3Bimestre().toString() : "");
		trocas.put("#f4", aluno.getFaltas4Bimestre() != null ? aluno.getFaltas4Bimestre().toString() : "");

		float notaPortuguesPrimeiroBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float notaPortuguesSegundoBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float notaPortuguesTerceiroBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float notaPortuguesQuartoBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float notaPortuguesPrimeiroRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float notaPortuguesSegundoRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float notaPortuguesTerceiroRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float notaPortuguesQuartoRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaPortuguesRecFinal = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, true, true);

		// PORTUGUES
		trocas.put("#np1", mostraNotas(notaPortuguesPrimeiroBimestre));
		trocas.put("#np2", mostraNotas(notaPortuguesSegundoBimestre));
		trocas.put("#np3", mostraNotas(notaPortuguesTerceiroBimestre));
		trocas.put("#np4", mostraNotas(notaPortuguesQuartoBimestre));

		// rec
		trocas.put("#npr1", mostraNotas(notaPortuguesPrimeiroRec));
		trocas.put("#npr2", mostraNotas(notaPortuguesSegundoRec));
		trocas.put("#npr3", mostraNotas(notaPortuguesTerceiroRec));
		trocas.put("#npr4", mostraNotas(notaPortuguesQuartoRec));
		// mediaFinal
		trocas.put("#mp1", mostraNotas(maior(notaPortuguesPrimeiroBimestre, notaPortuguesPrimeiroRec)));
		trocas.put("#mp2", mostraNotas(maior(notaPortuguesSegundoRec, notaPortuguesSegundoBimestre)));
		trocas.put("#mp3", mostraNotas(maior(notaPortuguesTerceiroRec, notaPortuguesTerceiroBimestre)));
		trocas.put("#mp4", mostraNotas(maior(notaPortuguesQuartoRec, notaPortuguesQuartoBimestre)));
		// Final do ano
		trocas.put("#npF",
				mostraNotas(media(maior(notaPortuguesPrimeiroBimestre, notaPortuguesPrimeiroRec),
						maior(notaPortuguesSegundoRec, notaPortuguesSegundoBimestre),
						maior(notaPortuguesTerceiroRec, notaPortuguesTerceiroBimestre),
						maior(notaPortuguesQuartoRec, notaPortuguesQuartoBimestre))));
		trocas.put("#nprf", mostraNotas(notaPortuguesRecFinal));

		// Matematica
		float notaMTM1Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float notaMTM2Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float notaMTM3Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float notaMTM4Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float notaMTM1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float notaMTM2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float notaMTM3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float notaMTM4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.QUARTO_BIMESTRE,
				true);
		float notaMtmRecFinal = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, true, true);

		trocas.put("#nm1", mostraNotas(notaMTM1Bimestre));
		trocas.put("#nm2", mostraNotas(notaMTM2Bimestre));
		trocas.put("#nm3", mostraNotas(notaMTM3Bimestre));
		trocas.put("#nm4", mostraNotas(notaMTM4Bimestre));
		// rec
		trocas.put("#nmr1", mostraNotas(notaMTM1Rec));
		trocas.put("#nmr2", mostraNotas(notaMTM2Rec));
		trocas.put("#nmr3", mostraNotas(notaMTM3Rec));
		trocas.put("#nmr4", mostraNotas(notaMTM4Rec));
		// mediaFinal
		trocas.put("#mm1", mostraNotas(maior(notaMTM1Bimestre, notaMTM1Rec)));
		trocas.put("#mm2", mostraNotas(maior(notaMTM2Bimestre, notaMTM2Rec)));
		trocas.put("#mm3", mostraNotas(maior(notaMTM3Bimestre, notaMTM3Rec)));
		trocas.put("#mm4", mostraNotas(maior(notaMTM4Bimestre, notaMTM4Rec)));
		// Final do ano
		trocas.put("#nmF", mostraNotas(media(maior(notaMTM1Bimestre, notaMTM1Rec), maior(notaMTM2Bimestre, notaMTM2Rec),
				maior(notaMTM3Bimestre, notaMTM3Rec), maior(notaMTM4Bimestre, notaMTM4Rec))));
		trocas.put("#nmrf", mostraNotas(notaMtmRecFinal));

		// Ingles
		float nota1BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.SEGUNDO_BIMESTRE,
				true);
		float nota3RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.QUARTO_BIMESTRE,
				true);
		float notaRecFinalIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, true, true);

		trocas.put("#ni1", mostraNotas(nota1BimestreIngles));
		trocas.put("#ni2", mostraNotas(nota2BimestreIngles));
		trocas.put("#ni3", mostraNotas(nota3BimestreIngles));
		trocas.put("#ni4", mostraNotas(nota4BimestreIngles));
		// rec
		trocas.put("#nir1", mostraNotas(nota1RecIngles));
		trocas.put("#nir2", mostraNotas(nota2RecIngles));
		trocas.put("#nir3", mostraNotas(nota3RecIngles));
		trocas.put("#nir4", mostraNotas(nota4RecIngles));
		// mediaFinal
		trocas.put("#mi1", mostraNotas(maior(nota1RecIngles, nota1BimestreIngles)));
		trocas.put("#mi2", mostraNotas(maior(nota2RecIngles, nota2BimestreIngles)));
		trocas.put("#mi3", mostraNotas(maior(nota3RecIngles, nota3BimestreIngles)));
		trocas.put("#mi4", mostraNotas(maior(nota4RecIngles, nota4BimestreIngles)));
		// Final do ano
		trocas.put("#niF",
				mostraNotas(media(maior(nota1RecIngles, nota1BimestreIngles),
						maior(nota2RecIngles, nota2BimestreIngles), maior(nota3RecIngles, nota3BimestreIngles),
						maior(nota4RecIngles, nota4BimestreIngles))));
		trocas.put("#nirf", mostraNotas(notaRecFinalIngles));

		// EdFisica
		float nota1BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, true, true);

		trocas.put("#ne1", mostraNotas(nota1BimestreEdFisica));
		trocas.put("#ne2", mostraNotas(nota2BimestreEdFisica));
		trocas.put("#ne3", mostraNotas(nota3BimestreEdFisica));
		trocas.put("#ne4", mostraNotas(nota4BimestreEdFisica));
		// rec
		trocas.put("#ner1", mostraNotas(nota1RecEdFisica));
		trocas.put("#ner2", mostraNotas(nota2RecEdFisica));
		trocas.put("#ner3", mostraNotas(nota3RecEdFisica));
		trocas.put("#ner4", mostraNotas(nota4RecEdFisica));
		// mediaFinal
		trocas.put("#me1", mostraNotas(maior(nota1BimestreEdFisica, nota1RecEdFisica)));
		trocas.put("#me2", mostraNotas(maior(nota2BimestreEdFisica, nota2RecEdFisica)));
		trocas.put("#me3", mostraNotas(maior(nota3BimestreEdFisica, nota3RecEdFisica)));
		trocas.put("#me4", mostraNotas(maior(nota4BimestreEdFisica, nota4RecEdFisica)));
		// Final do ano
		trocas.put("#neF",
				mostraNotas(media(maior(nota1BimestreEdFisica, nota1RecEdFisica),
						maior(nota2BimestreEdFisica, nota2RecEdFisica), maior(nota3BimestreEdFisica, nota3RecEdFisica),
						maior(nota4BimestreEdFisica, nota4RecEdFisica))));
		trocas.put("#nerf", mostraNotas(notaRecFinalEdFisica));

		// Geofrafia
		float nota1BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, true, true);

		trocas.put("#ng1", mostraNotas(nota1BimestreGeografia));
		trocas.put("#ng2", mostraNotas(nota2BimestreGeografia));
		trocas.put("#ng3", mostraNotas(nota3BimestreGeografia));
		trocas.put("#ng4", mostraNotas(nota4BimestreGeografia));
		// rec
		trocas.put("#ngr1", mostraNotas(nota1RecGeografia));
		trocas.put("#ngr2", mostraNotas(nota2RecGeografia));
		trocas.put("#ngr3", mostraNotas(nota3RecGeografia));
		trocas.put("#ngr4", mostraNotas(nota4RecGeografia));
		// mediaFinal
		trocas.put("#mg1", mostraNotas(maior(nota1RecGeografia, nota1BimestreGeografia)));
		trocas.put("#mg2", mostraNotas(maior(nota2RecGeografia, nota2BimestreGeografia)));
		trocas.put("#mg3", mostraNotas(maior(nota3RecGeografia, nota3BimestreGeografia)));
		trocas.put("#mg4", mostraNotas(maior(nota4RecGeografia, nota4BimestreGeografia)));
		// Final do ano
		trocas.put("#ngF",
				mostraNotas(media(maior(nota1RecGeografia, nota1BimestreGeografia),
						maior(nota2RecGeografia, nota2BimestreGeografia),
						maior(nota3RecGeografia, nota3BimestreGeografia),
						maior(nota4RecGeografia, nota4BimestreGeografia))));
		trocas.put("#ngrf", mostraNotas(notaRecFinalGeografia));

		// Historia
		float nota1BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, true, true);

		trocas.put("#nh1", mostraNotas(nota1BimestreHistoria));
		trocas.put("#nh2", mostraNotas(nota2BimestreHistoria));
		trocas.put("#nh3", mostraNotas(nota3BimestreHistoria));
		trocas.put("#nh4", mostraNotas(nota4BimestreHistoria));
		// rec
		trocas.put("#nhr1", mostraNotas(nota1RecHistoria));
		trocas.put("#nhr2", mostraNotas(nota2RecHistoria));
		trocas.put("#nhr3", mostraNotas(nota3RecHistoria));
		trocas.put("#nhr4", mostraNotas(nota4RecHistoria));
		// mediaFinal
		trocas.put("#mh1", mostraNotas(maior(nota1RecHistoria, nota1BimestreHistoria)));
		trocas.put("#mh2", mostraNotas(maior(nota2RecHistoria, nota2BimestreHistoria)));
		trocas.put("#mh3", mostraNotas(maior(nota3RecHistoria, nota3BimestreHistoria)));
		trocas.put("#mh4", mostraNotas(maior(nota4RecHistoria, nota4BimestreHistoria)));
		// Final do ano
		trocas.put("#nhF",
				mostraNotas(media(maior(nota1RecHistoria, nota1BimestreHistoria),
						maior(nota2RecHistoria, nota2BimestreHistoria), maior(nota3RecHistoria, nota3BimestreHistoria),
						maior(nota4RecHistoria, nota4BimestreHistoria))));
		trocas.put("#nhrf", mostraNotas(notaRecFinalHistoria));

		// Ciencias
		float nota1BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalCiencia = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, true, true);

		trocas.put("#nc1", mostraNotas(nota1BimestreCiencias));
		trocas.put("#nc2", mostraNotas(nota2BimestreCiencias));
		trocas.put("#nc3", mostraNotas(nota3BimestreCiencias));
		trocas.put("#nc4", mostraNotas(nota4BimestreCiencias));
		// rec
		trocas.put("#ncr1", mostraNotas(nota1RecCiencias));
		trocas.put("#ncr2", mostraNotas(nota2RecCiencias));
		trocas.put("#ncr3", mostraNotas(nota3RecCiencias));
		trocas.put("#ncr4", mostraNotas(nota4RecCiencias));
		// mediaFinal
		trocas.put("#mc1", mostraNotas(maior(nota1RecCiencias, nota1BimestreCiencias)));
		trocas.put("#mc2", mostraNotas(maior(nota2RecCiencias, nota2BimestreCiencias)));
		trocas.put("#mc3", mostraNotas(maior(nota3RecCiencias, nota3BimestreCiencias)));
		trocas.put("#mc4", mostraNotas(maior(nota4RecCiencias, nota4BimestreCiencias)));
		// Final do ano
		trocas.put("#ncF",
				mostraNotas(media(maior(nota1RecCiencias, nota1BimestreCiencias),
						maior(nota2RecCiencias, nota2BimestreCiencias), maior(nota3RecCiencias, nota3BimestreCiencias),
						maior(nota4RecCiencias, nota4BimestreCiencias))));
		trocas.put("#ncrf", mostraNotas(notaRecFinalHistoria));

		// Formacao Crista
		float nota1BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, true, true);

		trocas.put("#nf1", mostraNotas(nota1BimestreFormaCrista));
		trocas.put("#nf2", mostraNotas(nota2BimestreFormaCrista));
		trocas.put("#nf3", mostraNotas(nota3BimestreFormaCrista));
		trocas.put("#nf4", mostraNotas(nota4BimestreFormaCrista));
		// rec
		trocas.put("#nfr1", mostraNotas(nota1RecFormaCrista));
		trocas.put("#nfr2", mostraNotas(nota2RecFormaCrista));
		trocas.put("#nfr3", mostraNotas(nota3RecFormaCrista));
		trocas.put("#nfr4", mostraNotas(nota4RecFormaCrista));
		// mediaFinal
		trocas.put("#mf1", mostraNotas(maior(nota1RecFormaCrista, nota1BimestreFormaCrista)));
		trocas.put("#mf2", mostraNotas(maior(nota2RecFormaCrista, nota2BimestreFormaCrista)));
		trocas.put("#mf3", mostraNotas(maior(nota3RecFormaCrista, nota3BimestreFormaCrista)));
		trocas.put("#mf4", mostraNotas(maior(nota4RecFormaCrista, nota4BimestreFormaCrista)));
		// Final do ano
		trocas.put("#nfF",
				mostraNotas(media(maior(nota1RecFormaCrista, nota1BimestreFormaCrista),
						maior(nota2RecFormaCrista, nota2BimestreFormaCrista),
						maior(nota3RecFormaCrista, nota3BimestreFormaCrista),
						maior(nota4RecFormaCrista, nota4BimestreFormaCrista))));
		trocas.put("#nfrf", mostraNotas(notaRecFinalFormaCrista));

		// Artes
		float nota1BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.PRIMEIRO_BIMESTRE,
				true);
		float nota2RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.SEGUNDO_BIMESTRE,
				true);
		float nota3RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.TERCEIRO_BIMESTRE,
				true);
		float nota4RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.QUARTO_BIMESTRE,
				true);
		float notaRecFinalArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, true, true);

		trocas.put("#na1", mostraNotas(nota1BimestreArtes));
		trocas.put("#na2", mostraNotas(nota2BimestreArtes));
		trocas.put("#na3", mostraNotas(nota3BimestreArtes));
		trocas.put("#na4", mostraNotas(nota4BimestreArtes));
		// rec
		trocas.put("#nar1", mostraNotas(nota1RecArtes));
		trocas.put("#nar2", mostraNotas(nota2RecArtes));
		trocas.put("#nar3", mostraNotas(nota3RecArtes));
		trocas.put("#nar4", mostraNotas(nota4RecArtes));
		// mediaFinal
		trocas.put("#ma1", mostraNotas(maior(nota1RecArtes, nota1BimestreArtes)));
		trocas.put("#ma2", mostraNotas(maior(nota2RecArtes, nota2BimestreArtes)));
		trocas.put("#ma3", mostraNotas(maior(nota3RecArtes, nota3BimestreArtes)));
		trocas.put("#ma4", mostraNotas(maior(nota4RecArtes, nota4BimestreArtes)));
		// Final do ano
		trocas.put("#naF",
				mostraNotas(media(maior(nota1RecArtes, nota1BimestreArtes), maior(nota2RecArtes, nota2BimestreArtes),
						maior(nota3RecArtes, nota3BimestreArtes), maior(nota4RecArtes, nota4BimestreArtes))));
		trocas.put("#narf", mostraNotas(notaRecFinalArtes));

		return trocas;
	}

	public HashMap<String, String> montarAtestadoFrequencia(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonaiturma", aluno.getSerie().getName());
		trocas.put("adonaiperiodo", aluno.getPeriodo().getName());
		trocas.put("adonaidata", dataExtenso);

		return trocas;
	}

	public HashMap<String, String> montarAtestadoMatricula(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonaiturma", aluno.getSerie().getName());
		trocas.put("adonaiperiodo", aluno.getPeriodo().getName());
		trocas.put("adonaidata", dataExtenso);

		return trocas;
	}

	public HashMap<String, String> montarAtestadoNegativoDebito(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonaiturma", aluno.getSerie().getName());
		trocas.put("adonaiperiodo", aluno.getPeriodo().getName());
		trocas.put("adonaidata", dataExtenso);
		trocas.put("adonaicpfresponsavel", aluno.getContratoVigente().getCpfResponsavel());
		trocas.put("adonainomeresponsavel", aluno.getContratoVigente().getNomeResponsavel());

		return trocas;
	}

	public HashMap<String, String> montarAtestadoVaga(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("adonainomealuno", aluno.getNomeAluno());
		trocas.put("adonaiturma", aluno.getSerie().getName());
		trocas.put("adonaiperiodo", aluno.getPeriodo().getName());
		trocas.put("adonaidata", dataExtenso);

		return trocas;
	}

	private HistoricoAluno getHistorico(List<HistoricoAluno> historicos, Serie serie) {
		for (HistoricoAluno historico : historicos) {
			if (historico.getSerie() != null && historico.getSerie().equals(serie)) {
				return historico;
			}
		}
		return null;
	}

	public HashMap<String, String> montarHistorico(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());

		DateFormat formatadorNascimento = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("pt", "BR"));

		String dtNascimento = aluno.getDataNascimento() != null ? formatadorNascimento.format(aluno.getDataNascimento())
				: "";

		List<HistoricoAluno> historicoAlunos = alunoService.getHistoricoAluno(aluno);
		HistoricoAluno historico1Ano = getHistorico(historicoAlunos, Serie.PRIMEIRO_ANO);
		HistoricoAluno historico2Ano = getHistorico(historicoAlunos, Serie.SEGUNDO_ANO);
		HistoricoAluno historico3Ano = getHistorico(historicoAlunos, Serie.TERCEIRO_ANO);
		HistoricoAluno historico4Ano = getHistorico(historicoAlunos, Serie.QUARTO_ANO);
		HistoricoAluno historico5Ano = getHistorico(historicoAlunos, Serie.QUINTO_ANO);

		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("nomealunoiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii",
				aluno.getNomeAluno() + Util.criarEspacos(58 - aluno.getNomeAluno().length()));
		trocas.put("datanascimento", dtNascimento);
		trocas.put("naturalidade", aluno.getNacionalidade() + Util.criarEspacos(0));
		trocas.put("nomepais", (aluno.getNomeMaeAluno() != null ? aluno.getNomeMaeAluno() : "").toUpperCase());
		trocas.put("dataextenso", dataExtenso.toLowerCase());
		trocas.put("sexo", aluno.getSexo() != null ? aluno.getSexo().getName() : "");

		// ANO
		trocas.put("a1", historico1Ano != null ? "" + historico1Ano.getAno() : "");
		trocas.put("a2", historico2Ano != null ? "" + historico2Ano.getAno() : "");
		trocas.put("a3", historico3Ano != null ? "" + historico3Ano.getAno() : "");
		trocas.put("a4", historico4Ano != null ? "" + historico4Ano.getAno() : "");
		trocas.put("a5", historico5Ano != null ? "" + historico5Ano.getAno() : "");

		// Frequencia
		trocas.put("fb", historico1Ano != null ? historico1Ano.getFrequencia() : "");
		trocas.put("fc", historico2Ano != null ? historico2Ano.getFrequencia() : "");
		trocas.put("fd", historico3Ano != null ? historico3Ano.getFrequencia() : "");
		trocas.put("fg", historico4Ano != null ? historico4Ano.getFrequencia() : "");
		trocas.put("fh", historico5Ano != null ? historico5Ano.getFrequencia() : "");

		// Frequencia
		trocas.put("aprovado1", historico1Ano != null ? "Aprovado" : "");
		trocas.put("aprovado2", historico2Ano != null ? "Aprovado" : "");
		trocas.put("aprovado3", historico3Ano != null ? "Aprovado" : "");
		trocas.put("aprxp34", historico4Ano != null ? "Aprovado" : "");
		trocas.put("aprovado5", historico5Ano != null ? "Aprovado" : "");

		// ESCOLA
		trocas.put("e1", historico1Ano != null ? "" + historico1Ano.getEscola() : "");
		trocas.put("e2", historico2Ano != null ? "" + historico2Ano.getEscola() : "");
		trocas.put("e3", historico3Ano != null ? "" + historico3Ano.getEscola() : "");
		trocas.put("e4", historico4Ano != null ? "" + historico4Ano.getEscola() : "");
		trocas.put("e5", historico5Ano != null ? "" + historico5Ano.getEscola() : "");

		// Municipio
		trocas.put("m1", historico1Ano != null ? "" + historico1Ano.getMunicipio() : "");
		trocas.put("m2", historico2Ano != null ? "" + historico2Ano.getMunicipio() : "");
		trocas.put("m3", historico3Ano != null ? "" + historico3Ano.getMunicipio() : "");
		trocas.put("m4", historico4Ano != null ? "" + historico4Ano.getMunicipio() : "");
		trocas.put("m5", historico5Ano != null ? "" + historico5Ano.getMunicipio() : "");

		// Estado
		trocas.put("es1", historico1Ano != null ? "" + historico1Ano.getEstado() : "");
		trocas.put("es2", historico2Ano != null ? "" + historico2Ano.getEstado() : "");
		trocas.put("es3", historico3Ano != null ? "" + historico3Ano.getEstado() : "");
		trocas.put("es4", historico4Ano != null ? "" + historico4Ano.getEstado() : "");
		trocas.put("es5", historico5Ano != null ? "" + historico5Ano.getEstado() : "");

		// notar Portugues
		trocas.put("hp1", historico1Ano != null ? mostraNotas(historico1Ano.getNotaPortugues()) : "");
		trocas.put("hp2", historico2Ano != null ? mostraNotas(historico2Ano.getNotaPortugues()) : "");
		trocas.put("hp3", historico3Ano != null ? mostraNotas(historico3Ano.getNotaPortugues()) : "");
		trocas.put("hp4", historico4Ano != null ? mostraNotas(historico4Ano.getNotaPortugues()) : "");
		trocas.put("hp5", historico5Ano != null ? mostraNotas(historico5Ano.getNotaPortugues()) : "");

		// notar Matematica
		trocas.put("hm1", historico1Ano != null ? mostraNotas(historico1Ano.getNotaMatematica()) : "");
		trocas.put("hm2", historico2Ano != null ? mostraNotas(historico2Ano.getNotaMatematica()) : "");
		trocas.put("hm3", historico3Ano != null ? mostraNotas(historico3Ano.getNotaMatematica()) : "");
		trocas.put("hm4", historico4Ano != null ? mostraNotas(historico4Ano.getNotaMatematica()) : "");
		trocas.put("hm5", historico5Ano != null ? mostraNotas(historico5Ano.getNotaMatematica()) : "");

		// notar Ciências
		trocas.put("hc1", historico1Ano != null ? mostraNotas(historico1Ano.getNotaCiencias()) : "");
		trocas.put("hc2", historico2Ano != null ? mostraNotas(historico2Ano.getNotaCiencias()) : "");
		trocas.put("hc3", historico3Ano != null ? mostraNotas(historico3Ano.getNotaCiencias()) : "");
		trocas.put("hc4", historico4Ano != null ? mostraNotas(historico4Ano.getNotaCiencias()) : "");
		trocas.put("hc5", historico5Ano != null ? mostraNotas(historico5Ano.getNotaCiencias()) : "");

		// notar História
		trocas.put("hh1", historico1Ano != null ? mostraNotas(historico1Ano.getNotaHistoria()) : "");
		trocas.put("hh2", historico2Ano != null ? mostraNotas(historico2Ano.getNotaHistoria()) : "");
		trocas.put("hh3", historico3Ano != null ? mostraNotas(historico3Ano.getNotaHistoria()) : "");
		trocas.put("hh4", historico4Ano != null ? mostraNotas(historico4Ano.getNotaHistoria()) : "");
		trocas.put("hh5", historico5Ano != null ? mostraNotas(historico5Ano.getNotaHistoria()) : "");

		// notar Geografia
		trocas.put("hg1", historico1Ano != null ? mostraNotas(historico1Ano.getNotaGeografia()) : "");
		trocas.put("hg2", historico2Ano != null ? mostraNotas(historico2Ano.getNotaGeografia()) : "");
		trocas.put("hg3", historico3Ano != null ? mostraNotas(historico3Ano.getNotaGeografia()) : "");
		trocas.put("hg4", historico4Ano != null ? mostraNotas(historico4Ano.getNotaGeografia()) : "");
		trocas.put("hg5", historico5Ano != null ? mostraNotas(historico5Ano.getNotaGeografia()) : "");

		// Ed Fisica
		trocas.put("he1", historico1Ano != null ? mostraNotas(historico1Ano.getNotaEdFisica()) : "");
		trocas.put("he2", historico2Ano != null ? mostraNotas(historico2Ano.getNotaEdFisica()) : "");
		trocas.put("he3", historico3Ano != null ? mostraNotas(historico3Ano.getNotaEdFisica()) : "");
		trocas.put("he4", historico4Ano != null ? mostraNotas(historico4Ano.getNotaEdFisica()) : "");
		trocas.put("he5", historico5Ano != null ? mostraNotas(historico5Ano.getNotaEdFisica()) : "");

		// notar Artes
		trocas.put("ha1", historico1Ano != null ? mostraNotas(historico1Ano.getNotaArtes()) : "");
		trocas.put("ha2", historico2Ano != null ? mostraNotas(historico2Ano.getNotaArtes()) : "");
		trocas.put("ha3", historico3Ano != null ? mostraNotas(historico3Ano.getNotaArtes()) : "");
		trocas.put("ha4", historico4Ano != null ? mostraNotas(historico4Ano.getNotaArtes()) : "");
		trocas.put("ha5", historico5Ano != null ? mostraNotas(historico5Ano.getNotaArtes()) : "");

		// notar Inglês
		trocas.put("hi1", historico1Ano != null ? mostraNotas(historico1Ano.getNotaIngles()) : "");
		trocas.put("hi2", historico2Ano != null ? mostraNotas(historico2Ano.getNotaIngles()) : "");
		trocas.put("hi3", historico3Ano != null ? mostraNotas(historico3Ano.getNotaIngles()) : "");
		trocas.put("hi4", historico4Ano != null ? mostraNotas(historico4Ano.getNotaIngles()) : "");
		trocas.put("hi5", historico5Ano != null ? mostraNotas(historico5Ano.getNotaIngles()) : "");

		// notar Formacao Crista
		trocas.put("hf1", historico1Ano != null ? mostraNotas(historico1Ano.getNotaformacaoCrista()) : "");
		trocas.put("hf2", historico2Ano != null ? mostraNotas(historico2Ano.getNotaformacaoCrista()) : "");
		trocas.put("hf3", historico3Ano != null ? mostraNotas(historico3Ano.getNotaformacaoCrista()) : "");
		trocas.put("hf4", historico4Ano != null ? mostraNotas(historico4Ano.getNotaformacaoCrista()) : "");
		trocas.put("hf5", historico5Ano != null ? mostraNotas(historico5Ano.getNotaformacaoCrista()) : "");

		return trocas;
	}

	public HashMap<String, String> montarContrato(Aluno aluno) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());
		Calendar dataLim = Calendar.getInstance();
		dataLim.add(Calendar.MONTH, 1);
		String dataLimiteExtenso = formatador.format(dataLim.getTime());

		boolean rematricula = (aluno.getRematricular() != null && aluno.getRematricular()) ? true : false;
		HashMap<String, String> trocas = new HashMap<>();

		String nomeAluno = aluno.getNomeAluno();
		String nomeSerie = rematricula ? Serie.values()[aluno.getSerie().ordinal() + 1].getName()
				: Serie.values()[aluno.getSerie().ordinal()].getName();
		String nomePeriodo = aluno.getPeriodoProximoAno().getName();

		if (aluno.getIrmao1() != null
				&& ((aluno.getIrmao1().getRematricular() != null && aluno.getIrmao1().getRematricular())
						|| aluno.getIrmao1().getRematricular() == null)) {
			nomeAluno += ", " + aluno.getIrmao1().getNomeAluno();
			nomeSerie += ", " + (rematricula ? Serie.values()[aluno.getIrmao1().getSerie().ordinal() + 1].getName()
					: Serie.values()[aluno.getIrmao1().getSerie().ordinal()].getName());
			nomePeriodo += ", " + aluno.getIrmao1().getPeriodoProximoAno().getName();

		}
		if (aluno.getIrmao2() != null
				&& ((aluno.getIrmao2().getRematricular() != null && aluno.getIrmao2().getRematricular())
						|| aluno.getIrmao2().getRematricular() == null)) {
			nomeAluno += ", " + aluno.getIrmao2().getNomeAluno();
			nomeSerie += ", " + (rematricula ? Serie.values()[aluno.getIrmao2().getSerie().ordinal() + 1].getName()
					: Serie.values()[aluno.getIrmao2().getSerie().ordinal()].getName());
			nomePeriodo += ", " + aluno.getIrmao2().getPeriodoProximoAno().getName();
		}
		if (aluno.getIrmao3() != null
				&& ((aluno.getIrmao3().getRematricular() != null && aluno.getIrmao3().getRematricular())
						|| aluno.getIrmao3().getRematricular() == null)) {
			nomeAluno += ", " + aluno.getIrmao3().getNomeAluno();
			nomeSerie += ", " + (rematricula ? Serie.values()[aluno.getIrmao3().getSerie().ordinal() + 1].getName()
					: Serie.values()[aluno.getIrmao3().getSerie().ordinal()].getName());
			nomePeriodo += ", " + aluno.getIrmao3().getPeriodoProximoAno().getName();
		}
		if (aluno.getIrmao4() != null && aluno.getIrmao4().getRematricular() != null
				&& aluno.getIrmao4().getRematricular()) {
			nomeAluno += ", " + aluno.getIrmao4().getNomeAluno();
			nomeSerie += ", " + (rematricula ? Serie.values()[aluno.getIrmao4().getSerie().ordinal() + 1].getName()
					: Serie.values()[aluno.getIrmao4().getSerie().ordinal()].getName());
			nomePeriodo += ", " + aluno.getIrmao4().getPeriodoProximoAno().getName();
		}
		int ano = configuracao.getAnoLetivo();
		if (aluno.getRematricular() != null && aluno.getRematricular()) {
			ano = configuracao.getAnoRematricula();
		}

		trocas.put("adonaianoletivo", ano + "");
		trocas.put("adonainomealuno", nomeAluno);
		trocas.put("adonaiturma", nomeSerie);
		trocas.put("adonaiperiodo", nomePeriodo);
		trocas.put("adonaidata", dataExtenso);
		trocas.put("adonaidatalimtevaga", dataLimiteExtenso);
		trocas.put("adonaivalortotal",
				(aluno.getContratoVigente().getValorMensal() * aluno.getContratoVigente().getNumeroParcelas()) + "0");
		trocas.put("adonainumeroparcelas", (aluno.getContratoVigente().getNumeroParcelas()) + "");
		trocas.put("adonaimensalidade", (aluno.getContratoVigente().getValorMensal()) + "0");
		trocas.put("adonainomecontratado", aluno.getContratoVigente().getNomeResponsavel().toUpperCase());

		trocas.put("adonairgcontratado", aluno.getContratoVigente().getRgResponsavel());
		trocas.put("adonaicpfcontratado", aluno.getContratoVigente().getCpfResponsavel());
		trocas.put("adonaimesespagar",
				getMesInicio(aluno.getContratoVigente().getNumeroParcelas()) + " a " + "DEZEMBRO");
		trocas.put("mesinicio", getMesInicio(aluno.getContratoVigente().getNumeroParcelas()));
		trocas.put("mesfim", "DEZEMBRO");

		trocas.put("adonainometestemunha", "ABIMAEL ALDEVINO FIDENCIO");
		trocas.put("adonaicpftestemunha", "066.606.049-52");
		trocas.put("adonainomedoistestemunha", "MARCELO LOURENCO VANDRESEN");
		trocas.put("adonaicpftdoisestemunha", "057.002.879-51");

		return trocas;
	}

	public HashMap<String, String> montarContrato(ContratoAluno contrato) {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
		String dataExtenso = formatador.format(new Date());
		Calendar dataLim = Calendar.getInstance();
		dataLim.add(Calendar.MONTH, 1);
		String dataLimiteExtenso = formatador.format(dataLim.getTime());

		boolean rematricula = (contrato.getAluno().getRematricular() != null && contrato.getAluno().getRematricular()) ? true : false;
		HashMap<String, String> trocas = new HashMap<>();

		String nomeAluno = contrato.getAluno().getNomeAluno();
		String nomeSerie = rematricula ? Serie.values()[contrato.getAluno().getSerie().ordinal() + 1].getName()
				: Serie.values()[contrato.getAluno().getSerie().ordinal()].getName();

		String nomePeriodo = "";
		if(contrato.getAluno().getRematricular() != null && contrato.getAluno().getRematricular()){
			nomePeriodo = contrato.getAluno().getPeriodoProximoAno().getName();
		}else{
			nomePeriodo = contrato.getAluno().getPeriodo().getName();
		}

		if (contrato.getAluno().getIrmao1() != null
				&& ((contrato.getAluno().getIrmao1().getRematricular() != null && contrato.getAluno().getIrmao1().getRematricular())
						|| contrato.getAluno().getIrmao1().getRematricular() == null)) {
			nomeAluno += ", " + contrato.getAluno().getIrmao1().getNomeAluno();
			nomeSerie += ", " + (rematricula ? Serie.values()[contrato.getAluno().getIrmao1().getSerie().ordinal() + 1].getName()
					: Serie.values()[contrato.getAluno().getIrmao1().getSerie().ordinal()].getName());
			nomePeriodo += ", " + contrato.getAluno().getIrmao1().getPeriodoProximoAno().getName();

		}
		if (contrato.getAluno().getIrmao2() != null
				&& ((contrato.getAluno().getIrmao2().getRematricular() != null && contrato.getAluno().getIrmao2().getRematricular())
						|| contrato.getAluno().getIrmao2().getRematricular() == null)) {
			nomeAluno += ", " + contrato.getAluno().getIrmao2().getNomeAluno();
			nomeSerie += ", " + (rematricula ? Serie.values()[contrato.getAluno().getIrmao2().getSerie().ordinal() + 1].getName()
					: Serie.values()[contrato.getAluno().getIrmao2().getSerie().ordinal()].getName());
			nomePeriodo += ", " + contrato.getAluno().getIrmao2().getPeriodoProximoAno().getName();
		}
		if (contrato.getAluno().getIrmao3() != null
				&& ((contrato.getAluno().getIrmao3().getRematricular() != null && contrato.getAluno().getIrmao3().getRematricular())
						|| contrato.getAluno().getIrmao3().getRematricular() == null)) {
			nomeAluno += ", " + contrato.getAluno().getIrmao3().getNomeAluno();
			nomeSerie += ", " + (rematricula ? Serie.values()[contrato.getAluno().getIrmao3().getSerie().ordinal() + 1].getName()
					: Serie.values()[contrato.getAluno().getIrmao3().getSerie().ordinal()].getName());
			nomePeriodo += ", " + contrato.getAluno().getIrmao3().getPeriodoProximoAno().getName();
		}
		if (contrato.getAluno().getIrmao4() != null && contrato.getAluno().getIrmao4().getRematricular() != null
				&& contrato.getAluno().getIrmao4().getRematricular()) {
			nomeAluno += ", " + contrato.getAluno().getIrmao4().getNomeAluno();
			nomeSerie += ", " + (rematricula ? Serie.values()[contrato.getAluno().getIrmao4().getSerie().ordinal() + 1].getName()
					: Serie.values()[contrato.getAluno().getIrmao4().getSerie().ordinal()].getName());
			nomePeriodo += ", " + contrato.getAluno().getIrmao4().getPeriodoProximoAno().getName();
		}
		int ano = configuracao.getAnoLetivo();
		if (contrato.getAluno().getRematricular() != null && contrato.getAluno().getRematricular()) {
			ano = configuracao.getAnoRematricula();
		}

		trocas.put("adonaianoletivo", ano + "");
		trocas.put("adonainomealuno", nomeAluno);
		trocas.put("adonaiturma", nomeSerie);
		trocas.put("adonaiperiodo", nomePeriodo);
		trocas.put("adonaidata", dataExtenso);
		trocas.put("adonaidatalimtevaga", dataLimiteExtenso);
		trocas.put("adonaivalortotal",
				(contrato.getValorMensal() * contrato.getNumeroParcelas()) + "0");
		trocas.put("adonainumeroparcelas", (contrato.getNumeroParcelas()) + "");
		trocas.put("adonaimensalidade", (contrato.getValorMensal()) + "0");
		trocas.put("adonainomecontratado", contrato.getNomeResponsavel().toUpperCase());

		trocas.put("adonairgcontratado", contrato.getRgResponsavel());
		trocas.put("adonaicpfcontratado", contrato.getCpfResponsavel());
		trocas.put("adonaimesespagar",
				getMesInicio(contrato.getNumeroParcelas()) + " a " + "DEZEMBRO");
		trocas.put("mesinicio", getMesInicio(contrato.getNumeroParcelas()));
		trocas.put("mesfim", "DEZEMBRO");

		trocas.put("adonainometestemunha", "ABIMAEL ALDEVINO FIDENCIO");
		trocas.put("adonaicpftestemunha", "066.606.049-52");
		trocas.put("adonainomedoistestemunha", "MARCELO LOURENCO VANDRESEN");
		trocas.put("adonaicpftdoisestemunha", "057.002.879-51");

		return trocas;
	}

	
	private String getMesInicio(int parcelas) {
		String mes = "JANEIRO";
		switch (parcelas) {

		case 12:
			mes = "JANEIRO";
			break;

		case 11:
			mes = "FEVEREIRO";
			break;

		case 10:
			mes = "MARCO";
			break;

		case 9:
			mes = "ABRIL";
			break;

		case 8:
			mes = "MAIO";
			break;

		case 7:
			mes = "JUNHO";
			break;

		case 6:
			mes = "JULHO";
			break;

		case 5:
			mes = "AGOSTO";
			break;

		case 4:
			mes = "SETEMBRO";
			break;

		case 3:
			mes = "OUTUBRO";
			break;

		case 2:
			mes = "NOVEMBRO";
			break;

		case 1:
			mes = "DEZEMBRO";
			break;

		default:
			mes = "JANEIRO";
			break;
		}

		return mes;
	}

	public void onRowSelect(SelectEvent event) {
		Aluno al = (Aluno) event.getObject();
		FacesMessage msg = new FacesMessage("Car Selected");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselect(UnselectEvent event) {
		Aluno al = (Aluno) event.getObject();
		FacesMessage msg = new FacesMessage("Car Unselected");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public boolean isAlunoSelecionado() {
		if (aluno == null) {
			return false;
		}
		return aluno.getId() != null ? true : false;
	}

	public boolean isMatriculado() {
		if (aluno == null) {
			return false;
		}
		if (aluno.getRematricular() == null) {
			return false;
		}
		return aluno.getRematricular();
	}

	public boolean isCnabEnvado() {
		if (aluno == null) {
			return false;
		}
		if (aluno.getContratoVigente() == null) {
			return false;
		}
		if (aluno.getContratoVigente().getCnabEnviado() == null) {
			return false;
		}
		return aluno.getContratoVigente().getCnabEnviado();
	}

	public boolean isVerificadoOk() {
		if (aluno == null) {
			return false;
		}
		if (aluno.getVerificadoOk() == null) {
			return false;
		}
		return aluno.getVerificadoOk();
	}

	public boolean isVerificadoOk(Long idAluno) {
		Aluno a = alunoService.findById(idAluno);
		if (a == null) {
			return false;
		}
		if (a.getVerificadoOk() == null) {
			return false;
		}
		return a.getVerificadoOk();
	}

	public StreamedContent imprimirContratoAdonai(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if (aluno != null && aluno.getId() != null) {
			nomeArquivo = aluno.getId() + "b";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "mb1.docx", montarBoletim(aluno), nomeArquivo);
			nomeArquivo += ".doc";
		} else {
			nomeArquivo = "mb1.docx";
		}
		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + nomeArquivo;

		InputStream stream = new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	public StreamedContent imprimirContratoAdonai() throws IOException {
		return imprimirContratoAdonai(aluno);
	}

	public StreamedContent imprimirAtestadoFrequencia(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if (aluno != null && aluno.getId() != null) {
			nomeArquivo = aluno.getId() + "c";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloAtestadoFrequencia2017.docx",
					montarAtestadoFrequencia(aluno), nomeArquivo);
			nomeArquivo += ".doc";
		} else {
			nomeArquivo = "modeloAtestadoFrequencia2017.docx";
		}

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + nomeArquivo;
		InputStream stream = new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	public StreamedContent imprimirAtestadoFrequencia() throws IOException {
		return imprimirAtestadoFrequencia(aluno);
	}

	public StreamedContent imprimirAtestadoMatricula(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if (aluno != null && aluno.getId() != null) {
			nomeArquivo = aluno.getId() + "d";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloAtestadoMatricula2017.docx",
					montarAtestadoMatricula(aluno), nomeArquivo);

			nomeArquivo += ".doc";
		} else {
			nomeArquivo = "modeloAtestadoMatricula2017.docx";
		}

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + nomeArquivo;
		InputStream stream = new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	public StreamedContent imprimirAtestadoMatricula() throws IOException {
		return imprimirAtestadoMatricula(aluno);
	}

	public StreamedContent imprimirNegativoDebito(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if (aluno != null && aluno.getId() != null) {
			nomeArquivo = aluno.getId() + "f";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloNegativoDebito2017.docx",
					montarAtestadoNegativoDebito(aluno), nomeArquivo);

			nomeArquivo += ".doc";
		} else {
			nomeArquivo = "modeloNegativoDebito2017.docx";
		}

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + nomeArquivo;
		InputStream stream = new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	public StreamedContent imprimirNegativoDebito() throws IOException {
		return imprimirNegativoDebito(aluno);
	}

	public StreamedContent imprimirContrato(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if (aluno != null && aluno.getId() != null) {
			nomeArquivo = aluno.getId() + "g";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloContrato2017.docx", montarContrato(aluno),
					nomeArquivo);
			nomeArquivo += ".doc";
		} else {
			nomeArquivo = "modeloContrato2017.docx";
		}

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + nomeArquivo;
		InputStream stream = new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	public StreamedContent imprimirContrato() throws IOException {
		return imprimirContrato(aluno);
	}

	public StreamedContent imprimirContrato(ContratoAluno contrato) throws IOException {
		String nomeArquivo = "";
		if (contrato != null && contrato.getId() != null) {
			nomeArquivo = contrato.getAluno().getId() + "g";
			ImpressoesUtils.imprimirInformacoesAluno("modeloContrato2017.docx", montarContrato(contrato),nomeArquivo);
			nomeArquivo += ".doc";
		} else {
			nomeArquivo = "modeloContrato2017.docx";
		}

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + nomeArquivo;
		InputStream stream = new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}
	
	public StreamedContent imprimirAtestadoVaga(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if (aluno != null && aluno.getId() != null) {
			nomeArquivo = aluno.getId() + "h";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "modeloAtestadoVaga2017.docx", montarAtestadoVaga(aluno),
					nomeArquivo);
			nomeArquivo += ".doc";
		} else {
			nomeArquivo = "modeloAtestadoVaga2017.docx";
		}

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + nomeArquivo;
		InputStream stream = new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	public StreamedContent imprimirAtestadoVaga() throws IOException {
		return imprimirAtestadoVaga(aluno);
	}

	public StreamedContent imprimirHistorico(Aluno aluno) throws IOException {
		String nomeArquivo = "";
		if (aluno != null && aluno.getId() != null) {
			nomeArquivo = aluno.getId() + "k";
			ImpressoesUtils.imprimirInformacoesAluno(aluno, "historicoEscolar2017.docx", montarHistorico(aluno),
					nomeArquivo);
			nomeArquivo += ".doc";
		} else {
			nomeArquivo = "historicoEscolar2017.docx";
		}

		String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\" + nomeArquivo;
		InputStream stream = new FileInputStream(caminho);
		return FileDownload.getContentDoc(stream, nomeArquivo);
	}

	public StreamedContent imprimirHistorico() throws IOException {
		return imprimirHistorico(aluno);
	}

	public List<Aluno> getAlunos() {

		return alunoService.findAll();
	}

	public String salvar() {
		alunoService.save(aluno, aluno.getContratoVigente());
		Util.removeAtributoSessao("aluno");
		if (getLoggedUser().getTipoMembro().equals(TipoMembro.FINANCEIRO)) {
			return "indexFinanceiro";
		}
		return "index";
	}

	public String voltar() {
		if (getLoggedUser().getTipoMembro().equals(TipoMembro.FINANCEIRO)) {
			return "indexFinanceiro";
		}
		return "index";
	}

	public String editar(Long idprof) {
		aluno = alunoService.findById(idprof);
		Util.addAtributoSessao("aluno", aluno);
		return "cadastrar";
	}

	public String editarHistorico(HistoricoAluno historicoAluno) {
		this.historicoAluno = historicoAluno;
		return "";
	}

	public String visualizar(Aluno aluno) {
		popularAlunoAvaliacao(aluno);
		Util.addAtributoSessao("alunoAvaliacaoIngles", alunoAvaliacaoIngles);
		Util.addAtributoSessao("alunoAvaliacaoArtes", alunoAvaliacaoArtes);
		Util.addAtributoSessao("alunoAvaliacaoCiencias", alunoAvaliacaoCiencias);
		Util.addAtributoSessao("alunoAvaliacaoEdFisica", alunoAvaliacaoEDFisica);
		Util.addAtributoSessao("alunoAvaliacaoFormacaoCrista", alunoAvaliacaoFormacaoCrista);
		Util.addAtributoSessao("alunoAvaliacaoGeografia", alunoAvaliacaoGeografia);
		Util.addAtributoSessao("alunoAvaliacaoHistoria", alunoAvaliacaoHistoria);
		Util.addAtributoSessao("alunoAvaliacaoMatematica", alunoAvaliacaoMatematica);
		Util.addAtributoSessao("alunoAvaliacaoPortugues", alunoAvaliacaoPortugues);

		aluno = alunoService.findById(aluno.getId());
		Util.addAtributoSessao("aluno", aluno);

		return "cadastrar";
	}

	public String editar() {
		return editar(aluno.getId());
	}

	public String rematricular() {
		return rematricular(aluno.getId());
	}

	public String rematricularAlunoAntigo() {
		return rematricularAlunoAntigo(aluno.getId());
	}

	public String enviarCnab() {
		return enviarCnab(aluno.getId());
	}

	public String removerCnabEnviado() {
		return removerCnabEnviado(aluno.getId());
	}

	private String removerCnabEnviado(Long id) {
		alunoService.removerCnabEnviado(id);
		return "ok";
	}

	private String enviarCnab(Long id) {
		alunoService.enviarCnab(id);
		return "ok";
	}

	public String verificarOk() {
		return verificarOk(aluno.getId());
	}

	public String removerVerificadoOk() {
		return removerVerificadoOk(aluno.getId());
	}

	private String removerVerificadoOk(Long id) {
		alunoService.removerVerificadoOk(id);
		return "ok";
	}

	private String verificarOk(Long id) {
		alunoService.verificadoOk(id);
		return "ok";
	}

	public String desmatricular() {
		return desmatricular(aluno.getId());
	}

	private String rematricular(Long id) {
		alunoService.rematricular(id);
		return "ok";
	}

	private String rematricularAlunoAntigo(Long id) {
		alunoService.rematricularAlunoAntigo(id);
		return "ok";
	}

	private String desmatricular(Long id) {
		alunoService.desmatricular(id);
		return "ok";
	}

	public String restaurarCancelado(Long id) {
		alunoService.rematricularCancelado(id);
		return "ok";
	}

	public String restaurarCancelado() {
		return restaurarCancelado(aluno.getId());
	}

	public String remover(Long idTurma) {
		aluno = alunoService.findById(idTurma);
		Util.addAtributoSessao("aluno", aluno);
		/*
		 * alunoService.remover(idTurma); if
		 * (getLoggedUser().getTipoMembro().equals(TipoMembro.FINANCEIRO)) {
		 * return "indexFinanceiro"; }
		 */
		return "remover";
	}

	public void removerBoleto(Long idBoleto) {
		org.escola.model.Boleto b = alunoService.findBoletoById(idBoleto);
		for (ContratoAluno contrato : aluno.getContratos()) {
			for (org.escola.model.Boleto bol : contrato.getBoletos()) {
				if (bol.getId().equals(b.getId())) {
					bol.setCancelado(true);
					bol.setValorPago((double) 0);
				}
			}

		}
		// alunoService.removerBoleto(idBoleto);
	}

	public String removerAluno() {
		for (ContratoAluno contrato : aluno.getContratosVigentes()) {
			for (org.escola.model.Boleto b : contrato.getBoletos()) {
				if (b.getCancelado() == null || !b.getCancelado().booleanValue()) {
					b.setManterAposRemovido(true);
				}
			}

		}
		alunoService.remover(aluno);
		Util.removeAtributoSessao("aluno");
		if (getLoggedUser().getTipoMembro().equals(TipoMembro.FINANCEIRO)) {
			return "indexFinanceiro";
		}
		return "index";
	}

	public String restaurar(Long idTurma) {
		alunoService.restaurar(idTurma);
		if (getLoggedUser().getTipoMembro().equals(TipoMembro.FINANCEIRO)) {
			return "indexFinanceiro";
		}
		return "index";
	}

	public String adicionarNovo() {
		Util.removeAtributoSessao("aluno");
		return "cadastrar";
	}

	public void adicionarHistorico(HistoricoAluno historico) {
		historico.setAluno(aluno);
		alunoService.saveHistorico(historico);
	}

	public void removerHistorico(long idHistorico) {
		alunoService.removerHistorico(idHistorico);
	}

	public String cadastrarNovo() {

		return "exibirAluno";
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoPortugues() {
		return alunoAvaliacaoPortugues;
	}

	public void setAlunoAvaliacaoPortugues(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoPortugues) {
		this.alunoAvaliacaoPortugues = alunoAvaliacaoPortugues;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoIngles() {
		return alunoAvaliacaoIngles;
	}

	public void setAlunoAvaliacaoIngles(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoIngles) {
		this.alunoAvaliacaoIngles = alunoAvaliacaoIngles;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoMatematica() {
		return alunoAvaliacaoMatematica;
	}

	public void setAlunoAvaliacaoMatematica(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoMatematica) {
		this.alunoAvaliacaoMatematica = alunoAvaliacaoMatematica;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoHistoria() {
		return alunoAvaliacaoHistoria;
	}

	public void setAlunoAvaliacaoHistoria(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoHistoria) {
		this.alunoAvaliacaoHistoria = alunoAvaliacaoHistoria;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoEDFisica() {
		return alunoAvaliacaoEDFisica;
	}

	public void setAlunoAvaliacaoEDFisica(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoEDFisica) {
		this.alunoAvaliacaoEDFisica = alunoAvaliacaoEDFisica;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoGeografia() {
		return alunoAvaliacaoGeografia;
	}

	public void setAlunoAvaliacaoGeografia(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoGeografia) {
		this.alunoAvaliacaoGeografia = alunoAvaliacaoGeografia;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoCiencias() {
		return alunoAvaliacaoCiencias;
	}

	public void setAlunoAvaliacaoCiencias(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoCiencias) {
		this.alunoAvaliacaoCiencias = alunoAvaliacaoCiencias;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoFormacaoCrista() {
		return alunoAvaliacaoFormacaoCrista;
	}

	public void setAlunoAvaliacaoFormacaoCrista(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoFormacaoCrista) {
		this.alunoAvaliacaoFormacaoCrista = alunoAvaliacaoFormacaoCrista;
	}

	public Map<Aluno, List<AlunoAvaliacao>> getAlunoAvaliacaoArtes() {
		return alunoAvaliacaoArtes;
	}

	public void setAlunoAvaliacaoArtes(Map<Aluno, List<AlunoAvaliacao>> alunoAvaliacaoArtes) {
		this.alunoAvaliacaoArtes = alunoAvaliacaoArtes;
	}

	public DisciplinaEnum getDisciplinaSel() {
		return disciplinaSel;
	}

	public void setDisciplinaSel(DisciplinaEnum disciplinaSel) {
		this.disciplinaSel = disciplinaSel;
	}

	public BimestreEnum getBimestreSel() {
		return bimestreSel;
	}

	public void setBimestreSel(BimestreEnum bimestreSel) {
		this.bimestreSel = bimestreSel;
	}

	public BimestreEnum getPrimeiroBimestre() {
		return BimestreEnum.PRIMEIRO_BIMESTRE;
	}

	public BimestreEnum getSegundoBimestre() {
		return BimestreEnum.SEGUNDO_BIMESTRE;
	}

	public BimestreEnum getTerceiroBimestre() {
		return BimestreEnum.TERCEIRO_BIMESTRE;
	}

	public BimestreEnum getQuartoBimestre() {
		return BimestreEnum.QUARTO_BIMESTRE;
	}

	public DisciplinaEnum getPortugues() {
		return DisciplinaEnum.PORTUGUES;
	}

	public DisciplinaEnum getMatematica() {
		return DisciplinaEnum.MATEMATICA;
	}

	public DisciplinaEnum getHistoria() {
		return DisciplinaEnum.HISTORIA;
	}

	public DisciplinaEnum getIngles() {
		return DisciplinaEnum.INGLES;
	}

	public DisciplinaEnum getEDFisica() {
		return DisciplinaEnum.EDUCACAO_FISICA;
	}

	public DisciplinaEnum getGeografia() {
		return DisciplinaEnum.GEOGRAFIA;
	}

	public DisciplinaEnum getCiencias() {
		return DisciplinaEnum.CIENCIAS;
	}

	public DisciplinaEnum getFormacaoCrista() {
		return DisciplinaEnum.FORMACAO_CRISTA;
	}

	public DisciplinaEnum getArtes() {
		return DisciplinaEnum.ARTES;
	}

	public StreamedContent downloadBoleto(org.escola.model.Boleto boleto) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(boleto.getVencimento());
			CNAB240_SICOOB cnab = new CNAB240_SICOOB(2);
			String nomeArquivo = aluno.getCodigo() + c.get(Calendar.MONTH) + ".pdf";

			Pagador pagador = new Pagador();
			pagador.setBairro(boleto.getContrato().getBairro());
			pagador.setCep(boleto.getContrato().getCep());
			pagador.setCidade(boleto.getContrato().getCidade() != null ? boleto.getContrato().getCidade() : "PALHOCA");
			pagador.setCpfCNPJ(boleto.getContrato().getCpfResponsavel());
			pagador.setEndereco(boleto.getContrato().getEndereco());
			pagador.setNome(boleto.getContrato().getNomeResponsavel() + "   (" + boleto.getContrato().getAluno().getNomeAluno()+")");
			pagador.setNossoNumero(boleto.getNossoNumero() + "");
			pagador.setUF("SC");
			List<Boleto> boletos = new ArrayList<>();
			Boleto b = new Boleto();
			b.setEmissao(boleto.getEmissao());
			b.setId(boleto.getId());
			b.setNossoNumero(String.valueOf(boleto.getNossoNumero()));
			b.setValorNominal(boleto.getValorNominal());
			b.setVencimento(boleto.getVencimento());
			boletos.add(b);
			pagador.setBoletos(boletos);

			byte[] pdf = cnab.getBoletoPDF(pagador);

			OfficePDFUtil.geraPDF(nomeArquivo, pdf);

			String temp = System.getProperty("java.io.tmpdir");
			String caminho = temp + File.separator + nomeArquivo;

			InputStream stream;
			stream = new FileInputStream(caminho);
			return FileDownload.getContentDoc(stream, nomeArquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void importarCNAB240Sicoob2() {
		CNAB240_RETORNO_SICOOB.imporCNAB240(Constante.LOCAL_ARMAZENAMENTO_REMESSA + "tefamel\\enviando\\");
	}

	public void importarCNAB240Sicoob() throws ParseException {
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
		int contador = 0;
		for (Pagador pagador : boletosImportados) {
			Boleto boletoCNAB = pagador.getBoletos().get(0);
			String numeroDocumento = boletoCNAB.getNossoNumero();
			contador += 1;
			System.out.println("contador : " + contador);
			System.out.println("Documento : " + numeroDocumento);
			if (numeroDocumento != null && !numeroDocumento.equalsIgnoreCase("") && !numeroDocumento.contains("-")) {
				try {
					numeroDocumento = numeroDocumento.trim().replace(" ", "").replace("/",
							"".replace("-", "").replace(".", ""));
					if (numeroDocumento.matches("^[0-9]*$")) {
						Long numeroDocumentoLong = Long.parseLong(numeroDocumento);
						org.escola.model.Boleto boleto = null;
						if (!extratoBancario) {
							numeroDocumentoLong -= 10000;
							boleto = financeiroEscolaService.findBoletoByID(numeroDocumentoLong);
						} else {
							String numeroDocumentoExtrato = String.valueOf(numeroDocumentoLong);
							boleto = financeiroEscolaService.findBoletoByNumeroModel(
									numeroDocumentoExtrato.substring(0, numeroDocumentoExtrato.length() - 1));
						}
						System.out.println(numeroDocumento.trim().replace(" ", ""));
						if (boleto != null) {
							if (!Verificador.getStatusEnum(boleto).equals(StatusBoletoEnum.PAGO)) {
								boleto.setValorPago(boletoCNAB.getValorPago());
								boleto.setDataPagamento(OfficeUtil.retornaData(boletoCNAB.getDataPagamento()));
								boleto.setConciliacaoPorExtrato(extratoBancario);
								financeiroEscolaService.save(boleto);
								System.out.println("YESS, BOLETO PAGO");
							}
						}
					}

				} catch (ClassCastException cce) {
					cce.printStackTrace();
				}
			}
		}

	}

	public void gerarBoletoModel() {
		alunoService.gerarBoletos();
	}

	public StreamedContent gerarBoleto() {
		try {

			CNAB240_SICOOB cnab = new CNAB240_SICOOB(2);

			String nomeArquivo = aluno.getCodigo() + aluno.getContratoVigente().getNomeResponsavel().replace(" ", "")
					+ ".pdf";

			Pagador pagador = new Pagador();
			pagador.setBairro(aluno.getContratoVigente().getBairro());
			pagador.setCep(aluno.getContratoVigente().getCep());
			pagador.setCidade(aluno.getContratoVigente().getCidade() != null ? aluno.getContratoVigente().getCidade()
					: "PALHOCA");
			pagador.setCpfCNPJ(aluno.getContratoVigente().getCpfResponsavel());
			pagador.setEndereco(aluno.getContratoVigente().getEndereco());
			pagador.setNome(aluno.getContratoVigente().getNomeResponsavel());
			pagador.setNossoNumero(aluno.getCodigo());
			pagador.setUF("SC");
			pagador.setBoletos(Formatador.getBoletosFinanceiro(getBoletosParaPagar(aluno)));

			byte[] pdf = cnab.getBoletoPDF(pagador);

			OfficePDFUtil.geraPDF(nomeArquivo, pdf);

			String temp = System.getProperty("java.io.tmpdir");
			String caminho = temp + File.separator + nomeArquivo;

			InputStream stream;
			stream = new FileInputStream(caminho);
			return FileDownload.getContentDoc(stream, nomeArquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public StreamedContent gerarBoleto(ContratoAluno contrato) {
		try {

			CNAB240_SICOOB cnab = new CNAB240_SICOOB(2);

			String nomeArquivo = contrato.getAluno().getCodigo() + contrato.getAluno().getContratoVigente().getNomeResponsavel().replace(" ", "")
					+ ".pdf";

			Pagador pagador = new Pagador();
			pagador.setBairro(contrato.getBairro());
			pagador.setCep(contrato.getCep());
			pagador.setCidade(contrato.getCidade() != null ? contrato.getCidade(): "PALHOCA");
			pagador.setCpfCNPJ(contrato.getCpfResponsavel());
			pagador.setEndereco(contrato.getEndereco());
			pagador.setNome(contrato.getNomeResponsavel() +"  ("  + contrato.getAluno().getNomeAluno() + ")");
			pagador.setNossoNumero(contrato.getNumero());
			pagador.setUF("SC");
			pagador.setBoletos(Formatador.getBoletosFinanceiro(getBoletosParaPagar(contrato)));

			byte[] pdf = cnab.getBoletoPDF(pagador);

			OfficePDFUtil.geraPDF(nomeArquivo, pdf);

			String temp = System.getProperty("java.io.tmpdir");
			String caminho = temp + File.separator + nomeArquivo;

			InputStream stream;
			stream = new FileInputStream(caminho);
			return FileDownload.getContentDoc(stream, nomeArquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	private List<Boleto> getBoletoFinanceiro(org.escola.model.Boleto boleto) {
		List<org.aaf.financeiro.model.Boleto> boletosFinanceiro = new ArrayList<>();
		org.aaf.financeiro.model.Boleto boletoFinanceiro = new org.aaf.financeiro.model.Boleto();
		boletoFinanceiro.setEmissao(boleto.getEmissao());
		boletoFinanceiro.setId(boleto.getId());
		boletoFinanceiro.setValorNominal(boleto.getValorNominal());
		boletoFinanceiro.setVencimento(boleto.getVencimento());
		boletoFinanceiro.setNossoNumero(String.valueOf(boleto.getNossoNumero()));
		boletoFinanceiro.setDataPagamento(OfficeUtil.retornaDataSomenteNumeros(boleto.getDataPagamento()));
		boletoFinanceiro.setValorPago(boleto.getValorPago());
		boletosFinanceiro.add(boletoFinanceiro);
		return boletosFinanceiro;
	}

	public StreamedContent gerarArquivoBaixa() {

		List<org.escola.model.Boleto> boletosParaBaixa = financeiroEscolaService.getBoletosParaBaixa();
		String pastaTemporaria = FileUtils.getPastaTemporariaSistema().getAbsolutePath() + System.currentTimeMillis()
				+ File.separator;
		FileUtils.criarDiretorioSeNaoExiste(pastaTemporaria);
		for (org.escola.model.Boleto boleto : boletosParaBaixa) {
			try {
				String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";
				String nomeArquivo = pastaTemporaria + "CNAB240_" + boleto.getPagador().getCodigo() + "_BAIXA"
						+ System.currentTimeMillis() + ".txt";

				Pagador pagador = new Pagador();

				pagador.setBairro(boleto.getContrato().getBairro());
				pagador.setCep(boleto.getContrato().getCep());
				pagador.setCidade(
						boleto.getContrato().getCidade() != null ? boleto.getContrato().getCidade() : "PALHOCA");
				pagador.setCpfCNPJ(boleto.getContrato().getCpfResponsavel());
				pagador.setEndereco(boleto.getContrato().getEndereco());
				pagador.setNome(boleto.getContrato().getNomeResponsavel());
				pagador.setNossoNumero(boleto.getPagador().getCodigo());
				pagador.setUF("SC");

				pagador.setBoletos(getBoletoFinanceiro(boleto));

				CNAB240_REMESSA_SICOOB remessaCNAB240 = new CNAB240_REMESSA_SICOOB(2);
				byte[] arquivo = remessaCNAB240.geraBaixa(pagador, sequencialArquivo);

				FileUtils.escreveBinarioEmDiretorio(arquivo, new File(nomeArquivo));

				try {
					configuracaoService.incrementaSequencialArquivoCNAB();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		String nomeDoZip = pastaTemporaria + "_Baixa_.zip";

		try {
			CompactadorZip.compactarParaZip(nomeDoZip, pastaTemporaria);
			byte[] arquivoZip = FileUtils.getBytesFromFile(new File(nomeDoZip));
			InputStream stream = new ByteArrayInputStream(arquivoZip);
			return FileDownload.getContentDoc(stream, "baixa.zip");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public StreamedContent gerarCNB240() {
		try {
			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";
			String nomeArquivo = "CNAB240_" + aluno.getCodigo() + ".txt";

			InputStream stream = FileUtils.gerarCNB240(sequencialArquivo, nomeArquivo, aluno,
					aluno.getContratoVigente());
			configuracaoService.incrementaSequencialArquivoCNAB();

			return FileDownload.getContentDoc(stream, nomeArquivo);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public StreamedContent gerarCNB240(ContratoAluno contrato) {
		try {
			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";
			String nomeArquivo = "CNAB240_" + aluno.getCodigo() + ".txt";

			InputStream stream = FileUtils.gerarCNB240(sequencialArquivo, nomeArquivo, contrato);
			configuracaoService.incrementaSequencialArquivoCNAB();

			return FileDownload.getContentDoc(stream, nomeArquivo);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public Double getDesconto(org.escola.model.Boleto boleto) {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 1);
		if (boleto.getVencimento().compareTo(tomorrow.getTime()) == 1) {
			return 20d;
		} else {
			return 0d;
		}

	}

	public String getDescontoString(org.escola.model.Boleto boleto) {
		return Util.formatarDouble2Decimais(getDesconto(boleto));
	}

	public Double getJurosMulta(org.escola.model.Boleto boleto) {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 1);
		double multa = boleto.getValorNominal() * 0.02;
		long diasVencimento = Util.diferencaEntreDatas(tomorrow.getTime(), boleto.getVencimento());
		if (diasVencimento > 0) {
			double juros = (diasVencimento / 2); // juros de 50 centavos
			return multa + juros;

		} else {
			return 0D;
		}

	}

	public String getJurosMultaString(org.escola.model.Boleto boleto) {
		return Util.formatarDouble2Decimais(getJurosMulta(boleto));
	}

	public Double getValorFinal(org.escola.model.Boleto boleto) {

		return Verificador.getValorFinal(boleto);
	}

	public String getValorFinalString(org.escola.model.Boleto boleto) {

		return Util.formatarDouble2Decimais(getValorFinal(boleto));
	}

	public String getStatus(org.escola.model.Boleto boleto) {
		return Verificador.getStatus(boleto);
	}

	private boolean cadastroCompleto(Aluno aluno) {
		// TODO Auto-generated method stub
		return true;
	}

	public String removerContrato(ContratoAluno contrat) {

		for (org.escola.model.Boleto b : contrat.getBoletos()) {
			if (b.getCancelado() == null || !b.getCancelado().booleanValue()) {
				b.setManterAposRemovido(true);
			}
		}

		alunoService.removerContrato(contrat);
		Aluno al = alunoService.findById(contrat.getAluno().getId());
		Util.addAtributoSessao("aluno", al);
		this.aluno = al;
		// Util.removeAtributoSessao("aluno");
		// Util.removeAtributoSessao("contrato");
		//alunoService.removerContrato(contrat);

		return "ok";
	}

	public void adicionarNovoContrato(Aluno aluno){
		ContratoAluno contAntigo =  aluno.getUltimoContrato();
		ContratoAluno novoContrato = null;
		if(contAntigo != null){
			novoContrato = aluno.getUltimoContrato().clone();
		}else{
			novoContrato = new ContratoAluno();
		}
		
		novoContrato.setAno((short) configuracao.getAnoRematricula());
		novoContrato.setCnabEnviado(false);
		novoContrato.setCancelado(false);
		novoContrato.setDataCancelamento(null);
		novoContrato.setBoletos(null);
		novoContrato.setContratoTerminado(false);
		novoContrato.setDataCriacaoContrato(new Date());
		novoContrato.setEnviadoSPC(false);
		novoContrato.setEnviadoParaCobrancaCDL(false);
		
		String ano = String.valueOf(novoContrato.getAno());
		String finalANo = ano.substring(ano.length()-2 , ano.length());
		String numeroUltimoContrato = "01";
		if(aluno.getContratos() != null){
			for(ContratoAluno contratt : aluno.getContratos()){
				if(contratt.getNumero() != null && !contratt.getNumero().equalsIgnoreCase("")){
					if(contratt.getAno() == novoContrato.getAno()){
						String numeroContratt = contratt.getNumero();
						numeroContratt = numeroContratt.substring(numeroContratt.length()-2 , numeroContratt.length());
						if(Integer.parseInt(numeroContratt) > Integer.parseInt(numeroUltimoContrato) ){
							numeroUltimoContrato = numeroContratt;
						}
					}
				}
				int numeroNovo = Integer.parseInt(numeroUltimoContrato);
				numeroNovo++; 
				numeroUltimoContrato = String.valueOf(numeroNovo);
			}
		}
	
		
		String numero = finalANo + aluno.getCodigo()+"0"+numeroUltimoContrato;
		novoContrato.setNumero(numero);
		
		aluno = alunoService.adicionarContrato(aluno,novoContrato);
		this.aluno = aluno;
	}

	public void salvar(ContratoAluno contrato) {
		contrato = alunoService.saveContrato(contrato);
		
		Util.addAtributoSessao("contrato", contrato);
		Util.addAtributoSessao("aluno", contrato.getAluno());
		this.aluno = contrato.getAluno();

	}
	
	public boolean podeImprimir(ContratoAluno contrato) {
		if (contrato.getAluno() != null) {
			if(contrato.getAluno().getId() != null){
					if(contrato.getAluno().getId() > 0 ){
						return true;
					}
			}
		}
		
		return false;
	}

	public boolean podeGerarBoleto(ContratoAluno contrato) {
		if (contrato.getBoletos() == null || contrato.getBoletos().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public List<org.escola.model.Boleto> getBoletosParaPagar(Aluno aluno) {
		List<org.escola.model.Boleto> boletosParaPagar = new ArrayList<>();
		if (aluno.getContratosVigentes() != null) {
			for (ContratoAluno contrato : aluno.getContratosVigentes()) {
				if (contrato.getBoletos() != null) {
					for (org.escola.model.Boleto b : contrato.getBoletos()) {
						if ((!Verificador.getStatusEnum(b).equals(StatusBoletoEnum.PAGO))
								&& !(Verificador.getStatusEnum(b).equals(StatusBoletoEnum.CANCELADO))) {
							boletosParaPagar.add(b);
						}
					}
				}
			}
		}

		return boletosParaPagar;
	}

	
	public List<org.escola.model.Boleto> getBoletosParaPagar(ContratoAluno contrato) {
		List<org.escola.model.Boleto> boletosParaPagar = new ArrayList<>();
		if (contrato.getBoletos() != null) {
			for (org.escola.model.Boleto b : contrato.getBoletos()) {
				if ((!Verificador.getStatusEnum(b).equals(StatusBoletoEnum.PAGO))
						&& !(Verificador.getStatusEnum(b).equals(StatusBoletoEnum.CANCELADO))) {
					boletosParaPagar.add(b);
				}
			}
		}
		
		return Util.inverterArray(boletosParaPagar);
	}

	public List<org.escola.model.Boleto> getBoletosParaPagar(List<org.escola.model.Boleto> boletos) {
		List<org.escola.model.Boleto> boletosParaPagar = new ArrayList<>();

		if (aluno.getContratosVigentes() != null) {
			for (ContratoAluno contrato : aluno.getContratosVigentes()) {
				if (contrato.getBoletos() != null) {
					for (org.escola.model.Boleto b : boletos) {
						if ((!Verificador.getStatusEnum(b).equals(StatusBoletoEnum.PAGO))
								&& !(Verificador.getStatusEnum(b).equals(StatusBoletoEnum.CANCELADO))) {
							boletosParaPagar.add(b);
						}
					}

				}
			}
		}

		return boletosParaPagar;
	}

	public void alterarBoleto(org.escola.model.Boleto boleto) {
		financeiroEscolaService.alterarBoletoManualmente(boleto);
	}

	private boolean todosDadosContratoValidos(Aluno aluno, ContratoAluno contrato) {
		boolean todoDadosContratoValidos = true;

		if (aluno.getNomeAluno() == null || aluno.getNomeAluno().equalsIgnoreCase("")) {
			todoDadosContratoValidos = false;
		} else if (aluno.getCodigo() == null || aluno.getCodigo().equalsIgnoreCase("")) {
			todoDadosContratoValidos = false;
		} else if (contrato.getNomeResponsavel() == null || contrato.getNomeResponsavel().equalsIgnoreCase("")) {
			todoDadosContratoValidos = false;
		} else if (contrato.getCpfResponsavel() == null || contrato.getCpfResponsavel().equalsIgnoreCase("")
				|| contrato.getCpfResponsavel().length() < 11 || contrato.getCpfResponsavel().length() > 11) {
			todoDadosContratoValidos = false;
		} else if (contrato.getRgResponsavel() == null || contrato.getRgResponsavel().equalsIgnoreCase("")) {
			todoDadosContratoValidos = false;
		} else if (contrato.getEndereco() == null || contrato.getEndereco().equalsIgnoreCase("")) {
			todoDadosContratoValidos = false;
		} else if (contrato.getBairro() == null || contrato.getBairro().equalsIgnoreCase("")) {
			todoDadosContratoValidos = false;
		} else if (contrato.getCep() == null || contrato.getCep().equalsIgnoreCase("")) {
			todoDadosContratoValidos = false;
		} else if (contrato.getValorMensal() == 0 || contrato.getValorMensal() > 2000
				|| contrato.getValorMensal() < 150) {
			todoDadosContratoValidos = false;
		} else if (aluno.getContratoVigente() != null && aluno.getContratoVigente().getBoletos() != null
				&& aluno.getContratoVigente().getBoletos().size() < 1
				&& aluno.getContratoVigente().getBoletos().size() > 12) {
			todoDadosContratoValidos = false;
		}

		return todoDadosContratoValidos;
	}

	public void adicionarIrmao() {
		if (!isIrmao1()) {
			setIrmao1(true);
			aluno.setIrmao1(new Aluno());
		} else if (!isIrmao2()) {
			setIrmao2(true);
			aluno.setIrmao2(new Aluno());
		} else if (!isIrmao3()) {
			setIrmao3(true);
			aluno.setIrmao3(new Aluno());
		} else if (!isIrmao4()) {
			setIrmao4(true);
			aluno.setIrmao4(new Aluno());
		}
	}

	public void removerIrmao() {
		if (isIrmao4()) {
			setIrmao4(false);
		} else if (isIrmao3()) {
			setIrmao3(false);
		} else if (isIrmao2()) {
			setIrmao2(false);
		} else if (isIrmao1()) {
			setIrmao1(false);
		}
	}

	/*public boolean cadastroOk(Long idAluno) {
		Aluno a = alunoService.findById(idAluno);
		if (a.getContratoVigente() != null) {
			if (!Verificador.isCPF(a.getContratoVigente().getCpfResponsavel())) {
				return false;
			} else if (!todosDadosContratoValidos(a, a.getContratoVigente())) {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}*/
	
	public boolean cadastroOk(Aluno a) {
		/*if (a.getContratoVigente() != null) {
			if (!Verificador.isCPF(a.getContratoVigente().getCpfResponsavel())) {
				return false;
			} else if (!todosDadosContratoValidos(a, a.getContratoVigente())) {
				return false;
			}
		} else {
			return false;
		}*/

		return true;
	}

	public boolean cnabEnviado(Long idAluno) {
		Aluno a = alunoService.findById(idAluno);
		if (a != null) {
			if (a.getContratoVigente() != null
					&& (a.getContratoVigente().getCnabEnviado() != null && a.getContratoVigente().getCnabEnviado())) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
	
	public boolean cnabEnviado(Aluno a) {
//		if (a != null) {
//			if (a.getContratoVigente() != null
//					&& (a.getContratoVigente().getCnabEnviado() != null && a.getContratoVigente().getCnabEnviado())) {
//				return true;
//			} else {
//				return false;
//			}
//		}
		return true;
	}

	public String cancelar(ContratoAluno contrato) {
		Util.addAtributoSessao("contrato", contrato);
		Util.addAtributoSessao("aluno", aluno);
		return "remover";
	}

	public void salvarTodos() {
		alunoService.salvarTodos();
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public boolean isIrmao1() {
		return irmao1;
	}

	public void setIrmao1(boolean irmao1) {
		this.irmao1 = irmao1;
	}

	public boolean isIrmao2() {
		return irmao2;
	}

	public void setIrmao2(boolean irmao2) {
		this.irmao2 = irmao2;
	}

	public boolean isIrmao3() {
		return irmao3;
	}

	public void setIrmao3(boolean irmao3) {
		this.irmao3 = irmao3;
	}

	public boolean isIrmao4() {
		return irmao4;
	}

	public void setIrmao4(boolean irmao4) {
		this.irmao4 = irmao4;
	}

	public String getTituloContrato(ContratoAluno contrato) {
		if(contrato != null){
			String cancelado = "";
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.DATE_FIELD, new Locale("pt", "BR"));
			if (contrato.getCancelado() != null && contrato.getCancelado()) {
				cancelado = "            Cancelado";
				if (contrato.getDataCancelamento() != null) {
					String dataExtenso = formatador.format(contrato.getDataCancelamento());
					cancelado += " " + dataExtenso;
				}
			} else {
				if(contrato.getDataCriacaoContrato() != null){
					cancelado = "        Ativo " + formatador.format(contrato.getDataCriacaoContrato());
				}else{
					cancelado = "        Ativo ";
				}
			}
			return " -  " + contrato.getNumero() + " -  " + cancelado;
		}
		return "";
	}

	public void gerarBoletos(ContratoAluno contrato) {
		ContratoAluno cont = alunoService.criarBoletos(contrato.getAluno(), contrato.getAno(),
				contrato.getNumeroParcelas(), contrato);
		contrato = cont;
		Util.addAtributoSessao("contrato", contrato);
		Util.addAtributoSessao("aluno", contrato.getAluno());
		this.aluno = contrato.getAluno();
	}

	public Member getLoggedUser() {
		try {

			Member user = null;
			if (SecurityUtils.getSubject().getPrincipal() != null) {
				System.out.println("CONSTRUIU O USUARIO LOGADO !!");
				user = (Member) SecurityUtils.getSubject().getPrincipal();
			}

			return user;

		} catch (Exception ex) {
			// Logger.getLogger(MemberController.class.getSimpleName()).log(Level.WARNING,
			// null, ex);
			ex.printStackTrace();
		}

		return null;
	}

}

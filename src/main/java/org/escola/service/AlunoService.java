package org.escola.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.escola.enums.BimestreEnum;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.model.Aluno;
import org.escola.model.AlunoAvaliacao;
import org.escola.model.AlunoTurma;
import org.escola.model.Boleto;
import org.escola.model.ContratoAluno;
import org.escola.model.Evento;
import org.escola.model.HistoricoAluno;
import org.escola.model.Professor;
import org.escola.model.Turma;
import org.escola.util.Service;
import org.escola.util.UtilFinalizarAnoLetivo;
import org.escola.util.Verificador;

@Stateless
public class AlunoService extends Service {

	@Inject
	private Logger log;

	@Inject
	private EventoService eventoService;

	@Inject
	private UtilFinalizarAnoLetivo finalizarAnoLetivo;

	@Inject
	private ConfiguracaoService configuracaoService;

	@Inject
	private AulaService aulaService;
	
	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public Aluno findById(EntityManager em, Long id) {
		Aluno al = em.find(Aluno.class, id);
		if (al.getIrmao1() != null) {
			al.getIrmao1().getAnoLetivo();
		}

		if (al.getIrmao2() != null) {
			al.getIrmao2().getAnoLetivo();
		}

		if (al.getIrmao3() != null) {
			al.getIrmao3().getAnoLetivo();
		}

		if (al.getIrmao4() != null) {
			al.getIrmao4().getAnoLetivo();
		}
		/* al.getBoletos().size(); */

		return al;
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> findAlunoDoAnoLetivoComLzyContrato() {
		List<Aluno> alunos = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  Aluno al ");
		sql.append("where 1=1 ");
		sql.append(" and al.removido = false ");
		sql.append(" and al.anoLetivo = ");
		sql.append(configuracaoService.getConfiguracao().getAnoLetivo());
		Query query = em.createQuery(sql.toString());

		alunos = query.getResultList();
		List<Aluno> alunos2 = new ArrayList<>();
		for(Aluno al :alunos){
			if(al.getContratos() != null){
				for(ContratoAluno c : al.getContratos()){
					c.getId();
					c.getBoletos().size();
				}
			}
			alunos2.add(al);
		}
		return alunos2;
	}
	
	@SuppressWarnings("unchecked")
	public List<Aluno> findAlunoPrioritariosNFSeComLzyContrato() {
		List<Aluno> alunos = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  Aluno al ");
		sql.append("where 1=1 ");
		sql.append(" and al.removido = false ");
		sql.append(" and al.gerarNFSe = true ");
		sql.append(" and al.anoLetivo = ");
		sql.append(configuracaoService.getConfiguracao().getAnoLetivo());
		Query query = em.createQuery(sql.toString());

		alunos = query.getResultList();
		List<Aluno> alunos2 = new ArrayList<>();
		for(Aluno al :alunos){
			if(al.getContratos() != null){
				for(ContratoAluno c : al.getContratos()){
					c.getId();
					c.getBoletos().size();
				}
			}
			alunos2.add(al);
		}
		return alunos2;
	}
	
	public Boleto findBoletoById(Long id) {
		Boleto boleto = em.find(Boleto.class, id);
		return boleto;
	}

	public ContratoAluno findContratoById(Long id) {
		ContratoAluno contrato = em.find(ContratoAluno.class, id);
		contrato.getAluno().getAlergico();
		if (contrato.getAluno().getContratos() != null) {
			contrato.getAluno().getContratos().size();
			for (ContratoAluno contratos : contrato.getAluno().getContratos()) {
				contratos.getBoletos().size();
			}
		}

		if (contrato.getBoletos() != null) {
			contrato.getBoletos().size();
		}

		return contrato;
	}

	public Aluno findById(Long id) {
		Aluno al = em.find(Aluno.class, id);
		if (al != null) {
			if (al.getIrmao1() != null) {
				al.getIrmao1().getAnoLetivo();
			}

			if (al.getIrmao2() != null) {
				al.getIrmao2().getAnoLetivo();
			}

			if (al.getIrmao3() != null) {
				al.getIrmao3().getAnoLetivo();
			}

			if (al.getIrmao4() != null) {
				al.getIrmao4().getAnoLetivo();
			}
			/* al.getBoletos().size(); */

			if (al.getContratos() != null) {
				al.getContratos().size();
				for (ContratoAluno contrato : al.getContratos()) {
					contrato.getBoletos().size();
				}
			}
			if (al.getIrmao1() != null) {
				al.getIrmao1().getContratos().size();
				for (ContratoAluno contrato : al.getIrmao1().getContratos()) {
					contrato.getBoletos().size();
				}
			}
			if (al.getIrmao2() != null) {
				al.getIrmao2().getContratos().size();
				for (ContratoAluno contrato : al.getIrmao2().getContratos()) {
					contrato.getBoletos().size();
				}
			}
			if (al.getIrmao3() != null) {
				al.getIrmao3().getContratos().size();
				for (ContratoAluno contrato : al.getIrmao3().getContratos()) {
					contrato.getBoletos().size();
				}
			}
			if (al.getIrmao4() != null) {
				al.getIrmao4().getContratos().size();
				for (ContratoAluno contrato : al.getIrmao4().getContratos()) {
					contrato.getBoletos().size();
				}
			}

		}

		return al;
	}

	public HistoricoAluno findHistoricoById(Long id) {
		return em.find(HistoricoAluno.class, id);
	}

	public List<Aluno> findAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aluno> criteria = cb.createQuery(Aluno.class);
			Root<Aluno> member = criteria.from(Aluno.class);
			// Swap criteria statements if you would like to try out type-safe
			// criteria queries, a new
			// feature in JPA 2.0
			// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
			criteria.select(member).orderBy(cb.asc(member.get("nomeAluno")));
			return em.createQuery(criteria).getResultList();

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<Aluno> findAll(Map<String, Object> filtros) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aluno> criteria = cb.createQuery(Aluno.class);
			Root<Aluno> member = criteria.from(Aluno.class);
			criteria.select(member).orderBy(cb.asc(member.get("nomeAluno")));

			final List<Predicate> predicates = new ArrayList<Predicate>();
			for (Map.Entry<String, Object> entry : filtros.entrySet()) {

				Predicate pred = cb.and();
				if (entry.getValue() instanceof String) {
					pred = cb.and(pred, cb.like(member.<String> get(entry.getKey()), "%" + entry.getValue() + "%"));
				} else {
					pred = cb.equal(member.get(entry.getKey()), entry.getValue());
				}
				predicates.add(pred);
				// cq.where(pred);
			}

			List<Aluno> als = em.createQuery(criteria).getResultList();
			List<Aluno> aux = new ArrayList<>();
			for (Aluno al : als) {
				if (al.getContratos() != null) {
					al.getContratos().size();
				}
				aux.add(al);
			}
			return aux;

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<Aluno> findAll(Serie serie) {
		List<Aluno> alunos = findAll(serie, null);
		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("aula.visible", true);
		for (Aluno aluno : alunos) {
			filtros.put("aluno", aluno.getId());
			filtros.put("aula.assistiu", true);
			aluno.setDataUltimaAulaAssistida(aulaService.getDataUltimaAulaAssistida(filtros));
			
			filtros.remove("aula.assistiu");
			double totalAulas = aulaService.countAlunoAula(filtros);
			filtros.put("aula.assistiu", true);
			double totalAulaAssistidas = aulaService.countAlunoAula(filtros);
			
			aluno.setPercentualAulasAssistidas((long) (totalAulaAssistidas/totalAulas*100));
		}
		Collections.sort(alunos);
		return alunos;
	}

	public List<Aluno> findAll(Serie serie, PerioddoEnum periodo) {
		List<Aluno> alunos = new ArrayList<>();
		alunos.addAll(find(serie, periodo));
		if (periodo != null && periodo != PerioddoEnum.INTEGRAL) {
			alunos.addAll(find(serie, PerioddoEnum.INTEGRAL));
		} else if (periodo != null && periodo.equals(PerioddoEnum.INTEGRAL)) {
			alunos.addAll(find(serie, PerioddoEnum.MANHA));
			alunos.addAll(find(serie, PerioddoEnum.TARDE));
		}
		return alunos;
	}

	public List<Aluno> find(Serie serie, PerioddoEnum periodo) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aluno> criteria = cb.createQuery(Aluno.class);
			Root<Aluno> member = criteria.from(Aluno.class);

			Predicate whereSerie = null;
			Predicate wherePeriodo = null;
			Predicate whereRemovido = cb.equal(member.get("removido"), false);
			;
			Predicate whereAnoLetivo = cb.equal(member.get("anoLetivo"),
					configuracaoService.getConfiguracao().getAnoLetivo());

			StringBuilder sb = new StringBuilder();
			if (serie != null) {
				sb.append("A");
				whereSerie = cb.equal(member.get("serie"), serie);
			}

			if (periodo != null) {
				sb.append("B");
				wherePeriodo = cb.equal(member.get("periodo"), periodo);
			}

			switch (sb.toString()) {

			case "A":
				criteria.select(member).where(whereSerie, whereRemovido, whereAnoLetivo);
				break;

			case "B":
				criteria.select(member).where(wherePeriodo, whereRemovido, whereAnoLetivo);
				break;

			case "AB":
				criteria.select(member).where(whereSerie, wherePeriodo, whereRemovido, whereAnoLetivo);
				break;
			default:
				break;
			}

			criteria.select(member).orderBy(cb.asc(member.get("nomeAluno")));
			return em.createQuery(criteria).getResultList();

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> findAlunoTurmaBytTurma(long idTurma) {
		List<Aluno> alunos = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pt from  AlunoTurma pt ");
		sql.append("where pt.turma.id =   ");
		sql.append(idTurma);
		sql.append(" and pt.aluno.removido = ");
		sql.append(false);
		sql.append(" and pt.anoLetivo = ");
		sql.append(configuracaoService.getConfiguracao2().getAnoLetivo());

		Query query = em.createQuery(sql.toString());

		try {
			List<AlunoTurma> alunosTurmas = query.getResultList();
			for (AlunoTurma profT : alunosTurmas) {
				Aluno pro = profT.getAluno();
				alunos.add(pro);
			}

		} catch (NoResultException noResultException) {

		} catch (Exception e) {
			e.printStackTrace();
		}

		return alunos;

	}

	public List<Aluno> findAlunoTurmaBytTurma(List<Turma> turmas) {
		List<Aluno> alunos = new ArrayList<>();

		for (Turma turma : turmas) {
			alunos.addAll(findAlunoTurmaBytTurma(turma.getId()));
		}
		return alunos;

	}

	public Aluno save(Aluno aluno, ContratoAluno contrato) {
		return saveAluno(aluno, true);
	}

	public String removeCaracteresEspeciais(String texto) {
		texto = texto.replaceAll("[^aA-zZ-Z0-9 ]", "");
		return texto;
	}

	public Aluno saveAluno(Aluno aluno, boolean saveBrother) {
		Aluno user = null;
		ContratoAluno contratoPersistence;
		try {

			log.info("Registering " + aluno.getNomeAluno());

			if (aluno.getId() != null && aluno.getId() != 0L) {
				user = findById(aluno.getId());
			} else {
				user = new Aluno();
				user.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo());
				user.setRemovido(false);
			}

			user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());
			user.setNomeAluno(aluno.getNomeAluno().toUpperCase());
			user.setPeriodo(aluno.getPeriodo());
			user.setSerie(aluno.getSerie());

			user.setDataCancelamento(aluno.getDataCancelamento());
			user.setPeriodoProximoAno(aluno.getPeriodoProximoAno());
			user.setNacionalidade(aluno.getNacionalidade());
			user.setDataNascimento(aluno.getDataNascimento());
			user.setDataMatricula(aluno.getDataMatricula());
			user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());

			if (aluno.getPeriodoProximoAno() == null) {
				user.setPeriodoProximoAno(aluno.getPeriodo());
			}

			user.setFaltas1Bimestre(aluno.getFaltas1Bimestre());
			user.setFaltas2Bimestre(aluno.getFaltas2Bimestre());
			user.setFaltas3Bimestre(aluno.getFaltas3Bimestre());
			user.setFaltas4Bimestre(aluno.getFaltas4Bimestre());
			if (aluno.getRemovido() == null) {
				user.setRemovido(false);
			} else {
				user.setRemovido(aluno.getRemovido());
			}
			user.setCodigo(aluno.getCodigo());
			user.setSexo(aluno.getSexo());
			user.setNomeAvoHPaternoMae(aluno.getNomeAvoHPaternoMae());
			user.setCpfMae(aluno.getCpfMae());
			user.setDataMatricula(aluno.getDataMatricula());
			user.setEmailMae(aluno.getEmailMae());
			user.setEmailPai(aluno.getEmailPai());
			user.setEmpresaTrabalhaMae(aluno.getEmpresaTrabalhaMae());
			user.setEmpresaTrabalhaPai(aluno.getEmpresaTrabalhaPai());
			user.setLogin(aluno.getLogin());
			user.setNaturalidadeMae(aluno.getNaturalidadeMae());
			user.setNaturalidadePai(aluno.getNaturalidadePai());
			user.setNomeAvoHPaternoMae(aluno.getNomeAvoHPaternoMae());
			user.setNomeAvoHPaternoPai(aluno.getNomeAvoHPaternoPai());
			user.setNomeAvoPaternoMae(aluno.getNomeAvoPaternoMae());
			user.setNomeAvoPaternoPai(aluno.getNomeAvoPaternoPai());
			user.setNomeMaeAluno(aluno.getNomeMaeAluno());
			user.setNomePaiAluno(aluno.getNomePaiAluno());
			user.setTelefoneCelularPai(aluno.getTelefoneCelularPai());
			user.setTelefoneEmpresaTrabalhaPai(aluno.getTelefoneEmpresaTrabalhaPai());
			user.setTelefoneResidencialPai(aluno.getTelefoneResidencialPai());
			user.setTelefoneCelularMae(aluno.getTelefoneCelularMae());
			user.setTelefoneEmpresaTrabalhaMae(aluno.getTelefoneEmpresaTrabalhaMae());
			user.setTelefoneResidencialMae(aluno.getTelefoneResidencialMae());
			user.setEmpresaTrabalhaMae(aluno.getTelefoneEmpresaTrabalhaMae());
			user.setEmpresaTrabalhaPai(aluno.getEmpresaTrabalhaPai());

			user.setTelefoneResidencialPai(aluno.getTelefoneResidencialPai());
			user.setRgMae(aluno.getRgMae());
			user.setRgPai(aluno.getRgPai());
			user.setSenha(aluno.getSenha());
			user.setTelefone(aluno.getTelefone());
			user.setEmergenciaLigarPara(aluno.getEmergenciaLigarPara());

			user.setDoenca(aluno.getDoenca());
			user.setAlergico(aluno.getAlergico());
			user.setGerarNFSe(aluno.getGerarNFSe());
			user.setNomeAlergias(aluno.getNomeAlergias());
			user.setNomeDoencas(aluno.getNomeDoencas());
			user.setObservacaoProfessores(aluno.getObservacaoProfessores());
			user.setObservacaoSecretaria(aluno.getObservacaoSecretaria());

			user.setContatoEmail1(aluno.getContatoEmail1());
			user.setContatoEmail2(aluno.getContatoEmail2());
			user.setContatoNome1(aluno.getContatoNome1());
			user.setContatoNome2(aluno.getContatoNome2());
			user.setContatoNome3(aluno.getContatoNome3());
			user.setContatoNome4(aluno.getContatoNome4());
			user.setContatoNome5(aluno.getContatoNome5());

			user.setContatoTelefone1(aluno.getContatoTelefone1());
			user.setContatoTelefone2(aluno.getContatoTelefone2());
			user.setContatoTelefone3(aluno.getContatoTelefone3());
			user.setContatoTelefone4(aluno.getContatoTelefone4());
			user.setContatoTelefone5(aluno.getContatoTelefone5());

			user.setAutorizadoASairCom1(aluno.getAutorizadoASairCom1());
			user.setAutorizadoASairCom2(aluno.getAutorizadoASairCom2());
			user.setAutorizadoASairCom3(aluno.getAutorizadoASairCom3());
			user.setAutorizadoASairCom4(aluno.getAutorizadoASairCom4());
			user.setAutorizadoASairCom5(aluno.getAutorizadoASairCom5());
			user.setAutorizadoASairCom6(aluno.getAutorizadoASairCom6());
			user.setAutorizadoASairCom7(aluno.getAutorizadoASairCom7());

			user.setContatoEmail1(aluno.getContatoEmail1());
			user.setContatoEmail2(aluno.getContatoEmail2());

			user.setCpf(aluno.getCpf());
			user.setRg(aluno.getRg());

			user.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo());

			em.persist(user);

			if (user.getDataNascimento() != null) {
				Evento aniversario = eventoService.findByCodigo(user.getCodigo());

				if (aniversario == null) {
					aniversario = new Evento();
				}

				aniversario.setDataFim(finalizarAnoLetivo.mudarAno(user.getDataNascimento(),
						configuracaoService.getConfiguracao().getAnoLetivo()));
				aniversario.setDataInicio(finalizarAnoLetivo.mudarAno(user.getDataNascimento(),
						configuracaoService.getConfiguracao().getAnoLetivo()));
				aniversario.setCodigo(user.getCodigo());
				aniversario.setDescricao(" Aniversário do(a) aluno(a) " + user.getNomeAluno());
				aniversario.setNome(
						" Aniversário do(a) aluno(a) " + user.getNomeAluno() + " " + aluno.getSerie().getName());
				eventoService.save(aniversario);

			}

		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
			// builder = createViolationResponse(ce.getConstraintViolations());
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("email", "Email taken");

		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());

			e.printStackTrace();
		}

		try {
			if (saveBrother) {
				if (aluno.getContratos() != null)
					for (ContratoAluno cont : aluno.getContratos()) {
						if (cont.getCancelado() != null && !cont.getCancelado()) {
							em.merge(cont);
						}
					}
				salvarIrmaos(user, aluno);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return user;
	}

	private void popularContrato(ContratoAluno contrato, ContratoAluno contratoPersistence) {
		if (contrato.getEndereco() != null) {
			contratoPersistence.setEndereco(removeCaracteresEspeciais(contrato.getEndereco()));
		}
		if (contrato.getBairro() != null) {
			contratoPersistence.setBairro(removeCaracteresEspeciais(contrato.getBairro().replace("ç", "c")));

		}
		contratoPersistence.setCep(contrato.getCep());
		if (contrato.getCidade() != null) {
			contratoPersistence.setCidade(removeCaracteresEspeciais(contrato.getCidade().replace("ç", "c")));
		}
		contratoPersistence.setValorMensal(contrato.getValorMensal());

		contratoPersistence.setAnuidade(contrato.getAnuidade() != null ? contrato.getAnuidade() : 0);
		if (contrato.getBairro() != null) {
			contratoPersistence.setBairro(removeCaracteresEspeciais(contrato.getBairro().replace("ç", "c")));
		}

		contratoPersistence.setCep(contrato.getCep());
		if (contrato.getCidade() != null) {
			contratoPersistence.setCidade(removeCaracteresEspeciais(contrato.getCidade().replace("ç", "c")));
		}
		// contratoPersistence.setCpfPai(contrato.getCpfPai());
		if (contrato.getCpfResponsavel() != null) {
			contratoPersistence.setCpfResponsavel(contrato.getCpfResponsavel().replace(".", "").replace("-", ""));
		}
		contratoPersistence.setRgResponsavel(contrato.getRgResponsavel());
		if (contrato.getNomeResponsavel() != null) {
			contratoPersistence
					.setNomeResponsavel(removeCaracteresEspeciais(contrato.getNomeResponsavel().toUpperCase()));
		}

		if (contrato.getNomePaiResponsavel() != null) {
			contratoPersistence
					.setNomePaiResponsavel(removeCaracteresEspeciais(contrato.getNomePaiResponsavel().toUpperCase()));
		}

		if (contrato.getNomeMaeResponsavel() != null) {
			contratoPersistence
					.setNomeMaeResponsavel(removeCaracteresEspeciais(contrato.getNomeMaeResponsavel().toUpperCase()));
		}

		contratoPersistence.setNumeroParcelas(contrato.getNumeroParcelas());

		contratoPersistence.setValorMensal(contrato.getValorMensal());
		contratoPersistence.setDiaVencimento(contrato.getDiaVencimento());
		contratoPersistence.setVencimentoUltimoDia(contrato.getVencimentoUltimoDia());
		if (contratoPersistence.getCidade() != null && contratoPersistence.getCidade().equalsIgnoreCase("Palhoa")) {
			contratoPersistence.setCidade("Palhoca");
		}
		if (contratoPersistence.getBairro() != null && contratoPersistence.getBairro().equalsIgnoreCase("Palhoa")) {
			contratoPersistence.setBairro("Palhoca");
		}

		contratoPersistence.setCnabEnviado(contrato.getCnabEnviado());

	}

	private void salvarIrmaos(Aluno aluno, Aluno unMerge) {
		Aluno irmao1 = unMerge.getIrmao1();
		boolean tem1Irmao = irmao1 != null ? true : false;
		Aluno irmao2 = unMerge.getIrmao2();
		boolean tem2Irmao = irmao2 != null ? true : false;
		Aluno irmao3 = unMerge.getIrmao3();
		boolean tem3Irmao = irmao3 != null ? true : false;
		Aluno irmao4 = unMerge.getIrmao4();
		boolean tem4Irmao = irmao4 != null ? true : false;

		if (tem1Irmao) {
			clone(aluno, irmao1);
			irmao1 = saveAluno(irmao1, false);

			aluno.setIrmao1(irmao1);
			irmao1.setIrmao1(aluno);

			if (tem2Irmao) {
				clone(aluno, irmao2);
				irmao2 = saveAluno(irmao2, false);
				aluno.setIrmao2(irmao2);
				irmao1.setIrmao1(aluno);
				irmao1.setIrmao2(irmao2);
				irmao2.setIrmao1(aluno);
				irmao2.setIrmao2(irmao1);
			}
			if (tem3Irmao) {
				clone(aluno, irmao3);
				irmao3 = saveAluno(irmao3, false);
				aluno.setIrmao3(irmao3);
				irmao3.setIrmao1(aluno);
				irmao3.setIrmao2(irmao1);
				irmao3.setIrmao3(irmao2);

				irmao1.setIrmao1(aluno);
				irmao1.setIrmao2(irmao2);
				irmao1.setIrmao3(irmao3);
				irmao2.setIrmao1(aluno);
				irmao2.setIrmao2(irmao1);
				irmao2.setIrmao3(irmao3);
			}
			if (tem4Irmao) {
				clone(aluno, irmao4);
				irmao4 = saveAluno(irmao4, false);
				aluno.setIrmao4(irmao4);
				irmao4.setIrmao1(aluno);
				irmao4.setIrmao2(irmao1);
				irmao4.setIrmao3(irmao2);
				irmao4.setIrmao4(irmao3);

				irmao1.setIrmao1(aluno);
				irmao1.setIrmao2(irmao2);
				irmao1.setIrmao3(irmao3);
				irmao1.setIrmao4(irmao4);
				irmao2.setIrmao1(aluno);
				irmao2.setIrmao2(irmao1);
				irmao2.setIrmao3(irmao3);
				irmao2.setIrmao4(irmao4);

				irmao3.setIrmao1(aluno);
				irmao3.setIrmao2(irmao1);
				irmao3.setIrmao3(irmao2);
				irmao3.setIrmao4(irmao4);
			}
		}

		if (tem2Irmao) {
			clone(aluno, irmao2);
			irmao2 = saveAluno(irmao2, false);
			aluno.setIrmao2(irmao2);
			irmao2.setIrmao1(aluno);

			if (tem3Irmao) {
				clone(aluno, irmao3);
				irmao3 = saveAluno(irmao3, false);
				aluno.setIrmao3(irmao3);
				irmao3.setIrmao1(aluno);
				irmao3.setIrmao3(irmao2);

				irmao2.setIrmao1(aluno);
				irmao2.setIrmao3(irmao3);
			}
			if (tem4Irmao) {
				clone(aluno, irmao4);
				irmao4 = saveAluno(irmao4, false);
				aluno.setIrmao4(irmao4);
				irmao4.setIrmao1(aluno);
				irmao4.setIrmao3(irmao2);
				irmao4.setIrmao4(irmao3);

				irmao2.setIrmao1(aluno);
				irmao2.setIrmao3(irmao3);
				irmao2.setIrmao4(irmao4);

				irmao3.setIrmao1(aluno);
				irmao3.setIrmao3(irmao2);
				irmao3.setIrmao4(irmao4);
			}

		}
		if (tem3Irmao) {
			clone(aluno, irmao3);
			irmao3 = saveAluno(irmao3, false);
			aluno.setIrmao3(irmao3);
			irmao3.setIrmao1(aluno);

			if (tem4Irmao) {
				clone(aluno, irmao4);
				irmao4 = saveAluno(irmao4, false);
				aluno.setIrmao4(irmao4);
				irmao4.setIrmao1(aluno);
				irmao4.setIrmao4(irmao3);
				irmao3.setIrmao1(aluno);
				irmao3.setIrmao4(irmao4);
			}
		}

		if (tem4Irmao) {
			clone(aluno, irmao4);
			irmao4 = saveAluno(irmao4, false);
			aluno.setIrmao4(irmao4);
			irmao4.setIrmao1(aluno);
		}
	}

	public void clone(Aluno aluno, Aluno user) {
		// user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());
		user.setDataMatricula(aluno.getDataMatricula());
		// user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());
		user.setCodigo(aluno.getCodigo());

		user.setNomeAvoHPaternoMae(aluno.getNomeAvoHPaternoMae());
		user.setCpfMae(aluno.getCpfMae());
		user.setCpfPai(aluno.getCpfPai());
		user.setDataMatricula(aluno.getDataMatricula());
		user.setEmailMae(aluno.getEmailMae());
		user.setEmailPai(aluno.getEmailPai());
		user.setEmpresaTrabalhaMae(aluno.getEmpresaTrabalhaMae());
		user.setEmpresaTrabalhaPai(aluno.getEmpresaTrabalhaPai());
		user.setLogin(aluno.getLogin());
		user.setNaturalidadeMae(aluno.getNaturalidadeMae());
		user.setNaturalidadePai(aluno.getNaturalidadePai());
		user.setNomeAvoHPaternoMae(aluno.getNomeAvoHPaternoMae());
		user.setNomeAvoHPaternoPai(aluno.getNomeAvoHPaternoPai());
		user.setNomeAvoPaternoMae(aluno.getNomeAvoPaternoMae());
		user.setNomeAvoPaternoPai(aluno.getNomeAvoPaternoPai());
		user.setNomeMaeAluno(aluno.getNomeMaeAluno());
		user.setNomePaiAluno(aluno.getNomePaiAluno());
		user.setObservacaoSecretaria(aluno.getObservacaoSecretaria());
		user.setTelefoneResidencialPai(aluno.getTelefoneResidencialPai());
		user.setRgMae(aluno.getRgMae());
		user.setRgPai(aluno.getRgPai());
		user.setSenha(aluno.getSenha());
		user.setTelefone(aluno.getTelefone());
		user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());

		user.setCpfMae(aluno.getCpfMae());
		user.setCpfPai(aluno.getCpfPai());
		user.setEmergenciaLigarPara(aluno.getEmergenciaLigarPara());
		user.setNomecontatoSaidaEstabelecimento1(aluno.getNomecontatoSaidaEstabelecimento1());
		user.setNomecontatoSaidaEstabelecimento2(aluno.getNomecontatoSaidaEstabelecimento2());
		user.setNomecontatoSaidaEstabelecimento3(aluno.getNomecontatoSaidaEstabelecimento3());

		if (user.getContratosSux() != null) {
			if (aluno.getContratos() != null) {
				user.getContratosSux().addAll(aluno.getContratos());
			}
		} else {
			List<ContratoAluno> contratos = new ArrayList<>();
			user.setContratos(contratos);
		}
		if (aluno.getRemovido() == null) {
			user.setRemovido(false);
		} else {
			user.setRemovido(aluno.getRemovido());
		}

	}

	public String remover(Long idAluno) {
		Aluno al = findById(idAluno);
		al.setRemovido(true);
		al.setDataCancelamento(new Date());
		em.persist(al);
		em.flush();
		return "ok";
	}

	public String restaurar(Long idAluno) {
		Aluno al = findById(idAluno);
		ContratoAluno contratoAluno = al.getContratoVigente();
		al.setRemovido(false);
		al.setRestaurada(true);
		contratoAluno.setCnabEnviado(false);
		em.persist(al);
		em.persist(contratoAluno);
		em.flush();
		return "ok";
	}

	public void saveAlunoTurma(List<Aluno> target, Turma turma) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT at from  AlunoTurma at ");
		sql.append("where at.turma.id =   ");
		sql.append(turma.getId());

		Query query = em.createQuery(sql.toString());

		try {
			List<AlunoTurma> alunosTurmas = query.getResultList();
			for (AlunoTurma profT : alunosTurmas) {
				em.remove(profT);
				em.flush();
			}

			for (Aluno prof : target) {
				AlunoTurma pt = new AlunoTurma();
				pt.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo());
				pt.setAluno(prof);
				pt.setTurma(em.find(Turma.class, turma.getId()));
				em.persist(pt);
				em.flush();
			}

		} catch (NoResultException noResultException) {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Professor getProfessor(Long idAluno) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT at.turma from  AlunoTurma at ");
		sql.append("where 1 = 1");
		sql.append("and  at.aluno.id = ");
		sql.append(idAluno);
		Query query = em.createQuery(sql.toString());
		Turma t = (Turma) query.getResultList().get(0);

		StringBuilder sql2 = new StringBuilder();
		sql2.append("SELECT pt.professor from  ProfessorTurma pt ");
		sql2.append("where 1 = 1");
		sql2.append(" and  pt.turma.id = ");
		sql2.append(t.getId());
		sql2.append(" and  pt.professor.especialidade = 0");
		Query query2 = em.createQuery(sql2.toString());
		List<Professor> profs = query2.getResultList();
		if (profs != null && profs.size() > 0) {
			return profs.get(0);
		}
		return new Professor();

	}

	public float getNota(Long idAluno, DisciplinaEnum disciplina, BimestreEnum bimestre, boolean recupecacao) {
		try {
			if (idAluno == 5506L) {
				System.out.println("id");
			}
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT av from  AlunoAvaliacao av ");
			sql.append("where 1 = 1");
			sql.append(" and  av.aluno.id = ");
			sql.append(idAluno);
			sql.append(" and  av.avaliacao.disciplina = ");
			sql.append(disciplina.ordinal());
			if (bimestre != null) {
				sql.append(" and  av.avaliacao.bimestre = ");
				sql.append(bimestre.ordinal());
			}
			sql.append(" and  av.avaliacao.recuperacao = ");
			sql.append(recupecacao);

			sql.append(" and  av.avaliacao.anoLetivo = ");
			sql.append(configuracaoService.getConfiguracao().getAnoLetivo());
			Query query = em.createQuery(sql.toString());

			List<AlunoAvaliacao> notas = (List<AlunoAvaliacao>) query.getResultList();
			Float soma = 0F;
			Float pesos = 0F;
			if (notas != null && !notas.isEmpty()) {
				for (AlunoAvaliacao avas : notas) {
					soma += avas.getNota() * avas.getAvaliacao().getPeso();
					pesos += avas.getAvaliacao().getPeso();
				}
			}

			return soma / pesos;

		} catch (Exception e) {
			return 0f;
		}
	}

	public float getNota(Long idAluno, DisciplinaEnum disciplina, BimestreEnum bimestre, boolean recupecacao, int ano) {
		try {
			if (idAluno == 5506L) {
				System.out.println("id");
			}
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT av from  AlunoAvaliacao av ");
			sql.append("where 1 = 1");
			sql.append(" and  av.aluno.id = ");
			sql.append(idAluno);
			sql.append(" and  av.avaliacao.disciplina = ");
			sql.append(disciplina.ordinal());
			if (bimestre != null) {
				sql.append(" and  av.avaliacao.bimestre = ");
				sql.append(bimestre.ordinal());
			}
			sql.append(" and  av.avaliacao.recuperacao = ");
			sql.append(recupecacao);

			sql.append(" and  av.avaliacao.anoLetivo = ");
			sql.append(ano);
			Query query = em.createQuery(sql.toString());

			List<AlunoAvaliacao> notas = (List<AlunoAvaliacao>) query.getResultList();
			Float soma = 0F;
			Float pesos = 0F;
			if (notas != null && !notas.isEmpty()) {
				for (AlunoAvaliacao avas : notas) {
					soma += avas.getNota() * avas.getAvaliacao().getPeso();
					pesos += avas.getAvaliacao().getPeso();
				}
			}

			return soma / pesos;

		} catch (Exception e) {
			return 0f;
		}
	}

	public float getNota(Long idAluno, DisciplinaEnum disciplina, boolean recuperacao, boolean provafinal) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT avg(av.nota) from  AlunoAvaliacao av ");
		sql.append("where 1 = 1");
		sql.append(" and  av.aluno.id = ");
		sql.append(idAluno);
		sql.append(" and  av.avaliacao.disciplina = ");
		sql.append(disciplina.ordinal());
		sql.append(" and  av.avaliacao.provaFinal = ");
		sql.append(provafinal);
		sql.append(" and  av.avaliacao.recuperacao = ");
		sql.append(recuperacao);
		Query query = em.createQuery(sql.toString());

		Object valor = query.getSingleResult();
		if (valor != null) {
			valor = (double) valor;
		} else {
			return 0;
		}

		return Float.parseFloat(String.valueOf(valor));
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> find(int first, int size, String orderBy, String order, Map<String, Object> filtros) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aluno> criteria = cb.createQuery(Aluno.class);
			Root<Aluno> member = criteria.from(Aluno.class);
			CriteriaQuery cq = criteria.select(member);

			final List<Predicate> predicates = new ArrayList<Predicate>();
			for (Map.Entry<String, Object> entry : filtros.entrySet()) {

				Predicate pred = cb.and();
				if (entry.getValue() instanceof String) {
					pred = cb.and(pred, cb.like(member.<String> get(entry.getKey()), "%" + entry.getValue() + "%"));
				} else {
					pred = cb.equal(member.get(entry.getKey()), entry.getValue());
				}
				predicates.add(pred);
				// cq.where(pred);
			}

			cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
			cq.orderBy((order.equals("asc") ? cb.asc(member.get(orderBy)) : cb.desc(member.get(orderBy))));
			Query q = em.createQuery(criteria);
			q.setFirstResult(first);
			q.setMaxResults(size);
			List<Aluno> als = (List<Aluno>) q.getResultList();
			for (Aluno al : als) {
				carregarLazyContrato(al);
			}
			return als;

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

	}

	private void carregarLazyContrato(Aluno al) {
		if (al.getContratosSux() != null) {
			al.getContratosSux().size();
			for (ContratoAluno c : al.getContratosSux()) {
				c.getBoletos().size();
			}
		}
	}

	public long count(Map<String, Object> filtros) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			Root<Aluno> member = countQuery.from(Aluno.class);
			countQuery.select(cb.count(member));

			final List<Predicate> predicates = new ArrayList<Predicate>();
			if (filtros != null) {
				for (Map.Entry<String, Object> entry : filtros.entrySet()) {

					Predicate pred = cb.and();
					if (entry.getValue() instanceof String) {
						pred = cb.and(pred, cb.like(member.<String> get(entry.getKey()), "%" + entry.getValue() + "%"));
					} else {
						pred = cb.equal(member.get(entry.getKey()), entry.getValue());
					}
					predicates.add(pred);

				}

			}
			countQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
			Query q = em.createQuery(countQuery);
			return (long) q.getSingleResult();

		} catch (NoResultException nre) {
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public List<HistoricoAluno> getHistoricoAluno(Aluno aluno) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<HistoricoAluno> criteria = cb.createQuery(HistoricoAluno.class);
			Root<HistoricoAluno> member = criteria.from(HistoricoAluno.class);
			CriteriaQuery cq = criteria.select(member);

			Predicate pred = cb.and();
			pred = cb.equal(member.get("aluno"), aluno);
			cq.where(pred);

			cq.orderBy(cb.desc(member.get("ano")));
			Query q = em.createQuery(criteria);
			List<HistoricoAluno> hist = (List<HistoricoAluno>) q.getResultList();
			return hist;

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void saveHistorico(HistoricoAluno historico) {
		log.info("Registering historico do aluno " + historico.getAluno().getNomeAluno());
		HistoricoAluno ha = null;
		if (historico.getId() != null && historico.getId() != 0L) {
			ha = findHistoricoById(historico.getId());
		} else {
			ha = new HistoricoAluno();
		}
		ha.setNomeAluno(historico.getAluno().getNomeAluno());
		ha.setAluno(findById(historico.getAluno().getId()));
		ha.setAno(historico.getAno());
		ha.setEscola(historico.getEscola());
		ha.setNotaArtes(historico.getNotaArtes());
		ha.setNotaCiencias(historico.getNotaCiencias());
		ha.setNotaEdFisica(historico.getNotaEdFisica());
		ha.setNotaformacaoCrista(historico.getNotaformacaoCrista());
		ha.setNotaGeografia(historico.getNotaGeografia());
		ha.setNotaHistoria(historico.getNotaHistoria());
		ha.setNotaIngles(historico.getNotaIngles());
		ha.setNotaMatematica(historico.getNotaMatematica());
		ha.setNotaPortugues(historico.getNotaPortugues());
		ha.setSerie(historico.getSerie());
		ha.setEstado(historico.getEstado());
		ha.setMunicipio(historico.getMunicipio());
		ha.setFrequencia(historico.getFrequencia());

		em.persist(ha);
		em.flush();
	}

	public void removerHistorico(long idHistorico) {
		em.remove(findHistoricoById(idHistorico));
	}

	public boolean estaEmUmaTUrma(long idAluno) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			Root<AlunoTurma> member = countQuery.from(AlunoTurma.class);
			countQuery.select(cb.count(member));

			Predicate pred = cb.and();
			pred = cb.equal(member.get("aluno").get("id"), idAluno);
			countQuery.where(pred);

			Query q = em.createQuery(countQuery);
			return ((long) q.getSingleResult()) > 0 ? true : false;

		} catch (NoResultException nre) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public void rematricular(Long id) {
		Aluno rematriculado = findById(id);
		rematriculado.setRematricular(true);
		rematriculado.setPeriodoProximoAno(rematriculado.getPeriodo());
		em.merge(rematriculado);
		em.flush();

	}

	public void rematricularAlunoAntigo(Long id) {
		Aluno rematriculado = findById(id);
		rematriculado.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo());
		rematriculado.setPeriodoProximoAno(rematriculado.getPeriodo());
		rematriculado.setRemovido(false);
		em.merge(rematriculado);
		em.flush();

	}

	public void rematricularCancelado(Long id) {
		Aluno rematriculado = findById(id);
		rematriculado.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo());
		rematriculado.setRestaurada(true);
		rematriculado.setRemovido(false);
		// rematriculado.getContratoVigente().setCnabEnviado(false);
		em.merge(rematriculado);
		em.flush();

	}

	public void desmatricular(Long id) {
		Aluno rematriculado = findById(id);
		rematriculado.setRematricular(false);
		em.merge(rematriculado);
		em.flush();
	}

	public List<Boleto> gerarBoletos(Aluno user, ContratoAluno contrato) {
		return gerarBoletos(user, configuracaoService.getConfiguracao().getAnoLetivo(), contrato);
	}

	public List<Boleto> gerarBoletos(Aluno user, int ano, ContratoAluno contrato) {
		int quantidadeParcelas = 12 - contrato.getNumeroParcelas();
		return gerarBoletos(user, ano, quantidadeParcelas, contrato);
	}

	public List<Boleto> gerarBoletos(Aluno user, int ano, int quantidadeParcelas, ContratoAluno contrato) {
		List<Boleto> boletos = new ArrayList<>();
		long nossoNumero = getProximoNossoNumero();
		while (quantidadeParcelas < 12) {
			Boleto boleto = new Boleto();
			Calendar vencimento = Calendar.getInstance();
			vencimento.set(Calendar.MONTH, quantidadeParcelas);
			vencimento.set(Calendar.YEAR, ano);
			vencimento.set(Calendar.HOUR, 0);
			vencimento.set(Calendar.MINUTE, 0);
			vencimento.set(Calendar.SECOND, 0);
			if (contrato.getVencimentoUltimoDia() == null || !contrato.getVencimentoUltimoDia()) {
				vencimento.set(Calendar.DAY_OF_MONTH, contrato.getDiaVencimento());
			} else {
				int dia = vencimento.getActualMaximum(Calendar.DAY_OF_MONTH);
				vencimento.set(Calendar.DAY_OF_MONTH, dia);
			}
			boleto.setVencimento(vencimento.getTime());
			boleto.setEmissao(new Date());
			boleto.setValorNominal(contrato.getValorMensal());
			boleto.setPagador(user);
			boleto.setNossoNumero(nossoNumero);
			boleto.setContrato(contrato);
			em.persist(boleto);
			nossoNumero++;
			boletos.add(boleto);

			quantidadeParcelas++;
			em.flush();
		}
		return boletos;
	}

	public void gerarBoletos() {
		List<Aluno> alunos = findAll();
		int anoREmatricula = configuracaoService.getConfiguracao().getAnoRematricula();
		for (Aluno al : alunos) {
			for (ContratoAluno contrato : al.getContratosVigentes()) {
				if (al.getRemovido() != null && !al.getRemovido()) {

					if (contrato.getBoletos() == null || contrato.getBoletos().size() == 0) {
						if (contrato.getNumeroParcelas() != null && contrato.getNumeroParcelas() > 0) {
							if ((al.getAnoLetivo() == configuracaoService.getConfiguracao().getAnoLetivo())
									|| (al.getRematricular() != null && al.getRematricular())) {
								if (!irmaoJaTemBoleto(al)) {
									List<Boleto> boletos = gerarBoletos(al, contrato);
									contrato.setBoletos(boletos);
									em.persist(al);
								}
							}
						}
					} else if (contrato.getBoletos() != null && contrato.getBoletos().size() > 0) {
						List<Boleto> boletos = contrato.getBoletos();
						if (todosBoletosBaixados(boletos) && al.getRestaurada() != null && al.getRestaurada()) {
							List<Boleto> boletosGErados = gerarBoletos(al, contrato);
							boletosGErados.addAll(contrato.getBoletos());
							contrato.setBoletos(boletosGErados);
							em.persist(al);
						} else {
							for (Boleto b : boletos) {
								if (b.getAlteracaoBoletoManual() == null || !b.getAlteracaoBoletoManual()) {
									b.setValorNominal(contrato.getValorMensal());
									Calendar c = Calendar.getInstance();
									c.setTime(b.getVencimento());
									if (contrato.getVencimentoUltimoDia() == null
											|| !contrato.getVencimentoUltimoDia()) {
										c.set(Calendar.DAY_OF_MONTH, contrato.getDiaVencimento());
									} else {
										int dia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
										c.set(Calendar.DAY_OF_MONTH, dia);
									}
									b.setVencimento(c.getTime());
									em.merge(b);
								}
							}
						}
					}
				}
				// gerarBoletosRematricula(al,anoREmatricula);
			}
			correcaoModelagemAlunoContrato(al);
		}
		em.flush();

	}

	private void correcaoModelagemAlunoContrato(Aluno al) {
		if (al.getContratos() == null || al.getContratos().isEmpty()) {
			ContratoAluno contrato = new ContratoAluno();
			contrato.setAluno(al);
			contrato.setAno((short) 2018);
			contrato.setAnuidade(al.getAnuidade());
			contrato.setBairro(al.getBairro());
			contrato.setBoletos(al.getBoletos());
			contrato.setCep(al.getCep());
			contrato.setCidade(al.getCidade());
			contrato.setCnabEnviado(al.getCnabEnviado());
			contrato.setContratoTerminado(al.getContratoTerminado());
			contrato.setCpfResponsavel(al.getCpfResponsavel());
			contrato.setDataCancelamento(al.getDataCancelamento());
			contrato.setDataCriacaoContrato(al.getDataMatricula());
			contrato.setDiaVencimento(al.getDiaVencimento());
			contrato.setEndereco(al.getEndereco());
			contrato.setEnviadoParaCobrancaCDL(al.getEnviadoParaCobrancaCDL());
			contrato.setEnviadoSPC(al.getEnviadoSPC());
			contrato.setNomeMaeResponsavel(al.getNomeMaeResponsavel());
			contrato.setNomePaiResponsavel(al.getNomePaiResponsavel());
			contrato.setNomeResponsavel(al.getNomeResponsavel());

			String ano = String.valueOf(contrato.getAno());
			String finalANo = ano.substring(ano.length() - 2, ano.length());
			String numeroUltimoContrato = "01";

			String numero = finalANo + contrato.getAluno().getCodigo() + numeroUltimoContrato;
			contrato.setNumero(numero);

			contrato.setNumeroParcelas(al.getNumeroParcelas());
			contrato.setRgResponsavel(al.getRgResponsavel());
			contrato.setValorMensal(al.getValorMensal());
			contrato.setVencimentoUltimoDia(al.isVencimentoUltimoDia());
			List<ContratoAluno> contratos = new ArrayList<>();
			contratos.add(contrato);
			al.setContratos(contratos);
			al.setBoletos(null);
			em.persist(contrato);
			em.persist(al);
			em.flush();
		}

	}

	private void popularBoleto(ContratoAluno populista, ContratoAluno aserPopulado) {
		aserPopulado.setBairro(populista.getBairro());
		aserPopulado.setCep(populista.getCep());
		aserPopulado.setCidade(populista.getCidade());
		aserPopulado.setCpfResponsavel(populista.getCpfResponsavel());
		aserPopulado.setDataCriacaoContrato(new Date());
		aserPopulado.setDiaVencimento(populista.getDiaVencimento());
		aserPopulado.setEndereco(populista.getEndereco());
		aserPopulado.setNomeMaeResponsavel(populista.getNomeMaeResponsavel());
		aserPopulado.setNomePaiResponsavel(populista.getNomePaiResponsavel());
		aserPopulado.setNomeResponsavel(populista.getNomeResponsavel());
		aserPopulado.setNumeroParcelas(populista.getNumeroParcelas());
		aserPopulado.setRgResponsavel(populista.getRgResponsavel());
		aserPopulado.setVencimentoUltimoDia(populista.getVencimentoUltimoDia());
	}

	private void gerarBoletosRematricula(Aluno al, int anoRematricula) {

		if (al.getRemovido() != null && !al.getRemovido()) {
			if (al.getRematricular() != null && al.getRematricular()) {
				ContratoAluno contrato = new ContratoAluno();
				popularBoleto(al.getUltimoContrato(), contrato);
				// TODO pergar o valor da matricula de algum lugar // atualmente
				// +30 verificar irmaos tb
				contrato.setValorMensal(al.getUltimoContrato().getValorMensal() + 30);
				contrato.setAno((short) configuracaoService.getConfiguracao().getAnoRematricula());
				if (!possuiBoletoNoAno(al, anoRematricula, contrato)) {
					List<Boleto> boletosGErados = gerarBoletos(al, anoRematricula, 0, contrato);
					contrato.setBoletos(boletosGErados);
					contrato.setAluno(al);
					contrato.setNumero(10 + al.getCodigo());
					em.persist(contrato);
					al.getContratos().add(contrato);
					em.persist(al);
					em.flush();
				}
			}
		}
	}

	private boolean possuiBoletoNoAno(Aluno al, int anoRematricula, ContratoAluno contrato) {
		boolean retorno = false;
		List<Boleto> boletos = contrato.getBoletos();
		Calendar calendar = new GregorianCalendar();
		if (boletos != null && !boletos.isEmpty()) {
			for (Boleto boleto : boletos) {
				calendar.setTime(boleto.getVencimento());
				int anoBoleto = calendar.get(Calendar.YEAR);
				if (anoBoleto == anoRematricula) {
					retorno = true;
				}
			}
		} else {
			retorno = false;
		}
		return retorno;
	}

	private boolean irmaoJaTemBoleto(Aluno aluno) {
		Aluno irmao1 = aluno.getIrmao1();
		Aluno irmao2 = aluno.getIrmao2();
		Aluno irmao3 = aluno.getIrmao3();
		Aluno irmao4 = aluno.getIrmao4();

		if (irmao1 != null) {
			if (irmao1.getContratoVigente() != null && irmao1.getContratoVigente().getBoletos() != null
					&& irmao1.getContratoVigente().getBoletos().size() > 0) {
				return true;
			}
		}

		if (irmao2 != null) {
			if (irmao2.getContratoVigente().getBoletos() != null
					&& irmao2.getContratoVigente().getBoletos().size() > 0) {
				return true;
			}
		}

		if (irmao3 != null) {
			if (irmao3.getContratoVigente().getBoletos() != null
					&& irmao3.getContratoVigente().getBoletos().size() > 0) {
				return true;
			}
		}

		if (irmao4 != null) {
			if (irmao4.getContratoVigente().getBoletos() != null
					&& irmao4.getContratoVigente().getBoletos().size() > 0) {
				return true;
			}
		}

		return false;
	}

	public List<Boleto> gerarBoletos(Aluno user, boolean setUser, ContratoAluno contrato) {
		List<Boleto> boletos = gerarBoletos(user, contrato);
		contrato.setBoletos(boletos);
		em.persist(contrato);
		em.flush();
		return boletos;
	}

	private boolean todosBoletosBaixados(List<Boleto> boletos) {
		boolean todosBaixados = true;
		for (Boleto bol : boletos) {
			if (!(bol.getBaixaGerada() != null && bol.getBaixaGerada())) {
				if (!(bol.getBaixaManual() != null && bol.getBaixaManual())) {
					if (!(bol.getConciliacaoPorExtrato() != null && bol.getConciliacaoPorExtrato())) {
						if (!(bol.getValorPago() != null && bol.getValorPago() > 0)) {
							todosBaixados = false;
						}
					}
				}
			}
		}
		return todosBaixados;
	}

	public Long getProximoCodigo() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT max(cast(al.codigo AS integer))  from Aluno al ");

		Query query = em.createNativeQuery(sql.toString());
		Integer codigo = (Integer) query.getSingleResult();

		return codigo.longValue() + 1;
	}

	public Long getProximoNossoNumero() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT max(bol.nossoNumero) from Boleto bol ");

		Query query = em.createQuery(sql.toString());
		Long codigo = null;
		try {
			codigo = (Long) query.getSingleResult();

		} catch (Exception e) {
			codigo = 30000L;
		}
		if (codigo == null) {
			codigo = 30000L;
		}
		if (codigo < 10000) {
			codigo = 30000L;
		}

		return codigo + 1;
	}

	public List<Aluno> findAluno(String nome, String nomeResponsavel, String cpf, String numeroDocumento) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct(cont) from  Boleto bol ");
		sql.append("left join bol.contrato cont ");

		sql.append("where 1=2 ");
		if (nome != null && !nome.equalsIgnoreCase("")) {
			sql.append(" or cont.aluno.nomeAluno like '%");
			sql.append(nome);
			sql.append("%' ");
		} else {

		}
		if (nomeResponsavel != null && !nomeResponsavel.equalsIgnoreCase("")) {
			sql.append(" or cont.nomeResponsavel like '%");
			sql.append(nomeResponsavel);
			sql.append("%' ");
		}

		if (cpf != null && !cpf.equalsIgnoreCase("")) {
			sql.append(" or cont.cpfResponsavel like '%");
			sql.append(cpf);
			sql.append("%' ");
		}

		if (numeroDocumento != null && !numeroDocumento.equalsIgnoreCase("")) {
			sql.append(" or bol.nossoNumero = ");
			sql.append(numeroDocumento);
		}

		Query query = em.createQuery(sql.toString());

		@SuppressWarnings("unchecked")
		List<ContratoAluno> cas = query.getResultList();
		List<Aluno> alunos = new ArrayList<>();
		for (ContratoAluno ca : cas) {
			ca.getBoletos().size();
			ca.getId();
			for (ContratoAluno casac : ca.getAluno().getContratos()) {
				casac.getId();
				List<Boleto> bs = casac.getBoletos();
				if (bs != null) {
					bs.size();
				}
			}
			ca.getAluno();
			ca.getAluno().setNomeResponsavel(ca.getNomeResponsavel());
			ca.getAluno().setCpfResponsavel(ca.getCpfResponsavel());
			alunos.add(ca.getAluno());
		}

		return alunos;
	}

	public List<Aluno> findAlunoAlunoLetivo() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct(al) from  Aluno al ");
		sql.append("where 1=1 ");
		sql.append("and al.anoLetivo = ");
		sql.append(configuracaoService.getConfiguracao().getAnoLetivo());
		sql.append(" and (al.removido = false  or removido is null)");

		Query query = em.createQuery(sql.toString());

		@SuppressWarnings("unchecked")
		List<Aluno> alunos = query.getResultList();
		for (Aluno al : alunos) {
			al.getContratos().size();
			for (ContratoAluno contrato : al.getContratos()) {
				contrato.getBoletos().size();
			}
		}

		return alunos;
	}

	public void salvarTodos() {
		System.out.println("FAZENDO");
		atualizarContatosDeAbaPaiMaeParaContatos();

	}

	private void atualizarContatosDeAbaPaiMaeParaContatos() {
		for (Aluno al : findAll()) {
			if (al.getTelefoneCelularMae() != null && !al.getTelefoneCelularMae().equals("")) {
				al.setContatoTelefone1(al.getTelefoneCelularMae());
				al.setContatoNome1("Celular mae");
			}
			if (al.getTelefoneEmpresaTrabalhaMae() != null && !al.getTelefoneEmpresaTrabalhaMae().equals("")) {
				al.setContatoTelefone2(al.getTelefoneEmpresaTrabalhaMae());
				al.setContatoNome2("Telefone empresa mae");
			}
			if (al.getTelefoneResidencialMae() != null && !al.getTelefoneResidencialMae().equals("")) {
				al.setContatoTelefone3(al.getTelefoneResidencialMae());
				al.setContatoNome3("Residencial mae");
			}
			if (al.getTelefoneCelularPai() != null && !al.getTelefoneCelularPai().equals("")) {
				al.setContatoTelefone4(al.getTelefoneCelularPai());
				al.setContatoNome4("Celular pai");
			}

			if (al.getTelefoneEmpresaTrabalhaPai() != null && !al.getTelefoneEmpresaTrabalhaPai().equals("")) {
				al.setContatoTelefone5(al.getTelefoneEmpresaTrabalhaPai());
				al.setContatoNome5("Telefone empresa mae");
			}
			em.merge(al);
			em.flush();
		}
	}

	public void removerCnabEnviado(Long id) {
		Aluno aluno = findById(id);
		ContratoAluno contrato = aluno.getContratoVigente();
		contrato.setCnabEnviado(false);
		em.merge(contrato);
		em.flush();
	}

	public void enviarCnab(Long id) {
		Aluno aluno = findById(id);
		ContratoAluno contrato = aluno.getContratoVigente();
		contrato.setCnabEnviado(true);
		em.merge(contrato);
		em.flush();
	}

	public void removerVerificadoOk(Long id) {
		Aluno aluno = findById(id);
		aluno.setVerificadoOk(false);
		em.merge(aluno);
		em.flush();
	}

	public void verificadoOk(Long id) {
		Aluno aluno = findById(id);
		aluno.setVerificadoOk(true);
		em.merge(aluno);
		em.flush();
	}

	public void removerBoleto(Long idBoleto) {
		Boleto b = findBoletoById(idBoleto);
		b.setManterAposRemovido(false);
		b.setValorPago((double) 0);
		b.setDataPagamento(new Date());
		em.merge(b);
		em.flush();
	}

	public void manterBoleto(Long idBoleto) {
		Boleto b = findBoletoById(idBoleto);

		b.setManterAposRemovido(true);
		b.setCancelado(false);
		em.merge(b);
		em.flush();
	}

	public void remover(Aluno aluno) {
		for (ContratoAluno contrato : aluno.getContratosVigentes()) {
			for (Boleto b : contrato.getBoletos()) {
				if (b.getManterAposRemovido() != null && b.getManterAposRemovido()) {
					manterBoleto(b.getId());
				} else {
					removerBoleto(b.getId());
				}
			}

		}
		em.flush();
		remover(aluno.getId());
	}

	public void removerContrato(ContratoAluno contrat) {
		for (Boleto b : contrat.getBoletos()) {
			if (b.getManterAposRemovido() != null && b.getManterAposRemovido()) {
				manterBoleto(b.getId());
			} else {
				removerBoleto(b.getId());
			}
		}
		Aluno al = findById(contrat.getAluno().getId());
		ContratoAluno contratoPersistence = findContratoById(contrat.getId());
		contratoPersistence.setDataCancelamento(new Date());
		contratoPersistence.setCancelado(true);
		contratoPersistence.setAluno(al);
		contratoPersistence.setBoletos(contrat.getBoletos());
		try {
			em.merge(contratoPersistence);
		} catch (Exception e) {
			e.printStackTrace();
			em.persist(contratoPersistence);
		}
		em.flush();
		// em.getTransaction().commit();

	}

	public Aluno adicionarContrato(Aluno aluno, ContratoAluno novoContrato) {
		if (aluno.getAnoLetivo() == 0) {
			aluno.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo());
		}
		if (aluno.getId() != null) {
			aluno = findById(aluno.getId());
		} else {
			saveAluno(aluno, true);
		}
		novoContrato.setAluno(aluno);
		em.persist(novoContrato);
		List<ContratoAluno> contratos = aluno.getContratos();
		if (contratos == null) {
			contratos = new ArrayList<>();
		}
		contratos.add(novoContrato);
		aluno.setContratos(contratos);
		em.merge(aluno);
		em.flush();
		return aluno;

	}

	public ContratoAluno criarBoletos(Aluno aluno, short ano, Integer numeroParcelas, ContratoAluno contrato) {
		int numPa = 12 - contrato.getNumeroParcelas();
		contrato = findContratoById(contrato.getId());
		em.persist(contrato);
		List<Boleto> boletos = this.gerarBoletos(aluno, ano, numPa, contrato);
		contrato.setBoletos(boletos);
		em.flush();
		return contrato;
	}

	public ContratoAluno saveContrato(ContratoAluno contrato) {
		ContratoAluno c = new ContratoAluno();
		if (contrato.getId() != null) {
			c = findContratoById(contrato.getId());
		}
		c.setAno(contrato.getAno());
		c.setAnuidade(contrato.getAnuidade());
		c.setBairro(contrato.getBairro());
		c.setCancelado(contrato.getCancelado());
		c.setCep(contrato.getCep());
		c.setCidade(contrato.getCidade());
		c.setCnabEnviado(contrato.getCnabEnviado());
		c.setContratoTerminado(contrato.getContratoTerminado());
		c.setCpfResponsavel(contrato.getCpfResponsavel());
		c.setDataCancelamento(contrato.getDataCancelamento());
		c.setDataCriacaoContrato(contrato.getDataCriacaoContrato());
		c.setDiaVencimento(contrato.getDiaVencimento());
		c.setVencimentoUltimoDia(contrato.getVencimentoUltimoDia());
		c.setValorMensal(contrato.getValorMensal());
		c.setRgResponsavel(contrato.getRgResponsavel());
		c.setNumeroParcelas(contrato.getNumeroParcelas());
		c.setNomeResponsavel(contrato.getNomeResponsavel());
		c.setNomePaiResponsavel(contrato.getNomePaiResponsavel());
		c.setNomeMaeResponsavel(contrato.getNomeMaeResponsavel());
		c.setEnviadoSPC(contrato.getEnviadoSPC());
		c.setEnviadoParaCobrancaCDL(contrato.getEnviadoSPC());
		c.setEndereco(contrato.getEndereco());

		String ano = String.valueOf(contrato.getAno());
		String finalANo = ano.substring(ano.length() - 2, ano.length());
		String numeroUltimoContrato = "01";
		int numeroNovo = 1;
		for (ContratoAluno contratt : contrato.getAluno().getContratos()) {
			if (contratt.getNumero() != null && !contratt.getNumero().equalsIgnoreCase("")) {
				if (contratt.getAno() == contrato.getAno()) {
					if (contrato.getId() != null && contrato.getId() != contratt.getId()) {
						String numeroContratt = contratt.getNumero();
						numeroContratt = numeroContratt.substring(numeroContratt.length() - 2, numeroContratt.length());
						if (Integer.parseInt(numeroContratt) > Integer.parseInt(numeroUltimoContrato)) {
							numeroUltimoContrato = numeroContratt;
						}
						numeroNovo = Integer.parseInt(numeroUltimoContrato);
						numeroNovo++;
					}

				}

				numeroUltimoContrato = String.valueOf(numeroNovo);
			}
		}

		String numero = finalANo + contrato.getAluno().getCodigo() + "0" + numeroUltimoContrato;
		c.setNumero(numero);

		em.merge(c);
		em.flush();
		return c;
	}

	public void saveContactado(Aluno al) {
		Aluno ap = findById(al.getId());

		ap.setDataContato(al.getDataContato());
		ap.setContactado(al.getContactado());
		ap.setQuantidadeContatos(al.getQuantidadeContatos());
		ap.setObservacaoAtrasado(al.getObservacaoAtrasado());
		ap.setDataPrometeuPagar(al.getDataPrometeuPagar());
		em.merge(ap);
		em.flush();

	}

	public void cancelarAlunosSemContratoAtivo() {
		for (Aluno al : findAll()) {
			if (!temContratoAtivo(al)) {
				if (al.getIrmao1() != null && temContratoAtivo(al.getIrmao1())) {
				} else if (al.getIrmao2() != null && temContratoAtivo(al.getIrmao2())) {
				} else {
					cancelar(al);
				}
			}
		}
	}

	public boolean temContratoAtivo(Aluno al) {
		boolean ativo = false;
		if (al.getContratosSux() != null) {
			for (ContratoAluno ca : al.getContratosSux()) {

				if (ca.getCancelado() == null || !ca.getCancelado()) {
					if (Verificador.possuiBoletoAberto(ca)) {
						ativo = true;
					}
				}
			}
		}
		return ativo;
	}

	private void cancelar(Aluno al) {
		Aluno ap = findById(al.getId());
		if (ap.getRemovido() == null || !ap.getRemovido()) {
			ap.setDataCancelamento(new Date());
			ap.setRemovido(true);
			em.merge(ap);
			em.flush();
		}
	}

	public void colocarAlunosNaListaDeCobranca() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 15);
		Date diaAtualMais15 = calendar.getTime();

		boolean contactado = true;
		for (Aluno al : findAll()) {
			contactado = true;

			if ((al.getDataContato() != null && al.getDataContato().after(diaAtualMais15))
					|| (al.getDataPrometeuPagar() != null && al.getDataPrometeuPagar().after(new Date()))) {
				if (al.getContactado() != null && al.getContactado()) {
					contactado = false;
				}
			}
			if (!contactado) {
				desconectado(al);
			}
		}
	}

	private void desconectado(Aluno al) {
		Aluno ap = findById(al.getId());
		ap.setContactado(false);
		em.merge(ap);
		em.flush();
	}

	public AulaService getAulaService() {
		return aulaService;
	}

	public void setAulaService(AulaService aulaService) {
		this.aulaService = aulaService;
	}
	
	public void setNfsEnviada(Long idBoleto) {
		Boleto bol = findBoletoById(idBoleto);
		bol.setNfsEnviada(true);
		em.persist(bol);
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> findAlunoDoAnoLetivo() {
		List<Aluno> alunos = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  Aluno al ");
		sql.append("where 1=1 ");
		sql.append(" and al.removido = false ");
		sql.append(" and al.anoLetivo = ");
		sql.append(configuracaoService.getConfiguracao().getAnoLetivo());
		Query query = em.createQuery(sql.toString());

		alunos = query.getResultList();

		return alunos;
	}
	
}

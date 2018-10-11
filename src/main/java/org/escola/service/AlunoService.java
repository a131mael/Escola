package org.escola.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.escola.model.Evento;
import org.escola.model.HistoricoAluno;
import org.escola.model.Professor;
import org.escola.model.Turma;
import org.escola.util.Constant;
import org.escola.util.Service;
import org.escola.util.UtilFinalizarAnoLetivo;

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
		al.getBoletos().size();

		return al;
	}

	public Boleto findBoletoById(Long id){
		Boleto boleto = em.find(Boleto.class, id);
		return boleto;
	}
	
	public Aluno findById(Long id) {
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
		al.getBoletos().size();

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
			
			List<Aluno> als =em.createQuery(criteria).getResultList(); 
			List<Aluno> aux = new ArrayList<>();
			for(Aluno al: als){
				al.getBoletos().size();
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
			Predicate whereRemovido = cb.equal(member.get("removido"), false);;
			Predicate whereAnoLetivo = cb.equal(member.get("anoLetivo"), Constant.anoLetivoAtual);
			
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
				criteria.select(member).where(whereSerie,whereRemovido,whereAnoLetivo);
				break;

			case "B":
				criteria.select(member).where(wherePeriodo,whereRemovido,whereAnoLetivo);
				break;

			case "AB":
				criteria.select(member).where(whereSerie, wherePeriodo,whereRemovido,whereAnoLetivo);
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

	public Aluno save(Aluno aluno) {
		return saveAluno(aluno, true);
	}

	public String removeCaracteresEspeciais(String texto) {
		texto = texto.replaceAll("[^aA-zZ-Z0-9 ]", "");
		return texto;
	}

	public Aluno saveAluno(Aluno aluno, boolean saveBrother) {
		Aluno user = null;
		try {

			log.info("Registering " + aluno.getNomeAluno());

			if (aluno.getId() != null && aluno.getId() != 0L) {
				user = findById(aluno.getId());
			} else {
				user = new Aluno();
				user.setAnoLetivo(Constant.anoLetivoAtual);
			}

			user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());
			user.setNomeAluno(aluno.getNomeAluno().toUpperCase());
			user.setPeriodo(aluno.getPeriodo());
			user.setSerie(aluno.getSerie());
			if (aluno.getEndereco() != null) {
				user.setEndereco(removeCaracteresEspeciais(aluno.getEndereco()));
			}
			if (aluno.getBairro() != null) {
				user.setBairro(removeCaracteresEspeciais(aluno.getBairro().replace("ç", "c")));

			}
			user.setCep(aluno.getCep());
			if (aluno.getCidade() != null) {
				user.setCidade(removeCaracteresEspeciais(aluno.getCidade().replace("ç", "c")));
			}
			user.setNacionalidade(aluno.getNacionalidade());
			user.setValorMensal(aluno.getValorMensal());
			user.setDataNascimento(aluno.getDataNascimento());
			user.setDataMatricula(aluno.getDataMatricula());
			user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());

			user.setPeriodoProximoAno(aluno.getPeriodoProximoAno());

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
			user.setAnuidade(aluno.getAnuidade() != null ? aluno.getAnuidade() : 0);
			if (aluno.getBairro() != null) {
				user.setBairro(removeCaracteresEspeciais(aluno.getBairro().replace("ç", "c")));
			}

			user.setCep(aluno.getCep());
			if (aluno.getCidade() != null) {
				user.setCidade(removeCaracteresEspeciais(aluno.getCidade().replace("ç", "c")));
			}
			user.setCpfMae(aluno.getCpfMae());
			user.setCpfPai(aluno.getCpfPai());
			if (aluno.getCpfResponsavel() != null) {
				user.setCpfResponsavel(aluno.getCpfResponsavel().replace(".", "").replace("-", ""));
			}
			user.setRgResponsavel(aluno.getRgResponsavel());
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

			if (aluno.getNomeResponsavel() != null) {
				user.setNomeResponsavel(removeCaracteresEspeciais(aluno.getNomeResponsavel().toUpperCase()));
			}
			
			if (aluno.getNomePaiResponsavel() != null) {
				user.setNomePaiResponsavel(removeCaracteresEspeciais(aluno.getNomePaiResponsavel().toUpperCase()));
			}
			
			if (aluno.getNomeMaeResponsavel() != null) {
				user.setNomeMaeResponsavel(removeCaracteresEspeciais(aluno.getNomeMaeResponsavel().toUpperCase()));
			}
			
			user.setNumeroParcelas(aluno.getNumeroParcelas());
			user.setObservacaoProfessores(aluno.getObservacaoProfessores());
			user.setObservacaoSecretaria(aluno.getObservacaoSecretaria());
			user.setValorMensal(aluno.getValorMensal());
			user.setTelefoneResidencialPai(aluno.getTelefoneResidencialPai());
			user.setRgMae(aluno.getRgMae());
			user.setRgPai(aluno.getRgPai());
			user.setSenha(aluno.getSenha());
			user.setTelefone(aluno.getTelefone());
			user.setEmergenciaLigarPara(aluno.getEmergenciaLigarPara());

			user.setDoenca(aluno.getDoenca());
			user.setAlergico(aluno.getAlergico());
			user.setNomeAlergias(aluno.getNomeAlergias());
			user.setNomeDoencas(aluno.getNomeDoencas());

			user.setDiaVencimento(aluno.getDiaVencimento());
			user.setVencimentoUltimoDia(aluno.isVencimentoUltimoDia());
			if (user.getCidade() != null && user.getCidade().equalsIgnoreCase("Palhoa")) {
				user.setCidade("Palhoca");
			}
			if (user.getBairro() != null && user.getBairro().equalsIgnoreCase("Palhoa")) {
				user.setBairro("Palhoca");
			}

			user.setDataCancelamento(aluno.getDataCancelamento());
			user.setCnabEnviado(aluno.getCnabEnviado());

			em.persist(user);

			if (user.getDataNascimento() != null) {
				Evento aniversario = eventoService.findByCodigo(user.getCodigo());

				if (aniversario == null) {
					aniversario = new Evento();
				}

				aniversario.setDataFim(finalizarAnoLetivo.mudarAno(user.getDataNascimento(), Constant.anoLetivoAtual));
				aniversario
						.setDataInicio(finalizarAnoLetivo.mudarAno(user.getDataNascimento(), Constant.anoLetivoAtual));
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

		List<Boleto> boletos = null;
		if (saveBrother) {
			salvarIrmaos(user, aluno);
			if (aluno.getId() == null || aluno.getId() == 0L) {
				boletos = gerarBoletos(user);
			}
		}
		if (aluno.getId() == null || aluno.getId() == 0L) {
			user.setBoletos(boletos);
		}

		return user;
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
		user.setValorMensal(aluno.getValorMensal());
		user.setDataMatricula(aluno.getDataMatricula());
		// user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());
		user.setCodigo(aluno.getCodigo());

		user.setNomeAvoHPaternoMae(aluno.getNomeAvoHPaternoMae());
		user.setAnuidade(aluno.getAnuidade() != null ? aluno.getAnuidade() : 0);
		user.setCpfMae(aluno.getCpfMae());
		user.setCpfPai(aluno.getCpfPai());
		user.setCpfResponsavel(aluno.getCpfResponsavel());
		user.setRgResponsavel(aluno.getRgResponsavel());
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
		user.setNomeResponsavel(aluno.getNomeResponsavel());
		user.setNumeroParcelas(aluno.getNumeroParcelas());
		user.setObservacaoSecretaria(aluno.getObservacaoSecretaria());
		user.setValorMensal(aluno.getValorMensal());
		user.setTelefoneResidencialPai(aluno.getTelefoneResidencialPai());
		user.setRgMae(aluno.getRgMae());
		user.setRgPai(aluno.getRgPai());
		user.setSenha(aluno.getSenha());
		user.setTelefone(aluno.getTelefone());
		user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());

		user.setAnuidade(aluno.getAnuidade());
		if (aluno.getBairro() != null) {
			user.setBairro(removeCaracteresEspeciais(aluno.getBairro().replace("ç", "c")));
		}
		user.setCep(aluno.getCep());
		if (aluno.getCidade() != null) {
			user.setCidade(removeCaracteresEspeciais(aluno.getCidade().replace("ç", "c")));
		}
		user.setCpfMae(aluno.getCpfMae());
		user.setCpfPai(aluno.getCpfPai());
		user.setEmergenciaLigarPara(aluno.getEmergenciaLigarPara());
		user.setEndereco(aluno.getEndereco());
		user.setNomecontatoSaidaEstabelecimento1(aluno.getNomecontatoSaidaEstabelecimento1());
		user.setNomecontatoSaidaEstabelecimento2(aluno.getNomecontatoSaidaEstabelecimento2());
		user.setNomecontatoSaidaEstabelecimento3(aluno.getNomecontatoSaidaEstabelecimento3());
		user.setRgResponsavel(aluno.getRgResponsavel());

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
		return "ok";
	}

	public String restaurar(Long idAluno) {
		Aluno al = findById(idAluno);
		al.setRemovido(false);
		al.setRestaurada(true);
		al.setCnabEnviado(false);
		em.persist(al);
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
				pt.setAnoLetivo(Constant.anoLetivoAtual);
				pt.setAluno(prof);
				pt.setTurma(em.find(Turma.class, turma.getId()));
				em.persist(pt);
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
		sql2.append("and  pt.turma.id = ");
		sql2.append(t.getId());
		sql2.append("and  pt.professor.especialidade = 0");
		Query query2 = em.createQuery(sql2.toString());
		return (Professor) query2.getResultList().get(0);

	}

	public float getNota(Long idAluno, DisciplinaEnum disciplina, BimestreEnum bimestre, boolean recupecacao) {
		try {
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
			return (List<Aluno>) q.getResultList();

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
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
			return (List<HistoricoAluno>) q.getResultList();

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

	}

	public void rematricularAlunoAntigo(Long id) {
		Aluno rematriculado = findById(id);
		rematriculado.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo());
		rematriculado.setPeriodoProximoAno(rematriculado.getPeriodo());
		rematriculado.setRemovido(false);
		em.merge(rematriculado);

	}

	public void rematricularCancelado(Long id) {
		Aluno rematriculado = findById(id);
		rematriculado.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo());
		rematriculado.setRestaurada(true);
		rematriculado.setRemovido(false);
		rematriculado.setCnabEnviado(false);
		em.merge(rematriculado);

	}

	public void desmatricular(Long id) {
		Aluno rematriculado = findById(id);
		rematriculado.setRematricular(false);
		em.merge(rematriculado);
	}

	public List<Boleto> gerarBoletos(Aluno user) {
		List<Boleto> boletos = new ArrayList<>();
		int i = 12 - user.getNumeroParcelas();
		long nossoNumero = getProximoNossoNumero();
		while (i < 12) {
			Boleto boleto = new Boleto();
			Calendar vencimento = Calendar.getInstance();
			vencimento.set(Calendar.MONTH, i);
			vencimento.set(Calendar.YEAR, Constant.anoLetivoAtual);
			vencimento.set(Calendar.HOUR, 0);
			vencimento.set(Calendar.MINUTE, 0);
			vencimento.set(Calendar.SECOND, 0);
			if (!user.isVencimentoUltimoDia()) {
				vencimento.set(Calendar.DAY_OF_MONTH, user.getDiaVencimento());
			} else {
				int dia = vencimento.getActualMaximum(Calendar.DAY_OF_MONTH);
				vencimento.set(Calendar.DAY_OF_MONTH, dia);
			}
			boleto.setVencimento(vencimento.getTime());
			boleto.setEmissao(new Date());
			boleto.setValorNominal(user.getValorMensal());
			boleto.setPagador(user);
			boleto.setNossoNumero(nossoNumero);
			em.persist(boleto);
			nossoNumero++;
			boletos.add(boleto);

			i++;
		}
		return boletos;
	}

	public void gerarBoletos() {
		List<Aluno> alunos = findAll();
		for (Aluno al : alunos) {
			if(al.getCodigo().equals("135")){
			}
			if (al.getRemovido() != null && !al.getRemovido()) {

				if (al.getBoletos() == null || al.getBoletos().size() == 0) {
					if (al.getNumeroParcelas() != null && al.getNumeroParcelas() > 0) {
						if ((al.getAnoLetivo() == Constant.anoLetivoAtual)
								|| (al.getRematricular() != null && al.getRematricular())) {
							if (!irmaoJaTemBoleto(al)) {
								List<Boleto> boletos = gerarBoletos(al);
								al.setBoletos(boletos);
								em.persist(al);
							}
						}
					}
				} else if (al.getBoletos() != null && al.getBoletos().size() > 0) {
					List<Boleto> boletos = al.getBoletos();
					if (todosBoletosBaixados(boletos) && al.getRestaurada() != null && al.getRestaurada()) {
						List<Boleto> boletosGErados = gerarBoletos(al);
						boletosGErados.addAll(al.getBoletos());
						al.setBoletos(boletosGErados);
							em.persist(al);
					} else {
						for (Boleto b : boletos) {
							if (b.getAlteracaoBoletoManual() == null || !b.getAlteracaoBoletoManual()) {
								b.setValorNominal(al.getValorMensal());
								Calendar c = Calendar.getInstance();
								c.setTime(b.getVencimento());
								if (!al.isVencimentoUltimoDia()) {
									c.set(Calendar.DAY_OF_MONTH, al.getDiaVencimento());
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

		}
	}

	private boolean irmaoJaTemBoleto(Aluno aluno) {
		Aluno irmao1 = aluno.getIrmao1();
		Aluno irmao2 = aluno.getIrmao2();
		Aluno irmao3 = aluno.getIrmao3();
		Aluno irmao4 = aluno.getIrmao4();

		if (irmao1 != null) {
			if (irmao1.getBoletos() != null && irmao1.getBoletos().size() > 0) {
				return true;
			}
		}

		if (irmao2 != null) {
			if (irmao2.getBoletos() != null && irmao2.getBoletos().size() > 0) {
				return true;
			}
		}

		if (irmao3 != null) {
			if (irmao3.getBoletos() != null && irmao3.getBoletos().size() > 0) {
				return true;
			}
		}

		if (irmao4 != null) {
			if (irmao4.getBoletos() != null && irmao4.getBoletos().size() > 0) {
				return true;
			}
		}

		return false;
	}

	public List<Boleto> gerarBoletos(Aluno user, boolean setUser) {
		user = findById(user.getId());
		List<Boleto> boletos = gerarBoletos(user);
		user.setBoletos(boletos);
		em.persist(user);
		return boletos;
	}

	private boolean todosBoletosBaixados(List<Boleto> boletos) {
		boolean todosBaixados = true;
		for (Boleto bol : boletos) {
			if( !(bol.getBaixaGerada() != null && bol.getBaixaGerada())){
				if(!(bol.getBaixaManual() != null && bol.getBaixaManual())){
					if(!(bol.getConciliacaoPorExtrato() != null && bol.getConciliacaoPorExtrato())){
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
		sql.append("SELECT max(al.codigo)  from Aluno al ");

		Query query = em.createQuery(sql.toString());
		String codigo = (String) query.getSingleResult();
		return Long.parseLong(codigo) + 1;
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
		sql.append("SELECT distinct(al) from  Boleto bol ");
		sql.append("left join bol.pagador al ");
		sql.append("where 1=2 ");
		if (nome != null && !nome.equalsIgnoreCase("")) {
			sql.append(" or al.nomeAluno like '%");
			sql.append(nome);
			sql.append("%' ");
		} else {

		}
		if (nomeResponsavel != null && !nomeResponsavel.equalsIgnoreCase("")) {
			sql.append(" or al.nomeResponsavel like '%");
			sql.append(nomeResponsavel);
			sql.append("%' ");
		}

		if (cpf != null && !cpf.equalsIgnoreCase("")) {
			sql.append(" or al.cpfResponsavel like '%");
			sql.append(cpf);
			sql.append("%' ");
		}

		if (numeroDocumento != null && !numeroDocumento.equalsIgnoreCase("")) {
			sql.append(" or bol.nossoNumero = ");
			sql.append(numeroDocumento);
		}

		Query query = em.createQuery(sql.toString());

		@SuppressWarnings("unchecked")
		List<Aluno> alunos = query.getResultList();
		for (Aluno al : alunos) {
			al.getBoletos().size();
		}

		return alunos;
	}

	public void salvarTodos() {
		List<Aluno> todos = findAll();
		for (Aluno a : todos) {
			save(a);
		}

	}

	public void removerCnabEnviado(Long id) {
		Aluno aluno = findById(id);
		aluno.setCnabEnviado(false);
		em.merge(aluno);
	}

	public void enviarCnab(Long id) {
		Aluno aluno = findById(id);
		aluno.setCnabEnviado(true);
		em.merge(aluno);
	}

	public void removerVerificadoOk(Long id) {
		Aluno aluno = findById(id);
		aluno.setVerificadoOk(false);
		em.merge(aluno);
	}

	public void verificadoOk(Long id) {
		Aluno aluno = findById(id);
		aluno.setVerificadoOk(true);
		em.merge(aluno);
	}

	public void removerBoleto(Long idBoleto) {
		Boleto b = findBoletoById(idBoleto);
		b.setManterAposRemovido(false);
		b.setValorPago((double) 0);
		b.setDataPagamento(new Date());
		em.merge(b);
	}
	
	public void manterBoleto(Long idBoleto) {
		Boleto b = findBoletoById(idBoleto);
		
		b.setManterAposRemovido(true);
		b.setCancelado(false);
		em.merge(b);
	}

	
	public void remover(Aluno aluno) {
		for(Boleto b : aluno.getBoletos()){
			if(b.getManterAposRemovido() != null && b.getManterAposRemovido()){
				manterBoleto(b.getId());
			}else{
				removerBoleto(b.getId());
			}
		}
		em.flush();
		remover(aluno.getId());
	}
	
}

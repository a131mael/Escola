
package org.escola.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import org.escola.enums.DisciplinaEnum;
import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.model.Aluno;
import org.escola.model.AlunoAula;
import org.escola.model.Aula;
import org.escola.util.Service;

@Stateless
public class AulaService extends Service {

	@Inject
	private Logger log;

	@Inject
	private AlunoService alunoService;

	@Inject
	private ConfiguracaoService configuracaoService;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public Aula findById(EntityManager em, Long id) {
		return em.find(Aula.class, id);
	}

	public Aula findById(Long id) {
		return em.find(Aula.class, id);
	}

	public AlunoAula findAlunoAulaById(Long id) {
		return em.find(AlunoAula.class, id);
	}

	public List<Aula> findAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aula> criteria = cb.createQuery(Aula.class);
			Root<Aula> member = criteria.from(Aula.class);
			// Swap criteria statements if you would like to try out type-safe
			// criteria queries, a new
			// feature in JPA 2.0
			// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
			criteria.select(member).orderBy(cb.asc(member.get("dataAula")));
			return em.createQuery(criteria).getResultList();

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<Aula> findAll(Serie serie, PerioddoEnum periodo) {
		List<Aula> alunos = new ArrayList<>();
		alunos.addAll(find(serie, periodo));
		if (periodo != null) {
			alunos.addAll(find(serie, PerioddoEnum.INTEGRAL));
		}
		return alunos;
	}

	public List<Aula> find(Serie serie, PerioddoEnum periodo) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aula> criteria = cb.createQuery(Aula.class);
			Root<Aula> member = criteria.from(Aula.class);

			Predicate whereSerie = null;
			Predicate wherePeriodo = null;

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
				criteria.select(member).where(whereSerie);
				break;

			case "B":
				criteria.select(member).where(wherePeriodo);
				break;

			case "AB":
				criteria.select(member).where(whereSerie, wherePeriodo);
				break;
			default:
				break;
			}

			criteria.select(member).orderBy(cb.asc(member.get("nome")));
			return em.createQuery(criteria).getResultList();

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public Aula save(Aula aluno, Long idProf) {
		Aula user = null;
		try {
			boolean editar = false;
			log.info("Registering " + aluno.getTitulo());

			if (aluno.getId() != null && aluno.getId() != 0L) {
				user = findById(aluno.getId());
				editar = true;
			} else {
				user = new Aula();
			}

			user.setDataAula(aluno.getDataAula());
			user.setDescricao(aluno.getDescricao());
			user.setId(aluno.getId());
			user.setLinkYoutube(aluno.getLinkYoutube());
			user.setSerie(aluno.getSerie());
			user.setTitulo(aluno.getTitulo());
			user.setDisciplina(aluno.getDisciplina());
			user.setOrdem(aluno.getOrdem());
			em.persist(user);
			criarAlunoAula(user);

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
		em.flush();
		return user;
	}

	private void criarAlunoAula(Aula aula) {
		Set<Aluno> alunos = new HashSet<>();
		alunos.addAll(alunoService.find(aula.getSerie(), PerioddoEnum.INTEGRAL));
		alunos.addAll(alunoService.find(aula.getSerie(), PerioddoEnum.MANHA));
		alunos.addAll(alunoService.find(aula.getSerie(), PerioddoEnum.TARDE));

		for (Aluno al : alunos) {
			createAlunoAula(al, aula);
		}
	}

	public void salvarAlunoAula(AlunoAula alau) {
		AlunoAula a = findAlunoAulaById(alau.getId());
		a.setAssistiu(true);
		em.persist(a);
		em.flush();
	}

	public void createAlunoAula(final Aluno al, final Aula aula) {
		// new Thread() {
		// @Override
		// public void run() {
		System.out.println(al.getNomeAluno());
		if (al.getNomeAluno().equalsIgnoreCase("MIGUEL DE SOUZA PINHEIRO")) {
			System.out.println("MIguel");
		}
		if (findAlunoAulaby(al.getId(), aula.getId()).size() == 0) {
			AlunoAula aa = new AlunoAula();
			aa.setAluno(al);
			aa.setAula(aula);
			aa.setAssistiu(false);
			em.persist(aa);
			em.flush();
		}
		/*
		 * }
		 * 
		 * }.start();
		 */
	}

	/*
	 * private void createAlunoAula(Aula avaliacao) { Set<Aluno> alunos = new
	 * HashSet<>();
	 * 
	 * StringBuilder sql = new StringBuilder(); sql.append(
	 * "SELECT at from  ProfessorTurma at "); sql.append(
	 * "where at.professor.id = ");
	 * sql.append(avaliacao.getProfessor().getId()); sql.append(
	 * " and at.turma.serie = "); sql.append(avaliacao.getSerie().ordinal());
	 * 
	 * Query query = em.createQuery(sql.toString());
	 * 
	 * try { List<ProfessorTurma> turmas = query.getResultList(); for
	 * (ProfessorTurma t : turmas) {
	 * alunos.addAll(alunoService.findAlunoTurmaBytTurma(t.getTurma().getId()));
	 * }
	 * 
	 * } catch (NoResultException noResultException) {
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * for (Aluno al : alunos) { createAlunoAula(al, avaliacao); } }
	 * 
	 * public void createAlunoAula(Aluno al, Aula avaliacao) { AlunoAula aa =
	 * new AlunoAula(); aa.setAluno(al); aa.setAula(avaliacao);
	 * aa.setAnoLetivo(al.getAnoLetivo()); em.persist(aa); em.flush(); }
	 * 
	 */
	public String remover(Long idAula) {
		Aula av = findById(idAula);
		List<AlunoAula> alavs = findAlunoAulaby(null, av.getId());
		for (AlunoAula alav : alavs) {
			em.remove(alav);
		}

		em.remove(av);
		return "ok";
	}

	public List<AlunoAula> findAlunoAulaby(Long idAluno, Long idAula) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  AlunoAula al ");
		sql.append("where 1=1 ");
		if (idAluno != null) {
			sql.append(" and al.aluno.id =   ");
			sql.append(idAluno);
		}
		if (idAula != null) {
			sql.append(" and al.aula.id =   ");
			sql.append(idAula);
		}

		Query query = em.createQuery(sql.toString());

		try {
			return (List<AlunoAula>) query.getResultList();

		} catch (NoResultException noResultException) {
			noResultException.printStackTrace();
			return new ArrayList<>();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return new ArrayList<>();

	}

	public List<AlunoAula> findAlunoAulaby(Long idAluno, String dataAula) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  AlunoAula al ");
		sql.append("where 1=1 ");
		if (idAluno != null) {
			sql.append(" and al.aluno.id =   ");
			sql.append(idAluno);
		}
		if (dataAula != null) {
			sql.append(" and al.aula.dataAula = '");
			sql.append(dataAula);
			sql.append("'");
		}

		sql.append(" order by al.aula.ordem");

		Query query = em.createQuery(sql.toString());

		try {
			return (List<AlunoAula>) query.getResultList();

		} catch (NoResultException noResultException) {
			noResultException.printStackTrace();
			return new ArrayList<>();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return new ArrayList<>();

	}

	@SuppressWarnings("unchecked")
	public List<Aula> find(int first, int size, String orderBy, String order, Map<String, Object> filtros) {
		try {
			Long t1 = System.currentTimeMillis();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aula> criteria = cb.createQuery(Aula.class);
			Root<Aula> member = criteria.from(Aula.class);
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
			Long t2 = System.currentTimeMillis();
			List<Aula> avas = (List<Aula>) q.getResultList();
			Long t3 = System.currentTimeMillis();
			System.out.println("Tempo execução query getavaliacaoLazy -------------- ");
			System.out.println("T1 =  " + (t2 - t1) / 1000);
			System.out.println("T2 =  " + (t3 - t2) / 1000);
			System.out.println("T3 =  " + (t3 - t1) / 1000);
			return avas;

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

	}

	@SuppressWarnings("unchecked")
	public List<AlunoAula> findAlunoAula(int first, int size, String orderBy, String order,
			Map<String, Object> filtros) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  AlunoAula al ");
		sql.append("where 1=1 ");

		if (filtros != null) {
			for (Map.Entry<String, Object> entry : filtros.entrySet()) {

				if (entry.getKey() == "aula.disciplina") {
					sql.append(" and al.aula.disciplina =   ");
					sql.append(((DisciplinaEnum) entry.getValue()).ordinal());
				}

				if (entry.getKey() == "aula.semana") {
					sql.append(" and al.aula.dataAula >= '");
					sql.append(getLimitInferiorSemana(((int) entry.getValue())));
					sql.append(" ' and al.aula.dataAula <= '");
					sql.append(getLimitSuperiorSemana(((int) entry.getValue())));
					sql.append(" ' ");
				}

				if (entry.getKey() == "aula.dataAula") {
					sql.append(" and al.aula.dataAula = '");
					sql.append((String) entry.getValue());
					sql.append("'");
				}

				if (entry.getKey() == "aula.visible") {
					sql.append(" and al.aula.visible =   ");
					sql.append((Boolean) entry.getValue());
				}

				if (entry.getKey() == "aluno") {
					sql.append(" and al.aluno.id =   ");
					sql.append(Long.parseLong(entry.getValue().toString()));
				}

			}

		}
		sql.append(" order by  al.aula.dataAula desc  ");

		Query query = em.createQuery(sql.toString());

		try {
			return (List<AlunoAula>) query.getResultList();

		} catch (NoResultException noResultException) {
			noResultException.printStackTrace();
			return new ArrayList<>();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object getLimitSuperiorSemana(int i) {
		String date = "";
		switch (i) {

		case 1:
			date = "2020-08-07";
			break;

		case 2:
			date = "2020-08-14";
			break;

		case 3:
			date = "2020-08-21";
			break;

		case 4:
			date = "2020-08-27";
			break;
		case 5:
			date = "2020-09-04";
			break;
		case 6:
			date = "2020-09-11";
			break;
		case 7:
			date = "2020-09-18";
			break;
		case 8:
			date = "2020-09-25";
			break;
		case 9:
			date = "2020-10-02";
			break;
		case 10:
			date = "2020-10-09";
			break;
		case 11:
			date = "2020-10-16";
			break;
		case 12:
			date = "2020-10-23";
			break;
		case 13:
			date = "2020-10-30";
			break;
		case 14:
			date = "2020-11-06";
			break;
		case 15:
			date = "2020-11-13";
			break;
		case 16:
			date = "2020-11-20";
			break;
		case 17:
			date = "2020-11-27";
			break;
		case 18:
			date = "2020-12-04";
			break;
		case 19:
			date = "2020-12-10";
			break;
		case 20:
			date = "2020-12-18";
			break;
		case 21:
			date = "2020-12-25";
			break;
		case 22:
			date = "2021-01-01";
			break;

		default:
			break;
		}
		return date;
	}

	private String getLimitInferiorSemana(int i) {
		String date = "";
		switch (i) {

		case 1:
			date = "2020-08-03";
			break;

		case 2:
			date = "2020-08-10";
			break;

		case 3:
			date = "2020-08-17";
			break;

		case 4:
			date = "2020-08-24";
			break;
		case 5:
			date = "2020-08-31";
			break;
		case 6:
			date = "2020-09-07";
			break;
		case 7:
			date = "2020-09-14";
			break;
		case 8:
			date = "2020-09-21";
			break;
		case 9:
			date = "2020-09-28";
			break;
		case 10:
			date = "2020-10-05";
			break;
		case 11:
			date = "2020-10-12";
			break;
		case 12:
			date = "2020-10-19";
			break;
		case 13:
			date = "2020-10-26";
			break;
		case 14:
			date = "2020-11-02";
			break;
		case 15:
			date = "2020-11-09";
			break;
		case 16:
			date = "2020-11-16";
			break;
		case 17:
			date = "2020-11-23";
			break;
		case 18:
			date = "2020-11-30";
			break;
		case 19:
			date = "2020-12-07";
			break;
		case 20:
			date = "2020-12-14";
			break;
		case 21:
			date = "2020-12-21";
			break;
		case 22:
			date = "2020-12-28";
			break;

		default:
			break;
		}
		return date;
	}

	@SuppressWarnings("unchecked")
	public List<AlunoAula> findAlunoAula2(int first, int size, String orderBy, String order,
			Map<String, Object> filtros) {
		try {
			Long t1 = System.currentTimeMillis();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<AlunoAula> criteria = cb.createQuery(AlunoAula.class);
			Root<AlunoAula> member = criteria.from(AlunoAula.class);
			CriteriaQuery cq = criteria.select(member);

			final List<Predicate> predicates = new ArrayList<Predicate>();
			for (Map.Entry<String, Object> entry : filtros.entrySet()) {

				Predicate pred = cb.and();
				if (entry.getKey() == "aula.disciplina") {
					pred = cb.equal(member.get("aula").get("disciplina"), entry.getValue());
				} else if (entry.getValue() instanceof String) {
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
			List<AlunoAula> avas = (List<AlunoAula>) q.getResultList();
			return avas;

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
			Root<Aula> member = countQuery.from(Aula.class);
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

	public long countAlunoAula(Map<String, Object> filtros) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(al.id) from  AlunoAula al ");
		sql.append("where 1=1 ");
		if (filtros != null) {
			for (Map.Entry<String, Object> entry : filtros.entrySet()) {

				if (entry.getKey() == "aula.disciplina") {
					sql.append(" and al.aula.disciplina =   ");
					sql.append(((DisciplinaEnum) entry.getValue()).ordinal());
				}

				if (entry.getKey() == "aula.assistiu") {
					sql.append(" and al.assistiu =  true ");
				}

				if (entry.getKey() == "aula.visible") {
					sql.append(" and al.aula.visible =   ");
					sql.append((Boolean) entry.getValue());
				}

				if (entry.getKey() == "aluno") {
					sql.append(" and al.aluno.id =   ");
					sql.append(Long.parseLong(entry.getValue().toString()));
				}

			}

		}

		Query query = em.createQuery(sql.toString());
		Long m = (Long) query.getSingleResult();

		return m;

	}

	public long countAlunoAula2(Map<String, Object> filtros) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			Root<AlunoAula> member = countQuery.from(AlunoAula.class);
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

	public AlunoAula getPrimeiraAula(DisciplinaEnum disciplina, String idAluno) {
		Date hj = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String hjS = dateFormat.format(hj);

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  AlunoAula al ");
		sql.append("where 1=1 ");
		sql.append(" and al.aula.disciplina =   ");
		sql.append(disciplina.ordinal());
		sql.append(" and al.assistiu =  false ");
		sql.append(" and al.aluno.id =   ");
		sql.append(Long.parseLong(idAluno));
		sql.append(" and al.aula.dataAula <= '");
		sql.append(hjS);
		sql.append("'");
		sql.append(" order by al.aula.dataAula, al.aula.ordem  ");

		Query query = em.createQuery(sql.toString());
		List<AlunoAula> m = (List<AlunoAula>) query.getResultList();
		AlunoAula a = null;
		if (m != null) {
			a = m.get(0);
		}
		return a;
	}

	public void corrigirAulasDuplicadas() {

		List<Aula> all = findAll();
		int tamanhoBloco = 5;
		;
		int iteradorAtual = 0;

		while (iteradorAtual <= all.size()) {

			for (int i = iteradorAtual; i < tamanhoBloco + iteradorAtual; i++) {
				try {
					save(all.get(i), null);

				} catch (Exception e) {

				}
			}

			iteradorAtual++;
			iteradorAtual += tamanhoBloco;

		}
	}

	public void habilitarAulas() {
		Date dataTeste = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataTeste);
		cal.add(Calendar.DATE, 1);
		dataTeste = cal.getTime();

		StringBuilder sql = new StringBuilder();
		sql.append("update Aula set visible  = true ");
		sql.append("where 1=1 ");
		sql.append(" and dataAula <= ' ");
		sql.append(dataTeste);
		sql.append("'");
		Query query = em.createQuery(sql.toString());
		long updates = query.executeUpdate();
		System.out.println("Aulas atualizadas = " + updates);
	}

	public AlunoAula getPrimeiraAula(int semana, String idAluno) {
		Date hj = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String hjS = dateFormat.format(hj);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  al from  AlunoAula al ");
		sql.append("where 1=1 ");
		sql.append(" and al.aula.dataAula >= '");
		sql.append(getLimitInferiorSemana(semana));
		sql.append(" ' and al.aula.dataAula <= '");
		sql.append(getLimitSuperiorSemana(semana));
		sql.append(" ' ");

		sql.append(" and al.aula.dataAula <= '");
		sql.append(hjS);
		sql.append("'");

		sql.append(" and al.aluno.id =   ");
		sql.append(Long.parseLong(idAluno));

		sql.append(" order by al.aula.ordem  ");

		Query query = em.createQuery(sql.toString());
		List<AlunoAula> m = (List<AlunoAula>) query.getResultList();
		AlunoAula a = null;
		if (m != null) {
			a = m.get(0);
		}
		return a;
	}

	public AlunoAula getPrimeiraAulaNaoAssistida(int semana, String idAluno) {
		Date hj = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String hjS = dateFormat.format(hj);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  al from  AlunoAula al ");
		sql.append("where 1=1 ");
		sql.append(" and al.aula.dataAula >= '");
		sql.append(getLimitInferiorSemana(semana));
		sql.append(" ' and al.aula.dataAula <= '");
		sql.append(getLimitSuperiorSemana(semana));
		sql.append(" ' ");

		sql.append(" and al.aula.dataAula <= '");
		sql.append(hjS);
		sql.append("'");

		sql.append(" and al.aluno.id =   ");
		sql.append(Long.parseLong(idAluno));

		sql.append(" and al.assistiu = false  ");

		sql.append(" order by al.aula.dataAula, al.aula.ordem  ");

		Query query = em.createQuery(sql.toString());
		List<AlunoAula> m = new ArrayList<>();
		AlunoAula a = null;
		try {
			m = (List<AlunoAula>) query.getResultList();
			if (m != null) {
				a = m.get(0);
			}
		} catch (Exception e) {
			a = getPrimeiraAula(semana, idAluno);
		}
		return a;
	}

	public AlunoAula getPrimeiraAulaNaoAssistidaDodia(int semana, String idAluno) {
		Date hj = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String hjS = dateFormat.format(hj);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  al from  AlunoAula al ");
		sql.append("where 1=1 ");
		sql.append(" and al.aula.dataAula >= '");
		sql.append(getLimitInferiorSemana(semana));
		sql.append(" ' and al.aula.dataAula <= '");
		sql.append(getLimitSuperiorSemana(semana));
		sql.append(" ' ");

		sql.append(" and al.aula.dataAula <= '");
		sql.append(hjS);
		sql.append("'");

		sql.append(" and al.aluno.id =   ");
		sql.append(Long.parseLong(idAluno));

		sql.append(" and al.assistiu = false  ");

		sql.append(" order by al.aula.ordem  ");

		Query query = em.createQuery(sql.toString());
		List<AlunoAula> m = new ArrayList<>();
		AlunoAula a = null;
		try {
			m = (List<AlunoAula>) query.getResultList();
			if (m != null) {
				a = m.get(0);
			}
		} catch (Exception e) {
			a = getPrimeiraAula(semana, idAluno);
		}
		return a;
	}

	public AlunoAula getPrimeiraAula(String dataAula, String idAluno) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  AlunoAula al ");
		sql.append("where 1=1 ");
		sql.append(" and al.aula.dataAula = '");
		sql.append(dataAula);
		sql.append("'");

		sql.append(" and al.aluno.id =   ");
		sql.append(Long.parseLong(idAluno));

		sql.append(" order by  al.aula.ordem ");

		Query query = em.createQuery(sql.toString());
		List<AlunoAula> m = (List<AlunoAula>) query.getResultList();
		AlunoAula a = null;
		if (m != null) {
			a = m.get(0);
		}
		return a;
	}

	public AlunoAula getPrimeiraAulaNaoAssistida(String dataAula, String idAluno) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  AlunoAula al ");
		sql.append("where 1=1 ");
		sql.append(" and al.aula.dataAula = '");
		sql.append(dataAula);
		sql.append("'");

		sql.append(" and al.aluno.id =   ");
		sql.append(Long.parseLong(idAluno));

		sql.append(" and al.assistiu = false");

		sql.append(" order by  al.aula.ordem ");

		Query query = em.createQuery(sql.toString());
		List<AlunoAula> m = new ArrayList<>();
		AlunoAula a = null;
		try {
			m = (List<AlunoAula>) query.getResultList();
			if (m != null) {
				a = m.get(0);
			}
		} catch (Exception e) {
			a = getPrimeiraAula(dataAula, idAluno);
		}

		return a;
	}

	public AlunoAula getUltimaAula(String dataAula, String idAluno) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  AlunoAula al ");
		sql.append("where 1=1 ");
		sql.append(" and al.aula.dataAula = '");
		sql.append(dataAula);
		sql.append("'");

		sql.append(" and al.aluno.id =   ");
		sql.append(Long.parseLong(idAluno));

		sql.append(" order by  al.aula.ordem ");

		Query query = em.createQuery(sql.toString());
		List<AlunoAula> m = (List<AlunoAula>) query.getResultList();
		AlunoAula a = null;
		if (m != null) {
			a = m.get(m.size() - 1);
		}
		return a;
	}

	public AlunoAula getProximaAula(String dataAula, Long idAluno, Integer ordem) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al from  AlunoAula al ");
		sql.append("where 1=1 ");
		sql.append(" and al.aula.dataAula = '");
		sql.append(dataAula);
		sql.append("'");

		sql.append(" and al.aluno.id =   ");
		sql.append(idAluno);

		sql.append(" order by  al.aula.ordem ");

		Query query = em.createQuery(sql.toString());
		List<AlunoAula> m = (List<AlunoAula>) query.getResultList();
		AlunoAula a = null;
		if (m != null) {
			for (AlunoAula aa : m) {
				if (aa.getAula().getOrdem() != null) {
					if (aa.getAula().getOrdem().compareTo(ordem) == 1) {
						a = aa;
						break;
					}
				}
			}
		}
		return a;
	}

	public Date getDataUltimaAulaAssistida(Map<String, Object> filtros) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT al.aula.dataAula from  AlunoAula al ");
		sql.append("where 1=1 ");
		if (filtros != null) {
			for (Map.Entry<String, Object> entry : filtros.entrySet()) {

				if (entry.getKey() == "aula.disciplina") {
					sql.append(" and al.aula.disciplina =   ");
					sql.append(((DisciplinaEnum) entry.getValue()).ordinal());
				}

				if (entry.getKey() == "aula.assistiu") {
					sql.append(" and al.assistiu =  true ");
				}

				if (entry.getKey() == "aula.visible") {
					sql.append(" and al.aula.visible =   ");
					sql.append((Boolean) entry.getValue());
				}

				if (entry.getKey() == "aluno") {
					sql.append(" and al.aluno.id =   ");
					sql.append(Long.parseLong(entry.getValue().toString()));
				}
			}

		}
		sql.append(" order by al.aula.dataAula desc");

		Query query = em.createQuery(sql.toString());
		List<Date> dts = null;
		Date m = null;
		try {
			dts = ((List<Date>) query.getResultList());
			m = dts.get(0);
		} catch (Exception e) {
			System.out.println(e);
		}

		return m;
	}

	public AlunoAula  getPrimeiraAulaNaoAssistida(DisciplinaEnum disciplina, String idAluno) {
		Date hj = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String hjS = dateFormat.format(hj);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  al from  AlunoAula al ");
		sql.append("where 1=1 ");
		sql.append(" and al.aula.disciplina = ");
		sql.append(disciplina.ordinal());
		
		sql.append(" and al.aula.dataAula <= '");
		sql.append(hjS);
		sql.append("'");

		sql.append(" and al.aluno.id =   ");
		sql.append(Long.parseLong(idAluno));

		sql.append(" and al.assistiu = false  ");

		sql.append(" order by al.aula.dataAula, al.aula.ordem  ");

		Query query = em.createQuery(sql.toString());
		List<AlunoAula> m = new ArrayList<>();
		AlunoAula a = null;
		try {
			m = (List<AlunoAula>) query.getResultList();
			if (m != null) {
				a = m.get(0);
			}
		} catch (Exception e) {
			a = getPrimeiraAula(disciplina, idAluno);
		}
		return a;
	}

	/*
	 * public Usuario findMaiorPontuadorSemana() { StringBuilder sql = new
	 * StringBuilder(); sql.append("SELECT u from  Usuario u "); sql.append(
	 * "where pontosSemana =  (SELECT MAX(pontosSemana) FROM Usuario) ");
	 * 
	 * Query query = em.createQuery(sql.toString()); return (Usuario)
	 * query.getResultList().get(0); }
	 * 
	 * public Usuario findMaiorPontuadorMes() { StringBuilder sql = new
	 * StringBuilder(); sql.append("SELECT u from  Usuario u "); sql.append(
	 * "where pontosMes =  (SELECT MAX(pontosMes) FROM Usuario) ");
	 * 
	 * Query query = em.createQuery(sql.toString()); return (Usuario)
	 * query.getResultList().get(0);
	 * 
	 * }
	 * 
	 * public Usuario findMaiorPontuadorGeral() { StringBuilder sql = new
	 * StringBuilder(); sql.append("SELECT u from  Usuario u "); sql.append(
	 * "where pontosGeral =  (SELECT MAX(pontosGeral) FROM Usuario) ");
	 * 
	 * Query query = em.createQuery(sql.toString()); return (Usuario)
	 * query.getResultList().get(0); }
	 */
}

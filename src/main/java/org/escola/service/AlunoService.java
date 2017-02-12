
package org.escola.service;

import java.util.ArrayList;
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
import org.escola.model.AlunoTurma;
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

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public Aluno findById(EntityManager em, Long id) {
		return em.find(Aluno.class, id);
	}

	public Aluno findById(Long id) {
		return em.find(Aluno.class, id);
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

	@SuppressWarnings("unchecked")
	public List<Aluno> findAlunoTurmaBytTurma(List<Turma> turmas) {
		List<Aluno> alunos = new ArrayList<>();

		for (Turma turma : turmas) {
			alunos.addAll(findAlunoTurmaBytTurma(turma.getId()));
		}
		return alunos;

	}

	public Aluno save(Aluno aluno) {
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
			user.setNomeAluno(aluno.getNomeAluno());
			user.setPeriodo(aluno.getPeriodo());
			user.setSerie(aluno.getSerie());
			user.setEndereco(aluno.getEndereco());
			user.setBairro(aluno.getBairro());
			user.setCep(aluno.getCep());
			user.setCidade(aluno.getCidade());
			user.setNacionalidade(aluno.getNacionalidade());
			user.setValorMensal(aluno.getValorMensal());
			user.setDataNascimento(aluno.getDataNascimento());
			user.setDataMatricula(aluno.getDataMatricula());
			user.setAdministrarParacetamol(aluno.isAdministrarParacetamol());
			user.setCodigo(aluno.getCodigo());
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

		return user;
	}

	public String remover(Long idAluno) {
		Aluno al = findById(idAluno); 
		al.setRemovido(true);
		em.persist(al);
		return "ok";
	}
	
	public String restaurar(Long idAluno) {
		Aluno al = findById(idAluno); 
		al.setRemovido(false);
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
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT avg(av.nota) from  AlunoAvaliacao av ");
		sql.append("where 1 = 1");
		sql.append(" and  av.aluno.id = ");
		sql.append(idAluno);
		sql.append(" and  av.avaliacao.disciplina = ");
		sql.append(disciplina.ordinal());
		if(bimestre != null){
			sql.append(" and  av.avaliacao.bimestre = ");
			sql.append(bimestre.ordinal());	
		}
		sql.append(" and  av.avaliacao.recuperacao = ");
		sql.append(recupecacao);
		Query query = em.createQuery(sql.toString());

		Object valor = query.getSingleResult();
		if (valor != null) {
			valor = (double) valor;
		} else {
			return 0;
		}

		return Float.parseFloat(String.valueOf(valor));
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
				//cq.where(pred);
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

			if (filtros != null) {
				for (Map.Entry<String, Object> entry : filtros.entrySet()) {

					Predicate pred = cb.and();
					if (entry.getValue() instanceof String) {
						pred = cb.and(pred, cb.like(member.<String> get(entry.getKey()), "%" + entry.getValue() + "%"));
					} else {
						pred = cb.equal(member.get(entry.getKey()), entry.getValue());
					}
					countQuery.where(pred);
				}

			}

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
			return ((long) q.getSingleResult())>0?true:false;

		} catch (NoResultException nre) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}

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
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.escola.enums.Serie;
import org.escola.model.ProfessorTurma;
import org.escola.model.Turma;
import org.escola.util.Service;


@Stateless
public class TurmaService extends Service {

	@Inject
	private Logger log;
	
	@Inject
	private AlunoService alunoService;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public Turma findById(EntityManager em, Long id) {
		return em.find(Turma.class, id);
	}

	public Turma findById(Long id) {
		return em.find(Turma.class, id);
	}

	public List<Turma> findAll() {
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Turma> criteria = cb.createQuery(Turma.class);
			Root<Turma> member = criteria.from(Turma.class);
			// Swap criteria statements if you would like to try out type-safe
			// criteria queries, a new
			// feature in JPA 2.0
			// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
			criteria.select(member).orderBy(cb.asc(member.get("id")));
			List<Turma> turmas = new ArrayList<>();
			for(Turma t : em.createQuery(criteria).getResultList()){
				t.setTotalAlunos(alunoService.findAlunoTurmaBytTurma(t.getId()).size());
				turmas.add(t);
			}
			
			return turmas;
	
		}catch(NoResultException nre){
			return new ArrayList<>();
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public Turma save(Turma professor) {
		Turma user = null;
		try {

			log.info("Registering " + professor.getNome());
		
			if (professor.getId() != null && professor.getId() != 0L) {
				user = findById(professor.getId());
			} else {
				user = new Turma();
			}
			
			user.setNome(professor.getNome());
			user.setSerie(professor.getSerie());
			user.setPeriodo(professor.getPeriodo());
			em.persist(user);

			
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

	public String remove(Long idTurma) {
		/**CASCADE*/
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pt from  ProfessorTurma pt ");
		sql.append("where pt.turma.id =   ");
		sql.append(idTurma);

		Query query = em.createQuery(sql.toString());
		
		 
		try{
			List<ProfessorTurma> professorTurmas = query.getResultList();
			for(ProfessorTurma profT : professorTurmas){
				em.remove(profT);
			}
		
			em.remove(findById(idTurma));
			
		}catch(NoResultException noResultException){
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return "ok";
	}

	public List<Turma> findAll(Long idProfessor) {
		List<Turma> turmasDoProfessor = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pt from  ProfessorTurma pt ");
		sql.append("where pt.professor.id =   ");
		sql.append(idProfessor);
		Query query = em.createQuery(sql.toString());
		
		 
		try{
			List<ProfessorTurma> professorTurmas = query.getResultList();
			for(ProfessorTurma profT : professorTurmas){
				Turma t = profT.getTurma();
				t.setTotalAlunos(alunoService.findAlunoTurmaBytTurma(t.getId()).size());
				turmasDoProfessor.add(t);
				
			}
		
		}catch(NoResultException noResultException){
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return turmasDoProfessor;
	}
	
	public List<Serie> findAllSeries(Long idProfessor) {
		List<Turma> turmasDoProfessor = new ArrayList<>();
		List<Serie> series = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pt from  ProfessorTurma pt ");
		sql.append("where pt.professor.id =   ");
		sql.append(idProfessor);
		Query query = em.createQuery(sql.toString());
		
		 
		try{
			List<ProfessorTurma> professorTurmas = query.getResultList();
			for(ProfessorTurma profT : professorTurmas){
				Turma t = profT.getTurma();
				turmasDoProfessor.add(t);
			}
			
			for(Turma turma : turmasDoProfessor){
				if(series.size()>0){
					if(!series.contains(turma.getSerie())){
						series.add(turma.getSerie());
					}
				}else{
					series.add(turma.getSerie());
				}
			}
		
		}catch(NoResultException noResultException){
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return series;
	}

/*	public Usuario findMaiorPontuadorSemana() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u from  Usuario u ");
		sql.append("where pontosSemana =  (SELECT MAX(pontosSemana) FROM Usuario) ");

		Query query = em.createQuery(sql.toString());
		return (Usuario) query.getResultList().get(0);
	}

	public Usuario findMaiorPontuadorMes() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u from  Usuario u ");
		sql.append("where pontosMes =  (SELECT MAX(pontosMes) FROM Usuario) ");

		Query query = em.createQuery(sql.toString());
		return (Usuario) query.getResultList().get(0);

	}

	public Usuario findMaiorPontuadorGeral() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u from  Usuario u ");
		sql.append("where pontosGeral =  (SELECT MAX(pontosGeral) FROM Usuario) ");

		Query query = em.createQuery(sql.toString());
		return (Usuario) query.getResultList().get(0);
	}
*/
}

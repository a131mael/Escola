
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

import org.escola.enums.TipoMembro;
import org.escola.model.Evento;
import org.escola.model.Member;
import org.escola.model.Professor;
import org.escola.model.ProfessorTurma;
import org.escola.model.Turma;
import org.escola.util.Constant;
import org.escola.util.Service;
import org.escola.util.UtilFinalizarAnoLetivo;

@Stateless
public class ProfessorService extends Service {

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;
	
	@Inject
	private EventoService eventoService;
	
	@Inject
	private MemberRegistration memberRegistration;
	
	@Inject
	private UtilFinalizarAnoLetivo finalizarAnoLetivo;

	public Professor findById(EntityManager em, Long id) {
		return em.find(Professor.class, id);
	}

	public Professor findById(Long id) {
		return em.find(Professor.class, id);
	}
	
	public String remover(Long idTurma){
		em.remove(findById(idTurma));
		return "index";
	}

	public List<Professor> findAll() {
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Professor> criteria = cb.createQuery(Professor.class);
			Root<Professor> member = criteria.from(Professor.class);
			// Swap criteria statements if you would like to try out type-safe
			// criteria queries, a new
			// feature in JPA 2.0
			// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
			criteria.select(member).orderBy(cb.asc(member.get("id")));
			return em.createQuery(criteria).getResultList();
	
		}catch(NoResultException nre){
			return new ArrayList<>();
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public Professor save(Professor professor) {
		Professor user = null;
		try {

			log.info("Registering " + professor.getNome());
		
			if (professor.getId() != null && professor.getId() != 0L) {
				user = findById(professor.getId());
			} else {
				user = new Professor();
			}
			
			user.setCodigo(professor.getCodigo());
			user.setCpf(professor.getCpf());
			user.setEmail(professor.getEmail());
			user.setEndereco(professor.getEndereco());
			user.setInicio(professor.getInicio());
			user.setNascimento(professor.getNascimento());
			user.setNome(professor.getNome());
			user.setRg(professor.getRg());
			user.setSalario(professor.getSalario());
			user.setTelefone1(professor.getTelefone1());
			user.setTelefone2(professor.getTelefone2());
			user.setTelefone3(professor.getTelefone3());
			user.setTipoMembro(professor.getTipoMembro());
			user.setEspecialidade(professor.getEspecialidade());
			user.setLogin(professor.getLogin());
			user.setSenha(professor.getSenha());
			
			em.persist(user);
			Member m = null;
			if(user.getMember() != null && user.getMember().getId() != null){
				m = memberRegistration.findById(user.getMember().getId());
			}else{
				m = new Member();	
			}
			
			m.setLogin(professor.getLogin());
			m.setSenha(professor.getSenha());
			m.setName(professor.getNome());
			m.setTipoMembro(TipoMembro.PROFESSOR);
			em.persist(m);
			
			user.setMember(m);
			m.setProfessor(user);
			
			if(professor.getNascimento() != null){
				Evento aniversario = eventoService.findByCodigo(professor.getCodigo());
				if(aniversario == null){
					aniversario = new Evento();
					
				}
				aniversario.setCodigo(professor.getCodigo());
				aniversario.setDescricao(" Aniversário do(a) professor(a) " + user.getNome());		
				aniversario.setNome(" Aniversário do(a) professor(a) " + user.getNome());
				aniversario.setDataFim(finalizarAnoLetivo.mudarAno(professor.getNascimento(), Constant.anoLetivoAtual));
				aniversario.setDataInicio(finalizarAnoLetivo.mudarAno(professor.getNascimento(), Constant.anoLetivoAtual));
				em.persist(aniversario);	
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

	@SuppressWarnings("unchecked")
	public List<Professor> findProfessorTurmaBytTurma(long idTurma) {
		List<Professor> professors = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pt from  ProfessorTurma pt ");
		sql.append("where pt.turma.id =   ");
		sql.append(idTurma);

		Query query = em.createQuery(sql.toString());
		
		 
		try{
			List<ProfessorTurma> professorTurmas = query.getResultList();
			for(ProfessorTurma profT : professorTurmas){
				Professor pro = profT.getProfessor();
				professors.add(pro);
			}
			
		}catch(NoResultException noResultException){
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return professors;
	}

	@SuppressWarnings("unchecked")
	public List<Turma> findTurmaByProfessor(long idProfessor) {
		List<Turma> turmas = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pt from  ProfessorTurma pt ");
		sql.append("where pt.professor.id =   ");
		sql.append(idProfessor);

		Query query = em.createQuery(sql.toString());
		
		 
		try{
			List<ProfessorTurma> professorTurmas = query.getResultList();
			for(ProfessorTurma profT : professorTurmas){
				Turma pro = profT.getTurma();
				turmas.add(pro);
			}
			
		}catch(NoResultException noResultException){
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return turmas;

	}

	
	public void saveProfessorTurma(List<Professor> target, Turma turma) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pt from  ProfessorTurma pt ");
		sql.append("where pt.turma.id =   ");
		sql.append(turma.getId());

		Query query = em.createQuery(sql.toString());
		
		 
		try{
			List<ProfessorTurma> professorTurmas = query.getResultList();
			for(ProfessorTurma profT :professorTurmas){
				em.remove(profT);
				em.flush();
			}

			for(Professor prof : target){
				ProfessorTurma pt = new ProfessorTurma();
				pt.setProfessor(prof);
				pt.setTurma(em.find(Turma.class, turma.getId()));
				em.persist(pt);
			}
			
		}catch(NoResultException noResultException){
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
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

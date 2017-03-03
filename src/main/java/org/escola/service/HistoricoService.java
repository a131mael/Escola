
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.escola.model.Evento;
import org.escola.model.HistoricoAluno;
import org.escola.util.Service;


@Stateless
public class HistoricoService extends Service {

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public HistoricoAluno findById(EntityManager em, Long id) {
		return em.find(HistoricoAluno.class, id);
	}

	public HistoricoAluno findById(Long id) {
		return em.find(HistoricoAluno.class, id);
	}
	
	public HistoricoAluno findByCodigo(Long id) {
		return em.find(HistoricoAluno.class, id);
	}
	
	public String remover(Long idEvento){
		em.remove(findById(idEvento));
		return "index";
	}

	public List<HistoricoAluno> findAll() {
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<HistoricoAluno> criteria = cb.createQuery(HistoricoAluno.class);
			Root<HistoricoAluno> member = criteria.from(HistoricoAluno.class);
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

	public HistoricoAluno save(HistoricoAluno evento) {
		HistoricoAluno user = null;
		try {

			log.info("Registering " + evento.getAno());
		
			if (evento.getId() != null && evento.getId() != 0L) {
				user = findById(evento.getId());
			} else {
				user = new HistoricoAluno();
			}
			
			/*user.setDataFim(evento.getDataFim());
			user.setDataInicio(evento.getDataInicio());
			user.setDescricao(evento.getDescricao());
			user.setNome(evento.getNome());
			user.setCodigo(evento.getCodigo());*/
			
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

	public HistoricoAluno findByAluno(Long idAluno) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<HistoricoAluno> criteria = cb.createQuery(HistoricoAluno.class);
			Root<HistoricoAluno> member = criteria.from(HistoricoAluno.class);

			Predicate whereSerie = null;

			whereSerie = cb.equal(member.get("aluno").get("id"), idAluno);
			criteria.select(member).where(whereSerie);

			criteria.select(member);
			return em.createQuery(criteria).getSingleResult();

		} catch (NoResultException nre) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}


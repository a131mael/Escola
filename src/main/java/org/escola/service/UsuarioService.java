package org.escola.service;

import java.io.Serializable;
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

import org.escola.enums.Serie;
import org.escola.enums.TipoDestinatario;
import org.escola.model.Member;
import org.escola.model.Professor;
import org.escola.util.Service;

@Stateless
public class UsuarioService extends Service implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public Member findById(EntityManager em, Long id) {
		return em.find(Member.class, id);
	}

	public Member findById(Long id) {
		return em.find(Member.class, id);
	}

	public Professor findProfessorById(Long idProfessor) {
		return em.find(Professor.class, idProfessor);
	}

	public String remover(Long idTurma) {
		em.remove(findById(idTurma));
		em.flush();
		return "index";
	}

	public List<Member> findAllWithToken(TipoDestinatario tipoMembro) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT m from  Member m ");
		sql.append("where m.tokenFCM is not null ");
		if(!tipoMembro.equals(TipoDestinatario.TODOS)){
			sql.append(" and m.tipoMembro = ");
			sql.append(tipoMembro.ordinal());
		}
		
		Query query = em.createQuery(sql.toString());

		Object sqlReturn = query.getResultList();
		if (sqlReturn != null) {
			return (List<Member>) sqlReturn;
		} else {
			return null;
		}

	}

	public List<Member> findAllWithToken(Serie serie) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT m from  Member m ");
		sql.append("where m.tokenFCM is not null ");
		sql.append(" and m.alunoserie = ");
		sql.append(serie.ordinal());
		
		Query query = em.createQuery(sql.toString());

		Object sqlReturn = query.getResultList();
		if (sqlReturn != null) {
			return (List<Member>) sqlReturn;
		} else {
			return null;
		}

	}

	
	public List<Member> findAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
			Root<Member> member = criteria.from(Member.class);
			// Swap criteria statements if you would like to try out type-safe
			// criteria queries, a new
			// feature in JPA 2.0
			// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
			criteria.select(member).orderBy(cb.asc(member.get("id")));
			return em.createQuery(criteria).getResultList();

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public Member save(Member member) {
		Member user = null;
		try {

			log.info("Registering " + member.getName());

			if (member.getId() != null && member.getId() != 0L) {
				user = findById(member.getId());
				if (user != null && user.getProfessor() != null) {
					user.setProfessor(findProfessorById(user.getProfessor().getId()));
				}
			} else {
				user = new Member();
			}

			user.setEmail(member.getEmail());
			user.setLogin(member.getLogin());
			user.setName(member.getName());
			user.setSenha(member.getSenha());
			user.setTipoMembro(member.getTipoMembro());

			em.flush();
			em.persist(user);

		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());

			e.printStackTrace();
		}

		return user;
	}

}

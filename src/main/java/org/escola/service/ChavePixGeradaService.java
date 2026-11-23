package org.escola.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.escola.model.ChavePixGerada;
import org.escola.util.Service;

@Stateless
public class ChavePixGeradaService extends Service {

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public List<ChavePixGerada> findAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ChavePixGerada> criteria = cb.createQuery(ChavePixGerada.class);
			Root<ChavePixGerada> root = criteria.from(ChavePixGerada.class);
			criteria.select(root).orderBy(cb.desc(root.get("id")));
			return em.createQuery(criteria).getResultList();
		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public ChavePixGerada salvar(ChavePixGerada chave) {
		em.persist(chave);
		em.flush();
		return chave;
	}

	public void remover(Long id) {
		ChavePixGerada chave = em.find(ChavePixGerada.class, id);
		if (chave != null) {
			em.remove(chave);
			em.flush();
		}
	}

}

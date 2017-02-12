
package org.escola.service;

import java.util.ArrayList;
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

import org.escola.model.Aluno;
import org.escola.util.Service;


@Stateless
public class RelatorioService extends Service {

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;


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
				countQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

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

}


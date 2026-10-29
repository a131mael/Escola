package org.escola.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.escola.model.EventoArrecadacao;
import org.escola.util.Service;

@Stateless
public class EventoArrecadacaoService extends Service {

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	@Inject
	private ConfiguracaoService configuracaoService;

	public EventoArrecadacao findById(Long id) {
		return em.find(EventoArrecadacao.class, id);
	}

	public List<EventoArrecadacao> findAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventoArrecadacao> criteria = cb.createQuery(EventoArrecadacao.class);
			Root<EventoArrecadacao> root = criteria.from(EventoArrecadacao.class);
			criteria.select(root).orderBy(cb.desc(root.get("id")));
			return em.createQuery(criteria).getResultList();
		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public EventoArrecadacao save(EventoArrecadacao evento) {
		if (evento.getId() == null) {
			if (evento.getAnoLetivo() == 0) {
				evento.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo());
			}
			em.persist(evento);
			return evento;
		}
		return em.merge(evento);
	}

	public void remover(Long id) {
		EventoArrecadacao evento = findById(id);
		if (evento != null) {
			em.remove(evento);
		}
	}

}

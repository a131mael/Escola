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

	public double getTotalNotasEmitidas(int mes){
		try{
			StringBuilder sql = new StringBuilder();
			sql.append(" select sum(valorPago) from boleto ");
			sql.append("where ");
			sql.append("nfsEnviada = true ");
			sql.append("and vencimento >  ");
			sql.append(getInicioMes(mes));
			sql.append(" and vencimento < ");
			sql.append(getFimMes(mes));
			
			Query query = em.createNativeQuery(sql.toString());
			Double t = (Double) query.getSingleResult();
			
			return t;	
		}catch(NullPointerException e){
			return 0;
		}catch(Exception e){
			System.out.println(e);
			return 0;
		}
	}
	
	public List<String> getResponsaveisNotasEnviadas(int mes){
		try{
			StringBuilder sql = new StringBuilder();
			sql.append(" select contrato.nomeresponsavel || ' (' || aluno.nomealuno  || ' )'  ");
			sql.append(" from boleto bol ");
			sql.append("left join contratoaluno contrato ");
			sql.append("on contrato.id = bol.contrato_id ");
			sql.append("left join aluno aluno ");
			sql.append("on bol.pagador_id = aluno.id ");
			
			sql.append("where ");
			sql.append("nfsEnviada = true ");
			sql.append("and vencimento >  ");
			sql.append(getInicioMes(mes));
			sql.append(" and vencimento < ");
			sql.append(getFimMes(mes));
			
			Query query = em.createNativeQuery(sql.toString());
			List<String> t = (List<String>) query.getResultList();
			
			return t;	
		}catch(Exception e){
			return new ArrayList<>();
		}
	}
	
	private String getInicioMes(int mesDoAno) {
		String mes = "2022-01-01";
		switch (mesDoAno) {

		case 12:
			mes = "'2022-12-01'";
			break;

		case 11:
			mes = "'2022-11-01'";
			break;

		case 10:
			mes = "'2022-10-01'";
			break;

		case 9:
			mes = "'2022-09-01'";
			break;

		case 8:
			mes = "'2022-08-01'";
			break;

		case 7:
			mes = "'2022-07-01'";
			break;

		case 6:
			mes = "'2022-06-01'";
			break;

		case 5:
			mes = "'2022-05-01'";
			break;

		case 4:
			mes = "'2022-04-01'";
			break;

		case 3:
			mes = "'2022-03-01'";
			break;

		case 2:
			mes = "'2022-02-01'";
			break;

		case 1:
			mes = "'2022-01-01'";
			break;

		default:
			mes = "'2022-12-01'";
			break;
		}

		return mes;
	}
	
	private String getFimMes(int mesDoAno) {
		String mes = "2022-01-31";
		switch (mesDoAno) {

		case 12:
			mes = "'2022-12-31'";
			break;

		case 11:
			mes = "'2022-11-30'";
			break;

		case 10:
			mes = "'2022-10-31'";
			break;

		case 9:
			mes = "'2022-09-30'";
			break;

		case 8:
			mes = "'2022-08-31'";
			break;

		case 7:
			mes = "'2022-07-31'";
			break;

		case 6:
			mes = "'2022-06-30'";
			break;

		case 5:
			mes = "'2022-05-31'";
			break;

		case 4:
			mes = "'2022-04-30'";
			break;

		case 3:
			mes = "'2022-03-31'";
			break;

		case 2:
			mes = "'2022-02-28'";
			break;

		case 1:
			mes = "'2022-01-31'";
			break;

		default:
			mes = "'2022-12-31'";
			break;
		}

		return mes;
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


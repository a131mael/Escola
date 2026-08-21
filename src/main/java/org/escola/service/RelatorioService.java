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

	public double getTotalNotasEmitidas(int mes, int ano){
		try{
			StringBuilder sql = new StringBuilder();
			sql.append(" select sum(valorPago) from boleto ");
			sql.append("where ");
			sql.append("nfsEnviada = true ");
			sql.append("and vencimento >  ");
			sql.append(getInicioMes(mes,ano));
			sql.append(" and vencimento < ");
			sql.append(getFimMes(mes,ano));
			
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
	
	public List<String> getResponsaveisNotasEnviadas(int mes, int ano){
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
			sql.append(getInicioMes(mes, ano));
			sql.append(" and vencimento < ");
			sql.append(getFimMes(mes,ano));
			
			Query query = em.createNativeQuery(sql.toString());
			List<String> t = (List<String>) query.getResultList();
			
			return t;	
		}catch(Exception e){
			return new ArrayList<>();
		}
	}
	
	private String getInicioMes(int mesDoAno, int ano) {
		
		String mes = "'"+ano + "-01-01'";
		
		switch (mesDoAno) {

		case 12:
			mes = "'"+ano + "-12-01'";
			break;

		case 11:
			mes = "'"+ano + "-11-01'";
			break;

		case 10:
			mes = "'"+ano + "-10-01'";
			break;

		case 9:
			mes = "'"+ano + "-09-01'";
			break;

		case 8:
			mes ="'"+ano + "-08-01'";
			break;

		case 7:
			mes = "'"+ano + "-07-01'";
			break;

		case 6:
			mes = "'"+ano + "-06-01'";
			break;

		case 5:
			mes ="'"+ano + "-05-01'";
			break;

		case 4:
			mes = "'"+ano + "-04-01'";
			break;

		case 3:
			mes = "'"+ano + "-03-01'";
			break;

		case 2:
			mes = "'"+ano + "-02-01'";
			break;

		case 1:
			mes = "'"+ano + "-01-01'";
			break;

		default:
			mes = "'"+ano + "-12-01'";
			break;
		}

		return mes;
	}
	
	private String getFimMes(int mesDoAno, int ano) {
		String mes = "'"+ano + "-01-31";
		switch (mesDoAno) {

		case 12:
			mes ="'"+ano + "-12-31'";
			break;

		case 11:
			mes = "'"+ano + "-11-30'";
			break;

		case 10:
			mes = "'"+ano + "-10-31'";
			break;

		case 9:
			mes = "'"+ano + "-09-30'";
			break;

		case 8:
			mes = "'"+ano + "-08-31'";
			break;

		case 7:
			mes = "'"+ano + "-07-31'";
			break;

		case 6:
			mes = "'"+ano + "-06-30'";
			break;

		case 5:
			mes = "'"+ano + "-05-31'";
			break;

		case 4:
			mes = "'"+ano + "-04-30'";
			break;

		case 3:
			mes = "'"+ano + "-03-31'";
			break;

		case 2:
			mes = "'"+ano + "-02-28'";
			break;

		case 1:
			mes ="'"+ano + "-01-31'";
			break;

		default:
			mes = "'"+ano + "-12-31'";
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

	public java.util.List<org.escola.model.PedidoCancelamento> getPedidosCancelamentoPendentes() {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select pc.id, a.nomealuno, ca.numero, ca.ano, ca.nomeresponsavel, pc.motivo, pc.aluno_id, ");
			sql.append(" pc.data_pedido, pc.data_ultimo_uso, pc.valor_multa, bm.vencimento, ");
			sql.append(" (select string_agg( ");
			sql.append("    'R$ ' || to_char(b.valornominal, 'FM999999990.00') || ' (' || to_char(b.vencimento,'MM/YYYY') || ')', ");
			sql.append("    ' + ' order by x.posicao) ");
			sql.append("  from unnest(pc.boletos_mensalidade_ids) with ordinality as x(bid, posicao) ");
			sql.append("  join boleto b on b.id = x.bid) as detalhe_mensalidades ");
			sql.append(" from pedido_cancelamento_contrato pc ");
			sql.append(" join contratoaluno ca on ca.id = pc.contrato_id ");
			sql.append(" join aluno a on a.id = pc.aluno_id ");
			sql.append(" left join boleto bm on bm.id = pc.boleto_multa_id ");
			sql.append(" where coalesce(ca.cancelado, false) = false ");
			sql.append(" order by pc.data_pedido desc ");

			Query query = em.createNativeQuery(sql.toString());
			@SuppressWarnings("unchecked")
			java.util.List<Object[]> linhas = query.getResultList();

			java.util.List<org.escola.model.PedidoCancelamento> pedidos = new ArrayList<>();
			for (Object[] l : linhas) {
				org.escola.model.PedidoCancelamento p = new org.escola.model.PedidoCancelamento();
				p.setId(l[0] == null ? null : ((Number) l[0]).longValue());
				p.setNomeAluno((String) l[1]);
				p.setNumeroContrato(l[2] == null ? "" : String.valueOf(l[2]));
				p.setAno(l[3] == null ? "" : String.valueOf(l[3]));
				p.setNomeResponsavel((String) l[4]);
				p.setMotivo((String) l[5]);
				p.setAlunoId(l[6] == null ? null : ((Number) l[6]).longValue());
				p.setDataPedido(l[7] == null ? "" : l[7].toString());
				p.setDataUltimoUso(l[8] == null ? "" : l[8].toString());
				String vencMulta = l[10] == null ? "" : (" (" + l[10].toString().substring(0, 7) + ")");
				p.setValorMulta(l[9] == null ? "-" : ("R$ " + l[9].toString() + vencMulta));
				p.setDetalheMensalidades(l[11] == null ? "-" : (String) l[11]);
				pedidos.add(p);
			}
			return pedidos;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void descancelarPedido(Long pedidoId) {
		try {
			Query query = em.createNativeQuery("delete from pedido_cancelamento_contrato where id = :id");
			query.setParameter("id", pedidoId);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}


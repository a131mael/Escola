package org.escola.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.escola.model.Faturamento;
import org.escola.util.Constant;
import org.escola.util.Service;

@Stateless
public class FaturamentoService extends Service {

		
	@Inject
	private TurmaService carroService;

	@Inject
	private RelatorioService relatorioService;

	
	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public Faturamento findById(EntityManager em, Long id) {
		return em.find(Faturamento.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Faturamento> findFaturamentoPorMes(Date mes) {
		List<Faturamento> faturamentos = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT fat from  Faturamento fat ");
		sql.append("where 1=1 ");
		sql.append(" and fat.data =   ");
		sql.append(mes);
		sql.append(" and fat.anoLetivo = ");
		sql.append(Constant.anoLetivoAtual);

		Query query = em.createQuery(sql.toString());
		faturamentos = query.getResultList();
		return faturamentos;

	}

}

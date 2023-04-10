package org.escola.service;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.escola.model.Boleto;
import org.escola.model.ContratoAluno;
import org.escola.model.extrato.ItemExtrato;
import org.escola.util.Service;

@Stateless
public class FinanceiroEscolaService extends Service {

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;
	
	@Inject
	private AlunoService alunoService;
	
	@Inject
	private ExtratoBancarioService extratoBancarioService;
	
	@Inject
	private ConfiguracaoService configuracaoService;

	public Boleto findBoletoByNumero(String numeroBoleto) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT bol from Boleto bol ");
			sql.append("where 1=1 ");
			sql.append(" and bol.nossoNumero = '");
			sql.append(numeroBoleto);
			sql.append("'");

			Query query = em.createQuery(sql.toString());
			Boleto boleto = (Boleto) query.getSingleResult();
			return boleto;
		} catch (NoResultException nre) {
			return null;
		}
	}

	public org.escola.model.Boleto findBoletoByNumeroModel(String numeroBoleto) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT bol from Boleto bol ");
			sql.append("where 1=1 ");
			sql.append(" and bol.nossoNumero = '");
			sql.append(numeroBoleto);
			sql.append("'");

			Query query = em.createQuery(sql.toString());
			org.escola.model.Boleto boleto = (org.escola.model.Boleto) query.getSingleResult();
			return boleto;
		} catch (NoResultException nre) {
			return null;
		}
	}

	public double getPrevisto(int mes) {
		if (mes > 0) {
			mes--;
			try {
				Calendar c = Calendar.getInstance();
				c.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, 1, 0, 0, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, c.getMaximum(Calendar.MONTH), 23, 59, 59);

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT sum(bol.valorNominal) from Boleto bol ");
				sql.append("where 1=1 ");
				sql.append(" and bol.vencimento >= '");
				sql.append(c.getTime());
				sql.append("'");
				sql.append(" and bol.vencimento < '");
				sql.append(c2.getTime());
				sql.append("'");
				sql.append(" and bol.pagador.removido = false");
				
				Query query = em.createQuery(sql.toString());
				Double boleto = (Double) query.getSingleResult();
				if(boleto == null){
					return 0d;
				}
				return boleto;
			} catch (NoResultException nre) {
				return 0D;
			}
		}
		return 0D;

	}

	public Double getPago(int mes) {
		if (mes > 0) {
			mes --;
			try {
				Calendar c = Calendar.getInstance();
				c.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, 1, 0, 0, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, c.getMaximum(Calendar.MONTH), 23, 59, 59);

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT sum(bol.valorPago) from Boleto bol ");
				sql.append("where 1=1 ");
				sql.append(" and bol.dataPagamento >= '");
				sql.append(c.getTime());
				sql.append("'");
				sql.append(" and bol.dataPagamento < '");
				sql.append(c2.getTime());
				sql.append("'");
				sql.append(" and bol.pagador.removido = false");
				
				Query query = em.createQuery(sql.toString());
				Object retorno = query.getSingleResult();
				Double boleto = null;
				if (retorno != null) {
					boleto = (Double) retorno;
				} else {
					boleto = 0D;
				}

				return boleto;
			} catch (NoResultException nre) {
				return 0D;
			}
		}
		return 0D;

	}

	public List<Boleto> getBoletoMes(int mes) {
		if (mes > 0) {
			mes --;
			try {
				Calendar c = Calendar.getInstance();
				c.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, 1, 0, 0, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, c.getMaximum(Calendar.MONTH), 23, 59, 59);

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT bol from Boleto bol ");
				sql.append("where 1=1 ");
				sql.append(" and bol.vencimento >= '");
				sql.append(c.getTime());
				sql.append("'");
				sql.append(" and bol.vencimento < '");
				sql.append(c2.getTime());
				sql.append("'");
				sql.append(" AND bol.pagador.removido = false ");
				Query query = em.createQuery(sql.toString());
				List<Boleto> boleto = (List<Boleto>) query.getResultList();
				
				return boleto;
				
			} catch (NoResultException nre) {
				return null;
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<Boleto> find(int first, int size, String orderBy, String order, Map<String, Object> filtros) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Boleto> criteria = cb.createQuery(Boleto.class);
			Root<Boleto> member = criteria.from(Boleto.class);
			CriteriaQuery cq = criteria.select(member);

			final List<Predicate> predicates = new ArrayList<Predicate>();
			for (Map.Entry<String, Object> entry : filtros.entrySet()) {

				Predicate pred = cb.and();
				if (entry.getValue() instanceof String) {
					pred = cb.and(pred, cb.like(member.<String> get(entry.getKey()), "%" + entry.getValue() + "%"));
				} else {
					pred = cb.equal(member.get(entry.getKey()), entry.getValue());
				}
				predicates.add(pred);
				// cq.where(pred);
			}

			cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
			cq.orderBy((order.equals("asc") ? cb.asc(member.get(orderBy)) : cb.desc(member.get(orderBy))));
			Query q = em.createQuery(criteria);
			q.setFirstResult(first);
			q.setMaxResults(size);
			return (List<Boleto>) q.getResultList();

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

	}

	public long count(Map<String, Object> filtros) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			Root<Boleto> member = countQuery.from(Boleto.class);
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

			}
			countQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
			Query q = em.createQuery(countQuery);
			return (long) q.getSingleResult();

		} catch (NoResultException nre) {
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public org.escola.model.Boleto findBoletoByID(Long id) {
		try {
			return em.find(org.escola.model.Boleto.class, id);
		} catch (NoResultException nre) {
			return null;
		}
	}

	public List<org.escola.model.Boleto> getBoletosParaBaixa() {
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT bol from Boleto bol ");
			sql.append("where 1=1 ");
			sql.append(" and (bol.baixaManual = true ");
			sql.append(" or bol.conciliacaoPorExtrato = true )");
			sql.append(" and (bol.baixaGerada = false or bol.baixaGerada is null) ");

			Query query = em.createQuery(sql.toString());
			List<org.escola.model.Boleto> boleto = (List<org.escola.model.Boleto>) query.getResultList();
			return boleto;
		} catch (NoResultException nre) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void save(org.escola.model.Boleto boleto) {
		if (boleto.getId() != null) {
			org.escola.model.Boleto bol = findBoletoByID(boleto.getId());
			bol.setValorPago(boleto.getValorPago());
			bol.setDataPagamento(boleto.getDataPagamento());
			bol.setBaixaManual(boleto.getBaixaManual());
			bol.setConciliacaoPorExtrato(boleto.getConciliacaoPorExtrato());
			bol.setBaixaGerada(boleto.getBaixaGerada());
			bol.setCnabCanceladoEnviado(boleto.getCnabCanceladoEnviado());
			em.merge(bol);
			em.flush();
		}
	}

	public void alterarBoletoManualmente(org.escola.model.Boleto boleto) {
		org.escola.model.Boleto boletoMerged = em.find(org.escola.model.Boleto.class, boleto.getId());
		boletoMerged.setAlteracaoBoletoManual(true);
		boletoMerged.setVencimento(boleto.getVencimento());
		boletoMerged.setValorNominal(boleto.getValorNominal());
		em.merge(boletoMerged);
		em.flush();
	}


	public void saveCNABENviado(Aluno aluno){
		Aluno al =  em.find(Aluno.class, aluno.getId());
		ContratoAluno contrato = al.getContratoVigente();
		contrato.setCnabEnviado(true);
		em.merge(contrato);
		em.flush();
	}
	
	public void saveCNABENviado(Boleto b){
		Boleto al =  em.find(Boleto.class, b.getId());
		al.setCnabEnviado(true);
		em.merge(al);
		em.flush();
	}
	
	public List<ContratoAluno> getAlunosRemovidos() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct(al) from  Boleto bol ");
		sql.append("left join bol.contrato al ");
		sql.append("where 1=1 ");
		sql.append(" and al.cancelado = true ");
		sql.append(" and (bol.baixaGerada = false or bol.baixaGerada is null)");

		Query query = em.createQuery(sql.toString());

		@SuppressWarnings("unchecked")
		List<ContratoAluno> alunos = query.getResultList();
			for(ContratoAluno contrato : alunos){
				contrato.getBoletos().size();
			}

		return alunos;
	}
	
	public List<Aluno> getAlunosCNABNaoEnviado() {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT distinct(al) from  ContratoAluno al ");
			sql.append("where 1=1 ");
			sql.append("and (al.cnabEnviado = false or al.cnabEnviado is null )");
			sql.append("and al.cancelado=false");

			Query query = em.createQuery(sql.toString());

			@SuppressWarnings("unchecked")
			List<Aluno> alunos = query.getResultList();

			return alunos;
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	public List<org.escola.model.Boleto> getBoletosAtrasados(int mes) {
	
		return getBoletosAtrasadosAluno(mes, null);
	}

	public List<Boleto> getBoletosAtrasadosAluno(int mes, Long idAluno) {
		if (mes > 0) {
			mes --;
			try {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, configuracaoService.getConfiguracao().getAnoLetivo());
				c.set(Calendar.MONTH, mes);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.HOUR, 23);
				

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT bol from Boleto bol ");
				sql.append("where 1=1 ");
				sql.append(" and bol.vencimento <= '");
				sql.append(c.getTime());
				sql.append("'");
				if(idAluno != null){
					sql.append(" and bol.pagador.id = ");
					sql.append(idAluno);
				}
				sql.append(" and (bol.valorPago = 0");
				sql.append(" or bol.valorPago is null)");
				sql.append(" and (bol.baixaManual = false");
				sql.append(" or bol.baixaManual is null)");

				
				sql.append(" and (bol.conciliacaoPorExtrato = false");
				sql.append(" or bol.conciliacaoPorExtrato is null)");
				
				
				sql.append(" and (bol.baixaGerada = false");
				sql.append(" or bol.baixaGerada is null)");
				
				
				Query query = em.createQuery(sql.toString());
				List<org.escola.model.Boleto> boleto = (List<org.escola.model.Boleto>) query.getResultList();
				return boleto;
			} catch (NoResultException nre) {
				return null;
			}
		}
		return null;

	}

	public Boleto getBoletoMes(int mes ,Long idAluno) {
		if (mes > 0) {
			mes --;
			try {
				Calendar c = Calendar.getInstance();
				c.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, 1, 0, 0, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, c.getMaximum(Calendar.MONTH), 23, 59, 59);

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT bol from Boleto bol ");
				sql.append("where 1=1 ");
				sql.append(" and bol.vencimento >= '");
				sql.append(c.getTime());
				sql.append("'");
				sql.append(" and bol.vencimento < '");
				sql.append(c2.getTime());
				sql.append("'");
				if(idAluno != null){
					sql.append(" and bol.pagador.id = ");
					sql.append(idAluno);
				}
				sql.append(" AND bol.pagador.removido = false ");
				Query query = em.createQuery(sql.toString());
				Boleto boleto = (Boleto) query.getSingleResult();
				return boleto;
			} catch (NoResultException nre) {
				return null;
			}
		}
		return null;

	}

	public List<Boleto> getBoletosCNABNaoEnviado(int quantidadeDeMeses) {
		LocalDate hoje = LocalDate.now();
		LocalDate primeiroDiaDoMes = hoje.with(TemporalAdjusters.firstDayOfMonth());
		
		hoje.plusMonths(quantidadeDeMeses);
		LocalDate ultimoDiaDoMes = hoje.with(TemporalAdjusters.lastDayOfMonth());
		
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT distinct(bol) from  Boleto bol ");
			sql.append("where 1=1 ");
			sql.append("and (bol.cnabEnviado = false or bol.cnabEnviado is null )");
			sql.append("and (bol.cancelado=false or bol.cancelado is null)");
			sql.append("and vencimento < '");
			sql.append(ultimoDiaDoMes);
			sql.append("'");
			sql.append(" and vencimento > '");
			sql.append(primeiroDiaDoMes);
			sql.append("'");
			sql.append(" and ( valorPago < ");
			sql.append(20);
			sql.append(" or  valorPago is null ) ");
			
				
			Query query = em.createQuery(sql.toString());

			@SuppressWarnings("unchecked")
			List<Boleto> alunos = query.getResultList();

			return alunos;
		} catch (NoResultException nre) {
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getQuantidadeBoletosAtrasadosPorMes(int mes, int ano){
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from boleto bol left join contratoaluno contrato on bol.contrato_id = contrato.id where ");
		if(mes > 0){
			sql.append(getIntervaloMes(mes,ano));
			sql.append(" and (bol.cancelado is null or bol.cancelado = false)");
		}else{
			sql.append("  (bol.cancelado is null or bol.cancelado = false)");
		}
		sql.append(" and bol.datapagamento is null and (contrato.protestado is  null or contrato.protestado = false )");
		Query query = em.createNativeQuery(sql.toString());
		BigInteger codigo = (BigInteger) query.getSingleResult();
		if(codigo == null){
			return 0;
		}
		return codigo.intValue();
	}
	
	public Double getValorBoletosAtrasadosPorMes(int mes, int ano){
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(valornominal) from boleto bol  left join contratoaluno contrato on bol.contrato_id = contrato.id where ");
		sql.append(getIntervaloMes(mes,ano));
		sql.append(" and (bol.cancelado is null or bol.cancelado = false)");
		sql.append(" and bol.datapagamento is null and (contrato.protestado is  null or contrato.protestado = false ) ");
		Query query = em.createNativeQuery(sql.toString());
		Double codigo = (Double) query.getSingleResult();
		if(codigo == null){
			return 0d;
		}
		return codigo;
	}
	
	public int getQuantidadeBoletosAtrasadosPorQuantidade(int quantidade){
		return getQuantidadeBoletosAtrasadosPorQuantidade(quantidade, configuracaoService.getConfiguracao().getAnoLetivo());
	}
	public int getQuantidadeBoletosAtrasadosPorQuantidade(int quantidade, int ano){
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String hoje = df.format(new Date());
		
		if(ano < configuracaoService.getConfiguracao().getAnoLetivo()){
			hoje = ano + "-12-31";
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(1) from ( ");
		sql.append(" select sum(1) as quantidadeAtrasadas,pagador_id from boleto bol left join contratoaluno contrato on bol.contrato_id = contrato.id ");
		sql.append(" where ");
		sql.append(" bol.vencimento >  ");
		sql.append(" '");
		sql.append(ano);
		sql.append("-01-01' ");
		sql.append("  and bol.vencimento < '");
		sql.append(hoje);
		sql.append("'");
		sql.append(" and (bol.cancelado is null or bol.cancelado = false)");
		sql.append(" and bol.datapagamento is null and (contrato.protestado is  null or contrato.protestado = false ) group by pagador_id");
		sql.append(" order by quantidadeAtrasadas) as buscaAtrasadas");
		if(quantidade != 0){
			sql.append(" where quantidadeAtrasadas  ");
			if(quantidade > 6){
				sql.append(" > ");
				sql.append(quantidade-1);
			}else{
				sql.append(" = ");
				sql.append(quantidade);	
			}
		}
		
		Query query = em.createNativeQuery(sql.toString());
		BigInteger codigo = (BigInteger) query.getSingleResult();
		if(codigo == null){
			return 0;
		}
		return codigo.intValue();
	}
	
	public int getQuantidadeBoletosAtrasados(){
		return getQuantidadeBoletosAtrasados(configuracaoService.getConfiguracao().getAnoLetivo());
	}
	public int getQuantidadeBoletosAtrasados(int anoSelecionado){
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String hoje = df.format(new Date());
		
		if(anoSelecionado < configuracaoService.getConfiguracao().getAnoLetivo()){
			hoje = anoSelecionado + "-12-31";
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(1) as quantidadeAtrasadas from boleto bol left join contratoaluno contrato on bol.contrato_id = contrato.id  where ");
		sql.append("bol.vencimento >  '");
		sql.append(anoSelecionado);
		sql.append("-01-01' ");
		sql.append(" and bol.vencimento < '");
		sql.append(hoje);
		sql.append("'");
		sql.append(" and (bol.cancelado is null or bol.cancelado = false)	 and bol.datapagamento is null and (contrato.protestado is  null or contrato.protestado = false )");
		
		Query query = em.createNativeQuery(sql.toString());
		BigInteger codigo = (BigInteger) query.getSingleResult();
		if(codigo == null){
			return 0;
		}
		return codigo.intValue();
	}
	
	public Double getValorTotalAtrasado(){
		return getValorTotalAtrasado(configuracaoService.getConfiguracao().getAnoLetivo());
	}
	public Double getValorTotalAtrasado(int anoSelecionado){
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String hoje = df.format(new Date());
		
		if(anoSelecionado < configuracaoService.getConfiguracao().getAnoLetivo()){
			hoje = anoSelecionado + "-12-31";
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(valornominal) as quantidadeAtrasadas from boleto bol left join contratoaluno contrato on bol.contrato_id = contrato.id where	 ");
		
		sql.append(" bol.vencimento >  '");
		sql.append(anoSelecionado);
		sql.append("-01-01'");
		sql.append(" and bol.vencimento < '");
		sql.append(hoje);
		sql.append("'");
		sql.append(" and (bol.cancelado is null or bol.cancelado = false)	 and bol.datapagamento is null  and (contrato.protestado is  null or contrato.protestado = false )");
		
		Query query = em.createNativeQuery(sql.toString());
		Double codigo = (Double) query.getSingleResult();
		if(codigo == null){
			return 0d;
		}
		return codigo;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Aluno> findAlunoMes(int first, int size, String orderBy, String order,	Map<String, Object> filtros) {
			
			StringBuilder sql = new StringBuilder();
			sql.append("select pagador_id from boleto bol left join contratoaluno contrato on bol.contrato_id = contrato.id  where  ");
			sql.append(getIntervaloMes((Integer)filtros.get("mesAtrasado"),(Integer)filtros.get("anoSelecionado")));
			sql.append(" and (bol.cancelado is null or bol.cancelado = false)	 and bol.datapagamento is null  and (contrato.protestado is  null or contrato.protestado = false )");
			
			Query query = em.createNativeQuery(sql.toString());
			List<BigInteger> codigo = (List<BigInteger>) query.getResultList();
			if(codigo == null){
				return new ArrayList<>();
			}
			List<Aluno> alunos = new ArrayList<>();
			for(BigInteger id : codigo){
				alunos.add(alunoService.findById(id.longValue()));
			}
			
			return alunos;
	}
	
	@SuppressWarnings("unchecked")
	public List<Aluno> findAlunoQuantidade(int first, int size, String orderBy, String order,	Map<String, Object> filtros) {
		int quantidade = (Integer)filtros.get("quantidadeAtrasados");	
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String hoje = df.format(new Date());
		
		if((Integer)filtros.get("anoSelecionado") < configuracaoService.getConfiguracao().getAnoLetivo()){
			hoje = (Integer)filtros.get("anoSelecionado") + "-12-31";
		}
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select pagador_id from (");
		sql.append(" select sum(1) as quantidadeAtrasadas,pagador_id from boleto  bol left join contratoaluno contrato on bol.contrato_id = contrato.id ");
		sql.append(" where ");
		sql.append(" bol.vencimento >  '");
		sql.append((Integer)filtros.get("anoSelecionado"));
		sql.append("-01-01' and bol.vencimento < '");
		sql.append(hoje);
		sql.append("'");
		sql.append(" and (bol.cancelado is null or bol.cancelado = false)");
		sql.append(" and bol.datapagamento is null  and (contrato.protestado is  null or contrato.protestado = false ) ");
		sql.append(" group by pagador_id");
		sql.append("  order by quantidadeAtrasadas) as buscaAtrasadas");
		if(quantidade != 0){
			sql.append(" where quantidadeAtrasadas  ");
			if(quantidade > 6){
				sql.append(" > ");
				sql.append(quantidade-1);
			}else{
				sql.append(" = ");
				sql.append(quantidade);	
			}
		}
		
		Query query = em.createNativeQuery(sql.toString());
		List<BigInteger> codigo = (List<BigInteger>) query.getResultList();
		if(codigo == null){
			return new ArrayList<>();
		}
		List<Aluno> alunos = new ArrayList<>();
		for(BigInteger id : codigo){
			alunos.add(alunoService.findById(id.longValue()));
		}
		return alunos;
	}
	
	@SuppressWarnings("unchecked")
	public List<Aluno> findMaioresDevedores(Map<String, Object> filtros) {
		int quantidade = (Integer)filtros.get("quantidadeDevedores");	
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String hoje = df.format(new Date());
		
		if((Integer)filtros.get("anoSelecionado") < configuracaoService.getConfiguracao().getAnoLetivo()){
			hoje = (Integer)filtros.get("anoSelecionado") + "-12-31";
		}
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select aluno.id  from aluno aluno ");
		sql.append(" right join (");
		sql.append(" select * from (select sum(valorNominal) as valorNominal,pagador_id from boleto  bol ");
		sql.append(" left join contratoaluno contrato on bol.contrato_id = contrato.id ");
		sql.append(" where");
		sql.append(" bol.vencimento >  '2021-01-01' and bol.vencimento < '");
		sql.append(hoje);
		sql.append("'");
		sql.append(" and (bol.cancelado is null or bol.cancelado = false)");
		sql.append(" and bol.datapagamento is null  and (contrato.protestado is  null or contrato.protestado = false )");
		 
		sql.append(" group by pagador_id");
		sql.append(" order by valorNominal) as buscaAtrasadas");
		sql.append(" order by valornominal desc");
		sql.append(" limit ");
		sql.append(quantidade);
		sql.append(") as somatorio");
		sql.append(" on somatorio.pagador_id = aluno.id");
		sql.append(" order by somatorio.valorNominal desc");
		sql.append("");
		
		
		Query query = em.createNativeQuery(sql.toString());
		List<BigInteger> codigo = (List<BigInteger>) query.getResultList();
		if(codigo == null){
			return new ArrayList<>();
		}
		List<Aluno> alunos = new ArrayList<>();
		int indice = 0;
		for(BigInteger id : codigo){
			alunos.add(indice,alunoService.findById(id.longValue()));
			indice ++;
		}
		return alunos;
	}
	
	private String getIntervaloMes(int mes, int anoSelecionado){
		StringBuilder sb  = new StringBuilder();
		sb.append(" vencimento >  '");
		sb.append(anoSelecionado);
		switch (mes) {
		case 1:
			sb.append("-01-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-01-30' ");
			break;
		case 2:
			sb.append("-02-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-02-28' ");
			
			break;
		case 3:
			sb.append("-03-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-03-30' ");
			break;
		case 4:
			sb.append("-04-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-04-30' ");
			break;
		case 5:
			sb.append("-05-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-05-30' ");
			break;
		case 6:
			sb.append("-06-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-06-30' ");
			break;
		case 7:
			sb.append("-07-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-07-30' ");
			break;
		case 8:
			sb.append("-08-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-08-30' ");
			break;
		case 9:
			sb.append("-09-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-09-30' ");
			break;
		case 10:
			sb.append("-10-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-10-30' ");
			break;
		case 11:
			sb.append("-11-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-11-30' ");
			break;
		case 12:
			sb.append("-12-01' and vencimento < '");
			sb.append(anoSelecionado);
			sb.append("-12-30' ");
			break;
		default:
			break;
		}
		return sb.toString();
		
	}

	public int countContratos(int mes) {
		if (mes >= 0) {
			try {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT count(id) from ContratoAluno bol ");
				sql.append("where 1=1 ");
				sql.append(" and bol.ano = ");
				sql.append(configuracaoService.getConfiguracao().getAnoLetivo());
				sql.append(" and (bol.cancelado = false");
				sql.append(" or  bol.cancelado is null)");

				Query query = em.createQuery(sql.toString());
				Long boleto = (Long) query.getSingleResult();
				return boleto.intValue();
			} catch (NoResultException nre) {
				return 0;
			}
		}
		return 0;
	}
	
	public ItemExtrato findItemExtratoByID(Long id) {
		try {
			ItemExtrato ie = em.find(ItemExtrato.class, id);
			if(ie != null){
				ie.getItensFilhos().size();
			}
			return ie;
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	
	public List<ItemExtrato> findItemExtrato(int first, int size, String orderBy, String order,Map<String, Object> filtros) {
		try {
			return extratoBancarioService.getItemExtrato((int)filtros.get("mes"), (int)filtros.get("ano"),first,size,filtros);
		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public long countExtratosMes(Map<String, Object> filtros){
		if(filtros == null){
			return extratoBancarioService.getItemExtrato(1,configuracaoService.getConfiguracao().getAnoLetivo()).size();
		}
		return extratoBancarioService.getItemExtrato((int)filtros.get("mes"), (int)filtros.get("ano")).size();
	}
	
}

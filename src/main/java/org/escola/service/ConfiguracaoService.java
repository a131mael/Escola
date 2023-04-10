
package org.escola.service;

import java.math.BigInteger;
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

import org.escola.enums.Serie;
import org.escola.enums.TipoMembro;
import org.escola.model.Aluno;
import org.escola.model.Boleto;
import org.escola.model.Configuracao;
import org.escola.model.ContratoAluno;
import org.escola.model.Member;
import org.escola.util.Service;
import org.escola.util.Util;


@Stateless
public class ConfiguracaoService extends Service {

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public Configuracao findById(EntityManager em, Long id) {
		return em.find(Configuracao.class, id);
	}

	public Configuracao findById(Long id) {
		return em.find(Configuracao.class, id);
	}
	
	public Configuracao findByCodigo(Long id) {
		return em.find(Configuracao.class, id);
	}
	
	public String remover(Long idEvento){
		em.remove(findById(idEvento));
		return "index";
	}

	public Configuracao getConfiguracao(){
		return find();
	}
	
	public Configuracao getConfiguracao2(){
		return find();
	}
	
	private Configuracao find() {
		try{
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * from configuracao ");
			sql.append(" where 1 = 1");
			
			Query query = em.createNativeQuery(sql.toString(),Configuracao.class);
			Configuracao t = (Configuracao) query.getSingleResult();
			
			return t;	
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
		
	}
	
	public List<Aluno> findAlunosAlunoLetivo(int anoletivo, Serie serie) {
		try{
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct (aluno) from AlunoAvaliacao alunoavaliacao");
			sql.append(" left join alunoavaliacao.avaliacao avaliacao");
			sql.append(" left join alunoavaliacao.aluno aluno ");
			sql.append(" where alunoavaliacao.anoLetivo =");
			sql.append(anoletivo);
			sql.append(" and disciplina = 1");
			sql.append(" and bimestre = 3");
			sql.append(" and avaliacao.serie = ");
			sql.append(serie.ordinal());
			
			
			Query query = em.createQuery(sql.toString(), Aluno.class);
			List<Aluno> t = query.getResultList();
			
			return t;	
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
			return null;
		}
		
	}

	public List<Configuracao> findAll() {
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Configuracao> criteria = cb.createQuery(Configuracao.class);
			Root<Configuracao> member = criteria.from(Configuracao.class);
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

	public Configuracao save(Configuracao configuracao) {
		Configuracao user = null;
		try {

			log.info("Registering " + configuracao.getId());
		
			if (configuracao.getId() != null && configuracao.getId() != 0L) {
				user = findById(configuracao.getId());
			} else {
				user = new Configuracao();
			}
			
			user.setAnoLetivo(configuracao.getAnoLetivo());
			user.setBimestre(configuracao.getBimestre());
			user.setAnoRematricula(configuracao.getAnoRematricula());
			
			em.persist(user);
			em.flush();
			
		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
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
	
	public long getSequencialArquivo(){
		return getConfiguracao().getSequencialArquivoCNAB();
	}
	
	public void incrementaSequencialArquivoCNAB(){
		long sequecial = getSequencialArquivo();
		sequecial++;
		Configuracao conf = getConfiguracao();
		conf.setSequencialArquivoCNAB(sequecial);
		save(conf);
	}

	public List<Boleto> findBoletosMes(int mes, int ano) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct(bol) from Boleto bol ");
		sql.append(" where 1 = 1");
		sql.append(" and (bol.baixaGerada = false or bol.baixaGerada is null)");
		sql.append(" and (bol.cancelado = false or bol.cancelado is null)");
		sql.append(" and (bol.cnabEnviado = false or bol.cnabEnviado is null)");
		sql.append(" and (bol.valorPago = 0 or bol.valorPago is null)");
		sql.append(" and bol.vencimento > '" + Util.getDataInicioMesString(mes, ano) + "'");
		sql.append(" and bol.vencimento < '" + Util.getDataFimMesString(mes, ano) + "'");

		Query query = em.createQuery(sql.toString());
		List<Boleto> t = (List<Boleto>) query.getResultList();

		return t;
	}

	public ContratoAluno findContrato(Long idBoleto) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct(contratoaluno_id) from contratoaluno_boleto ");
		sql.append(" where 1 = 1");
		sql.append(" and boletos_id  = ");
		sql.append(idBoleto);
		Query query = em.createNativeQuery(sql.toString());
		BigInteger t = (BigInteger) query.getSingleResult();
		
		ContratoAluno c = em.find(ContratoAluno.class, t.longValue());
		c.getBoletos().size();

		return c;
	}

	public void mudarStatusParaCNABEnviado(Boleto b) {
		b = em.find(Boleto.class, b.getId());
		b.setCnabEnviado(true);
		em.merge(b);
		em.flush();
		
	}
	
	public List<Boleto> findBoletosCanceladosMes(int mes, int ano) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct(bol) from Boleto bol ");
		sql.append(" where 1 = 1");
		sql.append(" and (bol.cancelado = true)");
		sql.append(" and (bol.manterAposRemovido = false or manterAposRemovido is null)");
		sql.append(" and (bol.cnabCanceladoEnviado = false or cnabCanceladoEnviado is null)");
		sql.append(" and (bol.valorPago = 0 or bol.valorPago is null)");
		sql.append(" and bol.vencimento > '" + Util.getDataInicioMesString(mes, ano) + "'");
		sql.append(" and bol.vencimento < '" + Util.getDataFimMesString(mes, ano) + "'");

		Query query = em.createQuery(sql.toString());
		List<Boleto> t = (List<Boleto>) query.getResultList();

		return t;
	}

	public void mudarStatusParaProtestoEnviadoPorEmail(ContratoAluno ca) {
		ca = em.find(ContratoAluno.class, ca.getId());
		ca.setEnviadoPorEmailProtesto(true);
		em.merge(ca);
		em.flush();
		
	}

	public List<ContratoAluno> findContratosProtestados(boolean jaEnviadoPorEmail) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct(ca) from ContratoAluno ca");
		sql.append(" where 1 = 1");
		sql.append(" and ca.enviadoProtestoDefinitivo  = true");
		
		if(jaEnviadoPorEmail){
			sql.append(" and ca.enviadoPorEmailProtesto = true");
		}else{
			sql.append(" and (ca.enviadoPorEmailProtesto = false or ca.enviadoPorEmailProtesto is null)");
		}
		
		Query query = em.createQuery(sql.toString());
		List<ContratoAluno> cas = (List<ContratoAluno>) query.getResultList();
		
		for(ContratoAluno ca: cas){
			ca.getBoletos().size();
		}

		return cas;
	}
	
	
	public List<Aluno> findAlunosComContratoEm(int ano) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct(ca.aluno) from ContratoAluno ca");
		sql.append(" where 1 = 1");
		sql.append(" and ca.ano = ");
		sql.append(ano);
		
		Query query = em.createQuery(sql.toString());
		List<Aluno> cas = (List<Aluno>) query.getResultList();

		return cas;
	}

	public boolean temContratoNoAno(Aluno aluno, int anoRematricula) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select count(ca.id) from ContratoAluno ca");
		sql.append(" where 1 = 1");
		sql.append(" and ca.ano = ");
		sql.append(anoRematricula);
		sql.append(" and ca.cancelado = false ");
		sql.append(" and ca.aluno.id = ");
		sql.append(aluno.getId());
		
		Query query = em.createQuery(sql.toString(),Long.class);
		Long cas =  (Long) query.getSingleResult();
		if(cas>0){
			return true;
		}
		return false;
	}

	public void gerarUsuariosAlunos() {
		List<Aluno> als = findAlunosComContratoEm(getConfiguracao().getAnoLetivo());
		
		for(Aluno al : als){
			Member m = new Member();
			m.setName(al.getNomeAluno());
			m.setIdCrianca1(al.getId()+"");
			m.setSenha(al.getUltimoContrato().getCpfResponsavel());
			m.setLogin(al.getUltimoContrato().getCpfResponsavel());
			m.setTipoMembro(TipoMembro.ALUNO);
			if(al.getSerie().ordinal()<4){
				m.setInfantil(true);
			}else{
				m.setInfantil(false);
			}
			
			em.persist(m);
			em.flush();
		}
		
	}
	
}


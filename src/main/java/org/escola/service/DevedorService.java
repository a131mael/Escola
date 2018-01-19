
package org.escola.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.escola.enums.StatusBoletoEnum;
import org.escola.model.Aluno;
import org.escola.model.Boleto;
import org.escola.model.Devedor;
import org.escola.util.Service;
import org.escola.util.UtilFinalizarAnoLetivo;
import org.escola.util.Verificador;

@Stateless
public class DevedorService extends Service {

	@Inject
	private Logger log;

	@Inject
	private EventoService eventoService;

	@Inject
	private UtilFinalizarAnoLetivo finalizarAnoLetivo;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public Devedor findById(EntityManager em, Long id) {
		return em.find(Devedor.class, id);
	}

	public Aluno findById(Long id) {
		Aluno dev =em.find(Aluno.class, id);
		dev.getBoletos().size();
		return dev;
	}

	public List<Aluno> findAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aluno> criteria = cb.createQuery(Aluno.class);
			Root<Aluno> member = criteria.from(Aluno.class);
			// Swap criteria statements if you would like to try out type-safe
			// criteria queries, a new
			// feature in JPA 2.0
			// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
			criteria.select(member).orderBy(cb.asc(member.get("nome")));
			
			List<Aluno> dvs = new ArrayList<>();
			for(Aluno dev : em.createQuery(criteria).getResultList()){
				dev.getBoletos().size();
				dvs.add(dev);
			}
			
			return dvs;

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public Devedor save(Aluno aluno) {
		Aluno user = null;
		try {

			log.info("Registering " + aluno.getNomeAluno());

			if (aluno.getId() != null && aluno.getId() != 0L) {
				user = findById(aluno.getId());
			} 
//			 user.setEnviadoParaCobrancaCDL(aluno.getEnviadoParaCobrancaCDL());
//			user.setNomeAluno(aluno.getNomeAluno());
//			user.setEndereco(aluno.getEndereco());
//			user.setBairro(aluno.getBairro());
//			user.setCep(aluno.getCep());
//			user.setCidade(aluno.getCidade());
//			user.setBairro(aluno.getBairro());
//			user.setCep(aluno.getCep());
//			user.setCidade(aluno.getCidade());
//			user.setCpf(aluno.getCpf());
//			user.setTelefoneCelular(aluno.getTelefoneCelular());
//			user.setTelefoneCelular2(aluno.getTelefoneCelular2());
//			user.setTelefoneResidencial(aluno.getTelefoneResidencial());
//			user.setEnviadoSPC(aluno.getEnviadoSPC());
//			user.setEnviadoParaCobrancaCDL(aluno.getEnviadoParaCobrancaCDL());
//			user.setContratoTerminado(aluno.getContratoTerminado());
//			user.setObservacao(aluno.getObservacao());
			/*
			List<Boleto> bs = new ArrayList<>();
			if(aluno.getBoletos() != null){
				for(Boleto b : aluno.getBoletos()){
					if(b.getNumero() != null && !b.getNumero().equalsIgnoreCase("")){
						bs.add(getBoletoAttached(b));
					}
				}
			}
			
			if (aluno.getRemovido() == null) {
				user.setRemovido(false);
			} else {
				user.setRemovido(aluno.getRemovido());
			}


			em.persist(user);
			user.setBoletos(bs);*/

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

		return null;
	}

	private Boleto getBoletoAttached(Boleto boleto){
		/*String numero = boleto.getNumero();
		String numeroContrato = boleto.getNumeroContrato();
		Date dataGeracao =boleto.getDataGeracao();
		Double valor = boleto.getValor();
		
		if(boleto.getId() != null){
			boleto  =em.find(Boleto.class, boleto.getId());
			boleto.setDataGeracao(dataGeracao);
			boleto.setNumeroContrato(numeroContrato);
			boleto.setDataGeracao(dataGeracao);
			boleto.setValor(valor);*/
	/*	}
		return boleto;*/
		return null;
	}
	
	public String remover(Long idDevedor) {
		/*Aluno al = findById(idDevedor);
		al.setRemovido(true);
		em.persist(al);*/
		return "ok";
	}

	public String restaurar(Long idDevedor) {
		/*Devedor al = findById(idDevedor);
		al.setRemovido(false);
		em.persist(al);*/
		return "ok";
	}


	@SuppressWarnings("unchecked")
	public List<Aluno> find(int first, int size, String orderBy, String order, Map<String, Object> filtros) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Aluno> criteria = cb.createQuery(Aluno.class);
			Root<Aluno> member = criteria.from(Aluno.class);
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
		//	q.setMaxResults(size);
			boolean atrasado = false;
			List<Aluno> ds = new LinkedList<>();
			for(Aluno d : (List<Aluno>) q.getResultList()){
				d.getBoletos().size();
				System.out.println(d.getNomeAluno());
				for(Boleto b : d.getBoletos() ){
					if(b.getId()==3447L){
						System.out.println("a");
					}

					if(Verificador.getStatusEnum(b).equals(StatusBoletoEnum.ATRASADO)){
						b.setAtrasado(true);
						atrasado = true;
						
					}else{
						b.setAtrasado(false);
					}
				}
				if(atrasado){
					ds.add(d);
				}
				atrasado = false;
			}
			
			return ds;

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
			Root<Aluno> member = countQuery.from(Aluno.class);
			countQuery.select(cb.count(member));

			if (filtros != null) {
				for (Map.Entry<String, Object> entry : filtros.entrySet()) {

					Predicate pred = cb.and();
					if (entry.getValue() instanceof String) {
						pred = cb.and(pred, cb.like(member.<String> get(entry.getKey()), "%" + entry.getValue() + "%"));
					} else {
						pred = cb.equal(member.get(entry.getKey()), entry.getValue());
					}
					countQuery.where(pred);
				}

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

	public void saveObservacao(Aluno alunod) {
		Aluno aluno = findById(alunod.getId());
		aluno.setObservacaoSecretaria(alunod.getObservacaoSecretaria());
		em.merge(aluno);
		
	}

	
}

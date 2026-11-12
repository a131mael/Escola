package org.escola.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.escola.model.NotaFiscal;
import org.escola.util.Service;

@Stateless
public class NotaFiscalService extends Service {

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public NotaFiscal findById(Long id) {
		return em.find(NotaFiscal.class, id);
	}

	public NotaFiscal save(NotaFiscal nota) {
		if (nota.getId() == null) {
			em.persist(nota);
			em.flush();
			return nota;
		}
		NotaFiscal merged = em.merge(nota);
		em.flush();
		return merged;
	}

	@SuppressWarnings("unchecked")
	public List<NotaFiscal> findByMesAno(int mes, int ano) {
		Calendar inicio = Calendar.getInstance();
		inicio.set(ano, mes - 1, 1, 0, 0, 0);
		inicio.set(Calendar.MILLISECOND, 0);

		Calendar fim = Calendar.getInstance();
		fim.set(ano, mes - 1, inicio.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);

		try {
			Query query = em.createQuery(
					"SELECT n FROM NotaFiscal n WHERE n.dataEnvio BETWEEN :inicio AND :fim ORDER BY n.dataEnvio DESC");
			query.setParameter("inicio", inicio.getTime());
			query.setParameter("fim", fim.getTime());
			return query.getResultList();
		} catch (NoResultException nre) {
			return new ArrayList<>();
		}
	}

	public NotaFiscal findUltimaPorBoleto(Long boletoId) {
		try {
			Query query = em.createQuery(
					"SELECT n FROM NotaFiscal n WHERE n.boleto.id = :boletoId ORDER BY n.id DESC");
			query.setParameter("boletoId", boletoId);
			query.setMaxResults(1);
			List<NotaFiscal> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (NoResultException nre) {
			return null;
		}
	}

	/** Total de notas autorizadas no mes/ano (pro quadro "total por mes" pedido). */
	public long contarAutorizadasMesAno(int mes, int ano) {
		List<NotaFiscal> notas = findByMesAno(mes, ano);
		long total = 0;
		for (NotaFiscal n : notas) {
			if ("AUTORIZADA".equals(n.getStatus())) {
				total++;
			}
		}
		return total;
	}

	public void atualizarAutorizada(NotaFiscal nota, String numero, String chaveAcesso, String xmlRetorno) {
		nota.setStatus("AUTORIZADA");
		nota.setNumero(numero);
		nota.setChaveAcesso(chaveAcesso);
		nota.setXmlNfseRetorno(xmlRetorno);
		nota.setDataAutorizacao(new Date());
		save(nota);
	}

	public void atualizarRejeitada(NotaFiscal nota, String motivo) {
		atualizarRejeitada(nota, motivo, null);
	}

	public void atualizarRejeitada(NotaFiscal nota, String motivo, String xmlRetorno) {
		nota.setStatus("REJEITADA");
		nota.setMotivoRejeicao(motivo);
		if (xmlRetorno != null) {
			nota.setXmlNfseRetorno(xmlRetorno);
		}
		save(nota);
	}

}

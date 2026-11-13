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

	/** Boletos do aluno, pagos no mes/ano informado, que AINDA NAO tem NFS-e autorizada --
	 * usado pra decidir se o botao "Emitir Nota" fica habilitado na listagem de alunos.
	 * Query direta (sem rebuscar o aluno inteiro) pra nao pesar numa tabela paginada. */
	@SuppressWarnings("unchecked")
	public List<org.escola.model.Boleto> findBoletosPagosMesSemNota(Long alunoId, int mes, int ano) {
		Calendar inicio = Calendar.getInstance();
		inicio.set(ano, mes - 1, 1, 0, 0, 0);
		inicio.set(Calendar.MILLISECOND, 0);

		Calendar fim = Calendar.getInstance();
		fim.set(ano, mes - 1, inicio.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
		fim.set(Calendar.MILLISECOND, 999);

		try {
			Query query = em.createQuery("SELECT b FROM Boleto b WHERE b.contrato.aluno.id = :alunoId "
					+ "AND b.dataPagamento BETWEEN :inicio AND :fim "
					+ "AND (b.cancelado IS NULL OR b.cancelado = false) "
					+ "AND NOT EXISTS (SELECT n FROM NotaFiscal n WHERE n.boleto = b AND n.status = 'AUTORIZADA')");
			query.setParameter("alunoId", alunoId);
			query.setParameter("inicio", inicio.getTime());
			query.setParameter("fim", fim.getTime());
			return query.getResultList();
		} catch (NoResultException nre) {
			return new ArrayList<>();
		}
	}

	/** NFS-e AUTORIZADA do aluno no mes/ano informado (se existir) -- pra mostrar os
	 * botoes de download/envio em vez do botao "Emitir" na listagem de alunos. */
	public NotaFiscal findNotaAutorizadaMesAtual(Long alunoId, int mes, int ano) {
		Calendar inicio = Calendar.getInstance();
		inicio.set(ano, mes - 1, 1, 0, 0, 0);
		inicio.set(Calendar.MILLISECOND, 0);

		Calendar fim = Calendar.getInstance();
		fim.set(ano, mes - 1, inicio.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
		fim.set(Calendar.MILLISECOND, 999);

		try {
			Query query = em.createQuery("SELECT n FROM NotaFiscal n WHERE n.boleto.contrato.aluno.id = :alunoId "
					+ "AND n.status = 'AUTORIZADA' AND n.dataAutorizacao BETWEEN :inicio AND :fim "
					+ "ORDER BY n.dataAutorizacao DESC");
			query.setParameter("alunoId", alunoId);
			query.setParameter("inicio", inicio.getTime());
			query.setParameter("fim", fim.getTime());
			query.setMaxResults(1);
			List<NotaFiscal> resultado = query.getResultList();
			return resultado.isEmpty() ? null : resultado.get(0);
		} catch (NoResultException nre) {
			return null;
		}
	}

	public boolean existeComChaveAcesso(String chaveAcesso) {
		try {
			Query query = em.createQuery("SELECT COUNT(n) FROM NotaFiscal n WHERE n.chaveAcesso = :chave");
			query.setParameter("chave", chaveAcesso);
			Long count = (Long) query.getSingleResult();
			return count != null && count > 0;
		} catch (Exception e) {
			return false;
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

	/** Soma do valor das notas AUTORIZADAS no mes/ano (relatorio de total faturado). */
	public double somarValorAutorizadoMesAno(int mes, int ano) {
		List<NotaFiscal> notas = findByMesAno(mes, ano);
		double total = 0;
		for (NotaFiscal n : notas) {
			if ("AUTORIZADA".equals(n.getStatus()) && n.getValor() != null) {
				total += n.getValor();
			}
		}
		return total;
	}

	/** So as notas AUTORIZADAS do mes/ano, mais recentes primeiro (pra lista detalhada do relatorio). */
	public List<NotaFiscal> findAutorizadasMesAno(int mes, int ano) {
		List<NotaFiscal> resultado = new ArrayList<>();
		for (NotaFiscal n : findByMesAno(mes, ano)) {
			if ("AUTORIZADA".equals(n.getStatus())) {
				resultado.add(n);
			}
		}
		return resultado;
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

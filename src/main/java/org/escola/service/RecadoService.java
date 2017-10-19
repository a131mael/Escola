package org.escola.service;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.escola.enums.Serie;
import org.escola.enums.TipoDestinatario;
import org.escola.enums.TipoMembro;
import org.escola.model.Member;
import org.escola.model.Recado;
import org.escola.model.RecadoDestinatario;
import org.escola.util.Constants;
import org.escola.util.Herald;
import org.escola.util.Service;

@Stateless
public class RecadoService extends Service {

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	@Inject
	private UsuarioService usuarioService;

	public Recado findById(EntityManager em, Long id) {
		return em.find(Recado.class, id);
	}

	public Recado findById(Long id) {
		return em.find(Recado.class, id);
	}

	public Recado findByCodigo(Long id) {
		return em.find(Recado.class, id);
	}

	public String remover(Long idRecado) {
		em.remove(findById(idRecado));
		return "index";
	}

	public List<Recado> findAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Recado> criteria = cb.createQuery(Recado.class);
			Root<Recado> member = criteria.from(Recado.class);
			// Swap criteria statements if you would like to try out type-safe
			// criteria queries, a new
			// feature in JPA 2.0
			// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
			criteria.select(member).orderBy(cb.asc(member.get("id")));
			return em.createQuery(criteria).getResultList();

		} catch (NoResultException nre) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public Recado save(Recado recado) {
		Recado user = null;
		try {

			log.info("Registering " + recado.getNome());

			if (recado.getId() != null && recado.getId() != 0L) {
				user = findById(recado.getId());
			} else {
				user = new Recado();
			}

			user.setDataFim(recado.getDataFim());
			user.setDataInicio(recado.getDataInicio());
			user.setDescricao(recado.getDescricao());
			user.setNome(recado.getNome());
			user.setCodigo(recado.getCodigo());
			user.setBigQuestion(recado.isBigQuestion());
			user.setDescricaoUnder(recado.getDescricaoUnder());
			user.setFilePergunta(recado.getFilePergunta());
			user.setFontSizeQuestion(recado.getFontSizeQuestion());
			user.setId(recado.getId());
			user.setOpcao1(recado.getOpcao1());
			user.setOpcao2(recado.getOpcao2());
			user.setOpcao3(recado.getOpcao3());
			user.setOpcao4(recado.getOpcao4());
			user.setOpcao5(recado.getOpcao5());
			user.setOpcao6(recado.getOpcao6());
			user.setRespostaBooleana(recado.isRespostaBooleana());
			user.setQuestionario(recado.isQuestionario());
			user.setPrecisaDeResposta(recado.isPrecisaDeResposta());
			user.setRespostaAberta(recado.getRespostaAberta());
			user.setTipoDestinatario(recado.getTipoDestinatario());
			user.setDataParaExibicao(recado.getDataInicio() != null ? recado.getDataInicio() : new Date());

			enviarMensagemAPP(recado);

			em.persist(user);

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

		return user;
	}

	private void enviarMensagemAPP(final Recado recado) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Member> destinatarios = new ArrayList<>();
				switch (recado.getTipoDestinatario()) {

				case TODOS:
					destinatarios = usuarioService.findAllWithToken(recado.getTipoDestinatario());
					break;

				case PROFESSORES:
					destinatarios = usuarioService.findAllWithToken(recado.getTipoDestinatario());
					break;

				case MEMBRO:
					// destinatarios =
					// usuarioService.findAllWithToken(recado.getTipoDestinatario());
					break;

				case PRIMEIRO:
					destinatarios = usuarioService.findAllWithToken(Serie.PRIMEIRO_ANO);
					break;

				case SEGUNDO:
					destinatarios = usuarioService.findAllWithToken(Serie.SEGUNDO_ANO);
					break;

				case TERCEIRO:
					destinatarios = usuarioService.findAllWithToken(Serie.TERCEIRO_ANO);
					break;

				case QUARTO:
					destinatarios = usuarioService.findAllWithToken(Serie.QUARTO_ANO);
					break;

				case QUINTO:
					destinatarios = usuarioService.findAllWithToken(Serie.QUINTO_ANO);
					break;

				case SEXTO:
					destinatarios = usuarioService.findAllWithToken(Serie.SEXTO_ANO);
					break;

				case SETIMO:
					destinatarios = usuarioService.findAllWithToken(Serie.SETIMO_ANO);
					break;

				case OITAVO:
					destinatarios = usuarioService.findAllWithToken(Serie.OITAVO_ANO);
					break;

				case NONO:
					destinatarios = usuarioService.findAllWithToken(Serie.NONO_ANO);
					break;

				default:
					break;
				}

				// Envia mensagem para cada destinatario da lista
				for (Member m : destinatarios) {
					Herald.sendFCMMessage(Constants.FCM_URL_POST, Constants.FCM_KEY_APP, Constants.FCM_PRIORITY_HIGH,
							"Colégio Adonai", recado.getNome(), m.getTokenFCM());
					System.out.println("Envio Mensagem para...  :" + m.getTokenFCM());
				}
			}
		}).start();

	}

	public int getQtadeMensagensRespondidas(Recado recado) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(rd) from  RecadoDestinatario rd ");
			sql.append("where rd.recado.id = ");
			sql.append(recado.getId());

			Query query = em.createQuery(sql.toString());

			Object sqlReturn = query.getSingleResult();
			if (sqlReturn != null) {
				return (int) sqlReturn;
			} else {
				return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getTotalProfessores() {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(m) from  Member m ");
			sql.append("where m.tipoMembro = ");
			sql.append(TipoMembro.PROFESSOR.ordinal());
			sql.append(" and m.desabilitado = false");

			Query query = em.createQuery(sql.toString());

			Object sqlReturn = query.getSingleResult();
			if (sqlReturn != null) {
				Long ret = (Long) sqlReturn;
				if(ret <1){
					return 1;
				}else{
					return  ret.intValue();
				}
			} else {
				return 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	public int getTotalResponsaveis(Recado recado) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(m) from  Member m ");
			sql.append("where m.tipoMembro = ");
			sql.append(TipoMembro.ALUNO.ordinal());
			sql.append(" and m.desabilitado = false");

			Query query = em.createQuery(sql.toString());

			Object sqlReturn = query.getSingleResult();
			if (sqlReturn != null) {
				Long ret = (Long) sqlReturn;
				if(ret <1){
					return 1;
				}else{
					return ret.intValue();
				}
			} else {
				return 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	public int getTotalSim(Recado recado) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(rd) from  RecadoDestinatario rd ");
			sql.append("where rd.recado.id = ");
			sql.append(recado.getId());
			sql.append(" and rd.resposta = 1");

			Query query = em.createQuery(sql.toString());

			Object sqlReturn = query.getSingleResult();
			if (sqlReturn != null) {
				return (int) sqlReturn;
			} else {
				return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getTotalNao(Recado recado) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(rd) from  RecadoDestinatario rd ");
			sql.append("where rd.recado.id = ");
			sql.append(recado.getId());
			sql.append(" and rd.resposta = 2");

			Query query = em.createQuery(sql.toString());

			Object sqlReturn = query.getSingleResult();
			if (sqlReturn != null) {
				return (int) sqlReturn;
			} else {
				return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public List<Member> getMemberRespondeu(Recado recado, int resposta) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT rd.destinatario from  RecadoDestinatario rd ");
			sql.append("where rd.recado.id = ");
			sql.append(recado.getId());
			sql.append(" and rd.resposta = ");
			sql.append(resposta);

			Query query = em.createQuery(sql.toString());

			Object sqlReturn = query.getSingleResult();
			if (sqlReturn != null) {
				return (List<Member>) sqlReturn;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public List<RecadoDestinatario> getMemberRespondeu(Recado recado) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT rd from  RecadoDestinatario rd ");
			sql.append("where rd.recado.id = ");
			sql.append(recado.getId());

			Query query = em.createQuery(sql.toString());

			Object sqlReturn = query.getResultList();
			if (sqlReturn != null) {
				return (List<RecadoDestinatario>) sqlReturn;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Member> getMemberRespondeu(Recado recado, TipoDestinatario destinatario, int resposta) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT rd.destinatario from  RecadoDestinatario rd ");
			sql.append("where rd.recado.id = ");
			sql.append(recado.getId());
			sql.append(" and rd.resposta = ");
			sql.append(resposta);
			sql.append(" and rd.recado.tipoDestinatario = ");
			sql.append(destinatario.ordinal());
			Query query = em.createQuery(sql.toString());

			Object sqlReturn = query.getSingleResult();
			if (sqlReturn != null) {
				return (List<Member>) sqlReturn;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Member> getMemberRespondeu(Recado recado, TipoMembro tipoMembro) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT rd.destinatario from  RecadoDestinatario rd ");
			sql.append("where rd.recado.id = ");
			sql.append(recado.getId());
			sql.append(" and rd.destinatario.tipoMembro = ");
			sql.append(tipoMembro.ordinal());
			Query query = em.createQuery(sql.toString());

			Object sqlReturn = query.getResultList();
			if (sqlReturn != null) {
				return (List<Member>) sqlReturn;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Recado findByCodigo(String codigo) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Recado> criteria = cb.createQuery(Recado.class);
			Root<Recado> member = criteria.from(Recado.class);

			Predicate whereSerie = null;

			whereSerie = cb.equal(member.get("codigo"), codigo);
			criteria.select(member).where(whereSerie);

			criteria.select(member);
			return em.createQuery(criteria).getSingleResult();

		} catch (NoResultException nre) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Recado> find(int first, int size, String orderBy, String order, Map<String, Object> filtros) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Recado> criteria = cb.createQuery(Recado.class);
			Root<Recado> member = criteria.from(Recado.class);
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
			return (List<Recado>) q.getResultList();

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
			Root<Recado> member = countQuery.from(Recado.class);
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

}

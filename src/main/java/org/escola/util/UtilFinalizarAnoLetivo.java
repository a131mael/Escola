package org.escola.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.escola.model.Evento;
import org.escola.service.EventoService;


@Stateless
public class UtilFinalizarAnoLetivo {

	
	@Inject
	private Logger log;

	@Inject
	private EventoService eventoService;

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;
	
	public static void main(String[] args) {
		UtilFinalizarAnoLetivo finalizarAnoLetivo = new UtilFinalizarAnoLetivo();
		finalizarAnoLetivo.mudaDataDosEventosParaAnoLetivoAtual();
	}
	
	public void mudaDataDosEventosParaAnoLetivoAtual(){
		List<Evento> todosEventos = eventoService.findAll();
		for(Evento evento : todosEventos){
			evento.setDataFim(mudarAno(evento.getDataFim(), Constant.anoLetivoAtual));
			evento.setDataInicio(mudarAno(evento.getDataInicio(), Constant.anoLetivoAtual));
			em.persist(evento);
		}
	}

	public Date mudarAno(Date data, int ano){
		if(data != null){
			Calendar c = Calendar.getInstance();
			c.setTime(data);
			c.set(Calendar.YEAR, ano); 
			return c.getTime();	
		}
		return data;
	}
}

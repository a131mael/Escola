/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.escola.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.escola.model.Evento;
import org.escola.service.AlunoService;
import org.escola.service.EventoService;
import org.escola.util.UtilFinalizarAnoLetivo;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Named
@ViewScoped
public class CalendarioController implements Serializable{

	/****/
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private ScheduleModel eventModel;
	private ScheduleEvent event = new DefaultScheduleEvent();
		
	@Inject
    private AlunoService alunoService;

	@Inject
    private EventoService eventoService;

	@Produces
	@Named
	private Evento evento;
	
	@Inject
	private UtilFinalizarAnoLetivo mudarDatas;
	
	
	@PostConstruct
	private void init() {
		mudarDatas.mudaDataDosEventosParaAnoLetivoAtual();
		evento = new Evento();
		 setEventModel(new DefaultScheduleModel());
		 montarAgenda();
	        /*getEventModel().addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
	        getEventModel().addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));
	        getEventModel().addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", nextDay9Am(), nextDay11Am()));
	        getEventModel().addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));*/
	}

	private List<Evento> getEventos(){
		List<Evento> eventos = eventoService.findAll();
		return eventos;
	}

	private void montarAgenda(){
		  Calendar date = Calendar.getInstance();
		  Calendar dateFim = Calendar.getInstance();

		for(Evento evento : getEventos()){
			if(evento.getDataFim() != null && evento.getDataInicio() != null){
				date.setTime(evento.getDataInicio());
				date.add(Calendar.DAY_OF_MONTH, 1);
				dateFim.setTime(evento.getDataFim());
				dateFim.add(Calendar.DAY_OF_MONTH, 1);
				
				ScheduleEvent ev = new DefaultScheduleEvent(evento.getNome(),date.getTime(),dateFim.getTime());	
				getEventModel().addEvent(ev);	
			}
			
		}
	}
	
	public String salvar(){
		eventoService.save(evento);
		return "index";
	}
	
	public String voltar(){
		return "index";
	}
	
	public String editar(Long idprof){
		//event = alunoService.findById(idprof);
		//Util.addAtributoSessao("aluno", aluno);
		return "cadastrar";
	}	
	
	public String remover(Long idTurma){
		alunoService.remover(idTurma);
		return "index";
	}
	
	public String adicionarNovo(){
		return "cadastrar";
	}
	
	public String cadastrarNovo(){
		
		return "eventoAluno";
	}

	
	public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);    //set random day of month
         
        return date.getTime();
    }
     
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
	 private Calendar today() {
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
	 
	        return calendar;
	    }
	     
	    private Date previousDay8Pm() {
	        Calendar t = (Calendar) today().clone();
	        t.set(Calendar.AM_PM, Calendar.PM);
	        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
	        t.set(Calendar.HOUR, 8);
	         
	        return t.getTime();
	    }
	     
	    private Date previousDay11Pm() {
	        Calendar t = (Calendar) today().clone();
	        t.set(Calendar.AM_PM, Calendar.PM);
	        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
	        t.set(Calendar.HOUR, 11);
	         
	        return t.getTime();
	    }
	     
	    private Date today1Pm() {
	        Calendar t = (Calendar) today().clone();
	        t.set(Calendar.AM_PM, Calendar.PM);
	        t.set(Calendar.HOUR, 1);
	         
	        return t.getTime();
	    }
	     
	    private Date theDayAfter3Pm() {
	        Calendar t = (Calendar) today().clone();
	        t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);     
	        t.set(Calendar.AM_PM, Calendar.PM);
	        t.set(Calendar.HOUR, 3);
	         
	        return t.getTime();
	    }
	 
	    private Date today6Pm() {
	        Calendar t = (Calendar) today().clone(); 
	        t.set(Calendar.AM_PM, Calendar.PM);
	        t.set(Calendar.HOUR, 6);
	         
	        return t.getTime();
	    }
	     
	    private Date nextDay9Am() {
	        Calendar t = (Calendar) today().clone();
	        t.set(Calendar.AM_PM, Calendar.AM);
	        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
	        t.set(Calendar.HOUR, 9);
	         
	        return t.getTime();
	    }
	     
	    private Date nextDay11Am() {
	        Calendar t = (Calendar) today().clone();
	        t.set(Calendar.AM_PM, Calendar.AM);
	        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
	        t.set(Calendar.HOUR, 11);
	         
	        return t.getTime();
	    }
	     
	    private Date fourDaysLater3pm() {
	        Calendar t = (Calendar) today().clone(); 
	        t.set(Calendar.AM_PM, Calendar.PM);
	        t.set(Calendar.DATE, t.get(Calendar.DATE) + 4);
	        t.set(Calendar.HOUR, 3);
	         
	        return t.getTime();
	    }
	     
	    public ScheduleEvent getEvent() {
	        return event;
	    }
	 
	    public void setEvent(ScheduleEvent event) {
	        this.event = event;
	    }
	     
	    public void addEvent(ActionEvent actionEvent) {
	        if(event.getId() == null)
	            getEventModel().addEvent(event);
	        else
	            getEventModel().updateEvent(event);
	         
	        event = new DefaultScheduleEvent();
	    }
	     
	    public void onEventSelect(SelectEvent selectEvent) {
	        event = (ScheduleEvent) selectEvent.getObject();
	    }
	     
	    public void onDateSelect(SelectEvent selectEvent) {
	        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	    }
	     
	    public void onEventMove(ScheduleEntryMoveEvent event) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
	         
	        addMessage(message);
	    }
	     
	    public void onEventResize(ScheduleEntryResizeEvent event) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
	         
	        addMessage(message);
	    }
	     
	    private void addMessage(FacesMessage message) {
	        FacesContext.getCurrentInstance().addMessage(null, message);
	    }


		public ScheduleModel getEventModel() {
			return eventModel;
		}


		public void setEventModel(ScheduleModel eventModel) {
			this.eventModel = eventModel;
		}

		public Evento getEvento() {
			return evento;
		}

		public void setEvento(Evento evento) {
			this.evento = evento;
		}
}

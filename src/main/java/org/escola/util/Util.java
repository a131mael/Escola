package org.escola.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.escola.model.Boleto;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class Util {

	public static String criarEspacos(int quantidade){
		StringBuilder sb = new StringBuilder();
		for(int i= 0; i<quantidade; i++){
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public static String getRequestParam(String param) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return req.getParameter(param);
	}

	public static void addAtributoSessao(String nome, Object valor) {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		session.setAttribute(nome, valor);
	}
	
	public void cleanSession(){
		System.out.println("Limpar sessao");
	}

	public static Object getAtributoSessao(String nome) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		req.getRequestURL().toString();
		req.getRequestURI().toString();
		req.getContextPath();
		req.getPathInfo();
		req.getQueryString();
		req.getParameter(nome);
		req.getAttributeNames();
		req.getHeaderNames();
		req.getParameterMap();
		try {
			req.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpSession session = (HttpSession) req.getSession();
		Object obj = session.getAttribute(nome);
		session.getAttributeNames();
		return obj;
	}

	public Object getQueryValue(String param) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		Object obj = req.getParameter(param); 
		return obj;
	}
	
	
	public static void removeAtributoSessao(String nome) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) req.getSession();
		session.removeAttribute(nome);

	}
	
	public static Map<String, String> getQueryMap(String query)  
	{  
	    String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params)  
	    {  
	        String name = param.split("=")[0];  
	        String value = param.split("=")[1];  
	        map.put(name, value);  
	    }  
	    return map;  
	}
	
	public static long diferencaEntreDatas(Date data1, Date data2) {
		DateTime dt1 = new DateTime(data1.getTime());
		DateTime dt2 = new DateTime(data2.getTime());

		Days d = Days.daysBetween(dt2, dt1);
		int days = d.getDays();
		return days;
	}
	
	public static String formatarDouble2Decimais(double valor){
		String sb=  String.format("%.2f", valor);
		
		return sb;
	}
	
	public static List<Boleto> inverterArray(List<Boleto> boletos){
		List<Boleto> boletosAux = new ArrayList<>();
		for(Boleto bol : boletos){
			boletosAux.add(0,bol);
		}
		return boletosAux;
	}

	public static Date getDataInicioMes(int mes, int ano){
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTime(new Date());
		dataInicio.set(Calendar.MONTH, mes -1); 
		dataInicio.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio.set(Calendar.YEAR, ano);
		
		return dataInicio.getTime();
	}
	
	public static Date getDataFimMes(int mes, int ano){
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTime(new Date());
		dataInicio.set(Calendar.YEAR, ano);
		dataInicio.set(Calendar.MONTH, mes -1); 
		
		int  ultimo = dataInicio.getActualMaximum(Calendar.DAY_OF_MONTH);
		dataInicio.set(Calendar.DAY_OF_MONTH, ultimo); 
		dataInicio.set(Calendar.HOUR_OF_DAY, 23); 


		return dataInicio.getTime();
	}
	
	public static String getDataFimMesString(int mes, int ano){
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String dataFormatada = formato.format(getDataFimMes(mes,ano));
		return dataFormatada;
	}
	
	public static String getDataInicioMesString(int mes, int ano){
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String dataFormatada = formato.format(getDataInicioMes(mes,ano));
		return dataFormatada;
	}
	
}

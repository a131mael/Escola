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
package org.escola.auth;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.escola.enums.TipoMembro;
import org.escola.model.Member;

import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class AuthController implements Serializable {

	private final long serialVersionUID = 1L;

	private Member loggedUser;

	@Produces
	@Named
	private Member authUser;

	@PostConstruct
	public void initNewMember() {
		authUser = new Member();
	}

	/*public boolean hasPermission(TipoMembro... membros) {
		for (TipoMembro membro : membros) {
			if (getLoggedUser().getTipoMembro().equals(membro)) {
				return true;
			}
		}

		return false;
	}*/

	public boolean hasPermission(TipoMembro membro) {
		if (getLoggedUser().getTipoMembro().equals(membro)) {
			return true;
		}

		return false;

	}
	public boolean hasPermission(TipoMembro m1, TipoMembro m2) {
		if (getLoggedUser().getTipoMembro().equals(m1)) {
			return true;
		}
		if (getLoggedUser().getTipoMembro().equals(m2)) {
			return true;
		}
		

		return false;

	}

	public boolean hasPermission(TipoMembro m1, TipoMembro m2, TipoMembro m3) {
		if (getLoggedUser().getTipoMembro().equals(m1)) {
			return true;
		}
		if (getLoggedUser().getTipoMembro().equals(m2)) {
			return true;
		}
		
		if (getLoggedUser().getTipoMembro().equals(m3)) {
			return true;
		}
		
		return false;

	}

	public boolean hasPermission(TipoMembro m1, TipoMembro m2, TipoMembro m3, TipoMembro m4) {
		if (getLoggedUser().getTipoMembro().equals(m1)) {
			return true;
		}
		if (getLoggedUser().getTipoMembro().equals(m2)) {
			return true;
		}
		
		if (getLoggedUser().getTipoMembro().equals(m3)) {
			return true;
		}
		if (getLoggedUser().getTipoMembro().equals(m4)) {
			return true;
		}
		
		return false;

	}
	
	public String login() throws Exception {
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(authUser.getLogin(),
					authUser.getSenha().toCharArray(), true);
			SecurityUtils.getSubject().login(token);

			return getLoggedUser().getTipoMembro().getName();
		} catch (Exception ex) {
			// FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
			// "Erro", "Registration Fail");
			// facesContext.addMessage(null, m);
			ex.printStackTrace();
			return "erro";
		}
	}

	public Member getAuthUser() {
		return authUser;
	}

	public TipoMembro getTipoMembroLogado(){
		return getLoggedUser().getTipoMembro();
	}
	
	public boolean isTeacher(){
		return getLoggedUser().getTipoMembro().equals(TipoMembro.PROFESSOR);
	}
	
	public String logout() {
		try {
			SecurityUtils.getSubject().logout();
			/*
			 * FacesContext.getCurrentInstance().getExternalContext().
			 * invalidateSession();
			 */
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.invalidateSession();
			ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "/home.xhtml?faces-redirect=true";
		return "logout";
	}

	public void setAuthUser(Member authUser) {
		this.authUser = authUser;
	}

	public Member getLoggedUser() {
		try {
			if (loggedUser == null) {
				if (SecurityUtils.getSubject().getPrincipal() != null) {
					System.out.println("CONSTRUIU O USUARIO LOGADO !!");
					Member user = (Member) SecurityUtils.getSubject().getPrincipal();
					loggedUser = user;
				}
			}

			return loggedUser;

		} catch (Exception ex) {
			// Logger.getLogger(MemberController.class.getSimpleName()).log(Level.WARNING,
			// null, ex);
			ex.printStackTrace();
		}

		return null;
	}

	public static String getRequestParam(String param) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return req.getParameter(param);
	}

	public void addAtributoSessao(String nome, Object valor) {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		session.setAttribute(nome, valor);
	}

	public void cleanSession() {
		System.out.println("Limpar sessao");
	}

	public Object getAtributoSessao(String nome) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

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
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		Object obj = req.getParameter(param);
		return obj;
	}

	public void removeAtributoSessao(String nome) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) req.getSession();
		session.removeAttribute(nome);

	}

	public static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}
		return map;
	}

	public TipoMembro getSecretaria() {
		return TipoMembro.SECRETARIA;
	}

	public TipoMembro getProfessor() {
		return TipoMembro.PROFESSOR;
	}

	public TipoMembro getAluno() {
		return TipoMembro.ALUNO;
	}

	public TipoMembro getAdministrador() {
		return TipoMembro.SECRETARIA;
	}
	
}

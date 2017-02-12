/*
' * JBoss, Home of Professional Open Source
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
package org.escola.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.escola.enums.TipoMembro;
import org.escola.model.Member;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class MemberRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Member> memberEventSrc;
    

    public void register(Member member) throws Exception {
        log.info("Registering " + member.getName());
        em.persist(member);
        memberEventSrc.fire(member);
    }
    
    public Member login(Member member) throws Exception {
    	Member m = findMember(member);
    	
    	return m;
    }
    
    public Member findById(EntityManager em, Long id) {
		return em.find(Member.class, id);
	}

	public Member findById(Long id) {
		return em.find(Member.class, id);
	}
    
    private Member findMember(Member m){
    	Member member = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT m from  Member m ");
		sql.append("where (m.login = '");
		sql.append(m.getLogin());
		sql.append("' or m.email = '");
		sql.append(m.getEmail());
		sql.append("' ) and m.senha = '");
		sql.append(m.getSenha());
		sql.append("'");
		
		Query query = em.createQuery(sql.toString());
		 
		try{
			member = (Member) query.getSingleResult();
		}catch (NoResultException ne) {
			
		}catch (NonUniqueResultException nure) {
			System.out.println(nure);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return member;
    }
    
    private Member isInterMember(Member member) {
		boolean inter = false;
		if(member.getLogin().equalsIgnoreCase("admin")){
			if(member.getSenha().equalsIgnoreCase("12345")){
				member.setTipoMembro(TipoMembro.SECRETARIA);
				inter = true;
			}
		}
		if(member.getLogin().equalsIgnoreCase("professor")){
			if(member.getSenha().equalsIgnoreCase("12345")){
				member.setTipoMembro(TipoMembro.PROFESSOR);
				inter = true;
			}
		}
		return member;
	}
    
}

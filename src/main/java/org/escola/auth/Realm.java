package org.escola.auth;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.escola.model.Member;
import org.escola.service.MemberRegistration;
import org.escola.util.ServiceLocator;


/**
 *
 * @author abimael
 */
public class Realm extends AuthorizingRealm {
	
	private MemberRegistration userService;
	
	@PostConstruct
	public void initializy(){
		System.out.println("CONSTRUTOR ---------------------------------");
		setUserService(new MemberRegistration());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {

//        User usuario = (User) SecurityUtils.getSubject().getPrincipal();
//
//        SimpleAuthorizationInfo info = null;
//        
//        if (usuario != null) {
//        	info = new SimpleAuthorizationInfo();
//        }
//
//		return info;
		System.out.println("Autorizacao XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		try {
			MemberRegistration mr = (MemberRegistration) ServiceLocator.getInstance().getEjb(MemberRegistration.class.getSimpleName(),MemberRegistration.class.getName() );
			StringBuilder sb = new StringBuilder();
			
			for(char c : (char [])arg0.getCredentials()){
				sb.append(c);	
			}
			
			Member m = new Member();
			m.setLogin((String)arg0.getPrincipal());
			m.setSenha(sb.toString());
			Member member = mr.login(m);
			if(member != null){
				return new SimpleAuthenticationInfo(member, m.getSenha(), getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public MemberRegistration getUserService() {
		System.out.println("Autorizacao");
		return userService!= null?userService:new MemberRegistration();
	}

	public void setUserService(MemberRegistration userService) {
		System.out.println("Autorizacao");
		this.userService = userService;
	}

}
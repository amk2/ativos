package com.projtec.slingcontrol.infra;


import javax.servlet.http.HttpServletRequest;

import org.apache.click.extras.security.AccessController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;

public class Secure implements AccessController{

	 private static final long serialVersionUID = 1L;

	 public static boolean isAuthenticated() {
	  return SecurityUtils.getSubject().isAuthenticated();
	 }

	 public static boolean isPermitted(String permission) {
	  return isAuthenticated()
	    &&   SecurityUtils.getSubject().isPermitted(permission);
	 }
	 
	 public static void login(String user, String password) throws AuthenticationException {
		  UsernamePasswordToken token = new UsernamePasswordToken(user, password);
		  SecurityUtils.getSubject().login(token);
		  }

	@Override
	public boolean hasAccess(HttpServletRequest request, String resource) {
		return resource ==  null || Secure.isPermitted(resource); 
	}

}
	 

package com.projtec.slingcontrol.web.click;

import javax.servlet.http.HttpSession;

import org.apache.click.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class LogoutPage extends Page{

	
	
	
	   @Override
	    public void onInit() {
	        super.onInit();
	     
	        Subject subject = SecurityUtils.getSubject();

	        if (subject != null) {
	          
	            subject.logout();
	        }

	        HttpSession session = getContext().getRequest().getSession(false);
	        if (session != null) {
	            session.invalidate();
	        }
	        //setForward(url);
	        setForward(LoginPage.class);
	   }

}

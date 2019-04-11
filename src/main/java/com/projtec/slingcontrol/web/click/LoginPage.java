package com.projtec.slingcontrol.web.click;

import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.PasswordField;
import org.apache.click.control.TextField;
import org.apache.click.util.Bindable;
import org.apache.shiro.authc.AuthenticationException;
import org.bouncycastle.asn1.ocsp.Request;

import com.projtec.slingcontrol.infra.Secure;

public class LoginPage extends 	BorderLoginPage  implements Serializable {


	@Bindable
	public String title = getMessage("titulo");	
	
	private static final long serialVersionUID = 1L;
	
	private ImageSubmit okSubmit;

	private Form buttonsForm = new Form("buttonsForm");

	private Form form = new Form("form");
	@SuppressWarnings("unused")
	private HiddenField redirectField = new HiddenField("redirect",String.class);
	private Form optionsForm = new Form("optionsForm");

	public TextField usernameField = new TextField("username",true);
	public PasswordField passwordField = new PasswordField("password",true);

	
	public LoginPage() {
		addControl(form);
		addControl(optionsForm);
		addControl(buttonsForm);
		
		usernameField.setMaxLength(20);
		usernameField.setMinLength(2);
		usernameField.setFocus(true);
		usernameField.setRequired(true);
		usernameField.setLabel("Login: ");
		usernameField.setStyle("margin", "0");
		usernameField.setWidth("150px");
		usernameField.setAttribute("class", "validate[required]");
		
		passwordField.setMaxLength(20);
		passwordField.setMinLength(2);
		passwordField.setRequired(true);
		passwordField.setLabel("Senha: ");
		//passwordField.setStyle("margin", "0");
		//passwordField.setStyle("border-color","blue");
		passwordField.setStyle("border","1px solid black");
		passwordField.setWidth("150px");
		passwordField.setAttribute("class", "validate[required]");
		form.add(usernameField);
		form.add(passwordField);
		
		okSubmit = new ImageSubmit("ok", "/resources/images/btnEntrar.png");
		okSubmit.setListener(this, "onOkClicked");
        form.add(okSubmit);
        
		//form.add(redirectField);
	}
	

	@Override
	public void onInit() {

		//super.onInit();
	
		String username;
		
		if (getContext().isPost()) {
			username = getContext().getRequestParameter("username");
			
		} else {
			username = getContext().getCookieValue("username");
			
			if (username != null) {
				usernameField.setValue(username);
				usernameField.setFocus(true);
				passwordField.setFocus(false);
			}
		}
	}

	public boolean onOkClicked() {

		if (form.isValid()) {
			try {
				String nome = usernameField.getValue();
				String senha = passwordField.getValue();
				Secure.login(usernameField.getValue(), passwordField.getValue());
				
				nextPage();
			} catch (AuthenticationException e) {
				usernameField.setError("Login / Senha inv&aacute;lido");
				//form.setError("Login / Senha inv�lido");
			}
		}
		return true;
		
	}
	
	public boolean onLogoutClick() {
	    getContext().getSession().invalidate();
	    setRedirect(LoginPage.class);
	    return false;
	}

	private void nextPage() {
		HomePage nextPage = (HomePage)getContext().createPage(HomePage.class);
		setForward(nextPage);
	}


	@Override
	public Form getForm() {
		return null;
	}
	
}
package com.projtec.slingcontrol.web.click;

import org.apache.click.control.Form;
import org.springframework.stereotype.Component;


@Component
public class DemandasFormPage  extends BorderPage 
{

	@Override
	public Form getForm() {
		return null;
	}

	@Override
	protected boolean onPesquisaClick() {
		return false;
	}

	@Override
	protected boolean onCancelarClick() {
		return false;
	}

	@Override
	protected boolean onSalvarClick() {
		return false;
	}

	

}

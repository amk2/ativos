package com.projtec.slingcontrol.web.click;

import java.io.Serializable;
import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.Bindable;

public abstract class BorderLoginPage extends Page implements Serializable {
	private static final long serialVersionUID = -462703872616192856L;

	protected boolean test;

	private transient Menu rootMenu;

	@Bindable
	protected boolean showMenu = true;

	@Bindable
	public String title = getMessage("titulo");	
	/**
	 * @see org.apache.click.Page#onInit()
	 */
	@Override
	public void onInit() {
		super.onInit();
	}

	public abstract Form getForm();



}

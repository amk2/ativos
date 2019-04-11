package com.projtec.slingcontrol.web.click;

import java.io.Serializable;

import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;
import org.apache.click.util.Bindable;

import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.web.export.ExcelTableExporter;
import com.projtec.slingcontrol.web.export.ExportTable;
import com.projtec.slingcontrol.web.export.PDFTableExporter;

public abstract class BorderPage extends Page implements Serializable {
	private static final long serialVersionUID = -462703872616192856L;

	@Bindable
	protected ExportTable grid = new ExportTable();
	protected ExcelTableExporter excel = new ExcelTableExporter("Excel",
			"/resources/images/csv_file.png");
	protected PDFTableExporter pdf = new PDFTableExporter("Pdf",
			"/resources/images/pdf_file.png");

	@Bindable
	protected boolean showGrid;

	protected boolean test;

	private transient Menu rootMenu;

	@Bindable
	protected boolean showMenu = true;
	
	@Bindable
	protected String mensagemErro = new String();
	
	@Bindable
	protected String mensagemSucesso = new String();


	@Bindable
	public String title = getMessage("titulo");	
	
	
	public String getTemplate() {
		return "/border-template.htm";
	}

	/**
	 * @see org.apache.click.Page#onInit()
	 */
	@Override
	public void onInit() {
		super.onInit();
		
		mensagemErro = "";
		mensagemSucesso = "";
		/* Subm
		if (!test) {
			String acao = getContext().getRequestParameter("actionLink");
			if (acao != null) {
				if (acao.equalsIgnoreCase("Pesquisar")) {
					onPesquisaClick();
				} else if (acao.equalsIgnoreCase("Limpar")) {
					onCancelarClick();
				}
			}
		} */
		if (showMenu) {
			MenuFactory menuFactory = new MenuFactory();
			rootMenu = menuFactory.getRootMenu(new Secure());

			// Add rootMenu to Page control list. Note: rootMenu is removed in
			// Page
			// onDestroy() to ensure rootMenu is not serialized

			addControl(rootMenu);
		}
	}

	/**
	 * @see org.apache.click.Page#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// Remove menu for when BorderPage is serialized
		if (rootMenu != null) {
			removeControl(rootMenu);
		} 
	}

	public abstract Form getForm();

	protected abstract boolean onPesquisaClick();

	protected abstract boolean onCancelarClick();

	protected abstract boolean onSalvarClick();

}

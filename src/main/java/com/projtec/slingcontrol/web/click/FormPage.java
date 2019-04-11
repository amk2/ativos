package com.projtec.slingcontrol.web.click;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.HiddenField;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Table;
import org.apache.commons.lang.StringUtils;

import com.projtec.slingcontrol.web.exception.ValidacaoCampoException;
import com.projtec.slingcontrol.web.export.ExportTable;

public abstract class FormPage extends BorderPage {

	private static final long serialVersionUID = -7016724122264119986L;

	HiddenField imgField = new HiddenField("imgField", String.class);

	public FormPage() {

	}

	@Override
	public void onInit() {
		super.onInit();
	}

	@Override
	public void onRender() {
		
		String acao = getContext().getRequest().getParameter("actionLink");
		
		if (acao != null) {
			
			if (grid.getColumnList().size() == 0
					&& (acao.equalsIgnoreCase("Pesquisar") 
					|| acao.equalsIgnoreCase("grid-controlLink"))) {
				
				addPaginator();
				montarGrid();
				showGrid = true;
				
			} 
			else {
				
				removeGrid();
				showGrid = false;
				
			}
			
		}

	}

	@Deprecated
	public void montarBotoes() {

		// Isso � para manter o que j� est� pronto
		// Os UC atuais dever�o ser revistos para utilizarem o m�todo
		// montarBotoes(boolean, boolean, boolean)
		montarBotoes(true, true, true);

		/*
		 * 
		 * HiddenField idFields = new HiddenField("imageFields", String.class);
		 * idFields.setId("imageFields"); getForm().add(idFields); HiddenField
		 * acao = new HiddenField("actionLink", ""); getForm().add(acao);
		 * 
		 * ImageSubmit salvar = new ImageSubmit("Salvar",
		 * "/resources/images/btnSalvar.png"); salvar.setAttribute("onclick",
		 * "validar('Salvar')"); salvar.setDisabled(!permitidoSalvar());
		 * salvar.setStyle("border","1px solid black");
		 * salvar.setStyle("border-color","blue");
		 * 
		 * if (salvar.isDisabled()){
		 * salvar.setStyle("border","1px solid black");
		 * salvar.setStyle("border-color","red"); } salvar.setActionListener(new
		 * ActionListener() {
		 * 
		 * @Override public boolean onAction(Control source) { return
		 * onSalvarClick(); } }); getForm().add(salvar);
		 * 
		 * 
		 * ImageSubmit pesquisar = new ImageSubmit("Pesquisar",
		 * "/resources/images/btnPesquisar.png");
		 * pesquisar.setAttribute("onclick", "validar('Pesquisar')");
		 * pesquisar.setDisabled(!permitidoPesquisar());
		 * pesquisar.setStyle("border","1px solid black");
		 * pesquisar.setStyle("border-color","blue"); if
		 * (pesquisar.isDisabled()){
		 * pesquisar.setStyle("border","1px solid black");
		 * pesquisar.setStyle("border-color","red"); }
		 * pesquisar.setActionListener(new ActionListener() {
		 * 
		 * @Override public boolean onAction(Control source) {
		 * 
		 * return onPesquisaClick(); } }); getForm().add(pesquisar); ImageSubmit
		 * cancelar = new ImageSubmit("Cancelar",
		 * "/resources/images/btnLimpar.png"); //
		 * cancelar.setAttribute("onclick", "validar('Limpar')");
		 * cancelar.setAttribute("onclick",
		 * "return window.confirm('Deseja realmente limpar dados ?');");
		 * cancelar.setDisabled(!permitidoCancelar());
		 * cancelar.setStyle("border","1px solid black");
		 * cancelar.setStyle("border-color","blue"); if (cancelar.isDisabled()){
		 * cancelar.setStyle("border","1px solid black");
		 * cancelar.setStyle("border-color","red"); }
		 * cancelar.setActionListener(new ActionListener() {
		 * 
		 * @Override public boolean onAction(Control source) {
		 * 
		 * return onCancelarClick(); } }); getForm().add(cancelar);
		 */

	}

	@SuppressWarnings("serial")
	public void montarBotoes(boolean exibeBotaoSalvar,
			boolean exibeBotaoPesquisar, boolean exibeBotaoCancelar) {

		HiddenField idFields = new HiddenField("imageFields", String.class);
		idFields.setId("imageFields");
		getForm().add(idFields);
		HiddenField acao = new HiddenField("actionLink", "");
		getForm().add(acao);

		ImageSubmit salvar = new ImageSubmit("Salvar",
				"/resources/images/btnSalvar.png");
		salvar.setAttribute("onclick", "validar('Salvar')");
		salvar.setStyle("border", "1px solid black");
		salvar.setStyle("border-color", "blue");

		salvar.setDisabled(!permitidoSalvar() || !exibeBotaoSalvar);

		if (salvar.isDisabled()) {
			salvar.setStyle("border-color", "red");
		}

		salvar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
				return onSalvarClick();
			}
		});

		getForm().add(salvar);

		ImageSubmit pesquisar = new ImageSubmit("Pesquisar",
				"/resources/images/btnPesquisar.png");
		pesquisar.setAttribute("onclick", "validar('Pesquisar')");
		pesquisar.setStyle("border", "1px solid black");
		pesquisar.setStyle("border-color", "blue");

		pesquisar.setDisabled(!permitidoPesquisar() || !exibeBotaoPesquisar);

		if (pesquisar.isDisabled()) {
			pesquisar.setStyle("border-color", "red");
		}
		pesquisar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {

				return onPesquisaClick();
			}
		});

		getForm().add(pesquisar);

		ImageSubmit cancelar = new ImageSubmit("Cancelar",
				"/resources/images/btnLimpar.png");
		cancelar.setAttribute("onclick",
				"validar('Limpar'); return window.confirm('Deseja realmente limpar dados ?');");
		cancelar.setStyle("border", "1px solid black");
		cancelar.setStyle("border-color", "blue");

		cancelar.setDisabled(!permitidoCancelar() || !exibeBotaoCancelar);

		if (cancelar.isDisabled()) {
			// cancelar.setStyle("border","1px solid black");
			cancelar.setStyle("border-color", "red");
		}
		cancelar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {

				return onCancelarClick();
			}
		});

		getForm().add(cancelar);

	}

	public boolean permitidoCancelar() {
		return true;
	}

	public boolean permitidoPesquisar() {
		return true;
	}

	public boolean permitidoSalvar() {
		return true;

	}

	private void removeGrid() {
		grid.getColumnList().clear();
		grid.getColumns().clear();
		grid.setPaginatorAttachment(0);
	}

	private void removeExportContainer() {
		grid.setExportAttachment(0);
		grid.setExportBannerPosition(ExportTable.POSITION_BOTTOM);
		grid.getExportColumnList().clear();
		grid.getExportContainer().remove(excel);
		grid.getExportContainer().remove(pdf);
	}

	private void addExportContainer() {
		grid.getExportContainer().add(excel);
		grid.getExportContainer().setSeparator(" | ");
		grid.getExportContainer().add(pdf);
		grid.setExportAttachment(ExportTable.EXPORTER_INLINE);
		grid.setExportBannerPosition(ExportTable.POSITION_BOTTOM);
		grid.getExcludedExportColumns().add("action");
	}

	private void addPaginator() {
		grid.setClass(Table.CLASS_BLUE2);
		grid.setPageSize(20);
		grid.setPaginatorAttachment(Table.PAGINATOR_INLINE);
	}

	public void imageFields() {
		String aux = getContext().getRequestParameter("imageFields");
		List<String> list = new ArrayList<String>();
		if (aux != null) {
			String[] imageFields = aux.split(";");
			for (String f : imageFields) {
				list.add(getContext().getRequestParameter(f));
			}
		}
		getContext().getSession().setAttribute("imageFields", list);
	}

	protected abstract void montarGrid();

	protected void removeComponenteByName(String nomeComponente) {

		FieldSet field = (FieldSet) getForm().getControl(nomeComponente);
		if (field != null) {
			getForm().remove(field);
		}

	}

	protected void validaCampos(FieldSet fieldSet)
		throws ValidacaoCampoException {
	
		for (Field field : fieldSet.getFieldList()) {
		
			if (field.getLabel().contains("required")
					&& StringUtils.isEmpty(field.getValue())) {
		
				String label = field.getLabel().substring(0,
						field.getLabel().indexOf("<"));
				throw new ValidacaoCampoException("O campo " + label
						+ " &eacute; requerido");
		
			}
		
			if (field.getLabel().contains("99/99/9999")
					&& isNotDataValida(field)) {
		
				String label = field.getLabel().substring(0,
						field.getLabel().indexOf("<"));
				throw new ValidacaoCampoException("O campo " + label
						+ " possui uma data inv&aacute;lida");
		
			}
		
			if (StringUtils.isNotEmpty(field.getError())) {
		
				throw new ValidacaoCampoException(field.getError());
		
			}
		
		}
	
	}

	private boolean isNotDataValida(Field field) {
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			formatter.parse(field.getValue());
			return false;
		} catch (ParseException e) {
			return true;
		}
	}

	protected void trataCampo(Field campo, boolean habilitado) {

		campo.setStyle("border-color", "blue");
		campo.setDisabled(!habilitado);

		if (campo.isDisabled())
			campo.setStyle("border-color", "red");

	}

}
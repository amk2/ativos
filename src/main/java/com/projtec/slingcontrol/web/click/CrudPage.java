package com.projtec.slingcontrol.web.click;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Table;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.util.Bindable;

public abstract class CrudPage<T> extends FormPage implements Serializable {

	private static final long serialVersionUID = 1707075425386249222L;

	@Bindable
	protected String mesg;

	@Bindable
	protected ActionLink editLink = new ActionLink("edit", "Editar", this,
			"onEditClick");

	@Bindable
	protected ActionLink deleteLink = new ActionLink("delete", "Remover", this,
			"onDeleteClick");

	@Bindable
	protected List<T> lstObjetos = new ArrayList<T>();

	public abstract Table getTable();

	public abstract Boolean setFormObjeto();

	public abstract Boolean removerObjeto(Integer id);

	public abstract T montarObjeto();

	public abstract List<T> pesquisar(T objeto);

	public abstract void alterar(T obj);

	public abstract Integer gravar(T obj);

	public abstract Integer getIDFormObj();

	public abstract T pesquisar(Integer id);

	public abstract void removerLista(Integer id);

	public CrudPage() {
		super();

	}

	@Override
	public void onInit() {

		super.onInit();
		
		initCrud();
	}
	
	public void initCrud() {
		montarBotoes();
		// data provider
		getTable().setDataProvider(new DataProvider<T>() {
			public List<T> getData() {

				return lstObjetos;
			}
		});
	}

	// Event Handlers ---------------------------------------------------------
	public boolean onCancelarClick() {
		getForm().clearValues();
		getForm().clearErrors();
		mesg = "";
		return true;
	}

	public Boolean onEditClick() {
		getForm().clearValues();
		getForm().clearErrors();
		return setFormObjeto();

	}

	public boolean onDeleteClick() {
		Integer id = deleteLink.getValueInteger();
		boolean ok = removerObjeto(id);
		if (ok) {
			removerLista(id);
		}
		return ok;
	}

	
	public boolean onPesquisaClick() {
		getForm().clearErrors();

		T objeto = montarObjeto();

		lstObjetos = pesquisar(objeto);

		return true;

	}

	public boolean onSalvarClick() {
		
		if (getForm().isValid()) {

			T obj = montarObjeto();

			Integer id = getIDFormObj();
			if (id == 0) {
				Integer novoId = gravar(obj);

				T novoObj = null;

				if (novoId > 0) {
					novoObj = pesquisar(novoId);
				}

				if (novoObj != null) {
					lstObjetos.add(0, novoObj);
				}

			} else {
				alterar(obj);
				removerLista(id);
				T novoObj = pesquisar(id);
				lstObjetos.add(0, novoObj);
			}

			getForm().clearValues();
			return true;

		} else {
			return false;
		}

	}

}

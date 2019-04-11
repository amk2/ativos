package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.util.ClickUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.EstoqueGerencia;
import com.projtec.slingcontrol.gerencia.LocaisGerencia;
import com.projtec.slingcontrol.modelo.Estoque;
import com.projtec.slingcontrol.modelo.Local;

@Component
public class LocaisSeletorPage extends BorderPage {

	private static final long serialVersionUID = -4716501012361513584L;

	@Resource(name = "estoqueGerencia")
	protected EstoqueGerencia estoqueGerencia;

	@Resource(name = "locaisGerencia")
	protected LocaisGerencia locaisGerencia;

	private Form form = new Form("form");

	private Select estoque = new Select("selectEstoque");
	private Select local = new Select("selectLocal");

	// Event Handlers ---------------------------------------------------------

	@Override
	public void onInit() {
		showMenu = false;
		super.onInit();

		estoque.setId("selectEstoque");
		local.setId("selectLocal");

		addControl(form);

		FieldSet fieldSet = new FieldSet("locais");
		form.add(fieldSet);

		estoque.setAttribute("onchange", "handleChange('form_locais', form);");

		estoque.setWidth("200px");
		local.setWidth("200px");

		fieldSet.add(estoque);
		fieldSet.add(local);

		buildSelects();
	}

	public void buildSelects() {
		populateEstoqueData();
		ClickUtils.bind(form);
		populateLocalData(estoque.getValue().split(";")[0]);
	}

	@Override
	public List<Element> getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
			Map<String, Object> templateModel = new HashMap<String, Object>();
			templateModel.put("estoqueId", estoque.getId());
			templateModel.put("localId", local.getId());
			JsScript script = new JsScript(
					"/resources/js/populate-on-select.js", templateModel);
			headElements.add(script);
		}
		return headElements;
	}

	@SuppressWarnings({ "serial", "rawtypes" })
	private void populateEstoqueData() {
		estoque.setDataProvider(new DataProvider() {
			public List getData() {
				List<Option> optionList = new ArrayList<Option>();
				Collection<Estoque> estoques = estoqueGerencia.obterTodos();
				optionList.add(new Option("", "-- Selecione --"));
				for (Iterator<Estoque> iterator = estoques.iterator(); iterator
						.hasNext();) {
					Estoque estoque = iterator.next();
					optionList.add(new Option(estoque.getId() + ";"
							+ estoque.getNome(), estoque.getNome()));
				}
				return optionList;
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "serial" })
	private void populateLocalData(final String estoque) {
		local.setDataProvider(new DataProvider() {
			public List getData() {
				List<Option> optionList = new ArrayList<Option>();
				Collection<Local> locais = locaisGerencia
						.obterLocaisPorEstoque(estoque);
				optionList.add(new Option("", "-- Selecione --"));
				for (Iterator<Local> iterator = locais.iterator(); iterator
						.hasNext();) {
					Local local = iterator.next();
					optionList.add(new Option(local.getId() + ";"
							+ local.getNome(), local.getNome()));
				}
				return optionList;
			}
		});
	}

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

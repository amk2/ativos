package com.projtec.slingcontrol.web.click.adm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Table;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.util.Bindable;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.TipoLayout;
import com.projtec.slingcontrol.web.click.ControlesHTML;
import com.projtec.slingcontrol.web.click.CrudPage;

@Component
public class LayoutPage extends  CrudPage<Layout>
{

	@Bindable 
	public String title =  getMessage("titulo"); 
	 
	@Bindable
	protected Form formLayout = new Form();
	
	protected Select tipolayoutField = new Select("tipoLayout");


	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;
	
	public LayoutPage()
	{
		super();
		montarForm();
	}
	
	@Override
	public boolean permitidoSalvar() {
	
		return Secure.isPermitted("botoes:layout:salvar");
	}
	
	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:layout:pesquisar");
	}
	
	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:layout:limpar");
	}

	@Override
	public void onInit()
	{

		super.onInit();
		//select tipo layout 
		tipolayoutField.setDataProvider(new DataProvider<Option>() 
		{
			
			@Override
			public Iterable<Option> getData()
			{
				List<Option> optionList = new ArrayList<Option>();
				optionList.add(new Option("", "-- Selecione --"));
				Collection<TipoLayout> lstLayout = layoutGerencia
						.obterTiposLayout();
				for (Iterator iterator = lstLayout.iterator(); iterator
						.hasNext();)
				{
					TipoLayout tipolayout = (TipoLayout) iterator.next();
					optionList.add(new Option(tipolayout.getId(), tipolayout
							.getDescricao()));

				}
				return optionList;
			}
		});

	}

	protected void montarGrid()
	{
		Column column;

		column = new Column("nome");
		column.setWidth("220px;");
	
		grid.addColumn(column);

		column = new Column("descricao");
		column.setWidth("220px;");
		grid.addColumn(column);

		column = new Column("tipoLayout.descricao");
		column.setWidth("220px;");
		grid.addColumn(column);

		column = new Column("action");
		column.setWidth("120px;");
		column.setSortable(false);
		ActionLink[] links = new ActionLink[] { editLink, deleteLink };
		column.setDecorator(new LinkDecorator(grid, links, "id"));
		grid.addColumn(column);

		deleteLink.setAttribute("onclick",
				"return window.confirm('Deseja realmente remover ?');");

	}

	private void montarForm()
	{
		// form
		FieldSet fieldSet = new FieldSet("layout");

		Field nomeField = ControlesHTML.textField("nome");
		nomeField.setRequired(false);
		nomeField.setLabel(nomeField.getLabel() + " <span class='required'>*</span>");
		nomeField.setAttribute("class", "validate[required]");
		fieldSet.add(nomeField);

		Field descricaoField = ControlesHTML.textField("descricao");
		descricaoField.setRequired(false);
		descricaoField.setLabel(descricaoField.getLabel() + " <span class='required'>*</span>");
		descricaoField.setAttribute("class", "validate[required]");
		fieldSet.add(descricaoField);
		
		tipolayoutField.setLabel(tipolayoutField.getLabel() +" <span class='required'>*</span>");
		tipolayoutField.setAttribute("class", "validate[required]");
		fieldSet.add(tipolayoutField);
		
		HiddenField idField = new HiddenField("id", Integer.class);
		fieldSet.add(idField);
		
		//formLayout.setLabel(formLayout.getLabel() + " <span class='required'>*</span>");
		
		formLayout.add(fieldSet);
	}


	/******************************************************************/

	@Override
	public void alterar(Layout obj)
	{
		layoutGerencia.alterar(obj);
	}

	@Override
	public Form getForm()
	{		
		return formLayout;
	}

	@Override
	public Integer getIDFormObj()
	{

		String strId = formLayout.getFieldValue("id");
		Integer id;

		if (strId != null && strId.equals(""))
		{
			id = 0;
		} else
		{
			id = Integer.valueOf(strId);
		}
		
		return id;

	}

	@Override
	public Table getTable()
	{
		return grid;
	}

	@Override
	public Integer gravar(Layout obj) 
	{
		
		try {
		
			Layout layout = layoutGerencia.incluir(obj);
			return layout.getId();
			
		}
		catch (Exception e) {
			
			throw new RuntimeException(e.getMessage());
			
		}
		
	}

	@Override
	public Layout montarObjeto()
	{
		String nome = formLayout.getFieldValue("nome");
		String descricao = formLayout.getFieldValue("descricao");

		Layout layout = new Layout();
		layout.setId(getIDFormObj());
		layout.setNome(nome);
		layout.setDescricao(descricao);

		TipoLayout tipolayoutObj = new TipoLayout();
		Select selectField = (Select) formLayout.getField("tipoLayout");
		List listaValues = selectField.getSelectedValues();
		try {
			tipolayoutObj.setId(Integer.valueOf((String) listaValues.get(0)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		layout.setTipoLayout(tipolayoutObj);
		
		return layout;
	}

	@Override
	public List<Layout> pesquisar(Layout objeto)
	{
		
		List<Layout> layoutList = new ArrayList<Layout>(layoutGerencia.pesquisar(objeto));
		
		if (CollectionUtils.isEmpty(layoutList))
			mensagemErro = "N&atilde;o foram encontrados Layouts com os dados informados";
		
		return layoutList;
		
	}

	@Override
	public Layout pesquisar(Integer id)
	{
		return layoutGerencia.pesquisarId(id);
	}

	@Override
	public void removerLista(Integer id)
	{
		Layout l = new Layout();
		l.setId(id);
		lstObjetos.remove(l);				
	}

	@Override
	public Boolean removerObjeto(Integer id)
	{
		if (!layoutGerencia.verificaSePodeExluir(id)) {
			
			mensagemErro = "N&atilde;o pode ser removido. Existem configura&ccedil;&otilde;es associadas a esse layout.";
			return false;
			
		}
		
		if (layoutGerencia.excluir(id)) {
			
			mensagemSucesso = "Layout removido com sucesso";
		 	return true ;
		 	
		} 
		else {
			
			mensagemErro = "Problemas ao remover layout.";
			return false ;
			
		}
	
	}

	@Override
	public Boolean setFormObjeto()
	{
		Integer id = editLink.getValueInteger();
		Layout layout = layoutGerencia.pesquisarId(id);

		if (layout != null)
		{
			// formTipoLayout.copyFrom(tpLayout);
			formLayout.getField("nome").setValue(layout.getNome());
			formLayout.getField("descricao").setValue(layout.getDescricao());
			formLayout.getField("tipoLayout").setValue(
					String.valueOf(layout.getTipoLayout().getId()));
			formLayout.getField("id").setValueObject(layout.getId());

		}
		
		return true;
	}

	@Override
	public boolean onSalvarClick() {
		
		try {
			
			FieldSet layoutFieldSet = (FieldSet) formLayout.getControl("layout"); 
			validaCampos(layoutFieldSet);
			
			boolean retorno = super.onSalvarClick();
			mensagemSucesso = "Layout salvo com sucesso";
			
			return retorno;
			
		}
		catch(Exception e) {
			
			mensagemErro = e.getMessage();
			return false;
			
		}
			
	}

}
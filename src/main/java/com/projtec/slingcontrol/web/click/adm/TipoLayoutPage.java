
package com.projtec.slingcontrol.web.click.adm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Table;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.util.Bindable;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.TipoLayoutGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.TipoLayout;
import com.projtec.slingcontrol.web.click.ControlesHTML;
import com.projtec.slingcontrol.web.click.CrudPage;

@Component
public class TipoLayoutPage extends CrudPage<TipoLayout>
{
	 @Bindable
	public String title = getMessage("titulo");

	private static final long serialVersionUID = 1L;

	@Bindable
	protected Form formTipoLayout = new Form();

	@Resource(name = "tipoLayoutGerencia")
	private TipoLayoutGerencia tipoLayoutGerencia;

	public TipoLayoutPage()
	{
		super();
		montarForm();
	}
	
	@Override
	public boolean permitidoSalvar() {
	
		return Secure.isPermitted("botoes:tipoLayout:salvar");
	}
	
	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:tipoLayout:pesquisar");
	}
	
	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:tipoLayout:limpar");
	}

	@Override
	public void onInit()
	{
		super.onInit();
	}

	protected void montarGrid()
	{
		Column column = new Column("descricao");		
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

		FieldSet fieldSet = new FieldSet("tipoLayout");
		Field nomeField = ControlesHTML.textField("descricao");
		nomeField.setRequired(false);
		nomeField.setLabel(nomeField.getLabel() + " <span class='required'>*</span>");
		nomeField.setAttribute("class", "validate[required]");
		fieldSet.add(nomeField);

		HiddenField idField = new HiddenField("id", Integer.class);
		fieldSet.add(idField);
		formTipoLayout.add(fieldSet);

	}

	@Override
	public Form getForm()
	{

		return formTipoLayout;
	}

	@Override
	public Table getTable()
	{

		return grid;
	}

	@Override
	public Boolean setFormObjeto()
	{
		Integer id = editLink.getValueInteger();
		TipoLayout tpTipoLayout = tipoLayoutGerencia.pesquisarId(id);
		if (tpTipoLayout != null)
		{
			// formTipoLayout.copyFrom(tpLayout);
			formTipoLayout.getField("descricao").setValue(tpTipoLayout.getDescricao());
			formTipoLayout.getField("id").setValueObject(tpTipoLayout.getId());

		}
		return true;
	}

	@Override
	public TipoLayout montarObjeto()
	{
		Integer id = getIDFormObj();

		String descricao = formTipoLayout.getFieldValue("descricao");

		TipoLayout tipoLayout = new TipoLayout();
		tipoLayout.setId(id);
		tipoLayout.setDescricao(descricao);
		return tipoLayout;

	}

	@Override
	public Boolean removerObjeto(Integer id)
	{
		boolean retorno = tipoLayoutGerencia.excluir(id);
		mensagemSucesso = "Tipo de Layout removido com sucesso";
		return retorno; 
	}

	@Override
	public List<TipoLayout> pesquisar(TipoLayout objeto)
	{
		
		List<TipoLayout> tipoLayoutList = new ArrayList<TipoLayout>(tipoLayoutGerencia.pesquisar(objeto));
		
		if (CollectionUtils.isEmpty(tipoLayoutList))
			mensagemErro = "N&atilde;o foram encontrados Tipo de Layout com os dados informados";
		
		return tipoLayoutList;
		
	}

	@Override
	public void alterar(TipoLayout obj)
	{
		tipoLayoutGerencia.alterar(obj);
		mensagemSucesso = "Tipo de Layout salvo com sucesso";
	}

	@Override
	public Integer getIDFormObj()
	{
		String strId = formTipoLayout.getFieldValue("id");
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
	public Integer gravar(TipoLayout obj)
	{
		
		TipoLayout estNovo = tipoLayoutGerencia.incluir(obj);
		return estNovo.getId();
		
	}

	@Override
	public TipoLayout pesquisar(Integer id)
	{
		return tipoLayoutGerencia.pesquisarId(id);

	}

	@Override
	public void removerLista(Integer id)
	{
		TipoLayout tipoLayout = new TipoLayout();
		tipoLayout.setId(id);
		lstObjetos.remove(tipoLayout);

	}
	
	@Override
	public boolean onSalvarClick() {
		
		try {
			
			FieldSet tipoLayoutFieldSet = (FieldSet) formTipoLayout.getControl("tipoLayout"); 
			validaCampos(tipoLayoutFieldSet);
			
			boolean retorno = super.onSalvarClick();
			mensagemSucesso = "Tipo de Layout salvo com sucesso";
			
			return retorno;
			
		}
		catch(Exception e) {
			
			mensagemErro = e.getMessage();
			return false;
			
		}
			
	}

}
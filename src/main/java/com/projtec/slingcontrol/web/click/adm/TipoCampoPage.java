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
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.TipoCampoGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.web.click.ControlesHTML;
import com.projtec.slingcontrol.web.click.CrudPage;

@Component
public class TipoCampoPage extends CrudPage<TipoCampo> {

	// @Bindable 
	// public String title =  getMessage("titulo"); 
	 
	 private static final long serialVersionUID = 1L;
	 
	 @Bindable 
	 protected Form formTipoCampo = new Form();
	 
	 protected List<TipoCampo> lstTipoCampo;

	 @Resource(name="tipoCampoGerencia") 
	 protected TipoCampoGerencia tipoCampoGerencia;
	 
	 
	 public TipoCampoPage()
		{
			super();
			montarForm();
		}
		@Override
		public boolean permitidoSalvar() {
		
			return Secure.isPermitted("ativos:salvar");
		}
		
		@Override
		public boolean permitidoPesquisar() {
			return Secure.isPermitted("ativos:pesquisar");
		}
		
		@Override
		public boolean permitidoCancelar() {
			return Secure.isPermitted("ativos:limpar");
		}
	 
	 @Override
		public void onInit()
		{
			super.onInit();

		}
	 
	 protected void montarGrid(){
		 
		 Column column ;
		 column = new Column("descricao");	    
	     grid.addColumn(column);
	     
 	        
	     column = new Column("action");
	     column.setWidth("120px;");
	     column.setSortable(false);
	     
	     deleteLink.setAttribute("onclick", "return window.confirm('Deseja realmente remover ?');");
	     ActionLink[] links = new ActionLink[] { editLink, deleteLink };
	     column.setDecorator(new LinkDecorator(grid, links, "id"));
	     grid.addColumn(column);
		 
	 }
	 
	 private void montarForm(){
		
		    FieldSet fieldSet = new FieldSet("tipoCampo");
		    Field descricaoField = ControlesHTML.textField("descricao");
		    descricaoField.setRequired(false);
		    descricaoField.setLabel(descricaoField.getLabel() + " <span class='required'>*</span>");
		    descricaoField.setAttribute("class", "validate[required]");
		    fieldSet.add(descricaoField);
		    
			HiddenField idField = new HiddenField("id" , Integer.class);
			fieldSet.add(idField);
			formTipoCampo.add(fieldSet);
//			ClickUtils.bind(formTipoCampo);
		}
		 
		 
	 @Override
		public Form getForm()
		{

			return formTipoCampo;
		}

		@Override
		public Table getTable()
		{

			return grid;
		}
	 
	 
		@Override
		public Boolean setFormObjeto(){
			
			Integer id = editLink.getValueInteger();
	        TipoCampo tpCampo = tipoCampoGerencia.pesquisarId(id);
	        if (tpCampo != null) 
	        {
	            //formTipoLayout.copyFrom(tpLayout);
	        	formTipoCampo.getField("descricao").setValue(tpCampo.getDescricao()) ;
	        	formTipoCampo.getField("id").setValueObject(tpCampo.getId());
	          
	        }
	        return true;
			
			
		}
	 
		@Override
		public TipoCampo montarObjeto()
		{
			Integer id = getIDFormObj();
			
			String descricao = formTipoCampo.getFieldValue("descricao");
			
			TipoCampo tipoCampo = new TipoCampo();
			tipoCampo.setId(id);
			tipoCampo.setDescricao(descricao);
			return tipoCampo;

		
		}
		
	   
	    
		@Override
		public Boolean removerObjeto(Integer id)
		{
			
		     return tipoCampoGerencia.excluir(id);
		}

		@Override
		public List<TipoCampo> pesquisar(TipoCampo objeto)
		{
			
			return  new ArrayList<TipoCampo>(tipoCampoGerencia.pesquisar(objeto));
		}

		@Override
		public void alterar(TipoCampo obj)
		{
			tipoCampoGerencia.alterar(obj);
			
		}

		@Override
		public Integer getIDFormObj()
		{
			String strId = formTipoCampo.getFieldValue("id");
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
		public Integer gravar(TipoCampo obj)
		{
			TipoCampo estNovo = tipoCampoGerencia.incluir(obj);
			return estNovo.getId();
		}

		@Override
		public TipoCampo pesquisar(Integer id)
		{
			return tipoCampoGerencia.pesquisarId(id);

		}

		@Override
		public void removerLista(Integer id)
		{
			TipoCampo tipoCampo = new TipoCampo();
			tipoCampo.setId(id);
			lstObjetos.remove(tipoCampo);
			
		}

	    
	   

	 
}


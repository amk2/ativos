package com.projtec.slingcontrol.web.click;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.TextArea;
import org.apache.click.util.Bindable;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.ItensAtivos;

@Component
public class DemandaTrocaItemPage  extends FormPage 
{
	
	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;
	
	@Resource(name = "ativosGerencia")
	protected AtivosGerencia ativosGerencia;
	
	@Bindable 
	public String title = getMessage("titulo");
	
	@Bindable
	protected Form demandasTrocaItemForm = new Form("demandasTrocaItemForm");

	@Bindable
	protected Integer demanda;
	
	@Bindable
	protected Integer  item;
	
	
	@Bindable
	protected Integer  idItemTroca;
	
	
	@Bindable
	protected ItensAtivos  objItemTroca;
	
	@Bindable
	protected Demandas objDemanda;
	
	
	@Bindable
	protected ItensAtivos objItem;

	@Override
	public void onInit() {
		
		super.onInit();
		initForm();
	}
	

	public void initForm() {
		//super.onInit();
		
		 HiddenField  idDemanda = new HiddenField("demanda" , Integer.class);	 
		 demandasTrocaItemForm.add(idDemanda);
		    
		 HiddenField  idItem = new HiddenField("item" , Integer.class);	 
		 demandasTrocaItemForm.add(idItem);
		 
		  
	     HiddenField  idTrocado = new HiddenField("idItemTroca" , Integer.class);         
	     demandasTrocaItemForm.add(idTrocado);	 
	     
	     
	     TextArea textObs = new TextArea("observacao");
	     textObs.setRequired(true);
	     demandasTrocaItemForm.add(textObs);
	     
	     exibeBotoes();
		 
		if (demanda!=null)
		{
			objDemanda= demandaGerencia.pesquisarID(demanda);
			idDemanda.setValueObject(demanda);			
		}
		
		if (item!=null)
		{
			
			objItem = demandaGerencia.obterItensAtivo(item); 
			Ativos atv = ativosGerencia.pesquisarId(objItem.getAtivo().getId());
			objItem.setAtivo(atv);
			idItem.setValueObject(item);
		}
		
		if (idItemTroca!=null)
	     {
	    	 idTrocado.setValueObject(idItemTroca);
	    	 objItemTroca = demandaGerencia.obterItensAtivo(idItemTroca);
	    	 Ativos atv = ativosGerencia.pesquisarId(objItemTroca.getAtivo().getId());
	    	 objItemTroca.setAtivo(atv);
	     }	     
	     
	     
	}
	

	

	private void exibeBotoes()
	{
		
		ImageSubmit salvar = new ImageSubmit("Salvar",
				"/resources/images/btnSalvar.png");
		salvar.setAttribute("onclick", "validar('Salvar')");
		salvar.setStyle("border","1px solid black");
		salvar.setStyle("border-color","blue");
		
	
		
		if (salvar.isDisabled()){
			salvar.setStyle("border-color","blue");
		}
		
		salvar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
				return onSalvarClick();
			}
		});
		
		getForm().add(salvar);
	
		ImageSubmit cancelar = new ImageSubmit("Cancelar",
				"/resources/images/btnLimpar.png");
		cancelar.setAttribute("onclick",
		"return window.confirm('Deseja realmente Retornar ?');");
		cancelar.setDisabled(!permitidoCancelar());
		cancelar.setStyle("border","1px solid black");
		cancelar.setStyle("border-color","blue");
		if (cancelar.isDisabled()){
			cancelar.setStyle("border","1px solid black");
			cancelar.setStyle("border-color","blue");
		}
		
	
		cancelar.bindRequestValue();
		cancelar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
		        
				return onCancelarClick();
			}
		});
		getForm().add(cancelar);
	}
	@Override
	protected void montarGrid() {
		
		
	}

	@Override
	public Form getForm() {		
		return demandasTrocaItemForm;
	}

	
	public void onAdicionarItemTroca()
	{
		
		//HiddenField  fieldTroca = (HiddenField) demandasTrocaItemForm.getField("trocaPor");
		//if(fieldTroca)
		//fieldTroca.setValueObject(idItemTroca);
		
	}
	
	@Override
	protected boolean onPesquisaClick() {
		
		return false;
	}

	@Override
	protected boolean onCancelarClick() {
		
		 HiddenField  fieldDemanda = (HiddenField) demandasTrocaItemForm.getControl("demanda");
		 String strDemanda = fieldDemanda.getValue(); 
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("demanda", strDemanda);
		 setRedirect(DemandasDetalhePage.class , params);

		 return true;
	}

	@Override
	protected boolean onSalvarClick() 
	{
		
		 Integer idLocalDestino;
		
		 /*if (!demandasTrocaItemForm.isValid())
		 {
			 return false;
		 }*/
		 
		 TextArea textObs = (TextArea) demandasTrocaItemForm.getField("observacao");

		 if (textObs==null  || textObs.equals("")  || textObs.getValueObject()==null)
		 {
				mensagemErro = "Ocorreu o seguinte erro: Campo observa&ccedil;&atilde;o vazio.";
				initForm();					
				return false; 
		 }
		
		 HiddenField fieldIdTroca=(HiddenField) demandasTrocaItemForm.getField("idItemTroca");
		 
		 
		 
		 if (fieldIdTroca==null  || fieldIdTroca.getValueObject()==null)
		 {
				mensagemErro = "Ocorreu o seguinte erro: Selecionar um item para troca";
				initForm();					
				return false; 
		 }
		 
		 HiddenField fieldIdAtual = (HiddenField) demandasTrocaItemForm.getField("item");
		 HiddenField fieldIdDemanda = (HiddenField) demandasTrocaItemForm.getField("demanda");
		

		Integer idItemAnterior = (Integer) fieldIdAtual.getValueObject();
		
		Integer idDemanda  = (Integer) fieldIdDemanda.getValueObject();
		String observacao = textObs.getValue() ;
		Integer idItemAtual = (Integer) fieldIdTroca.getValueObject();
		ItensAtivos itemAtivoTroca = demandaGerencia.obterItensAtivo(idItemAtual);
		
		
		//se não tem local para o  item ativo, então retorna nullo
		if (itemAtivoTroca.getLocal()==null)
		{
			mensagemErro = "Ocorreu o seguinte erro: A demanda selecionada para troca não possui nenhuma movimentação/local de destino";
			initForm();					
			return false; 
		}
		else
		{
			idLocalDestino =itemAtivoTroca.getLocal().getId();
		}
		
		if (demandaGerencia.verificarExisteItemDemanda(idDemanda , idItemAtual) )
		{
			mensagemErro = "Ocorreu o seguinte erro: O item selecionado j&aacute; existe  na demanda";
			initForm();					
			return false; 
		}
		
		demandaGerencia.trocaItemDemanda(idDemanda, idItemAtual, idItemAnterior, idLocalDestino, observacao);
		
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("demanda", String.valueOf(idDemanda) );
		 params.put("mensagemSucesso", " A troca do item foi  executada com sucesso");
		 setRedirect(DemandasDetalhePage.class , params);
		
		return true;
	}




	

}

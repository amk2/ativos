package com.projtec.slingcontrol.web.click;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.ImageSubmit;
import org.apache.click.util.Bindable;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.gerencia.MovimentoAtivoGerencia;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.DemandasItemAtivo;
@Component
public class MovimentoAtivoDemandaFinalizarPage  extends BorderPage {

	@Bindable
	public String title = getMessage("titulo");
	
	@Bindable
	protected Form movimentoAtivoFinalizarForm = new Form("movimentoAtivoFinalizarForm");
	
	@Resource(name = "movimentoAtivoGerencia")
	protected MovimentoAtivoGerencia movimentoAtivoGerencia;
	
	
	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;

	@Bindable
	protected Integer demanda;
	
	@Bindable
	protected Map<Integer,ListaGridView> mapListGridView = new HashMap<Integer,ListaGridView>();
	
	@Bindable
	protected Demandas objDemanda;
	
	@Bindable 
	protected String acaodaurl;
	


	
	public MovimentoAtivoDemandaFinalizarPage(){
		super();
	}
	
	
	@Override
	public void onInit() 
	{
		
		  super.onInit();
		  
		  if (demanda != null) 
				  {
			  			getContext().getSession().setAttribute("demanda", demanda);
				  }
		  demanda = (Integer) getContext().getSession().getAttribute("demanda");
		  
		  List<DemandasItemAtivo> lstItensDemandas = demandaGerencia.obterItensAtivoDemanda(demanda);
		  objDemanda = demandaGerencia.pesquisarID(demanda);
		  
		  if (lstItensDemandas!=null)
		  {
		      
			  mapListGridView =  DemandasUtil.montarTabelaItensDemanda(lstItensDemandas,false);
		  }
		  
		  montarBotoes();
		
		  FieldSet movimentoAtivoDados = new FieldSet("MovimentoAtivo");
		  HiddenField idField = new HiddenField("idDemanda", Integer.class);
		  idField.setValueObject(demanda);
		  movimentoAtivoDados.add(idField);
	
		  HiddenField fielddaurl = new HiddenField("acaodaurl", String.class);
		  fielddaurl.setValueObject(acaodaurl);
		  movimentoAtivoDados.add(fielddaurl);
	
	
	}
	
	
	@Override
	public Form getForm() {
		return movimentoAtivoFinalizarForm;
	}
	
	@Override
	protected boolean onPesquisaClick() {
		return false;
	}

	@Override
	protected boolean onCancelarClick() {
		setRedirect(MovimentoAtivoPage.class);
		
		return true;
	}
	
	@Override
	protected boolean onSalvarClick() {
		
				movimentoAtivoGerencia.finalizardemanda(demanda);
				Map<String, String> params = new HashMap<String, String>();
				params.put("mensagemSucesso", " A Demanda foi finalizada com sucesso");
				setRedirect(MovimentoAtivoPage.class , params);
					
				return true;
	}

	
	public void montarBotoes() {
			
		
			HiddenField acao = new HiddenField("actionLink", "");
			getForm().add(acao);
			
			ImageSubmit salvar = new ImageSubmit("Salvar","/resources/images/btnSalvar.png");
			//salvar.setAttribute("onclick", "validar('Salvar')");
			salvar.setActionListener(new ActionListener() {
				@Override
				public boolean onAction(Control source) {
					return onSalvarClick();
				}
	
				
			});
			getForm().add(salvar);
			
			ImageSubmit cancelar = new ImageSubmit("Fechar","/resources/images/btnLimpar.png");
			cancelar.setAttribute("onclick","return window.confirm('Deseja realmente cancelar a Movimenta&ccedil;&atilde;o?');");
			cancelar.setActionListener(new ActionListener() {
				@Override
				public boolean onAction(Control source) {
					return onCancelarClick();
				}
			});
			getForm().add(cancelar);
	
	}

}

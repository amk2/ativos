package com.projtec.slingcontrol.web.click;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.control.Form;
import org.apache.click.util.Bindable;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.DemandasItemAtivo;

@Component
public class MovimentoAtivoAvisoFinalizarPage  extends BorderPage {

	
	
	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;

	@Bindable
	protected Integer demanda;
	
	@Bindable
	protected Map<Integer,ListaGridView> mapListGridView = new HashMap<Integer,ListaGridView>();
	
	@Bindable
	protected Demandas objDemanda;
	
	
	
	public MovimentoAtivoAvisoFinalizarPage() 
	{
	
	}
	
	@Override
	public void onInit() 
	{
	  showMenu = false;//nao exibe o menu
	  List<DemandasItemAtivo> lstItensDemandas = demandaGerencia.obterItensAtivoDemanda(demanda);
	  objDemanda = demandaGerencia.pesquisarID(demanda);
	  
	  if (lstItensDemandas!=null)
	  {
	      
		  mapListGridView =  DemandasUtil.montarTabelaItensDemanda(lstItensDemandas,false);
	  }
	  
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

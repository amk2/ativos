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
import com.projtec.slingcontrol.modelo.StatusDemanda;

@Component
public class DemandasDetalhePage  extends FormPage {

	private static final long serialVersionUID = 1L;

	@Bindable
	public String title = getMessage("titulo");
	
	@Bindable
	protected Integer demanda;

	@Bindable
	protected Integer idDaDemandaNova;
	
	@Bindable
	protected Demandas objDemanda;
	
	@Bindable
	protected String mensagemSucesso = new String();
	
	@Bindable
	protected Map<Integer,ListaGridView> mapListGridView = new HashMap<Integer,ListaGridView>();
	
	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;
	
	
	 @Override
	public void onInit() {
		 
		 super.onInit();
		 
		 Demandas demandas = (Demandas) getContext().getSession().getAttribute("aberturaDemandas");
		 if ( demandas!=null)
		 {
			 //objDemanda = demandas;
			 //idDaDemandaNova = demandas.getId();
			 objDemanda = demandaGerencia.pesquisarID(demandas.getId());
			 
		 }else 
		 {
			 objDemanda = demandaGerencia.pesquisarID(demanda);
		 }
		
		 
		 List<DemandasItemAtivo> lstItensDemandas = demandaGerencia.obterItensAtivoDemanda(demanda);
		 if (lstItensDemandas!=null && objDemanda!=null &&  objDemanda.getStatusDemanda()!=null)
		 {
			 //trocar somente demanda em ABERTO
			 Boolean trocarItem =  objDemanda.getStatusDemanda().getId().equals(StatusDemanda.ID_ABERTO);
			 mapListGridView = DemandasUtil.montarTabelaItensDemanda(lstItensDemandas,trocarItem);
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

	@Override
	protected void montarGrid() {
		
	}

}

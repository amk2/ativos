package com.projtec.slingcontrol.web.click;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.util.Bindable;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.modelo.Demandas;

@Component
public class DetalhesDemanda extends Page implements Serializable {

	private static final long serialVersionUID = 3483727362538929199L;

	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;

	@Bindable
	protected Integer demanda;

	@Bindable
	protected Demandas objDemanda;
	
	


	public DetalhesDemanda(){
	}

	@Override
	public void onInit() 
	{
		objDemanda = demandaGerencia.pesquisarID(demanda);
		
	}

}

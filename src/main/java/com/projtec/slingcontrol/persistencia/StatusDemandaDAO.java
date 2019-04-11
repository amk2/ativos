package com.projtec.slingcontrol.persistencia;

import java.util.Collection;

import com.projtec.slingcontrol.modelo.StatusDemanda;

public interface StatusDemandaDAO 
{
	
	Collection<StatusDemanda> obterTodos();

	StatusDemanda pesquisarPorId(Integer id);

}

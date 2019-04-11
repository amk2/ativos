package com.projtec.slingcontrol.persistencia;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.TipoDemanda;
@Repository
public interface TipoDemandaDAO {
	
	
	TipoDemanda incluir(TipoDemanda objTipoDemanda);
	
	boolean excluir (int id) ; 
	
	boolean alterar(TipoDemanda objTipoDemanda); 
	
	Collection<TipoDemanda>pesquisar(TipoDemanda objTipoDemanda);
	
	TipoDemanda pesquisarPorId(Integer id);
	
	Collection<TipoDemanda> obterTodos();
}

package com.projtec.slingcontrol.persistencia;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.TipoLayout;
@Repository
public interface TipoLayoutDAO {
	
		
		TipoLayout incluir(TipoLayout objTipoLayout);
		
		boolean excluir (int id) ; 
		
		boolean alterar(TipoLayout objTipoLayout); 
		
		Collection<TipoLayout> pesquisar(TipoLayout objTipoLayout);
		
		TipoLayout pesquisarPorId(Integer id);

		Collection<TipoLayout> obterTodos();
		

	}



package com.projtec.slingcontrol.persistencia;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.TipoCampo;
@Repository
public interface TipoCampoDAO {
	
		
		TipoCampo incluir(TipoCampo objTipoCampo);
		
		boolean excluir (int id) ; 
		
		boolean alterar(TipoCampo objTipoCampo); 
		
		Collection<TipoCampo>pesquisar(TipoCampo objTipoCampo);
		
		TipoCampo pesquisarPorId(Integer id);

		Collection<TipoCampo> obterTodos();
		

	}



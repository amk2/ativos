package com.projtec.slingcontrol.persistencia;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Estoque;


@Repository
public interface EstoqueDAO 
{
	
	Estoque adicionar(Estoque objEstoque);
	
	boolean excluir (int id) ; 
	
	boolean alterar(Estoque objestoque); 
	
	Collection<Estoque> pesquisar(Estoque estoque);
	
	Estoque pesquisarPorId(Integer id);
	
	Collection<Estoque> obterTodos();
	

}

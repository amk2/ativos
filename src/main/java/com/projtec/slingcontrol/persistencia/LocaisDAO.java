package com.projtec.slingcontrol.persistencia;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Estoque;
import com.projtec.slingcontrol.modelo.Local;

@Repository
public interface LocaisDAO 
{
	
	Collection<Local> obterTodosNomes();
	
	Local incluir(Local local);
	
	Boolean excluir(Integer idLocal);
	
	Boolean alterar(Local local); 
		
	Local pesquisarPorId(Integer id);

	List<Local> obterTodos();
	List<Local> pesquisar(Local local);
	
	Local pesquisarPelaDescricao(String nome) throws Exception;

	List<Local> obterLocaisPorEstoque(Estoque estoque);

}

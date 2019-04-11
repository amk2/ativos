package com.projtec.slingcontrol.persistencia;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Layout;

@Repository
public interface LayoutDAO {

	Layout incluir(Layout objLayout);

	boolean excluir(Integer id);

	boolean alterar(Layout objLayout);

	Layout pesquisarPorId(Integer id);

	Collection<Layout>  obterTodosComID(Integer id);
	
	Collection<Layout> obterTodos();

	Collection<Layout> pesquisar(Layout layout);

	Collection<Layout> obterTodosFiltroLayout(String nomeLayout);
	
	Layout pesquisar(String nome, Integer layoutId);
	
}
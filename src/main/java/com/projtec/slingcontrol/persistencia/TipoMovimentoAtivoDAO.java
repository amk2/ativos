package com.projtec.slingcontrol.persistencia;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.TipoMovimentoAtivo;

@Repository
public interface TipoMovimentoAtivoDAO {
 
	TipoMovimentoAtivo incluir(TipoMovimentoAtivo objTipoMovimentoAtivo);
	
	boolean excluir (int id) ; 
	
	boolean alterar(TipoMovimentoAtivo objTipoMovimentoAtivo); 
	
	Collection<TipoMovimentoAtivo>pesquisar(TipoMovimentoAtivo objTipoMovimentoAtivo);
	
	TipoMovimentoAtivo pesquisarPorId(Integer id);

	Collection<TipoMovimentoAtivo> obterTodos();	
	
}

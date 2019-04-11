package com.projtec.slingcontrol.persistencia;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.MovimentoAtivo;
@Repository
public interface MovimentoAtivoDAO {
	
		
		MovimentoAtivo incluir(MovimentoAtivo objMovimentoAtivo);
		
		boolean excluir (int id) ; 
		
		boolean alterar(MovimentoAtivo objMovimentoAtivo); 
		
		//Collection<MovimentoAtivo> pesquisar(MovimentoAtivo objMovimentoAtivo);
		
		MovimentoAtivo pesquisarPorId(Integer id);

		List<MovimentoAtivo> obterTodos();
		
		List<MovimentoAtivo> pesquisar(MovimentoAtivo objMovimentoAtivo);
		
		

	}



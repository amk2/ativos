
package com.projtec.slingcontrol.persistencia;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.AtivosNo;

@Repository
public interface AtivosDAO {
	
		
		Ativos incluir(Ativos objAtivos);
		
		boolean incluirDep(Integer idAtivo, Collection<Integer>  lstDependencias) ;
		
		boolean excluir (int id) ; 
		
		boolean excluirDep (int id) ;
		
		boolean alterar(Ativos objAtivos); 
		
		List<Ativos> pesquisar(Ativos objAtivos);
		
		List<Ativos> pesquisar(String criterio);
		
		
		Ativos pesquisarPorId(Integer id);
		
		List<Integer> pesquisarPaiPorIdFilho(Integer id);
		
		List<AtivosNo> pesquisarFilhoPorIdPai(Integer id, List<AtivosNo> no);
		
		List<Ativos> obterTodos();

		List<Ativos> pesquisarAtivosEstoque(Integer idEstoque, String codIdentificacao, Ativos atv); 
 }



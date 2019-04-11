package com.projtec.slingcontrol.persistencia;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Configuracoes;

@Repository
public interface ConfiguracoesDAO 
{
	
	Configuracoes incluir(Configuracoes objConfiguracoes);
	
	Boolean excluirConfiguracoesLayout (Integer idLayout) ; 
	
	Boolean alterar(Configuracoes objConfiguracoes); 
	
	Collection<Configuracoes> obterConfiguracoesLayout(Integer idLayout);
	
	Collection<Configuracoes> obterConfiguracoesFilho(Integer idConfig);
	
	Boolean excluir(Integer id);

	Configuracoes pesquisarId(Integer id);

	Collection<Configuracoes> pesquisar(Configuracoes conf);

	Boolean excluirConfiguracoesFilho(Integer id);
	
	Boolean existeRegistroIdLayout(Integer id) ;

}

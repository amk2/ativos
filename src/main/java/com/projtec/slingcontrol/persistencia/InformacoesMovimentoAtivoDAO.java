package com.projtec.slingcontrol.persistencia;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Informacoes;

@Repository
public interface InformacoesMovimentoAtivoDAO {

	Informacoes incluir(Informacoes objInformacoesMovimentoAtivo);
	
	boolean excluir (int id) ; 
	
	boolean alterar(Informacoes objInformacoesMovimentoAtivo); 
	
	boolean excluirAtivoID(int id );
	
	Informacoes pesquisarPorId(Integer id);

	Collection<Informacoes> obterInformacoesMovimentoAtivo(Integer id);

	List<Informacoes> obterInformacoesReferencia(Integer id);
	
	boolean existeRegistroLayoutId(Integer layoutId);
	
	
}

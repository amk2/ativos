package com.projtec.slingcontrol.persistencia;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Informacoes;

@Repository
public interface InformacoesDemandaDAO
{

	Informacoes incluir(Informacoes objInformacoesDemanda);

	boolean excluir(int id);

	boolean alterar(Informacoes objInformacoesDemanda);

	boolean excluirDemandaID(int id);

	Informacoes pesquisarPorId(Integer id);

	Collection<Informacoes> obterInformacoesDemanda(Integer id);

	List<Informacoes> obterInformacoesReferencia(Integer id);
	
	boolean existeRegistroLayoutId(Integer layoutId);
}

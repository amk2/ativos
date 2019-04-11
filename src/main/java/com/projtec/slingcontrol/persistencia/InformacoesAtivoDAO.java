package com.projtec.slingcontrol.persistencia;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Informacoes;

@Repository
public interface InformacoesAtivoDAO
{

	Informacoes incluir(Informacoes objInformacoes);

	boolean excluir(int id);

	boolean alterar(Informacoes objInformacoes);

	boolean excluirAtivoID(int id);
	
	boolean existeRegistroLayoutId(Integer layoutId);

	Informacoes pesquisarPorId(Integer id);

	Collection<Informacoes> obterInformacoesAtivos(Integer id);

	List<Informacoes> obterInformacoesReferencia(Integer idReferencia);
	
	List<Informacoes> obterInformacoesPelaConfiguracaoId(Integer configuracaoId,
			Integer ativoId);

}

package com.projtec.slingcontrol.persistencia;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Informacoes;


@Repository
public interface InformacoesLocaisDAO 
{
	Informacoes incluir(Informacoes objInformacoes);
	
	boolean alterar(Informacoes objInformacoes); 
	
	boolean excluirPorLocaisID(Integer idLocais );
	
	Collection<Informacoes> obterInformacoesLocais(Integer idLocais);

	List<Informacoes>  obterInformacoesReferencia(Integer id);
	
	boolean existeRegistroLayoutId(Integer layoutId);
	

}

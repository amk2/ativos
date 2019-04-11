package com.projtec.slingcontrol.persistencia;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Imagens;


@Repository
public interface ImagensDAO { 

	
	Imagens incluir(Imagens objImagens);
	
	boolean excluir (int id) ; 
	
	boolean alterar(Imagens objImagens); 
	
	Collection<Imagens> pesquisar(Imagens objImagens);
	
	Imagens pesquisarPorId(Integer id);
	
	byte[] pesquisarByteId(Integer id);

	Collection<Imagens> obterTodos();
	
	List<Imagens> pesquisar(String nome, String descricao) throws Exception;
	
	public Imagens pesquisarPeloNome(String nome);
	
}

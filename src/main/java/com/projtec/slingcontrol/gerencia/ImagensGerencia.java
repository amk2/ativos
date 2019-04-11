package com.projtec.slingcontrol.gerencia;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projtec.slingcontrol.modelo.Imagens;
import com.projtec.slingcontrol.persistencia.ImagensDAO;


@Service("imagensGerencia")
public class ImagensGerencia {
	
	@Autowired 
    private ImagensDAO imagensDAO;
	
	@Transactional
    public Imagens incluir(Imagens objImagens)
    {
    	return imagensDAO.incluir(objImagens);
    }
	
	
	@Transactional
    public Boolean alterar(Imagens objImagens)
    {
		return imagensDAO.alterar(objImagens);
    }
	
	
	@Transactional
    public Boolean excluir(Integer idobjImagens)
    {
    	return  imagensDAO.excluir(idobjImagens);
    }
	
    public Collection<Imagens> pesquisar(Imagens objImagens)
    {
    	return   imagensDAO.pesquisar(objImagens);
    }
    
    @Transactional
    public Imagens pesquisarId(Integer id)
    {
    	return   imagensDAO.pesquisarPorId(id);
    }


	public Collection<Imagens> obterTodos() {
		
		return imagensDAO.obterTodos();
	}
	
	public List<Imagens> pesquisar(String nome, String descricao) throws Exception {
		return imagensDAO.pesquisar(nome, descricao);
	}
	
	public byte[] pesquisarBytePorId(Integer id) {
		
		return imagensDAO.pesquisarByteId(id);
	}
	
	public Imagens pesquisarPeloNome(String nome) {
		return imagensDAO.pesquisarPeloNome(nome);
	}
	
}

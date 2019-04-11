package com.projtec.slingcontrol.gerencia;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projtec.slingcontrol.modelo.TipoLayout;
import com.projtec.slingcontrol.persistencia.TipoLayoutDAO;

@Service("tipoLayoutGerencia" )
@Scope("singleton")
public class TipoLayoutGerencia {
	@Autowired 
    private TipoLayoutDAO tipoLayoutDAO;
	
	@Transactional
    public TipoLayout incluir(TipoLayout objTipoLayout)
    {
    	return tipoLayoutDAO.incluir(objTipoLayout);
    }
	
	@Transactional
    public Boolean alterar(TipoLayout objTipoLayout)
    {
		return tipoLayoutDAO.alterar(objTipoLayout);
    }
	
	
	@Transactional
    public Boolean excluir(Integer idTipoLayout)
    {
    	return  tipoLayoutDAO.excluir(idTipoLayout);
    }
	
    public Collection<TipoLayout> pesquisar(TipoLayout objTipoLayout)
    {
    	return   tipoLayoutDAO.pesquisar(objTipoLayout);
    }
    
    
    public TipoLayout pesquisarId(Integer id)
    {
    	return   tipoLayoutDAO.pesquisarPorId(id);
    }

	public Collection<TipoLayout> obterTodos() {
		
		return tipoLayoutDAO.obterTodos();
	}
	
	
}

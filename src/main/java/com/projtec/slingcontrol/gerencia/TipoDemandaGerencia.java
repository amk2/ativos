package com.projtec.slingcontrol.gerencia;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.persistencia.TipoDemandaDAO;


@Service("tipoDemandaGerencia")
public class TipoDemandaGerencia {

	@Autowired 
    private TipoDemandaDAO tipoDemandaDAO;
	
	@Transactional
    public TipoDemanda incluir(TipoDemanda objTipoDemanda)
    {
    	return tipoDemandaDAO.incluir(objTipoDemanda);
    }
	
	
	@Transactional
    public Boolean alterar(TipoDemanda objTipoDemanda)
    {
		return tipoDemandaDAO.alterar(objTipoDemanda);
    }
	
	
	@Transactional
    public Boolean excluir(Integer idTipoDemanda)
    {
    	return  tipoDemandaDAO.excluir(idTipoDemanda);
    }
	
    public Collection<TipoDemanda> pesquisar(TipoDemanda objTipoDemanda)
    {
    	return   tipoDemandaDAO.pesquisar(objTipoDemanda);
    }
    
    
    public TipoDemanda pesquisarId(Integer id)
    {
    	return   tipoDemandaDAO.pesquisarPorId(id);
    }


    public Collection<TipoDemanda> obterTodos()
    {
    	return   tipoDemandaDAO.obterTodos();
    }
	
	
}

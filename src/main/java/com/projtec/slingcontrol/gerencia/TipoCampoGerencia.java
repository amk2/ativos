package com.projtec.slingcontrol.gerencia;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.persistencia.TipoCampoDAO;

@Service("tipoCampoGerencia")
public class TipoCampoGerencia {
	
	@Autowired 
    private TipoCampoDAO tipoCampoDAO ;
	
	@Transactional
    public TipoCampo incluir(TipoCampo objTipoCampo)
    {
    	return tipoCampoDAO.incluir(objTipoCampo);
    }
	
	@Transactional
    public Boolean alterar(TipoCampo objTipoCampo)
    {
		return tipoCampoDAO.alterar(objTipoCampo);
    }
	
	@Transactional
    public Boolean excluir(Integer idTipoCampo)
    {
    	return  tipoCampoDAO.excluir(idTipoCampo);
    }
	
    public Collection<TipoCampo> pesquisar(TipoCampo objTipoCampo)
    {
    	return   tipoCampoDAO.pesquisar(objTipoCampo);
    }
    
    
    public TipoCampo pesquisarId(Integer id)
    {
    	return   tipoCampoDAO.pesquisarPorId(id);
    }

	public Collection<TipoCampo> obterTodos() {
		
		return tipoCampoDAO.obterTodos();
    
	}
	
}

package com.projtec.slingcontrol.gerencia;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projtec.slingcontrol.modelo.TipoMovimentoAtivo;
import com.projtec.slingcontrol.persistencia.TipoMovimentoAtivoDAO;

@Service("tipoMovimentoAtivoGerencia")
public class TipoMovimentoAtivoGerencia {
	@Autowired 
    private TipoMovimentoAtivoDAO tipoMovimentoAtivoDAO;
	
	@Transactional
    public TipoMovimentoAtivo incluir(TipoMovimentoAtivo objTipoMovimentoAtivo)
    {
    	return tipoMovimentoAtivoDAO.incluir(objTipoMovimentoAtivo);
    }
	
	
	@Transactional
    public Boolean alterar(TipoMovimentoAtivo objTipoMovimentoAtivo)
    {
		return tipoMovimentoAtivoDAO.alterar(objTipoMovimentoAtivo);
    }
	
	
	@Transactional
    public Boolean excluir(Integer idTipoMovimentoAtivo)
    {
    	return  tipoMovimentoAtivoDAO.excluir(idTipoMovimentoAtivo);
    }
	
    public Collection<TipoMovimentoAtivo> pesquisar(TipoMovimentoAtivo objTipoMovimentoAtivo)
    {
    	return   tipoMovimentoAtivoDAO.pesquisar(objTipoMovimentoAtivo);
    }
    
    
    public TipoMovimentoAtivo pesquisarId(Integer id)
    {
    	return   tipoMovimentoAtivoDAO.pesquisarPorId(id);
    }


	public Collection<TipoMovimentoAtivo> obterTodos() {
		
		return tipoMovimentoAtivoDAO.obterTodos();
	}
	
}

package com.projtec.slingcontrol.persistencia;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.ConfigInfoPesquisaVO;
import com.projtec.slingcontrol.modelo.ItensAtivos;

@Repository
public interface ItensAtivosDAO {

	ItensAtivos incluir(ItensAtivos objItensAtivos);
    ItensAtivos obterID(Integer id);	
	boolean excluir (int id); 
	boolean alterar(ItensAtivos objItensAtivos);
	List<ItensAtivos> obterItensdoAtivo(Integer idAtivo);

	//coloquei para testar. Ocorria um erro quando pesquisava os itens do ativo na movimentação para itens que ainda não possuiam local_id
	List<ItensAtivos> obterItensdoAtivoComLocalID(Integer idAtivo);
	
	List<ItensAtivos> pesquisarItensAtivos(Integer idLayout, String identificacao, List<ConfigInfoPesquisaVO> lstConfigisValor);
	ItensAtivos obterIDparaItemAtivo(Integer id);
	ItensAtivos incluirItenAtivoFinalizado(ItensAtivos itenAtivoRecebeCamposdaDemanda); 

}

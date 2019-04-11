package com.projtec.slingcontrol.persistencia;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.DemandasItemAtivo;

@Repository
public interface DemandasItemAtivoDAO 
{

	List<DemandasItemAtivo> obterItensAtivoDemanda(Integer idDemanda);
	
	void incluir(DemandasItemAtivo demandasItemAtv);
	
	void excluirItensDemanda(Integer idDemanda);
	
	void excluirItemTroca(Integer idDemanda , Integer idItemAnterior);
	
	void incluirItensDemandaTroca(Integer idDemanda , Integer idItemAtual   ,Integer idItemAnterior, String observacao );

	Boolean existeItemDemanda(Integer idDemanda, Integer idItem);

	void incluirDemandaItemAtivo(Integer id, Integer itensAtivosId);

	Boolean pesquisarPossivelFinalizacao(Integer id);

}

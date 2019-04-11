package com.projtec.slingcontrol.persistencia;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projtec.slingcontrol.modelo.Demandas;


@Repository
public interface DemandaDAO 
{

			Demandas incluir(Demandas objDemanda);			
			boolean excluir (int id);
			
			boolean alterar(Demandas objDemanda);		
			
			Demandas pesquisarPorId(Integer id);

			Collection<Demandas> obterTodos();
			
			List <Demandas> pesquisar(Demandas objDemanda);
			
			void alterarStatus(Integer id, Integer idStatus);
			
			List<Demandas> pesquisarDemandasMovimento(Integer origem,
					Integer destino, Integer status, Integer demanda);
			
			boolean verificaItensLocalOrigem( Integer idDemanda);

			
			
			
				
		


	}



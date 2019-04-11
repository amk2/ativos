package com.projtec.slingcontrol.persistencia.postgres;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.projtec.slingcontrol.modelo.Informacoes;

public abstract class BasePostgresDAO {
	
	/**
	 * Método genérico de pesquisa de campos dinâmico, originalmente criado para atender ao cadastro de ativos,
	 * mas posteriomente refatorado para atender a todos os cadastros que possuam campos dinâmicos
	 * 
	 * Ex.: nomeTabela = atv - (pode ser o alías usado na querie de base)
	 * 		nomeColuna = ativos_id - (nome da coluna usado na querie de base)
	 * 		nomeTabelaInformacoes = informacoes_ativo
	 * 
	 * @author Oséas Santana - 06/05/2011 às 11:06 hs
	 * 
	 * @param nomeTabela
	 * @param nomeColuna
	 * @param nomeTabelaInformacoes
	 * @param informacoesList
	 * @return
	 */
	
	public static String getComplementoQuerieParaConsultaComCamposDinamicos(
			String nomeTabela, String nomeColuna, 
			String nomeTabelaInformacoes, List<Informacoes> informacoesList) {
		
		String retorno = "";
		
		if (temValorValidoParaSerUsadoNaConsulta(informacoesList)) {
		
			StringBuffer sql = new StringBuffer();
			
			String nomeTabelaEColuna = nomeColuna;
			if (StringUtils.isNotEmpty(nomeTabela))
				nomeTabelaEColuna = nomeTabela + "." + nomeColuna;
				
			sql.append(" and " + nomeTabelaEColuna + " in (    "); 
			sql.append("\n"); // Quebra de linha para facilitar a depuração
			sql.append(" 	select distinct info." + nomeColuna);
			sql.append("\n");
			sql.append(" 	  from " + nomeTabelaInformacoes + " info ");
			sql.append("\n");
			sql.append(" 	 where 1 = 1 ");
			sql.append("\n");
			
			for (Informacoes informacoes : informacoesList) {
				
				if (temValorValidoParaSerUsadoNaConsulta(informacoes)) {
				
					sql.append("   	   and info." + nomeColuna + " in ( ");
					sql.append("\n");
					sql.append("   	   		select info1. " + nomeColuna);
					sql.append("\n");
					sql.append("   	    	  from " + nomeTabelaInformacoes + " info1  ");
					sql.append("\n");
					sql.append("   	   		 where 1 = 1 ");
					sql.append("\n");
					sql.append("   	   	       and ( ");
					sql.append("\n");
					
					if (StringUtils.isNotEmpty(informacoes.getDescricao())) {
						sql.append("   	   	       		lower(info1.informacoes_descricao) like '%" + informacoes.getDescricao().toLowerCase() + "%' ");
					}
					else {
						sql.append("   	   	       		info1.informacoes_id_referencia = " + informacoes.getReferenciaId());
					}
					sql.append("\n");
					sql.append("   	   	       		and info1.layout_configuracoes_id = " + informacoes.getConfiguracoesId());
					sql.append("\n");
					sql.append("   	   	       		) ");
					sql.append("\n");
					sql.append(" 		) ");
					sql.append("\n");
					
				}
				
			}
			
			// O parentese abaixo refere-se a primeira linha do StringBuffer
			sql.append(" ) ");  
			
			retorno = sql.toString();
		
		}
		
		return retorno;
		
	}
	
	private static boolean temValorValidoParaSerUsadoNaConsulta(List<Informacoes> informacoesList) {
		
		for (Informacoes informacoes : informacoesList) {
			
			// if (StringUtils.isNotBlank(informacoes.getDescricao()) || informacoes.getReferenciaId() != null)
				// return true;
			if (temValorValidoParaSerUsadoNaConsulta(informacoes))
				return true;
			
		}
		
		return false;
		
	}
	
	private static boolean temValorValidoParaSerUsadoNaConsulta(Informacoes informacoes) {
		
		if (StringUtils.isNotBlank(informacoes.getDescricao()) || informacoes.getReferenciaId() != null)
			return true;
		else
			return false;
		
	}
	
}

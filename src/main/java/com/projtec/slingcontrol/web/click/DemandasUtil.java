package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.projtec.slingcontrol.modelo.DemandasItemAtivo;

public class DemandasUtil 
{
	public static  Map<Integer,ListaGridView> montarTabelaItensDemanda (List<DemandasItemAtivo>	lstItensDemandas ,Boolean trocaItem) {
		
	    Map<Integer,ListaGridView> mapListGridView = new HashMap<Integer,ListaGridView>();
		
		ListaGridView estLstView; 
		Integer soma=0;
		for (Iterator<DemandasItemAtivo> iterator = lstItensDemandas.iterator(); iterator.hasNext();) 
		{
			final  DemandasItemAtivo demItem = iterator.next();
			
			if (mapListGridView.containsKey(demItem.getItemAtivos().getAtivo().getLayout().getId()))
			{
				estLstView =mapListGridView.get(demItem.getItemAtivos().getAtivo().getLayout().getId());				
				
			}else
			{	
			
				estLstView = new ListaGridView();
				estLstView.setTitulo(demItem.getItemAtivos().getAtivo().getLayout().getNome());
				estLstView.setIdLayout(Integer.valueOf(demItem.getItemAtivos().getAtivo().getLayout().getId()));				
				estLstView.setNomes(new  LinkedHashMap<String, String>());
				estLstView.setLinhasValores(new ArrayList<Map<String,String>>());
				mapListGridView.put(demItem.getItemAtivos().getAtivo().getLayout().getId(), estLstView);	
				estLstView.setTotal(new Integer(0));
				
				estLstView.getNomes().put("rfid",  "TAG RFID");
				estLstView.getNomes().put("localAtual",  "Local Atual");	
				estLstView.getNomes().put("origem",  "Origem");
				estLstView.getNomes().put("destino",  "Destino");
				if(trocaItem  )
					estLstView.getNomes().put("trocaItem",  "Trocar Item");
				
				
				
			}
			
			//colocar os valores
		  
		    Map<String, String> mapLinha = new LinkedHashMap<String, String>();
		    estLstView.getLinhasValores().add(mapLinha);
	
		
		    //mapLinha.put("id", String.valueOf(dem.getId()));
		    mapLinha.put("rfid", demItem.getItemAtivos().getIdentificacao());
		    String localOrigem = "";
		    if (demItem.getLocalOrigem()!=null)
		    {
		    	   localOrigem = demItem.getLocalOrigem().getNome();
		    }			      
		    mapLinha.put("origem", localOrigem);
		    String localDestino = "";
		    if(demItem.getLocalDestino()!=null)
		    {
		    	localDestino =  demItem.getLocalDestino().getNome();
		    }
		    mapLinha.put("destino", localDestino);	    
		   
		    mapLinha.put("localAtual", demItem.getItemAtivos().getLocal().getNome());
		    
		    if(trocaItem)
		    {
		    	 String linkAcoes = "<a href='demandaTrocaItem.htm?demanda="+demItem.getIdDemanda()
                 + "&item=" +demItem.getItemAtivos().getItensAtivosId() 
                 + "'>Trocar</a> " ;   
		    	 
		    	 mapLinha.put("trocaItem", linkAcoes);
		    	
		    }
		   
		  
	
		}
		
		return mapListGridView;
		
	}

}

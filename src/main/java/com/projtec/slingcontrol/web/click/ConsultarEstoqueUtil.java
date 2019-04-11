package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.gerencia.LocaisGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.web.tipocampo.TipoCampoView;

@Component("consultarEstoqueUtil")
public class ConsultarEstoqueUtil 
{
	@Resource(name = "locaisGerencia")
	public LocaisGerencia locaisGerencia;
	
	@Resource(name = "layoutGerencia")
	public LayoutGerencia layoutGerencia;
	
	@Autowired
	public ApplicationContext appContext;


	public  HashMap<Integer,ListaGridView> montarTabela(List<Ativos> lstAtivos , Boolean exibirDependencia) {

		HashMap<Integer,ListaGridView> mapEstoqueView = new HashMap<Integer,ListaGridView>();
		
		ListaGridView estLstView; 
		Integer soma=0;
		for (Iterator iterator = lstAtivos.iterator(); iterator.hasNext();) 
		{
			final Ativos ativos = (Ativos) iterator.next();
			
			if (mapEstoqueView.containsKey(ativos.getLayout().getId()))
			{
				estLstView = mapEstoqueView.get(ativos.getLayout().getId());				
				
			}else
			{	
			
				estLstView = new ListaGridView();
				estLstView.setTitulo(ativos.getLayout().getNome());
				estLstView.setIdLayout(Integer.valueOf(ativos.getLayout().getId()));				
				estLstView.setNomes(new  LinkedHashMap<String, String>());
				estLstView.setLinhasValores(new ArrayList<Map<String,String>>());
				mapEstoqueView.put(ativos.getLayout().getId(), estLstView);	
				estLstView.setTotal(new Integer(0));
				
				//nomes .....
				Collection<Configuracoes> lstConfigs = ativos.getLayout().getConfiguracoes();
				
				for (Iterator iterator2 = lstConfigs.iterator(); iterator2.hasNext();) 
				{
					Configuracoes configuracoes = (Configuracoes) iterator2.next();
					
					estLstView.getNomes().put(String.valueOf(configuracoes.getId()), configuracoes.getNomecampo());
					
				}	
			     estLstView.getNomes().put("local",  "Local");

			    
			     
			    if (exibirDependencia)
				   	 estLstView.getNomes().put("dependencia",  "Depend&ecirc;ncia");
			     
			     estLstView.getNomes().put("qtd",  "Quantidade");
				   				
			}
			
			//colocar os valores
		    Collection<Informacoes>	 lstInfos = ativos.getInformacoes();
		    Map<String, String> mapLinha = new LinkedHashMap<String, String>();
		    estLstView.getLinhasValores().add(mapLinha);
		    for (Iterator iterator2 = lstInfos.iterator(); iterator2.hasNext();) {
				Informacoes informacoes = (Informacoes) iterator2.next();
				
	            Configuracoes config = layoutGerencia.pesquisarConfiguracao(informacoes.getConfiguracoesId());
				
				String strValor = formataValor(config,informacoes);
				
				mapLinha.put(String.valueOf(informacoes.getConfiguracoesId()), strValor);
							
			}
		  
		    Local local = locaisGerencia.perquisarID(ativos.getLocalID());
			String lupa = "";
			if (local != null) {
				lupa = "<a href='javascript:void(0)' onclick=\"window.open('localizacao.htm?latitude="	+ local.getLatitude()+ "&longitude=" + local.getLongitude()	+ "&nome=" 	+ local.getNome() +   "&identificacao=" + local.getIdentificacao() + "&local="  + local.getNome() + "','Teste','resizable=yes,width=640,height=480');\" > <img src='resources/images/lupa.png' /> </a> ";
			}
		    mapLinha.put("local", "Estoque:" + local.getEstoque().getNome() + " <BR>  Local:" + local.getNome() +  " | " + lupa);
		    
		    if (exibirDependencia)
		    {
		    	String linkEstrutura = "&nbsp;";
		    	if (ativos.getLstFilhos() !=null && ativos.getLstFilhos().size() > 0 )
		    	{
			    	linkEstrutura = " <a href='#' onclick='exibirItensFilhos("+ ativos.getId() +")'>[+]</a>";
		    	}
			    mapLinha.put("dependencia",   linkEstrutura);
		    }
		   
		 

		    mapLinha.put("qtd", String.valueOf(ativos.getQtdItens()));
		    estLstView.setTotal(estLstView.getTotal() + ativos.getQtdItens());
	
		}
		
		
		return mapEstoqueView;
		
	
	}

	private String formataValor(Configuracoes config, Informacoes info)
	{
		TipoCampoView tpCampo = (TipoCampoView) appContext.getBean(String.valueOf(config.getTipoCampo().getId()));
		String valor =  tpCampo.formataInformacao(config, info);
		
		return valor;
	}
 }
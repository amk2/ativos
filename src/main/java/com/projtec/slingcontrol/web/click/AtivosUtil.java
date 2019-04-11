package com.projtec.slingcontrol.web.click;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.web.tipocampo.TipoCampoView;

@Component("ativosUtil")
public class AtivosUtil 
{
	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private LayoutGerencia layoutGerencia;
	
	public  String ativosDinamicoJSON(List<Ativos> listaAtivos)
	{
		StringBuilder colModel =  new StringBuilder(); 
		StringBuilder colNames =new StringBuilder();
		StringBuilder dataSet =new StringBuilder();	
		Map mapLabel = new HashMap<Integer, String>();
		 
		String strJSON = new String("{\n"); 
		
		/*strJSON = strJSON + "\"page\":\"1\","; 
		strJSON = strJSON + "\"total\":\"1\","; 
		strJSON = strJSON + "\"records\":\""+  listaAtivos.size() +  "\","; 
		strJSON = strJSON + "\n\"rows\":[" ; */		
		
		//StringBuffer strBuffer = new StringBuffer();
		Ativos ativos = listaAtivos.get(0);		
		//colNames
		Collection<Configuracoes> lstConfigs = ativos.getLayout().getConfiguracoes();	
		colNames.append("\n\"colNames\":[");
		colNames.append("\"ID\",\"NOME LAYOUT\",");
		
		
		
		//colModel
		/*
		"colModel":[
				    {"editable":true,"edittype":"integer","index":"userInfoId","jsonmap":"userInfoId","key":true,"name":"userInfoId","resizable":true,"search":false,"sortable":true,"width":300},
				    {"editable":true,"edittype":"text","index":"UserID","jsonmap":"userID","key":false,"name":"userID","resizable":true,"search":false,"sortable":true,"width":300}
				    ]*/
		colModel.append("\n\"colModel\":[");
		colModel.append("\n{\"editable\":false,\"index\":\"ID\",\"jsonmap\":\"ID\",\"key\":true,\"name\":\"ID\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":20},\n");
		colModel.append("\n{\"editable\":false,\"index\":\"LAYOUT\",\"jsonmap\":\"LAYOUT\",\"key\":false,\"name\":\"LAYOUT\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":100},\n");
		
		for (Iterator<Configuracoes>  iterator = lstConfigs.iterator(); iterator.hasNext();)
		{
			Configuracoes  conf =  iterator.next();
			colNames.append("\"" + conf.getNomecampo() + "\"");
			// {"editable":true,"edittype":"integer","index":"userInfoId","jsonmap":"userInfoId","key":true,"name":"userInfoId","resizable":true,"search":false,"sortable":true,"width":300},

			//			colModel.append("\n{\"editable\":false,\"index\":\"" +  conf.getNomecampo() + "\",\"jsonmap\":\"" + conf.getNomecampo()   +  "\",\"key\":false,\"name\":\""+conf.getNomecampo()+ "\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":"+ String.valueOf(conf.getTamanho()) + "}\n");

			colModel.append("\n{\"editable\":false,\"index\":\"" +  conf.getNomecampo() + "\",\"jsonmap\":\"" + conf.getNomecampo()   +  "\",\"key\":false,\"name\":\""+conf.getNomecampo()+ "\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":200}\n");

			
			mapLabel.put(conf.getId(), conf.getNomecampo());
			if(iterator.hasNext())
			{
				colNames.append(",");
				colModel.append(",");
			}
					
			
		}
		colModel.append("],\n");
		strJSON =	strJSON + colModel.toString();
		
		colNames.append("],\n");
		strJSON =	strJSON + colNames.toString();
		

		
		 //DataSet 
		   /* "dataset":[
			        {"userID":"SMI","userInfoId":5},
			        {"userID":"ABC","userInfoId":7},
			        {"userID":"PQR","userInfoId":8},
			        {"userID":"FUR","userInfoId":10},
			        {"userID":"COO","userInfoId":13}
			        ],*/
		 
		strJSON =	strJSON + "\"gridModel\":{" ;
			
		dataSet.append("\"dataset\":[");
		String strValor; 
		for (Iterator iterator = listaAtivos.iterator(); iterator.hasNext();)
		{
			Ativos atv= (Ativos) iterator.next();
			
			dataSet.append("\n{");
			dataSet.append("\"ID\":\"" + String.valueOf(atv.getId()) + "\" , \"LAYOUT\":\"" +  ativos.getLayout().getNome() + "\", ");
			// {"userID":"SMI","userInfoId":5},
			Collection<Informacoes> lstInf = atv.getInformacoes();
			
			for (Iterator iterator2 = lstInf.iterator(); iterator2.hasNext();)
			{
				Informacoes informacoes = (Informacoes) iterator2.next();
				Configuracoes config = layoutGerencia.pesquisarConfiguracao(informacoes.getConfiguracoesId());
				
				// strValor = "'" + formataValor(config,informacoes) + "'";
				strValor = formataValor(config, informacoes);
				if (config.getTipoCampo().equals(TipoCampo.IMAGEM))
				{
					strValor = "<img style=width:60px;height:60px; src='/slingcontrol/adm/imagens.htm?pageAction=onRenderImage&imageid="
						+ strValor + "'/>";
				}
				//strValor = informacoes.getDescricao().replaceAll("\\\"", "\\'"); 
				
				dataSet.append( "\"" + mapLabel.get(informacoes.getConfiguracoesId())  + "\":\"" +( strValor) + "\"");
				if( iterator2.hasNext())
				{
					dataSet.append(",");
				}
				
			}
			dataSet.append("}\n");
			if( iterator.hasNext())
			{
				dataSet.append(",");
			}
			
		}
		dataSet.append("]\n");
		 
		strJSON = strJSON + dataSet.toString();
		 
		strJSON =	strJSON + "}" ;//fechar gridmodel
		 
		
		
		 
		strJSON = strJSON + "\n}"; //fechar objeto
		return  strJSON;
	}
	
	
	
	private String formataValor(Configuracoes config, Informacoes info)
	{
		TipoCampoView tpCampo = (TipoCampoView) appContext.getBean(String.valueOf(config.getTipoCampo().getId()));
		String valor = tpCampo.formataInformacao(config, info);	
		
		return valor;
	}

}

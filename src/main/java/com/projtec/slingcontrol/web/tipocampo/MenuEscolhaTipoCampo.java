package com.projtec.slingcontrol.web.tipocampo;

import java.util.Collection;
import java.util.Iterator;

import org.apache.click.control.Field;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.persistencia.ConfiguracoesDAO;

@Component("4")
public class MenuEscolhaTipoCampo implements TipoCampoView
{
	@Autowired
	private ConfiguracoesDAO configuracoesDAO;

	@Override
	public Field montarField(Configuracoes configuracoes, Informacoes info)
	{
		Select selectField = new Select(String.valueOf(configuracoes.getId()));
		selectField.setLabel(configuracoes.getNomecampo());
		
		 if (configuracoes.getObrigatoriedade().equals("S"))
		 {
		    	selectField.setLabel(selectField.getLabel() + " <span class='required'>*</span>");
		    	selectField.setAttribute("class", "validate[required]");
		 }
		selectField.setStyle("width", "5cm");
		selectField.add(new Option("", "-- Selecione --"));
		selectField.setValue(" ");
		//valores do option - Configuracoes filho; 
		Configuracoes config;
		if (configuracoes.getFilhosConfig()!=null)
		{   Collection<Configuracoes> lstOpcoes = configuracoes.getFilhosConfig();
			for (Iterator<Configuracoes> iterator = lstOpcoes.iterator(); iterator.hasNext();) 
			{
				config = iterator.next();
				selectField.add(new Option(config.getId(),config.getNomecampo()));
				
			}		
		
		}
		
		if(info!=null )
		{
			selectField.setValue("" + info.getReferenciaId());
		}
		
		return selectField; 
	}

	
	@Override
	public String formataInformacao(Configuracoes configuracoes, Informacoes info) {
		
		Configuracoes configReferencia = configuracoesDAO.pesquisarId(info.getReferenciaId());
		
		if (configReferencia != null)
			return configReferencia.getNomecampo();
		else // Necessário por conta dos dados antigos no banco
			return "";
			
	}

}

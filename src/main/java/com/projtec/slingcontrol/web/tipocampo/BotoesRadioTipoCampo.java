package com.projtec.slingcontrol.web.tipocampo;

import java.util.Collection;
import java.util.Iterator;

import org.apache.click.control.Field;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.persistencia.ConfiguracoesDAO;

@Component("3")
public class BotoesRadioTipoCampo implements TipoCampoView
{
	
	@Autowired
	private ConfiguracoesDAO configuracoesDAO;

	@Override
	public Field montarField(Configuracoes configuracoes, Informacoes info)
	{
		RadioGroup radioGroup = new RadioGroup(String.valueOf(configuracoes.getId()));
		radioGroup.setLabel(configuracoes.getNomecampo());
		
		if (configuracoes.getObrigatoriedade().equals("S"))
		{
			radioGroup.setLabel(radioGroup.getLabel() + " <span class='required'>*</span>");
		}
		 
		radioGroup.setStyle("width", "" + configuracoes.getTamanho() + "px");
		
		if(info!=null )
		{
			radioGroup.setValue("" + info.getReferenciaId());
		}
		
		//valores do radio - opcoes.
		Configuracoes conf;
		if (configuracoes.getFilhosConfig()!=null)
		{   Collection<Configuracoes> lstConfFilho = configuracoes.getFilhosConfig();
			for (Iterator<Configuracoes> iterator = lstConfFilho.iterator(); iterator.hasNext();) 
			{
				conf = iterator.next();
				Radio rd = new Radio(String.valueOf(conf.getId()),conf.getNomecampo());
				if (configuracoes.getObrigatoriedade().equals("S"))
				{
					rd.setAttribute("class", "validate[required]");
				}
			
				radioGroup.add(rd);
				
			}
		    
		}
		
		return radioGroup; 
	}

	@Override
	public String formataInformacao(Configuracoes configuracoes,Informacoes info) 
	{
		
		Configuracoes configReferencia = configuracoesDAO.pesquisarId(info.getReferenciaId());
		
		if (configReferencia != null)
			return configReferencia.getNomecampo();
		else // Necessário por conta dos dados antigos no banco
			return "";
		
	}

}

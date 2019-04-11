package com.projtec.slingcontrol.web.tipocampo;

import org.apache.click.control.Field;
import org.apache.click.control.TextField;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;

@Component("7")
public class DataTipoCampo extends BaseTipoCampo implements TipoCampoView
{

	@Override
	public Field montarField(Configuracoes configuracoes, Informacoes info)
	{
		TextField dateField = new TextField(String.valueOf(configuracoes.getId()));
		String inputClass = "date-pick";
		dateField.setLabel(configuracoes.getNomecampo());
		
		//colocado em 15/04/2013
		dateField.setSize(10);
		dateField.setMaxLength(configuracoes.getTamanho());
		
		if (configuracoes.getObrigatoriedade().equals("S"))
		{
			dateField.setLabel(dateField.getLabel() + " <span class='required'>*</span>");
			inputClass += " validate[required]";
		}
		
		adicionaMascaraAoCampo(dateField, configuracoes);
		
		dateField.setAttribute("class", inputClass);
		
		if(info!=null )
		{
			dateField.setValue(info.getDescricao());
		}
		
		return dateField ; 	
	
	}

	@Override
	public String formataInformacao(Configuracoes configuracoes,Informacoes info) {
		
		return info.getDescricao();
	}
	
}

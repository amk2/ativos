package com.projtec.slingcontrol.web.tipocampo;

import org.apache.click.control.Field;
import org.apache.click.control.TextField;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;

@Component("5")
public class NumericoInteiroTipoCampo extends BaseTipoCampo implements TipoCampoView
{

	@Override
	public Field montarField(Configuracoes configuracoes, Informacoes info)
	{
		
		// IntegerField intField = new IntegerField(String.valueOf(configuracoes.getId()));
		
		TextField intField = new TextField(String.valueOf(configuracoes.getId()));
		intField.setLabel(configuracoes.getNomecampo());
		
		if (configuracoes.getObrigatoriedade().equals("S"))
		{
			intField.setLabel(intField.getLabel() + " <span class='required'>*</span>");
			intField.setAttribute("class", "validate[required]");
		}
		
		adicionaMascaraAoCampo(intField, configuracoes);
		
		// intField.setMaxLength(15);
		intField.setMaxLength(configuracoes.getTamanho());
		
		intField.setStyle("float","left");
		intField.setStyle("width", "5cm");
		
		if(info!=null )
		{
			intField.setValue(info.getDescricao());
		}
		
		return intField ; 
	}

	@Override
	public String formataInformacao(Configuracoes configuracoes, Informacoes info) 
	{
		
		return info.getDescricao();
	}

}

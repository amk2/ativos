package com.projtec.slingcontrol.web.tipocampo;

import org.apache.click.control.Field;
import org.apache.click.control.TextField;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;

@Component("6")
public class NumericoDecimalTipoCampo extends BaseTipoCampo implements TipoCampoView
{

	@Override
	public Field montarField(Configuracoes configuracoes, Informacoes info)
	{
		// NumberField numField = new NumberField(String.valueOf(configuracoes.getId()));
		
		TextField numField = new TextField(String.valueOf(configuracoes.getId()));
		
		numField.setLabel(configuracoes.getNomecampo());
		if (configuracoes.getObrigatoriedade().equals("S"))
		{
			numField.setLabel(numField.getLabel() + " <span class='required'>*</span>");
			numField.setAttribute("class", "validate[required]");
		}
		
		adicionaMascaraAoCampo(numField, configuracoes);
		
		numField.setStyle("width", "5cm");
		numField.setStyle("float","left");
		//textArea.setRows(configuracoes.getTamanho()/2);
		
		if(info!=null )
		{
			numField.setValue(info.getDescricao());
		}
		
		return numField ; 
	}

	@Override
	public String formataInformacao(Configuracoes configuracoes, Informacoes info) {
		
		return info.getDescricao();
	}

}

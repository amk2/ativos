package com.projtec.slingcontrol.web.tipocampo;

import org.apache.click.control.Field;
import org.apache.click.control.TextField;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;

@Component("1")
public class TextoTipoCampo implements TipoCampoView
{

	@Override
	public Field montarField(Configuracoes configuracoes, Informacoes info)
	{
		TextField textField = new TextField(String.valueOf(configuracoes.getId()));
		textField.setLabel(configuracoes.getNomecampo());
		
		if (configuracoes.getObrigatoriedade().equals("S"))
		{
			textField.setLabel(textField.getLabel() + " <span class='required'>*</span>");
			textField.setAttribute("class", "validate[required]");
		}

		//tirado em 15/04/2013
		//textField.setStyle("width", "10cm");
		//textField.setSize(100);


		//========== colocado em 15/04/2013 ===============================================
		if (configuracoes.getTamanho()>180)
		{
			textField.setSize(149);
		}
		else if ((configuracoes.getTamanho()>150) && (configuracoes.getTamanho()<=180))
		{
			textField.setSize(149);
		}
		else
		{
			textField.setSize(configuracoes.getTamanho());
		}
		//=================================================================================
		
		
		textField.setMaxLength(configuracoes.getTamanho());

		//textField.setMinLength(3);

		if(info!=null )
		{
			textField.setValue(info.getDescricao());
		}
		return textField ; 
	}

	@Override
	public String formataInformacao(Configuracoes configuracoes,Informacoes info) 
	{
		
		return info.getDescricao();
	}

}

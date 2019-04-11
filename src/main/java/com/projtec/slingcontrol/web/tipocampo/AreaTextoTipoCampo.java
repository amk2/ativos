package com.projtec.slingcontrol.web.tipocampo;

import org.apache.click.control.Field;
import org.apache.click.control.TextArea;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;

@Component("2")
public class AreaTextoTipoCampo implements TipoCampoView
{

	@Override
	public Field montarField(Configuracoes configuracoes, Informacoes info)
	{
		TextArea textArea ; 
		textArea = new TextArea(String.valueOf(configuracoes.getId()));
		textArea.setLabel(configuracoes.getNomecampo());
		// textArea.setRequired(configuracoes.getObrigatoriedade());
		 if (configuracoes.getObrigatoriedade().equals("S"))
		 {
			 textArea.setLabel(textArea.getLabel() + " <span class='required'>*</span>");
			 textArea.setAttribute("class", "validate[required]");
		 }
		 
		textArea.setWidth(configuracoes.getTamanho() + "pol" );
	
		
		if(info!=null )
		{
			textArea.setValue(info.getDescricao());
		}
		
		return  textArea ; 
	}

	@Override
	public String formataInformacao(Configuracoes configuracoes, Informacoes info) 
	{
		return info.getDescricao();
	}
	

}

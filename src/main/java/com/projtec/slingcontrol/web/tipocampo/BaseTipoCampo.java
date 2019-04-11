package com.projtec.slingcontrol.web.tipocampo;

import org.apache.click.control.Field;

import com.projtec.slingcontrol.modelo.Configuracoes;

public abstract class BaseTipoCampo {
	
	public void adicionaMascaraAoCampo(Field campo, Configuracoes configuracoes) {
		
		if (this.getClass().equals(DataTipoCampo.class)) {
		
			// Referencia: http://digitalbush.com/projects/masked-input-plugin/
			
			String funcaoJSData = "<script type='text/javascript'>jQuery(function($){$('#campoData').mask('99/99/9999',{placeholder:'_'});});</script>";
			funcaoJSData = funcaoJSData.replaceAll("campoData", "campoData" + configuracoes.getId());
			campo.setId("campoData" + configuracoes.getId());
			campo.setLabel(campo.getLabel() + funcaoJSData);
			
		}
		
		if (this.getClass().equals(NumericoDecimalTipoCampo.class)) {
			
			// Referencia: http://www.tuliofaria.net/jquery-floatnumber/
			// Alternativa não adotada ainda: http://www.meiocodigo.com/projects/meiomask/
			
			campo.setId("campoNumericoDecimal" + configuracoes.getId());
			campo.setAttribute("maxlength", "" + configuracoes.getTamanho());
			campo.setLabel(campo.getLabel());			
			campo.setStyle("text-align","right");
			
			// Esta classe css será usada pela API JQuery (jquery-floatnumber e jquery-numeric-pack)
			// Ver ultima linha do border-template
			campo.setAttribute("class", "numero-decimal");
			
			
			
		}
		
		if (this.getClass().equals(NumericoInteiroTipoCampo.class)) {
			
			// Referencia: http://www.tuliofaria.net/jquery-floatnumber/
			// Alternativa não adotada ainda: http://www.meiocodigo.com/projects/meiomask/
			
			campo.setId("campoNumericoInteiro" + configuracoes.getId());
			campo.setLabel(campo.getLabel());
			campo.setStyle("text-align","right");
			campo.setAttribute("maxlength", "" + configuracoes.getTamanho());
			
			// Esta classe css será usada pela API JQuery (jquery-floatnumber e jquery-numeric-pack)
			// Ver ultima linha do border-template
			campo.setAttribute("class", "numero-inteiro");  
			
		}
		
	}

}

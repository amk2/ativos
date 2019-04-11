package com.projtec.slingcontrol.web.tipocampo;

import org.apache.click.control.Field;

import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;

public interface TipoCampoView
{

	public static final String CAMPO_PESQUISA_SIMBOLO = "+" ; 
	
	Field  montarField(Configuracoes configuracoes, Informacoes info);
	
	String formataInformacao(Configuracoes configuracoes, Informacoes info);
}

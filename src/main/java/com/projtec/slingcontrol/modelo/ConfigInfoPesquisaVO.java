package com.projtec.slingcontrol.modelo;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ConfigInfoPesquisaVO 
{
	 private Configuracoes config;
	 
	 private Informacoes infoDe; 
	 
	 private Informacoes infoAte;
	 

	public Configuracoes getConfig() {
		return config;
	}

	public void setConfig(Configuracoes config) {
		this.config = config;
	}

	public Informacoes getInfoDe() {
		return infoDe;
	}

	public void setInfoDe(Informacoes infoDe) {
		this.infoDe = infoDe;
	}

	public Informacoes getInfoAte() {
		return infoAte;
	}

	public void setInfoAte(Informacoes infoAte) {
		this.infoAte = infoAte;
	} 
	
	
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}

}

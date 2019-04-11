package com.projtec.slingcontrol.modelo;

import org.apache.commons.lang.builder.ToStringBuilder;

public class DemandasItemAtivo 
{
	private Integer idDemanda;
	private ItensAtivos itemAtivos;
	private Local  localOrigem;
	private Local  localDestino;
	
	
	public ItensAtivos getItemAtivos() {
		return itemAtivos;
	}
	public void setItemAtivos(ItensAtivos itemAtivos) {
		this.itemAtivos = itemAtivos;
	}
	public Integer getIdDemanda() {
		return idDemanda;
	}
	public void setIdDemanda(Integer idDemanda) {
		this.idDemanda = idDemanda;
	}
	public Local getLocalDestino() {
		return localDestino;
	}
	public void setLocalDestino(Local localDestino) {
		this.localDestino = localDestino;
	}
	public Local getLocalOrigem() {
		return localOrigem;
	}
	public void setLocalOrigem(Local localOrigem) {
		this.localOrigem = localOrigem;
	}
	
	@Override
	public String toString() {
	
		return ToStringBuilder.reflectionToString(this);
	}

}

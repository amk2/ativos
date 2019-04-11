package com.projtec.slingcontrol.modelo;

import java.util.Collection;

import org.apache.commons.lang.builder.ToStringBuilder;


public class Layout {

	private int id;
	private String nome;	
	private String descricao;
	private TipoLayout tipoLayout;	
	private Collection<Configuracoes> configuracoes; 





	public Collection<Configuracoes> getConfiguracoes() {
		return configuracoes;
	}

	public void setConfiguracoes(Collection<Configuracoes> configuracoes) {
		this.configuracoes = configuracoes;
	}

	public TipoLayout getTipoLayout() {
		return tipoLayout;
	}

	public void setTipoLayout(TipoLayout tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	@Override
	public String toString() 
	{	
    	return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj)
	{
		Layout l = (Layout) obj; 
		
		return  this.id == l.id;
	}

}

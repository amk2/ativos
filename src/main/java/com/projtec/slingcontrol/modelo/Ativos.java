package com.projtec.slingcontrol.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Ativos implements Serializable
{
	private Integer id;
	private int qtdItens;
	private int localID;
	private Layout layout;
	private Collection<Informacoes> informacoes;
	private Map<Integer, Informacoes> mapInformacoes;
	private Collection<Integer> lstPais ;
	private List<AtivosNo> lstFilhos ;

	
	public String getItens_ativo_identificacaodoSQL() {
		return itens_ativo_identificacaodoSQL;
	}

	public void setItens_ativo_identificacaodoSQL(
			String itens_ativo_identificacaodoSQL) {
		this.itens_ativo_identificacaodoSQL = itens_ativo_identificacaodoSQL;
	}

	private String itens_ativo_identificacaodoSQL;

	
	
	public List<AtivosNo> getLstFilhos() {
		return lstFilhos;
	}

	public void setLstFilhos(List<AtivosNo> lstFilhos) {
		this.lstFilhos = lstFilhos;
	}

	public Collection<Integer> getDependencias() 
	{
		return lstPais;
	}

	public void setDependencias(Collection<Integer> dependencias) {
		this.lstPais = dependencias;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}
	
	public Layout getLayout()
	{
		return layout;
	}

	public void setLayout(Layout layout)
	{
		this.layout = layout;
	}



	public Collection<Informacoes> getInformacoes()
	{
		return informacoes;
	}

	public void setInformacoes(Collection<Informacoes> informacoes)
	{
		this.informacoes = informacoes;
	}

	public Map<Integer, Informacoes> getMapInformacoes()
	{
		return mapInformacoes;
	}

	public void setMapInformacoes(Map<Integer, Informacoes> mapInformacoes)
	{
		this.mapInformacoes = mapInformacoes;
	}

	@Override
	public String toString()
	{

		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj)
	{
		Ativos atv = (Ativos) obj;
		return  this.id==atv.getId() ;
	}
	
	@Override
	public int hashCode()
	{
		int hash = 7;
		if (id!=null)
		{
			hash = 23 * hash + this.id;
		}
		return hash;
	}

	public void setQtdItens(int qtdItens) {
		this.qtdItens = qtdItens;
	}

	public int getQtdItens() {
		return qtdItens;
	}

	public int getLocalID() {
		return localID;
	}

	public void setLocalID(int localID) {
		this.localID = localID;
	}
	
}


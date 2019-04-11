package com.projtec.slingcontrol.modelo;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Informacoes
{
	protected Integer id;
	protected Integer idTipo;
	protected String descricao;
	protected Integer configuracoesId;
	protected Integer referenciaId;
	
	public Informacoes() {
		
	}
	
	public Informacoes(String descricao) {
		this.descricao = descricao;
	}

	public Integer getConfiguracoesId()
	{
		return configuracoesId;
	}

	public void setConfiguracoesId(Integer configuracoesId)
	{
		this.configuracoesId = configuracoesId;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getIdTipo()
	{
		return idTipo;
	}

	public void setIdTipo(Integer idTipo)
	{
		this.idTipo = idTipo;
	}

	public String getDescricao()
	{
		return descricao;
	}

	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	@Override
	public String toString()
	{

		return ToStringBuilder.reflectionToString(this);
	}

	public void copiaInformacoes(Informacoes info)
	{
		id = info.getId();
		//idReferencia = info.getIdReferencia();
		descricao = info.getDescricao();
		configuracoesId = info.getConfiguracoesId();
		configuracoesId = info.getReferenciaId();
		
		// lstReferencias = info.getLstReferencias();
		
	}

	@Override
	public boolean equals(Object obj)
	{

		return EqualsBuilder.reflectionEquals(obj, this);
	}

	public Integer getReferenciaId() {
		return referenciaId;
	}

	public void setReferenciaId(Integer referenciaId) {
		this.referenciaId = referenciaId;
	}

}

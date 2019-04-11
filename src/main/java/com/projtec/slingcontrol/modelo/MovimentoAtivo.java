package com.projtec.slingcontrol.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public class MovimentoAtivo implements Serializable
{
	private Integer id;
	private Layout layoutId;
	private Demandas demandaId;
	private TipoMovimentoAtivo tipomovimento;
	private Collection<Informacoes> InformacoesMovimentoAtivo;
	private Map<Integer, Informacoes> mapInformacoesMovimentoAtivo;
	private Date dataCadastro;

	public Map<Integer, Informacoes> getMapInformacoesMovimentoAtivo()
	{
		return mapInformacoesMovimentoAtivo;
	}

	public void setMapInformacoesMovimentoAtivo(
			Map<Integer, Informacoes> mapInformacoesMovimentoAtivo)
	{
		this.mapInformacoesMovimentoAtivo = mapInformacoesMovimentoAtivo;
	}

	public Collection<Informacoes> getInformacoesMovimentoAtivo()
	{
		return InformacoesMovimentoAtivo;
	}

	public void setInformacoesMovimentoAtivo(
			Collection<Informacoes> informacoesMovimentoAtivo)
	{
		InformacoesMovimentoAtivo = informacoesMovimentoAtivo;
	}

	public Layout getLayoutId()
	{
		return layoutId;
	}

	public void setLayoutId(Layout layoutId)
	{
		this.layoutId = layoutId;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Demandas getDemandaId()
	{
		return demandaId;
	}

	public void setDemandaId(Demandas demandaId)
	{
		this.demandaId = demandaId;
	}

	public TipoMovimentoAtivo getTipomovimento()
	{
		return tipomovimento;
	}

	public void setTipomovimento(TipoMovimentoAtivo tipomovimento)
	{
		this.tipomovimento = tipomovimento;
	}
	
	
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Override
	public boolean equals(Object obj)
	{
		MovimentoAtivo mvAtivo = (MovimentoAtivo) obj; 
		return this.getId()==mvAtivo.getId();
		
	}

	@Override
	public String toString()
	{
		
		return ToStringBuilder.reflectionToString(this);
	}



}

package com.projtec.slingcontrol.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Demandas implements Serializable {

	private Integer id;
	private Layout layout;
	private TipoDemanda tipoDemanda;
	private StatusDemanda statusDemanda;
	private Integer idPai;
	private Collection<Informacoes> informacoesDemanda;
	private Map<Integer, Informacoes> mapInformacoesDemanda;
	private Collection<DemandasItemAtivo> lstDemandasItensAtivos;
	private Date dataCadastro;
	private Date dataFechamento;
	private Boolean todosItensLocalOrigem;

	public Map<Integer, Informacoes> getMapInformacoesDemanda() {
		return mapInformacoesDemanda;
	}

	public void setMapInformacoesDemanda(
			Map<Integer, Informacoes> mapInformacoesDemanda) {
		this.mapInformacoesDemanda = mapInformacoesDemanda;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public TipoDemanda getTipoDemanda() {
		return tipoDemanda;
	}

	public void setTipoDemanda(TipoDemanda tipoDemanda) {
		this.tipoDemanda = tipoDemanda;
	}

	public Collection<Informacoes> getInformacoesDemanda() {
		return informacoesDemanda;
	}

	public void setInformacoesDemanda(Collection<Informacoes> informacoesDemanda) {
		this.informacoesDemanda = informacoesDemanda;
	}
	
	public Integer getIdPai() {
		return idPai;
	}

	public void setIdPai(Integer idPai) {
		this.idPai = idPai;
	}



	public Collection<DemandasItemAtivo> getLstDemandasItensAtivos() {
		return lstDemandasItensAtivos;
	}

	public void setLstDemandasItensAtivos(
			Collection<DemandasItemAtivo> lstDemandasItensAtivos) {
		this.lstDemandasItensAtivos = lstDemandasItensAtivos;
	}

	public StatusDemanda getStatusDemanda() {
		return statusDemanda;
	}

	public void setStatusDemanda(StatusDemanda statusDemanda) {
		this.statusDemanda = statusDemanda;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public Boolean getTodosItensLocalOrigem() {
		return todosItensLocalOrigem;
	}

	public void setTodosItensLocalOrigem(Boolean todosItensLocalOrigem) {
		this.todosItensLocalOrigem = todosItensLocalOrigem;
	}
	
	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}
	
	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this);

	}

	@Override
	public boolean equals(Object obj) {
		Demandas demanda = (Demandas) obj;

		return this.id == demanda.id;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.id==null?0:this.id);
		return hash;
	}



	

}

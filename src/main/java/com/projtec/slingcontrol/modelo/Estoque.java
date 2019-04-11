package com.projtec.slingcontrol.modelo;

import java.util.Collection;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Estoque {

	private int id;
	private Layout layout;
	
	@NotNull(message = "{estoque.nome.requerido}")
	@Size(min = 3, max = 10, message = "{estoque.nome.size}")
	private String nome;

	@NotNull(message = "{estoque.descricao.requerido}")
	@Size(min = 3, max = 80, message = "{estoque.descricao.size}")
	private String descricao;

	private Collection<Informacoes> infos;
	private Map<Integer, Informacoes> mapInformacoesLocais;
	
	public Map<Integer, Informacoes> getMapInformacoesLocais() {
		return mapInformacoesLocais;
	}

	public void setMapInformacoesLocais(Map<Integer, Informacoes> mapInformacoesLocais) {
		this.mapInformacoesLocais = mapInformacoesLocais;
	}
	
	public Collection<Informacoes> getInfos() {
		return infos;
	}

	public void setInfos(Collection<Informacoes> infos) {
		this.infos = infos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	

	@Override
	public boolean equals(Object obj) {
		Estoque est = (Estoque) obj;
		return this.id == est.getId();
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public Layout getLayout() {
		return layout;
	}

}

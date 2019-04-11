package com.projtec.slingcontrol.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Local implements Serializable
{
	private Integer id;
	private String nome;
	private String descricao;
	private Local localRef;
	private Layout layout;
	private String localizacao_x;
	private String localizacao_y;
	private String latitude;
	private String longitude;
	private String identificacao;
	private Estoque estoque;
	
	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	private Collection<Informacoes> infos;
	private Map<Integer, Informacoes> mapInformacoesLocais;

	public Map<Integer, Informacoes> getMapInformacoesLocais()
	{
		return mapInformacoesLocais;
	}

	public void setMapInformacoesLocais(
			Map<Integer, Informacoes> mapInformacoesLocais)
	{
		this.mapInformacoesLocais = mapInformacoesLocais;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getDescricao()
	{
		return descricao;
	}

	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	public Local getLocalRef()
	{
		return localRef;
	}

	public void setLocalRef(Local localRef)
	{
		this.localRef = localRef;
	}

	public Layout getLayout()
	{
		return layout;
	}

	public void setLayout(Layout layout)
	{
		this.layout = layout;
	}
	
	public String getLocalizacao_x() {
		return localizacao_x;
	}

	public void setLocalizacao_x(String localizacao_x) {
		this.localizacao_x = localizacao_x;
	}

	public String getLocalizacao_y() {
		return localizacao_y;
	}

	public void setLocalizacao_y(String localizacao_y) {
		this.localizacao_y = localizacao_y;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}

	public Estoque getEstoque() {
		return estoque;
	}

	public Collection<Informacoes> getInfos()
	{
		return infos;
	}

	public void setInfos(Collection<Informacoes> infos)
	{
		this.infos = infos;
	}

	@Override
	public String toString()
	{

		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj)
	{
		Local l = (Local) obj; 
		return this.getId()==l.getId();
	}
}

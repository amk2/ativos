package com.projtec.slingcontrol.modelo;

import java.util.Collection;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Configuracoes
{

	public static final String PESQ_SIM="S";
	public static final String PESQ_NAO="N";
	  
	private Integer id;
	private Integer idRef;
	private String nomecampo;
	private String obrigatoriedade;
	private String pesquisa;	
	private Integer tamanho;
	private Integer ordem;
	private TipoCampo tipoCampo;
	private Integer idLayout;
	private Layout layout; 
	private Collection<Configuracoes> filhosConfig;//<- opções

	public Collection<Configuracoes> getFilhosConfig()
	{
		return filhosConfig;
	}

	public void setFilhosConfig(Collection<Configuracoes> filhosConfig)
	{
		this.filhosConfig = filhosConfig;
	}

	public Integer getOrdem()
	{
		return ordem;
	}

	public void setOrdem(Integer ordem)
	{
		this.ordem = ordem;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}
	
	
	public Integer getIdRef()
	{
		return idRef;
	}

	public void setIdRef(Integer idRef)
	{
		this.idRef = idRef;
	}

	public String getNomecampo()
	{
		return nomecampo;
	}

	public void setNomecampo(String nomecampo)
	{
		this.nomecampo = nomecampo;
	}

	public Integer getTamanho()
	{
		return tamanho;
	}

	public String getObrigatoriedade() {
		return obrigatoriedade;
	}

	public void setObrigatoriedade(String obrigatoriedade) {
		this.obrigatoriedade = obrigatoriedade;
	}
	
	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	
	public void setTamanho(Integer tamanho)
	{
		this.tamanho = tamanho;
	}

	public TipoCampo getTipoCampo()
	{
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampo tipoCampo)
	{
		this.tipoCampo = tipoCampo;
	}

	public Integer getIdLayout()
	{
		return idLayout;
	}

	public void setIdLayout(Integer idLayout)
	{
		this.idLayout = idLayout;
	}
	
	public Layout getLayout()
	{
		return layout;
	}

	public void setLayout(Layout layout)
	{
		this.layout = layout;
	}
	


	@Override
	public String toString()
	{

		return ToStringBuilder.reflectionToString(this);

	}

	@Override
	public boolean equals(Object obj)
	{
		Configuracoes conf = (Configuracoes) obj;
		return this.id == conf.id;
	}



}

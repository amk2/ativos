package com.projtec.slingcontrol.modelo;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AtivosNo {
	private Integer pai ;
	private Integer filho;
	
	public Integer getPai() 
	{
		return pai;
	}
	public void setPai(Integer pai) 
	{
		this.pai = pai;
	}
	public Integer getFilho() 
	{
		return filho;
	}
	public void setFilho(Integer filho) 
	{
		this.filho = filho;
	}
	
	@Override
	public String toString()
	{

		return ToStringBuilder.reflectionToString(this);
	}
	
}

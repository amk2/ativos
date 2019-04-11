package com.projtec.slingcontrol.modelo;

public class StatusDemanda {
	
	public final static  String ABERTO="ABERTA"; 
	public final static  Integer ID_ABERTO=1; 
	public final static  String CONCLUIDO= "CONCLUIDA"; 
	public final static  String EM_ANDAMENTO= "EM ANDAMENTO"; 
	public final static  String CANCELADO= "CANCELADA"; 
	

	private Integer id;
	private String descricao;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	

}

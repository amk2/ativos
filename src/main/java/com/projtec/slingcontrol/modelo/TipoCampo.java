package com.projtec.slingcontrol.modelo;

import org.apache.commons.lang.builder.ToStringBuilder;

public class TipoCampo {
	
    public static final TipoCampo TEXTO=new TipoCampo(1);
	public static final TipoCampo AREA_TEXTO=new TipoCampo(2);
	public static final TipoCampo BOTOES_RADIO=new TipoCampo(3);
	public static final TipoCampo MENU_ESCOLHA=new TipoCampo(4);
	public static final TipoCampo NUMERO_INT = new TipoCampo(5);
	public static final TipoCampo NUMERO_DEC = new TipoCampo(6);
	public static final TipoCampo DATA = new TipoCampo(7);
	public static final TipoCampo IMAGEM = new TipoCampo(8);
	public static final TipoCampo MENU_ESCOLHA_MULTIPLO = new TipoCampo(9);
	
	
	
	private int id;
	private String descricao;
	
	
	public TipoCampo()
	{
	
	}
	
	public TipoCampo(int i )
	{
		id = i; 
		
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
		TipoCampo tp = (TipoCampo) obj;
		return this.id == tp.id;
		
	}

}

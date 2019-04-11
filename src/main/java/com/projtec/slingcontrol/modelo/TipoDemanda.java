package com.projtec.slingcontrol.modelo;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TipoDemanda {

	private int id;
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	 @Override
	public boolean equals(Object obj) {
		 TipoDemanda tp = (TipoDemanda) obj;
			return tp.getId()==this.getId();
		
	}

}

package com.projtec.slingcontrol.modelo;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ItensAtivos 
{

	private Integer itensAtivosId;
	private Local   local;
	private Ativos   ativo;
	private String identificacao;
	private Date dtEntrada;
	private Date dtSaida; 
	private Date dtVencimento;
	private String codFabricante;



	private String 	itens_ativo_localizacao_x;
	private String 	itens_ativo_localizacao_y;
	private String 	itens_ativo_localizacao_z;
	private String 	itens_ativo_latitude;
	private String 	itens_ativo_longitude;
	
	
	public Integer getItensAtivosId() {
		return itensAtivosId;
	}

	public void setItensAtivosId(Integer itensAtivosId) {
		this.itensAtivosId = itensAtivosId;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Ativos getAtivo() {
		return ativo;
	}

	public void setAtivo(Ativos ativo) {
		this.ativo = ativo;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this);
	}

	public Date getDtEntrada() {
		return dtEntrada;
	}

	public void setDtEntrada(Date dtEntrada) {
		this.dtEntrada = dtEntrada;
	}

	public Date getDtSaida() {
		return dtSaida;
	}

	public void setDtSaida(Date dtSaida) {
		this.dtSaida = dtSaida;
	}

	public Date getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public String getCodFabricante() {
		return codFabricante;
	}

	public void setCodFabricante(String codFabricante) {
		this.codFabricante = codFabricante;
	}
	
//*********************************************************************************************************
	public String getItens_ativo_localizacao_x() {
		return itens_ativo_localizacao_x;
	}

	public void setItens_ativo_localizacao_x(String itens_ativo_localizacao_x) {
		this.itens_ativo_localizacao_x = itens_ativo_localizacao_x;
	}

	public String getItens_ativo_localizacao_y() {
		return itens_ativo_localizacao_y;
	}

	public void setItens_ativo_localizacao_y(String itens_ativo_localizacao_y) {
		this.itens_ativo_localizacao_y = itens_ativo_localizacao_y;
	}

	public String getItens_ativo_localizacao_z() {
		return itens_ativo_localizacao_z;
	}

	public void setItens_ativo_localizacao_z(String itens_ativo_localizacao_z) {
		this.itens_ativo_localizacao_z = itens_ativo_localizacao_z;
	}

	public String getItens_ativo_latitude() {
		return itens_ativo_latitude;
	}

	public void setItens_ativo_latitude(String itens_ativo_latitude) {
		this.itens_ativo_latitude = itens_ativo_latitude;
	}

	public String getItens_ativo_longitude() {
		return itens_ativo_longitude;
	}

	public void setItens_ativo_longitude(String itens_ativo_longitude) {
		this.itens_ativo_longitude = itens_ativo_longitude;
	}
	

	
	
	
}

package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaGridView 
{
	private Integer idLayout;
	private String titulo; 
	//chave  configuracao - valores
    private  List<Map<String,String>> linhasValores = new ArrayList<Map<String,String>>();
    //nomes para as tabelas...
    private  Map<String, String> nomes = new HashMap<String, String>();
    private  Integer total;
    
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Map<String, String> getNomes() {
		return nomes;
	}
	public void setNomes(Map<String, String> nomes) {
		this.nomes = nomes;
	}

	public Integer getIdLayout() {
		return idLayout;
	}
	public void setIdLayout(Integer idLayout) {
		this.idLayout = idLayout;
	}
	public List<Map<String, String>> getLinhasValores() {
		return linhasValores;
	}
	public void setLinhasValores(List<Map<String, String>> linhasValores) {
		this.linhasValores = linhasValores;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	

    
}

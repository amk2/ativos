package com.projtec.slingcontrol.gerencia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;

import com.projtec.slingcontrol.modelo.Informacoes;

public abstract class InformacoesTemplate
{


	public Collection<Informacoes> incluirListaInformacoes(Collection<Informacoes> lstInformacoes , Integer idTipo) {
		
		Informacoes novaInfo;
		Collection<Informacoes> infosNova = new ArrayList<Informacoes>();
		
		if (CollectionUtils.isNotEmpty(lstInformacoes)) {
		
			for (Iterator<Informacoes> iterator = lstInformacoes.iterator(); iterator.hasNext();) 
			{
				Informacoes informacoes = iterator.next();
	
				informacoes.setIdTipo(idTipo);
				novaInfo = incluirInfomacoes(informacoes);			
				infosNova.add(novaInfo);
	
			}
			
		}
		
		return infosNova;
	}
	
	
	public abstract Informacoes incluirInfomacoes(Informacoes info);
	

}

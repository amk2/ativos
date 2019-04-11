package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.click.Page;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Select;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.ImagensGerencia;
import com.projtec.slingcontrol.modelo.ConfigInfoPesquisaVO;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Imagens;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.web.tipocampo.ImageField;

@Component("informacoesUtil")
public class InformacoesUtil extends Page
{
	
	@Autowired
	protected ImagensGerencia  imagensGerencia;

	public List<Informacoes> montarInfomacoes(FieldSet configs)
	{
		Collection<Field> fields;
		Informacoes info;
		List<Informacoes> lstInfos = new ArrayList<Informacoes>();
		fields = configs.getFieldList();
		@SuppressWarnings("unchecked")
		List<String> imageFields = (List<String>) getContext().getSession().getAttribute("imageFields");
		int i = 0;
		for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();)
		{
			Field field = iterator.next();

			if (field instanceof Select)
			{
				montarInfoSelect(lstInfos, field);

			} 
			else if (field instanceof RadioGroup) {
				
				montarInfoRadio(lstInfos, field);
					
			}
			else if (field instanceof ImageField) {
				
				montarInfoImagem(lstInfos, field);
				
			}
			// else if (!field.getValue().equals(""))
			else if (StringUtils.isNotBlank(field.getValue()))
			{
				info = new Informacoes();
				info.setConfiguracoesId(Integer.valueOf(field.getName()));
				info.setDescricao(field.getValue());
				lstInfos.add(info);
			} 
			
		}

		return lstInfos;
	}
	
	
	
	public Collection<ConfigInfoPesquisaVO> montarConfInfoPesquisa(FieldSet configs)
	{
		Collection<Field> fields;
		Informacoes info;
		ConfigInfoPesquisaVO confInfoVo;
		Map<Integer, ConfigInfoPesquisaVO> mapConfgInfo = new HashMap<Integer, ConfigInfoPesquisaVO>();
		//List<Informacoes> lstInfos = new ArrayList<Informacoes>();
		fields = configs.getFieldList();
		
	
		
		for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();)
		{
			Field field = iterator.next();

	
	     	if (!field.getValue().equals(""))
			{
	     		
	     		String strName = field.getName();
	     		String strIdConfig;
	     		if (StringUtils.contains(strName, "_DE") )
	     		{
	     			strIdConfig = StringUtils.substringBefore(strName, "_DE");
	     			info = new Informacoes();
	     			info.setConfiguracoesId(Integer.valueOf(strIdConfig));
					info.setDescricao(field.getValue());
					if (mapConfgInfo.containsKey(info.getConfiguracoesId() ) )
					{
						confInfoVo = mapConfgInfo.get(info.getConfiguracoesId());	
						confInfoVo.setInfoDe(info);
						
					}else
					{
						confInfoVo = new ConfigInfoPesquisaVO();
						Configuracoes config = new Configuracoes();
						config.setId(info.getConfiguracoesId());
						confInfoVo.setConfig(config);
						confInfoVo.setInfoDe(info);
						mapConfgInfo.put(info.getConfiguracoesId(), confInfoVo);
					}
	     			
	     		}else if (StringUtils.contains(strName, "_ATE") )
	     		{
	     			strIdConfig = StringUtils.substringBefore(strName, "_ATE");
	     			info = new Informacoes();
	     			info.setConfiguracoesId(Integer.valueOf(strIdConfig));
					info.setDescricao(field.getValue());
					if (mapConfgInfo.containsKey(info.getConfiguracoesId() ) )
					{
						confInfoVo = mapConfgInfo.get(info.getConfiguracoesId());
						confInfoVo.setInfoAte(info);
						
					}else
					{
						confInfoVo = new ConfigInfoPesquisaVO();
						Configuracoes config = new Configuracoes();
						config.setId(info.getConfiguracoesId());
						confInfoVo.setConfig(config);
						confInfoVo.setInfoAte(info);
						mapConfgInfo.put(info.getConfiguracoesId(), confInfoVo);
					}
	     			
	     		}else 
	     		{
	     			
	     			
	     			List<Informacoes>  lstInfos;
	     			lstInfos = new ArrayList<Informacoes>();
	     			
	     			if (field instanceof Select)
	    			{
	    				montarInfoSelect(lstInfos, field);
	    			} 
	    			else if (field instanceof RadioGroup) 
	    			{
	    				montarInfoRadio(lstInfos, field);		
	    			}
	    			else if (field instanceof ImageField) {
	    				
	    				montarInfoImagem(lstInfos, field);
	    				
	    			}
	    			// else if (!field.getValue().equals(""))
	    			else if (StringUtils.isNotBlank(field.getValue()))
	    			{
	    				info = new Informacoes();
	    				info.setConfiguracoesId(Integer.valueOf(field.getName()));
	    				info.setDescricao(field.getValue());
	    				lstInfos.add(info);
	    			}

	     			montarConfigInfoPesquisa(mapConfgInfo,lstInfos);
	     			
	     			
	     		}//fim else 
			
			} 
			
		}

		return mapConfgInfo.values();
	}



	private void montarInfoImagem(List<Informacoes> lstInfos, Field field) {
		Informacoes info;
		String configId = field.getName().replace("campoImagem", ""); // este foi o nome dado no ImagemTipoCampo
		
		info = new Informacoes();
		info.setConfiguracoesId(Integer.valueOf(configId));
		
		if (StringUtils.isNotBlank(field.getValue())) {
			
			Imagens imagem = imagensGerencia.pesquisarPeloNome(field.getValue());
			info.setReferenciaId(imagem.getId());
			
		}
		
		lstInfos.add(info);
	}



	private void montarInfoRadio(List<Informacoes> lstInfos, Field field) {
		Informacoes info;
		// if (field.getValue() != null && !"".equals(field.getValue())) {
		if (StringUtils.isNotBlank(field.getValue())) 
		{
		
			info = new Informacoes();
			info.setConfiguracoesId(Integer.valueOf(field.getName()));
			info.setReferenciaId(new Integer(field.getValue()));
			
			lstInfos.add(info);
		
		}
	}



	private void montarInfoSelect(List<Informacoes> lstInfos, Field field) {
		
		Informacoes info;
		Select fieldSelect = (Select) field;
		List<Informacoes> lstReferencias = new ArrayList<Informacoes>();
		Informacoes infoRef;
		if (fieldSelect.isMultiple())
		{
			List<String> lstValores = fieldSelect.getSelectedValues();
			
			for (String informacoesEscolhidasNoSelectMultiple : lstValores) {
				
				infoRef = new Informacoes();
				
				infoRef.setConfiguracoesId(new Integer(field.getName()));
				infoRef.setReferenciaId(new Integer(informacoesEscolhidasNoSelectMultiple));
				
				lstInfos.add(infoRef);
				
			}
			
		}
		else { // Para o caso de não ser Multiple
		
			if (StringUtils.isNotBlank(fieldSelect.getValue()))
			{
				info = new Informacoes();
				
				info.setConfiguracoesId(Integer.valueOf(fieldSelect.getName()));
				info.setReferenciaId(new Integer(fieldSelect.getValue()));
				
				lstInfos.add(info);
			}
			
		}
	}
	
	
	




	private void montarConfigInfoPesquisa(Map<Integer, ConfigInfoPesquisaVO> mapConfgInfo, List<Informacoes> lstInfo) 
	{
		
	  for (Iterator iterator = lstInfo.iterator(); iterator.hasNext();) 
	  {
		Informacoes info = (Informacoes) iterator.next();
		

		ConfigInfoPesquisaVO confInfoVo;
		if (mapConfgInfo.containsKey(info.getConfiguracoesId() ) )
		{
			confInfoVo = mapConfgInfo.get(info.getConfiguracoesId());
			confInfoVo.setInfoDe(info);
			
		}else
		{
			confInfoVo = new ConfigInfoPesquisaVO();
			Configuracoes config = new Configuracoes();
			config.setId(info.getConfiguracoesId());
			confInfoVo.setConfig(config);
			confInfoVo.setInfoDe(info);
			mapConfgInfo.put(info.getConfiguracoesId(), confInfoVo);
		}
		
	}
	}

}

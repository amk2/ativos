package com.projtec.slingcontrol.web.click;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.LocaisGerencia;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.web.tipocampo.TipoCampoView;

@Component("configuracoesUtil")
public class ConfiguracoesUtil {
	
	
	@Autowired
	private ApplicationContext appContext;
	
	private static final String NM_INICIO = "  <B> de </B>";
	
	private static final String NM_FIM = "  <B> at&eacute;  </B>";

	@Resource(name = "locaisGerencia")
	protected LocaisGerencia locaisGerencia;

	public void exibirConfiguracoes(Layout layout,
			Map<Integer, Informacoes> mapInfo, Form form) {

		Collection<Configuracoes> lstConfiguracoes = layout.getConfiguracoes();

		if (CollectionUtils.isNotEmpty(lstConfiguracoes)) {
			
			FieldSet fieldSet = new FieldSet("configuracoes");
			
			for (Configuracoes configuracoes : lstConfiguracoes) {
	
				Field configField;
				
				if (mapInfo != null && mapInfo.containsKey(configuracoes.getId())) {
					
					Informacoes info = mapInfo.get(configuracoes.getId());
					configField = montarField(configuracoes, info);
	
				} 
				else {
					configField = montarField(configuracoes, null);
				}
				
				fieldSet.add(configField);
				
			}
			
			form.add(fieldSet);
			
		}

	}

	public void exibirConfiguracoesDependencias(Layout layout,
			Map<Integer, Informacoes> mapInfo, Form form) {
		FieldSet fieldSet = new FieldSet("configuracoes");

		Collection<Configuracoes> lstConfiguracoes = layout.getConfiguracoes();
		Field configField;
		Informacoes info;

		for (Iterator<Configuracoes> iterator = lstConfiguracoes.iterator(); iterator
				.hasNext();) {
			Configuracoes configuracoes = (Configuracoes) iterator.next();

			if (mapInfo != null && mapInfo.containsKey(configuracoes.getId())) {
				info = mapInfo.get(configuracoes.getId());
				configField = montarField(configuracoes, info);

			} else {
				configField = montarField(configuracoes, null);
			}

			fieldSet.add(configField);

		}
		Select ativoRefField = new Select("ativosRef", false);
		fieldSet.add(ativoRefField);

		form.add(fieldSet);

	}

	public void exibirConfiguracoesItemAtivos(Layout layout,
			Map<Integer, Informacoes> mapInfo, Form form) {
		FieldSet fieldSet = new FieldSet("configuracoes");

		Collection<Configuracoes> lstConfiguracoes = layout.getConfiguracoes();
		Field configField;
		Informacoes info;

		for (Iterator<Configuracoes> iterator = lstConfiguracoes.iterator(); iterator
				.hasNext();) {
			Configuracoes configuracoes = (Configuracoes) iterator.next();

			if (mapInfo != null && mapInfo.containsKey(configuracoes.getId())) {
				info = mapInfo.get(configuracoes.getId());
				configField = montarField(configuracoes, info);

			} else {
				configField = montarField(configuracoes, null);
			}

			fieldSet.add(configField);

		}
		Select ativoRefField = new Select("localOrigem", false);
		ativoRefField.add(new Option("", "-- Selecione --"));
		ativoRefField.addAll(locaisGerencia.obterTodos());
		fieldSet.add(ativoRefField);

		form.add(fieldSet);

	}
	
	
	//Exibir os campos para pesquisa
	public void exibirConfiguracoesPesquisa(Layout layout, Form form) {
		FieldSet fieldSet = new FieldSet("configuracoes");

		Collection<Configuracoes> lstConfiguracoes = layout.getConfiguracoes();
		//Informacoes info;

		for (Iterator<Configuracoes> iterator = lstConfiguracoes.iterator(); iterator
				.hasNext();) {
			
			Configuracoes configuracoes = (Configuracoes) iterator.next();

			Field configField;
			
			
			
			//if for pesquisavel 
			if (configuracoes.getTipoCampo().equals(TipoCampo.DATA) && configuracoes.getPesquisa().equals(Configuracoes.PESQ_SIM))
			{
				configField = montarField(configuracoes, null);
				configField.setName(configField.getName() + "_DE");
				configField.setId(configField.getId() + "_DE");
				configField.setLabel(  configField.getLabel()  + NM_INICIO);
				fieldSet.add(configField);
				
				configField = montarField(configuracoes, null);
				configField.setLabel(NM_FIM);
				configField.setName(configField.getName() + "_ATE");	
				configField.setId(configField.getId() + "_ATE");
				fieldSet.add(configField);
			} else if ( configuracoes.getTipoCampo().equals(TipoCampo.NUMERO_INT) && configuracoes.getPesquisa().equals(Configuracoes.PESQ_SIM))
			{
				configField = montarField(configuracoes, null);
				configField.setLabel(configField.getLabel()  + NM_INICIO);
				configField.setName(configField.getName() + "_DE");
				configField.setId(configField.getId() + "_DE");
				fieldSet.add(configField);
				configField = montarField(configuracoes, null);
				configField.setLabel( NM_FIM );
				configField.setName(configField.getName() + "_ATE");	
				configField.setId(configField.getId() + "_ATE");
				fieldSet.add(configField);
				
			}  else if ( configuracoes.getTipoCampo().equals(TipoCampo.NUMERO_DEC) && configuracoes.getPesquisa().equals(Configuracoes.PESQ_SIM))
			{
				configField = montarField(configuracoes, null);
				configField.setLabel( configField.getLabel() + NM_INICIO);
				configField.setName(configField.getName() + "_DE");
				configField.setId(configField.getId() + "_DE");
				fieldSet.add(configField);
				configField = montarField(configuracoes, null);
				configField.setLabel(NM_FIM);
				configField.setName(configField.getName() + "_ATE");	
				configField.setId(configField.getId() + "_ATE");
				fieldSet.add(configField);
				
			}else if (configuracoes.getPesquisa().equals(Configuracoes.PESQ_SIM) )
			{
				configField = montarField(configuracoes, null);
				fieldSet.add(configField);
			}
			
			
			
			
		}
		form.add(fieldSet);

	}

	private Field montarField(Configuracoes configuracoes, Informacoes info) {
		Field configField = null;

		TipoCampoView tpCampo = (TipoCampoView) appContext.getBean(String
				.valueOf(configuracoes.getTipoCampo().getId()));
		configField = tpCampo.montarField(configuracoes, info);

		return configField;

	}

}

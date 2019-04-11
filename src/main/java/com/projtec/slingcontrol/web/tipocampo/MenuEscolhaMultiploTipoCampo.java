package com.projtec.slingcontrol.web.tipocampo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.click.control.Field;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.persistencia.ConfiguracoesDAO;

@Component("9")
public class MenuEscolhaMultiploTipoCampo implements TipoCampoView
{

	//	private final  Logger logger  = LoggerFactory.getLogger(MenuEscolhaMultiploTipoCampo.class);
	
	@Resource(name = "ativosGerencia")
	protected AtivosGerencia ativosGerencia;
	
	@Autowired
	private ConfiguracoesDAO configuracoesDAO;
	
	@Override
	public Field montarField(Configuracoes configuracoes, Informacoes info)
	{
		Select selectField = new Select(String.valueOf(configuracoes.getId()));
		selectField.setLabel(configuracoes.getNomecampo());
	    if (configuracoes.getObrigatoriedade().equals("S"))
	    {
	    	selectField.setLabel(selectField.getLabel() + " <span class='required'>*</span>");
	    	selectField.setAttribute("class", "validate[required]");
	    }
		//selectField.setStyle("width", "" + configuracoes.getTamanho() + "pol");
		selectField.setStyle("width", "5cm");
		selectField.setStyle("height", "1.5cm");
		selectField.setMultiple(true);
		
		
		//valores do option - Configuracoes filho; 
		Configuracoes config;
		if (configuracoes.getFilhosConfig()!=null)
		{   Collection<Configuracoes> lstOpcoes = configuracoes.getFilhosConfig();
			for (Iterator<Configuracoes> iterator = lstOpcoes.iterator(); iterator.hasNext();) 
			{
				config = iterator.next();
				selectField.add(new Option(config.getId(),config.getNomecampo()));
				
			}
		    
			selectField.add(new Option(" ", " "));
			
			selectField.setSize(configuracoes.getFilhosConfig().size() + 1);
		}
		
		List<String> multipleValues = new ArrayList<String>();
		
		if (info != null) {
		
			List<Informacoes> informacoesList = new ArrayList<Informacoes>(
					ativosGerencia.obterInformacoesPelaConfiguracaoId(info.getConfiguracoesId(), info.getIdTipo()));
			
			for (Informacoes informacoesSelecionado : informacoesList) {
				multipleValues.add(String.valueOf(informacoesSelecionado.getReferenciaId()));
			}
			
		}
		
		selectField.setSelectedValues(multipleValues);
		
		
		
		/*
		logger.debug("info do campo:" + info);
		if(info!=null  && info.getLstReferencias()!=null)
		{
			Collection<Informacoes> lstSelecionados = info.getLstReferencias();
			List<String> multipleValues =new ArrayList<String>();
			Informacoes infoSelecionado;
			for (Iterator iterator = lstSelecionados.iterator(); iterator.hasNext();)
			{
				infoSelecionado = (Informacoes) iterator.next();
				logger.debug("info da seleção:" + infoSelecionado);
				multipleValues.add(String.valueOf(infoSelecionado.getConfiguracoesId()));
			}
			
			selectField.setSelectedValues(multipleValues);
		}
		*/
		
		return selectField; 
	}

	@Override
	public String formataInformacao(Configuracoes configuracoes , Informacoes info) 
	{
		
		Configuracoes configReferencia = configuracoesDAO.pesquisarId(info.getReferenciaId());
		
		if (configReferencia != null)
			return configReferencia.getNomecampo();
		else // Necessário por conta dos dados antigos no banco
			return "";
		
	}

}

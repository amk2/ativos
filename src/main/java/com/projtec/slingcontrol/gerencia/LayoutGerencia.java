package com.projtec.slingcontrol.gerencia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.modelo.TipoLayout;
import com.projtec.slingcontrol.persistencia.ConfiguracoesDAO;
import com.projtec.slingcontrol.persistencia.InformacoesAtivoDAO;
import com.projtec.slingcontrol.persistencia.InformacoesDemandaDAO;
import com.projtec.slingcontrol.persistencia.InformacoesLocaisDAO;
import com.projtec.slingcontrol.persistencia.InformacoesMovimentoAtivoDAO;
import com.projtec.slingcontrol.persistencia.LayoutDAO;
import com.projtec.slingcontrol.persistencia.TipoCampoDAO;
import com.projtec.slingcontrol.persistencia.TipoLayoutDAO;

@Service("layoutGerencia")
public class LayoutGerencia 
{
	
	@Autowired 
    private LayoutDAO layoutDAO;
	
	@Autowired 
	private TipoLayoutDAO tpLayoutDAO;
	
	@Autowired 
	private TipoCampoDAO tpCampoDAO; 
	
	@Autowired 
	private ConfiguracoesDAO configuracoesDAO; 
	
	@Autowired
	private InformacoesAtivoDAO informacoesAtivoDAO;
	
	@Autowired
	private InformacoesDemandaDAO informacoesDemandaDAO;
	
	@Autowired
	private InformacoesLocaisDAO informacoesLocaisDAO; 
	
	@Autowired
	private InformacoesMovimentoAtivoDAO informacoesMovimentoAtivoDAO; 
	
	
	@Transactional
    public Layout incluir(Layout layout) throws Exception
    {

		Layout layoutExistente = layoutDAO.pesquisar(
				layout.getNome(), layout.getTipoLayout().getId());
		
		if (layoutExistente != null)
			throw new Exception("Layout j&aacute; cadastrado com essa descri&ccedil;&atilde;o");
		
    	Layout layoutNovo = layoutDAO.incluir(layout);
    	return layoutNovo;

    }
	
	@Transactional
    public Boolean verificaSePodeExluir(Integer layoutId)
    {
		//verifica se existe alguma informacao associada a esta configuracaona tabela ativos
		if(informacoesAtivoDAO.existeRegistroLayoutId(layoutId))
			return false;

		//verifica se existe alguma informacao associada a esta configuracaona tabela demanda
		if(informacoesDemandaDAO.existeRegistroLayoutId(layoutId))
			return false;
		
		//verifica se existe alguma informacao associada a esta configuracaona tabela local
		if (informacoesLocaisDAO.existeRegistroLayoutId(layoutId))
			return false;
		//verifica se existe alguma informacao associada a esta configuracaona tabela movimentacao_demanda
		if (informacoesMovimentoAtivoDAO.existeRegistroLayoutId(layoutId))
			return false;
		
		//se nao encontrou registro em nenhuma das tabelas de informacao, pode remover.
		return true;
    }
	
	@Transactional
    public Boolean verificaSePodeAlterar(Integer layoutId)
    {
		//verifica se existe alguma informacao associada a esta configuracaona tabela ativos
		if(informacoesAtivoDAO.existeRegistroLayoutId(layoutId))
			return false;

		//verifica se existe alguma informacao associada a esta configuracaona tabela demanda
		if(informacoesDemandaDAO.existeRegistroLayoutId(layoutId))
			return false;
		
		//verifica se existe alguma informacao associada a esta configuracaona tabela local
		if (informacoesLocaisDAO.existeRegistroLayoutId(layoutId))
			return false;
		//verifica se existe alguma informacao associada a esta configuracaona tabela movimentacao_demanda
		if (informacoesMovimentoAtivoDAO.existeRegistroLayoutId(layoutId))
			return false;
		
		//se nao encontrou registro em nenhuma das tabelas de informacao, pode remover.
		return true;
    }
	
	@Transactional
    public Boolean excluir(Integer idLayout)
    {
	
		List<Configuracoes> configuracoesList = new ArrayList<Configuracoes>();
		configuracoesList.addAll(configuracoesDAO.obterConfiguracoesLayout(idLayout));
		
		for (Configuracoes config : configuracoesList) {
			configuracoesDAO.excluir(config.getId());
		}

		return layoutDAO.excluir(idLayout);
		
    }
	
	@Transactional
    public Boolean alterar(Layout layout)
    {
				
		return layoutDAO.alterar(layout);
    }
    
    
    public Layout pesquisarId(Integer id)
    {
		Layout layout;
		Collection<Configuracoes> configuracoes;
		Configuracoes config;

		layout = layoutDAO.pesquisarPorId(id);

		configuracoes = configuracoesDAO.obterConfiguracoesLayout(id);
		
		for (Iterator<Configuracoes> iterator = configuracoes.iterator(); iterator.hasNext();) {
			config = iterator.next();
			config.setFilhosConfig(configuracoesDAO.obterConfiguracoesFilho(config.getId()));
		}

		layout.setConfiguracoes(configuracoes);

		return layout;
    }
    
    public Collection<Layout> pesquisar(Layout layout)
    {
    	return layoutDAO.pesquisar(layout);
    }
    
    @Transactional(readOnly=true)
   public Collection<Layout> obterTodos()
   {
   	return   layoutDAO.obterTodos();
   }
    
    @Transactional(readOnly=true)
    public Collection<Configuracoes>  configuracoesLayout(Integer idLayout)
    {
    	Layout layout = layoutDAO.pesquisarPorId(idLayout);
    	Collection<Configuracoes> lstConfs =  configuracoesDAO.obterConfiguracoesLayout(idLayout);
    	for (Iterator iterator = lstConfs.iterator(); iterator.hasNext();)
		{
			Configuracoes configuracoes = (Configuracoes) iterator.next();
			
			configuracoes.setLayout(layout);
			
		}
    	return   lstConfs;
    }
   
    
  
    public Collection<TipoLayout> obterTiposLayout()
    {
    	return tpLayoutDAO.obterTodos();
    }
    
    
    public Collection<TipoCampo> obterTiposCampos()
    {
    	return  tpCampoDAO.obterTodos();
    }

    @Transactional
	public Boolean excluirConfiguracao(Integer id)
	{
    	configuracoesDAO.excluirConfiguracoesFilho(id);
		return configuracoesDAO.excluir(id);		
	}

	public Configuracoes pesquisarConfiguracao(Integer id)
	{
		Configuracoes conf = configuracoesDAO.pesquisarId(id);
		Layout l = layoutDAO.pesquisarPorId(conf.getIdLayout());		
		Collection<Configuracoes> lstConfigs = configuracoesDAO.obterConfiguracoesFilho(conf.getId());
		conf.setFilhosConfig(lstConfigs);
		conf.setLayout(l);
		return conf;
	}

	@Transactional
	public Configuracoes gravarConfiguracao(Configuracoes config)
	{
		Collection<Configuracoes> filhos = config.getFilhosConfig();		
		Configuracoes configNovo = configuracoesDAO.incluir(config);
		
		if (filhos!=null)
		{
			for (Iterator iterator = filhos.iterator(); iterator.hasNext();)
			{
				Configuracoes configuracoes = (Configuracoes) iterator.next();
				configuracoes.setIdLayout(configNovo.getIdLayout());
				configuracoes.setIdRef(configNovo.getId());
				configuracoes.setIdLayout(null);
				configuracoes.setPesquisa(config.getPesquisa());
				configuracoes.setObrigatoriedade(config.getObrigatoriedade());
				configuracoes.setOrdem(1);
				configuracoes.setTamanho(100);
				configuracoes.setTipoCampo(configNovo.getTipoCampo());
				configuracoesDAO.incluir(configuracoes);				
			}
			
		}
		
		return configNovo;
		
	}
	
	@Transactional
	public void alteraConfiguracao(Configuracoes config)
	{
		if (config.getFilhosConfig()!=null)
		{
			
			configuracoesDAO.excluirConfiguracoesFilho(config.getId());
			Collection<Configuracoes> lstConfigsfilho =config.getFilhosConfig();
			for (Iterator iterator = lstConfigsfilho.iterator(); iterator.hasNext();)
			{
				Configuracoes configuracoes = (Configuracoes) iterator.next();
				configuracoes.setIdRef(config.getId());
				configuracoes.setIdLayout(null);
				configuracoes.setPesquisa(config.getPesquisa());
				configuracoes.setObrigatoriedade(config.getObrigatoriedade());
				configuracoes.setOrdem(1);
				configuracoes.setTamanho(100);
				configuracoes.setTipoCampo(config.getTipoCampo());
				configuracoesDAO.incluir(configuracoes);		
				
			}
		}
		configuracoesDAO.alterar(config);		
	}
	
	public Collection<Configuracoes> pesquisar(Configuracoes conf)
	{
		Collection<Configuracoes> lstConfiguracoes = configuracoesDAO.pesquisar(conf); 
		Map<Integer, Layout> mapLayout = new HashMap<Integer, Layout>();
		
		for (Iterator iterator = lstConfiguracoes.iterator(); iterator.hasNext();)
		{
			Configuracoes config = (Configuracoes) iterator.next();
			if (mapLayout.containsKey(config.getIdLayout()))
			{
				config.setLayout(mapLayout.get(config.getIdLayout()));
			}else
			{
				Layout layout = layoutDAO.pesquisarPorId(config.getIdLayout());
				if(layout!=null)
				{
					config.setLayout(layout);
					mapLayout.put(layout.getId(), layout);
				}			
				
			}
			
		}		
		
		return lstConfiguracoes; 
	}
	
	public Collection<Layout> obterTodosNomesLayouts(String nomeLayout) {
		
		return layoutDAO.obterTodosFiltroLayout(nomeLayout);

	}
	
	public Collection<Layout>  obterTodosComID(Integer id)
	{
		return layoutDAO.obterTodosComID(id);
	}
	
	

}

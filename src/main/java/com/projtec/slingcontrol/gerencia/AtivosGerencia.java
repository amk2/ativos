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

import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.AtivosNo;
import com.projtec.slingcontrol.modelo.ConfigInfoPesquisaVO;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.persistencia.AtivosDAO;
import com.projtec.slingcontrol.persistencia.ConfiguracoesDAO;
import com.projtec.slingcontrol.persistencia.InformacoesAtivoDAO;
import com.projtec.slingcontrol.persistencia.ItensAtivosDAO;
import com.projtec.slingcontrol.persistencia.LayoutDAO;
import com.projtec.slingcontrol.persistencia.LocaisDAO;

@Service("ativosGerencia")
public class AtivosGerencia extends InformacoesTemplate {
	@Autowired
	private AtivosDAO ativosDAO;

	@Autowired
	private LayoutDAO layoutDAO;

	@Autowired
	private InformacoesAtivoDAO informacoesAtivoDAO;

	@Autowired
	private ConfiguracoesDAO configuracoesDAO;
	
	@Autowired
	private ItensAtivosDAO itensAtivosDAO;
	
	@Autowired
	private LocaisDAO locaisDAO;
	

	public Collection<Layout> obterTodosLayouts() {
		return layoutDAO.obterTodos();
	}

	public Collection<Layout> obterTodosNomesLayouts(String nomeLayout) {

		return layoutDAO.obterTodosFiltroLayout(nomeLayout);

	}

	@Transactional
	public Ativos incluir(Ativos ativos) {
		Ativos novoAtivo;
		Collection<Informacoes> infosNova = new ArrayList<Informacoes>();

		novoAtivo = ativosDAO.incluir(ativos);

		Collection<Informacoes> lstInformacoes = ativos.getInformacoes();

		infosNova = incluirListaInformacoes(lstInformacoes, novoAtivo.getId());

		novoAtivo.setInformacoes(infosNova);

		if (ativos.getDependencias() != null
				&& ativos.getDependencias().size() > 0) {
			ativosDAO.incluirDep(novoAtivo.getId(), ativos.getDependencias());
		}

		return novoAtivo;
	}

	@Transactional
	public Boolean excluir(Integer idAtivo) {
		informacoesAtivoDAO.excluirAtivoID(idAtivo);
		ativosDAO.excluirDep(idAtivo);
		return ativosDAO.excluir(idAtivo);
	}

	@Transactional
	public Boolean alterar(Ativos objAtivo) {
		informacoesAtivoDAO.excluirAtivoID(objAtivo.getId());
		Collection<Informacoes> lstInformacoes = objAtivo.getInformacoes();
		incluirListaInformacoes(lstInformacoes, objAtivo.getId());
		ativosDAO.excluirDep(objAtivo.getId());
		ativosDAO.incluirDep(objAtivo.getId(), objAtivo.getDependencias());
		return ativosDAO.alterar(objAtivo);
	}

	public Ativos pesquisarId(Integer id) {

		Ativos objAtivos;
		Collection<Informacoes> informacoesAtivo;
		Map<Integer, Informacoes> mapInformacoesAtivo = new HashMap<Integer, Informacoes>();
		Informacoes infoAtivo;
		List<Informacoes> lstinfoAtivoRef;

		informacoesAtivo = informacoesAtivoDAO.obterInformacoesAtivos(id);
		for (Iterator<Informacoes> iterator = informacoesAtivo.iterator(); iterator
				.hasNext();) {
			
			infoAtivo = iterator.next();
			mapInformacoesAtivo.put(infoAtivo.getConfiguracoesId(), infoAtivo);

		}
		objAtivos = ativosDAO.pesquisarPorId(id);
		objAtivos.setInformacoes(informacoesAtivo);
		objAtivos.setMapInformacoes(mapInformacoesAtivo);

		Layout l = layoutDAO.pesquisarPorId(objAtivos.getLayout().getId());		
		Collection<Configuracoes> lstConfgs = configuracoesDAO.obterConfiguracoesLayout(l.getId());
		l.setConfiguracoes(lstConfgs);
		objAtivos.setLayout(l);
		

		List<Integer> ativosDep = ativosDAO.pesquisarPaiPorIdFilho(id);
		objAtivos.setDependencias(ativosDep);

		return objAtivos;
	}

	public List<Ativos> obterTodos() {
		List<Ativos> lstAtivos = new ArrayList<Ativos>();
		Ativos ativos;
		Layout layout;
		List<Integer> lstAtivosDep;

		lstAtivos = ativosDAO.obterTodos();
		for (int i = 0; i < lstAtivos.size(); i++) {
			ativos = lstAtivos.get(i);
			lstAtivosDep = ativosDAO.pesquisarPaiPorIdFilho(ativos.getId());
			ativos.setDependencias(lstAtivosDep);
			layout = layoutDAO.pesquisarPorId(ativos.getLayout().getId());
			layout.setConfiguracoes(configuracoesDAO.obterConfiguracoesLayout(layout.getId()));
			ativos.setLayout(layout);
			ativos.setInformacoes(obterInformacoes(ativos));
		}
		return lstAtivos;
	}

	public List<Ativos> pesquisar(Ativos ativo) {
		List<Ativos> lstAtivos = new ArrayList<Ativos>();
		Ativos ativos;
		List<Integer> lstAtivosDep;
		Layout layout;

		lstAtivos = ativosDAO.pesquisar(ativo);
		for (int i = 0; i < lstAtivos.size(); i++) {
			ativos = lstAtivos.get(i);
			lstAtivosDep = ativosDAO.pesquisarPaiPorIdFilho(ativos.getId());
			ativos.setDependencias(lstAtivosDep);
			layout = layoutDAO.pesquisarPorId(ativos.getLayout().getId());
			layout.setConfiguracoes(configuracoesDAO.obterConfiguracoesLayout(layout.getId()));
			ativos.setLayout(layout);
			ativos.setInformacoes(obterInformacoes(ativos));
			
		}
		return lstAtivos;

	}

	private Collection<Informacoes> obterInformacoes(Ativos ativos) 
	{
		Collection<Informacoes> lstInformacoes = informacoesAtivoDAO.obterInformacoesAtivos(ativos.getId());
		return  lstInformacoes;
	}


	/**
	 * @param ativo
	 * @return
	 */
	public List<ItensAtivos> pesquisarItensComLocalID(Ativos ativo) 
	{
		List<Ativos> lstAtivos;
		List<ItensAtivos> listItensTemp = new ArrayList<ItensAtivos>();
		List<ItensAtivos> listItensAtivos = new ArrayList<ItensAtivos>();		
		lstAtivos = pesquisar(ativo);
		
		for (Iterator iterator = lstAtivos.iterator(); iterator.hasNext();) 
		{
			Ativos ativos = (Ativos) iterator.next();
			listItensTemp = itensAtivosDAO.obterItensdoAtivoComLocalID(ativos.getId());
			if(listItensTemp!=null && listItensTemp.size() > 0 )
			{
				montarItens(listItensTemp,ativos);
				listItensAtivos.addAll(listItensTemp);
			}
			
		}
		
		return listItensAtivos;
	}
	
	
	/**
	 * @param ativo
	 * @return
	 */
	public List<ItensAtivos> pesquisarItens(Ativos ativo) 
	{
		List<Ativos> lstAtivos;
		List<ItensAtivos> listItensTemp = new ArrayList<ItensAtivos>();
		List<ItensAtivos> listItensAtivos = new ArrayList<ItensAtivos>();		
		lstAtivos = pesquisar(ativo);
		
		for (Iterator iterator = lstAtivos.iterator(); iterator.hasNext();) 
		{
			Ativos ativos = (Ativos) iterator.next();
			listItensTemp = itensAtivosDAO.obterItensdoAtivo(ativos.getId());
			if(listItensTemp!=null && listItensTemp.size() > 0 )
			{
				montarItens(listItensTemp,ativos);
				listItensAtivos.addAll(listItensTemp);
			}
			
		}
		
		return listItensAtivos;
	}
	
	/**
	 * Agrupar o retorno por layout
	 * @param ativo
	 * @return
	 */
	public List<Ativos> pesquisarAtivosEstoque(Integer idEstoque , String codIdentificacao, Ativos atv ) 
	{
		List<Ativos> lstAtivos = new ArrayList<Ativos>();
		List<Ativos> lstAtivosCompleto = new ArrayList<Ativos>();
		Ativos ativos;
		Ativos novoAtivo;
		
		lstAtivos = ativosDAO.pesquisarAtivosEstoque(idEstoque , codIdentificacao, atv);
		
		for (int i = 0; i < lstAtivos.size(); i++) {
			ativos = lstAtivos.get(i);	
			novoAtivo = pesquisarId(ativos.getId());
			novoAtivo.setQtdItens(ativos.getQtdItens());
			novoAtivo.setLocalID(ativos.getLocalID());
			novoAtivo.setItens_ativo_identificacaodoSQL(ativos.getItens_ativo_identificacaodoSQL());
			lstAtivosCompleto.add(novoAtivo);			
		}
		return lstAtivosCompleto;
		
	}
	public List<Ativos> pesquisar(String criterio) 
	{
		List<Ativos> lstAtivos = new ArrayList<Ativos>();
		Ativos ativos;
		List<Integer> lstAtivosDep;
		Layout layout;

		lstAtivos = ativosDAO.pesquisar(criterio);
		for (int i = 0; i < lstAtivos.size(); i++) {
			ativos = lstAtivos.get(i);
			lstAtivosDep = ativosDAO.pesquisarPaiPorIdFilho(ativos.getId());
			ativos.setDependencias(lstAtivosDep);
			layout = layoutDAO.pesquisarPorId(ativos.getLayout().getId());
			ativos.setLayout(layout);
		}
		return lstAtivos;
	}

	public List<AtivosNo> getFilhos(Integer idPai, List<AtivosNo> lstPaiFilho) {

		ativosDAO.pesquisarFilhoPorIdPai(idPai, lstPaiFilho);
		return lstPaiFilho;
	}

	public List<Informacoes> obterInformacoesPelaConfiguracaoId(Integer configuracaoId, Integer ativoId) {
		return informacoesAtivoDAO.obterInformacoesPelaConfiguracaoId(configuracaoId, ativoId);
	}
	
	@Override
	public Informacoes incluirInfomacoes(Informacoes info) {
		return informacoesAtivoDAO.incluir(info);
	}
	
	
	/**
	 * @param ativo
	 * @return List<ItensAtivos>
	 */
	public List<ItensAtivos> pesquisarItens(Integer  idLayout , String identificacao, List<ConfigInfoPesquisaVO>   lstConfigisValor) 
	{
		List<ItensAtivos> listItensAtivos = new ArrayList<ItensAtivos>();	
		
		for (Iterator<ConfigInfoPesquisaVO> iterator = lstConfigisValor.iterator(); iterator.hasNext();) 
		{
			ConfigInfoPesquisaVO config = iterator.next();
			config.setConfig(configuracoesDAO.pesquisarId(config.getConfig().getId()));
			
		}
	
		listItensAtivos= itensAtivosDAO.pesquisarItensAtivos(idLayout , identificacao, lstConfigisValor);
		for (Iterator iterator = listItensAtivos.iterator(); iterator.hasNext();) {
			ItensAtivos itensAtivos = (ItensAtivos) iterator.next();
			montarItensPesquisa(itensAtivos);		
		}
		
	
		return listItensAtivos;
	}
	
	private void montarItens(List<ItensAtivos> listItensAtivos,Ativos ativo) 
	{
		
		for (Iterator iterator = listItensAtivos.iterator(); iterator.hasNext();) 
		{
			ItensAtivos itensAtivos = (ItensAtivos) iterator.next();
			Local local = locaisDAO.pesquisarPorId(itensAtivos.getLocal().getId());
			itensAtivos.setLocal(local);
			itensAtivos.setAtivo(ativo);
			
		}
		
		
	}
	
	
	
	private void montarItensPesquisa(ItensAtivos itensAtivos) 
	{		
			Local local = locaisDAO.pesquisarPorId(itensAtivos.getLocal().getId());
			itensAtivos.setLocal(local);
			Ativos ativos = pesquisarId(itensAtivos.getAtivo().getId());
			itensAtivos.setAtivo(ativos);			
	}

}

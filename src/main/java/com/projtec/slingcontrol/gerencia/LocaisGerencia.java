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

import com.projtec.slingcontrol.modelo.Estoque;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.persistencia.EstoqueDAO;
import com.projtec.slingcontrol.persistencia.InformacoesLocaisDAO;
import com.projtec.slingcontrol.persistencia.LayoutDAO;
import com.projtec.slingcontrol.persistencia.LocaisDAO;

@Service("locaisGerencia")
public class LocaisGerencia extends InformacoesTemplate
{

	@Autowired
	private LocaisDAO locaisDAO;

	@Autowired
	private LayoutDAO layoutDAO;

	@Autowired
	private EstoqueDAO estoqueDAO;

	@Autowired
	private InformacoesLocaisDAO informacoesLocaisDAO;

	@Transactional
	public Local incluir(Local local) throws Exception
	{	
		
		Local localExistente = locaisDAO.pesquisarPelaDescricao(local.getNome());
		
		if (localExistente != null)
			throw new Exception("Local j&aacute; cadastrado com essa nome");
		
		Local localNovo = locaisDAO.incluir(local);		
	    
		Collection<Informacoes> lstInformacoes = local.getInfos();		
		Collection<Informacoes> infosNova = incluirListaInformacoes(lstInformacoes, localNovo.getId());         
		localNovo.setInfos(infosNova);
		
		return localNovo;
		
	}

	@Transactional
	public Boolean excluir(Integer idLocal)
	{
		informacoesLocaisDAO.excluirPorLocaisID(idLocal);

		return locaisDAO.excluir(idLocal);

	}

	@Transactional
	public Boolean alterar(Local objLocal)
	{

		// excluir as informacoes dos locais ... verificar se layout mudou ?
		informacoesLocaisDAO.excluirPorLocaisID(objLocal.getId());

		Collection<Informacoes> lstInformacoes = objLocal.getInfos();
		incluirListaInformacoes(lstInformacoes, objLocal.getId());   

		return locaisDAO.alterar(objLocal);
	}

	public Local perquisarID(Integer idLocal)
	{
		Local objLocais;
		Collection<Informacoes> informacoesLocais;
		Map<Integer, Informacoes> mapInformacoesLocais = new HashMap<Integer, Informacoes>();
		Informacoes infoLocais;
		
		informacoesLocais = informacoesLocaisDAO
				.obterInformacoesLocais(idLocal);
		for (Iterator<Informacoes> iterator = informacoesLocais.iterator(); iterator
				.hasNext();)
		{
			
			infoLocais = iterator.next();
			mapInformacoesLocais.put(infoLocais.getConfiguracoesId(),infoLocais);
			
		}

		objLocais = locaisDAO.pesquisarPorId(idLocal);
		if (objLocais != null)
		{
			objLocais.setInfos(informacoesLocais);
			objLocais.setMapInformacoesLocais((mapInformacoesLocais));
			Layout l = layoutDAO.pesquisarPorId(objLocais.getLayout().getId());
			objLocais.setLayout(l);
			Estoque estoque = estoqueDAO.pesquisarPorId(objLocais.getEstoque().getId());
			objLocais.setEstoque(estoque);
		}

		return objLocais;

	}
	
	public Collection<Layout> obterTodosLayouts()
	{
		return layoutDAO.obterTodos();
	}
	
	public Collection<Estoque> obterEstoques() {
		return estoqueDAO.obterTodos();
	}

	public Collection<Layout> obterTodosLayouts(String nomeLayout)
	{
		return layoutDAO.obterTodosFiltroLayout(nomeLayout);
	}

	public List<Local> pesquisar(Local objLocal)
	{

		List<Local> lstLocal = new ArrayList<Local>();
		Local locais;

		Layout layout;

		lstLocal = (List<Local>) locaisDAO.pesquisar(objLocal);

		for (int i = 0; i < lstLocal.size(); i++)
		{
			locais = lstLocal.get(i);

			locais.getNome();
			locais.getDescricao();
			layout = layoutDAO.pesquisarPorId(locais.getLayout().getId());
			locais.setLayout(layout);
			locais.setInfos(informacoesLocaisDAO.obterInformacoesLocais(locais
					.getId()));
		}

		return lstLocal;

	}

	public List<Local> obterTodos()
	{
		List<Local> lstLocal = new ArrayList<Local>();
		Local locais;

		Layout layout;

		lstLocal = (List<Local>) locaisDAO.obterTodos();

		for (int i = 0; i < lstLocal.size(); i++)
		{
			locais = lstLocal.get(i);

			layout = layoutDAO.pesquisarPorId(locais.getLayout().getId());
			locais.setLayout(layout);
		}

		return lstLocal;
	}
	
	public List<Local> obterLocaisPorEstoque(String value) {
		List<Local> lstLocal = new ArrayList<Local>();
		Local locais;
		Layout layout;
		Estoque estoque = new Estoque();
		if (value.length() == 0) {
			value = "0";
		}
		estoque.setId(Integer.parseInt(value));
		lstLocal = (List<Local>) locaisDAO.obterLocaisPorEstoque(estoque);
		for (int i = 0; i < lstLocal.size(); i++) {
			locais = lstLocal.get(i);
			layout = layoutDAO.pesquisarPorId(locais.getLayout().getId());
			locais.setLayout(layout);
		}
		return lstLocal;
	}

	@Override
	public Informacoes incluirInfomacoes(Informacoes info)
	{
		
		return informacoesLocaisDAO.incluir(info);
	}

}

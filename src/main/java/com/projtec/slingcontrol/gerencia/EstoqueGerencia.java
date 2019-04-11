package com.projtec.slingcontrol.gerencia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projtec.slingcontrol.modelo.Estoque;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.persistencia.EstoqueDAO;
import com.projtec.slingcontrol.persistencia.InformacoesEstoqueDAO;
import com.projtec.slingcontrol.persistencia.LayoutDAO;
import com.projtec.slingcontrol.persistencia.LocaisDAO;

@Service("estoqueGerencia")
public class EstoqueGerencia extends InformacoesTemplate {

	@Autowired
	private LayoutDAO layoutDAO;

	@Autowired
	private EstoqueDAO estoqueDAO;

	@Autowired
	private LocaisDAO locaisDAO;

	@Autowired
	private InformacoesEstoqueDAO informacoesEstoqueDAO;

	@Transactional
	public Estoque gravar(Estoque estoque) {
		Estoque estoqueNovo;
		Collection<Informacoes> infosNova = new ArrayList<Informacoes>();
		estoqueNovo = estoqueDAO.adicionar(estoque);
		Collection<Informacoes> lstInformacoes = estoque.getInfos();
		infosNova = incluirListaInformacoes(lstInformacoes, estoqueNovo.getId());
		estoqueNovo.setInfos(infosNova);
		return estoqueNovo;
	}

	@Transactional
	public Boolean alterar(Estoque estoque) {
		informacoesEstoqueDAO.excluirPorEstoqueID(estoque.getId());
		Collection<Informacoes> lstInformacoes = estoque.getInfos();
		incluirListaInformacoes(lstInformacoes, estoque.getId());   
		return estoqueDAO.alterar(estoque);
	}

	@Transactional
	public Boolean excluir(Integer idEstoque) {
		return estoqueDAO.excluir(idEstoque);
	}

	@Transactional(readOnly = true)
	public Collection<Estoque> pesquisar(Estoque estoque) {
		return estoqueDAO.pesquisar(estoque);
	}

	@Transactional(readOnly = true)
	public Estoque pesquisarId(Integer id) {
		// return estoqueDAO.pesquisarPorId(id);

		Estoque estoque;
		Collection<Informacoes> informacoesEstoque;
		Map<Integer, Informacoes> mapInformacoesEstoque = new HashMap<Integer, Informacoes>();
		Informacoes infoEstoque;
		informacoesEstoque = informacoesEstoqueDAO.obterInformacoesEstoque(id);
		for (Iterator<Informacoes> iterator = informacoesEstoque.iterator(); iterator
				.hasNext();) {
			
			infoEstoque = iterator.next();
			mapInformacoesEstoque.put(infoEstoque.getConfiguracoesId(), infoEstoque);
			
		}
		estoque = estoqueDAO.pesquisarPorId(id);
		if (estoque != null) {
			estoque.setInfos(informacoesEstoque);
			estoque.setMapInformacoesLocais((mapInformacoesEstoque));
		}
		return estoque;
	}

	@Transactional(readOnly = true)
	public Collection<Estoque> obterTodos() {
		return estoqueDAO.obterTodos();
	}

	public Collection<Local> obterTodosLocais() {
		return locaisDAO.obterTodosNomes();
	}

	public Collection<ItensAtivos> pesquisarItens() {
		Collection<ItensAtivos> itensAtivos = new ArrayList<ItensAtivos>();
		ItensAtivos itensAtivo = new ItensAtivos();
		itensAtivo.setIdentificacao("xxxxxx");
		return itensAtivos;
	}

	public Collection<Layout> obterTodosLayouts(String nomeLayout) {
		return layoutDAO.obterTodosFiltroLayout(nomeLayout);
	}

	public Collection<Informacoes> incluirListaInformacoes(
			Collection<Informacoes> lstInformacoes, Integer idTipo) {
		
		Collection<Informacoes> infosNova = new ArrayList<Informacoes>();
		
		for (Iterator<Informacoes> iterator = lstInformacoes.iterator(); iterator
				.hasNext();) {

			Informacoes informacoes = iterator.next();
			informacoes.setIdTipo(idTipo);
			Informacoes novaInfo = incluirInfomacoes(informacoes);

			infosNova.add(novaInfo);

		}
		
		return infosNova;
		
	}

	@Override
	public Informacoes incluirInfomacoes(Informacoes info) {
		return informacoesEstoqueDAO.incluir(info);
	}

}

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
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.DemandasItemAtivo;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.modelo.StatusDemanda;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.persistencia.ConfiguracoesDAO;
import com.projtec.slingcontrol.persistencia.DemandaDAO;
import com.projtec.slingcontrol.persistencia.DemandasItemAtivoDAO;
import com.projtec.slingcontrol.persistencia.InformacoesDemandaDAO;
import com.projtec.slingcontrol.persistencia.ItensAtivosDAO;
import com.projtec.slingcontrol.persistencia.LayoutDAO;
import com.projtec.slingcontrol.persistencia.LocaisDAO;
import com.projtec.slingcontrol.persistencia.StatusDemandaDAO;
import com.projtec.slingcontrol.persistencia.TipoDemandaDAO;

@Service("demandaGerencia")
public class DemandaGerencia extends InformacoesTemplate
{
	
	
	@Autowired
	private LayoutDAO layoutDAO ;
	@Autowired
	private DemandaDAO demandaDAO;
	
	@Autowired
	private TipoDemandaDAO tipodemandaDAO;
	
	@Autowired
	private InformacoesDemandaDAO informacoesDemandaDAO;
	
	
	@Autowired
	private StatusDemandaDAO statusDemandaDAO;
	
	@Autowired
	private ItensAtivosDAO itensAtivosDAO;
	
	@Autowired
	private DemandasItemAtivoDAO demandasItemAtivoDAO;
	
	
	@Autowired
	private ConfiguracoesDAO configuracoesDAO;
	
	@Autowired
	private LocaisDAO locaisDAO;
	
	
	
	
	public Collection<TipoDemanda> obterTodosTipoDemanda() {

		return tipodemandaDAO.obterTodos();

	}
	
	public Collection<StatusDemanda> obterTodosStatusDemanda() {

		return statusDemandaDAO.obterTodos();

	}

	public Collection<Layout> obterTodosNomesLayouts(String nomeLayout) {
		
		return layoutDAO.obterTodosFiltroLayout(nomeLayout);

	}
	
	public Collection<Layout> obterTodosLayouts()
	{
		return layoutDAO.obterTodos();
	}
	
	@Transactional
    public Demandas incluir(Demandas demandas)
	{
		Demandas novodemanda;
		Collection<Informacoes> infosNova = new ArrayList<Informacoes>();	

		novodemanda = demandaDAO.incluir(demandas);

		Collection<Informacoes> lstInformacoesDemanda = demandas.getInformacoesDemanda();
		
		infosNova = incluirListaInformacoes(lstInformacoesDemanda, novodemanda.getId());         
		novodemanda.setInformacoesDemanda(infosNova);
		
		salvarDemandasItemAtivo(demandas , novodemanda.getId());
		
		return novodemanda;
		
	}

	
	
	
	 @Transactional
	 public Boolean excluir(Integer idDemanda)
	 {
		 informacoesDemandaDAO.excluirDemandaID(idDemanda);
		 demandasItemAtivoDAO.excluirItensDemanda(idDemanda);
		 
		 return demandaDAO.excluir(idDemanda);
	 }
	 
	 @Transactional
	 public Boolean alterar(Demandas objDemanda)
	 {		
		 informacoesDemandaDAO.excluirDemandaID(objDemanda.getId());
		 demandasItemAtivoDAO.excluirItensDemanda(objDemanda.getId());
		 
		 Collection<Informacoes> lstInformacoes = objDemanda.getInformacoesDemanda();
		 incluirListaInformacoes(lstInformacoes, objDemanda.getId());
		 
		 salvarDemandasItemAtivo(objDemanda , objDemanda.getId());
			
		 return demandaDAO.alterar(objDemanda);
	 }
	 
	
	 public Demandas pesquisarID(Integer idDemanda)
	 {
		    Demandas objDemanda;
			Collection<Informacoes> informacoesDemanda;
			
			Map<Integer, Informacoes> mapInformacoesDemandas =new HashMap<Integer, Informacoes>();
			Informacoes infoDemanda;
			informacoesDemanda = informacoesDemandaDAO.obterInformacoesDemanda(idDemanda);
			for (Iterator<Informacoes> iterator = informacoesDemanda.iterator(); iterator.hasNext();) 
			{
				infoDemanda = iterator.next();
				mapInformacoesDemandas.put(infoDemanda.getConfiguracoesId(),infoDemanda);
			}
		
			objDemanda = demandaDAO.pesquisarPorId(idDemanda);
			if (objDemanda!=null)
			{
				TipoDemanda tpDemanda = tipodemandaDAO.pesquisarPorId(objDemanda.getTipoDemanda().getId());
				objDemanda.setTipoDemanda(tpDemanda);
			    objDemanda.setInformacoesDemanda(informacoesDemanda);
			    objDemanda.setMapInformacoesDemanda(mapInformacoesDemandas);
			    
			    Layout layout = layoutDAO.pesquisarPorId(objDemanda.getLayout().getId());
			    objDemanda.setLayout(layout);
				
			}
			
			
			return objDemanda;
	 }


	 public Demandas pesquisarIDdoStatusDaDemanda(Integer idDemanda)
	 {
		    Demandas objDemanda;
			objDemanda = demandaDAO.pesquisarPorId(idDemanda);
			return objDemanda;
	 }
	 
	 
	 
	 public List<Demandas> obterTodos() 
	 {
			List<Demandas> lstDemandas = new ArrayList<Demandas>();
			Demandas demandas; 
			
			lstDemandas = (List<Demandas>) demandaDAO.obterTodos(); 
		
			
			for (int i=0 ; i < lstDemandas.size() ; i++)
			{
				demandas = lstDemandas.get(i);
				montarObjetoDemanda(demandas);
				
				
			}

			return lstDemandas;
		}

	public List <Demandas>pesquisar(Demandas objDemanda)
	{		
		
		List<Demandas> lstDemandas = new ArrayList<Demandas>();
		Demandas demanda; 
		lstDemandas = (List<Demandas>) demandaDAO.pesquisar(objDemanda); 
		
		for (int i=0 ; i < lstDemandas.size() ; i++)
		{
			demanda = lstDemandas.get(i);
			montarObjetoDemanda(demanda);
			
		}	
		
		return lstDemandas;	
		
		
	}

	@Override
	public Informacoes incluirInfomacoes(Informacoes info)
	{
		
		return informacoesDemandaDAO.incluir(info);
	}
	
	
	 public List<DemandasItemAtivo> obterItensAtivoDemanda(Integer idDemanda) 
	 {
			List<DemandasItemAtivo> lstItensAtivo = new ArrayList<DemandasItemAtivo>();		
			
		
		   
			lstItensAtivo = demandasItemAtivoDAO.obterItensAtivoDemanda(idDemanda);

			return lstItensAtivo;
    }
	 
	 
	 public List <Demandas>  pesquisarDemandasMovimento(Integer origem , Integer destino, Integer status, Integer demanda)
	 {
			
			
			List<Demandas> lstDemandas = new ArrayList<Demandas>();
			Demandas demandas;
			
			lstDemandas = (List<Demandas>) demandaDAO.pesquisarDemandasMovimento(origem ,destino,status,demanda); 
	
			
			for (int i=0 ; i < lstDemandas.size() ; i++)
			{
				demandas = lstDemandas.get(i);
				montarObjetoDemanda(demandas);
			}

			
			
			return lstDemandas;
			
			
			
    }
	 
	 
	 public ItensAtivos  obterItensAtivo(Integer idItem) 
	 {	
		   ItensAtivos item =  itensAtivosDAO.obterID(idItem);
		   Local local = locaisDAO.pesquisarPorId(item.getLocal().getId());
		   item.setLocal(local);
		   return item;
     }
	 
	 
	 public Boolean pesquisarDemandaItemAtivo(Integer id) {
			
		 return demandasItemAtivoDAO.pesquisarPossivelFinalizacao(id);
	 }

		
		
	 @Transactional
	 public void trocaItemDemanda(Integer idDemanda,  Integer idItemAtual, 
			                      Integer idItemAnterior, Integer idLocalDestino , String observacao)
	 {      
		   //excluir o item da demanda
		   demandasItemAtivoDAO.excluirItemTroca(idDemanda, idItemAnterior);
		   
		   //incluir o novo item
		   DemandasItemAtivo demandasItemAtv = new DemandasItemAtivo();
		   demandasItemAtv.setIdDemanda(idDemanda);
		   ItensAtivos itemAtivos = itensAtivosDAO.obterID(idItemAtual);
		   demandasItemAtv.setItemAtivos(itemAtivos);
		   demandasItemAtv.setLocalOrigem(itemAtivos.getLocal());
		   Local localDestino = new Local();
		   localDestino.setId(idLocalDestino);
		   demandasItemAtv.setLocalDestino(localDestino);
		   demandasItemAtivoDAO.incluir(demandasItemAtv);
		   //registrar a troca.
		   demandasItemAtivoDAO.incluirItensDemandaTroca(idDemanda, idItemAtual, idItemAnterior, observacao);
		
	 }
	 
	 
	

	private void montarObjetoDemanda(Demandas demandas) 
	{
		TipoDemanda tipoDemanda;
		Layout layout;
		StatusDemanda stsDemanda;
		tipoDemanda = tipodemandaDAO.pesquisarPorId(demandas.getTipoDemanda().getId());
		demandas.setTipoDemanda(tipoDemanda);
		layout = layoutDAO.pesquisarPorId(demandas.getLayout().getId());
		Collection<Configuracoes> lstConfs = configuracoesDAO.obterConfiguracoesLayout(layout.getId());
		layout.setConfiguracoes(lstConfs);
		demandas.setLayout(layout);
		Collection<Informacoes> lstInfos = informacoesDemandaDAO.obterInformacoesDemanda(demandas.getId());
		demandas.setInformacoesDemanda(lstInfos);
		stsDemanda = statusDemandaDAO.pesquisarPorId(demandas.getStatusDemanda().getId());
		demandas.setStatusDemanda(stsDemanda);
		demandas.setTodosItensLocalOrigem(demandaDAO.verificaItensLocalOrigem(demandas.getId()));
	}
	 
	 
	 private void salvarDemandasItemAtivo(Demandas demandas, Integer idDemanda) {
		 
			Collection<DemandasItemAtivo> demandasItem = demandas.getLstDemandasItensAtivos();
			DemandasItemAtivo demandasItemAtivo;
			
		  if(demandasItem!=null)
		  {
			for (Iterator<DemandasItemAtivo> iterator = demandasItem.iterator(); iterator.hasNext();) 
			{
				demandasItemAtivo = iterator.next();
				
				if(demandasItemAtivo.getItemAtivos().getItensAtivosId()!=null && demandasItemAtivo.getItemAtivos().getItensAtivosId() > 0 )
				{
					demandasItemAtivo.setIdDemanda(idDemanda);
					demandasItemAtivoDAO.incluir(demandasItemAtivo);
				}else
				{
					
					ItensAtivos itensAtivoNovo = itensAtivosDAO.incluir(demandasItemAtivo.getItemAtivos());
					demandasItemAtivo.getItemAtivos().setItensAtivosId(itensAtivoNovo.getItensAtivosId());
					demandasItemAtivo.setIdDemanda(idDemanda);
					demandasItemAtivoDAO.incluir(demandasItemAtivo);
				}
				
			}
		  }
		}

	public Boolean verificarExisteItemDemanda(Integer idDemanda , Integer idItem) 
	{
	    return demandasItemAtivoDAO.existeItemDemanda(idDemanda,idItem);
		
	}

	 
	 
	
	
	
	 
}

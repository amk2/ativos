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

import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.MovimentoAtivo;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.modelo.TipoMovimentoAtivo;
import com.projtec.slingcontrol.persistencia.AtivosDAO;
import com.projtec.slingcontrol.persistencia.DemandaDAO;
import com.projtec.slingcontrol.persistencia.DemandasItemAtivoDAO;
import com.projtec.slingcontrol.persistencia.InformacoesMovimentoAtivoDAO;
import com.projtec.slingcontrol.persistencia.ItensAtivosDAO;
import com.projtec.slingcontrol.persistencia.LayoutDAO;
import com.projtec.slingcontrol.persistencia.LocaisDAO;
import com.projtec.slingcontrol.persistencia.MovimentoAtivoDAO;
import com.projtec.slingcontrol.persistencia.TipoDemandaDAO;
import com.projtec.slingcontrol.persistencia.TipoMovimentoAtivoDAO;

@Service("movimentoAtivoGerencia")
public class MovimentoAtivoGerencia extends InformacoesTemplate
{
	
	@Autowired
	private MovimentoAtivoDAO movimentoAtivoDAO;

	@Autowired
	private LayoutDAO layoutDAO ;
	
	@Autowired
	private DemandaDAO demandaDAO;
	
	@Autowired
	private DemandasItemAtivoDAO demandasItemAtivoDAO;

	
	@Autowired
	private TipoMovimentoAtivoDAO tipoMovAtivoDAO;
	
	@Autowired
	private InformacoesMovimentoAtivoDAO informacoesMovimentoAtivoDAO;
	
	
	@Autowired
	private TipoDemandaDAO tipoDemandaDAO;
	
	@Autowired
	private LocaisDAO locaisDAO;
	
	@Autowired
	private AtivosDAO ativoDAO;

	@Autowired
	private ItensAtivosDAO itensAtivosDAO;

	
	public Collection<TipoMovimentoAtivo> obterTodosTipoMovimento() {

		return tipoMovAtivoDAO.obterTodos();

	}

	public Collection<Layout> obterTodosNomesLayouts(String nomeLayout) {
		
		return layoutDAO.obterTodosFiltroLayout(nomeLayout);

	}
	
	public Collection<Layout> obterTodosLayouts()
	{
		return layoutDAO.obterTodos();
	}
	
	public Collection<Demandas> obterTodosNomesDemandas() {
		
		return  demandaDAO.obterTodos();

	}
	
	
	@Transactional
    public MovimentoAtivo incluir(MovimentoAtivo movimentoAtivos)
	{
		MovimentoAtivo novoMovimentoAtivo;
		Collection<Informacoes> infosNova = new ArrayList<Informacoes>();
	
		novoMovimentoAtivo = movimentoAtivoDAO.incluir(movimentoAtivos);

		Collection<Informacoes> lstInformacoesMovimentoAtivo = movimentoAtivos.getInformacoesMovimentoAtivo();

		infosNova = incluirListaInformacoes(lstInformacoesMovimentoAtivo, novoMovimentoAtivo.getId());  
         
		novoMovimentoAtivo.setInformacoesMovimentoAtivo(infosNova);

		return novoMovimentoAtivo;
		
		
		
	}
	
	
	 @Transactional
	 public Boolean excluir(Integer idMovimentoAtivo)
	 {
		    informacoesMovimentoAtivoDAO.excluirAtivoID(idMovimentoAtivo);

			return movimentoAtivoDAO.excluir(idMovimentoAtivo);
	 }
	 
	 @Transactional
	 public Boolean alterar(MovimentoAtivo objMovimentoAtivo)
	 {		
		    informacoesMovimentoAtivoDAO.excluirAtivoID(objMovimentoAtivo.getId());
			Collection<Informacoes> lstInformacoesMovimentoAtivo = objMovimentoAtivo.getInformacoesMovimentoAtivo();
			incluirListaInformacoes(lstInformacoesMovimentoAtivo, objMovimentoAtivo.getId());  
	         
			return movimentoAtivoDAO.alterar(objMovimentoAtivo);

	 }
	 
	
	 public MovimentoAtivo perquisarID(Integer idmovimentoAtivo)
	 {
		 MovimentoAtivo objMovimentoAtivo;
			Collection<Informacoes> informacoesMovimentoAtivo;
			Map<Integer, Informacoes> mapInformacoesMovimentoAtivo =new HashMap<Integer, Informacoes>();
			Informacoes infoMovimentoAtivo;
		
			informacoesMovimentoAtivo = informacoesMovimentoAtivoDAO.obterInformacoesMovimentoAtivo(idmovimentoAtivo);
			for (Iterator<Informacoes> iterator = informacoesMovimentoAtivo.iterator(); iterator.hasNext();) 
			{
				
				infoMovimentoAtivo = iterator.next();
				mapInformacoesMovimentoAtivo.put(infoMovimentoAtivo.getConfiguracoesId(),infoMovimentoAtivo);
				
			}
			
			objMovimentoAtivo = movimentoAtivoDAO.pesquisarPorId(idmovimentoAtivo);		
			objMovimentoAtivo.setInformacoesMovimentoAtivo(informacoesMovimentoAtivo);
			objMovimentoAtivo.setMapInformacoesMovimentoAtivo((mapInformacoesMovimentoAtivo));
			TipoMovimentoAtivo tpMovimento = tipoMovAtivoDAO.pesquisarPorId(objMovimentoAtivo.getTipomovimento().getId());
			objMovimentoAtivo.setTipomovimento(tpMovimento);
			Demandas demanda = demandaDAO.pesquisarPorId(objMovimentoAtivo.getDemandaId().getId());
			TipoDemanda tpDemanda = tipoDemandaDAO.pesquisarPorId(demanda.getTipoDemanda().getId());
			demanda.setTipoDemanda(tpDemanda);
			objMovimentoAtivo.setDemandaId(demanda); 
			Layout l = layoutDAO.pesquisarPorId(objMovimentoAtivo.getLayoutId().getId());
			objMovimentoAtivo.setLayoutId(l);
			return objMovimentoAtivo;
	 }
	 

	 
	 public List<MovimentoAtivo> obterTodos() 
		{
			List<MovimentoAtivo> lstMovimentoAtivo = new ArrayList<MovimentoAtivo>();
			MovimentoAtivo movimentoAtivo; 
			TipoMovimentoAtivo tipoMovimentoAtivo; 
			Layout layout; 
			Demandas demanda;
			
			lstMovimentoAtivo = (List<MovimentoAtivo>) movimentoAtivoDAO.obterTodos(); 
		
			
			for (int i=0 ; i < lstMovimentoAtivo.size() ; i++)
			{
				movimentoAtivo = lstMovimentoAtivo.get(i);
				tipoMovimentoAtivo = tipoMovAtivoDAO.pesquisarPorId(movimentoAtivo.getTipomovimento().getId());
				movimentoAtivo.setTipomovimento(tipoMovimentoAtivo);
				layout = layoutDAO.pesquisarPorId(movimentoAtivo.getLayoutId().getId());
				movimentoAtivo.setLayoutId(layout);
				demanda = demandaDAO.pesquisarPorId(movimentoAtivo.getDemandaId().getId());
				TipoDemanda tpDemanda = tipoDemandaDAO.pesquisarPorId(demanda.getTipoDemanda().getId());
				demanda.setTipoDemanda(tpDemanda);
				movimentoAtivo.setDemandaId(demanda); 
			}

			return lstMovimentoAtivo;
		}
	 
	 
	 public List<MovimentoAtivo>pesquisar(MovimentoAtivo objMovimentoAtivo){
			
			
			List<MovimentoAtivo> lstMovimentoAtivo = new ArrayList<MovimentoAtivo>();
			MovimentoAtivo movimentoativo; 
			TipoMovimentoAtivo tipomovimentoAtivo; 
			Demandas demanda;
			Layout layout; 
			
			lstMovimentoAtivo = (List<MovimentoAtivo>) movimentoAtivoDAO.pesquisar(objMovimentoAtivo); 
		
			
			for (int i=0 ; i < lstMovimentoAtivo.size() ; i++)
			{
				movimentoativo = lstMovimentoAtivo.get(i);
				tipomovimentoAtivo = tipoMovAtivoDAO.pesquisarPorId(movimentoativo.getTipomovimento().getId());
				movimentoativo.setTipomovimento(tipomovimentoAtivo);
				demanda =demandaDAO.pesquisarPorId(movimentoativo.getDemandaId().getId());
				movimentoativo.setDemandaId(demanda);
				layout = layoutDAO.pesquisarPorId(movimentoativo.getLayoutId().getId());
				try {
					TipoDemanda tpDemanda = tipoDemandaDAO.pesquisarPorId(demanda.getTipoDemanda().getId());
					demanda.setTipoDemanda(tpDemanda);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				movimentoativo.setLayoutId(layout);
				
			}

			
			
			return lstMovimentoAtivo;
			
			
			
		}

	@Override
	public Informacoes incluirInfomacoes(Informacoes info)
	{
		return informacoesMovimentoAtivoDAO.incluir(info);
	
	}

	public void executarDemanda(MovimentoAtivo movAtivo) 
	{		
		  incluir(movAtivo);
		  Integer idStatus = 2 ; //EM ANDAMENTO
		  demandaDAO.alterarStatus(movAtivo.getDemandaId().getId(),idStatus );
	}

	public void finalizardemanda(Integer demanda) 
	{

		  Integer idStatus = 3 ; //Concluido
		  demandaDAO.alterarStatus(demanda,idStatus );  //passa o codigo da demanda e o status
		  

		  
	}
		 
	 
	 
}

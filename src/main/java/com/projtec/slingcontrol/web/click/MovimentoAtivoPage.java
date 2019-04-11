package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.util.Bindable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.gerencia.LocaisGerencia;
import com.projtec.slingcontrol.gerencia.MovimentoAtivoGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.modelo.StatusDemanda;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.web.tipocampo.TipoCampoView;

@Component
public class MovimentoAtivoPage extends FormPage
{
	private static final long serialVersionUID = 1L;	
	
	@Bindable
	public String title = getMessage("titulo");

	@Bindable
	protected Form demandaPesquisaForm = new Form("demandaPesquisaForm");
	
	@Bindable
	protected   ActionLink pesquisar = new ActionLink("Pesquisar");
	
	@Bindable
	protected   ActionLink itensDemanda = new ActionLink("obterItensDemanda");
	
	//DAdos para pesquisa
	
	@Bindable
	protected Integer localOrigem; 
	
	@Bindable
	protected Integer localDestino; 

	@Bindable
	protected Boolean objDemandaItemativo;
	
	@Bindable
	protected Integer statusDemanda; 
	
	@Bindable
	protected Integer tipoDeDemanda; 
	
	@Bindable
	protected String mesgPesquisa = new String();
	
	@Bindable
	protected String mensagemSucesso = new String();
		
	@Bindable
	private Map<Integer,ListaGridView> mapListGridView = new HashMap<Integer,ListaGridView>();
	
	
	
	
	@Bindable
	protected String mesg;

	@Resource(name = "locaisGerencia")
	protected LocaisGerencia locaisGerencia;
	
	@Resource(name = "movimentoAtivoGerencia")
	protected MovimentoAtivoGerencia movimentoAtivoGerencia;

	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;
	
	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;
	
	@Autowired
	private ApplicationContext appContext;
	
	private static java.text.DateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");



	// Constructor ------------------------------------------------------------

	public MovimentoAtivoPage()
	{

	}
	@Override
	public boolean permitidoSalvar() {
	
		return Secure.isPermitted("botoes:movimentoativos:salvar");
	}
	
	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:movimentoativos:pesquisar");
	}
	
	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:movimentoativos:limpar");
	}

	
	@Override
	public void onInit() {
		super.onInit();
		initMovimentoAtivo();
		
	}

	private void initMovimentoAtivo()
	{		

		FieldSet movimentoAtivoDados = new FieldSet("MovimentoAtivo");
	//	FieldSet EntradaItemAtivoDados = new FieldSet("EntradaAtivo");
		

		DataProvider dataProviderLocais = new DataProvider<Option>()
		{
			@Override
			public Iterable<Option> getData()
			{
				List<Option> optionList = new ArrayList<Option>();
				optionList.add(new Option("","-- Selecione --"));

				Collection<Local> lstLocais = locaisGerencia.obterTodos();
				
				for (Iterator<Local> iterator = lstLocais.iterator(); iterator.hasNext();)
				{
					Local local = iterator.next();
					optionList.add(new Option(local.getId(), local.getNome()));
				}
				return optionList;
			}

		}; 
		Select origem = new Select("localOrigem");
		Select destino = new Select("localDestino");
		origem.add(new Option("","-- Selecione --"));		
		origem.setDataProvider(dataProviderLocais);
		origem.setRequired(false);	
		movimentoAtivoDados.add(origem);
		
		destino.add(new Option("","-- Selecione --"));
		destino.setDataProvider(dataProviderLocais);
		destino.setRequired(false);		
		movimentoAtivoDados.add(destino);
		
		// STATUS DA DEMANDA 
		Select statusDemanda = new Select("statusDemanda");		
		statusDemanda.add(new Option("","-- Selecione --"));
		
		statusDemanda.setDataProvider(new DataProvider<Option>() {

			@Override
			public Iterable<Option> getData() {
				List<Option> optionList = new ArrayList<Option>();
				// optionList.add(Option.EMPTY_OPTION);
				optionList.add(new Option("", " -- Selecione --"));

				Collection<StatusDemanda> lstTipoDemanda = demandaGerencia.obterTodosStatusDemanda();
				for (Iterator<StatusDemanda> iterator = lstTipoDemanda.iterator(); iterator
						.hasNext();) {
					StatusDemanda statusDemanda = iterator.next();
					optionList.add(new Option(statusDemanda.getId(), statusDemanda.getDescricao()));

				}
				return optionList;
			}

		});
		movimentoAtivoDados.add(statusDemanda);	

		//Tipo Demanda
		Select selectTipoDemanda = new Select("tipoDeDemanda");
		selectTipoDemanda.setRequired(false);		
		selectTipoDemanda.setDataProvider(new DataProvider<Option>() {

			@Override
			public Iterable<Option> getData() {
				List<Option> optionList = new ArrayList<Option>();
				// optionList.add(Option.EMPTY_OPTION);
				optionList.add(new Option("", " -- Selecione --"));

				Collection<TipoDemanda> lstTipoDemanda = demandaGerencia
						.obterTodosTipoDemanda();
				for (Iterator<TipoDemanda> iterator = lstTipoDemanda.iterator(); iterator
						.hasNext();) {
					TipoDemanda tipodemanda = iterator.next();
					optionList.add(new Option(tipodemanda.getId(), tipodemanda
							.getDescricao()));

				}
				return optionList;
			}

		});
		movimentoAtivoDados.add(selectTipoDemanda);
		demandaPesquisaForm.add(movimentoAtivoDados);		
		montasBotoes();		
		addControl(demandaPesquisaForm);
	}
	
	
	private void montasBotoes()
	{
	    ImageSubmit pesquisar = new ImageSubmit("Pesquisar",
				"/resources/images/btnPesquisar.png");
		pesquisar.setAttribute("onclick", "validar('Pesquisar')");
		pesquisar.setDisabled(!permitidoPesquisar());
		pesquisar.setStyle("border","1px solid black");
		pesquisar.setStyle("border-color","blue");
		if (pesquisar.isDisabled()){
			pesquisar.setStyle("border","1px solid black");
			pesquisar.setStyle("border-color","red");
		}
		pesquisar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
		
				return onPesquisaClick();
			}
		});
		getForm().add(pesquisar);
		
		ImageSubmit cancelar = new ImageSubmit("Cancelar",
				"/resources/images/btnLimpar.png");
		cancelar.setAttribute("onclick",
		"return window.confirm('Deseja realmente limpar dados ?');");
		cancelar.setDisabled(!permitidoCancelar());
		cancelar.setStyle("border","1px solid black");
		cancelar.setStyle("border-color","blue");
		if (cancelar.isDisabled()){
			cancelar.setStyle("border","1px solid black");
			cancelar.setStyle("border-color","red");
		}
		cancelar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
		
				return onCancelarClick();
			}
		});
		getForm().add(cancelar);
	}
	

	
	public Form getForm()
	{
		return demandaPesquisaForm;
	}

	
	public boolean onCancelarClick()
	{

		demandaPesquisaForm.clearValues();
		demandaPesquisaForm.clearErrors();
		mesgPesquisa= null;

		return true;
	}

	public boolean onPesquisaClick()
	{

		demandaPesquisaForm.clearErrors();
		List<Demandas> listaDemandas = demandaGerencia.pesquisarDemandasMovimento(localOrigem, localDestino, statusDemanda, tipoDeDemanda);
		
		if (listaDemandas != null && listaDemandas.size() > 0 ) {
			
			montarTabela(listaDemandas);
			 
		}
		else {
			
			 mensagemErro = "N&atilde;o foram encontrados Movimentos de Ativos com os dados informados";
			 
		}

		return true;

	}
	private void montarTabela(List<Demandas> listaDemandas) {
	
	mapListGridView = new HashMap<Integer,ListaGridView>();
		
		ListaGridView estLstView; 
		Integer soma=0;
		for (Iterator<Demandas> iterator = listaDemandas.iterator(); iterator.hasNext();) 
		{
			final  Demandas dem = iterator.next();
			
			if (mapListGridView.containsKey(dem.getLayout().getId()))
			{
				estLstView =mapListGridView.get(dem.getLayout().getId());				
				
			}else
			{	
			
				estLstView = new ListaGridView();
				estLstView.setTitulo(dem.getLayout().getNome());
				estLstView.setIdLayout(Integer.valueOf(dem.getLayout().getId()));				
				estLstView.setNomes(new  LinkedHashMap<String, String>());
				estLstView.setLinhasValores(new ArrayList<Map<String,String>>());
				mapListGridView.put(dem.getLayout().getId(), estLstView);	
				estLstView.setTotal(new Integer(0));
				
				//nomes .....
				Collection<Configuracoes> lstConfigs = dem.getLayout().getConfiguracoes();
				
				for (Iterator iterator2 = lstConfigs.iterator(); iterator2.hasNext();) 
				{
					Configuracoes configuracoes = (Configuracoes) iterator2.next();
					if (configuracoes.getPesquisa().equals(Configuracoes.PESQ_SIM))
					{
						estLstView.getNomes().put(String.valueOf(configuracoes.getId()), configuracoes.getNomecampo());
					}
					
					
					
				}	
				      estLstView.getNomes().put("data",  "Data Cria&ccedil;&atilde;o");
				      estLstView.getNomes().put("itens",  " Demanda ");
				      estLstView.getNomes().put("tipoDemanda",  "Tipo Demanda");
				      estLstView.getNomes().put("statusDemanda",  "Status Demanda");
				      estLstView.getNomes().put("acoes",  "A&ccedil;&otilde;es");

				
				
			}
			
			//colocar os valores
		    Collection<Informacoes>	 lstInfos = dem.getInformacoesDemanda();
		    Map<String, String> mapLinha = new LinkedHashMap<String, String>();
		    estLstView.getLinhasValores().add(mapLinha);
		    for (Iterator iterator2 = lstInfos.iterator(); iterator2.hasNext();) 
		    {
				Informacoes informacoes = (Informacoes) iterator2.next();
				
	            Configuracoes config = layoutGerencia.pesquisarConfiguracao(informacoes.getConfiguracoesId());
	            if (config.getPesquisa().equals(Configuracoes.PESQ_SIM))
	            {
	            	String strValor = formataValor(config,informacoes);
					
					mapLinha.put(String.valueOf(informacoes.getConfiguracoesId()), strValor);
	            	
	            }			
							
			}
		    
		    String linkAcoes = "";
		    String acaodaurl="";
		    if (dem.getStatusDemanda().getDescricao().equals(StatusDemanda.ABERTO)) 
		    {
		    		//aberto para andamento
		    		acaodaurl = "executar";
			    	if (dem.getTodosItensLocalOrigem()) {
			    		linkAcoes = "<a href='#' onclick='movimentarDemanda(" + dem.getId() + ");'>Executar</a>" ;
			    	}
			    	else {
			    		linkAcoes = "<img src='resources/images/warning.png' alt='Os itens n&atilde;o se encontram na mesma localiza&ccedil;&atilde;o de origem' />" ;
			    	}
		    	
		    }
		    if (dem.getStatusDemanda().getDescricao().equals(StatusDemanda.EM_ANDAMENTO)) 
		    {
		    		//de andamento para concluido
		    		acaodaurl = "finalizar";
		    		objDemandaItemativo = demandaGerencia.pesquisarDemandaItemAtivo(dem.getId());
		    		// retorna resultado da query que verifica se pode finalizar ou não.
		    		if (objDemandaItemativo==true) {
			    		linkAcoes = "<a href='#' onclick='movimentarDemandaFinalizar(" + dem.getId() + ");'>Finalizar</a>" ;
			    	}
			    	else {
			    		linkAcoes = "<a href='#' onclick='avisoFinalizar(" + dem.getId() + ");'>Finalizar</a>" ;
			    	}
		    		
		    }
		   
		    String verItens = "<a href='#' onclick='verItens(" + dem.getId() + ");'>Ver Itens</a>"  
		                    +  " | <a href='demandasDetalhe.htm?demanda=" +  dem.getId() +"'>Deltalhes</a> " ; 
		    mapLinha.put("data",   dem.getDataCadastro()!=null ? formatter.format(dem.getDataCadastro()):"" );
		    mapLinha.put("itens", verItens);
		    mapLinha.put("tipoDemanda", dem.getTipoDemanda().getDescricao());
		    mapLinha.put("statusDemanda", dem.getStatusDemanda().getDescricao());
		    mapLinha.put("acoes", linkAcoes);
	
		}	
		
	}
	
	@Override
	protected boolean onSalvarClick() {
		return false;
	}
	@Override
	protected void montarGrid() {
		
	}
	
	
	private String formataValor(Configuracoes config, Informacoes info)
	{
		TipoCampoView tpCampo = (TipoCampoView) appContext.getBean(String.valueOf(config.getTipoCampo().getId()));
		String valor =  tpCampo.formataInformacao(config, info);
		
		return valor;
	}
	
	


}

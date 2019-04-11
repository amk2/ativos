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
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Option;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Select;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.StatusDemanda;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;
import com.projtec.slingcontrol.web.tipocampo.TipoCampoView;

@Component
public class DemandasPesquisaPage extends BorderPage 
{

	private static final String DEM_ENTRADA = "DEM_ENTRADA";
	private static final String DEM_SAIDA = "DEM_SAIDA";
	private static final String DEM_MOV_ATV = "DEM_MOV_ATV";
	
	
	private static final String NOME_LAYOUT = "Demandas";
	
	@Bindable
	protected Form demandasPesquisaForm = new Form("demandasPesquisaForm");
	
	
	private Select select = new Select("layout", true);
	
	@Bindable
	protected String mesg;	
	
	@Bindable
	protected String mesgPesquisa;	

	
	@Bindable
	public String title = getMessage("titulo");

	@Bindable
	public String mesgAtualizarDaURL;
	
	@Bindable
	public String mesgAtualizarparaURL;
	
	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;

	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;
	
	@Bindable
	protected Boolean exibirSelecao;
	
	
	@Bindable
	private Map<Integer,ListaGridView> mapListGridView = new HashMap<Integer,ListaGridView>();
	
	@Autowired
	private ApplicationContext appContext;
	
//	private static java.text.DateFormat formatterData = new java.text.SimpleDateFormat("dd/MM/yyyy HH:MM:ss");
	private static java.text.DateFormat formatterData = new java.text.SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public void onInit() {
		super.onInit();
		mesgAtualizarDaURL = mesgAtualizarparaURL;

		montaForm();
		montarBotoes();
		
	}
	
	private void montaForm()
	{
		FieldSet demandasDados = new FieldSet("DemandasDados");
		
		
		//exibir selecionado		
		HiddenField  hExibirSelecao = new HiddenField("exibirSelecao" , Boolean.class);
		if (exibirSelecao!=null && exibirSelecao)
		{
			hExibirSelecao.setValueObject(true);
			showMenu = false;
		}else
		{
			hExibirSelecao.setValueObject(false);	
		}
		demandasPesquisaForm.add(hExibirSelecao);
		
	
		Select selectTipoDemanda = new Select("tipoDemanda");
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

		demandasDados.add(selectTipoDemanda);
		
		
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
		demandasDados.add(statusDemanda);
		
		
/*		RadioGroup tipoMovimentacao = new RadioGroup("tipoMovimentacao");
		tipoMovimentacao.add(new Radio(DEM_MOV_ATV, getMessage("movAtivo")));
		tipoMovimentacao.add(new Radio(DEM_ENTRADA,
				getMessage("entradaAtivo")));
		tipoMovimentacao.add(new Radio(DEM_SAIDA,
				getMessage("saidaAtivo")));
		tipoMovimentacao.setVerticalLayout(true);
		demandasDados.add(tipoMovimentacao);
*/
		
		
		select.add(new Option("", "-- Selecione --"));
		select.addAll(obterLayouts());
		demandasDados.add(select);

		demandasPesquisaForm.add(demandasDados);

		select.setAttribute("onchange", "Click.submit(demandasPesquisaForm, false)");
		select.setRequired(false);
		
		addControl(demandasPesquisaForm);

	

		// Bind the form field request values
		ClickUtils.bind(demandasPesquisaForm);

		obterConfiguracoes(null);
		
		
		
	}
	
	
	
	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) {
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);

			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, demandasPesquisaForm);

		}
	}
	
	
	private Collection<Option> obterLayouts() {

		List<Option> optionList = new ArrayList<Option>();
		//Collection<Layout> lstLayout =  layoutGerencia.obterTodos();  // demandaGerencia.obterTodosNomesLayouts(NOME_LAYOUT);
		Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDDEMANDA);
		
		for (Layout layout : lstLayout) {
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}
		return optionList;
	}
	
	
	private void montarBotoes()
	{
	    
		//botão pesquisar
		ImageSubmit pesquisar = new ImageSubmit("Pesquisar",
				"/resources/images/btnPesquisar.png");
		//pesquisar.setAttribute("onclick", "validar('Pesquisar')");
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
		
		
		
		
		//botão cancelar ou limpar	
		ImageSubmit cancelar = new ImageSubmit("Cancelar",
				"/resources/images/btnLimpar.png");
		cancelar.setAttribute("onclick",
		"return window.confirm('Deseja realmente limpar dados ?');");
		
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
		
		
		
		
/*		//botao proximo
		ImageSubmit proximo = new ImageSubmit("Pesquisar",
				"/resources/images/btnAdcionar.png");
		//pesquisar.setAttribute("onclick", "validar('Pesquisar')");
		pesquisar.setStyle("border","1px solid black");
		pesquisar.setStyle("border-color","blue");
		if (pesquisar.isDisabled()){
			pesquisar.setStyle("border","1px solid black");
			pesquisar.setStyle("border-color","red");
		}
		pesquisar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
		
				return onProximoClick();
			}
		});
		getForm().add(proximo);
*/
	}
	
	/*protected boolean onProximoClick() {
		// TODO Auto-generated method stub
		return false;
	}*/

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
						if (exibirSelecao!=null && exibirSelecao)
						{
							   estLstView.getNomes().put("selecionar",  "Selecionar");
						}
						

						Collection<Configuracoes> lstConfigs = dem.getLayout().getConfiguracoes();
						
						for (Iterator iterator2 = lstConfigs.iterator(); iterator2.hasNext();) 
						{
							Configuracoes configuracoes = (Configuracoes) iterator2.next();
							if (configuracoes.getPesquisa().equals(Configuracoes.PESQ_SIM))
							{
								estLstView.getNomes().put(String.valueOf(configuracoes.getId()), configuracoes.getNomecampo());
							}
							
							
							
						}					
						
						//http://www.icmc.usp.br/ensino/material/html/especiais.html
						estLstView.getNomes().put("data",  "Data Cria&ccedil;&atilde;o");
						estLstView.getNomes().put("itens",  "Itens da Demanda");
						estLstView.getNomes().put("tipoDemanda",  "Tipo Demanda");
						estLstView.getNomes().put("statusDemanda",  "Status Demanda");
						if ( !(exibirSelecao!=null && exibirSelecao))
						{
						    	  estLstView.getNomes().put("acoes",  "A&ccedil;&otilde;es");
						}
						     
					}
					
					//colocar os valores
				    Collection<Informacoes>	 lstInfos = dem.getInformacoesDemanda();
				    Map<String, String> mapLinha = new LinkedHashMap<String, String>();
				    estLstView.getLinhasValores().add(mapLinha);
				    for (Iterator iterator2 = lstInfos.iterator(); iterator2.hasNext();) {
						Informacoes informacoes = (Informacoes) iterator2.next();
						
			            Configuracoes config = layoutGerencia.pesquisarConfiguracao(informacoes.getConfiguracoesId());
			            if (config.getPesquisa().equals(Configuracoes.PESQ_SIM))
			            {
			            	String strValor = formataValor(config,informacoes);
							
							mapLinha.put(String.valueOf(informacoes.getConfiguracoesId()), strValor);
			            	
			            }			
									
					}
				    
				    String linkAcoes = "";
				    //Verificar as possiveis acoes para a demanda - editar - remover - cancelar. ver 
				    if (dem.getStatusDemanda().getId().equals(StatusDemanda.ID_ABERTO) )
				    {
				    	 linkAcoes = linkAcoes   +   "<a href='demandas.htm?actionLink=edit&amp;value=" +  dem.getId() +"'>Editar</a> |"  
						    +   "<a href=\"demandas.htm?actionLink=delete&value=" +   dem.getId()
						    +   "\" onclick=\"return window.confirm('Deseja realmente Remover ?');\">Remover</a>" ;
				    	
				    }
				   
					String verItens = "<a href='#' onclick='verItens(" + dem.getId() + ");'>Ver Itens</a>" ; 
				    mapLinha.put("data",   dem.getDataCadastro()!=null ? formatterData.format(dem.getDataCadastro()):"" );
				    mapLinha.put("itens", verItens);
				    mapLinha.put("tipoDemanda", dem.getTipoDemanda().getDescricao());
				    mapLinha.put("statusDemanda", dem.getStatusDemanda().getDescricao());
				    
				    if ( !(exibirSelecao!=null && exibirSelecao))
				    {
				    	 linkAcoes  = linkAcoes 
				    	             +  (linkAcoes.length() > 0 ?"|": "") 
                                     +  "<a href='demandasDetalhe.htm?demanda=" 
                                     +  dem.getId() +"'>Ver</a> " ; 
						    
				    	  mapLinha.put("acoes", linkAcoes);
				    }
				  
				    if (exibirSelecao!=null && exibirSelecao)
				    {
				    	  mapLinha.put("selecionar", "<input type='radio' name='selectItem' value='" + dem.getId() + "'>");
				    }
				    
				   
			
				}	
				
	}
	
	

	private String formataValor(Configuracoes config, Informacoes info)
	{
		TipoCampoView tpCampo = (TipoCampoView) appContext.getBean(String.valueOf(config.getTipoCampo().getId()));
		String valor =  tpCampo.formataInformacao(config, info);
		
		return valor;
	}
	
	@Override
	public Form getForm() {
		
		return demandasPesquisaForm;
	}

	@Override
	protected boolean onPesquisaClick() 
	{
		
		demandasPesquisaForm.clearErrors();
		Demandas demandas = montarDemandasPesquisa();
		final List<Demandas> lstDemandas;

		
		if ( demandas==null ||
			 ( ( demandas.getTipoDemanda()==null || demandas.getTipoDemanda().getId()  <= 0  )  
			 && (demandas.getStatusDemanda()==null || demandas.getStatusDemanda().getId() <= 0 )
			 && (demandas.getLayout()==null || demandas.getLayout().getId()  <= 0 )
			 )
		   )
		{
			
			mesgPesquisa = "Selecionar no minimo um crit&eacute;rio para a pesquisa";
			return false; 
		}
		
		lstDemandas = new ArrayList<Demandas>(demandaGerencia.pesquisar(demandas));
		 if(lstDemandas!=null && lstDemandas.size() > 0 )
		 {
			 montarTabela(lstDemandas);
			 mesgPesquisa= null;
			 
		 }else
		 {
			 mesgPesquisa = "N&atilde;o existem registros para os parametros da pesquisa";
		 }
		
		 
		mesg = getMessage("mesgPesquisa", lstDemandas.size());
		montarTabela(lstDemandas);
		return true;
	}

	private Demandas montarDemandasPesquisa() {

		Demandas demandas = new Demandas();
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		TipoDemanda tipoDemandaId = new TipoDemanda();
		Layout layoutId = new Layout();

		FieldSet demandasDados = (FieldSet) demandasPesquisaForm.getControl("DemandasDados");

		Field fieldTipoDemanda = demandasDados.getField("tipoDemanda");

		if (fieldTipoDemanda.getValue() != null
				&& !"".equals(fieldTipoDemanda.getValue())) {
			tipoDemandaId.setId(Integer.valueOf(fieldTipoDemanda.getValue()));
		}

		if (select.getValue() != null && !"".equals(select.getValue())) {
			layoutId.setId(Integer.valueOf(select.getValue()));
		}

		demandas.setTipoDemanda(tipoDemandaId);
		demandas.setLayout(layoutId);

		// status
		Field fieldStatus = demandasDados.getField("statusDemanda");
	
		if (fieldStatus.getValue() != null && !"".equals(fieldStatus.getValue())) {
			StatusDemanda statusDemanda = new StatusDemanda();
			statusDemanda.setId(Integer.valueOf(fieldStatus.getValue()));
			demandas.setStatusDemanda(statusDemanda);
			// demandas.setStatus(fieldStatus.getValue());
		}

		Collection<Field> fields;
		
		FieldSet configs = (FieldSet) demandasPesquisaForm.getControl("configuracoes");
		// configs.add(imgField);
		// System.out.println("TESTE: " + configs.getControl("imgName"));
		if (configs != null) {

			lstInfos = informacoesUtil.montarInfomacoes(configs);
			demandas.setInformacoesDemanda(lstInfos);

		}
		
		return demandas;

	}

	@Override
	protected boolean onCancelarClick() {
		
		demandasPesquisaForm.clearErrors();
		if (!(exibirSelecao!=null && exibirSelecao) )
		{
			demandasPesquisaForm.clearValues();
			FieldSet configs = (FieldSet) demandasPesquisaForm.getControl("configuracoes");
			if(configs!=null)
			{
				demandasPesquisaForm.remove(configs);
			}
		}
		return true;
	}

	@Override
	protected boolean onSalvarClick() {
		return false;
	}
	
}

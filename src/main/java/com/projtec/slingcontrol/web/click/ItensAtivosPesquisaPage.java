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
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.TextField;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.ConfigInfoPesquisaVO;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;
import com.projtec.slingcontrol.web.tipocampo.TipoCampoView;

@Component
public class ItensAtivosPesquisaPage  extends  FormPage 
{   
	private static final String NOME_LAYOUT = "Ativos";
	
	 @Bindable
	public String title = getMessage("titulo");
	
	@Bindable
	protected Form itenAtivoPesquisaForm = new Form("itenAtivoPesquisaForm");;
	
	private Select select = new Select("layout", true);
	
	private TextField identificacao = new TextField("identificacao");
	
	
	@Bindable
	protected String mesgPesquisa = new String();
	
	@Bindable
	protected Boolean exibirSelecao;
	
	@Bindable
	protected Boolean exibirSelecaoMultiplo;
	
	@Bindable
	protected Integer idLayout;
	
	@Bindable
	private Map<Integer,ListaGridView> mapListGridView = new HashMap<Integer,ListaGridView>();
	
	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;
	
	@Resource(name = "ativosGerencia")
	protected AtivosGerencia ativosGerencia;
	
	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;
	
	@Autowired
	private ApplicationContext appContext;
	
	
	@Override
	public void onInit() 
	{		
		super.onInit();
	    iniciarPesquisaForm();
	}

	private void iniciarPesquisaForm() {
		
		
		FieldSet itemAtivosDados = new FieldSet("itemAtivosDados");
		
		//id do Layout quando troca de itens
		HiddenField idLayoutField = new HiddenField("idLayout", Integer.class);
		if (idLayout!=null && idLayout > 0 )
		{
			idLayoutField.setValueObject(idLayout);
		}
		itenAtivoPesquisaForm.add(idLayoutField);
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
		itenAtivoPesquisaForm.add(hExibirSelecao);
		HiddenField  hExibirSelecaoMult = new HiddenField("exibirSelecaoMultiplo" , Boolean.class);
		if (exibirSelecaoMultiplo!=null && exibirSelecaoMultiplo)
		{
			hExibirSelecaoMult.setValueObject(true);
			showMenu = false;
		}else
		{
			hExibirSelecaoMult.setValueObject(false);	
		}
		itenAtivoPesquisaForm.add(hExibirSelecaoMult);
	
		
		select.setRequired(false);
		select.setLabel(select.getLabel() + " <span class='required'>*</span>");
		select.setAttribute("class", "validate[required]");

		// select.add(Option.EMPTY_OPTION);
		select.add(new Option("", "-- Selecione --"));
		select.addAll(obterLayouts());// obterLayouts()
		select.setAttribute("class", "validate[required]");
		if (  !(idLayout!=null && idLayout > 0 ) )
		{ 
			itemAtivosDados.add(select);
		}

		itemAtivosDados.add(identificacao);
		
		itenAtivoPesquisaForm.add(itemAtivosDados);

		select.setAttribute("onchange", "Click.submit(itenAtivoPesquisaForm, false)");
		
		
	    addControl(itenAtivoPesquisaForm);
		
		ClickUtils.bind(itenAtivoPesquisaForm);
		
		obterConfiguracoesPesquisa();
		
		exibeBotoes();
		
		

		
	}
	
	

	private Collection<Option> obterLayouts() {
		List<Option> optionList = new ArrayList<Option>();
		
		//Collection<Layout> lstLayout = layoutGerencia.obterTodos();    //obterTodosNomesLayouts(NOME_LAYOUT);
		
		Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDATIVO);
		
		
		for (Layout layout : lstLayout) 
		{
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}
		
		return optionList;
	}
	
	
	private void obterConfiguracoesPesquisa() 
	{
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);	
			configuracoesUtil.exibirConfiguracoesPesquisa(layout,itenAtivoPesquisaForm);
		
		}else if (idLayout!=null && idLayout > 0 )
		{
			Layout layout = layoutGerencia.pesquisarId(idLayout);
			configuracoesUtil.exibirConfiguracoesPesquisa(layout,itenAtivoPesquisaForm);
		}
	}
	
	private void exibeBotoes()
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
	

	@Override
	public Form getForm() {
	
		return itenAtivoPesquisaForm;
	}

	@Override
	protected boolean onPesquisaClick() {
				 
	
		 List<ConfigInfoPesquisaVO> lstConfigisValor = new ArrayList<ConfigInfoPesquisaVO>(montarListaConfigValores() );
		 Integer idLayout = obterIDLayout();
		 String codIdentificacao = identificacao.getValue();
		
	     List<ItensAtivos> listaItensAtivos = ativosGerencia.pesquisarItens(idLayout , codIdentificacao, lstConfigisValor);
			
		 if(listaItensAtivos!=null && listaItensAtivos.size() > 0 )
		 {
			 montarTabela(listaItensAtivos);
			 mesgPesquisa= null;
			 
		 }else
		 {
			 mesgPesquisa = getMessage("nao_existe_resultado");
		 }
		

		return true;
	}
	
	


	@Override
	protected boolean onCancelarClick() {
		
		itenAtivoPesquisaForm.clearErrors();
		if (!(exibirSelecao!=null && exibirSelecao) )
		{
			itenAtivoPesquisaForm.clearValues();
			FieldSet configs = (FieldSet) itenAtivoPesquisaForm.getControl("configuracoes");
			if(configs!=null)
			{
				itenAtivoPesquisaForm.remove(configs);
			}
		}
		
		mesgPesquisa= null;

		return true;
	}
	
	
	@Override
	protected boolean onSalvarClick() {
		
		return false;
	}

	@Override
	protected void montarGrid() {
		// TODO Auto-generated method stub
		
	}

	
	private Integer obterIDLayout() 
	{
		Integer idLayoutLocal = null;
		HiddenField fieldIdLayout = (HiddenField) itenAtivoPesquisaForm.getField("idLayout");
		if (select.getValue() != null && !"".equals(select.getValue())) {
			idLayoutLocal = Integer.valueOf(select.getValue());

		}else if (fieldIdLayout!=null && fieldIdLayout.getValue() !=null  )
		{
			idLayoutLocal = (Integer) fieldIdLayout.getValueObject();
		}

		return idLayoutLocal;			
	}

	private Collection<ConfigInfoPesquisaVO> montarListaConfigValores() 
	{
		Collection<ConfigInfoPesquisaVO> lstConfValores = new ArrayList<ConfigInfoPesquisaVO>();

		FieldSet configs = (FieldSet) itenAtivoPesquisaForm.getControl("configuracoes");

		if (configs != null) {

			lstConfValores = informacoesUtil.montarConfInfoPesquisa(configs);
		}

		return lstConfValores;
	}

	
	private void montarTabela(List<ItensAtivos> listaItensAtivos) 
	{

		mapListGridView = new HashMap<Integer,ListaGridView>();
		Ativos ativos;
		
		ListaGridView estLstView; 
		Integer soma=0;
		for (Iterator<ItensAtivos> iterator = listaItensAtivos.iterator(); iterator.hasNext();) 
		{
			final ItensAtivos itemAtivos = iterator.next();
			
			ativos =itemAtivos.getAtivo();
			if (mapListGridView.containsKey(ativos.getLayout().getId()))
			{
				estLstView =mapListGridView.get(ativos.getLayout().getId());				
				
			}else
			{	
			
				estLstView = new ListaGridView();
				estLstView.setTitulo(ativos.getLayout().getNome());
				estLstView.setIdLayout(Integer.valueOf(ativos.getLayout().getId()));				
				estLstView.setNomes(new  LinkedHashMap<String, String>());
				estLstView.setLinhasValores(new ArrayList<Map<String,String>>());
				mapListGridView.put(ativos.getLayout().getId(), estLstView);	
				estLstView.setTotal(new Integer(0));
				if (exibirSelecao!=null && exibirSelecao)
				{
					estLstView.getNomes().put("selecionar",  "Selecionar");
				}
				
				//nomes .....
				Collection<Configuracoes> lstConfigs = ativos.getLayout().getConfiguracoes();
				
				for (Iterator iterator2 = lstConfigs.iterator(); iterator2.hasNext();) 
				{
					Configuracoes configuracoes = (Configuracoes) iterator2.next();
					if (configuracoes.getPesquisa().equals(Configuracoes.PESQ_SIM))
					{
						estLstView.getNomes().put(String.valueOf(configuracoes.getId()), configuracoes.getNomecampo());
					}
					
					
				}	
				
				estLstView.getNomes().put("identificacao",  "Identifica&ccedil;&atilde;o");
				estLstView.getNomes().put("local",  "Local");
			
				
				
			}
			
			//colocar os valores
		    Collection<Informacoes>	 lstInfos = ativos.getInformacoes();
		    Map<String, String> mapLinha = new LinkedHashMap<String, String>();
		    estLstView.getLinhasValores().add(mapLinha);
		    for (Iterator iterator2 = lstInfos.iterator(); iterator2.hasNext();) {
				Informacoes informacoes = (Informacoes) iterator2.next();
				
	            Configuracoes config = layoutGerencia.pesquisarConfiguracao(informacoes.getConfiguracoesId());
	            if (config.getPesquisa().equals(Configuracoes.PESQ_SIM))
	            {
	            	String strValor = formataValor(config,informacoes);
	            	String key = String.valueOf(informacoes.getConfiguracoesId());
					
	            	// Para os casos dos campos multivalorados
	            	if (mapLinha.containsKey(key)) {
	            		strValor = mapLinha.get(key) + "|" + strValor; 
	            	}
	            	
					mapLinha.put(key, strValor);
	            	
	            }			
							
			}
		 
		    String lupa = "";
		   if ( itemAtivos.getLocal()!=null)
		   {
			        lupa = "<a href='javascript:void(0)' onclick=\"window.open('localizacao.htm?latitude="
					+ itemAtivos.getLocal().getLatitude() + "&longitude="	+ itemAtivos.getLocal().getLongitude() + "&nome=" 
					+ itemAtivos.getAtivo().getLayout().getNome()
					+ "&identificacao=" 
					+ itemAtivos.getIdentificacao() 
					+ "&local=" + itemAtivos.getLocal().getNome() 
					+ "','Teste','resizable=yes,width=640,height=480');\" > <img src='resources/images/lupa.png' /> </a> ";
		   }
			
		    
		     mapLinha.put("local", lupa);
		    
		    
		    mapLinha.put("identificacao", itemAtivos.getIdentificacao());
		    if (exibirSelecaoMultiplo!=null && exibirSelecaoMultiplo )
		    {
		    	  mapLinha.put("selecionar", "<input type='checkbox' name='selectItem' value='" + itemAtivos.getItensAtivosId() + "'>");
		    	  
		    }else if (exibirSelecao!=null && exibirSelecao )
		    {
		    	  mapLinha.put("selecionar", "<input type='radio' name='selectItem' value='" + itemAtivos.getItensAtivosId() + "'>");
		    }
		  
		    estLstView.setTotal(estLstView.getTotal() + ativos.getQtdItens());
	
		}	
		
	}

	private Ativos montarAtivosPesquisa() 
	{
		Ativos ativos = new Ativos();
		ativos.setId(1);//fake para testes
	    FieldSet atvDados = (FieldSet) itenAtivoPesquisaForm.getControl("itemAtivosDados"); 
	    
		Layout layoutId = new Layout();	
		if (select.getValue() != null && !"".equals(select.getValue())) 
		{
			layoutId.setId(Integer.valueOf(select.getValue()));
			ativos.setLayout(layoutId);
		}
		
		return ativos;
		
		
	}
	
	private String formataValor(Configuracoes config, Informacoes info)
	{
		TipoCampoView tpCampo = (TipoCampoView) appContext.getBean(String.valueOf(config.getTipoCampo().getId()));
		String valor =  tpCampo.formataInformacao(config, info);
		
		return valor;
	}

	
}

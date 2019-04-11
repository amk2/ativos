package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.click.ActionResult;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.HiddenList;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.AtivosNo;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.web.exception.ValidacaoCampoException;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;
import com.projtec.slingcontrol.web.tipocampo.TipoCampoView;


@Component
public class AtivosPage extends FormPage
{
	private static final long serialVersionUID = 1L;
	private static final String NOME_LAYOUT = "Ativos";
	private static final String LISTA = "lista.AtivosPage";
	private final Logger logger = LoggerFactory.getLogger(AtivosPage.class);

	//private static final Integer IDATIVO = 6;
	
	@Bindable
	public String title = getMessage("titulo");

	@Bindable
	protected Form ativosForm = new Form("ativosForm");

	private Select select = new Select("layout", true);

	@Bindable
	protected ActionLink editLink = new ActionLink("edit", "Editar", this, "onEditClick");

	@Bindable
	protected ActionLink deleteLink = new ActionLink("delete", "Remover", this,	"onDeleteClick");
	
	@Bindable
	private Map<Integer,ListaGridView> mapListGridView = new HashMap<Integer,ListaGridView>();
	
	@Autowired
	private ApplicationContext appContext;

	@Bindable
	protected String mesg;

	@Resource(name = "ativosGerencia")
	protected AtivosGerencia ativosGerencia;

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;

	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;
	
	@Resource(name = "ativosUtil")
	protected AtivosUtil ativosUtil;

	// Constructor ------------------------------------------------------------

	public AtivosPage() 
	{
		
	}
	
	@Override
	public boolean permitidoSalvar() {
	
		return Secure.isPermitted("botoes:ativos:salvar");
	}
	
	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:ativos:pesquisar");
	}
	
	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:ativos:limpar");
	}
	
	@Override
	public void onInit() 
	{
		initAtivos();
		
		test = true;
		super.onInit();
		
	}

	private void initAtivos() 
	{
		HiddenField idField = new HiddenField("id", Integer.class);
		ativosForm.add(idField);
		
		FieldSet ativosDados = new FieldSet("ativoDados");
		
		select.setRequired(false);
		select.setLabel(select.getLabel() + " <span class='required'>*</span>");
		select.setAttribute("class", "validate[required]");

		select.getOptionList().clear();

		select.add(new Option("", "-- Selecione --"));
		select.addAll(obterLayouts());
		select.setAttribute("class", "validate[required]");
		ativosDados.add(select);

		ativosForm.add(ativosDados);

		select.setAttribute("onchange", "Click.submit(ativosForm, false)");

		HiddenList hiddenListAtivos = new HiddenList("ativosDependencia"); 
		hiddenListAtivos.addValue("0");
		
		ativosForm.add(hiddenListAtivos);
		 
//		 montarBotoes();
		// montarBotoesPesquisa();
		addControl(ativosForm);
		
		ClickUtils.bind(ativosForm);
		
		montarBotoes(habilitaBotaoSalvar(), true, true);

		obterConfiguracoes(null);

	}

	protected void montarGrid() {
		Column column;

		column = new Column("layout.nome");
		grid.addColumn(column);

		column = new Column("action");
		column.setWidth("120px;");
		column.setSortable(false);
		ActionLink[] links = new ActionLink[] { editLink, deleteLink };
		column.setDecorator(new LinkDecorator(grid, links, "id"));
		grid.addColumn(column);

		deleteLink.setAttribute("onclick","return window.confirm('Deseja realmente remover ?');");

		grid.setDataProvider(new DataProvider<Ativos>() 
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public List<Ativos> getData() 
			{
				return obterListaSessao();
			}
		});

	}

	// metodos para lista sessao ///metodos para manibular lista
	private void listaSessao() 
	{
		List lst = (List) getContext().getSession().getAttribute(LISTA);
		if (lst == null) 
		{
			lst = new ArrayList();
			getContext().getSession().setAttribute(LISTA, lst);
		}
	}

	private List obterListaSessao() 
	{
		List lst = (List) getContext().getSession().getAttribute(LISTA);
		return lst;
	}

	public void removerLista(Integer id) 
	{
		for (Iterator iterator = obterListaSessao().iterator(); iterator.hasNext();) 
		{
			Ativos ativos = (Ativos) iterator.next();
			if (ativos.getId().equals(id)) 
			{
				iterator.remove();

			}
		}
	}

	public void adicionarLista(Ativos l) {
		obterListaSessao().add(0, l);
	}

	public void novaLista(List lstNova) 
	{
		List lstAntiga = (List) getContext().getSession().getAttribute(LISTA);
		lstAntiga = null;
		getContext().getSession().setAttribute(LISTA, lstNova);
	}

	// fim metodos lista

	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) 
	{
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);	
			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, ativosForm);
		}
	}

	private Collection<Option> obterLayouts() {
		List<Option> optionList = new ArrayList<Option>();
		Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDATIVO);

		for (Layout layout : lstLayout) 
		{
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}
		
		return optionList;
	}

	private Ativos montarAtivos()  
	{
		Ativos ativos = new Ativos();
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		Layout layoutId = new Layout();

		String strId = ativosForm.getFieldValue("id");
		Integer id;
		if (strId == null) {
			id = 0;
		} else if (strId.length() <= 0) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}

		ativos.setId(id);

		FieldSet atvDados = (FieldSet) ativosForm.getControl("ativoDados"); 
		
		if (select.getValue() != null && !"".equals(select.getValue())) 
		{
			layoutId.setId(Integer.valueOf(select.getValue()));
		}
		
		ativos.setLayout(layoutId);

		FieldSet configs = (FieldSet) ativosForm.getControl("configuracoes");

		logger.debug("Config FildSet :" + configs );
		if (configs != null) 
		{
			lstInfos = informacoesUtil.montarInfomacoes(configs);
			ativos.setInformacoes(lstInfos);
		}
		 HiddenList   lstDepAtivos = (HiddenList) ativosForm.getControl("ativosDependencia");
		 
		 if(lstDepAtivos!=null)
		 {
			 List<String> lstValores = lstDepAtivos.getValues(); 
			 Set<Integer> lstDependencia = new HashSet<Integer>();
			 lstValores.remove("0");//remover o zero fake...
			 for (Iterator<String> iterator = lstValores.iterator(); iterator.hasNext();)
			 {
				 String strValor = iterator.next();
				lstDependencia.add(Integer.valueOf(strValor));			
			 }		 
			 ativos.setDependencias(lstDependencia);
			 
		 }
		 return ativos;
	}

	// Event Handlers ---------------------------------------------------------
	public boolean onSalvarClick() {
		
		try {
		
			imageFields();
			
			FieldSet configs = (FieldSet) ativosForm.getControl("configuracoes");
			validaCampos(configs);
	
			Ativos ativos = montarAtivos();
	
			if (ativos.getId() == 0) 
			{
	
				Ativos at = ativosGerencia.incluir(ativos);
				Ativos t = ativosGerencia.pesquisarId(at.getId());
//				adicionarLista(t);
	
			} else {
//				removerLista(ativos.getId());
				ativosGerencia.alterar(ativos);
				Ativos t = ativosGerencia.pesquisarId(ativos.getId());
//				adicionarLista(t);
	
			}
	
			// mensagem
			ativosForm.clearValues();
			limparListaDependencia();
			
			mensagemSucesso = "O Ativo foi salvo com sucesso ";
			ativosForm.remove(configs);
			
			return true;
			
		}
		catch (ValidacaoCampoException e) {
			
			mensagemErro = e.getMessage();
			return false;
			
		}
		
	}

	public boolean onDeleteClick() 
	{
		Integer id = deleteLink.getValueInteger();
		ativosGerencia.excluir(id);
//		removerLista(id);
		// mesg = " Ativo removido  com sucesso";
		mensagemSucesso = " Ativo removido com sucesso";
		return true;
	}

	public boolean onEditClick() {
		
		Integer id = editLink.getValueInteger();
		Ativos ativo = ativosGerencia.pesquisarId(id);
		
		if (ativo != null) 
		{
			// id
			HiddenField idField = (HiddenField) ativosForm.getField("id");
			idField.setValueObject(ativo.getId());

			// layout
			select.setValue(String.valueOf(ativo.getLayout().getId()));
			
			//dependencias
			 HiddenList   fieldLstDepAtivos =  new HiddenList("ativosDependenciaEdit");
			 fieldLstDepAtivos.setId("ativosDependenciaEdit");
			 fieldLstDepAtivos.setName("ativosDependenciaEdit");
			
			
			 Collection<Integer> lstAtvDependencia = ativo.getDependencias();
			 Integer qtd = 0 ; 
			 for (Iterator<Integer> iterator = lstAtvDependencia.iterator(); iterator.hasNext();) 
			 {
				Integer idAtvDep =  iterator.next();
				fieldLstDepAtivos.addValue(String.valueOf(idAtvDep));
				qtd++;
			 }
			 HiddenField ativosDependenciaEditQTD = new HiddenField("ativosDependenciaEditQTD" , Integer.class);
			 ativosDependenciaEditQTD.setId("ativosDependenciaEditQTD");
			 ativosDependenciaEditQTD.setValueObject(qtd);
			 ativosForm.add(ativosDependenciaEditQTD);
			 ativosForm.add(fieldLstDepAtivos);
		
			 obterConfiguracoes(ativo.getMapInformacoes());
			
			 trataCampo(ativosForm.getField("Salvar"), true);
			
		}
		return true;
	}

	public boolean onPesquisaClick() {
		ativosForm.clearErrors();
		if (StringUtils.isEmpty(select.getValue())) {
			mensagemErro = "Selecionar um Layout para pesquisa";
		} else {
			Ativos ativo = montarAtivos();
			List<Ativos> lstAtivos=null;
			if(ativo.getLayout()!=null && ativo.getLayout().getId() > 0 ) {
				lstAtivos = ativosGerencia.pesquisar(ativo);
			}
			if (lstAtivos!=null && lstAtivos.size() > 0) {
				montarTabela(lstAtivos);
				mesg = " Pesquisar: Total: " + lstAtivos.size() + " Ativos";
			} else {
				mensagemErro = "N&atilde;o foram encontrados Ativos com os dados informados";
				initAtivos();
			}
		}
		return true;
		
	}


	public boolean onCancelarClick() 
	{
		ativosForm.clearValues();
		ativosForm.clearErrors();
		limparListaDependencia();
		mapListGridView = new HashMap<Integer,ListaGridView>();
		
		FieldSet configs = (FieldSet) ativosForm.getControl("configuracoes");
		if(configs!=null)
		{
			ativosForm.remove(configs);
		}
		
		trataCampo(ativosForm.getField("Salvar"), false);
		
		return true;
	}
	
	
	
    public ActionResult obterAtivos() 
    {

    	Context context = getContext();
       
        String ids[] = context.getRequestParameterValues("id");
        Ativos atv; 
        String strListaAtivo = "  { \"listaAtivos\": [" ; 
        for (int i = 0; i < ids.length; i++)
		{
			atv = ativosGerencia.pesquisarId(Integer.valueOf(ids[i]));
			strListaAtivo = strListaAtivo + "{\"id\": \"" + atv.getId() + "\" , \"nome\": \"" + atv.getLayout().getNome() + "\"}";
			if((ids.length-1) > (i))
			{
			   strListaAtivo = strListaAtivo + "," ;
			}
		}
        
        strListaAtivo =strListaAtivo + "] }";
        
        return new ActionResult(strListaAtivo, ActionResult.JSON);
    }


	@Override
	public Form getForm() 
	{
		return ativosForm;
	}
	
	public Ativos getFilhos(Ativos ativo)
	{
		List<AtivosNo> lstPaiFilho = new ArrayList<AtivosNo>();
		ativo.setLstFilhos(ativosGerencia.getFilhos(ativo.getId(),lstPaiFilho)); 
		return ativo ;
	}
	
	
    /*
	private void montarBotoesPesquisa()
	{	
		
		
		pesquisar.setImageSrc("/resources/images/btnPesquisar.png");
	
		pesquisar.setId("pesquisarAtivosBt");
		pesquisar.setDisabled(!permitidoPesquisar());	
		
		pesquisar.addBehavior(new DefaultAjaxBehavior() {
			
			@Override
			public ActionResult onAction(org.apache.click.Control source) {
				
				Ativos atv = montarAtivos();
				
                List<Ativos> listaAtivos = ativosGerencia.pesquisar(atv);
                ActionResult result;
                if (listaAtivos!=null && listaAtivos.size() > 0 )
                {
                	 result = new ActionResult(ativosUtil.ativosDinamicoJSON(listaAtivos), ActionResult.JSON);
                	 
                }else
                {
                	 result = new ActionResult("{\"mesg\":0}", ActionResult.JSON);
                }
               
                return result;              
                
			};

           
        });	
		getForm().add(pesquisar);
		
		ControlRegistry.registerAjaxTarget(ativosForm);	
		
	}*/
	
	

	
	private void montarTabela(List<Ativos> lstAtivos) {

		mapListGridView = new HashMap<Integer,ListaGridView>();
		
		ListaGridView estLstView; 
		Integer soma=0;
		for (Iterator iterator = lstAtivos.iterator(); iterator.hasNext();) 
		{
			final Ativos ativos = (Ativos) iterator.next();
			
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
				estLstView.getNomes().put("dependencia",  "Depend&ecirc;ncia");
				estLstView.getNomes().put("acoes",  "A&ccedil;&otilde;es");
				
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
		    String linkAcoes = "<a href='#' onclick='editarAtivo(" + ativos.getId() + ");'>Editar</a>" +
		                       "   | " +
		                       " <a href='#' onclick='removerAtivo("+ ativos.getId() +")'>Remover</a>";
		    
		    mapLinha.put("acoes", linkAcoes);
		    
		    String linkEstrutura = "&nbsp;";
		    if (ativos.getLstFilhos() !=null && ativos.getLstFilhos().size() > 0 )
		    {
		    	linkEstrutura = linkEstrutura + " <a href='#' onclick='exibirEstrutura("+ ativos.getId() +")'>[+]</a>";
		    }
		   // linkEstrutura = linkEstrutura + " <a href='#' onclick='exibirEstrutura("+ ativos.getId() +")'>[+]</a>";
		    mapLinha.put("dependencia", linkEstrutura);
		    estLstView.setTotal(estLstView.getTotal() + ativos.getQtdItens());
	
		}	
		
	}

	
	private void limparListaDependencia()
	{
		 HiddenList   lstDepAtivos = (HiddenList) ativosForm.getControl("ativosDependencia");
		 lstDepAtivos.setValueObject(new ArrayList<String>());
		
	}
	
	private String formataValor(Configuracoes config, Informacoes info)
	{
		TipoCampoView tpCampo = (TipoCampoView) appContext.getBean(String.valueOf(config.getTipoCampo().getId()));
		String valor =  tpCampo.formataInformacao(config, info);
		
		return valor;
	}
	
	private boolean habilitaBotaoSalvar() {
		
		if (!StringUtils.isEmpty(select.getValue()) || editLink.getValueInteger() != null)
			return true;
		else
			return false;
		
	}

}

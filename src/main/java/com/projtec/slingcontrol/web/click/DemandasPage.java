package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.ActionResult;
import org.apache.click.control.ActionButton;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Option;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Select;
import org.apache.click.control.TextField;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.HiddenList;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.gerencia.LocaisGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.DemandasItemAtivo;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.modelo.StatusDemanda;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;

@Component
public class DemandasPage extends FormPage {

	private static final String DEM_ENTRADA = "DEM_ENTRADA";
	private static final String DEM_MOV_ATV = "DEM_MOV_ATV";
	private static final long serialVersionUID = -7795645000085052974L;
	public static final String LISTA = "lista.demanda";
	private static final String NOME_LAYOUT = "Demandas";

	
//	private static final String CHAVE_DENPENDENCIADEMANDAS = "demandasForm.dependenciaID";
	
	
	private TextField statusDemandaField = ControlesHTML
			.textField("statusDemanda");

	
	@Bindable
	private Demandas demandaDependencia;
	
	@Bindable
	private Integer IdDemanDep;
	
	
	@Bindable
	public String title = getMessage("titulo");

	@Bindable
	public String STATUS_NM_FECHADO = getMessage("statusFechado");

	@Bindable
	public String STATUS_NM_ABERTO = getMessage("statusAberto");

	@Bindable
	protected Form demandasForm = new Form("demandasForm");

	@Bindable
	protected Form ativosField = new Form("ativosField");
	

	@Bindable
	protected  Collection<DemandasItemAtivo>  listaItensDemanda; 

	private Select select = new Select("layout", true);

	@Bindable
	protected ActionLink editLink = new ActionLink("edit", "Editar", this,
			"onEditClick");

	@Bindable
	protected ActionLink dependenciaLink = new ActionLink("dependenciaLink",
			" + ", this, "onDependenciaClick");

	@Bindable
	protected ActionLink deleteLink = new ActionLink("delete", "Remover", this,
			"onDeleteClick");

	@Bindable
	protected ActionButton limparLink = new ActionButton("limpar", "Cancelar",
			this, "onLimparClick");

	@Bindable
	protected String mesg;
	
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

	@Resource(name = "locaisGerencia")
	protected LocaisGerencia locaisGerencia;
	
	// Constructor ------------------------------------------------------------

	public DemandasPage() {
		
		// initDemandas();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}

	@Override
	public boolean permitidoSalvar() {

		return Secure.isPermitted("botoes:demandas:salvar");
	}

	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:demandas:pesquisar");
	}

	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:demandas:limpar");
	}

	@Override
	public void onInit() {
		
		super.onInit();
		initDemandas();
		
		//getContext().getSession().getAttribute(CHAVE_DENPENDENCIADEMANDAS);
		Integer idDependencia = (Integer) getContext().getSession().getAttribute("CHAVE_DENPENDENCIADEMANDAS");
		if (idDependencia!=null)
		{
			 demandaDependencia = demandaGerencia.pesquisarID(idDependencia);
		}
		
		
	
	}


	@SuppressWarnings("serial")
	private void initDemandas() {

		FieldSet demandasDados = new FieldSet("DemandasDados");

		HiddenField idField = new HiddenField("id", Integer.class);
		demandasForm.add(idField);
		
		//campos para lista de itens ativos
		HiddenList hiddenListItensDemanda= new HiddenList("itensDemanda"); 
		hiddenListItensDemanda.setId("itensDemanda");
		hiddenListItensDemanda.addValue("0");
		demandasForm.add(hiddenListItensDemanda);

		
		HiddenField dependenciaID = new HiddenField("dependenciaID",
				String.class);
		demandasDados.add(dependenciaID);

		Select selectTipoDemanda = new Select("tipoDemanda");
		selectTipoDemanda.setRequired(false);
		selectTipoDemanda.setLabel(selectTipoDemanda.getLabel()
				+ " <span class='required'>*</span>");
		selectTipoDemanda.setAttribute("class", "validate[required]");

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
		HiddenField statusDemandaFieldID = new HiddenField("statusDemandaID",
				Integer.class);

		statusDemandaFieldID.setValueObject(StatusDemanda.ID_ABERTO);
		demandasDados.add(statusDemandaFieldID);

		statusDemandaField.setReadonly(true);
		statusDemandaField.setRequired(false);
		statusDemandaField.setValue(StatusDemanda.ABERTO.toString());
		demandasDados.add(statusDemandaField);

		// radioGroup.add(new Radio("STD", "Standard "));
	
		//tipo de movimentacao
		RadioGroup tipoMovimentacao = new RadioGroup("tipoMovimentacao");
		tipoMovimentacao.add(new Radio(DEM_MOV_ATV, getMessage("movAtivo")));
		tipoMovimentacao.add(new Radio(DEM_ENTRADA,	getMessage("entradaAtivo")));
		tipoMovimentacao.setVerticalLayout(true);
		demandasDados.add(tipoMovimentacao);
		

		select.add(new Option("", "-- Selecione --"));
		select.addAll(obterLayouts());
		demandasDados.add(select);

		demandasForm.add(demandasDados);

		select.setAttribute("onchange", "Click.submit(demandasForm, false)");
		select.setRequired(false);
		select.setLabel(select.getLabel() + " <span class='required'>*</span>");
		select.setAttribute("class", "validate[required]");

		montarBotoes();
		addControl(demandasForm);

		Select selectLocais = new Select("locais");
		selectLocais.setRequired(true);
		selectLocais.setId("ComboBoxLocais");
		selectLocais.setSize(1);
		selectLocais.setLabel("");
		selectLocais.setStyle("display", "none");

		selectLocais.setDataProvider(new DataProvider<Option>() {

			@Override
			public Iterable<Option> getData() {
				List<Option> optionList = new ArrayList<Option>();
				Collection<Local> lstLocais = locaisGerencia.obterTodos();
				for (Iterator<Local> iterator = lstLocais.iterator(); iterator
						.hasNext();) {
					Local local = iterator.next();
					optionList.add(new Option(local.getId(), local.getNome()));
				}
				return optionList;
			}

		});

		demandasDados.add(selectLocais);

		// Bind the form field request values
		ClickUtils.bind(demandasForm);

		obterConfiguracoes(null);
		adicionarItens();

	}

	public Form getForm() {
		return demandasForm;
	}



	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) {
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);	

			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, demandasForm);

		}
	}

	
	private Demandas montarDemandas() {

		Demandas demandas = new Demandas();
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		TipoDemanda tipoDemandaId = new TipoDemanda();
		Layout layoutId = new Layout();

		String strId = demandasForm.getFieldValue("id");
		Integer id;
		if (strId == null) {
			id = 0;
		} else if (strId.length() <= 0) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}

		demandas.setId(id);

		FieldSet demandasDados = (FieldSet) demandasForm
				.getControl("DemandasDados");

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
		Field fieldStatusID = demandasDados.getField("statusDemandaID");

		if (fieldStatus.getValue() != null
				&& !"".equals(fieldStatus.getValue())) {
			StatusDemanda statusDemanda = new StatusDemanda();
			statusDemanda.setId(Integer.valueOf(fieldStatusID.getValue()));
			demandas.setStatusDemanda(statusDemanda);
			// demandas.setStatus(fieldStatus.getValue());
		}

		
		// dependencia
		//Field fieldDemandaRef = demandasDados.getField("dependenciaID");
		//if (fieldDemandaRef.getValue() != null
				//&& !"".equals(fieldDemandaRef.getValue())) {
			//demandas.setIdPai(Integer.valueOf(fieldDemandaRef.getValue()));
		//}

		//coloquei isso pois o codigo acima nao estava funcionando. Ja esta na sessao a demanda dependente.
		Integer idDependencia = (Integer) getContext().getSession().getAttribute("CHAVE_DENPENDENCIADEMANDAS");
		demandas.setIdPai(idDependencia);
		
		Collection<Field> fields;
		
		FieldSet configs = (FieldSet) demandasForm.getControl("configuracoes");
		// configs.add(imgField);
		// System.out.println("TESTE: " + configs.getControl("imgName"));
		if (configs != null) {
			lstInfos = informacoesUtil.montarInfomacoes(configs);
			demandas.setInformacoesDemanda(lstInfos);
		}
		
		
		//itens de ativos lista ....... 
		demandas.getLstDemandasItensAtivos();
		Field tpMovimentacao =  demandasDados.getField("tipoMovimentacao");
		String qualTipoMovimentacao = tpMovimentacao.getValue();
		List<DemandasItemAtivo> lstDemandasItens = new ArrayList<DemandasItemAtivo>();
		
		if(qualTipoMovimentacao.equals(DEM_ENTRADA))
		{
			System.out.println("********************************************ENTRADA____________");
			//[ID_ATIVO;IDENTIFICADOR;ID_LOCAL_DESTINO]
			HiddenList  hiddenItems = (HiddenList) demandasForm.getControl("itemAtivoEntrada");
			List<String> valores = hiddenItems.getValues();
			String arrayIDs[]; 
			DemandasItemAtivo dmdItem ;
			ItensAtivos itensAtivos;
			
			for (String str : valores) 
			{
				arrayIDs = str.split(";");
				if(arrayIDs.length >= 2)
				{
					dmdItem  = new DemandasItemAtivo();
					itensAtivos = new ItensAtivos();
					itensAtivos.setIdentificacao(arrayIDs[1]);
					Local local =new Local();
					local.setId(Integer.valueOf(arrayIDs[2]));
					itensAtivos.setLocal(local);
					Ativos ativo =new Ativos();
					ativo.setId(Integer.valueOf(arrayIDs[0]));
					itensAtivos.setAtivo(ativo);
					dmdItem.setItemAtivos(itensAtivos);
					dmdItem.setLocalDestino(local);
					lstDemandasItens.add(dmdItem);
				}
				
				
			}
	 
			
			
		}else if (qualTipoMovimentacao.equals(DEM_MOV_ATV))
		{
			System.out.println("********************************************MOVIMENTO ________");
			
			//[ID_ITEM;ID_LOCAL_ORIGEM;ID_LOCAL_DESTINO]
			HiddenList  hiddenItems = (HiddenList) demandasForm.getControl("itemAtivosMovAtivo");
			List<String> valores = hiddenItems.getValues();
			String arrayIDs[]; 
			DemandasItemAtivo dmdItem ;
			ItensAtivos itensAtivos;
			
			for (String str : valores) 
			{
				arrayIDs = str.split(";");
				if(arrayIDs.length >= 2)
				{

					/*
					id - 0
					identificador - 1
					(id__local) local atual - 2
					destino - 3
					*/

					
					dmdItem  = new DemandasItemAtivo();
					itensAtivos = new ItensAtivos();
					itensAtivos.setItensAtivosId((Integer.valueOf(arrayIDs[0])));
					
					//coloquei isto pois o que retorno na posi��o 1 � a identifica��o "RFID" da demanda. Estava ocorrendo
					//um erro pois estava passando string para esta posi��o 1 que estava como inteiro... quando na verdade � string.
				//	itensAtivos.setIdentificacao((String.valueOf(arrayIDs[1])));

					
					dmdItem.setItemAtivos(itensAtivos);
					Local localOrigem =new Local();
					localOrigem.setId(Integer.valueOf(arrayIDs[1]));
					dmdItem.setLocalOrigem(localOrigem);
					Local localDestino =new Local();
					localDestino.setId(Integer.valueOf(arrayIDs[2]));
					dmdItem.setLocalDestino(localDestino);				
					lstDemandasItens.add(dmdItem);
				}
			}
	
			
			
		}
		
		demandas.setLstDemandasItensAtivos(lstDemandasItens);

		return demandas;

	}

	private Collection<Option> obterLayouts() {

		List<Option> optionList = new ArrayList<Option>();
		//Collection<Layout> lstLayout = layoutGerencia.obterTodos(); // demandaGerencia.obterTodosNomesLayouts(NOME_LAYOUT);
		Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDDEMANDA);
		
		for (Layout layout : lstLayout) {
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}
		return optionList;
	}

	public ActionResult onAdicionarDependencia() {
		
		demandaDependencia = demandaGerencia.pesquisarID(IdDemanDep);		
		getContext().getSession().setAttribute("CHAVE_DENPENDENCIADEMANDAS", IdDemanDep);

		// Return an action result containing the message
		return new ActionResult("OK", ActionResult.HTML);
	}

	// Event Handlers ---------------------------------------------------------
	public boolean onSalvarClick() {
		
		getContext().getSession().setAttribute("imgField", getContext().getRequestParameter("imgField"));

		Demandas demandas = montarDemandas();

//		if (!demandasForm.isValid())
//			return false;

		if (demandas.getId() == 0) {
			Demandas d = demandaGerencia.incluir(demandas);
			//d = demandaGerencia.pesquisarID(d.getId());
			DemandasDetalhePage nextPage = getContext().createPage(DemandasDetalhePage.class);
			nextPage.demanda = d.getId(); // id 	
			getContext().getSession().removeAttribute("CHAVE_DENPENDENCIADEMANDAS");
			setForward(nextPage);

		} else {
		
			demandaGerencia.alterar(demandas);
			Demandas dAlt = demandaGerencia.pesquisarID(demandas.getId());

			demandasForm.clearValues();
	  
	  		//remove os controles da configurac�o
			FieldSet configs = (FieldSet) demandasForm.getControl("configuracoes");
			demandasForm.remove(configs);

			Map<String, String> params = new HashMap<String, String>();
			
			//vai exibir no HTML onde estiver a variavel $mesgAtualizarDaURL
			getContext().getSession().removeAttribute("CHAVE_DENPENDENCIADEMANDAS");
			params.put("mesgAtualizarparaURL", " A Demanda foi atualizada com sucesso");
			setRedirect(DemandasPesquisaPage.class, params);
		}

	
		return true;
	}

	public boolean onDependenciaClick() {

		Integer id = dependenciaLink.getValueInteger();
		Demandas demandas = demandaGerencia.pesquisarID(id);
		if (demandas != null) {

			// retorna tipo de demanda
			HiddenField idField = (HiddenField) demandasForm.getField("id");
			idField.setValueObject(demandas.getId());
			FieldSet demandasDados = (FieldSet) demandasForm
					.getControl("DemandasDados");
			Field fieldTipoDemanda = demandasDados.getField("tipoDemanda");
			fieldTipoDemanda.setValue(String.valueOf(demandas.getTipoDemanda()
					.getId()));

			// retorna tipo layout de demanda
			select.setValue(String.valueOf(demandas.getLayout().getId()));
			obterConfiguracoes(demandas.getMapInformacoesDemanda());

		}
		return true;
	}

	public boolean onDeleteClick() 
	{
		Integer id = deleteLink.getValueInteger();
		demandaGerencia.excluir(id);
		mesg = getMessage("mesgRemover");

		setForward(DemandasPesquisaPage.class);		
		
		return true;
	}

	public boolean onCancelarClick() {
		
		//demandasForm.clearValues();
		demandasForm.clearErrors();
		FieldSet configs = (FieldSet) demandasForm.getControl("configuracoes");
		if(configs!=null)
		{
			demandasForm.remove(configs);
			
		}
		getContext().getSession().removeAttribute("CHAVE_DENPENDENCIADEMANDAS");
		demandaDependencia = null;

		setRedirect(DemandasPesquisaPage.class);

		return true;
	}

	public boolean onEditClick() {
		Integer id = editLink.getValueInteger();
		Demandas demandas = demandaGerencia.pesquisarID(id);
		if (demandas != null) {

			// id
			HiddenField idField = (HiddenField) demandasForm.getField("id");
			idField.setValueObject(demandas.getId());

			FieldSet demandasDados = (FieldSet) demandasForm.getControl("DemandasDados");

			Field fieldTipoDemanda = demandasDados.getField("tipoDemanda");

			//tentar mostrar o tipo da movimentação selecionado
			Field tpMovimentacao =  demandasDados.getField("tipoMovimentacao");

			fieldTipoDemanda.setValue(String.valueOf(demandas.getTipoDemanda()
					.getId()));

			// dependencia
			Field fieldDemandaRef = demandasDados.getField("dependenciaID");
			fieldDemandaRef.setValue(String.valueOf(demandas.getIdPai()));
			if (demandas.getIdPai() != null & demandas.getIdPai() != 0) {
				HiddenField nomeDemandaReferencia = new HiddenField(
						"nomeDemandaReferencia", String.class);
				nomeDemandaReferencia.setId("nomeDemandaReferencia");
				Demandas demandaRef = demandaGerencia.pesquisarID(demandas
						.getIdPai());
				nomeDemandaReferencia.setValue(demandaRef.getId()
						+ demandaRef.getLayout().getNome()
						+ demandaRef.getTipoDemanda().getDescricao());
				demandasDados.add(nomeDemandaReferencia);
			}
			//----------------------------------------------------------------
			/*			
			coloquei para fazer um teste pois 
			na edi��o n�o tem vindo a dependencia da demanda depois que eu comentei 
			a declara��o final
			*/
			demandaDependencia = demandaGerencia.pesquisarID(demandas.getIdPai());
			getContext().getSession().setAttribute("CHAVE_DENPENDENCIADEMANDAS", demandas.getIdPai());
			//--------------------------------------------------------------

			// status
			Field fieldStatus = demandasDados.getField("statusDemanda");
			// fieldStatus.setValue(demandas.getStatus());
			// layout
			select.setValue(String.valueOf(demandas.getLayout().getId()));

			obterConfiguracoes(demandas.getMapInformacoesDemanda());

		}
		return true;
	}
	
	
	public void  adicionarItens() 
	{
		
		   Collection<DemandasItemAtivo> lstViewItensDemanda; 
		   
		   lstViewItensDemanda = montarItensDemanda(); 	   
		     
		   listaItensDemanda = lstViewItensDemanda;
		   HiddenList  fieldItensDemanda =  (HiddenList) demandasForm.getField("itensDemanda");
		   if (fieldItensDemanda!=null)
		   {
			   for (DemandasItemAtivo demandasItemAtivo : lstViewItensDemanda) {
				   
					  String valores = String.valueOf(demandasItemAtivo.getItemAtivos().getItensAtivosId()) 
					                  + ";"
					                  + ( demandasItemAtivo.getLocalDestino()!=null 
					                    &&  demandasItemAtivo.getLocalOrigem().getId() >0?String.valueOf(demandasItemAtivo.getLocalOrigem().getId()):"0")
					                  + ";"
					                  +  ( demandasItemAtivo.getLocalDestino()!=null 
							                    &&  demandasItemAtivo.getLocalDestino().getId() >0?String.valueOf(demandasItemAtivo.getLocalDestino().getId()):"0");
					   fieldItensDemanda.addValue(valores);
					   
			    }
			   
		   }
	
	}

	private Collection<DemandasItemAtivo> montarItensDemanda() {
		
		List<DemandasItemAtivo> demandasItemAtivoList = new ArrayList<DemandasItemAtivo>();
		//[ID_ITEM;ID_LOCAL_ORIGEM;ID_LOCAL_DESTINO]
		HiddenList  hiddenItems = (HiddenList) demandasForm.getField("itensDemanda");
		if(hiddenItems!=null)
		{
			
			List<String> valores = hiddenItems.getValues();
			String arrayIDs[]; 
			DemandasItemAtivo dmdItem ;
			ItensAtivos itensAtivos;
			
			for (String str : valores) 
			{

				arrayIDs = str.split(";");
				
				if (arrayIDs.length >= 2)
				{
					
					dmdItem  = new DemandasItemAtivo();
					itensAtivos = new ItensAtivos();
					itensAtivos.setItensAtivosId((Integer.valueOf(arrayIDs[0])));
					dmdItem.setItemAtivos(itensAtivos);
					Local localOrigem =new Local();
					localOrigem.setId(Integer.valueOf(arrayIDs[1]));
					dmdItem.setLocalOrigem(localOrigem);
					Local localDestino =new Local();
					localDestino.setId(Integer.valueOf(arrayIDs[2]));
					dmdItem.setLocalDestino(localDestino);
					
					demandasItemAtivoList.add(dmdItem);
				}
				
			}
			
			
			
			
		}
		
		preencherDadosItem(demandasItemAtivoList);
		
		return demandasItemAtivoList;
		
		
	}
	



	private void preencherDadosItem(
			List<DemandasItemAtivo> demandasItemAtivoList) 
	{
		
		for (Iterator iterator = demandasItemAtivoList.iterator(); iterator.hasNext();) 
		{
			 DemandasItemAtivo demandasItemAtivo = (DemandasItemAtivo) iterator.next();
			
		 	 ItensAtivos  itemAtv =  demandaGerencia.obterItensAtivo(demandasItemAtivo.getItemAtivos().getItensAtivosId());
		 	 demandasItemAtivo.setItemAtivos(itemAtv);
			
		}
		
	}

	public boolean onPesquisaClick() {

	   return false;

	}
	
	@Override
	protected void montarGrid() {
		
		
		
	}

}
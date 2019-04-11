package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.ActionResult;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.DemandasItemAtivo;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.web.exception.ValidacaoCampoException;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;


@Component
public class DemandasFormDadosPage  extends FormPage
{

	private static final String NOME_LAYOUT = "Demandas";

	private static final String CHAVE_DENPENDENCIA  = "demandasForm.dependenciaID";
	private static final String CHAVE_LISTA_ITENS = "demandasForm.listaItens";
	
	@Bindable
	protected Form demandasForm = new Form("demandasForm");

	@Bindable
	public String title = getMessage("titulo");
	
	@Bindable
	private Select select = new Select("layout", true);
	 
	@Bindable
	private Demandas demandaDependencia;
	
	@Bindable
	private Integer IdDemanDep;
	
	@Bindable
	private Integer IdItens[];
	
	@Bindable
	Collection<DemandasItemAtivo> listaDemandasItensAtivos;
	
	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;

	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;

	private ImageSubmit okSubmit;	
	
	@Override
	public void onInit() {
		
		super.onInit();
		initDemandas();
		
		Integer idDependencia = (Integer) getContext().getSession().getAttribute(CHAVE_DENPENDENCIA);
		if (idDependencia!=null)
		{
			 demandaDependencia = demandaGerencia.pesquisarID(idDependencia);
		}
		
		List<DemandasItemAtivo>  lstDemandasItensAtivos =  (List<DemandasItemAtivo>) getContext().getSession().getAttribute(CHAVE_LISTA_ITENS);
		
		if (lstDemandasItensAtivos!=null)
		{
			listaDemandasItensAtivos= lstDemandasItensAtivos;
		}
		
		okSubmit = new ImageSubmit("next", "/resources/images/btnAdicionar.png");
		okSubmit.setListener(this, "onNextItens");
		okSubmit.setAttribute("onclick", "validar('Pesquisar')");
		okSubmit.setStyle("border","1px solid black");
		okSubmit.setStyle("border-color","blue");
		demandasForm.add(okSubmit);
        
		
	}
	
	private void initDemandas() {

		FieldSet demandasDados = new FieldSet("DemandasDados");

		Select selectTipoDemanda = new Select("tipoDemanda");
		selectTipoDemanda.setRequired(true);
		selectTipoDemanda.setLabel(selectTipoDemanda.getLabel()
				+ " <span class='required'></span>");
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

	

		select.add(new Option("", "-- Selecione --"));
		select.addAll(obterLayouts());
		demandasDados.add(select);

		demandasForm.add(demandasDados);

		select.setAttribute("onchange", "Click.submit(demandasForm, false)");
		select.setRequired(true);
		select.setLabel(select.getLabel() + " <span class='required'></span>");
		select.setAttribute("class", "validate[required]");

		montarBotoes();
		addControl(demandasForm);



		// Bind the form field request values
		ClickUtils.bind(demandasForm);

		obterConfiguracoes(null);

	}
	
	
	private Collection<Option> obterLayouts() {

		List<Option> optionList = new ArrayList<Option>();
		//Collection<Layout> lstLayout = layoutGerencia.obterTodos(); //demandaGerencia.obterTodosNomesLayouts(NOME_LAYOUT);
		
		Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDDEMANDA);
		
		for (Layout layout : lstLayout) {
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}
		return optionList;
	}





	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) {
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);

			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, demandasForm);

		}
	}

	
	public Form getForm() {
		return demandasForm;
	}
	@Override
	protected boolean onPesquisaClick() {
	
		return false;
	}

	@Override
	protected boolean onCancelarClick() {
		
		demandasForm.clearValues();
		demandasForm.clearErrors();
		FieldSet configs = (FieldSet) demandasForm.getControl("configuracoes");
		if(configs!=null)
		{
			demandasForm.remove(configs);
		}
		getContext().getSession().removeAttribute(CHAVE_DENPENDENCIA);
		demandaDependencia = null;

		return true;
	}

	@Override
	protected boolean onSalvarClick() {
		return false;
	}
	
	public ActionResult onAdicionarDependencia() {
	
		demandaDependencia = demandaGerencia.pesquisarID(IdDemanDep);		
		getContext().getSession().setAttribute(CHAVE_DENPENDENCIA, IdDemanDep);

		// Return an action result containing the message
		return new ActionResult("OK", ActionResult.HTML);
	}

	

	public boolean onNextItens() throws ValidacaoCampoException {

		Demandas demandas = montarDemandas();

		FieldSet configs = (FieldSet) demandasForm.getControl("configuracoes");
		
		if ((demandas.getInformacoesDemanda()!=null) && (demandas.getInformacoesDemanda().size() > 0))
		{
			
			DemandasFormItensPage nextPage = getContext().createPage(DemandasFormItensPage.class);
			
			getContext().getSession().setAttribute("aberturaDemandas", demandas);
			getContext().getSession().removeAttribute(CHAVE_DENPENDENCIA);
			setForward(nextPage);

			return true;
		}
		else
		{
			mensagemErro="Sem informa&ccedil;&otilde;es da demanda";	
			return false;
		}

		

	}

	@Override
	protected void montarGrid() {
	
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}
	
	private Demandas montarDemandas()  {

		Demandas demandas = new Demandas();
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		TipoDemanda tipoDemanda = new TipoDemanda();
		Layout layout = new Layout();

		FieldSet demandasDados = (FieldSet) demandasForm.getControl("DemandasDados");
		Field fieldTipoDemanda = demandasDados.getField("tipoDemanda");

		if (StringUtils.isNotBlank(fieldTipoDemanda.getValue())) {
			tipoDemanda.setId(Integer.valueOf(fieldTipoDemanda.getValue()));
		}

		if (StringUtils.isNotBlank(select.getValue())) {
			layout.setId(Integer.valueOf(select.getValue()));
		}

		demandas.setTipoDemanda(tipoDemanda);
		demandas.setLayout(layout);

		/**
		
		Tratar a edicao da Demanda e a relacao entre demandas
		
		
		Field fieldDemandaRef = demandasDados.getField("dependenciaID");
		if (StringUtils.isNotBlank(fieldDemandaRef.getValue())) {
			demandas.setIdPai(Integer.valueOf(fieldDemandaRef.getValue()));
		}
		
		*/

		FieldSet configs = (FieldSet) demandasForm.getControl("configuracoes");
		if (configs != null) {

			lstInfos = informacoesUtil.montarInfomacoes(configs);
			demandas.setInformacoesDemanda(lstInfos);

		}
		
		
		
		return demandas;

	}

}
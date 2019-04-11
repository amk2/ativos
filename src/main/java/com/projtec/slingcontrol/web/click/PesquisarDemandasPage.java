package com.projtec.slingcontrol.web.click;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.ActionListener;
import org.apache.click.ActionResult;
import org.apache.click.Control;
import org.apache.click.ControlRegistry;
import org.apache.click.Page;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.TextField;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.StatusDemanda;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;

@Component
public class PesquisarDemandasPage extends Page implements Serializable {
	private static final long serialVersionUID = -5968485640625599442L;

	private static final String NOME_LAYOUT = "Demandas";

	
	 @Bindable 
	 public String title = getMessage("titulo");
	

	@Bindable
	protected Form form = new Form("ativosForm");

	private Select select = new Select("layout");

	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;

	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;

	@Bindable
	private ActionLink pesquisarLink = new ActionLink("pesquisarLink",	"Pesquisar");

	@Bindable
	private ActionLink limparLink = new ActionLink("limparLink", "Limpar");
	
	@Resource(name = "ativosGerencia")
	protected AtivosGerencia ativosGerencia;

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;

	public PesquisarDemandasPage() {
		remoteAjax();
	}

	private void remoteAjax() {
		pesquisarLink.setId("pesquisarID");
		pesquisarLink.setImageSrc("/resources/images/btnPesquisar.png");
		//pesquisarLink.setAttribute("onchange", "ativosForm.submit();");
		pesquisarLink.addBehavior(new DefaultAjaxBehavior() {
			@Override
			public ActionResult onAction(org.apache.click.Control source) {
				
				String json = pesquisarComRetornoJSON();
				// ActionResult result = new ActionResult(, ActionResult.JSON);
				ActionResult result = new ActionResult(json, ActionResult.JSON);
				
				return result;
				
			};
		});
		
		
		
		//acrescentado em 15/07/2012
		limparLink.setId("limparID");
		limparLink.setImageSrc("/resources/images/btnLimpar.png");
		limparLink.setAttribute("style", "margin-right: 10px;");
		limparLink.setAttribute("onclick",
			"return window.confirm('Deseja realmente limpar dados ?');");
		
		limparLink.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
		
				return true;
			}
		});		
		
		
		ControlRegistry.registerAjaxTarget(form);
	}
	
	private String pesquisarComRetornoJSON() {
		
		Demandas dem = montarDemandas();
		List<Demandas> listaDemandas = demandaGerencia.pesquisar(dem);
		
		String json="";
		json = demandaDinamicoJSON(listaDemandas);
		
		return json;
		
	}

	@Override
	public void onInit() {

		initAtivos();

	}

	private void initAtivos() {

		form.setValidate(false);
		

		FieldSet pesquisarDemandas = new FieldSet("pesquisarDemandas");
		// pesquisarDemandas.setLabel("Pesquisar Ativos");
		// ativosDados.setAttribute("class", "validate[required]");
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

		pesquisarDemandas.add(selectTipoDemanda);

		
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
		//TextField statusDemandaField = ControlesHTML.textField("statusDemanda");
		statusDemanda.setRequired(false);
		pesquisarDemandas.add(statusDemanda);

		select.add(new Option("", "-- Selecione --"));
		select.addAll(obterLayouts());// obterLayouts()
		pesquisarDemandas.add(select);

		form.add(pesquisarDemandas);
		// select.setAttribute("onchange", "Click.submit(ativosForm, false)");
		select.setAttribute("onchange", "ativosForm.submit();");

		// montarBotoes();
		addControl(form);
		// Bind the form field request values
		ClickUtils.bind(form);

		obterConfiguracoes(null);

	}

	private Collection<Option> obterLayouts() {

		List<Option> optionList = new ArrayList<Option>();
		
		//Collection<Layout> lstLayout = layoutGerencia.obterTodos();       //ativosGerencia.obterTodosNomesLayouts(NOME_LAYOUT);
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
			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, form);

		}
	}

	private String demandaDinamicoJSON(List<Demandas> listaDemandas) {
		StringBuilder colModel = new StringBuilder();
		StringBuilder colNames = new StringBuilder();
		StringBuilder dataSet = new StringBuilder();
		Map mapLabel = new HashMap<Integer, String>();
		String strJSON = new String("{\n");
		
		colNames.append("\n\"colNames\":[");
		colNames.append("\"ID\",\"TIPO\",\"NOME LAYOUT\",");
		colModel.append("\n\"colModel\":[");
		colModel.append("\n{\"editable\":false,\"index\":\"ID\",\"jsonmap\":\"ID\",\"key\":true,\"name\":\"ID\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":20},\n");
		colModel.append("\n{\"editable\":false,\"index\":\"TIPO\",\"jsonmap\":\"TIPO\",\"key\":true,\"name\":\"TIPO\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":200},\n");
		colModel.append("\n{\"editable\":false,\"index\":\"LAYOUT\",\"jsonmap\":\"LAYOUT\",\"key\":false,\"name\":\"LAYOUT\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":200},\n");
		colModel.append("],\n");

		strJSON = strJSON + colModel.toString();

		colNames.append("],\n");
		strJSON = strJSON + colNames.toString();
		strJSON = strJSON + "\"gridModel\":{";
		dataSet.append("\"dataset\":[");

		for (Iterator iterator = listaDemandas.iterator(); iterator.hasNext();) {
			
			Demandas d = (Demandas) iterator.next();
			
			dataSet.append("\n{");
			dataSet.append("\"ID\":\"" + String.valueOf(d.getId())
					+ "\" ,\"TIPO\":\"" + d.getTipoDemanda().getDescricao()
					+ "\" , \"LAYOUT\":\"" + d.getLayout().getNome()
					+ "\", ");
			dataSet.append("}\n");
			
			if (iterator.hasNext()) {
				dataSet.append(",");
			}

		}
		
		dataSet.append("]\n");

		strJSON = strJSON + dataSet.toString();

		strJSON = strJSON + "}";// fechar gridmodel

		strJSON = strJSON + "\n}"; // fechar objeto
		return strJSON;
	}

	private Demandas montarDemandas() {

		Demandas demandas = new Demandas();
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		Informacoes infoDemanda;
		TipoDemanda tipoDemandaId = new TipoDemanda();
		Layout layoutId = new Layout();

		String strId = form.getFieldValue("id");
		Integer id;
		if (strId == null) {
			id = 0;
		} else if (strId.length() <= 0) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}

		demandas.setId(id);

		FieldSet demandasDados = (FieldSet) form.getControl("pesquisarDemandas");

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

		if (fieldStatus.getValue() != null
				&& !"".equals(fieldStatus.getValue())) {
			StatusDemanda statusDemanda = new StatusDemanda();
			// Integer.valueOf(fieldStatus.getValue())
			
			statusDemanda.setId(Integer.valueOf(fieldStatus.getValue()));
			
			//statusDemanda.setId(1);  //comentei, estava fixo no codigo. 
			demandas.setStatusDemanda(statusDemanda);
			
			// demandas.setStatus(fieldStatus.getValue());
		}

		// dependencia
//		Field fieldDemandaRef = demandasDados.getField("dependencia");
//		if (fieldDemandaRef.getValue() != null
//				&& !"".equals(fieldDemandaRef.getValue())) {
//			demandas.setIdPai(Integer.valueOf(fieldDemandaRef.getValue()));
//		}

		Collection<Field> fields;
		FieldSet configs = (FieldSet) form.getControl("configuracoes");
		if (configs != null) {

			lstInfos = informacoesUtil.montarInfomacoes(configs);
			demandas.setInformacoesDemanda(lstInfos);

		}

		return demandas;

	}

}

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
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;

@Component
public class EntradaAtivosPage extends Page implements Serializable {

	private static final long serialVersionUID = 416077943204051523L;

	//private static final String NOME_LAYOUT = "Ativos";

	@Bindable
	protected Form ativosForm = new Form("ativosForm");

	private Select select = new Select("layout", true);

	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;

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
	
	@Resource(name = "ativosUtil")
	protected AtivosUtil ativosUtil;

	public EntradaAtivosPage() {
		remoteAjax();
	}

	private void remoteAjax() {
			pesquisarLink.setId("pesquisarID");
			pesquisarLink.setImageSrc("/resources/images/btnPesquisar.png");
			pesquisarLink.addBehavior(new DefaultAjaxBehavior() {
			
			@Override
			public ActionResult onAction(org.apache.click.Control source) {

				Ativos atv = montarAtivos();
				List<Ativos> listaAtivos = ativosGerencia.pesquisar(atv);

				ActionResult result = new ActionResult(ativosUtil.ativosDinamicoJSON(listaAtivos), ActionResult.JSON);

				return result;

			};

		});
		
		
		//colocado em 15/07/2012
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

		
		
		ControlRegistry.registerAjaxTarget(ativosForm);

	}

	@Override
	public void onInit() {

		initAtivos();

	}

	private void initAtivos() {

		ativosForm.setValidate(false);

		FieldSet ativosDados = new FieldSet("pesquisarAtivos");

		//select.add(new Option("", "-- Selecione --"));
		select.addAll(obterLayouts());// obterLayouts()
		// select.setAttribute("class", "validate[required]");
		ativosDados.add(select);

		ativosForm.add(ativosDados);
		// select.setAttribute("onchange", "Click.submit(ativosForm, false)");
		select.setAttribute("onchange", "ativosForm.submit();");

		// montarBotoes();
		addControl(ativosForm);
		// Bind the form field request values
		ClickUtils.bind(ativosForm);

		obterConfiguracoes(null);

	}

	private Collection<Option> obterLayouts() {

		List<Option> optionList = new ArrayList<Option>();
		//Collection<Layout> lstLayout = layoutGerencia.obterTodos(); // ativosGerencia.obterTodosNomesLayouts(NOME_LAYOUT);

		Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDATIVO);
		
		for (Layout layout : lstLayout) {
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}
		return optionList;
	}

	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) {
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);
			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, ativosForm);

		}
	}

	/*
	 * 
	 * "colModel":[
	 * {"editable":true,"edittype":"integer","index":"userInfoId","jsonmap"
	 * :"userInfoId"
	 * ,"key":true,"name":"userInfoId","resizable":true,"search":false
	 * ,"sortable":true,"width":300},
	 * {"editable":true,"edittype":"text","index":
	 * "UserID","jsonmap":"userID","key"
	 * :false,"name":"userID","resizable":true,"search"
	 * :false,"sortable":true,"width":300} ],
	 * "colNames":["UserInfo ID","User ID"], "gridModel":{ "dataset":[
	 * {"userID":"SMI","userInfoId":5}, {"userID":"ABC","userInfoId":7},
	 * {"userID":"PQR","userInfoId":8}, {"userID":"FUR","userInfoId":10},
	 * {"userID":"COO","userInfoId":13} ], "page":1, "records":56, "rows":15,
	 * "sidx":"userInfoId", "sord":"asc", "total":0 } };
	 */

/*	private String ativosDinamicoJSON(List<Ativos> listaAtivos) {
		StringBuilder colModel = new StringBuilder();
		StringBuilder colNames = new StringBuilder();
		StringBuilder dataSet = new StringBuilder();
		Map mapLabel = new HashMap<Integer, String>();

		String strJSON = new String("{\n");

		
		 * strJSON = strJSON + "\"page\":\"1\","; strJSON = strJSON +
		 * "\"total\":\"1\","; strJSON = strJSON + "\"records\":\""+
		 * listaAtivos.size() + "\","; strJSON = strJSON + "\n\"rows\":[" ;
		 

		// StringBuffer strBuffer = new StringBuffer();
		Ativos ativos = listaAtivos.get(0);
		// obter o layout do Ativo.
		Collection<Informacoes> lstInfos = ativos.getInformacoes();

		// colNames
		Collection<Configuracoes> lstConfigs = ativos.getLayout()
				.getConfiguracoes();
		colNames.append("\n\"colNames\":[");
		colNames.append("\"ID\",\"NOME LAYOUT\",");

		// colModel
		
		 * "colModel":[
		 * {"editable":true,"edittype":"integer","index":"userInfoId"
		 * ,"jsonmap":"userInfoId"
		 * ,"key":true,"name":"userInfoId","resizable":true
		 * ,"search":false,"sortable":true,"width":300},
		 * {"editable":true,"edittype"
		 * :"text","index":"UserID","jsonmap":"userID"
		 * ,"key":false,"name":"userID"
		 * ,"resizable":true,"search":false,"sortable":true,"width":300} ]
		 
		colModel.append("\n\"colModel\":[");
		colModel.append("\n{\"editable\":false,\"index\":\"ID\",\"jsonmap\":\"ID\",\"key\":true,\"name\":\"ID\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":20},\n");
		colModel.append("\n{\"editable\":false,\"index\":\"LAYOUT\",\"jsonmap\":\"LAYOUT\",\"key\":false,\"name\":\"LAYOUT\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":100},\n");

		for (Iterator<Configuracoes> iterator = lstConfigs.iterator(); iterator
				.hasNext();) {
			Configuracoes conf = iterator.next();
			colNames.append("\"" + conf.getNomecampo() + "\"");
			// {"editable":true,"edittype":"integer","index":"userInfoId","jsonmap":"userInfoId","key":true,"name":"userInfoId","resizable":true,"search":false,"sortable":true,"width":300},
			colModel.append("\n{\"editable\":false,\"index\":\""
					+ conf.getNomecampo()
					+ "\",\"jsonmap\":\""
					+ conf.getNomecampo()
					+ "\",\"key\":false,\"name\":\""
					+ conf.getNomecampo()
					+ "\",\"resizable\":true,\"search\":false,\"sortable\":true,\"width\":120}\n");
			// + String.valueOf(conf.getTamanho()) + "}\n");

			mapLabel.put(conf.getId(), conf.getNomecampo());
			if (iterator.hasNext()) {
				colNames.append(",");
				colModel.append(",");
			}

		}
		colModel.append("],\n");
		strJSON = strJSON + colModel.toString();

		colNames.append("],\n");
		strJSON = strJSON + colNames.toString();

		// DataSet
		
		 * "dataset":[ {"userID":"SMI","userInfoId":5},
		 * {"userID":"ABC","userInfoId":7}, {"userID":"PQR","userInfoId":8},
		 * {"userID":"FUR","userInfoId":10}, {"userID":"COO","userInfoId":13} ],
		 

		strJSON = strJSON + "\"gridModel\":{";

		dataSet.append("\"dataset\":[");
		String strValor;
		for (Iterator iterator = listaAtivos.iterator(); iterator.hasNext();) {
			Ativos atv = (Ativos) iterator.next();

			dataSet.append("\n{");
			dataSet.append("\"ID\":\"" + String.valueOf(atv.getId())
					+ "\" , \"LAYOUT\":\"" + ativos.getLayout().getNome()
					+ "\", ");
			// {"userID":"SMI","userInfoId":5},
			Collection<Informacoes> lstInf = atv.getInformacoes();

			for (Iterator iterator2 = lstInf.iterator(); iterator2.hasNext();) {
				Informacoes informacoes = (Informacoes) iterator2.next();
				strValor = informacoes.getDescricao().replaceAll("\\\"", "\\'");

				dataSet.append("\""
						+ mapLabel.get(informacoes.getConfiguracoesId())
						+ "\":\"" + (strValor) + "\"");
				if (iterator2.hasNext()) {
					dataSet.append(",");
				}

			}
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
	}*/

	private Ativos montarAtivos() {
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

		//FieldSet atvDados = (FieldSet) ativosForm.getControl("ativoDados");

		if (select.getValue() != null && !"".equals(select.getValue())) {
			layoutId.setId(Integer.valueOf(select.getValue()));
		}

		ativos.setLayout(layoutId);

		FieldSet configs = (FieldSet) ativosForm.getControl("configuracoes");

		if (configs != null) {
			lstInfos = informacoesUtil.montarInfomacoes(configs);
			ativos.setInformacoes(lstInfos);
		}

		return ativos;
	}

}

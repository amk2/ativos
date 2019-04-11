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
import javax.servlet.http.HttpSession;

import org.apache.click.ActionListener;
import org.apache.click.ActionResult;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.HiddenList;
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
public class ItensCadastroPage extends FormPage {

	private static final long serialVersionUID = 5468988716022882660L;

	private static final String NOME_LAYOUT = "Ativos";

	private final Logger logger = LoggerFactory.getLogger(AtivosPage.class);

	@Bindable
	protected Form ativosForm = new Form("ativosForm");

	@Bindable
	Form formQtd = new Form("formQtd");

	@Bindable
	private Map<Integer, ListaGridView> mapListGridView = new HashMap<Integer, ListaGridView>();

	@Bindable
	private Map<Integer, ListaGridView> mapListGridViewItem = new HashMap<Integer, ListaGridView>();

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

	@Autowired
	private ApplicationContext appContext;

	private Select select = new Select("layout", true);

	public ItensCadastroPage() {
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
	public void onInit() {
		super.onInit();
		initAtivos();
		test = true;
	}

	private void initAtivos() {

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
		addControl(ativosForm);
		ClickUtils.bind(ativosForm);
		montarBotoes(true, true, true);
		obterConfiguracoes(null);

		montarFormAdicionar();
	}

	// fim metodos lista

	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) {
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);
			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, ativosForm);

		}
	}

	private Collection<Option> obterLayouts() {
		List<Option> optionList = new ArrayList<Option>();
		//Collection<Layout> lstLayout =  layoutGerencia.obterTodos();   // ativosGerencia.obterTodosNomesLayouts(NOME_LAYOUT);

		
		Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDATIVO);
		
		for (Layout layout : lstLayout) {
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}

		return optionList;
	}

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

		@SuppressWarnings("unused")
		FieldSet atvDados = (FieldSet) ativosForm.getControl("ativoDados");

		if (select.getValue() != null && !"".equals(select.getValue())) {
			layoutId.setId(Integer.valueOf(select.getValue()));
		}

		ativos.setLayout(layoutId);

		FieldSet configs = (FieldSet) ativosForm.getControl("configuracoes");

		logger.debug("Config FildSet :" + configs);
		if (configs != null) {
			lstInfos = informacoesUtil.montarInfomacoes(configs);
			ativos.setInformacoes(lstInfos);
		}
		HiddenList lstDepAtivos = (HiddenList) ativosForm
				.getControl("ativosDependencia");

		if (lstDepAtivos != null) {
			List<String> lstValores = lstDepAtivos.getValues();
			Set<Integer> lstDependencia = new HashSet<Integer>();
			lstValores.remove("0");// remover o zero fake...
			for (Iterator<String> iterator = lstValores.iterator(); iterator
					.hasNext();) {
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

			FieldSet configs = (FieldSet) ativosForm
					.getControl("configuracoes");
			validaCampos(configs);

			Ativos ativos = montarAtivos();

			if (ativos.getId() == 0) {

				Ativos at = ativosGerencia.incluir(ativos);
				@SuppressWarnings("unused")
				Ativos t = ativosGerencia.pesquisarId(at.getId());
				// adicionarLista(t);

			} else {
				// removerLista(ativos.getId());
				ativosGerencia.alterar(ativos);
				@SuppressWarnings("unused")
				Ativos t = ativosGerencia.pesquisarId(ativos.getId());
				// adicionarLista(t);

			}

			// mensagem
			ativosForm.clearValues();
			limparListaDependencia();

			mensagemSucesso = "O Ativo foi salvo com sucesso ";
			ativosForm.remove(configs);

			return true;

		} catch (ValidacaoCampoException e) {

			mensagemErro = e.getMessage();
			return false;

		}

	}

	public boolean onPesquisaClick() {
		ativosForm.clearErrors();
		if (StringUtils.isEmpty(select.getValue())) {
			mensagemErro = "Selecionar um Layout para pesquisa";
		} else {
			Ativos ativo = montarAtivos();
			List<Ativos> lstAtivos = null;
			if (ativo.getLayout() != null && ativo.getLayout().getId() > 0) {
				lstAtivos = ativosGerencia.pesquisar(ativo);
			}
			if (lstAtivos != null && lstAtivos.size() > 0) {
				montarTabela(lstAtivos);
				mesg = " Pesquisar: Total: " + lstAtivos.size() + " Ativos";
			} else {
				mensagemErro = "N&atilde;o foram encontrados Ativos com os dados informados";
				initAtivos();
			}
		}
		return true;

	}

	public boolean onCancelarClick() {
		ativosForm.clearValues();
		ativosForm.clearErrors();
		limparListaDependencia();
		mapListGridView = new HashMap<Integer, ListaGridView>();

		FieldSet configs = (FieldSet) ativosForm.getControl("configuracoes");
		if (configs != null) {
			ativosForm.remove(configs);
		}

		trataCampo(ativosForm.getField("Salvar"), false);

		return true;
	}

	public ActionResult obterAtivos() {

		Context context = getContext();

		String ids[] = context.getRequestParameterValues("id");
		Ativos atv;
		String strListaAtivo = "  { \"listaAtivos\": [";
		for (int i = 0; i < ids.length; i++) {
			atv = ativosGerencia.pesquisarId(Integer.valueOf(ids[i]));
			strListaAtivo = strListaAtivo + "{\"id\": \"" + atv.getId()
					+ "\" , \"nome\": \"" + atv.getLayout().getNome() + "\"}";
			if ((ids.length - 1) > (i)) {
				strListaAtivo = strListaAtivo + ",";
			}
		}

		strListaAtivo = strListaAtivo + "] }";

		return new ActionResult(strListaAtivo, ActionResult.JSON);
	}

	@Override
	public Form getForm() {
		return ativosForm;
	}

	public Ativos getFilhos(Ativos ativo) {
		List<AtivosNo> lstPaiFilho = new ArrayList<AtivosNo>();
		ativo.setLstFilhos(ativosGerencia.getFilhos(ativo.getId(), lstPaiFilho));
		return ativo;
	}

	@SuppressWarnings("rawtypes")
	private void montarTabela(Ativos ativo, int qtd) {
		HttpSession session = getContext().getSession();
		session.setAttribute("ativo", ativo);
		mapListGridViewItem = new HashMap<Integer, ListaGridView>();
		ListaGridView estLstViewItem;
		List<Ativos> lstAtivos = new ArrayList<Ativos>();
		for (int i = 0; i < qtd; i++) {
			lstAtivos.add(ativo);
		}
		for (Iterator iterator = lstAtivos.iterator(); iterator.hasNext();) {
			final Ativos ativos = (Ativos) iterator.next();
			if (mapListGridViewItem.containsKey(ativos.getLayout().getId())) {
				estLstViewItem = mapListGridViewItem.get(ativos.getLayout()
						.getId());
			} else {
				estLstViewItem = new ListaGridView();
				estLstViewItem.setTitulo(ativos.getLayout().getNome());
				estLstViewItem.setIdLayout(Integer.valueOf(ativos.getLayout()
						.getId()));
				estLstViewItem.setNomes(new LinkedHashMap<String, String>());
				estLstViewItem
						.setLinhasValores(new ArrayList<Map<String, String>>());
				mapListGridViewItem.put(ativos.getLayout().getId(),
						estLstViewItem);
				estLstViewItem.setTotal(new Integer(0));
				Collection<Configuracoes> lstConfigs = ativos.getLayout()
						.getConfiguracoes();
				for (Iterator iterator2 = lstConfigs.iterator(); iterator2
						.hasNext();) {
					Configuracoes configuracoes = (Configuracoes) iterator2
							.next();
					if (configuracoes.getPesquisa().equals(
							Configuracoes.PESQ_SIM)) {
						estLstViewItem.getNomes().put(
								String.valueOf(configuracoes.getId()),
								configuracoes.getNomecampo());
					}
				}
				estLstViewItem.getNomes().put("identificador", "Identificador");
			}
			Collection<Informacoes> lstInfos = ativos.getInformacoes();
			Map<String, String> mapLinha = new LinkedHashMap<String, String>();
			estLstViewItem.getLinhasValores().add(mapLinha);
			for (Iterator iterator2 = lstInfos.iterator(); iterator2.hasNext();) {
				Informacoes informacoes = (Informacoes) iterator2.next();
				Configuracoes config = layoutGerencia
						.pesquisarConfiguracao(informacoes.getConfiguracoesId());
				if (config.getPesquisa().equals(Configuracoes.PESQ_SIM)) {
					String strValor = formataValor(config, informacoes);
					String key = String.valueOf(informacoes
							.getConfiguracoesId());
					if (mapLinha.containsKey(key)) {
						strValor = mapLinha.get(key) + "|" + strValor;
					}
					mapLinha.put(key, strValor);
				}
			}
			mapLinha.put("identificador",
					"<input type='text' name='identificador' />");
			estLstViewItem.setTotal(estLstViewItem.getTotal()
					+ ativos.getQtdItens());
		}
	}

	private void montarTabela(List<Ativos> lstAtivos) {
		HttpSession session = getContext().getSession();
		session.setAttribute("lstAtivos", lstAtivos);
		mapListGridView = new HashMap<Integer, ListaGridView>();
		ListaGridView estLstView;
		@SuppressWarnings("unused")
		Integer soma = 0;
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = lstAtivos.iterator(); iterator.hasNext();) {
			final Ativos ativos = (Ativos) iterator.next();
			if (mapListGridView.containsKey(ativos.getLayout().getId())) {
				estLstView = mapListGridView.get(ativos.getLayout().getId());
			} else {
				estLstView = new ListaGridView();
				estLstView.setTitulo(ativos.getLayout().getNome());
				estLstView.setIdLayout(Integer.valueOf(ativos.getLayout()
						.getId()));
				estLstView.setNomes(new LinkedHashMap<String, String>());
				estLstView
						.setLinhasValores(new ArrayList<Map<String, String>>());
				mapListGridView.put(ativos.getLayout().getId(), estLstView);
				estLstView.setTotal(new Integer(0));
				estLstView.getNomes().put("selecionar", "Selecionar");
				Collection<Configuracoes> lstConfigs = ativos.getLayout()
						.getConfiguracoes();
				for (@SuppressWarnings("rawtypes")
				Iterator iterator2 = lstConfigs.iterator(); iterator2.hasNext();) {
					Configuracoes configuracoes = (Configuracoes) iterator2
							.next();
					if (configuracoes.getPesquisa().equals(
							Configuracoes.PESQ_SIM)) {
						estLstView.getNomes().put(
								String.valueOf(configuracoes.getId()),
								configuracoes.getNomecampo());
					}
				}
				estLstView.getNomes().put("dependencia", "Depend&ecirc;ncia");
			}
			Collection<Informacoes> lstInfos = ativos.getInformacoes();
			Map<String, String> mapLinha = new LinkedHashMap<String, String>();
			estLstView.getLinhasValores().add(mapLinha);
			for (@SuppressWarnings("rawtypes")
			Iterator iterator2 = lstInfos.iterator(); iterator2.hasNext();) {
				Informacoes informacoes = (Informacoes) iterator2.next();
				Configuracoes config = layoutGerencia
						.pesquisarConfiguracao(informacoes.getConfiguracoesId());
				if (config.getPesquisa().equals(Configuracoes.PESQ_SIM)) {
					String strValor = formataValor(config, informacoes);
					String key = String.valueOf(informacoes
							.getConfiguracoesId());
					if (mapLinha.containsKey(key)) {
						strValor = mapLinha.get(key) + "|" + strValor;
					}
					mapLinha.put(key, strValor);
				}
			}
			String linkEstrutura = "&nbsp;";
			linkEstrutura = linkEstrutura
					+ " <a href='#' onclick='exibirEstrutura(" + ativos.getId()
					+ ")'>[+]</a>";
			mapLinha.put("dependencia", linkEstrutura);
			mapLinha.put("selecionar",
					"<input type='radio' name='selectItem' onclick='hiddenSelecionado("
							+ ativos.getId() + ")' value='" + ativos.getId()
							+ "'>");
			estLstView.setTotal(estLstView.getTotal() + ativos.getQtdItens());
		}
	}

	@SuppressWarnings("serial")
	public void montarFormAdicionar() {
		HiddenField hiddenSelecionado = new HiddenField("hiddenSelecionado",
				String.class);
		formQtd.add(hiddenSelecionado);
		TextField quantidade = new TextField("quantidade");
		formQtd.add(quantidade);
		ImageSubmit adicionar = new ImageSubmit("Adicionar",
				"/resources/images/btnAdicionar.png");
		adicionar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
				return onAdicionarClick();
			}
		});
		formQtd.add(adicionar);
	}

	public boolean onAdicionarClick() {
		Field quantidade = formQtd.getField("quantidade");
		Field hiddenSelecionado = formQtd.getField("hiddenSelecionado");
		hiddenSelecionado.setId("hiddenSelecionado");
		int idAtivo = Integer.parseInt(hiddenSelecionado.getValue());
		int qtd = Integer.parseInt(quantidade.getValue());
		Ativos ativo = ativosGerencia.pesquisarId(idAtivo);
		if (ativo != null) {
			montarTabela(ativo, qtd);
		} else {
			mensagemErro = "N&atilde;o foram encontrados Ativos com os dados informados";
			initAtivos();
		}
		return true;
	}

	private void limparListaDependencia() {
		HiddenList lstDepAtivos = (HiddenList) ativosForm
				.getControl("ativosDependencia");
		lstDepAtivos.setValueObject(new ArrayList<String>());
	}

	private String formataValor(Configuracoes config, Informacoes info) {
		TipoCampoView tpCampo = (TipoCampoView) appContext.getBean(String
				.valueOf(config.getTipoCampo().getId()));
		String valor = tpCampo.formataInformacao(config, info);
		return valor;
	}

	@Override
	protected void montarGrid() {
	}

}

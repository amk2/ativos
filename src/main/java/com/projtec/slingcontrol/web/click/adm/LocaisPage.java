package com.projtec.slingcontrol.web.click.adm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.extras.control.SubmitLink;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.gerencia.LocaisGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.Estoque;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.web.click.ConfiguracoesUtil;
import com.projtec.slingcontrol.web.click.ControlesHTML;
import com.projtec.slingcontrol.web.click.FormPage;
import com.projtec.slingcontrol.web.click.InformacoesUtil;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;

@Component
public class LocaisPage extends FormPage {

	private static final long serialVersionUID = 3090155414070587885L;
	
	private static final String NOME_LAYOUT = "Locais";

	@Bindable
	public String title = getMessage("titulo");

	@Bindable
	protected Form locaisForm = new Form("LocaisForm");

	private Select select = new Select("layout");

	public static final String LISTA = "lista.Locais";

	private Select selectEstoque = new Select("estoque");

	@Bindable
	protected ActionLink editLink = new ActionLink("edit", "Editar", this,
			"onEditClick");

	@Bindable
	protected ActionLink deleteLink = new ActionLink("delete", "Remover", this,
			"onDeleteClick");

	@Bindable
	protected SubmitLink cancelar = new SubmitLink("Cancelar");

	@Resource(name = "locaisGerencia")
	protected LocaisGerencia locaisGerencia;

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;

	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;

	// Constructor ------------------------------------------------------------

	public LocaisPage() {
	}

	@Override
	public boolean permitidoSalvar() {

		return Secure.isPermitted("botoes:locais:salvar");
	}

	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:locais:pesquisar");
	}

	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:locais:limpar");
	}

	@Override
	public void onInit() {
		test = true;
		String acao = getContext().getRequestParameter("actionLink");
		if (acao != null) {
			if (acao.equalsIgnoreCase("Pesquisar")
					& getForm().getControl("nome") != null) {
				onPesquisaClick();
			} else if (acao.equalsIgnoreCase("Limpar")) {
				onCancelarClick();
			}
		}
		super.onInit();
		initLocais();
		listaSessao();
	}

	private void initLocais() {

		HiddenField idField = new HiddenField("id", Integer.class);
		locaisForm.add(idField);

		FieldSet locaisDados = new FieldSet("LocaisDados");

		Field nomeField = ControlesHTML.textField("nome");
		nomeField.setRequired(false);
		nomeField.setLabel(nomeField.getLabel()
				+ " <span class='required'>*</span>");
		nomeField.setAttribute("class", "validate[required]");

		Field descricaoField = ControlesHTML.textField("descricao");
		descricaoField.setRequired(false);
		descricaoField.setLabel(descricaoField.getLabel()
				+ " <span class='required'>*</span>");
		descricaoField.setAttribute("class", "validate[required]");

		Field latitudeField = ControlesHTML.textField("latitude");
		latitudeField.setRequired(false);

		Field longitudeField = ControlesHTML.textField("longitude");
		longitudeField.setRequired(false);

		Field localizacaoXField = ControlesHTML.textField("localizacaoX");
		localizacaoXField.setRequired(false);

		Field localizacaoYField = ControlesHTML.textField("localizacaoY");
		localizacaoYField.setRequired(false);

		Field identificacaoField = ControlesHTML.textField("identificacao");
		identificacaoField.setRequired(false);
		identificacaoField.setLabel(identificacaoField.getLabel()
				+ " <span class='required'>*</span>");
		identificacaoField.setAttribute("class", "validate[required]");

		selectEstoque.setRequired(false);
		selectEstoque.setLabel(selectEstoque.getLabel()
				+ " <span class='required'>*</span>");
		selectEstoque.setAttribute("class", "validate[required]");
		selectEstoque.addAll(obterEstoques());

		select.setRequired(false);
		select.setLabel(select.getLabel() + " <span class='required'>*</span>");
		select.setAttribute("class", "validate[required]");

		select.addAll(obterLayouts());// obterLayouts()

		locaisDados.add(nomeField);
		locaisDados.add(descricaoField);
		locaisDados.add(latitudeField);
		locaisDados.add(longitudeField);
		locaisDados.add(localizacaoXField);
		locaisDados.add(localizacaoYField);
		locaisDados.add(identificacaoField);
		locaisDados.add(selectEstoque);
		locaisDados.add(select);
		locaisForm.add(locaisDados);

		select.setAttribute("onchange", "Click.submit(LocaisForm, false)");

		locaisForm.add(locaisDados);

		montarBotoes();

		addControl(locaisForm);

		// Bind the form field request values
		ClickUtils.bind(locaisForm);

		obterConfiguracoes(null);

	}

	protected void montarGrid() {

		Column column;

		column = new Column("nome");
		grid.addColumn(column);

		column = new Column("descricao");
		grid.addColumn(column);

		column = new Column("latitude");
		grid.addColumn(column);

		column = new Column("longitude");
		grid.addColumn(column);

		column = new Column("localizacao_x");
		grid.addColumn(column);

		column = new Column("localizacao_y");
		grid.addColumn(column);

		column = new Column("identificacao");
		grid.addColumn(column);
		
		
		column = new Column("action");
		column.setWidth("120px;");
		ActionLink[] links = new ActionLink[] { editLink, deleteLink };
		column.setDecorator(new LinkDecorator(grid, links, "id"));
		grid.addColumn(column);

		deleteLink.setAttribute("onclick",
				"return window.confirm('Deseja realmente remover ?');");

		grid.setDataProvider(new DataProvider<Local>() {
			public List<Local> getData() {
				return obterListaSessao();
			}
		});
	}

	public Form getForm() {
		return locaisForm;
	}

	// metodos para lista sessao ///metodos para manibular lista
	private void listaSessao() {
		List<Local> lst = (List<Local>) getContext().getSession().getAttribute(
				LISTA);
		if (lst == null) {
			lst = new ArrayList<Local>();
			getContext().getSession().setAttribute(LISTA, lst);
		}
	}

	private List<Local> obterListaSessao() {
		List<Local> lst = (List<Local>) getContext().getSession().getAttribute(
				LISTA);
		return lst;
	}

	public void removerLista(Integer id) {
		Local local;
		List lst = obterListaSessao();

		for (Iterator iterator = obterListaSessao().iterator(); iterator
				.hasNext();) {
			local = (Local) iterator.next();
			if (local.getId().equals(id)) {
				iterator.remove();
			}

		}
	}

	public void adicionarLista(Local l) {
		obterListaSessao().add(0, l);
	}

	public void novaLista(List<Local> lstNova) {
		List<Local> lstAntiga = (List<Local>) getContext().getSession()
				.getAttribute(LISTA);
		lstAntiga = null;
		getContext().getSession().setAttribute(LISTA, lstNova);
	}

	// fim metodos lista

	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) {
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);
			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, locaisForm);

		}
	}

	private Local montarLocais() {

		Local locais = new Local();
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		Informacoes infoLocais;

		Layout layoutId = new Layout();
		Estoque estoqueId = new Estoque();

		String strId = locaisForm.getFieldValue("id");
		Integer id;
		if (strId == null) {
			id = 0;
		} else if (strId.length() <= 0) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}

		FieldSet locaisDados = (FieldSet) locaisForm.getControl("LocaisDados");

		Field fieldNome = locaisDados.getField("nome");
		locais.setNome(fieldNome.getValue());

		Field fieldDescr = locaisDados.getField("descricao");
		locais.setDescricao(fieldDescr.getValue());
		
		// IFS pra verificar se os coampos tao vazio
		Field latitudeField = locaisDados.getField("latitude");		
		locais.setLatitude(latitudeField.getValue());

		Field longitudeField = locaisDados.getField("longitude");
		locais.setLongitude(longitudeField.getValue());

		Field localizacaoXField = locaisDados.getField("localizacaoX");
		locais.setLocalizacao_x(localizacaoXField.getValue());

		Field localizacaoYField = locaisDados.getField("localizacaoY");
		locais.setLocalizacao_y(localizacaoYField.getValue());

		Field identificacaoField = locaisDados.getField("identificacao");
		locais.setIdentificacao(identificacaoField.getValue());

		if (select.getValue() != null && !"".equals(select.getValue())) {
			layoutId.setId(Integer.valueOf(select.getValue()));
		}

		if (selectEstoque.getValue() != null
				&& !"".equals(selectEstoque.getValue())) {
			estoqueId.setId(Integer.valueOf(selectEstoque.getValue()));
		}

		locais.setLayout(layoutId);
		locais.setEstoque(estoqueId);
		locais.setId(id);

		Collection<Field> fields;
		FieldSet configs = (FieldSet) locaisForm.getControl("configuracoes");
		if (configs != null) {
			lstInfos = informacoesUtil.montarInfomacoes(configs);
		}

		locais.setInfos(lstInfos);
		
		return locais;

	}

	private Collection<Option> obterEstoques() {
		List<Option> optionList = new ArrayList<Option>();
		optionList.add(new Option("", "-- Selecione --"));
		Collection<Estoque> lstEstoque = locaisGerencia.obterEstoques();
		for (Estoque estoque : lstEstoque) {
			optionList.add(new Option(estoque.getId(), estoque.getNome()));
		}
		return optionList;
	}

	private Collection<Option> obterLayouts() {

		
		List<Option> optionList = new ArrayList<Option>();
		optionList.add(new Option("", "-- Selecione --"));
		//Collection<Layout> lstLayout = layoutGerencia.obterTodos();

		Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDLOCAL);

		for (Layout layout : lstLayout) {
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}
		return optionList;
	}

	// Event Handlers ---------------------------------------------------------
	public boolean onSalvarClick() {
		
		imageFields();
		getContext().getSession().setAttribute("imgField",
				getContext().getRequestParameter("imgField"));
		FieldSet fieldSet = (FieldSet) locaisForm.getControl("LocaisDados");
		FieldSet configuracoes = (FieldSet) locaisForm.getControl("configuracoes");

		try {
			
			validaCampos(fieldSet);
			
			if (configuracoes!=null)
				validaCampos(configuracoes);
			
			if (!locaisForm.isValid())
				return false;
			
			Local locais = montarLocais();
			
			if (locais.getId() == 0) {
				
				Local l = locaisGerencia.incluir(locais);
				l = locaisGerencia.perquisarID(locais.getId());
				adicionarLista(l);
				
			} 
			else {
				
				removerLista(locais.getId());
				locaisGerencia.alterar(locais);
				Local l = locaisGerencia.perquisarID(locais.getId());
				adicionarLista(l);
				
			}

			// mensagem
			locaisForm.clearValues();

			mensagemSucesso = "O Local foi salvo com sucesso ";

			return true;
			
		} 
		catch (Exception e) {

			mensagemErro = e.getMessage();
			return false;

		}

	}
	
	public boolean onDeleteClick() {
		
		try {
		
			Integer id = deleteLink.getValueInteger();
			locaisGerencia.excluir(id);
			
//			removerLista(id);
			mensagemSucesso = "O Local foi excluído com sucesso ";
			
			System.out.println("############################### MENSAGEM " + mensagemSucesso);
			
			return true;
			
		}
		catch (Exception e) {
			
			mensagemErro = e.getMessage();
			e.printStackTrace();
			return false;
			
		}
		
	}

	public boolean onCancelarClick() {

		locaisForm.clearValues();
		locaisForm.clearErrors();

		return true;
	}

	public boolean onEditClick() {
		
		Integer id = editLink.getValueInteger();
		Local locais = locaisGerencia.perquisarID(id);
		
		if (locais != null) {

			// id
			HiddenField idField = (HiddenField) locaisForm.getField("id");
			idField.setValueObject(locais.getId());

			locaisForm.getField("nome").setValue(locais.getNome());
			locaisForm.getField("descricao").setValue(locais.getDescricao());
			locaisForm.getField("latitude").setValue(locais.getLatitude());
			locaisForm.getField("longitude").setValue(locais.getLongitude());
			locaisForm.getField("localizacaoX").setValue(
					locais.getLocalizacao_x());
			locaisForm.getField("localizacaoY").setValue(
					locais.getLocalizacao_y());
			locaisForm.getField("identificacao").setValue(
					locais.getIdentificacao());
			// layout
			selectEstoque.setValue(String.valueOf(locais.getEstoque().getId()));
			select.setValue(String.valueOf(locais.getLayout().getId()));
			locaisForm.getField("id").setValueObject(locais.getId());

			obterConfiguracoes(locais.getMapInformacoesLocais());

		}
		
		return true;
	}

	public boolean onPesquisaClick() {

		locaisForm.clearErrors();

		Local locais = montarLocais();
		final List<Local> lstLocal;

		lstLocal = new ArrayList<Local>(locaisGerencia.pesquisar(locais));

		novaLista(lstLocal);

		// mesg = "Pesquisar: Total: " + lstLocal.size() + " Locais ";

		return true;

	}

}
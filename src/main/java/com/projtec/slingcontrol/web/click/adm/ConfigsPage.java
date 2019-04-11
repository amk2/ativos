package com.projtec.slingcontrol.web.click.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Table;
import org.apache.click.control.TextArea;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.web.click.ControlesHTML;
import com.projtec.slingcontrol.web.click.CrudPage;
import com.projtec.slingcontrol.web.exception.ValidacaoCampoException;

@Component
public class ConfigsPage extends CrudPage<Configuracoes> implements
		Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String LISTA_OBJETOS_CONFIG_PAGE = "LISTA_OBJETOS_CONFIG_PAGE";

	@Bindable
	public String title = getMessage("titulo");

	@Bindable
	protected Form configForm = new Form();

	@Bindable
	FieldSet fieldSet = new FieldSet("configs");

	Select tipocampoField = new Select("tipoCampo");

	Select layoutField = new Select("layout");
	Select pesquisaField = new Select("pesquisa");
	Select obrigatoriedadeField = new Select("Obrigatoriedade");

	// private Checkbox checkboxObrigatoriedade = new
	// Checkbox("Obrigatoriedade");
	// private Checkbox checkboxLocalizacao = new Checkbox("Localizacao");

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	public ConfigsPage() {
		super();
	}

	@Override
	public boolean permitidoSalvar() {
		return Secure.isPermitted("botoes:configuracao:salvar");
	}

	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:configuracao:pesquisar");
	}

	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:configuracao:limpar");
	}

	@Override
	public void onInit() {
		
		test = true;
		
		super.onInit();
		montarForm();

		// montar tipo campos para ocorrer cache
		tipocampoField.setDataProvider(new DataProvider<Option>() {
			@Override
			public Iterable<Option> getData() {
				List<Option> optionList = new ArrayList<Option>();
				optionList.add(new Option("", "-- Selecione --"));
				Collection<TipoCampo> lstConfiguracoes = layoutGerencia
						.obterTiposCampos();
				for (Iterator iterator = lstConfiguracoes.iterator(); iterator
						.hasNext();) {
					TipoCampo tipocampo = (TipoCampo) iterator.next();
					optionList.add(new Option(tipocampo.getId(), tipocampo
							.getDescricao()));

				}
				return optionList;
			}
		});

		// layout
		layoutField.setDataProvider(new DataProvider<Option>() {
			@Override
			public Iterable<Option> getData() {
				List<Option> optionList = new ArrayList<Option>();
				optionList.add(new Option("", "-- Selecione --"));
				Collection<Layout> lstLayout = layoutGerencia.obterTodos();
				for (Iterator<Layout> iterator = lstLayout.iterator(); iterator
						.hasNext();) {
					Layout layout = iterator.next();
					optionList.add(new Option(layout.getId(), layout.getNome()));
				}
				return optionList;
			}
		});

		tipocampoField.setAttribute("onchange",
				"Click.submit(configForm, false)");
		criarCampoOpcoes();
	}

	private void montarForm() {

		String valor = getContext().getRequestParameter("tipoCampo");
		String acao = getContext().getRequestParameter("actionLink");
		String vlAux = getContext().getRequestParameter("vlAux");

		// Layout
		layoutField.setRequired(false);
		layoutField.setLabel(layoutField.getLabel()
				+ " <span class='required'>*</span>");
		layoutField.setAttribute("class", "validate[required]");
		fieldSet.add(layoutField);
		HiddenField hidden = new HiddenField("layoutHidden", "");
		fieldSet.add(hidden);

		// Nome do campo
		Field nomeCampoField = ControlesHTML.textField("nomeCampo");
		nomeCampoField.setRequired(false);
		nomeCampoField.setLabel(nomeCampoField.getLabel()
				+ " <span class='required'>*</span>"
				+ "<span class='max_nomecampo'>(Max. 250 Carac.)</span>");
		nomeCampoField.setAttribute("class", "validate[required]");
		fieldSet.add(nomeCampoField);

		obrigatoriedadeField.setRequired(false);
		obrigatoriedadeField.setLabel(obrigatoriedadeField.getLabel()
				+ " <span class='required'>*</span>");
		obrigatoriedadeField.setAttribute("class", "validate[required]");
		obrigatoriedadeField.add(new Option("", "--Todos--"));
		obrigatoriedadeField.add(new Option("S", "SIM"));
		obrigatoriedadeField.add(new Option("N", "NAO"));
		fieldSet.add(obrigatoriedadeField);
		HiddenField obrigatorio = new HiddenField("layoutHidden", "");
		fieldSet.add(obrigatorio);

		// Localiza��o campo
		pesquisaField.setRequired(false);
		pesquisaField.setLabel(pesquisaField.getLabel()
				+ " <span class='required'>*</span>");
		pesquisaField.setAttribute("class", "validate[required]");
		pesquisaField.add(new Option("", "--Todos--"));
		pesquisaField.add(new Option("S", "SIM"));
		pesquisaField.add(new Option("N", "NAO"));
		fieldSet.add(pesquisaField);
		HiddenField localizacao = new HiddenField("layoutHidden", "");
		fieldSet.add(localizacao);

		// checkboxObrigatoriedade.setChecked(true);
		// fieldSet.add(checkboxObrigatoriedade);
		// checkboxLocalizacao.setChecked(true);
		// fieldSet.add(checkboxLocalizacao);

		// Label Ordem
		IntegerField ordemField = new IntegerField("ordem");
		ordemField.setSize(100);
		ordemField.setRequired(false);
		ordemField.setLabel(ordemField.getLabel()
				+ " <span class='required'>*</span>");
		ordemField.setAttribute("class", "validate[required]");
		fieldSet.add(ordemField);

		// Label Tipo do Campo
		tipocampoField.setRequired(false);
		tipocampoField.setLabel(tipocampoField.getLabel()
				+ " <span class='required'>*</span>");
		tipocampoField.setAttribute("class", "validate[required]");
		fieldSet.add(tipocampoField);
		HiddenField idField = new HiddenField("id", Integer.class);
		fieldSet.add(idField);

		// Somente o campo do tipo texto sera possivel definir tamanho
		IntegerField tamanhoField = new IntegerField("tamanho");
		tamanhoField.setSize(100);
		tamanhoField.setMaxLength(100);
		tamanhoField.setMinLength(3);
		tamanhoField.setRequired(true);
		tamanhoField.setLabel(tamanhoField.getLabel()
				+ "<span class='max_tamanho'>(Max. 500)</span>");
		tamanhoField.setAttribute("class", "validate[required]");
		fieldSet.add(tamanhoField);

		if ((valor != null && !valor.equals("1"))) {
			tamanhoField.setStyle("display", "none");
			tamanhoField.setLabel(" ");
			tamanhoField.setAttribute("class", "");
			tamanhoField.setRequired(false);
		}

		configForm.add(fieldSet);

		ClickUtils.bind(configForm);
		montarBotoes();

	}

	private void criarCampoOpcoes() {
		String valor = tipocampoField.getValue();

		if (valor != null
				&& (valor
						.equals(String.valueOf(TipoCampo.MENU_ESCOLHA.getId()))
						|| valor.equals(String.valueOf(TipoCampo.BOTOES_RADIO
								.getId())) || valor.equals(String
						.valueOf(TipoCampo.MENU_ESCOLHA_MULTIPLO.getId())))) {
			Field opcoes = new TextArea("opcoes");
			fieldSet.add(opcoes);
		} else {
			fieldSet.removeField("opcoes");
		}
	}

	protected void validaCampos(FieldSet fieldSet)
			throws ValidacaoCampoException {
		for (Field field : fieldSet.getFieldList()) {
			if (field.getLabel().contains("required")
					&& StringUtils.isEmpty(field.getValue())) {
				String label = field.getLabel().substring(0,
						field.getLabel().indexOf("<"));
				throw new ValidacaoCampoException("O campo " + label
						+ " &eacute; requerido");
			}
			if (StringUtils.isNotEmpty(field.getError())) {
				throw new ValidacaoCampoException(field.getError());
			}
		}
	}

	protected void montarGrid() {

		String valor = tipocampoField.getValue();

		Column column;
		column = new Column("layout.nome");
		column.setWidth("220px;");
		grid.addColumn(column);

		column = new Column("nomecampo");
		column.setWidth("220px;");
		grid.addColumn(column);

		column = new Column("obrigatoriedade");
		column.setWidth("120px;");
		Decorator decorator = new Decorator() {

			@Override
			public String render(Object row, Context context) {
				String str = null;
				Configuracoes conf = (Configuracoes) row;
				if (conf.getObrigatoriedade().equals("S")) {
					str = "SIM";
				} else if (conf.getObrigatoriedade().equals("N")) {
					str = "NAO";
				}
				return str;
			}
		};

		column.setDecorator(decorator);
		grid.addColumn(column);

		column = new Column("pesquisa");
		column.setWidth("120px;");
		Decorator decoratorLocal = new Decorator() {
			@Override
			public String render(Object row, Context context) {
				String str = null;
				Configuracoes conf = (Configuracoes) row;
				if (conf.getPesquisa().equals("S")) {
					str = "SIM";
				}
				if (conf.getPesquisa().equals("N")) {
					str = "NAO";
				}
				return str;
			}
		};

		column.setDecorator(decoratorLocal);
		grid.addColumn(column);

		if (valor != null
				&& (!valor.equals(String.valueOf(TipoCampo.DATA.getId())))) {
			column = new Column("tamanho");
			column.setWidth("120px;");
			grid.addColumn(column);
		}

		column = new Column("ordem");
		column.setWidth("60px;");

		grid.addColumn(column);

		column = new Column("tipoCampo.descricao");
		column.setWidth("125px;");
		grid.addColumn(column);

		column = new Column("action");
		column.setSortable(false);
		ActionLink[] links = new ActionLink[] { editLink, deleteLink };
		column.setDecorator(new LinkDecorator(grid, links, "id"));
		column.setWidth("110px");
		grid.addColumn(column);

		deleteLink.setAttribute("onclick",
				"return window.confirm('Deseja realmente remover ?');");
		
		grid.setDataProvider(new DataProvider<Configuracoes>() {

			private static final long serialVersionUID = 1L;

			public List<Configuracoes> getData() 
			{
				return obterLista();
			}
			
		});
		
	}
	
	@SuppressWarnings("unchecked")
	private List<Configuracoes> obterLista() {
		
		List<Configuracoes> listaObjetos = (List<Configuracoes>) getContext().
			getSession().getAttribute(LISTA_OBJETOS_CONFIG_PAGE);
		
		return listaObjetos;
		
	}
	
	@Override
	public boolean onPesquisaClick() {
		
		boolean retorno = super.onPesquisaClick();
		
		getContext().getSession().setAttribute(LISTA_OBJETOS_CONFIG_PAGE, lstObjetos);
		
		return retorno;
		
	}
	
	public boolean onSalvarClick() {
		FieldSet configs = (FieldSet) getForm().getControl("configs");
		try {
			validaCampos(configs);
			Configuracoes obj = montarObjeto();
			Integer id = getIDFormObj();
			if (id == 0) {
				Integer novoId = gravar(obj);
				Configuracoes novoObj = null;
				if (novoId > 0) {
					novoObj = pesquisar(novoId);
				}
				if (novoObj != null) {
					lstObjetos.add(0, novoObj);
				}
			} else {
				alterar(obj);
				removerLista(id);
				Configuracoes novoObj = pesquisar(id);
				lstObjetos.add(0, novoObj);
			}
			mensagemSucesso = "A Configura&ccedil;&atilde;o foi salva com sucesso ";
			getForm().clearValues();
			return true;
		} catch (ValidacaoCampoException e) {
			mensagemErro = e.getMessage();
			return false;
		}
	}

	@Override
	public Boolean onEditClick() {
		if (!layoutGerencia.verificaSePodeAlterar(editLink.getValueInteger())) {
			mesg = "Nao pode ser alterado. Existe informacoes associada a essa configuracao de layout.";
			return false;
		}
		getForm().clearValues();
		getForm().clearErrors();
		return setFormObjeto();
	}

	@Override
	public void alterar(Configuracoes obj) {
		layoutGerencia.alteraConfiguracao(obj);
	}

	@Override
	public Form getForm() {
		return configForm;
	}

	@Override
	public Integer getIDFormObj() {
		String strId = configForm.getFieldValue("id");
		Integer id;

		if (strId == null) {
			id = 0;
		} else if (strId.length() <= 0) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}
		return id;
	}

	@Override
	public Table getTable() {
		return grid;
	}

	@Override
	public Integer gravar(Configuracoes obj) {
		int id = -1;
		try {
			Configuracoes conf = layoutGerencia.gravarConfiguracao(obj);
			id = conf.getId();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public Configuracoes montarObjeto() {
		Configuracoes config = new Configuracoes();
		config.setId(getIDFormObj());
		Select selectField = (Select) configForm.getField("layout");

		String valorTpCampo = getContext().getRequestParameter("tipoCampo");
		String acao = getContext().getRequestParameter("actionLink");

		if (selectField.getValue() != null
				&& !"".equals(selectField.getValue())) {
			config.setIdLayout(Integer.valueOf(selectField.getValue()));
		}

		config.setNomecampo(configForm.getFieldValue("nomeCampo"));

		// tamanhoField.setRequired(false);
		config.setObrigatoriedade(String.valueOf(configForm
				.getFieldValue("Obrigatoriedade")));

		config.setPesquisa(String.valueOf(configForm.getFieldValue("pesquisa")));

		if (valorTpCampo != null && valorTpCampo.equals("1")) {
			System.out.println("montando objeto ");
			String strTam = configForm.getFieldValue("tamanho");
			strTam.trim();
			if (strTam != null && !"".equals(strTam)) {
				config.setTamanho(Integer.valueOf(strTam));
			}
		} else {
			if (acao != null) {
				if (acao.equals("Pesquisar")) {
					config.setTamanho(null);
				} else {
					// banco aceita ate 500 caracteres de informacao para um
					// campo
					config.setTamanho(500);
				}
			}
		}

		String strOrdem = configForm.getFieldValue("ordem");
		strOrdem.trim();
		if (strOrdem != null && !"".equals(strOrdem)) {
			config.setOrdem(Integer.valueOf(strOrdem));
		}

		Select tpSelectField = (Select) configForm.getField("tipoCampo");

		if (tpSelectField.getValue() != null
				&& !"".equals(tpSelectField.getValue())) {
			TipoCampo tpCampo = new TipoCampo();
			tpCampo.setId(Integer.valueOf(tpSelectField.getValue()));
			config.setTipoCampo(tpCampo);
		}

		Field opcoes = configForm.getField("opcoes");

		if (opcoes != null && !"".equals(opcoes.getValue())) {
			montaOpcoesObjetos(opcoes.getValue(), config);
		}

		return config;
	}

	@Override
	public List<Configuracoes> pesquisar(Configuracoes objeto) {
		return new ArrayList<Configuracoes>(layoutGerencia.pesquisar(objeto));
	}

	@Override
	public Configuracoes pesquisar(Integer id) {
		return layoutGerencia.pesquisarConfiguracao(id);
	}

	@Override
	public void removerLista(Integer id) {
		Configuracoes c;
		for (Iterator iterator = lstObjetos.iterator(); iterator.hasNext();) {
			c = (Configuracoes) iterator.next();
			if (c.getId().equals(id)) {
				iterator.remove();
			}
		}
	}

	@Override
	public Boolean removerObjeto(Integer id) {

		// verifica se existe alguma informacao associada a essa configuracao de
		// layot.
		if (!layoutGerencia.verificaSePodeExluir(id)) {
			mensagemErro = "A exclus&atilde;o n&atilde;o pode ser realizada. Existem informa&ccedil;&otilde;es associada a essa configura&ccedil;&atilde;o de layout.";
			return false;
		}

		if (layoutGerencia.excluirConfiguracao(id)) {
			mensagemSucesso = "Configura&ccedil;&atilde;o removida com sucesso.";
			return true;
		} else {
			mensagemErro = "Problemas ao remover esta Configura&ccedil;&atilde;o.";
			return false;
		}

	}

	@Override
	public Boolean setFormObjeto() {
		Integer id = editLink.getValueInteger();
		Configuracoes conf = layoutGerencia.pesquisarConfiguracao(id);

		if (conf != null) {

			configForm.getField("id").setValueObject(conf.getId());
			configForm.getField("layout").setValueObject(conf.getIdLayout());
			configForm.getField("layout").setDisabled(true);
			configForm.getField("layoutHidden").setValue(
					conf.getIdLayout().toString());
			configForm.getField("layout").setName("layout_");
			configForm.getField("layout").setLabel("Layout");
			configForm.getField("layoutHidden").setName("layout");
			configForm.getField("nomeCampo").setValue(conf.getNomecampo());
			configForm.getField("Obrigatoriedade").setValueObject(
					conf.getObrigatoriedade());
			configForm.getField("pesquisa").setValueObject(conf.getPesquisa());
			configForm.getField("ordem").setValueObject(conf.getOrdem());
			configForm.getField("tipoCampo").setValueObject(
					conf.getTipoCampo().getId());
			configForm.getField("tamanho").setValueObject(conf.getTamanho());

			// Se tipo campo nao for texto
			if (conf.getTipoCampo().getId() != 1) {
				// esconde
				Field tamanhoField = configForm.getField("tamanho");
				tamanhoField.setStyle("display", "none");
				tamanhoField.setLabel(" ");
				tamanhoField.setAttribute("class", "");
				tamanhoField.setRequired(false);
			}

			Collection<Configuracoes> lstFilhos = conf.getFilhosConfig();

			if (lstFilhos != null) {
				StringBuffer strBf = new StringBuffer();

				for (Iterator iterator = lstFilhos.iterator(); iterator
						.hasNext();) {
					Configuracoes configuracoes = (Configuracoes) iterator
							.next();
					strBf.append(configuracoes.getNomecampo());
					strBf.append(";");
				}

				if ((conf.getTipoCampo().getId() == 3)
						|| (conf.getTipoCampo().getId() == 4)
						|| (conf.getTipoCampo().getId() == 9)) {
					Field opcoes = new TextArea("opcoes");
					opcoes.setValue(strBf.toString());
					fieldSet.add(opcoes);
				}

			}

		}
		return true;
	}

	private void montaOpcoesObjetos(String opcoes, Configuracoes config) {
		String strOpcoes[] = StringUtils.split(opcoes, ";");
		Configuracoes confFilho;
		config.setFilhosConfig(new ArrayList<Configuracoes>());
		for (int i = 0; i < strOpcoes.length; i++) {
			confFilho = new Configuracoes();
			confFilho.setId(0);
			confFilho.setNomecampo(strOpcoes[i]);
			config.getFilhosConfig().add(confFilho);
		}
	}
}

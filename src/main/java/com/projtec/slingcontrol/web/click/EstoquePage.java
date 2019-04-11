package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
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
import org.apache.click.control.Table;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.EstoqueGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.Estoque;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;

@Component
public class EstoquePage extends CrudPage<Estoque> {

	private static final long serialVersionUID = 8054556769766102899L;

	private static final String NOME_LAYOUT = "Estoques";

	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;

	@Bindable
	public String title = getMessage("titulo");

	@Bindable
	protected Form formEstoque = new Form("formEstoque");

	private Select select = new Select("layout");

	protected List<Estoque> lstEstoque;

	@Resource(name = "estoqueGerencia")
	protected EstoqueGerencia estoqueGerencia;

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;

	public EstoquePage() {
		super();
		// montarForm();
	}

	@Override
	public boolean permitidoSalvar() {
		return Secure.isPermitted("botoes:estoque:salvar");
	}

	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:estoque:pesquisar");
	}

	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:estoque:limpar");
	}

	@Override
	public void onInit() {
		super.onInit();
		montarForm();
	}

	protected void montarGrid() {
		Column column;

		column = new Column("nome");
		column.setWidth("220px;");
		grid.addColumn(column);
		column = new Column("descricao");
		column.setWidth("220px;");
		grid.addColumn(column);
		// column = new Column("layout.nome");
		// column.setWidth("220px;");
		// grid.addColumn(column);

		column = new Column("action");
		column.setWidth("120px;");
		column.setSortable(false);

		deleteLink.setAttribute("onclick",
				"return window.confirm('Deseja realmente remover ?');");
		ActionLink[] links = new ActionLink[] { editLink, deleteLink };
		column.setDecorator(new LinkDecorator(grid, links, "id"));
		grid.addColumn(column);
	}

	private Collection<Option> obterLayouts() {
		List<Option> optionList = new ArrayList<Option>();
		optionList.add(new Option("", "-- Selecione --"));
		//Collection<Layout> lstLayout = layoutGerencia.obterTodos();
		Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDESTOQUE);

		for (Layout layout : lstLayout) {
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}
		return optionList;
	}

	private void montarForm() {
		// form
		FieldSet fieldSet = new FieldSet("estoque");

		Field nomeField = ControlesHTML.textField("nome");
		nomeField.setLabel(nomeField.getLabel()
				+ " <span class='required'>*</span>");
		nomeField.setRequired(false);
		nomeField.setAttribute("class", "validate[required]");
		fieldSet.add(nomeField);

		Field descricaoField = ControlesHTML.textField("descricao");
		descricaoField.setLabel(descricaoField.getLabel()
				+ " <span class='required'>*</span>");
		descricaoField.setRequired(false);
		descricaoField.setAttribute("class", "validate[required]");
		fieldSet.add(descricaoField);

		select.setRequired(false);
		
		if (!select.getLabel().contains("required"))
			select.setLabel(select.getLabel() + " <span class='required'>*</span>");
		
		select.setAttribute("class", "validate[required]");
		fieldSet.add(select);

		select.getOptionList().clear();
		select.addAll(obterLayouts());
		select.setAttribute("onchange", "Click.submit(formEstoque, false)");
		fieldSet.add(select);

		HiddenField idField = new HiddenField("id", Integer.class);
		fieldSet.add(idField);

		formEstoque.add(fieldSet);

		ClickUtils.bind(formEstoque);

		obterConfiguracoes(null);
	}

	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) {
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);
			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, formEstoque);

		}
	}

	@Override
	public Form getForm() {
		return formEstoque;
	}

	@Override
	public Table getTable() {
		return grid;
	}

	@Override
	public Boolean setFormObjeto() {
		Integer id = editLink.getValueInteger();

		Estoque estoque = estoqueGerencia.pesquisarId(id);

		if (estoque != null) {
			formEstoque.getField("nome").setValue(estoque.getNome());
			formEstoque.getField("descricao").setValue(estoque.getDescricao());
			formEstoque.getField("layout").setValueObject(
					estoque.getLayout().getId());
			formEstoque.getField("id").setValueObject(estoque.getId());
		}
		select.setValue(String.valueOf(estoque.getLayout().getId()));
		obterConfiguracoes(estoque.getMapInformacoesLocais());
		return true;

	}

	@Override
	public Estoque montarObjeto() {

		// Integer id = getIDFormObj();
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		Layout layoutId = new Layout();

		String nome = formEstoque.getFieldValue("nome");
		String descricao = formEstoque.getFieldValue("descricao");

		Estoque estoque = new Estoque();
		
		Integer id = null;
		if (StringUtils.isNotEmpty(formEstoque.getFieldValue("id"))) {
			
			id = new Integer(formEstoque.getFieldValue("id"));
			estoque.setId(id);
			
		}
		
		estoque.setNome(nome);
		estoque.setDescricao(descricao);
		if (select.getValue() != null && !"".equals(select.getValue())) {
			layoutId.setId(Integer.valueOf(select.getValue()));
		}
		estoque.setLayout(layoutId);

		@SuppressWarnings("unused")
		Collection<Field> fields;
		FieldSet configs = (FieldSet) formEstoque.getControl("configuracoes");
		if (configs != null) {
			lstInfos = informacoesUtil.montarInfomacoes(configs);
			estoque.setInfos(lstInfos);
		}

		return estoque;
	}

	@Override
	public Boolean removerObjeto(Integer id) {
		return estoqueGerencia.excluir(id);
	}

	@Override
	public List<Estoque> pesquisar(Estoque objeto) {

		List<Estoque> estoquesLista = new ArrayList<Estoque>(estoqueGerencia.pesquisar(objeto));
		
		if (CollectionUtils.isEmpty(estoquesLista))
			mensagemErro = "N&atilde;o foram encontrados estoque com os dados informados";
		
		return estoquesLista;
		
	}

	@Override
	public void alterar(Estoque obj) {
		estoqueGerencia.alterar(obj);

	}

	@Override
	public Integer getIDFormObj() {
		String strId = formEstoque.getFieldValue("id");
		Integer id = 0;

		if (strId != null && !strId.equals("")) {
			id = Integer.valueOf(strId);
		}
		return id;
	}

	@Override
	public boolean onSalvarClick() {
		
		try {
			
			FieldSet estoqueFieldSet = (FieldSet) formEstoque.getControl("estoque"); 
			validaCampos(estoqueFieldSet);
			
			FieldSet configuracoesFieldSet = (FieldSet) formEstoque.getControl("configuracoes");
			validaCampos(configuracoesFieldSet);
			
			boolean retorno = super.onSalvarClick();
			mensagemSucesso = "Estoque salvo com sucesso";
			
			return retorno;
			
		}
		catch(Exception e) {
			
			mensagemErro = e.getMessage();
			return false;
			
		}
			
	}


	@Override
	public Integer gravar(Estoque obj) {

		int id = -1;

		try {

			Estoque estNovo = estoqueGerencia.gravar(obj);
			id = estNovo.getId();
			
		} 
		catch (IllegalStateException e) {

			mensagemSucesso = "Ocorreu o seguinte erro: " + e.getMessage();
			e.printStackTrace();

		}

		return id;

	}

	@Override
	public Estoque pesquisar(Integer id) {
		return estoqueGerencia.pesquisarId(id);
	}

	@Override
	public void removerLista(Integer id) {

		Estoque est = new Estoque();
		est.setId(id);
		lstObjetos.remove(est);

	}
	
	@Override
	public boolean onCancelarClick() {
		
		boolean retorno = super.onCancelarClick();
		FieldSet configs = (FieldSet) formEstoque.getControl("configuracoes");
		
		if (configs != null)
			formEstoque.remove(configs);

		return retorno;
		
	}

}
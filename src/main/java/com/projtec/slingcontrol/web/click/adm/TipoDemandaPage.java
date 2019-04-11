package com.projtec.slingcontrol.web.click.adm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Table;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.util.Bindable;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.TipoDemandaGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.web.click.ControlesHTML;
import com.projtec.slingcontrol.web.click.CrudPage;

@Component
public class TipoDemandaPage extends CrudPage<TipoDemanda> {

	@Bindable
	public String title = getMessage("titulo");

	private static final long serialVersionUID = 1L;

	@Bindable
	protected Form formTipoDemanda = new Form();

	protected List<TipoCampo> lstTipoCampo;

	@Resource(name = "tipoDemandaGerencia")
	protected TipoDemandaGerencia tipoDemandaGerencia;

	public TipoDemandaPage() {
		super();
		montarForm();
	}

	@Override
	public boolean permitidoSalvar() {

		return Secure.isPermitted("botoes:tipoDemanda:salvar");
	}

	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:tipoDemanda:pesquisar");
	}

	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:tipoDemanda:limpar");
	}

	@Override
	public void onInit() {
		super.onInit();

	}

	protected void montarGrid() {

		Column column = new Column("descricao");
		grid.addColumn(column);

		column = new Column("action");
		column.setWidth("120px;");
		column.setSortable(false);

		ActionLink[] links = new ActionLink[] { editLink, deleteLink };
		column.setDecorator(new LinkDecorator(grid, links, "id"));
		deleteLink.setAttribute("onclick",
				"return window.confirm('Deseja realmente remover ?');");
		grid.addColumn(column);

	}

	private void montarForm() {

		FieldSet fieldSet = new FieldSet("tipoDemanda");

		Field descricaoField = ControlesHTML.textField("descricao");
		descricaoField.setRequired(false);
		descricaoField.setLabel(descricaoField.getLabel()
				+ " <span class='required'>*</span>");
		descricaoField.setAttribute("class", "validate[required]");
		fieldSet.add(descricaoField);
		HiddenField idField = new HiddenField("id", Integer.class);
		fieldSet.add(idField);
		formTipoDemanda.add(fieldSet);

	}

	@Override
	public Form getForm() {

		return formTipoDemanda;
	}

	@Override
	public Table getTable() {

		return grid;
	}

	@Override
	public Boolean setFormObjeto() {
		Integer id = editLink.getValueInteger();
		TipoDemanda tpDemanda = tipoDemandaGerencia.pesquisarId(id);
		if (tpDemanda != null) {

			formTipoDemanda.getField("descricao").setValue(
					tpDemanda.getDescricao());
			formTipoDemanda.getField("id").setValueObject(tpDemanda.getId());

		}
		return true;

	}

	@Override
	public TipoDemanda montarObjeto() {
		Integer id = getIDFormObj();

		String descricao = formTipoDemanda.getFieldValue("descricao");

		TipoDemanda tipoDemanda = new TipoDemanda();
		tipoDemanda.setId(id);
		tipoDemanda.setDescricao(descricao);
		return tipoDemanda;

	}

	@Override
	public Boolean removerObjeto(Integer id) {

		boolean retorno = tipoDemandaGerencia.excluir(id);
		mensagemSucesso = "Tipo de Demanda removido com sucesso";
		
		return retorno;
		
	}

	@Override
	public List<TipoDemanda> pesquisar(TipoDemanda objeto) {

		List<TipoDemanda> tipoDemandaList = new ArrayList<TipoDemanda>(tipoDemandaGerencia.pesquisar(objeto));
		
		if (CollectionUtils.isEmpty(tipoDemandaList))
			mensagemErro = "N&atilde;o foram encontrados Tipo de Demanda com os dados informados";
		
		return tipoDemandaList;
		
	}

	@Override
	public void alterar(TipoDemanda obj) {
		tipoDemandaGerencia.alterar(obj);

	}

	@Override
	public Integer getIDFormObj() {
		String strId = formTipoDemanda.getFieldValue("id");
		Integer id;

		if (strId != null && strId.equals("")) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}
		return id;
	}

	@Override
	public Integer gravar(TipoDemanda obj) {
		TipoDemanda estNovo = tipoDemandaGerencia.incluir(obj);
		return estNovo.getId();
	}

	@Override
	public TipoDemanda pesquisar(Integer id) {
		return tipoDemandaGerencia.pesquisarId(id);

	}

	@Override
	public void removerLista(Integer id) {
		TipoDemanda tipoDemanda = new TipoDemanda();
		tipoDemanda.setId(id.intValue());
		lstObjetos.remove(tipoDemanda);

	}

	@Override
	public boolean onSalvarClick() {

		try {

			FieldSet tipoDemandaFieldSet = (FieldSet) formTipoDemanda.getControl("tipoDemanda");
			validaCampos(tipoDemandaFieldSet);

			boolean retorno = super.onSalvarClick();
			mensagemSucesso = "Tipo de Demanda salvo com sucesso";

			return retorno;

		} catch (Exception e) {

			mensagemErro = e.getMessage();
			return false;

		}

	}

}
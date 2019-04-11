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

import com.projtec.slingcontrol.gerencia.TipoMovimentoAtivoGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.TipoMovimentoAtivo;
import com.projtec.slingcontrol.web.click.ControlesHTML;
import com.projtec.slingcontrol.web.click.CrudPage;

@Component
public class TipoMovimentoAtivoPage extends CrudPage<TipoMovimentoAtivo> {

	private static final long serialVersionUID = 1L;

	@Bindable
	public String title = getMessage("titulo");

	@Bindable
	protected Form formTipoMovimentoAtivo = new Form();

	@Resource(name = "tipoMovimentoAtivoGerencia")
	protected TipoMovimentoAtivoGerencia tipoMovimentoAtivoGerencia;

	public TipoMovimentoAtivoPage() {
		super();
		montarForm();
	}

	@Override
	public boolean permitidoSalvar() {
		return Secure.isPermitted("botoes:tipoMovimentoativo:salvar");
	}

	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:tipoMovimentoativo:pesquisar");
	}

	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:tipoMovimentoativo:limpar");
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
		FieldSet fieldSet = new FieldSet("tipoMovimentoAtivo");
		Field nomeField = ControlesHTML.textField("descricao");
		nomeField.setRequired(false);
		nomeField.setLabel(nomeField.getLabel()
				+ " <span class='required'>*</span>");
		nomeField.setAttribute("class", "validate[required]");
		fieldSet.add(nomeField);
		HiddenField idField = new HiddenField("id", Integer.class);
		fieldSet.add(idField);
		formTipoMovimentoAtivo.add(fieldSet);
	}

	@Override
	public Form getForm() {

		return formTipoMovimentoAtivo;
	}

	@Override
	public Table getTable() {

		return grid;
	}

	@Override
	public Boolean setFormObjeto() {
		Integer id = editLink.getValueInteger();
		TipoMovimentoAtivo tpAtivo = tipoMovimentoAtivoGerencia.pesquisarId(id);
		if (tpAtivo != null) {

			formTipoMovimentoAtivo.getField("descricao").setValue(
					tpAtivo.getDescricao());
			formTipoMovimentoAtivo.getField("id").setValueObject(
					tpAtivo.getId());

		}
		return true;
	}

	@Override
	public TipoMovimentoAtivo montarObjeto() {
		Integer id = getIDFormObj();

		String descricao = formTipoMovimentoAtivo.getFieldValue("descricao");

		TipoMovimentoAtivo tipoMovimentoAtivo = new TipoMovimentoAtivo();
		tipoMovimentoAtivo.setId(id);
		tipoMovimentoAtivo.setDescricao(descricao);
		return tipoMovimentoAtivo;

	}

	@Override
	public Boolean removerObjeto(Integer id) {

		boolean retorno = tipoMovimentoAtivoGerencia.excluir(id);
		mensagemSucesso = "Tipo de Demanda removido com sucesso";
		
		return retorno;
		
	}

	@Override
	public List<TipoMovimentoAtivo> pesquisar(TipoMovimentoAtivo objeto) {

		List<TipoMovimentoAtivo> tipoMovimentoAtivoList = new ArrayList<TipoMovimentoAtivo>(tipoMovimentoAtivoGerencia.pesquisar(objeto));
		
		if (CollectionUtils.isEmpty(tipoMovimentoAtivoList))
			mensagemErro = "N&atilde;o foram encontrados Tipo de Movimento de Ativo com os dados informados";
		
		return tipoMovimentoAtivoList;
		
	}

	@Override
	public void alterar(TipoMovimentoAtivo obj) {
		tipoMovimentoAtivoGerencia.alterar(obj);

	}

	@Override
	public Integer getIDFormObj() {
		String strId = formTipoMovimentoAtivo.getFieldValue("id");
		Integer id;

		if (strId != null && strId.equals("")) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}
		return id;
	}

	@Override
	public Integer gravar(TipoMovimentoAtivo obj) {
		TipoMovimentoAtivo estNovo = tipoMovimentoAtivoGerencia.incluir(obj);
		return estNovo.getId();
	}

	@Override
	public TipoMovimentoAtivo pesquisar(Integer id) {
		return tipoMovimentoAtivoGerencia.pesquisarId(id);

	}

	@Override
	public void removerLista(Integer id) {
		TipoMovimentoAtivo tipoMovimentoAtivo = new TipoMovimentoAtivo();
		tipoMovimentoAtivo.setId(id.intValue());
		lstObjetos.remove(tipoMovimentoAtivo);

	}

	@Override
	public boolean onSalvarClick() {

		try {

			FieldSet tipoMovimentoAtivoFieldSet = (FieldSet) formTipoMovimentoAtivo.getControl("tipoMovimentoAtivo");
			validaCampos(tipoMovimentoAtivoFieldSet);

			boolean retorno = super.onSalvarClick();
			mensagemSucesso = "Tipo de Movimento de Ativo salvo com sucesso";

			return retorno;

		} catch (Exception e) {

			mensagemErro = e.getMessage();
			return false;

		}

	}

}

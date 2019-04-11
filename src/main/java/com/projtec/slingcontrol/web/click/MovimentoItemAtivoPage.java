package com.projtec.slingcontrol.web.click;

import javax.annotation.Resource;

import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Label;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.extras.control.SubmitLink;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.gerencia.LocaisGerencia;
import com.projtec.slingcontrol.gerencia.MovimentoAtivoGerencia;
import com.projtec.slingcontrol.infra.Secure;

public class MovimentoItemAtivoPage  extends FormPage
{

	private static final long serialVersionUID = 1L;

	//private static final String NOME_LAYOUT_LOCAIS = "Locais";
	
	@Bindable
	protected Form movimentoItemAtivoForm = new Form("movimentoItemDeAtivoForm");
		
	private Select select = new Select("layout");
	private Select origem = new Select("estoqueOrigem");
	private Select destino = new Select("estoqueDestino");
	
	@Bindable
	protected ActionLink editLink = new ActionLink("edit", "Editar", this,
			"onEditClick");

	@Bindable
	protected ActionLink deleteLink = new ActionLink("delete", "Remover", this,
			"onDeleteClick");
	
	@Bindable 
	protected SubmitLink cancelar = new SubmitLink("Cancelar");
	
	@Bindable
	protected String mesg;
	
	@Resource(name = "locaisGerencia")
	protected LocaisGerencia locaisGerencia;
	
	@Resource(name = "ativosGerencia")
	protected AtivosGerencia ativosGerencia;
	
	@Resource(name = "movimentoAtivoGerencia")
	protected MovimentoAtivoGerencia movimentoAtivoGerencia;

	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;
	
	 @Resource(name="configuracoesUtil") 
	 protected ConfiguracoesUtil configuracoesUtil;
	 
	 @Resource(name="informacoesUtil") 
	 protected InformacoesUtil informacoesUtil; 
	public MovimentoItemAtivoPage()
	{

	}
	
	@Override
	public boolean permitidoSalvar() {
	
		return Secure.isPermitted("ativos:salvar");
	}
	
	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("ativos:pesquisar");
	}
	
	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("ativos:limpar");
	}
	
	@Override
	public void onInit() {
		test = true;
		String acao = getContext().getRequestParameter("actionLink");
		if (acao != null) {
			if (acao.equalsIgnoreCase("Pesquisar")
					& getForm().getControl("tipoDemanda") != null) {
				onPesquisaClick();
			} else if (acao.equalsIgnoreCase("Limpar")) {
				onCancelarClick();
			}
		}
		super.onInit();
		initMovimentoItemAtivo();
//		listaSessao();
	}
	
	private void initMovimentoItemAtivo()
	{

		HiddenField idField = new HiddenField("id", Integer.class);
		movimentoItemAtivoForm.add(idField);

		FieldSet movimentoItemAtivoDados = new FieldSet("MovimentoItemDeAtivo");
		
		//Nome do campo	
		Label label = new Label("Item do Ativo 1 ");
		movimentoItemAtivoDados.add(label);
				
		//Identificacao
		Field identItemAtivos = ControlesHTML.textField("Identifica&ccedil;&atilde;o");
		identItemAtivos.setRequired(false);
		identItemAtivos.setLabel(identItemAtivos.getLabel() + " <span class='required'>*</span>");
		identItemAtivos.setAttribute("class", "validate[required]");
		identItemAtivos.setStyle("width", "5cm");
		movimentoItemAtivoDados.add(identItemAtivos);
		
		origem.add(new Option("","-- Selecione --"));
	//	origem.addAll(obterLayouts());
		origem.setRequired(false);
		origem.setLabel(origem.getLabel() + " <span class='required'>*</span>");
		origem.setAttribute("class", "validate[required]");
		movimentoItemAtivoDados.add(origem);
		
		destino.add(new Option("","-- Selecione --"));
	//	destino.addAll(obterLayouts());
		destino.setRequired(false);
		destino.setLabel(destino.getLabel() + " <span class='required'>*</span>");
		destino.setAttribute("class", "validate[required]");
		movimentoItemAtivoDados.add(destino);
//--------------------------------
		movimentoItemAtivoForm.add(movimentoItemAtivoDados);
		select.setAttribute("onchange","Click.submit(movimentoAtivoForm, false)");	
		montarBotoes();
		
		addControl(movimentoItemAtivoForm);

// Bind the form field request values
		ClickUtils.bind(movimentoItemAtivoForm);

	}
	
	
	@Override
	protected void montarGrid() {

		Column column;

		column = new Column("tipomovimento.descricao");
		grid.addColumn(column);

		column = new Column("demandaId.tipoDemanda.descricao");
		grid.addColumn(column);

		column = new Column("layoutId.nome");
		grid.addColumn(column);

		column = new Column("action");
		column.setWidth("120px;");
		column.setSortable(false);

		ActionLink[] links = new ActionLink[] { editLink, deleteLink };
		column.setDecorator(new LinkDecorator(grid, links, "id"));
		grid.addColumn(column);

		deleteLink.setAttribute("onclick",
				"return window.confirm('Deseja realmente remover ?');");
				
	}
	@Override
	public Form getForm() {
		return movimentoItemAtivoForm;
	}

	@Override
	protected boolean onPesquisaClick() {
		return false;
	}

	@Override
	protected boolean onCancelarClick() {
		return false;
	}

	@Override
	protected boolean onSalvarClick() {
		return false;
	}
}

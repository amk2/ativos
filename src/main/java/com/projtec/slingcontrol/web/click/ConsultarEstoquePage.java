package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.ActionListener;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.TextField;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.EstoqueGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Estoque;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;

@Component
public class ConsultarEstoquePage extends BorderPage {

	private static final long serialVersionUID = 9192636304205517901L;
	private static final String NOME_LAYOUT = "Ativos";
	
	@Bindable
	public String title = getMessage("titulo");

	public List<Estoque> lstEstoque;
	@Bindable
	public String mesgPesquisa;

	@Bindable
	private Map<Integer,ListaGridView> mapEstoqueView = new HashMap<Integer,ListaGridView>();
	
	@Bindable
	protected Form formEstoque = new Form("formEstoque");
	
	private Select estoqueField = new Select("estoque");
	
	private Select layoutField = new Select("layoutAtivo");
	
	private TextField identificacao = new TextField("identificacao");

	@Resource(name = "ativosGerencia")
	public AtivosGerencia ativosGerencia;
	
	@Resource(name = "layoutGerencia")
	public LayoutGerencia layoutGerencia;
	
	@Resource(name = "configuracoesUtil")
	public ConfiguracoesUtil configuracoesUtil;
	
	@Resource(name = "informacoesUtil")
	public InformacoesUtil informacoesUtil;
	
	@Resource(name = "consultarEstoqueUtil")
	protected ConsultarEstoqueUtil consultarEstoqueUtil;
	
	@Resource(name = "estoqueGerencia")
	protected EstoqueGerencia estoqueGerencia;


	public ConsultarEstoquePage() {
		super();
	}


	@SuppressWarnings("serial")
	@Override
	public void onInit() {
		super.onInit();
		estoqueField.setDataProvider(new DataProvider<Option>() {
			@Override
			public Iterable<Option> getData() {
				List<Option> optionList = new ArrayList<Option>();
				optionList.add(new Option("", "-- Selecione --"));
			
				Collection<Estoque> lstEstoque = estoqueGerencia.obterTodos();
				for (Iterator<Estoque> iterator = lstEstoque.iterator(); iterator
						.hasNext();) {
					Estoque estoque = iterator.next();
					optionList.add(new Option(estoque.getId(), estoque
							.getNome()));
				}
				return optionList;
			}
		});
		layoutField.setDataProvider(new DataProvider<Option>() {
			@Override
			public Iterable<Option> getData() {
				List<Option> optionList = new ArrayList<Option>();
				optionList.add(new Option("", "-- Selecione --"));
				//Collection<Layout> lstLayout = layoutGerencia.obterTodos();   
				Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDESTOQUE);

						//ativosGerencia.obterTodosNomesLayouts(NOME_LAYOUT);
				for (Layout layout : lstLayout) {
					optionList.add(new Option(layout.getId(), layout.getNome()));
				}
				return optionList;
			}
		});
		
		
		montarForm();
		montarBotoes();
	}

	protected void montarGrid() {
		Column column;
		column = new Column("ativo.layout.nome");
		column.setWidth("150px;");
		grid.addColumn(column);
		column = new Column("ativo.qtdItens");
		column.setWidth("150px;");
		grid.addColumn(column);
		column = new Column("action");
		column.setWidth("120px;");
		column.setSortable(false);
		column.setDecorator(new Decorator() {
		     public String render(Object row, Context context) {
		         ItensAtivos itensAtivos = (ItensAtivos) row;
		         return "<a href='pesquisarItens.htm?ativo=" + itensAtivos.getAtivo().getId() + "' class='exibirItensAtivo'> <img src='resources/images/lupa.png' /> </a>";
		     }
		 });
		grid.addColumn(column);
	}

	private void montarForm() {
		
		FieldSet fieldSet = new FieldSet("consultarEstoque");
		fieldSet.add(estoqueField);				
		fieldSet.add(identificacao);
		fieldSet.add(layoutField);
		layoutField.setAttribute("onchange", "Click.submit(formEstoque, false)");		
		formEstoque.add(fieldSet);
		ClickUtils.bind(formEstoque);
		obterConfiguracoes(null);
	}



	public Ativos montarAtivo() {
		Ativos ativo = new Ativos();
		
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		Layout layoutId = new Layout();
		
		if (layoutField.getValue() != null && !"".equals(layoutField.getValue())) 
		{
			layoutId.setId(Integer.valueOf(layoutField.getValue()));
		}
		
		ativo.setLayout(layoutId);

		FieldSet configs = (FieldSet) formEstoque.getControl("configuracoes");

		if (configs != null) 
		{
			lstInfos = informacoesUtil.montarInfomacoes(configs);
			ativo.setInformacoes(lstInfos);
		}
		return ativo;
	}

	public boolean onPesquisaClick() {
		
		Integer idEstoque = obterIdEstoque();
		String codIdentificacao = identificacao.getValue();
		Ativos atv= montarAtivo();		
		List<Ativos> lstAtivos = ativosGerencia.pesquisarAtivosEstoque(idEstoque , codIdentificacao, atv);
		 if(lstAtivos!=null && lstAtivos.size() > 0 )
		 {
			 mapEstoqueView =  consultarEstoqueUtil.montarTabela(lstAtivos,true);
			 mesgPesquisa= null;
			 
		 }else
		 {
			 lstAtivos = new ArrayList<Ativos>();
			 mesgPesquisa = getMessage("nao_existe_resultado");
		 }
		 
		return true;
	}
	

	
	public Integer obterIdEstoque() 
	{	
		Integer idEstoque = null; 
		Select selectEstoque = (Select) formEstoque.getField("estoque");
		String id = null;
		if (selectEstoque != null) {
			id = selectEstoque.getValue();
			if (id != null && !"".equals(id)) {
				 idEstoque = Integer.parseInt(id);
			}
		}
		return idEstoque;
	}

	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) {
		if (layoutField.getValue() != null
				&& !"".equals(layoutField.getValue())) {
			Integer idLayout = Integer.valueOf(layoutField.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);
			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, formEstoque);

		}
	}
	
	
	public void montarBotoes() {
		HiddenField acao = new HiddenField("actionLink", "");
		getForm().add(acao);
				
		ImageSubmit pesquisar = new ImageSubmit("Pesquisar",
				"/resources/images/btnPesquisar.png");
		pesquisar.setAttribute("onclick", "validar('Pesquisar')");
		//pesquisar.setDisabled(!permitidoPesquisar());
		pesquisar.setStyle("border","1px solid black");
		pesquisar.setStyle("border-color","blue");
		if (pesquisar.isDisabled()){
			pesquisar.setStyle("border","1px solid black");
			pesquisar.setStyle("border-color","red");
		}
		pesquisar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {

				return onPesquisaClick();
			}
		});
		getForm().add(pesquisar);
		ImageSubmit cancelar = new ImageSubmit("Cancelar",
				"/resources/images/btnLimpar.png");
	//	cancelar.setAttribute("onclick", "validar('Limpar')");
		cancelar.setAttribute("onclick",
		"return window.confirm('Deseja realmente limpar dados ?');");
		//cancelar.setDisabled(!permitidoCancelar());
		cancelar.setStyle("border","1px solid black");
		cancelar.setStyle("border-color","blue");
		if (cancelar.isDisabled()){
			cancelar.setStyle("border","1px solid black");
			cancelar.setStyle("border-color","red");
		}
		cancelar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {

				return onCancelarClick();
			}
		});
		getForm().add(cancelar);
	}
	
	
	

	@Override
	public Form getForm() {
		
		return formEstoque;
	}


	@Override
	protected boolean onCancelarClick() {
		
		formEstoque.clearErrors();
		formEstoque.clearValues();
		FieldSet configs = (FieldSet) formEstoque.getControl("configuracoes");
		if(formEstoque!=null && configs!=null)
		{
			formEstoque.remove(configs);
		}
		
		return true;
	}


	@Override
	protected boolean onSalvarClick() {
		
		return false;
	}
	




}

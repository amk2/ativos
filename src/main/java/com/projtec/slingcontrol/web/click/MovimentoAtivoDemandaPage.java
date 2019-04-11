package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Field;
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

import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.gerencia.MovimentoAtivoGerencia;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.MovimentoAtivo;
import com.projtec.slingcontrol.modelo.StatusDemanda;
import com.projtec.slingcontrol.modelo.TipoMovimentoAtivo;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;

@Component
public class MovimentoAtivoDemandaPage extends FormPage
{

	@Bindable
	public String title = getMessage("titulo");
	
	//private static final String NOME_LAYOUT = "Movimentos";
	
	private Select select = new Select("layout");

	@Bindable
	protected Demandas objDemanda;
	
	@Bindable
	protected Form movimentoAtivoForm = new Form("movimentoAtivoForm");
	
	@Bindable 
	protected Integer idDemanda;

	@Bindable 
	protected String acaodaurl;
	
	@Bindable
	protected   Boolean ok = false;
	
	@Resource(name="demandaGerencia")
	protected DemandaGerencia demandaGerencia;
	
	@Resource(name="layoutGerencia")
	protected LayoutGerencia layoutGerencia;
	
	@Resource(name="configuracoesUtil") 
	protected ConfiguracoesUtil configuracoesUtil;

	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;
	
	
	@Resource(name = "movimentoAtivoGerencia")
	protected MovimentoAtivoGerencia movimentoAtivoGerencia;
	
	
	
	

	
	
	@Override
	public void onInit() 
	{
		super.onInit();
		objDemanda = demandaGerencia.pesquisarID(idDemanda);
		onInitMovimentoAtivoDemanda();
	}



	private void onInitMovimentoAtivoDemanda() 
	{
		montarBotoes();
		
		FieldSet movimentoAtivoDados = new FieldSet("MovimentoAtivo");
		HiddenField idField = new HiddenField("idDemanda", Integer.class);
		idField.setValueObject(idDemanda);
		movimentoAtivoDados.add(idField);

		HiddenField fielddaurl = new HiddenField("acaodaurl", String.class);
		fielddaurl.setValueObject(acaodaurl);
		movimentoAtivoDados.add(fielddaurl);
		
		Select selectTipoMovimentoAtivo = new Select("TipoMovimento");
		setRequerido(selectTipoMovimentoAtivo);
		selectTipoMovimentoAtivo.setDataProvider(new DataProvider<Option>()
		{
			@Override
			public Iterable<Option> getData()
			{
				List<Option> optionList = new ArrayList<Option>();
				optionList.add(new Option("","-- Selecione --"));

				Collection<TipoMovimentoAtivo> lstTipoMovimentoAtivo = movimentoAtivoGerencia
						.obterTodosTipoMovimento();
				for (Iterator<TipoMovimentoAtivo> iterator = lstTipoMovimentoAtivo
						.iterator(); iterator.hasNext();)
				{
					TipoMovimentoAtivo tipoMovimentoAtivo = iterator.next();
					optionList.add(new Option(tipoMovimentoAtivo.getId(),
							tipoMovimentoAtivo.getDescricao()));

				}
				return optionList;
			}

		});
		movimentoAtivoDados.add(selectTipoMovimentoAtivo);
		
		select.add(new Option("", "-- Selecione --"));
		select.addAll(obterLayouts());
		setRequerido(select);
		movimentoAtivoDados.add(select);
		movimentoAtivoForm.add(movimentoAtivoDados);
		//select.setAttribute("onchange","Click.submit(movimentoAtivoForm, false)");	
		select.setAttribute("onchange", "movimentoAtivoForm.submit();");
		
		addControl(movimentoAtivoForm);
		// Bind the form field request values
		ClickUtils.bind(movimentoAtivoForm);

		obterConfiguracoes(null);
		
		
	}
	
	

		
	
	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) {
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);

			// ConfiguracoesUtil.exibirConfiguracoes(layout, mapInfo,
			// demandasForm);

			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, movimentoAtivoForm);

		}
	}
	
	private Collection<Option> obterLayouts() {

		List<Option> optionList = new ArrayList<Option>();
		if(layoutGerencia==null)
		{
			optionList.add(new Option("","N&atilde;o existe"));
		}else
		{
			//Collection<Layout> lstLayout =  layoutGerencia.obterTodos();  // layoutGerencia.obterTodosNomesLayouts(NOME_LAYOUT);
			Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDMOVIMENTO);
			
			for (Layout layout : lstLayout) {
				optionList.add(new Option(layout.getId(), layout.getNome()));
			}
		}
		
		return optionList;
	}
	
	
	
	
	public void montarBotoes() {
		
		HiddenField acao = new HiddenField("actionLink", "");
		getForm().add(acao);
		
		ImageSubmit salvar = new ImageSubmit("Salvar",
		"/resources/images/btnSalvar.png");
		salvar.setAttribute("onclick", "validar('Salvar')");
		salvar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
				return onSalvarClick();
			}

			
		});
		
		movimentoAtivoForm.add(salvar);	
		
		
		
		ImageSubmit cancelar = new ImageSubmit("Fechar",
		"/resources/images/btnLimpar.png");
		cancelar.setAttribute("onclick",
		"return window.confirm('Deseja realmente cancelar a Movimenta&ccedil;&atilde;o?');");
		
		cancelar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
		
				return onCancelarClick();
			}
		});
		movimentoAtivoForm.add(cancelar);

		
		
			
	
		
	}
	
	private MovimentoAtivo montarMovimentoAtivo()
	{

		MovimentoAtivo movimentoativo = new MovimentoAtivo();
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		
		TipoMovimentoAtivo tipoMovimentoAtivoId;
		Layout layoutId ;
		Demandas demandasId;
		String strId = movimentoAtivoForm.getFieldValue("id");
		Integer id;
		if (strId == null) {
			id = 0;
		} else if (strId.length() <= 0) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}
		
		movimentoativo.setId(id);

		FieldSet movimentoAtivoDados = (FieldSet) movimentoAtivoForm.getControl("MovimentoAtivo");

		org.apache.click.control.Field fieldTipoMovimentoAtivo = movimentoAtivoDados.getField("TipoMovimento");

		if (fieldTipoMovimentoAtivo.getValue() != null
				&& !"".equals(fieldTipoMovimentoAtivo.getValue()))
		{
			tipoMovimentoAtivoId= new TipoMovimentoAtivo();
			tipoMovimentoAtivoId.setId(Integer.valueOf(fieldTipoMovimentoAtivo
					.getValue()));
			movimentoativo.setTipomovimento(tipoMovimentoAtivoId);
		}
		
		demandasId = new Demandas();
		demandasId.setId(idDemanda);
		
		movimentoativo.setDemandaId(demandasId);

		if (select.getValue() != null && !"".equals(select.getValue()))
		{
			layoutId = new Layout();
			layoutId.setId(Integer.valueOf(select.getValue()));
			movimentoativo.setLayoutId(layoutId);

		}	
	
		Collection<Field> fields;
		FieldSet configs = (FieldSet) movimentoAtivoForm.getControl("configuracoes");		
		if (configs != null) 
		{			
			lstInfos = informacoesUtil.montarInfomacoes(configs);
			movimentoativo.setInformacoesMovimentoAtivo(lstInfos);	

		}
		

		return movimentoativo;

	}
	
	
	
	
	public boolean onSalvarClick() {
		
		imageFields();
		
	   if (!movimentoAtivoForm.isValid())
			return false;
		
		MovimentoAtivo movAtivo = montarMovimentoAtivo();
		
		objDemanda = demandaGerencia.pesquisarIDdoStatusDaDemanda(movAtivo.getDemandaId().getId());
		if (objDemanda.getStatusDemanda().getDescricao().equals(StatusDemanda.ABERTO))
		{
			movimentoAtivoGerencia.executarDemanda(movAtivo);
			Map<String, String> params = new HashMap<String, String>();
			params.put("mensagemSucesso", " A Demanda foi executada com sucesso");
			setRedirect(MovimentoAtivoPage.class , params);
			
		}
		/*if (objDemanda.getStatusDemanda().getDescricao().equals(StatusDemanda.EM_ANDAMENTO))
		{
			movimentoAtivoGerencia.finalizardemanda(movAtivo);
			Map<String, String> params = new HashMap<String, String>();
			params.put("mensagemSucesso", " A Demanda foi finalizada com sucesso");
			setRedirect(MovimentoAtivoPage.class , params);
			
		}*/
		ok=true;
		return true;
		
	}

	@Override
	protected void montarGrid() {
	}

	@Override
	public Form getForm() {
		return movimentoAtivoForm;
	}

	@Override
	protected boolean onPesquisaClick() {
		return false;
	}

	@Override
	protected boolean onCancelarClick() {
		
		setRedirect(MovimentoAtivoPage.class);
		
		return true;
	}
	
	
	private void setRequerido(Field campo )
	{
		campo.setRequired(false);
		campo.setLabel(campo.getLabel()
				+ " <span class='required'>*</span>");
		campo.setAttribute("class", "validate[required]");
	}

}

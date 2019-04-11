package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
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
import org.apache.click.extras.control.HiddenList;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;

public class HomePage extends BorderPage
{

	@Bindable
	public String title = getMessage("titulo");

	@Autowired
	private ApplicationContext appContext;
	
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

	Select layoutField = new Select("layout");
	
	@Bindable
	protected String mesgPesquisa = new String();

	@Bindable
	protected Form homepageForm = new Form("homepageForm");

	private Select select = new Select("layout");
	
	
	//==================================================================================================
	@Override
	protected boolean onPesquisaClick() {
		return false;
	}

	@Override
	protected boolean onCancelarClick() {
		homepageForm.clearValues();
		homepageForm.clearErrors();
		mesgPesquisa= null;

		return true;
	}

	@Override
	public Form getForm() {
		return homepageForm;
	}

	@Override
	protected boolean onSalvarClick() {
		return false;
	} 
	//====================================================================================================
	
	@Override
	public void onInit() 
	{
		
		carregaTela();
		
		super.onInit();
		
	}

	public void carregaTela()
	{
		//HiddenField idField = new HiddenField("id", Integer.class);
		//homepageForm.add(idField);
	
		FieldSet fieldSet = new FieldSet("pesquisa");

        TextField nomeField = new TextField("tagRFID"); 
		nomeField.setTabIndex(1);
		nomeField.setSize(40);
		nomeField.setMaxLength(40);
		nomeField.setTrim(true);
		nomeField.setRequired(false);
		nomeField.setLabel(nomeField.getLabel() + " <span class='required'>*</span>");
		nomeField.setAttribute("class", "validate[required]");
		fieldSet.add(nomeField);
		
		homepageForm.add(fieldSet);
		
		select.setRequired(false);
		select.setLabel(select.getLabel() + " <span class='required'>*</span>");
		select.setAttribute("class", "validate[required]");
		select.getOptionList().clear();
		select.add(new Option("", "-- Selecione --"));
		select.addAll(obterLayouts());

		fieldSet.add(select);
		
		
		
		

		homepageForm.add(fieldSet);
		
		addControl(homepageForm);
		
		ClickUtils.bind(homepageForm);
		
		montasBotoes();

		obterConfiguracoes(null);
		
		
	}

	private void montasBotoes()
	{
	    ImageSubmit pesquisar = new ImageSubmit("Pesquisar",
				"/resources/images/btnPesquisar.png");
		pesquisar.setAttribute("onclick", "validar('Pesquisar')");
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
		cancelar.setAttribute("onclick",
		"return window.confirm('Deseja realmente limpar dados ?');");
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
	
	
	private void obterConfiguracoes(Map<Integer, Informacoes> mapInfo) 
	{
		if (select.getValue() != null && !"".equals(select.getValue())) {
			Integer idLayout = Integer.valueOf(select.getValue());
			Layout layout = layoutGerencia.pesquisarId(idLayout);	
			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, homepageForm);
		}
	}

	
	
	private Collection<Option> obterLayouts() {
		List<Option> optionList = new ArrayList<Option>();
		Collection<Layout> lstLayout;
		lstLayout = layoutGerencia.obterTodos();

		for (Layout layout : lstLayout) 
		{
			optionList.add(new Option(layout.getId(), layout.getNome()));
		}
		
		return optionList;
	}
	
	
	
}

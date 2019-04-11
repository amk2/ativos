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
public class ConsultarRelatorioPage extends BorderPage {

	private static final long serialVersionUID = 9192636304205517901L;
	
	@Bindable
	public String title = getMessage("titulo");

	public List<Estoque> lstEstoque;
	@Bindable
	public String mesgPesquisa;

	@Bindable
	protected Form relatorioForm = new Form("relatorioForm");
	
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


	public ConsultarRelatorioPage() {
		super();
	}


	@SuppressWarnings("serial")
	@Override
	public void onInit() {
		super.onInit();

		//montarForm();
		montarBotoes();		
	}


	public boolean onPesquisaClick() {
		 
		return true;
	}
	

	public void montarBotoes() {
		HiddenField acao = new HiddenField("actionLink", "");
		getForm().add(acao);
				
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
	
	
	@Override
	public Form getForm() {
		return relatorioForm;
	}


	@Override
	protected boolean onCancelarClick() {
		
		relatorioForm.clearErrors();
		relatorioForm.clearValues();
		FieldSet configs = (FieldSet) relatorioForm.getControl("configuracoes");
		if(relatorioForm!=null && configs!=null)
		{
			relatorioForm.remove(configs);
		}
		
		return true;
	}


	@Override
	protected boolean onSalvarClick() {
		
		return false;
	}
	

}

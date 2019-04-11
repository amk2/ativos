package com.projtec.slingcontrol.web.click;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.ActionListener;
import org.apache.click.ActionResult;
import org.apache.click.Control;
import org.apache.click.ControlRegistry;
import org.apache.click.Page;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.web.tipocampo.ConstantesAtivos;

@Component
public class PesquisarAtivosPage extends BorderPage implements Serializable
{
	private static final String NOME_LAYOUT = "Ativos";
	
	@Bindable
	protected Form pesquisarAtivosForm = new Form("pesquisarAtivosForm");
	
	private Select select = new Select("layout", true);
	
	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;
	
	@Bindable
	private ActionLink pesquisarLink = new ActionLink("pesquisarLink", "Pesquisar");
	
	@Bindable
	private ActionLink limparLink = new ActionLink("limparLink", "Limpar");
	
	@Resource(name = "ativosGerencia")
	protected AtivosGerencia ativosGerencia;

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;
	
	@Resource(name = "ativosUtil")
	protected AtivosUtil ativosUtil;
	
	@Bindable
	protected String mensagemErro = new String();
	
	@Bindable
	protected String mensagemSucesso = new String();
	
	@Autowired
	private ApplicationContext appContext;

	public PesquisarAtivosPage()
	{
		remoteAjax();	
	}

	private void remoteAjax()
	{	
		pesquisarLink.setId("pesquisarID");
		pesquisarLink.setImageSrc("/resources/images/btnPesquisar.png");
		
		pesquisarLink.addBehavior(new DefaultAjaxBehavior() {
			
			@Override
			public ActionResult onAction(org.apache.click.Control source) {
				
				String json = onPesquisaComRetornoJSON();
				ActionResult result = new ActionResult(json, ActionResult.JSON);
                
                return result;              
                
			};
           
        });
		
		limparLink.setId("limparID");
		limparLink.setImageSrc("/resources/images/btnLimpar.png");
		limparLink.setAttribute("style", "margin-right: 10px;");
		limparLink.setAttribute("onclick",
			"return window.confirm('Deseja realmente limpar dados ?');");
		
		limparLink.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
		
				return true;
			}
		});
       
		ControlRegistry.registerAjaxTarget(pesquisarAtivosForm);	
		
	}
	
	public String onPesquisaComRetornoJSON(){
		
		Ativos atv = montarAtivos();
        List<Ativos> listaAtivos = ativosGerencia.pesquisar(atv);
        
		if (listaAtivos != null && listaAtivos.size() > 0 ) {
			
			return ativosUtil.ativosDinamicoJSON(listaAtivos);
			 
		}
		else {
			
			return mensagemErro = "N&atilde;o foram encontrados dados com os parametros informados.";
			 
		}

		
	}
	
	@Override
	public void onInit()
	{
		showMenu = false;
		super.onInit();
		initAtivos();
	}
	
	private void initAtivos() {
		
	    pesquisarAtivosForm.setValidate(false);
	
		FieldSet ativosDados = new FieldSet("ativoDados");
		ativosDados.setLabel("Pesquisar Ativos");

        //select.add(new Option("","-- Selecione --"));
        select.addAll(obterLayouts());//obterLayouts()
        select.setAttribute("class", "validate[required]");
        ativosDados.add(select);        
        
        pesquisarAtivosForm.add(ativosDados);      
        
        select.setAttribute("onchange", "pesquisarAtivosForm.submit();");
        
        addControl(pesquisarAtivosForm);
        
        ClickUtils.bind(pesquisarAtivosForm);
        
        
        
        ImageSubmit pesquisar = new ImageSubmit("Pesquisar2",
				"/resources/images/btnPesquisar.png");
		pesquisar.setStyle("border","1px solid black");
		pesquisar.setStyle("border-color","blue");
		
		if (pesquisar.isDisabled()){
			pesquisar.setStyle("border-color","red");
		}

		pesquisar.setActionListener(new ActionListener() {
			@Override
			public boolean onAction(Control source) {
				
				onPesquisaComRetornoJSON();
				return true;
				
			}
		});
		
		pesquisarAtivosForm.add(pesquisar);
        

        obterConfiguracoes(null);
        
	}

	private Collection<Option> obterLayouts() {
		
		List<Option> optionList = new ArrayList<Option>();
	    //Collection<Layout> lstLayout = layoutGerencia.obterTodos();
	    
	    Collection<Layout> lstLayout = layoutGerencia.obterTodosComID(ConstantesAtivos.IDATIVO);
		 
	    for (Layout layout : lstLayout) {
	           optionList.add(new Option(layout.getId(), layout.getNome()));
	       }
	       return optionList;
	}
	
	private void obterConfiguracoes( Map<Integer, Informacoes> mapInfo) 
	{
		if (select.getValue()!=null && !"".equals(select.getValue()) ) 
		{						
			Integer idLayout = Integer.valueOf(select.getValue());			
			Layout layout = layoutGerencia.pesquisarId(idLayout);
			configuracoesUtil.exibirConfiguracoes(layout, mapInfo, pesquisarAtivosForm);
          
        }
	}	
	
	private Ativos montarAtivos() 
	{
		Ativos ativos = new Ativos();
		Collection<Informacoes> lstInfos = new ArrayList<Informacoes>();
		Layout layoutId = new Layout();

		String strId = pesquisarAtivosForm.getFieldValue("id");
		Integer id;
		if (strId == null) {
			id = 0;
		} else if (strId.length() <= 0) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}
		ativos.setId(id);
		FieldSet atvDados = (FieldSet) pesquisarAtivosForm.getControl("ativoDados");
		if (select.getValue() != null && !"".equals(select.getValue())) {
			layoutId.setId(Integer.valueOf(select.getValue()));
		}
		ativos.setLayout(layoutId);
		FieldSet configs = (FieldSet) pesquisarAtivosForm.getControl("configuracoes");
		if (configs != null) {
			List<Field> fields = configs.getFieldList();
			for (Field field : fields) {
				String s = getContext().getRequestParameter(field.getName());
				configs.getField(field.getName()).setValue(s);
			}
			lstInfos = informacoesUtil.montarInfomacoes(configs);
			ativos.setInformacoes(lstInfos);
		}
		return ativos;
	}

	@Override
	public Form getForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean onPesquisaClick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean onCancelarClick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean onSalvarClick() {
		// TODO Auto-generated method stub
		return false;
	}

}
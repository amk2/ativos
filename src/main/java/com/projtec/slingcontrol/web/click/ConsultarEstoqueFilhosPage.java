package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.click.control.Form;
import org.apache.click.util.Bindable;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.gerencia.EstoqueGerencia;
import com.projtec.slingcontrol.gerencia.LayoutGerencia;
import com.projtec.slingcontrol.gerencia.LocaisGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.AtivosNo;

@Component
public class ConsultarEstoqueFilhosPage  extends BorderPage
{

	
	
	@Bindable
	protected Integer ativo;
	
	@Bindable
	private Map<Integer,ListaGridView> mapEstoqueView = new HashMap<Integer,ListaGridView>();	

	@Resource(name = "ativosGerencia")
	protected AtivosGerencia ativosGerencia;

	@Resource(name = "layoutGerencia")
	protected LayoutGerencia layoutGerencia;

	@Resource(name = "estoqueGerencia")
	protected EstoqueGerencia estoqueGerencia;

	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;
	
	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;
	
	@Resource(name = "locaisGerencia")
	protected LocaisGerencia locaisGerencia;
	
	@Resource(name = "consultarEstoqueUtil")
	protected ConsultarEstoqueUtil consultarEstoqueUtil;
	
	@Override
	public void onInit() 
	{
		
		super.onInit();
		showMenu = false;
		
		if (ativo!=null)
		{
			List<AtivosNo> lstPaiFilho = new ArrayList<AtivosNo>();
			ativosGerencia.getFilhos(ativo, lstPaiFilho);
			List<Ativos> ativosFilhoEstoque = new ArrayList<Ativos>();
			
			for (Iterator iterator = lstPaiFilho.iterator(); iterator.hasNext();) {
				AtivosNo ativosNo = (AtivosNo) iterator.next();
				if (ativosNo.getPai().equals(ativo))
				{
					Integer filho=ativosNo.getFilho();
					if (!filho.equals(ativo))
					{
						Ativos atv = new Ativos();
						
						atv.setId(filho);
						  List<Ativos>  lstAtvs = ativosGerencia.pesquisarAtivosEstoque(null, null , atv);
						  if (lstAtvs!=null && lstAtvs.size() > 0 )
						  {
							  ativosFilhoEstoque.addAll(lstAtvs);
						  }
						
					}
					
				}
			}		
			
			mapEstoqueView = consultarEstoqueUtil.montarTabela(ativosFilhoEstoque, false);
		}	
		
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

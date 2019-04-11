package com.projtec.slingcontrol.web.click;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Select;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.HiddenList;
import org.apache.click.util.Bindable;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.DemandaGerencia;
import com.projtec.slingcontrol.gerencia.LocaisGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.DemandasItemAtivo;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.modelo.StatusDemanda;
import com.projtec.slingcontrol.web.exception.ValidacaoCampoException;


@Component
public class DemandasFormItensPage extends FormPage
{

	private static final String DEM_ENTRADA = "DEM_ENTRADA";
	private static final String DEM_SAIDA = "DEM_SAIDA";
	private static final String DEM_MOV_ATV = "DEM_MOV_ATV";
	
	@Bindable
	protected Form demandasForm = new Form("demandasForm");
	
	@Resource(name = "locaisGerencia")
	protected LocaisGerencia locaisGerencia;
	
	@Resource(name = "demandaGerencia")
	protected DemandaGerencia demandaGerencia;
	
	@Override
	public void onInit() {
		
		super.onInit();
		initForm();
	}
	
	private void initForm() {
		
		FieldSet demandasDados = new FieldSet("DemandasDados");
		
		HiddenList hiddenListAtivos = new HiddenList("itemAtivoEntrada"); 

		HiddenList hiddenListAtivos1 = new HiddenList("itemAtivosMovAtivo"); 

		demandasForm.add(hiddenListAtivos);
		demandasForm.add(hiddenListAtivos1);

		RadioGroup tipoMovimentacao = new RadioGroup("tipoMovimentacao");

		tipoMovimentacao.add(new Radio(DEM_MOV_ATV, getMessage("movAtivo")));
		tipoMovimentacao.add(new Radio(DEM_ENTRADA,
				getMessage("entradaAtivo")));
		tipoMovimentacao.add(new Radio(DEM_SAIDA,
				getMessage("saidaAtivo")));
		tipoMovimentacao.setVerticalLayout(false);

		demandasDados.add(tipoMovimentacao);
		
		Select selectLocais = new Select("locais");
		selectLocais.setId("ComboBoxLocais");
		selectLocais.setSize(1);
		selectLocais.setLabel("");
		selectLocais.setStyle("display", "none");

		selectLocais.setDataProvider(new DataProvider<Option>() {

			@Override
			public Iterable<Option> getData() {
				List<Option> optionList = new ArrayList<Option>();
				Collection<Local> lstLocais = locaisGerencia.obterTodos();
				for (Iterator<Local> iterator = lstLocais.iterator(); iterator
						.hasNext();) {
					Local local = iterator.next();
					optionList.add(new Option(local.getId(), local.getNome()));
				}
				return optionList;
			}

		});
		
		demandasDados.add(selectLocais);	
		
		demandasForm.add(demandasDados);
		
		montarBotoes();		
	}

	@Override
	public Form getForm() {
	
		return demandasForm;
	}

	@Override
	protected boolean onPesquisaClick() {
		return false;
	}

	@Override
	protected boolean onCancelarClick() {
		setRedirect(DemandasFormDadosPage.class);

		return true;
	}

	@Override
	protected boolean onSalvarClick() 
	{
		
		try {
		
			Demandas demandas = (Demandas) getContext().getSession().getAttribute("aberturaDemandas");

			//teste para validar o campo da pagina apos o adicionar da outra tela			
			FieldSet demandasDados = (FieldSet) demandasForm.getControl("DemandasDados");
			Field fieldTipoDemanda = demandasDados.getField("tipoMovimentacao");
			String qualTipoMovimentacao = fieldTipoDemanda.getValue();

			
			List<DemandasItemAtivo> demandasItemAtivoList = montarDemandasItemAtivo();

			
			//se for demanda de entrada ou de movimenta��o e a lista de demanda selecionada na lupa for maior que zero, grava, sen�o da mensagem de erro
			if ((qualTipoMovimentacao.equals("DEM_MOV_ATV") || qualTipoMovimentacao.equals("DEM_ENTRADA"))  && demandasItemAtivoList.size() > 0  )
			{
		
					demandas.setLstDemandasItensAtivos(demandasItemAtivoList);

					if (demandas.getId() == null || demandas.getId() <= 0) {
						
						// O Status Default da Demanda � Aberto 
						List<StatusDemanda> statusDemandaList = new ArrayList<StatusDemanda>(); 
						statusDemandaList.addAll(demandaGerencia.obterTodosStatusDemanda());
						
						for (StatusDemanda statusDemanda : statusDemandaList) {
							if (statusDemanda.getDescricao().equals(StatusDemanda.ABERTO))
								demandas.setStatusDemanda(statusDemanda);
							
						}
						demandaGerencia.incluir(demandas);
						
					}
					else
						demandaGerencia.alterar(demandas);
					
					
					
					getContext().getSession().setAttribute("aberturaDemandas", demandas);
					//salvar e obter id
					DemandasDetalhePage nextPage = getContext().createPage(DemandasDetalhePage.class);
					nextPage.demanda = 2; // id 
					setForward(nextPage);
					mensagemSucesso = "Demanda gravada com sucesso";
			
			} 
			else
			{
					mensagemErro = "Ocorreu o seguinte erro: N&atilde;o foi selecionada nenhuma demanda para movimenta&ccedil;&atilde;o ,ou entrada , ou saida";
					initForm();					
			}
			
			//Se for demanda de saida
			if ((qualTipoMovimentacao.equals("DEM_SAIDA")))
			{
		
					demandas.setLstDemandasItensAtivos(demandasItemAtivoList);

					if (demandas.getId() == null || demandas.getId() <= 0) {
						
						// O Status Default da Demanda e Aberto 
						List<StatusDemanda> statusDemandaList = new ArrayList<StatusDemanda>(); 
						statusDemandaList.addAll(demandaGerencia.obterTodosStatusDemanda());
						
						for (StatusDemanda statusDemanda : statusDemandaList) {
							if (statusDemanda.getDescricao().equals(StatusDemanda.ABERTO))
								demandas.setStatusDemanda(statusDemanda);
							
						}
						demandaGerencia.incluir(demandas);
						
					}
					else
						demandaGerencia.alterar(demandas);
					
					
					
					getContext().getSession().setAttribute("aberturaDemandas", demandas);
					//salvar e obter id
					DemandasDetalhePage nextPage = getContext().createPage(DemandasDetalhePage.class);
					nextPage.demanda = 2; // id 
					setForward(nextPage);
					mensagemSucesso = "Demanda gravada com sucesso";
			
			} 
			
			
			
		}
		catch (Exception e) {
			
			mensagemErro = "Ocorreu o seguinte erro: " + e.getMessage();
			initForm();
			e.printStackTrace();
			
		}
		
		return false;
		
	}

	@Override
	protected void montarGrid() {
		
	}
	
	private List<DemandasItemAtivo> montarDemandasItemAtivo() throws ValidacaoCampoException {
		
		List<DemandasItemAtivo> demandasItemAtivoList = new ArrayList<DemandasItemAtivo>();
		//demandasForm.getControl("itemAtivoEntrada");
		FieldSet demandasDados = (FieldSet) demandasForm.getControl("DemandasDados");
		Field fieldTipoDemanda = demandasDados.getField("tipoMovimentacao");
		String qualTipoMovimentacao = fieldTipoDemanda.getValue();
		
		DemandasItemAtivo dmdItem ;
		dmdItem  = new DemandasItemAtivo();
		
		if (qualTipoMovimentacao.equals(DEM_ENTRADA))
		{
			
			System.out.println("********************************************ENTRADA____________");
			//[ID_ATIVO;IDENTIFICADOR;ID_LOCAL_DESTINO]
			
			HiddenList  hiddenItems = (HiddenList) demandasForm.getControl("itemAtivoEntrada");
			List<String> valores = hiddenItems.getValues();
			
			String arrayIDs[]; 
			//DemandasItemAtivo dmdItem ;
			ItensAtivos itensAtivos;
			
			for (String str : valores) 
			{
				
				arrayIDs = str.trim().split(";");
				
				if (arrayIDs.length >= 2)
				{
					
					//dmdItem  = new DemandasItemAtivo();
					
					itensAtivos = new ItensAtivos();
					itensAtivos.setIdentificacao(arrayIDs[1]);
					
					Local local =new Local();
					local.setId(Integer.valueOf(arrayIDs[2]));
					//itensAtivos.setLocal(local);
					
					Ativos ativo =new Ativos();
					ativo.setId(Integer.valueOf(arrayIDs[0]));
					
					itensAtivos.setAtivo(ativo);
					dmdItem.setItemAtivos(itensAtivos);
					dmdItem.setLocalDestino(local);
					
					demandasItemAtivoList.add(dmdItem);
					
				}
				
			}
			
		}

		if (qualTipoMovimentacao.equals(DEM_MOV_ATV))
		{
			
			System.out.println("********************************************MOVIMENTO ________");
			
			//[ID_ITEM;ID_LOCAL_ORIGEM;ID_LOCAL_DESTINO]
			HiddenList  hiddenItems = (HiddenList) demandasForm.getControl("itemAtivosMovAtivo");
			List<String> valores = hiddenItems.getValues();
			String arrayIDs[]; 
			//DemandasItemAtivo dmdItem ;
			ItensAtivos itensAtivos;
			
			for (String str : valores) 
			{

				arrayIDs = str.split(";");
				
				if (arrayIDs.length == 4)
				{

					
					/*
					id - 0
					identificador - 1
					(id__local) local atual - 2
					destino - 3
					*/
					
					//dmdItem  = new DemandasItemAtivo();
					itensAtivos = new ItensAtivos();
					itensAtivos.setItensAtivosId((Integer.valueOf(arrayIDs[0])));
					
					//coloquei isto pois o que retorno na posi��o 1 � a identifica��o "RFID" da demanda. Estava ocorrendo
					//um erro pois estava passando string para esta posi��o 1 que estava como inteiro... quando na verdade � string.
					itensAtivos.setIdentificacao((String.valueOf(arrayIDs[1])));
					
					dmdItem.setItemAtivos(itensAtivos);
					Local localOrigem =new Local();
					localOrigem.setId(Integer.valueOf(arrayIDs[2]));
					dmdItem.setLocalOrigem(localOrigem);
					Local localDestino =new Local();
					localDestino.setId(Integer.valueOf(arrayIDs[3]));
					
					dmdItem.setLocalDestino(localDestino);
					
					demandasItemAtivoList.add(dmdItem);
				}
				else
				{
					mensagemErro="";
					mensagemErro = "Ocorreu o seguinte erro: N&atilde;o foi selecionada nenhum destino no Movimento Ativo";
					initForm();					
				}
				
			}
			
		}
		
		return demandasItemAtivoList;
		
	}
	
}

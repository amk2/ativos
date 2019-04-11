package com.projtec.slingcontrol.web.click.adm;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.click.ActionResult;
import org.apache.click.Control;
import org.apache.click.ControlRegistry;
import org.apache.click.Page;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.TextField;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.ImagensGerencia;
import com.projtec.slingcontrol.modelo.Imagens;
import com.projtec.slingcontrol.web.click.AtivosUtil;
import com.projtec.slingcontrol.web.click.ConfiguracoesUtil;
import com.projtec.slingcontrol.web.click.ControlesHTML;
import com.projtec.slingcontrol.web.click.InformacoesUtil;

@Component
public class PesquisarImagensPage extends Page {
//public class PesquisarImagensPage extends FormPage {

	private static final long serialVersionUID = -4934699296262101888L;

	@Bindable
	protected Form form = new Form("form");

	@Resource(name = "informacoesUtil")
	protected InformacoesUtil informacoesUtil;

	@Bindable
	private ActionLink pesquisarLink = new ActionLink("pesquisarLink",
			"Pesquisar");

	@Bindable
	private ActionLink limparLink = new ActionLink("limparLink", "Limpar");

	@Resource(name = "imagensGerencia")
	protected ImagensGerencia imagensGerencia;
	
	@Resource(name = "configuracoesUtil")
	protected ConfiguracoesUtil configuracoesUtil;

	@Resource(name = "ativosUtil")
	protected AtivosUtil ativosUtil;
	
	@Bindable
	protected String mensagemErro = new String();
	
	@Bindable
	protected String mensagemSucesso = new String();
	
	public PesquisarImagensPage() {
		 remoteAjax();
	}
	
	private List<Imagens> onPesquisaClick() {
		
		try {
		
			FieldSet pesquisarImagens = (FieldSet) form.getControl("pesquisarImagens");
			
			Field campoNome = pesquisarImagens.getField("nome");
			Field campoDescricao = pesquisarImagens.getField("descricao");
			
			List<Imagens> listaImagens = imagensGerencia.pesquisar(campoNome.getValue(), campoDescricao.getValue());
			
			return listaImagens;
			
		}
		catch (Exception e) {
			
			mensagemErro = e.getMessage();
			return new ArrayList<Imagens>();
			
		}
		
	}
	
	private void remoteAjax() {
		
		pesquisarLink.setId("pesquisarID");
		pesquisarLink.setImageSrc("/resources/images/btnPesquisar.png");
		pesquisarLink.addBehavior(new DefaultAjaxBehavior() {
			@Override
			public ActionResult onAction(Control source) {
				
				List<Imagens> imagensList = onPesquisaClick();
				ActionResult result = new ActionResult(imagensDinamicoJSON(imagensList), ActionResult.JSON);
				
				return result;
				
			};
		});
		
		ControlRegistry.registerAjaxTarget(form);
		
	}

	@Override
	public void onInit() {
		
		form.setValidate(false);
		
		FieldSet fieldSet = new FieldSet("pesquisarImagens");
		
		TextField nomeField = ControlesHTML.textField("nome");
		nomeField.setStyle("width", "400px;");
		nomeField.setRequired(false);
		
		fieldSet.add(nomeField);
		
		TextField descricaoField = ControlesHTML.textField("descricao");
		descricaoField.setStyle("width", "400px;");
		
		fieldSet.add(descricaoField);
		
		form.add(fieldSet);
		
		limparLink.setStyle("margin-right", "10px");
		
		addControl(form);
		ClickUtils.bind(form);
		
	}

	@SuppressWarnings("unused")
	private String imagensDinamicoJSON(List<Imagens> listaImagens) {
		
		StringBuilder colModel = new StringBuilder();
		StringBuilder colNames = new StringBuilder();
		StringBuilder dataSet = new StringBuilder();
		
		Map<Integer, String> mapLabel = new HashMap<Integer, String>();
		
		String strJSON = new String("{\n");
		
		if (CollectionUtils.isNotEmpty(listaImagens)) {

			Imagens imagem = listaImagens.get(0);
			
			colNames.append("\n\"colNames\":[");
			colNames.append("\"NOME\",\"DESCRICAO\",\"IMAGEM\"");
			colModel.append("\n\"colModel\":[");
			colModel.append("\n{\"editable\":false,\"index\":\"NOME\",\"jsonmap\":\"NOME\",\"key\":true,\"name\":\"NOME\",\"resizable\":true,\"search\":false,\"sortable\":false,\"width\":300},\n");
			colModel.append("\n{\"editable\":false,\"index\":\"DESCRICAO\",\"jsonmap\":\"DESCRICAO\",\"key\":false,\"name\":\"DESCRICAO\",\"resizable\":true,\"search\":false,\"sortable\":false,\"width\":350},\n");
			colModel.append("\n{\"editable\":false,\"index\":\"IMAGEM\",\"jsonmap\":\"IMAGEM\",\"key\":false,\"name\":\"IMAGEM\",\"resizable\":true,\"search\":false,\"sortable\":false,\"width\":77}\n");
			colModel.append("],\n");
			strJSON = strJSON + colModel.toString();
			colNames.append("],\n");
			strJSON = strJSON + colNames.toString();
			strJSON = strJSON + "\"gridModel\":{";
			dataSet.append("\"dataset\":[");
			
			String strValor;
			String fileName = null;
			
			for (Iterator<Imagens> iterator = listaImagens.iterator(); iterator.hasNext();) {
				
				Imagens img = (Imagens) iterator.next();
				byte[] bytes = imagensGerencia.pesquisarBytePorId(img.getId());
				String path = getContext().getServletContext().getRealPath("/resources/images/");
				
				try {
					
					BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
					int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
					BufferedImage image = resizeImage(bufferedImage, type);
	
					fileName = "tmp_" + img.getNome() + "__@" + img.getId();
					fileName = fileName.replaceAll("\"", "");
					fileName = fileName.replaceAll("'", "");
					
					ImageIO.write(image, "jpg", new File(path + "/" + fileName + ".jpg"));
					
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
				
				String nome = img.getNome();
				String descricao = img.getDescricao();
				
				if (nome.contains("\"")) {
					nome = nome.replaceAll("\"", "''");
				}
				
				if (descricao.contains("\"")) {
					descricao = descricao.replaceAll("\"", "''");
				}
				
				dataSet.append("\n{");
				dataSet.append("\"NOME\":\"" + nome
						+ "\" , \"DESCRICAO\":\"" + descricao
						+ "\" , \"IMAGEM\":\"" + fileName + ".jpg\"");
				dataSet.append("}\n");
				
				if (iterator.hasNext()) {
					dataSet.append(",");
				}
				
			}
			
			dataSet.append("]\n");
			
			strJSON = strJSON + dataSet.toString();
			strJSON = strJSON + "}";
			
		}
		
		strJSON = strJSON + "\n}";
		
		return strJSON;
		
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage,
			int type) {
		
		BufferedImage resizedImage = new BufferedImage(50, 50, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 50, 50, null);
		g.dispose();
		
		return resizedImage;
		
	}
	
}

package com.projtec.slingcontrol.web.click.adm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.click.ActionResult;
import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Select;
import org.apache.click.control.Table;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.extras.control.SubmitLink;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.ImagensGerencia;
import com.projtec.slingcontrol.infra.Secure;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.Imagens;
import com.projtec.slingcontrol.web.click.ControlesHTML;
import com.projtec.slingcontrol.web.click.FormPage;
import com.projtec.slingcontrol.web.tipocampo.ImageField;

@Component
public class ImagensPage extends FormPage {

	public static final String LISTA = "lista.imagens";

	private final Logger logger = LoggerFactory.getLogger(ImagensPage.class);

	private static final long serialVersionUID = 1L;
	@Bindable
	protected Form formImagens = new Form("formImagens");

	@Bindable
	protected List<Imagens> lstObjetos = new ArrayList<Imagens>();

	Select locaisField = new Select("locais");

	protected List<Imagens> lstImagens;

	private FileField fileField;

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

	@Resource(name = "imagensGerencia")
	protected ImagensGerencia imagensGerencia;

	public ImagensPage() {
		// super();
		// montarForm();
		// montarGrid();
	}
	
	@Override
	public boolean permitidoSalvar() {
	
		return Secure.isPermitted("botoes:imagens:salvar");
	}
	
	@Override
	public boolean permitidoPesquisar() {
		return Secure.isPermitted("botoes:imagens:pesquisar");
	}
	
	@Override
	public boolean permitidoCancelar() {
		return Secure.isPermitted("botoes:imagens:limpar");
	}


	public void onInit() {
		test = true;
		super.onInit();
		initImagens();
		listaSessao();
	}

	private void listaSessao() {
		List<Demandas> lstDemandas = (List<Demandas>) getContext().getSession()
				.getAttribute(LISTA);
		if (lstDemandas == null) {
			lstDemandas = new ArrayList<Demandas>();
			getContext().getSession().setAttribute(LISTA, lstDemandas);
		}
	}

	private void initImagens() {
		// data provider
		getTable().setDataProvider(new DataProvider<Imagens>() {
			public List<Imagens> getData() {
				return lstObjetos;
			}
		});
		montarBotoes();
		montarForm();
	}

	protected void montarGrid() {
		Column column;
		column = new Column("nome");
		column.setWidth("220px;");
		grid.addColumn(column);
		column = new Column("descricao");
		column.setWidth("220px;");
		grid.addColumn(column);

		column = new Column("imagem");
		column.setWidth("220px;");
		column.setDecorator(new Decorator() {
			public String render(Object row, Context context) {
				Imagens img = (Imagens) row;
				String url = "<img style=width:60px;height:60px; src=\"/slingcontrol/adm/imagens.htm?pageAction=onRenderImage&imageid="
						+ img.getId() + "\"/>";
				return (url);
			}
		});
		grid.addColumn(column);

		column = new Column("action");
		column.setWidth("120px;");
		column.setSortable(false);
		deleteLink.setAttribute("onclick",
				"return window.confirm('Deseja realmente remover?');");
		ActionLink[] links = new ActionLink[] { editLink, deleteLink };
		column.setDecorator(new LinkDecorator(grid, links, "id"));
		grid.addColumn(column);

		grid.setDataProvider(new DataProvider<Imagens>() {
			public List<Imagens> getData() {
				return obterListaSessao();
			}
		});
	}

	public ActionResult onRenderImage() {

		Context context = getContext();
		String imageId = context.getRequestParameter("imageid");
		int imageID = Integer.parseInt(imageId);

		byte[] bytes = imagensGerencia.pesquisarBytePorId(imageID);

		String contentType = ClickUtils.getMimeType("gif");
		return new ActionResult(bytes, contentType);
	}
	
	public ActionResult onRenderImagePeloNome() {

		String imagemNome = getContext().getRequestParameter("imagemNome");
		Imagens imagem = imagensGerencia.pesquisarPeloNome(imagemNome);

		byte[] bytes = imagensGerencia.pesquisarBytePorId(imagem.getId());

		String contentType = ClickUtils.getMimeType("gif");
		return new ActionResult(bytes, contentType);
	}

	private List<Imagens> obterListaSessao() {
		List<Imagens> lstImagens = (List<Imagens>) getContext().getSession()
				.getAttribute(LISTA);
		return lstImagens;
	}

	private void montarForm() {
		Field nomeField;
		Field descricaoField;

		FieldSet fieldSet = new FieldSet("imagem");
		nomeField = ControlesHTML.textField("nome");
		nomeField.setRequired(false);
		nomeField.setLabel(nomeField.getLabel() + " <span class='required'>*</span>");
		nomeField.setAttribute("class", "validate[required]");
		fieldSet.add(nomeField);
		
		descricaoField = ControlesHTML.textField("descricao");
		descricaoField.setRequired(false);
		descricaoField.setLabel(descricaoField.getLabel() + " <span class='required'>*</span>");
		descricaoField.setAttribute("class", "validate[required]");
		fieldSet.add(descricaoField);

		fileField = new FileField("fileField1", "Imagem", 40);
		fieldSet.add(fileField);
		
		//nao identifiquei para que servia este campo na tela, comentei ate descobrir a logica dele.
		//ImageField imageField = new ImageField("imagem");
		//imageField.setId("campoImagem");
		//imageField.setThumb("<img src=\"resources/images" + fileName + ".jpg \" />");
		//fieldSet.add(imageField);
		
		HiddenField idField = new HiddenField("id", Integer.class);
		fieldSet.add(idField);
		formImagens.add(fieldSet);
		ClickUtils.bind(formImagens);
	}

	public Form getForm() {
		return formImagens;
	}

	public Table getTable() {
		return grid;
	}

	// usado pelo editar
	public Boolean setFormObjeto() throws Exception {
		
		Integer id = editLink.getValueInteger();
		Imagens imagens = imagensGerencia.pesquisarId(id);

		if (imagens != null) {
			
			formImagens.getField("nome").setValue(imagens.getNome());
			formImagens.getField("descricao").setValue(imagens.getDescricao());
			formImagens.getField("imagem").setValueObject(imagens.getImagem());
			formImagens.getField("id").setValueObject(imagens.getId());
			ImageField campoImagem = (ImageField) formImagens.getField("campoImagem");
			
			byte[] bytes = imagensGerencia.pesquisarBytePorId(imagens.getId());
			
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
			int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
			BufferedImage image = resizeImage(bufferedImage, type);
			String fileName = "TmpThumb" + imagens.getId();
			String path = campoImagem.getPath();
			ImageIO.write(image, "jpg", new File(path + fileName + ".jpg"));
			
			campoImagem.setThumb("<img src='resources/images" + fileName + ".jpg' />");
			
		}
		
		return true;
		
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
		
		BufferedImage resizedImage = new BufferedImage(50, 50, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 50, 50, null);
		g.dispose();
		
		return resizedImage;
		
	}

	public Imagens montarObjeto() {

		Integer id = getIDFormObj();
		String nome = formImagens.getFieldValue("nome");
		String descricao = formImagens.getFieldValue("descricao");

		Imagens objImagens = new Imagens();
		objImagens.setId(id);
		objImagens.setNome(nome);
		objImagens.setDescricao(descricao);
		InputStream fl = null;

		try {
			fl = fileField.getFileItem().getInputStream();
			objImagens.setTam(fileField.getFileItem().getSize());
			objImagens.setExt(fileField.getFileItem().getContentType());
			objImagens.setNome_arquivo(fileField.getFileItem().getName());
			objImagens.setImagem(fl);

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("imagemObjeto: " + objImagens.toString() + "\n");

		return objImagens;
	}

	public boolean onOkClick() {
		if (formImagens.isValid()) {
			if (fileField.getFileItem() != null) {
				addModel("fileItem1", fileField.getFileItem());
			}
		}
		return true;
	}

	public Integer getIDFormObj() {
		String strId = formImagens.getFieldValue("id");
		Integer id;
		if (strId == null) {
			id = 0;
		} else if (strId.length() <= 0) {
			id = 0;
		} else {
			id = Integer.valueOf(strId);
		}
		return id;
	}

	public Integer gravar(Imagens obj) {
		// onOkClick();
		Imagens objNovo = imagensGerencia.incluir(obj);
		return objNovo.getId();
	}

	public List<Imagens> pesquisar(Imagens objImagens) {
		
		List<Imagens> imagensList = new ArrayList<Imagens>(imagensGerencia.pesquisar(objImagens));
		
		if (CollectionUtils.isEmpty(imagensList))
			mensagemErro = "N&atilde;o foram encontradas Imagens com os dados informados";
		
		return imagensList;
		
	}

	public Imagens pesquisar(Integer id) {
		return imagensGerencia.pesquisarId(id);
	}

	public Boolean removerObjeto(Integer id) {
		
		boolean retorno = imagensGerencia.excluir(id);
		mensagemSucesso = "Imagem removida com sucesso";
		
		return retorno;

	}

	public void removerLista(Integer id) {
		Imagens i;
		for (Iterator iterator = obterListaSessao().iterator(); iterator
				.hasNext();) {
			i = (Imagens) iterator.next();
			if (i.getId().equals(id)) {
				iterator.remove();
			}

		}
	}

	public void adicionarLista(Imagens d) {
		obterListaSessao().add(0, d);
	}

	public void novaLista(List<Imagens> lstNova) {
		// List<Imagens> lstAntiga= (List<Imagens>)
		// getContext().getSession().getAttribute(LISTA);
		// lstAntiga = null ;
		getContext().getSession().setAttribute(LISTA, lstNova);
	}

	public void alterar(Imagens obj) {
		imagensGerencia.alterar(obj);

	}

	// Event Handlers ---------------------------------------------------------
	/*
	public boolean onSalvarClick() {
		if (getForm().isValid()) {
			Imagens obj = montarObjeto();
			Integer id = getIDFormObj();

			if (id == 0) {
				Integer novoId = gravar(obj);
				Imagens novoObj = pesquisar(novoId);
				if (novoObj != null) {
					adicionarLista(novoObj);
				}
			} else {
				alterar(obj);
				removerLista(id);
				Imagens novoObj = pesquisar(id);
				adicionarLista(novoObj);
			}
			getForm().clearValues();
			mesg = "Salvo com sucesso ";
			return true;
		} else {
			return false;
		}
	}
	
	*/

	public boolean onDeleteClick() {
		Integer id = deleteLink.getValueInteger();
		boolean ok = removerObjeto(id);
		if (ok) {
			removerLista(id);
		}
		return ok;
	}

	public boolean onCancelarClick() {
		
		getForm().clearValues();
		getForm().clearErrors();
		mesg = "";
		
		return true;
		
	}

	public boolean onEditClick() {

		try {

			getForm().clearValues();
			getForm().clearErrors();
			return setFormObjeto();

		}
		catch(Exception e) {

			mensagemErro = e.getMessage();
			logger.error(e.getMessage(), e);

			return false;

		}

	}
	
	public boolean onPesquisaClick() {
		
		getForm().clearErrors();
		Imagens objeto = montarObjeto();
		final List<Imagens> lstImagens = new ArrayList<Imagens>(imagensGerencia.pesquisar(objeto));
		
		if (CollectionUtils.isEmpty(lstImagens)) {
			
			mensagemErro = "N&atilde;o foram encontradas Imagens com os dados informados";
			return false;
			
		}
		else {
			
			novaLista(lstImagens);
			return true;
			
		}
		
	}
	
	public boolean onSalvarClick() {

		try {

			FieldSet imagensFieldSet = (FieldSet) formImagens.getControl("imagem");
			validaCampos(imagensFieldSet);

			boolean retorno = salvar();
			mensagemSucesso = "Imagem salva com sucesso";

			return retorno;

		} catch (Exception e) {

			mensagemErro = e.getMessage();
			return false;

		}

	}
	
	private boolean salvar() {
		
		if (getForm().isValid()) {
			Imagens obj = montarObjeto();
			Integer id = getIDFormObj();

			if (id == 0) {
				Integer novoId = gravar(obj);
				Imagens novoObj = pesquisar(novoId);
				if (novoObj != null) {
					adicionarLista(novoObj);
				}
			} else {
				alterar(obj);
				removerLista(id);
				Imagens novoObj = pesquisar(id);
				adicionarLista(novoObj);
			}
			getForm().clearValues();
			mesg = "Salvo com sucesso ";
			return true;
		} else {
			return false;
		}
		
	}

	

}

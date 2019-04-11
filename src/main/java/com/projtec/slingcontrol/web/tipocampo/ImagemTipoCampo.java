package com.projtec.slingcontrol.web.tipocampo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.click.control.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.ImagensGerencia;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.Imagens;
import com.projtec.slingcontrol.modelo.Informacoes;

@Component("8")
public class ImagemTipoCampo implements TipoCampoView {
	
	@Autowired
	protected ImagensGerencia  imgGerencia;

	@Override
	public Field montarField(Configuracoes configuracoes, Informacoes info) {
		
		ImageField imageField = new ImageField(String.valueOf("campoImagem" + configuracoes.getId()));
		
		imageField.setLabel(configuracoes.getNomecampo());
		if (configuracoes.getObrigatoriedade().equals("S")) {
			imageField.setLabel(imageField.getLabel() + " <span class='required'>*</span>");
			imageField.setAttribute("class", "validate[required]");
		}
		imageField.setStyle("width", "10cm");
		
		if (info != null && info.getReferenciaId() > 0) {
			
			Imagens img = imgGerencia.pesquisarId(info.getReferenciaId());
			imageField.setValue(img.getNome());
			
			byte[] bytes = imgGerencia.pesquisarBytePorId(img.getId());
			
			try {
				
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
				int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
				BufferedImage image = resizeImage(bufferedImage, type);
				String fileName = "TmpThumb" + img.getId();
				String path = imageField.getPath();
				ImageIO.write(image, "jpg", new File(path + fileName + ".jpg"));
				
				imageField.setThumb("<img src=\"resources/images" + fileName + ".jpg \" />");
				
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return imageField;
		
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
		BufferedImage resizedImage = new BufferedImage(50, 50, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 50, 50, null);
		g.dispose();
		return resizedImage;
	}

	@Override
	public String formataInformacao(Configuracoes configuracoes, Informacoes info) {
		
		String strValor = ""; 
		
		if (info.getReferenciaId() != null && info.getReferenciaId() > 0) {
		
			Imagens imagem = imgGerencia.pesquisarId(info.getReferenciaId());
			
			strValor = "<img style=width:60px;height:60px; src='adm/imagens.htm?pageAction=onRenderImagePeloNome&imagemNome=" + imagem.getNome() + 
				"' title='" + imagem.getDescricao() + "' />";
			
		}
		
		return strValor;
		
	}

}

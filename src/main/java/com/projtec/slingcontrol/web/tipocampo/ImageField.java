package com.projtec.slingcontrol.web.tipocampo;

import java.io.File;

import org.apache.click.control.Field;
import org.apache.click.util.HtmlStringBuffer;

public class ImageField extends Field {

	private static final long serialVersionUID = 1682451636689639870L;
	
	private String path = getContext().getServletContext().getRealPath("/resources/images/");
	
	protected String id;
	protected String thumb = "";
	
	public ImageField() {
	}

	public ImageField(String name) {
		setName(name);
	}
	
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	@Override
	public void render(HtmlStringBuffer buffer) {

		buffer.elementStart("input");
        buffer.appendAttribute("type", "text");
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("id", getName());
        buffer.appendAttributeEscaped("value", getValue());
        buffer.appendAttribute("title", getTitle());
        
        if (getTabIndex() > 0) {
            buffer.appendAttribute("tabindex", getTabIndex());
        }
        appendAttributes(buffer);
        if (isDisabled()) {
            buffer.appendAttributeDisabled();
        }
        buffer.appendAttributeReadonly();
        buffer.elementEnd();
        
        buffer.elementStart("input");
        buffer.appendAttribute("type", "hidden");

        buffer.appendAttribute("name", "imgField" + getName());
        buffer.appendAttribute("id", "imgField" + getName());
        
        buffer.appendAttributeEscaped("value", getValue());
        appendAttributes(buffer);
        buffer.elementEnd();
        
        // buffer.append("<a class=\"imgID\" href=\"/slingcontrol/adm/pesquisarImagens.htm?id=" + getName() + "\" style=\"margin-left:5px;\"> " +
        
        String urlBotaoPesquisar = getContext().getRequest().getContextPath() + "/adm/pesquisarImagens.htm?id=" + getName();
        
        buffer.append("<a class='imgID' href='" + urlBotaoPesquisar + "' style='margin-left:5px;'> " +
        		"<img src=\"resources/images/btnPesquisar.png\" /> </a>"
				+ "<div id='imgThumb" + getName() + "'> " + thumb + "</div>");
        
		deleteTmp();
	}

	public void deleteTmp() {
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i ++){
			if(files[i].getAbsoluteFile().toString().contains("tmp_")) {
				files[i].delete();
			}
		}
	}
	
	@Override
	public void setValue(String value) {
		super.setValue(value);
		id = value;
	}
	
	public String getPath(){
		return path;
	}

}

package com.projtec.slingcontrol.web.click;

import org.apache.click.Page;
import org.apache.click.util.Bindable;
import org.springframework.stereotype.Component;

@Component
public class LocalizacaoPage extends Page
{
	@Bindable
	public String title = getMessage("titulo");
	
	 @Bindable 
	 public String mensagem = "localiza&ccedil;&atilde;o";
}

package com.projtec.slingcontrol.web.click;

import org.apache.click.control.TextField;

public class ControlesHTML
{
	
	public static TextField textField(String nomeCampo)
	{
		TextField nomeField = new TextField(nomeCampo); 
		
		nomeField.setTabIndex(1);
		nomeField.setRequired(true);
		nomeField.setSize(100);
		nomeField.setMaxLength(100);
		/*nomeField.setMinLength(3);*/
		nomeField.setTrim(true);
		//nomeField.setStyle("width", "100%");
		
		
		return nomeField;
		
	}

}

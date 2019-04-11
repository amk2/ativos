package com.projtec.slingcontrol.infra;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class QueryUtilXML 
{
	
	public static Map<String, String> obterMapSQL(Class recurso) throws IOException
	{
		XStream xstream = new XStream(new DomDriver());
		String filename = recurso.getSimpleName()+ ".xml";		
		URL res = recurso.getResource(filename);		
		InputStream fin = res.openStream();
		Map<String, String> map =(Map<String, String>)xstream.fromXML(fin);
		
		return map;
		
	}

}

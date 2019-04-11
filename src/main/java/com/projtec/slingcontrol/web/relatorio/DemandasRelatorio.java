package com.projtec.slingcontrol.web.relatorio;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/demanda")
public class DemandasRelatorio 
{
	
	@RequestMapping("/teste")
	public ModelAndView montarRelatorioApropriacao(HttpServletRequest request, 
			                    HttpServletResponse response    ) {
	
		String uri = request.getRequestURI();
		String format = uri.substring(uri.lastIndexOf(".") + 1);
		
		
		
		Map<String, String> model =new HashMap<String, String>();
				
		String view = "TESTE" ;
		
		
		model.put("format", format);
	    
		return new ModelAndView(view, model);	
	}

}

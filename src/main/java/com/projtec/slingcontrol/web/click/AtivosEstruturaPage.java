package com.projtec.slingcontrol.web.click;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.click.ActionResult;
import org.apache.click.Page;
import org.apache.click.util.Bindable;
import org.springframework.stereotype.Component;

import com.projtec.slingcontrol.gerencia.AtivosGerencia;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.AtivosNo;

@Component
public class AtivosEstruturaPage extends Page implements Serializable {

	private static final long serialVersionUID = 7246632039167305819L;

	List<AtivosNo> ativosNos = new ArrayList<AtivosNo>();
	StringBuffer sb = new StringBuffer();
	String estrutura;

	@Resource(name = "ativosGerencia")
	protected AtivosGerencia ativosGerencia;

	@Bindable
	protected Integer idAtivo;

	public AtivosEstruturaPage() {
	}

	@Override
	public void onRender() {
		System.out.println("onRender" + idAtivo);
		getContext().getSession().setAttribute("idAtivo", idAtivo);
	}

	public ActionResult carregarEstruturaAtivo() {
		idAtivo = (Integer) getContext().getSession().getAttribute("idAtivo");
		if (idAtivo != null) {
			ativosNos = ativosGerencia.getFilhos(idAtivo, ativosNos);
			sb.append("<root>");
			if (ativosNos.size() > 0) {
				AtivosNo atvNoPai = ativosNos.get(0);
				if (atvNoPai.getPai() != null) {
					Ativos atvPai = ativosGerencia.pesquisarId(atvNoPai
							.getPai());
					sb.append("<item id='" + atvPai.getId() + "'>");
					sb.append("<content><name>" + atvPai.getLayout().getNome()
							+ "</name></content>");
					sb.append("</item>");
				}
			}
			for (Iterator<AtivosNo> iterator = ativosNos.iterator(); iterator
					.hasNext();) {
				AtivosNo ativosNo = (AtivosNo) iterator.next();
				sb.append("<item id='" + ativosNo.getFilho() + "'");
				if (ativosNo.getPai() != null) {
					sb.append(" parent_id='" + ativosNo.getPai() + "'");
				}
				Ativos atv = ativosGerencia.pesquisarId(ativosNo.getFilho());
				sb.append("><content><name>" + atv.getLayout().getNome()
						+ "</name></content>");
				sb.append("</item>");
			}
			sb.append("</root>");
			estrutura = sb.toString();
		}
		/*
		 * String estrutura = "" + "<root>" + "<item id='node_1'>" + "<content>
		 * <name>Root node servidor1</name> </content>" + "</item>" +
		 * "<item id='node_2'>" + "<content> <name>Root node 2 server </name>
		 * </content>" + "</item>" + "<item>" + "<content> <name>Root node
		 * 3</name> </content>" + "</item>" +
		 * "<item id='node_21' parent_id='node_2'>" +
		 * "<content><name>Child node dddd</name></content>" + "</item>" +
		 * "<item id='node_22' parent_id='node_21'>" +
		 * "<content><name>Child node dddd</name></content>" + "</item>" +
		 * "<item parent_id='node_1'>" +
		 * "<content><name>Child node</name></content>" + "</item>" + "</root>";
		 */

		// Return an action result containing the message
		return new ActionResult(estrutura, ActionResult.XML);
	}
}

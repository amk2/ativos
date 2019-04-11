package com.projtec.slingcontrol.persistencia.postgres;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.projtec.slingcontrol.infra.QueryUtilXML;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.ConfigInfoPesquisaVO;
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.persistencia.ItensAtivosDAO;



public class ItensAtivosPostgresDAO  extends BasePostgresDAO implements ItensAtivosDAO
{
	
	private final Logger logger = LoggerFactory.getLogger(ItensAtivosPostgresDAO.class);

	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource DataSourceUtils) throws IOException
	{
		dataSource = DataSourceUtils;
		querys = QueryUtilXML.obterMapSQL(ItensAtivosPostgresDAO.class);
	}	


	@Override
	public ItensAtivos incluir(ItensAtivos objItensAtivos) 
	{
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			/*
			 *  itens_ativo_identificacao, itens_ativo_altura, 
            itens_ativo_profundidade, ativos_id, locais_id,itens_ativo_id
			 */

			pst.setString(1, objItensAtivos.getIdentificacao());			
			pst.setNull(2, java.sql.Types.INTEGER);
			pst.setNull(3, java.sql.Types.INTEGER);
			pst.setInt(4,  objItensAtivos.getAtivo().getId());
			
			if (objItensAtivos.getLocal()!=null  && objItensAtivos.getLocal().getId()!=0 )
			{
				pst.setInt(5,  objItensAtivos.getLocal().getId());
				
			}else
			{
				pst.setNull(5, java.sql.Types.INTEGER);
			}
		
			
			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt(1);
				objItensAtivos.setItensAtivosId(id);

			} else
			{
				throw new IllegalStateException(
						"Erro ao inserir Itens Ativo :Nao tem chave?");
			}

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objItensAtivos;
		}
	

	@Override
	public boolean excluir(int id) {
		
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluir");
			logger.info("excluir:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro remover Itens Ativo :Muitos registros");

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		


		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return false;
	}

	@Override
	public ItensAtivos obterID(Integer id) 
	{
		Connection con = null;
		ItensAtivos itemAtivo = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterID");
			logger.info("obterID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				itemAtivo = mapper(rs);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return itemAtivo;
	}


	@Override
	public boolean alterar(ItensAtivos objItensAtivos) 
	{
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, objItensAtivos.getIdentificacao());			
			pst.setNull(2, java.sql.Types.INTEGER);
			pst.setNull(3, java.sql.Types.INTEGER);
			pst.setInt(4,  objItensAtivos.getAtivo().getId());
			pst.setInt(5,  objItensAtivos.getLocal().getId());
			pst.setInt(6,  objItensAtivos.getItensAtivosId());

			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro alterar Itens Ativos :Muitos registros");

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}
	
	
	private ItensAtivos mapper(ResultSet rs) throws SQLException 
	{	       
		ItensAtivos itens = new ItensAtivos();
		itens.setItensAtivosId(rs.getInt("itens_ativo_id"));
		Ativos ativo = new Ativos();
		ativo.setId(rs.getInt("ativos_id"));
		itens.setAtivo(ativo);
		itens.setIdentificacao(rs.getString("itens_ativo_identificacao"));
		Local local = new Local();
		local.setId(rs.getInt("locais_id"));
		itens.setLocal(local);
		return itens;
	}


	@Override
	public List<ItensAtivos> obterItensdoAtivo(Integer idAtivo) 
	{

		ItensAtivos itensAtivo = null;
		List<ItensAtivos> lstItens = new java.util.ArrayList<ItensAtivos>();
		Connection con = null;
		
		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterItensAtivo");
			logger.info("obterItensAtivo:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idAtivo);
			
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				itensAtivo = mapper(rs);

				lstItens.add(itensAtivo);
			}
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstItens;
		
	}
	

	
	@Override
	public List<ItensAtivos> obterItensdoAtivoComLocalID(Integer idAtivo) {

		ItensAtivos itensAtivo = null;
		List<ItensAtivos> lstItens = new java.util.ArrayList<ItensAtivos>();
		Connection con = null;
		
		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterItensAtivosSemLocalID");
			logger.info("obterItensAtivosSemLocalID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idAtivo);
			
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				itensAtivo = mapper(rs);

				lstItens.add(itensAtivo);
			}
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstItens;

	
	}
	
	
	@Override
	public List<ItensAtivos> pesquisarItensAtivos(Integer idLayout, String identificacao, List<ConfigInfoPesquisaVO> lstConfigisValor) 
	{
		ItensAtivos objItemAtivo = null;
		List<ItensAtivos> lstItemAtivo = new java.util.ArrayList<ItensAtivos>();
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = " "
				 + "\n select i.* from itens_ativo i  " 
				 + "             inner join ativos atv  on (atv.ativos_id= i.ativos_id ) "
				 + "WHERE 1=1" 
				 
				 + " and atv.layout_id="  + idLayout ;
				 
				 if(identificacao!=null && identificacao.length() > 0 )
				 {
					 sql = sql +  " and  lower(i.itens_ativo_identificacao)  like '%" +  identificacao.toLowerCase()  + "%'";
					 
				 }
				
			
			  if ( lstConfigisValor !=null && lstConfigisValor.size() > 0)
			  {
				
				  
			
				
			     for (Iterator<ConfigInfoPesquisaVO> iterator = lstConfigisValor.iterator(); iterator.hasNext();) 
			     {
			   	     sql = sql   +   "\n and  atv.ativos_id in (select info1.ativos_id  from informacoes_ativo info1 " 
	                             +   " where 1 = 1 " ; 
			   	  
			    	 ConfigInfoPesquisaVO configInfoPesquisaVO = iterator.next();
			    	 Configuracoes config = configInfoPesquisaVO.getConfig();
			    	 if (config.getTipoCampo().equals(TipoCampo.DATA) ) 
			    	 {
			    		
			    		if (configInfoPesquisaVO.getInfoDe()!=null)
			    		{
			    			 sql = sql  
				    		    +  "\n and  " 
				    		    +  " to_date(info1.informacoes_descricao, 'DD/MM/YYYY')   >= to_date('" 
				    		    +  configInfoPesquisaVO.getInfoDe().getDescricao() + "' ,'DD/MM/YYYY') " 
		       		            +  "  and info1.layout_configuracoes_id = "  
		       		            + configInfoPesquisaVO.getConfig().getId(); 
			    			
			    		}	    	
			    		 
			    		 if (configInfoPesquisaVO.getInfoAte()!=null)
			    		 {
			    			 sql = sql  
			    			   +  "\n and  " 
				    		    +  " to_date(info1.informacoes_descricao, 'DD/MM/YYYY')   <= to_date('" 
				    		    +  configInfoPesquisaVO.getInfoAte().getDescricao() + "' ,'DD/MM/YYYY') " 
		       		            +  "  and info1.layout_configuracoes_id = "  
		       		            + configInfoPesquisaVO.getConfig().getId(); 
			    		 }
			    		 
			    		 
			    	 
			    	 }else if ( config.getTipoCampo().equals(TipoCampo.NUMERO_INT) ) 
			    	 {
			    		if (configInfoPesquisaVO.getInfoDe()!=null)
			    		{
			    			sql = sql  
			    		    +  "\n and  " 
			    		    +  " to_number( info1.informacoes_descricao, '9999999999999')  >= " 
			    		    +    configInfoPesquisaVO.getInfoDe().getDescricao()
	       		            +  " and info1.layout_configuracoes_id = "  
	       		            + configInfoPesquisaVO.getConfig().getId(); 
			    			
			    		}		    		
			    		 
			    		 if (configInfoPesquisaVO.getInfoAte()!=null)
			    		 {
			    			 sql = sql  
				    		    +  "\n and  " 
				    		    +  " to_number( info1.informacoes_descricao, '9999999999999')  <= " 
				    		    +    configInfoPesquisaVO.getInfoAte().getDescricao()
		       		            +  " and info1.layout_configuracoes_id = "  
		       		            + configInfoPesquisaVO.getConfig().getId(); 
			    		 }
			    		 
			    	 }else if ( config.getTipoCampo().equals(TipoCampo.NUMERO_DEC) )
			    	 {
			    		 //select to_number('1933,45' , '999999999D9999')
			    		if (configInfoPesquisaVO.getInfoDe()!=null)
			    		{
				    		 sql = sql  
				    		    +  "\n and  " 
				    		    +  " to_number(info1.informacoes_descricao, '999999999D9999')  >= " 
				    		    +    StringUtils.replace(configInfoPesquisaVO.getInfoDe().getDescricao() , "," , "." )
		       		            +  " and info1.layout_configuracoes_id = "  
		       		            + configInfoPesquisaVO.getConfig().getId(); 
			    		}
			    	
			    		 
			    		 if (configInfoPesquisaVO.getInfoAte()!=null)
			    		 {
			    			 sql = sql  
				    		    +  "\n and  " 
				    		    +  " to_number(info1.informacoes_descricao, '999999999D9999')  <= " 
				    		    +   StringUtils.replace(configInfoPesquisaVO.getInfoAte().getDescricao() , "," , "." )
		       		            +  " and info1.layout_configuracoes_id = "  
		       		            + configInfoPesquisaVO.getConfig().getId(); 
			    		 }
			    		 
			    		 
			    	 }else  
			    	 {
                        
			    		 if (StringUtils.isNotEmpty(configInfoPesquisaVO.getInfoDe().getDescricao()))
			    		 {
			    			 sql = sql  
				    	 	    +  "\n and  " 
				    		    +  " lower(info1.informacoes_descricao) like '%" 
				    		    +    configInfoPesquisaVO.getInfoDe().getDescricao().toLowerCase() + "%'"
	   	       		            +  " and info1.layout_configuracoes_id = "  
	   	       		            + configInfoPesquisaVO.getConfig().getId(); 
			    		 }else
			    		 {
			    			 sql = sql  
				    		    +  "\n and  " 
				    		    +  " info1.informacoes_id_referencia = " + 
				    		    +    configInfoPesquisaVO.getInfoDe().getReferenciaId() 
	   	       		            +  " and info1.layout_configuracoes_id = "  
	   	       		            + configInfoPesquisaVO.getConfig().getId(); 
			    		 }
			    		 
			    		
			    		 
			    	 }
			    	 sql = sql  + ")";   	   	       					
			     }
			     
			    
				  
			 }
			logger.info("pesquisar itens:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				
				objItemAtivo = mapper(rs);
			

				lstItemAtivo.add(objItemAtivo);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstItemAtivo;
	}

	
	
	

	@Override
	public ItensAtivos obterIDparaItemAtivo(Integer id) {


		Connection con = null;
		ItensAtivos itemAtivo = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterIDsParaItemAtivo");
			logger.info("obterIDsParaItemAtivo:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				itemAtivo = mapeamento(rs);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return itemAtivo;
	
	
	
	}

	

	private ItensAtivos mapeamento(ResultSet rs) throws SQLException 
	{	       

		ItensAtivos itens = new ItensAtivos();
		itens.setItens_ativo_localizacao_x(rs.getString("itens_ativo_localizacao_x"));
		itens.setItens_ativo_localizacao_y(rs.getString("itens_ativo_localizacao_y"));
		itens.setItens_ativo_latitude(rs.getString("itens_ativo_latitude"));
		itens.setItens_ativo_longitude(rs.getString("itens_ativo_longitude"));
		itens.setIdentificacao(rs.getString("itens_ativo_identificacao"));
		itens.getAtivo().setId(rs.getInt("ativos_id"));
		itens.getLocal().setId(rs.getInt("locais_id"));
		
		return itens;
	}


	@Override
	public ItensAtivos incluirItenAtivoFinalizado(ItensAtivos itenAtivoRecebeCamposdaDemanda) 
	{

		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("inserirItemAtivo");
			logger.info("inserirItemAtivo:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);


			pst.setString(1, itenAtivoRecebeCamposdaDemanda.getItens_ativo_localizacao_x());
			pst.setString(2, itenAtivoRecebeCamposdaDemanda.getItens_ativo_localizacao_y());
			pst.setString(3, itenAtivoRecebeCamposdaDemanda.getItens_ativo_latitude());
			pst.setString(4, itenAtivoRecebeCamposdaDemanda.getItens_ativo_longitude());
			pst.setString(5, itenAtivoRecebeCamposdaDemanda.getIdentificacao());
			pst.setInt(6, itenAtivoRecebeCamposdaDemanda.getAtivo().getId());
			pst.setInt(7, itenAtivoRecebeCamposdaDemanda.getLocal().getId());
			pst.execute();

			//pegar a nova ID 
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt(1);
				itenAtivoRecebeCamposdaDemanda.setItensAtivosId(id);

			} else
			{
				throw new IllegalStateException("Erro ao inserir Item Ativo :Nao tem chave?");
			}

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
			
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		
		return itenAtivoRecebeCamposdaDemanda;
	}


	
	
	

}

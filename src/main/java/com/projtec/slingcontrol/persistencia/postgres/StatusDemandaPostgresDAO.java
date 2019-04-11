package com.projtec.slingcontrol.persistencia.postgres;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.projtec.slingcontrol.infra.QueryUtilXML;
import com.projtec.slingcontrol.modelo.StatusDemanda;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.persistencia.StatusDemandaDAO;

public class StatusDemandaPostgresDAO implements StatusDemandaDAO
{
	private final Logger logger = LoggerFactory
	.getLogger(InformacoesDemandaPostgresDAO.class);
	private DataSource dataSource;
	
	private Map<String, String> querys;
	
	@Autowired
	public void init(DataSource DataSourceUtils) throws IOException
	{
		dataSource = DataSourceUtils;
		querys = QueryUtilXML.obterMapSQL(StatusDemandaPostgresDAO.class);

	}
	

	@Override
	public Collection<StatusDemanda> obterTodos() {
	
		StatusDemanda objStatusDemanda = null;
		Collection<StatusDemanda> lstStatusDemanda = new java.util.ArrayList<StatusDemanda>();
		Connection con=null;
		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTodos");
			logger.info("obterTodos:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
	
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objStatusDemanda = mapper(rs);
				
				lstStatusDemanda.add(objStatusDemanda);
			}
		}
		catch(SQLException e)
		{
			
			throw new IllegalStateException(e.getMessage());		


		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstStatusDemanda;
	}
	
	
	@Override
	public StatusDemanda pesquisarPorId(Integer id) 
	{
		
		Connection con=null;
		StatusDemanda statusDemanda = null;
		
		try 
		{
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterStatusPorID");
			logger.info("obterStatusPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
			
			if (rs.next())
			{
				
				statusDemanda = mapper(rs);
				
				 
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
			
			
			return statusDemanda;
	}
	

	
	
	private StatusDemanda mapper(ResultSet rs) throws SQLException 
	{
		StatusDemanda objStatusDemanda;
		objStatusDemanda = new StatusDemanda();
		objStatusDemanda.setId(rs.getInt("status_demanda_id"));		
		objStatusDemanda.setDescricao(rs.getString("status_demanda_descricao"));
		return objStatusDemanda;
	}

}

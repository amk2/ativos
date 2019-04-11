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
import com.projtec.slingcontrol.modelo.TipoLayout;
import com.projtec.slingcontrol.persistencia.TipoLayoutDAO;


public class TipoLayoutPostgresDAO implements TipoLayoutDAO {
	
	
	 private final  Logger logger  = LoggerFactory.getLogger(TipoLayoutPostgresDAO.class);  
	 private DataSource dataSource;
	 private Map<String, String> querys; 
	
	
	@Autowired
	public void init(DataSource dtSource) throws IOException 
	{
		dataSource = dtSource;		
		querys = QueryUtilXML.obterMapSQL(TipoLayoutPostgresDAO.class);
		
	}
	
	@Override
	public TipoLayout incluir(TipoLayout objTipoLayout) 
	{
	
		Connection con = null; 
		
		try 
		{
			
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setString(1,objTipoLayout.getDescricao());
			
			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql) ; 
			ResultSet rs = pst.executeQuery() ; 
			if (rs.next())
			{
				 int id = rs.getInt(1); 
				 objTipoLayout.setId(id);
				 
			}else 
			{
				throw new IllegalStateException("Erro ao inserir Tipo layout :Nao tem chave?");	
			}		
			
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		

		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return objTipoLayout ;
	}
	
	
	
	@Override
	public boolean excluir (int id)
	{
		Connection con=null;
		try 
		{
		    con=DataSourceUtils.getConnection(dataSource);
		    String sql = querys.get("excluir");
		    logger.info("excluir:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);			
			pst.setInt(1, id);	
			int qtd = pst.executeUpdate();
			
			if (qtd > 1)
				throw  new IllegalStateException("Erro remover Tipo layout:Muitos registros");	 
				
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
					
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return true;
	}




	@Override
	public boolean alterar(TipoLayout objTipoLayout) 
	{
		Connection con=null;
		
		try 
		{	
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setString(1, objTipoLayout.getDescricao());
			pst.setInt(2, objTipoLayout.getId());
			
			int qtd = pst.executeUpdate();
			
			if (qtd > 1)
				throw  new IllegalStateException("Erro alterar Tipo  layout:Muitos registros");	 
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return true;
	}




	@Override
	public Collection<TipoLayout>pesquisar(TipoLayout tipolayout) 
	{
		TipoLayout objTipoLayout = null;
		Collection<TipoLayout> lstLayout = new java.util.ArrayList<TipoLayout>();
		Connection con=null;
		
		try 
		{
				
		    con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("pesquisar");
			logger.info("pesquisar:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
		
		   
		    if ( tipolayout!=null &&  tipolayout.getDescricao()!=null )
		    {
		    	pst.setString(1, "%" + tipolayout.getDescricao().toLowerCase() + "%"  );
		    	
		    }else
		    {
		    	pst.setString(1, "");
		    }
		    
		  		
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objTipoLayout = new TipoLayout ();
				objTipoLayout.setId(rs.getInt("tipo_layout_id"));
				objTipoLayout.setDescricao(rs.getString("tipo_layout_descricao"));
				
				
				
				lstLayout.add(objTipoLayout);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstLayout;
	}




	@Override
	public TipoLayout pesquisarPorId(Integer id) 
	{
		TipoLayout  objTipoLayout= null;
		Connection con=null;
		
		try 
		{
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterLayoutPorID");
			logger.info("obterLayoutPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
			
			if (rs.next())
			{
			   objTipoLayout = mapperTipoLayout(rs);				 
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
			
			
			return objTipoLayout;
	}
	
	@Override
	public Collection<TipoLayout> obterTodos() 
	{
		TipoLayout objTipoLayout= null;
		Collection<TipoLayout> lstTipoLayout = new java.util.ArrayList<TipoLayout>();
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
				objTipoLayout = mapperTipoLayout(rs);
				
				lstTipoLayout.add(objTipoLayout);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstTipoLayout;
	}
	
	private TipoLayout mapperTipoLayout(ResultSet rs) throws SQLException {
		TipoLayout objTipoLayout;
		objTipoLayout = new TipoLayout();
		objTipoLayout.setId(rs.getInt("tipo_layout_id"));
		
		objTipoLayout.setDescricao(rs.getString("tipo_layout_descricao"));
		return objTipoLayout;
	}



}

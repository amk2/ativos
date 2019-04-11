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
import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.persistencia.TipoCampoDAO;


public class TipoCampoPostgresDAO implements TipoCampoDAO {
	
	
	 private final  Logger logger  = LoggerFactory.getLogger(TipoCampoPostgresDAO.class);  
	 
	private DataSource dataSource;
	private Map<String, String> querys; 
	
	
	@Autowired
	public void init(DataSource dtSource) throws IOException 
	{
		dataSource = dtSource;		
		querys = QueryUtilXML.obterMapSQL(TipoCampoPostgresDAO.class);
		
	}
	
	public TipoCampo incluir(TipoCampo  objTipoCampo) 
	{
	
		Connection con = null; 
		
		try 
		{
			
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setString(1,objTipoCampo.getDescricao());
			
			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql) ; 
			ResultSet rs = pst.executeQuery() ; 
			if (rs.next())
			{
				 int id = rs.getInt(1); 
				 objTipoCampo.setId(id);
				 
			}else 
			{
				throw new IllegalStateException("Erro ao inserir Tipo Campo :Nao tem chave?");	
			}		
			
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		

		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return objTipoCampo ;
	}
	
	
	
	@Override
	public boolean excluir (int id)
	{
		Connection con = null; 
		try 
		{
		    con=DataSourceUtils.getConnection(dataSource);
		    String sql = querys.get("excluir");
		    logger.info("excluir:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);			
			pst.setInt(1, id);	
			int qtd = pst.executeUpdate();
			
			if (qtd > 1)
				throw  new IllegalStateException("Erro remover Tipo Campo:Muitos registros");	 
				
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
					
		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return true;
	}




	@Override
	public boolean alterar(TipoCampo objTipoCampo) 
	{
		Connection con = null; 
		try 
		{	
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setString(1, objTipoCampo.getDescricao());
			pst.setInt(2,objTipoCampo.getId());
			
			int qtd = pst.executeUpdate();
			
			if (qtd > 1)
				throw  new IllegalStateException("Erro alterar Tipo Campo:Muitos registros");	 
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		

		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return true;
	}




	@Override
	public Collection<TipoCampo>pesquisar(TipoCampo tipocampo) 
	{
		TipoCampo objTipoCampo = null;
		Collection<TipoCampo> lstTipoCampo= new java.util.ArrayList<TipoCampo>();
		Connection con = null; 
		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("pesquisar");
			logger.info("pesquisar:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
		
		   
		    if ( tipocampo!=null &&  tipocampo.getDescricao()!=null )
		    {
		    	pst.setString(1, "%" + tipocampo.getDescricao().toLowerCase() + "%"  );
		    	
		    }else
		    {
		    	pst.setString(1, "");
		    }
		    
		  		
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objTipoCampo = mapperTipoCampo(rs);	
				
				lstTipoCampo.add(objTipoCampo);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		


		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstTipoCampo;
	}




	@Override
	public TipoCampo pesquisarPorId(Integer id) 
	{
		Connection con = null; 
		TipoCampo objTipoCampo = null;
		
		try 
		{
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTipoCampoPorID");
			logger.info("obterTipoCampoPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
			
			if (rs.next())
			{
				objTipoCampo = mapperTipoCampo(rs);
				
				
				 
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		


		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
			
			
			return objTipoCampo;
	}
	@Override
	public Collection<TipoCampo> obterTodos() 
	{
		TipoCampo objTipoCampo= null;
		Collection<TipoCampo> lstTipoCampo = new java.util.ArrayList<TipoCampo>();
		Connection con = null; 
		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTodos");
			logger.info("obterTodos:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
	
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objTipoCampo = mapperTipoCampo(rs);
				
				lstTipoCampo.add(objTipoCampo);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		


		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstTipoCampo;
	}
	
	private TipoCampo mapperTipoCampo(ResultSet rs) throws SQLException {
		TipoCampo objTipoCampo;
		objTipoCampo = new TipoCampo();
		objTipoCampo.setId(rs.getInt("tipos_campo_id"));
		
		objTipoCampo.setDescricao(rs.getString("tipos_campo_descricao"));
		return objTipoCampo;
	}


}


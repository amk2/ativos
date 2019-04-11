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
import com.projtec.slingcontrol.modelo.TipoMovimentoAtivo;
import com.projtec.slingcontrol.persistencia.TipoMovimentoAtivoDAO;

public class TipoMovimentoAtivoPostgresDAO implements TipoMovimentoAtivoDAO {
	
	 private final  Logger logger  = LoggerFactory.getLogger(TipoMovimentoAtivoPostgresDAO.class);  
	 private DataSource dataSource;
	 private Map<String, String> querys; 
	
	
	@Autowired
	public void init(DataSource dtSource) throws IOException 
	{
		dataSource = dtSource;		
		querys = QueryUtilXML.obterMapSQL(TipoMovimentoAtivoPostgresDAO.class);
		
	}
	
	public TipoMovimentoAtivo incluir(TipoMovimentoAtivo objTipoMovimentoAtivo) 
	{
		Connection con = null; 
	
		try 
		{
			
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1,objTipoMovimentoAtivo.getDescricao());
			
			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql) ; 
			ResultSet rs = pst.executeQuery() ; 
			if (rs.next())
			{
				 int id = rs.getInt(1); 
				 objTipoMovimentoAtivo.setId(id);
				 
			}else 
			{
				throw new IllegalStateException("Erro ao inserir tipo movimento ativo :Nao tem chave?");	
			}		
			
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return objTipoMovimentoAtivo ;
	}
	
	
	
	
	public boolean excluir (int id)
	{
		Connection con = null; 
		
		try 
		{
		    con =DataSourceUtils.getConnection(dataSource);
		    String sql = querys.get("excluir");
		    logger.info("excluir:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);			
			pst.setInt(1, id);	
			int qtd = pst.executeUpdate();
			
			if (qtd > 1)
				throw  new IllegalStateException("Erro remover tipo movimento ativo:Muitos registros");	 
				
		} catch (SQLException e) 
		{
			throw new IllegalStateException("Erro ao Remover tipo movimento ativo \n", e);		
					
		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return true;
	}




	@Override
	public boolean alterar(TipoMovimentoAtivo objTipoMovimentoAtivo) 
	{
		Connection con = null; 
		try 
		{	
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setString(1, objTipoMovimentoAtivo.getDescricao());
			pst.setInt(2, objTipoMovimentoAtivo.getId());
			
			int qtd = pst.executeUpdate();
			
			if (qtd > 1)
				throw  new IllegalStateException("Erro alterar tipo tipo movimento ativo:Muitos registros");	 
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException("Erro ao Alterar tipo movimento ativo \n", e);	
		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return true;
	}




	@Override
	public Collection<TipoMovimentoAtivo>pesquisar(TipoMovimentoAtivo tipomovimentoativo) 
	{
		TipoMovimentoAtivo objTipoMovimentoAtivo = null;
		Collection<TipoMovimentoAtivo> lstTipoMovimentoAtivo = new java.util.ArrayList<TipoMovimentoAtivo>();
		Connection con = null; 
		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("pesquisar");
			logger.info("pesquisar:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
		
		   
		    if ( tipomovimentoativo!=null &&  tipomovimentoativo.getDescricao()!=null )
		    {
		    	pst.setString(1, "%" + tipomovimentoativo.getDescricao().toLowerCase() + "%"  );
		    	
		    }else
		    {
		    	pst.setString(1, "");
		    }
		    
		  		
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objTipoMovimentoAtivo = mapperTipoMovimentoAtivo(rs);			
				lstTipoMovimentoAtivo.add(objTipoMovimentoAtivo);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException("Erro ao Pesquisa tipo movimento ativo \n", e);	
		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstTipoMovimentoAtivo;
	}




	@Override
	public TipoMovimentoAtivo pesquisarPorId(Integer id) 
	{
		Connection con = null; 
		TipoMovimentoAtivo  objTipoMovimentoAtivo= null;
		
		try 
		{
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTipoMovimentoAtivoPorID");
			logger.info("obterTipoMovimentoAtivoPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
			
			if (rs.next())
			{
				objTipoMovimentoAtivo = mapperTipoMovimentoAtivo(rs);
				
				 
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException("Erro ao Pesquisa tipo movimento ativo \n", e);	
		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
			
			
			return objTipoMovimentoAtivo;
	}

	@Override
	public Collection<TipoMovimentoAtivo> obterTodos() 
	{
		TipoMovimentoAtivo objTipoMovimentoAtivo = null;
		Collection<TipoMovimentoAtivo> lstTipoMovimentoAtivo = new java.util.ArrayList<TipoMovimentoAtivo>();
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
				objTipoMovimentoAtivo = mapperTipoMovimentoAtivo(rs);
				
				lstTipoMovimentoAtivo.add(objTipoMovimentoAtivo);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException("Erro ao Pesquisa Tipo Movimento Ativo \n", e);	
		}finally 
		{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstTipoMovimentoAtivo;
	}
	
	private TipoMovimentoAtivo mapperTipoMovimentoAtivo(ResultSet rs) throws SQLException {
		TipoMovimentoAtivo objTipoMovimentoAtivo;
		objTipoMovimentoAtivo = new TipoMovimentoAtivo ();
		objTipoMovimentoAtivo.setId(rs.getInt("tipo_movimento_ativo_id"));
		objTipoMovimentoAtivo.setDescricao(rs.getString("tipo_movimento_ativo_descricao"));
		return objTipoMovimentoAtivo;
	}

	
}

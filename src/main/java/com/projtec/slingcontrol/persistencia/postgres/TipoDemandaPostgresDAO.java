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
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.persistencia.TipoDemandaDAO;

public class TipoDemandaPostgresDAO implements TipoDemandaDAO {

	 private final  Logger logger  = LoggerFactory.getLogger(TipoDemandaPostgresDAO.class);  
	 
		private DataSource dataSource;
		private Map<String, String> querys; 
		
		
		@Autowired
		public void init(DataSource dtSource) throws IOException 
		{
			dataSource = dtSource;		
			querys = QueryUtilXML.obterMapSQL(TipoDemandaPostgresDAO.class);
			
		}
		
		public TipoDemanda incluir(TipoDemanda  objTipoDemanda) 
		{
			Connection con =null;
		
			try 
			{
				
				con = DataSourceUtils.getConnection(dataSource);
				String sql = querys.get("adicionar");
				logger.info("adicionar:" + sql);
				PreparedStatement pst = con.prepareStatement(sql);
				
				pst.setString(1,objTipoDemanda.getDescricao());
				
				pst.execute();
				sql = querys.get("idAtual");
				pst = con.prepareStatement(sql) ; 
				ResultSet rs = pst.executeQuery() ; 
				if (rs.next())
				{
					 int id = rs.getInt(1); 
					 objTipoDemanda.setId(id);
					 
				}else 
				{
					
					throw new IllegalStateException("Erro ao inserir Tipo Demanda :Nao tem chave?");	
				}		
				
				
			} catch (SQLException e) 
			{
				throw new IllegalStateException(e.getMessage());		
			}finally {
				DataSourceUtils.releaseConnection(con, dataSource);
			}
			
			return objTipoDemanda ;
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
					throw  new IllegalStateException("Erro remover Tipo Demanda:Muitos registros");	 
					
			} catch (SQLException e) 
			{
				throw new IllegalStateException(e.getMessage());		

						
			}finally {
				
				DataSourceUtils.releaseConnection(con, dataSource);
				
			}
			
			return true;
		}




		@Override
		public boolean alterar(TipoDemanda objTipoDemanda) 
		{
			Connection con=null;
			try 
			{	
				con=DataSourceUtils.getConnection(dataSource);
				String sql = querys.get("alterar");
				logger.info("alterar:" + sql);
				PreparedStatement pst = con.prepareStatement(sql);
				
				pst.setString(1,objTipoDemanda.getDescricao());
				pst.setInt(2,objTipoDemanda.getId());
				
				int qtd = pst.executeUpdate();
				
				if (qtd > 1)
					throw  new IllegalStateException("Erro alterar Tipo Demanda:Muitos registros");	 
				
			} catch (SQLException e) 
			{
				throw new IllegalStateException(e.getMessage());		
			}finally {
				
				DataSourceUtils.releaseConnection(con, dataSource);
			}
			
			return true;
		}




		@Override
		public Collection<TipoDemanda>pesquisar(TipoDemanda tipodemanda) 
		{
			TipoDemanda objTipoDemanda = null;
			Collection<TipoDemanda> lstTipoCampo= new java.util.ArrayList<TipoDemanda>();
			Connection con=null;
			
			try 
			{
					
				con=DataSourceUtils.getConnection(dataSource);
				String sql = querys.get("pesquisar");
				logger.info("pesquisar:" + sql);
			    PreparedStatement pst = con.prepareStatement(sql);
			
			   
			    if ( tipodemanda!=null &&  tipodemanda.getDescricao()!=null )
			    {
			    	pst.setString(1, "%" + tipodemanda.getDescricao().toLowerCase() + "%"  );
			    	
			    }else
			    {
			    	pst.setString(1, "");
			    }
			    
			  		
				
				ResultSet rs = pst.executeQuery();
				
				while (rs.next())
				{
					objTipoDemanda = mapperTipoDemanda(rs);					
					
					lstTipoCampo.add(objTipoDemanda);
				}
			}
			catch(SQLException e)
			{
				throw new IllegalStateException(e.getMessage());		

			}finally {
				
				DataSourceUtils.releaseConnection(con, dataSource);
			}
			
			
			return lstTipoCampo;
		}




		@Override
		public TipoDemanda pesquisarPorId(Integer id) 
		{
			
			Connection con=null;
			TipoDemanda objTipoDemanda = null;
			
			try 
			{
				con=DataSourceUtils.getConnection(dataSource);
				String sql = querys.get("obterTipoDemandaPorID");
				logger.info("obterTipoDemandaPorID:" + sql);
				PreparedStatement pst = con.prepareStatement(sql);
				
				pst.setInt(1, id);
				
				ResultSet rs = pst.executeQuery();
				
				if (rs.next())
				{
					
					objTipoDemanda = mapperTipoDemanda(rs);
					
					 
				}
			}
			catch(SQLException e)
			{
				throw new IllegalStateException(e.getMessage());		

			}finally {
				
				DataSourceUtils.releaseConnection(con, dataSource);
			}
				
				
				return objTipoDemanda;
		}
		@Override
		public Collection<TipoDemanda> obterTodos() 
		{
			TipoDemanda objTipoDemanda = null;
			Collection<TipoDemanda> lstTipoDemanda = new java.util.ArrayList<TipoDemanda>();
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
					objTipoDemanda = mapperTipoDemanda(rs);
					
					lstTipoDemanda.add(objTipoDemanda);
				}
			}
			catch(SQLException e)
			{
				throw new IllegalStateException(e.getMessage());		


			}finally {
				
				DataSourceUtils.releaseConnection(con, dataSource);
			}
			
			
			return lstTipoDemanda;
		}
		
		private TipoDemanda mapperTipoDemanda(ResultSet rs) throws SQLException {
			TipoDemanda objTipoDemanda;
			objTipoDemanda = new TipoDemanda ();
			objTipoDemanda.setId(rs.getInt("tipo_demanda_id"));
			
			objTipoDemanda.setDescricao(rs.getString("tipo_demanda_descricao"));
			return objTipoDemanda;
		}
	
}

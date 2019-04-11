package com.projtec.slingcontrol.persistencia.postgres;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.projtec.slingcontrol.infra.QueryUtilXML;
import com.projtec.slingcontrol.modelo.Imagens;
import com.projtec.slingcontrol.persistencia.ImagensDAO;


public class ImagensPostgresDAO  implements ImagensDAO {

	
	private final  Logger logger  = LoggerFactory.getLogger(ImagensPostgresDAO.class);  
	 private DataSource dataSource;
	 private Map<String, String> querys; 
	
	
	@Autowired
	public void init(DataSource dtSource) throws IOException 
	{
		dataSource = dtSource;		
		querys = QueryUtilXML.obterMapSQL(ImagensPostgresDAO.class);
		
	}
	
	@Override
	public Imagens incluir(Imagens objImagens ) 
	{
	
		Connection con = null; 
		
		try 
		{
			
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			
			PreparedStatement pst = con.prepareStatement(sql);		
			pst.setString(1,objImagens.getNome());
			pst.setString(2,objImagens.getDescricao());

		
			if (objImagens.getImagem()!=null)
			{	
				logger.info("objImagens" + "entro");
				pst.setBinaryStream(3, objImagens.getImagem(), (int)objImagens.getTam());
			}
			
		    pst.setString(4,objImagens.getExt());
		    pst.setString(5,objImagens.getNome_arquivo());
		    logger.info("incluir:" + pst.toString());
			
		    
		    
		    
		    
			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql) ; 
			ResultSet rs = pst.executeQuery() ; 
			if (rs.next())
			{
				 int id = rs.getInt(1); 
				 objImagens.setId(id);
				 
			} else {
				throw new IllegalStateException("Erro ao inserir Imagens :Nao tem chave?");	
			}		
			
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return objImagens ;
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
				throw  new IllegalStateException("Erro remover Imagens:Muitos registros");	 
				
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return true;
	}




	@Override
	public boolean alterar(Imagens objImagens ) 
	{
		Connection con=null;
		int qtd ;
		
		try 
		{
			
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterarNomeDesc");
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, objImagens.getNome());
			pst.setString(2, objImagens.getDescricao());
			pst.setInt(3, objImagens.getId());
			logger.info("alterar:" + pst.toString());
			qtd = pst.executeUpdate();
			
			
			if (objImagens.getTam() > 1)
			{
				
				String sql2 = querys.get("alterarImg");
				PreparedStatement pst2 = con.prepareStatement(sql2);
				pst2.setBinaryStream(1, objImagens.getImagem(), (int)objImagens.getTam());
				pst2.setString(2,objImagens.getExt());
			    pst2.setString(3,objImagens.getNome_arquivo());
			    pst2.setInt(4, objImagens.getId());
			    logger.info("alterar:" + pst2.toString());
			    pst2.executeUpdate();
			    
			}
		
			
			
			
			if (qtd > 1 )
				throw  new IllegalStateException("Erro alterar Imagens:Muitos registros");	 
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return true;
	}




	@Override
	public Collection<Imagens> pesquisar (Imagens objImagens) 
	{
		Imagens objetoImagens = null;
		Collection<Imagens> lstImagens = new java.util.ArrayList<Imagens>();
		Connection con=null;
		
		try 
		{
				
		    con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("pesquisar");
			logger.info("pesquisar:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
		
		   
		    if (StringUtils.isNotEmpty(objImagens.getNome()))
		    {
		    	pst.setString(1, "%" + objImagens.getNome().toLowerCase() + "%"  );
		    } else {
		    	pst.setString(1, "%");
		    }
		  
		    if (StringUtils.isNotEmpty(objImagens.getDescricao()))
		    {
		    	pst.setString(2, "%" + objImagens.getDescricao().toLowerCase() + "%" ); 
		    } else {
		    	pst.setString(2, "%");
		    }	

		    logger.info("pesquisar:" + pst.toString());

			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objetoImagens = mapperImagens(rs);
				lstImagens.add(objetoImagens);
			}
			
		} 
		catch(SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} 
		finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
				
		return lstImagens;
	}




	@Override
	public Imagens pesquisarPorId(Integer id) 
	{
	   Imagens  objImagens= null;
		Connection con=null;
		
		try 
		{
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterImagensPorID");
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			logger.info("obterImagensPorID:" + pst.toString());
			ResultSet rs = pst.executeQuery();
			
			if (rs.next())
			{
				objImagens = mapperImagens(rs);				 
			}
		} catch(SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objImagens;
	}
	
	@Override
	public byte[] pesquisarByteId(Integer id)
	{
		
		byte[] imgData= new byte[1024];
		Connection con=null;
		
		try 
		{
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterBytePorID");
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			logger.info("obterBytePorID" + pst.toString());
			ResultSet rs = pst.executeQuery();
			while (rs.next ())
		    {   
				 imgData= rs.getBytes("imagens_arq");
		    }
			
		} 
		catch(SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} 
		finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return imgData ;
		
	}
	
	@Override
	public List<Imagens> pesquisar(String nome, String descricao) throws Exception 
	{
		Imagens objImagens= null;
		Connection con = null;
		
		List<Imagens> lstImagens = new ArrayList<Imagens>();
		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			StringBuffer sql = new StringBuffer();
			
			sql.append(" select imagens_id, imagens_nome, imagens_descricao, imagens_arq, imagens_extensao ");
			sql.append("   from imagens  ");
			sql.append("  where imagens_nome like ? ");
			sql.append("    and imagens_descricao like ? ");
			sql.append("  order by  imagens_nome");
		    
			PreparedStatement pst = con.prepareStatement(sql.toString());
			
			pst.setString(1, nome + "%");
			pst.setString(2, descricao + "%");
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objImagens = mapperImagens(rs);
				lstImagens.add(objImagens);
			}
			
		} 
		catch(SQLException e)	{
			throw new IllegalStateException(e.getMessage());		
		} 
		finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstImagens;
		
	}
	
	@Override
	public Imagens pesquisarPeloNome(String nome)  
	{
		Imagens objImagens= null;
		Connection con = null;
		
		List<Imagens> lstImagens = new ArrayList<Imagens>();
		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			StringBuffer sql = new StringBuffer();
			
			sql.append(" select imagens_id, imagens_nome, imagens_descricao, imagens_arq, imagens_extensao ");
			sql.append("   from imagens  ");
			sql.append("  where imagens_nome like ? ");
			sql.append("  order by  imagens_nome");
		    
			PreparedStatement pst = con.prepareStatement(sql.toString());
			pst.setString(1, nome);
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objImagens = mapperImagens(rs);
				lstImagens.add(objImagens);
			}
			
		} 
		catch(SQLException e)	{
			throw new IllegalStateException(e.getMessage());		
		} 
		finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		if (lstImagens.size() > 0)
			return lstImagens.iterator().next();
		else
			return null;
		
	}
	
	@Override
	public Collection<Imagens> obterTodos() 
	{
		Imagens objImagens= null;
		Collection<Imagens> lstImagens = new java.util.ArrayList<Imagens>();
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
				objImagens = mapperImagens(rs);
				
				lstImagens.add(objImagens);
			}
		} catch(SQLException e)	{
			throw new IllegalStateException(e.getMessage());		
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return lstImagens;
	}
	
	private Imagens mapperImagens(ResultSet rs) throws SQLException {
		
		Imagens objImagens;
		
		objImagens = new Imagens();
		objImagens.setId(rs.getInt("imagens_id"));
		objImagens.setNome(rs.getString("imagens_nome"));
		objImagens.setDescricao(rs.getString("imagens_descricao"));
		objImagens.setImagem(rs.getBinaryStream("imagens_arq"));
		objImagens.setExt(rs.getString("imagens_extensao"));
		return objImagens;
	}

	
	
}

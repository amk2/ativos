package com.projtec.slingcontrol.persistencia.postgres;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.projtec.slingcontrol.infra.QueryUtilXML;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.persistencia.InformacoesLocaisDAO;

public class InformacoesLocaisPostgresDAO implements InformacoesLocaisDAO
{

	private final Logger logger = LoggerFactory.getLogger(InformacoesLocaisPostgresDAO.class);

	private DataSource dataSource;
	
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource dtSource) throws IOException {
		dataSource = dtSource;
		querys = QueryUtilXML.obterMapSQL(InformacoesLocaisPostgresDAO.class);

	}

	
	@Override
	public boolean alterar(Informacoes objInformacoes) 
	{
		Connection con = null; 
		try 
		{	
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			setParametrosStatment(objInformacoes,pst);
			int qtd = pst.executeUpdate();
			
			if (qtd > 1)
				throw  new IllegalStateException("Erro alterar informacoes locais :Muitos registros");	 
			
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
	public boolean excluirPorLocaisID(Integer idLocais) {
		
		Connection con = null; 
		try 
		{
		    con =DataSourceUtils.getConnection(dataSource);
		    String sql = querys.get("excluirPorLocais");
		    logger.info("excluirPorLocais:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);			
			pst.setInt(1, idLocais);	
			pst.executeUpdate();
			
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
	public Informacoes incluir(Informacoes objInformacoes) {
		
		Connection con = null; 
		
		try 
		{
			
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			setParametrosStatment(objInformacoes,pst);
			
			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql) ; 
			ResultSet rs = pst.executeQuery() ; 
			if (rs.next())
			{
				 int id = rs.getInt(1); 
				 objInformacoes.setId(id);
				 
			}else 
			{
				throw new IllegalStateException("Erro ao inserir Informacoes Locais:Nao tem chave?");	
			}		
			
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
		}finally 
		{
			  DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return objInformacoes ;
	}
	
	@Override
	public boolean existeRegistroLayoutId(Integer layoutId)
	{
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("existeConfiguracoesAssociada");
			logger.info("existeConfiguracoesAssociada:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, layoutId);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				DataSourceUtils.releaseConnection(con, dataSource);
				return true;
			}
			DataSourceUtils.releaseConnection(con, dataSource);
			return false ;
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}
	}

	@Override
	public Collection<Informacoes> obterInformacoesLocais(Integer idLocais) {
		
		Informacoes objInfos= null;
		Collection<Informacoes> lstInformacoes = new java.util.ArrayList<Informacoes>();
		Connection con = null; 		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesLocais");
			logger.info("obterInformacoesLocais:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
		    pst.setInt(1, idLocais);
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objInfos = mapper(rs);
				
				lstInformacoes.add(objInfos);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		}finally 
		{
			  DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstInformacoes;
	}

	@Override
	public List<Informacoes> obterInformacoesReferencia(Integer idRef)
	{
		Informacoes objInfos= null;
		List<Informacoes> lstInformacoes = new java.util.ArrayList<Informacoes>();
		Connection con = null; 		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesLocaisRef");
			logger.info("obterInformacoesLocaisRef:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
		    pst.setInt(1, idRef);
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objInfos = mapper(rs);
				
				lstInformacoes.add(objInfos);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		}finally 
		{
			  DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstInformacoes;
	}


	private void setParametrosStatment(Informacoes objInformacoes,PreparedStatement pst) throws SQLException 
	{
		pst.setString(1, objInformacoes.getDescricao());
		pst.setInt(2, objInformacoes.getConfiguracoesId());
	    pst.setInt(3,objInformacoes.getIdTipo());
	    
	    
	    if (objInformacoes.getReferenciaId()==null)
		{
			pst.setNull(4,java.sql.Types.INTEGER );
			
		}else
		{
			pst.setInt(4, objInformacoes.getReferenciaId());
		}
		
	}
	
	private Informacoes mapper(ResultSet rs) throws SQLException {
		
		Informacoes objInformacoes = new Informacoes();
        
		objInformacoes.setId(rs.getInt("informacoes_id"));				
        objInformacoes.setDescricao(rs.getString("informacoes_descricao"));		
        objInformacoes.setConfiguracoesId(rs.getInt("layout_configuracoes_id"));
        objInformacoes.setIdTipo(rs.getInt("locais_id"));
        
        objInformacoes.setReferenciaId(rs.getInt("informacoes_id_referencia"));
       	
        return objInformacoes;
	}

}

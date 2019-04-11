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
import com.projtec.slingcontrol.persistencia.InformacoesAtivoDAO;


public class InformacoesAtivoPostgresDAO implements InformacoesAtivoDAO {
	
	 private final  Logger logger  = LoggerFactory.getLogger(InformacoesAtivoPostgresDAO.class);  
	 private DataSource dataSource;
	 private Map<String, String> querys; 
	
	
	@Autowired
	public void init(DataSource DataSourceUtils) throws IOException 
	{
		dataSource = DataSourceUtils;		
		querys = QueryUtilXML.obterMapSQL(InformacoesAtivoPostgresDAO.class);
		
	}
	
	public Informacoes incluir(Informacoes objInformacoes) 
	{
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
				throw new IllegalStateException("Erro ao inserir Informacoes :Nao tem chave?");	
			}		
			
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return objInformacoes ;
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
				throw  new IllegalStateException("Erro remover Informacoes:Muitos registros");	 
				
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
					
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return false;
	}
	
	
	
	public boolean excluirAtivoID (int id)
	{
		Connection con = null; 
		try 
		{
		    con =DataSourceUtils.getConnection(dataSource);
		    String sql = querys.get("excluirAtivoId");
		    logger.info("excluir:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);			
			pst.setInt(1, id);	
			pst.executeUpdate();
			
			
				
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
					
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return false;
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
				throw  new IllegalStateException("Erro alterar informacoes :Muitos registros");	 
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return true;
	}

	@Override
	public Informacoes pesquisarPorId(Integer id) 
	{
		
		Informacoes objInformacoes= null;
		Connection con = null; 
		try 
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesPorID");
			logger.info("obterInformacoesPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				objInformacoes = mapper(rs);			
	 		}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return objInformacoes;
	}
	
	@Override
	public boolean existeRegistroLayoutId(Integer layoutId) 
	{
		Connection con = null; 
		try 
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("existeConfiguracoesAssociada");
			logger.info("existeConfiguracoesAssociada:" + sql + layoutId);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, layoutId);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				DataSourceUtils.releaseConnection(con, dataSource);
				return true;			
	 		}
			DataSourceUtils.releaseConnection(con, dataSource);
			return false;
			
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
	}


	
	
	@Override
	public Collection<Informacoes> obterInformacoesAtivos(Integer idAtivo) 
	{
		Informacoes objInformacoesAtivo= null;
		Collection<Informacoes> lstInformacoes = new java.util.ArrayList<Informacoes>();
		Connection con = null; 
		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesAtivos");
			logger.info("\n obterInformacoesAtivos:\n" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
		    pst.setInt(1, idAtivo);
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objInformacoesAtivo = mapper(rs);
				
				lstInformacoes.add(objInformacoesAtivo);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstInformacoes;
	}
	
	
	@Override
	public List<Informacoes> obterInformacoesReferencia(
			Integer idReferencia)
	{
		Informacoes objInformacoesAtivo= null;
		List<Informacoes> lstInformacoes = new java.util.ArrayList<Informacoes>();
		Connection con = null; 
		
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesReferenciaAtivos");
			logger.info("obterInformacoesReferenciaAtivos:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
		    pst.setInt(1, idReferencia);
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objInformacoesAtivo = mapper(rs);
				
				lstInformacoes.add(objInformacoesAtivo);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstInformacoes;
	}
	
	@Override
	public List<Informacoes> obterInformacoesPelaConfiguracaoId(
			Integer configuracaoId, Integer ativoId)
	{

		Informacoes objInformacoesAtivo= null;
		List<Informacoes> lstInformacoes = new java.util.ArrayList<Informacoes>();
		Connection con = null; 
		
		try 
		{
				
			StringBuffer sql = new StringBuffer();
			sql.append(" select informacoes_id, informacoes_descricao, layout_configuracoes_id , informacoes_id_referencia, ativos_id ");
			sql.append("   from informacoes_ativo ");
			sql.append("  where layout_configuracoes_id = ? ");
			sql.append("    and ativos_id = ? ");
			
			logger.info("obterInformacoesPelaConfiguracaoId: " + sql.toString());
			
			con = DataSourceUtils.getConnection(dataSource);
			PreparedStatement pst = con.prepareStatement(sql.toString());
			
		    pst.setInt(1, configuracaoId);
		    pst.setInt(2, ativoId);
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				
				objInformacoesAtivo = mapper(rs);
				lstInformacoes.add(objInformacoesAtivo);
				
			}
			
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstInformacoes;
	}

	private void setParametrosStatment(Informacoes objInformacoes,
			PreparedStatement pst) throws SQLException {
		pst.setString(1, objInformacoes.getDescricao());
		pst.setInt(2, objInformacoes.getConfiguracoesId());
		pst.setInt(3, objInformacoes.getIdTipo());
		
		// if (objInformacoes.getIdReferencia()==null)
		if (objInformacoes.getReferenciaId()==null)
		{
			pst.setNull(4,java.sql.Types.INTEGER );
			
		}else
		{
			// pst.setInt(4, objInformacoes.getIdReferencia());
			pst.setInt(4, objInformacoes.getReferenciaId());
		}
		
		
		

	}

	private Informacoes mapper(ResultSet rs) throws SQLException {
		Informacoes objInformacoes = new Informacoes();
		objInformacoes.setId(rs.getInt("informacoes_id"));
		objInformacoes.setDescricao(rs.getString("informacoes_descricao"));
		objInformacoes.setConfiguracoesId(rs.getInt("layout_configuracoes_id"));
		objInformacoes.setIdTipo(rs.getInt("ativos_id"));
		
		objInformacoes.setReferenciaId(rs.getInt("informacoes_id_referencia"));

		return objInformacoes;
	}


	
}

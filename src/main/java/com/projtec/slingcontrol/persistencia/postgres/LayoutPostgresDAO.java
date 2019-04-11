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
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.TipoLayout;
import com.projtec.slingcontrol.persistencia.LayoutDAO;

public class LayoutPostgresDAO implements LayoutDAO {

	private final Logger logger = LoggerFactory
			.getLogger(LayoutPostgresDAO.class);

	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource dtSource) throws IOException {
		dataSource = dtSource;
		querys = QueryUtilXML.obterMapSQL(LayoutPostgresDAO.class);
	}

	public Layout incluir(Layout objLayout) {

		Connection con = null;
		
		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParameter(objLayout, pst);

			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				objLayout.setId(id);

			} else {
				throw new IllegalStateException("Erro ao inserir layout :Nao tem chave?");
			}

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		}finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objLayout;
	}
	
	
	

	
	
	public boolean excluir(Integer id) {
		Connection con=null; 
		
		try {
			con = DataSourceUtils.getConnection(dataSource);
			
			String sql = querys.get("excluir");
			logger.info("excluir:" + sql + id);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro remover layout:Muitos registros");

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		}finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}

	@Override
	public boolean alterar(Layout objLayout) {
		Connection con =null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParameter(objLayout, pst);
			pst.setInt(4, objLayout.getId());
			
			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro alterar layout:Muitos registros");

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		}finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}
	
	@Override
	public Layout pesquisarPorId(Integer id) {

		Layout objLayout = null;
		Connection con =null;

		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterLayoutPorID");
			logger.info("obterLayoutPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				objLayout = mapperLayout(rs);

			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		}finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objLayout;
	}
	
	@Override
	public Layout pesquisar(String nome, Integer tipoLayoutId) {

		Connection con = null;

		try {
			
			con = DataSourceUtils.getConnection(dataSource);
			
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT layout_id, layout_descricao, layout_nome, tplayout.tipo_layout_id, ");
			sql.append("        tplayout.tipo_layout_descricao ");
			sql.append("	    FROM layouts l "); 
			sql.append("   LEFT JOIN tipo_layout tpLayout on (l.tipo_layout_id=tplayout.tipo_layout_id) ");
			sql.append("  WHERE tplayout.tipo_layout_id = ? ");
			sql.append("    AND layout_nome = ? ");
			
			PreparedStatement pst = con.prepareStatement(sql.toString());

			pst.setInt(1, tipoLayoutId);
			pst.setString(2, nome);

			ResultSet rs = pst.executeQuery();
			
			Layout objLayout = null;

			if (rs.next()) {
				objLayout = mapperLayout(rs);
			}
			
			return objLayout;
			
		} 
		catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		}
		finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

	}
	
	
	@Override
	public Collection<Layout> pesquisar(Layout paramLayout)
	{
		Layout layout = null;
		Collection<Layout> listaLayout = new java.util.ArrayList<Layout>();
		Connection con = null; 
		
		try 
		{
				
			con= DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("pesquisar");
		 
			if (paramLayout != null && paramLayout.getNome() != null
					&& !"".equals(paramLayout.getNome())) { 
		//		if (paramLayout.getTipoLayout() == null) {
					sql = sql
							+ " AND lower(layout_nome) like '%"
							+ paramLayout.getNome().toLowerCase() + "%'";
				 
		//	}
			}
		    
			if (paramLayout != null && paramLayout.getDescricao() != null
					&& !"".equals(paramLayout.getDescricao())) {
			//	if (paramLayout.getTipoLayout() == null) {
					
					sql = sql
							+ " AND lower(layout_descricao) like '%"
							+ paramLayout.getDescricao().toLowerCase() + "%'";
				 
			//}
			}
				
			if (paramLayout != null && paramLayout.getTipoLayout() != null){
				if (paramLayout.getTipoLayout().getId()!=0) {
					sql = sql + " AND tplayout.tipo_layout_id= " + paramLayout.getTipoLayout().getId();				 
			}else{
				sql = sql + "AND tplayout.tipo_layout_id!= " + paramLayout.getTipoLayout().getId();
			}
			    sql = sql + "order by layout_nome";
				}
			 	
		    logger.info("pesquisar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				layout = mapperLayout(rs);
				
				listaLayout.add(layout);
			}
		    	
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return listaLayout;
	}


	public Collection<Layout>  obterTodosComID(Integer id)
	{

		Layout objLayout= null;
		Collection<Layout> lstLayout = new java.util.ArrayList<Layout>();
		Connection con=null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTodosComID");
			logger.info("obterTodosComID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				objLayout = mapperLayout(rs);
				lstLayout.add(objLayout);

			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstLayout;
	}	
	
	
	@Override
	public Collection<Layout> obterTodos() 
	{
		Layout objLayout= null;
		Collection<Layout> lstLayout = new java.util.ArrayList<Layout>();
		Connection con=null;
		
		try 
		{
			con= DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTodos");
			logger.info("obterTodos:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
	
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objLayout = mapperLayout(rs);
				
				lstLayout.add(objLayout);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		}finally{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstLayout;
	}	

	private void setParameter(Layout objLayout, PreparedStatement pst) throws SQLException 
	{
		pst.setString(1, objLayout.getNome());
		pst.setString(2, objLayout.getDescricao());
		pst.setInt(3, objLayout.getTipoLayout().getId());
	}
	
	
	private Layout mapperLayout(ResultSet rs) throws SQLException {
		Layout objLayout;
		TipoLayout tipo;
		objLayout = new Layout();
		objLayout.setId(rs.getInt("layout_id"));
		objLayout.setNome(rs.getString("layout_nome"));
		objLayout.setDescricao(rs.getString("layout_descricao"));
		tipo = new TipoLayout();
		tipo.setId(rs.getInt("tipo_layout_id"));
		tipo.setDescricao(rs.getString("tipo_layout_descricao"));
		objLayout.setTipoLayout(tipo);
		objLayout.setDescricao(rs.getString("layout_descricao"));
		return objLayout;
	}


	@Override
	public Collection<Layout> obterTodosFiltroLayout(String nomeLayout) 
	{
		Layout objLayout= null;
		Collection<Layout> lstLayout = new java.util.ArrayList<Layout>();
		Connection con=null;
		
		try 
		{
			con= DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTodosFiltroLayout");
			logger.info("obterTodosFiltroLayout:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
		    pst.setString(1, nomeLayout);
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objLayout = mapperLayout(rs);
				
				lstLayout.add(objLayout);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		}finally{
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return lstLayout;
		
	}	

}

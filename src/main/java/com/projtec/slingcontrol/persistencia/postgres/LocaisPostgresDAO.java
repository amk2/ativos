package com.projtec.slingcontrol.persistencia.postgres;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.projtec.slingcontrol.infra.QueryUtilXML;
import com.projtec.slingcontrol.modelo.Estoque;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.persistencia.LocaisDAO;

public class LocaisPostgresDAO implements LocaisDAO {

	private final Logger logger = LoggerFactory.getLogger(LocaisPostgresDAO.class);

	private DataSource dataSource;
	
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource dtSource) throws IOException {
		dataSource = dtSource;
		querys = QueryUtilXML.obterMapSQL(LocaisPostgresDAO.class);

	}
	
	@Override
	public Local pesquisarPelaDescricao(String nome) 
		throws Exception
	{
		
		Connection con = null;
		try 
		{
				
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT locais_id, locais_nome, locais_descricao, locais_latitude, ");
			sql.append(" 		locais_longitude, locais_localizacao_x, locais_localizacao_y, ");
			sql.append(" 		locais_identificacao, estoque_id ");
			sql.append("   FROM locais ");
			sql.append("  WHERE locais_nome = ? ");
			sql.append("  ORDER BY locais_nome ");
			
			con=DataSourceUtils.getConnection(dataSource);
		    PreparedStatement pst = con.prepareStatement(sql.toString());
		    
		    pst.setString(1, nome);
			
			ResultSet rs = pst.executeQuery();
			
			Local objLocal= null;
			
			while (rs.next())
			{
				objLocal = new Local();
				objLocal.setId(rs.getInt("locais_id"));
				objLocal.setNome(rs.getString("locais_nome"));
				objLocal.setDescricao(rs.getString("locais_descricao")); 
				objLocal.setLatitude(rs.getString("locais_latitude"));
				objLocal.setLongitude(rs.getString("locais_longitude"));
				objLocal.setLocalizacao_x(rs.getString("locais_localizacao_x"));
				objLocal.setLocalizacao_y(rs.getString("locais_localizacao_y"));
				objLocal.setIdentificacao(rs.getString("locais_identificacao"));
				
			}
			
			return objLocal;
			
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
			
		}finally 
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

	}

	@Override
	public Collection<Local> obterTodosNomes() 
	{
		
		Local objLocal= null;
		Collection<Local> lstLocal = new java.util.ArrayList<Local>();
		Connection con = null;
		try 
		{
				
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTodosNomes");
			logger.info("obterTodosNomes:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
	
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objLocal = new Local();
				objLocal.setId(rs.getInt("locais_id"));
				objLocal.setNome(rs.getString("locais_nome"));
				objLocal.setDescricao(rs.getString("locais_descricao")); 
				objLocal.setLatitude(rs.getString("locais_latitude"));
				objLocal.setLongitude(rs.getString("locais_longitude"));
				objLocal.setLocalizacao_x(rs.getString("locais_localizacao_x"));
				objLocal.setLocalizacao_y(rs.getString("locais_localizacao_y"));
				objLocal.setIdentificacao(rs.getString("locais_identificacao"));
				
								
				lstLocal.add(objLocal);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
			
		}finally 
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstLocal;
	}

	@Override
	public Local incluir(Local local) {
		
		Connection con = null;
		try {

			
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("incluir");
			logger.info("incluir:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParameter(local, pst);

			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				local.setId(id);

			} else {
				throw new IllegalStateException("Erro ao inserir locais :Nao tem chave?");
			}

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		}finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return local;
	}
	
	
	

	@Override
	public Boolean alterar(Local local) {
		
		Connection con = null;
		
		try 
		{	
			con=DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			setParameter(local, pst);
			pst.setInt(10,local.getId());
			
			int qtd = pst.executeUpdate();
			
			if (qtd > 1)
				throw  new IllegalStateException("Erro alterar Local:Muitos registros");	 
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
		}finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return Boolean.TRUE;
	}

	@Override
	public Boolean excluir(Integer idLocal) {
		
		Connection con = null;
		
		try 
		{
		    con = DataSourceUtils.getConnection(dataSource);
		    String sql = querys.get("excluir");
		    logger.info("excluir:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);			
			pst.setInt(1, idLocal);	
			int qtd = pst.executeUpdate();
			
			if (qtd > 1)
				throw  new IllegalStateException("Erro remover Locais:Muitos registros");	 
				
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
					
		}finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		return true;
	}



	@Override
	public Local pesquisarPorId(Integer id) {

		Local local = null;
		Connection con = null;

		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterLocalPorID");
			logger.info("obterLocalPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				local =  new Local(); 
				local.setDescricao(rs.getString("locais_descricao"));
				local.setNome(rs.getString("locais_nome"));
				local.setId(rs.getInt("locais_id"));
				local.setLatitude(rs.getString("locais_latitude"));
				local.setLongitude(rs.getString("locais_longitude"));
				local.setLocalizacao_x(rs.getString("locais_localizacao_x"));
				local.setLocalizacao_y(rs.getString("locais_localizacao_y"));
				local.setIdentificacao(rs.getString("locais_identificacao"));
				Estoque estoque = new Estoque();
				estoque.setId(rs.getInt("estoque_id"));
				local.setEstoque(estoque);
				Layout layout =new Layout();
				layout.setId(rs.getInt("layout_id"));
				local.setLayout(layout);

			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		}finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return local;
	}
	
	
	private void setParameter(Local local, PreparedStatement pst) throws SQLException {
		
		pst.setInt(1, local.getLayout().getId());
		pst.setString(2, local.getNome());
		pst.setString(3, local.getDescricao());
		pst.setString(4, local.getLatitude());
		pst.setString(5, local.getLongitude());
		pst.setString(6, local.getLocalizacao_x());
		pst.setString(7, local.getLocalizacao_y());
		pst.setString(8, local.getIdentificacao());
		pst.setInt(9, local.getEstoque().getId());
		
		
	}
	
	
	@Override
	public List<Local> pesquisar(Local objLocal) 
	{
		Local locais = null;
		Connection con = null;
		
		List<Local> lstLocal= new java.util.ArrayList<Local>();		
		
		try
		{
		
			con = DataSourceUtils.getConnection(dataSource);
			String sql = " " + 
					"	SELECT               " + 
					"	 DISTINCT  locais_id,         " + 
					"	  locais_id_referencia, " + 
					"	  l.layout_id,  " + 
					"	  locais_nome, "+ 
					"	  locais_descricao, "+ 
					"	  locais_latitude, "+
					"	  locais_longitude, "+
					"	  locais_localizacao_x, "+
					"	  locais_localizacao_y, "+
					"	  locais_identificacao "+
					
					"   FROM  " + 
					"   locais l"  + 
					"   left join   layout_configuracoes conf on (l.layout_id =  conf.layout_id) " + 
					"  where  1=1  "  ; 
					
			if (objLocal.getNome() != null
					&& !"".equals(objLocal.getNome()))
			{
				sql = sql + " AND  l.locais_nome like  '%"+ objLocal.getNome() + "%' "; 
			}
			
			
			if (objLocal.getDescricao() != null
					&& !"".equals(objLocal.getDescricao()))
			{
				sql = sql + " AND  l.locais_descricao like  '%"+ objLocal.getDescricao() + "%' "; 
			}

			if (objLocal.getLayout() != null && objLocal.getLayout().getId() != 0)
			{
				sql = sql + " AND  l.layout_id=" + objLocal.getLayout().getId();
			}	
			
			
			
			
			
			if (objLocal.getLatitude() != null && !"".equals(objLocal.getLatitude()))
			{
				sql = sql + " AND  l.locais_latitude= '" + objLocal.getLatitude() + "' ";
			}		
			
			if (objLocal.getLongitude() != null && !"".equals(objLocal.getLongitude()))
			{
				sql = sql + " AND  l.locais_longitude= '" + objLocal.getLongitude() + "' ";
			}	
			
			if (objLocal.getLocalizacao_x() != null && !"".equals(objLocal.getLocalizacao_x()))
			{
				sql = sql + " AND  l.locais_localizacao_x= '" + objLocal.getLocalizacao_x() + "' ";
			}		
			
			if (objLocal.getLocalizacao_y() != null && !"".equals(objLocal.getLocalizacao_y()))
			{
				sql = sql + " AND  l.locais_localizacao_y= '" + objLocal.getLocalizacao_y() + "' ";
			}
			
			if (objLocal.getIdentificacao() != null && !"".equals(objLocal.getIdentificacao()))
			{
				sql = sql + " AND  l.locais_identificacao= '" + objLocal.getIdentificacao() + "' ";
			
			}
		
			
			if (objLocal.getInfos() != null)
			{
				Collection<Informacoes> lstInfo =objLocal.getInfos();
					
				Informacoes info;
				Boolean maior = objLocal.getInfos().size() > 0;
				if (maior)
				{
					sql = sql + " AND (";
				}
				
				for (Iterator<Informacoes> iterator = lstInfo.iterator(); iterator
						.hasNext();)
				{
					info = iterator.next();
					info.getDescricao();
					if (!"".equals(info.getDescricao()))
					{
					
						sql = sql
								+ ""
								+ " conf.layout_configuracoes_id in (  select    "
								+ "       config.layout_configuracoes_id        "
								+ " from layout_configuracoes config inner join informacoes_locais info "
								+ " on (config.layout_configuracoes_id = info.layout_configuracoes_id and  l.locais_id=info.locais_id) "
								+ " where lower(info.informacoes_descricao) like '%"
								+ info.getDescricao() + "%' )";

						if (iterator.hasNext())
						{
							sql = sql + " or ";
						}
					}

				}

				
				if (maior)
				{
					sql = sql + " )";
				}

			}
			
			sql = sql + " order by locais_nome";

			logger.info("pesquisar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				locais = mapper(rs);
				lstLocal.add(locais);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}	
		return lstLocal;
	}
	
//	@Override
//	public Collection<Estoque> obterEstoques() {
//		Layout objLayout = null;
//		Collection<Layout> lstLayout = new ArrayList<Layout>();
//		Connection con = null;
//		try {
//			con = DataSourceUtils.getConnection(dataSource);
//			String sql = querys.get("obterEstoques");
//			logger.info("obterTodos:" + sql);
//		    PreparedStatement pst = con.prepareStatement(sql);
//			ResultSet rs = pst.executeQuery();
//			while (rs.next()) {
//				objLayout = mapperLayout(rs);
//				lstLayout.add(objLayout);
//			}
//		} catch(SQLException e) {
//			throw new IllegalStateException("Erro ao Pesquisa Layout \n", e);	
//		} finally {
//			DataSourceUtils.releaseConnection(con, dataSource);
//		}
//		return lstLayout;
//	}	

	
	@Override
	public List<Local> obterTodos() 
	{	

		Local objLocais= null;
		List<Local> lstLocais= new java.util.ArrayList<Local>();
		Connection con = null; 
		
		try 
		{
				
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTodos");
			logger.info("obterTodos:" + sql);
		    PreparedStatement pst = con.prepareStatement(sql);
	
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next())
			{
				objLocais = mapper(rs);
				lstLocais.add(objLocais);
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		}finally {
			
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		return lstLocais;
		
		
		
	}
	
	@Override
	public List<Local> obterLocaisPorEstoque(Estoque estoque) {
		Local objLocais = null;
		List<Local> lstLocais = new ArrayList<Local>();
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterLocaisPorEstoque");
			logger.info("obterLocaisPorEstoque:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, estoque.getId());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				objLocais = mapper(rs);
				lstLocais.add(objLocais);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return lstLocais;
	}
	
	private Local mapper(ResultSet rs) throws SQLException 
	{
		Local	local =  new Local(); 
		local.setDescricao(rs.getString("locais_descricao"));
		local.setNome(rs.getString("locais_nome"));
		local.setId(rs.getInt("locais_id"));
		local.setLatitude(rs.getString("locais_latitude"));
		local.setLongitude(rs.getString("locais_longitude"));
		local.setLocalizacao_x(rs.getString("locais_localizacao_x"));
		local.setLocalizacao_y(rs.getString("locais_localizacao_y"));
		local.setIdentificacao(rs.getString("locais_identificacao"));
		Layout layout =new Layout();
		layout.setId(rs.getInt("layout_id"));
		local.setLayout(layout);
		return local;
	}
	
	

}

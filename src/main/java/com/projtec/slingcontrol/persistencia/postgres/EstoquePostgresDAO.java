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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.projtec.slingcontrol.infra.QueryUtilXML;
import com.projtec.slingcontrol.modelo.Estoque;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.persistencia.EstoqueDAO;

public class EstoquePostgresDAO extends BasePostgresDAO implements EstoqueDAO {

	private final Logger logger = LoggerFactory
			.getLogger(EstoquePostgresDAO.class);

	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource dtSource) throws IOException {
		dataSource = dtSource;
		querys = QueryUtilXML.obterMapSQL(EstoquePostgresDAO.class);

	}

	public Estoque adicionar(Estoque objEstoque) {
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setString(1, objEstoque.getNome());
			pst.setString(2, objEstoque.getDescricao());
			pst.setInt(3, objEstoque.getLayout().getId());

			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				objEstoque.setId(id);

			} else {
				throw new IllegalStateException(
						"Erro ao inserir estoque :Nao tem chave?");
			}

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objEstoque;
	}

	public boolean excluir(int id) {
		Connection con = null;

		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluir");
			logger.info("excluir:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro remover estoque:Muitos registros");

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}

	@Override
	public boolean alterar(Estoque objestoque) {
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setString(1, objestoque.getNome());
			pst.setString(2, objestoque.getDescricao());
			pst.setInt(3, objestoque.getLayout().getId());

			pst.setInt(4, objestoque.getId());
			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro alterar estoque:Muitos registros");

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}

	@Override
	public Collection<Estoque> pesquisar(Estoque estoque) {
		Estoque obje = null;
		Collection<Estoque> listaEstoque = new java.util.ArrayList<Estoque>();
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			StringBuffer sql = new StringBuffer();
			// sql.append(querys.get("pesquisar"));
			
			sql.append(" SELECT est.estoque_id, est.estoque_nome, est.estoque_descricao, est.layout_id ");
			sql.append("   FROM estoque est ");
			sql.append("  WHERE lower(est.estoque_nome) like ? and lower(est.estoque_descricao) like ? ");
			
			if(estoque != null && estoque.getLayout() !=null && estoque.getLayout().getId() > 0) {
				sql.append(" and est.layout_id = "  + estoque.getLayout().getId()) ;
			}
			
			if (estoque.getInfos() != null) {
				
				List<Informacoes> informacoesList = new ArrayList<Informacoes>(estoque.getInfos());
				String sqlComplementar = getComplementoQuerieParaConsultaComCamposDinamicos(
						"est", "estoque_id", "informacoes_estoque", informacoesList);
				sql.append(sqlComplementar);
				 
			}
			
			sql.append(" ORDER BY est.estoque_nome ");
			
			logger.info("pesquisar:" + sql);
			
			PreparedStatement pst = con.prepareStatement(sql.toString());

			System.out.println("Estoque pesquisa:" + estoque);
			if (estoque != null && estoque.getNome() != null
					&& !"".equals(estoque.getNome())) {
				pst.setString(1, "%" + estoque.getNome().toLowerCase() + "%");

			} else {
				pst.setString(1, "%");
			}

			if (estoque != null && estoque.getDescricao() != null
					&& !"".equals(estoque.getDescricao())) {
				pst.setString(2, "%" + estoque.getDescricao().toLowerCase()
						+ "%");

			} else {
				pst.setString(2, "%");
			}

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				obje = mapperEstoque(rs);

				listaEstoque.add(obje);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return listaEstoque;
	}

	@Override
	public Estoque pesquisarPorId(Integer id) {
		Estoque objestoque = null;
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterEstoquePorID");
			logger.info("obterEstoquePorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				objestoque = mapperEstoque(rs);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return objestoque;
	}

	@Override
	public Collection<Estoque> obterTodos() {
		Estoque objestoque = null;
		Collection<Estoque> listaEstoque = new java.util.ArrayList<Estoque>();
		Connection con = null;

		try {
			
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTodos");
			logger.info("obterTodos:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				objestoque = mapperEstoque(rs);

				listaEstoque.add(objestoque);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return listaEstoque;
	}

	private Estoque mapperEstoque(ResultSet rs) throws SQLException {
		Estoque estoque = new Estoque();
		Layout layout = new Layout();
		estoque.setId(rs.getInt("estoque_id"));
		estoque.setNome(rs.getString("estoque_nome"));
		estoque.setDescricao(rs.getString("estoque_descricao"));
		layout.setId(rs.getInt("layout_id"));
		estoque.setLayout(layout);
		return estoque;
	}

}

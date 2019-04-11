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
import com.projtec.slingcontrol.persistencia.InformacoesEstoqueDAO;

public class InformacoesEstoquePostgresDAO implements InformacoesEstoqueDAO {

	private final Logger logger = LoggerFactory
			.getLogger(InformacoesLocaisPostgresDAO.class);
	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource dtSource) throws IOException {
		dataSource = dtSource;
		querys = QueryUtilXML.obterMapSQL(InformacoesEstoquePostgresDAO.class);
	}

	@Override
	public Informacoes incluir(Informacoes objInformacoes) {
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			setParametrosStatment(objInformacoes, pst);
			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				objInformacoes.setId(id);
			} else {
				throw new IllegalStateException(
						"Erro ao inserir Informacoes Estoque: Nao tem chave?");
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return objInformacoes;
	}

	private void setParametrosStatment(Informacoes objInformacoes,
			PreparedStatement pst) throws SQLException {
		pst.setString(1, objInformacoes.getDescricao());
		pst.setInt(2, objInformacoes.getConfiguracoesId());
		pst.setInt(3, objInformacoes.getIdTipo());
		
		if (objInformacoes.getReferenciaId() == null) {
			pst.setNull(4, java.sql.Types.INTEGER);
		} else {
			pst.setInt(4, objInformacoes.getReferenciaId());
		}
		
	}

	@Override
	public boolean excluir(int id) {
		return false;
	}

	@Override
	public boolean alterar(Informacoes objInformacoes) {
		return false;
	}

	@Override
	public boolean excluirAtivoID(int id) {
		return false;
	}

	@Override
	public boolean existeRegistroConfiguracaoId(Integer configuracaoId) {
		return false;
	}

	@Override
	public Informacoes pesquisarPorId(Integer id) {
		return null;
	}

	@Override
	public Collection<Informacoes> obterInformacoesAtivos(Integer id) {
		return null;
	}

	@Override
	public boolean excluirPorEstoqueID(Integer idEstoque) {
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluirPorEstoque");
			logger.info("excluirPorEstoque:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idEstoque);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return true;
	}

	@Override
	public List<Informacoes> obterInformacoesReferencia(Integer idReferencia) {
		return null;
	}

	@Override
	public Collection<Informacoes> obterInformacoesEstoque(Integer idEstoque) {
		Informacoes objInfos = null;
		Collection<Informacoes> lstInformacoes = new java.util.ArrayList<Informacoes>();
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesEstoque");
			logger.info("obterInformacoesEstoque:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idEstoque);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				objInfos = mapper(rs);
				lstInformacoes.add(objInfos);
			}
		} catch (SQLException e) {
			
			logger.error("Ocorreu um erro durante a execucação do método: InformacoesEstoquePostgresDAO.obterInformacoesEstoque", e);
			
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return lstInformacoes;
	}

	private Informacoes mapper(ResultSet rs) throws SQLException {
		Informacoes objInformacoes = new Informacoes();
		objInformacoes.setId(rs.getInt("informacoes_id"));
		objInformacoes.setDescricao(rs.getString("informacoes_descricao"));
		objInformacoes.setConfiguracoesId(rs.getInt("layout_configuracoes_id"));
		objInformacoes.setIdTipo(rs.getInt("estoque_id"));
		objInformacoes.setReferenciaId(rs.getInt("informacoes_id_referencia"));
		return objInformacoes;
	}

}

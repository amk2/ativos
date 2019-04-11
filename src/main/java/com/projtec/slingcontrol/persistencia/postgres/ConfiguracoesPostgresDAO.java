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
import com.projtec.slingcontrol.modelo.Configuracoes;
import com.projtec.slingcontrol.modelo.TipoCampo;
import com.projtec.slingcontrol.persistencia.ConfiguracoesDAO;

public class ConfiguracoesPostgresDAO implements ConfiguracoesDAO {

	private final Logger logger = LoggerFactory
			.getLogger(ConfiguracoesPostgresDAO.class);

	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource dtSource) throws IOException {
		dataSource = dtSource;
		querys = QueryUtilXML.obterMapSQL(ConfiguracoesPostgresDAO.class);

	}

	public Configuracoes incluir(Configuracoes objConfiguracoes) {
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);

			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParametrosStatment(objConfiguracoes, pst);

			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				objConfiguracoes.setId(id);

			} else {
				throw new IllegalStateException(
						"Erro ao inserir configuracoes :Nao tem chave?");
			}

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objConfiguracoes;
	}

	public Boolean excluirConfiguracoesLayout(Integer idLayout) {
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluirConfiguracoesLayout");
			logger.info("excluirConfiguracoesLayout:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idLayout);
			pst.executeUpdate();
			
			sql = querys.get("excluirConfiguracoesFilho");
			logger.info("excluirConfiguracoesFilho:" + sql);
			pst = con.prepareStatement(sql);
			pst.setInt(1, idLayout);
			pst.executeUpdate();
			

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return Boolean.TRUE;
	}

	@Override
	public Boolean alterar(Configuracoes objConfiguracoes) {
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParametrosStatment(objConfiguracoes, pst);
			pst.setInt(8, objConfiguracoes.getId());

			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro alterar  configuracoes:Muitos registros");

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}

	@Override
	public Collection<Configuracoes> pesquisar(Configuracoes paramConf) {

		Configuracoes conf = null;
		Collection<Configuracoes> listaConf = new java.util.ArrayList<Configuracoes>();
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("pesquisar");

			if (paramConf != null && paramConf.getNomecampo() != null
					&& !"".equals(paramConf.getNomecampo())) {
			//	if (paramConf.getIdLayout() == null) {
					sql = sql
							+ " AND  lower(layout_configuracoes_nome_campo) like '%"
							+ paramConf.getNomecampo().toLowerCase() + "%'";
				 
			}//}
			if (paramConf != null && paramConf.getIdLayout() != null) {
				sql = sql + " AND  layout_id=" + paramConf.getIdLayout();
			}

			if (paramConf != null && paramConf.getObrigatoriedade() != null) {
				
				//if (paramConf.getIdLayout() == null) {
					
					if  (paramConf.getObrigatoriedade().trim().length()>0 ){
											
					sql = sql + " AND layout_configuracoes_obrigatoriedade="
					+"'"+ (paramConf.getObrigatoriedade())+"'";
			//	} 
			}
			}

			if (paramConf != null && paramConf.getPesquisa() != null) {
				
		//		if (paramConf.getIdLayout() == null) {
					
				if  (paramConf.getPesquisa().trim().length()>0 ){
				
					sql = sql + " AND layout_configuracoes_pesquisa="
							+"'"+ (paramConf.getPesquisa())+"'";
			//	} 
			}
			}

			if (paramConf != null && paramConf.getTamanho() != null) {
				
					sql = sql + " AND  layout_configuracoes_tamanho_campo="
							+ paramConf.getTamanho();
				
			}

			if (paramConf != null && paramConf.getOrdem() != null) {
					sql = sql + " AND  layout_configuracoes_ordem="
							+ paramConf.getOrdem();
				
			}
			if (paramConf != null && paramConf.getTipoCampo() != null) {
				if (paramConf.getIdLayout() == null) {
					sql = sql + " AND  c.tipos_campo_id="
							+ paramConf.getTipoCampo().getId();
				}else{
					sql = sql + " AND  c.tipos_campo_id="
					+ paramConf.getTipoCampo().getId();
				}
			}
			sql = sql + " ORDER BY layout_configuracoes_ordem";

			logger.info("pesquisar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				conf = mapper(rs);

				listaConf.add(conf);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Erro ao Pesquisar Configuracoes \n", e);
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return listaConf;

	}

	@Override
	public Collection<Configuracoes> obterConfiguracoesLayout(Integer idLayout) {
		Configuracoes configuracoes = null;
		Collection<Configuracoes> lstConfiguracoes = new java.util.ArrayList<Configuracoes>();
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterConfiguracoesPorLayout");
			logger.info("obterConfiguracoesPorLayout:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idLayout);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {

				configuracoes = mapper(rs);

				lstConfiguracoes.add(configuracoes);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstConfiguracoes;
	}

	@Override
	public Collection<Configuracoes> obterConfiguracoesFilho(Integer idConfig) {
		Configuracoes configuracoes = null;
		Collection<Configuracoes> lstConfiguracoes = new java.util.ArrayList<Configuracoes>();
		Connection con = null;

		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterConfiguracoesFilho");
			logger.info("obterConfiguracoesFilho:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idConfig);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {

				configuracoes = mapper(rs);

				lstConfiguracoes.add(configuracoes);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstConfiguracoes;
	}

	private Configuracoes mapper(ResultSet rs) throws SQLException {
		Configuracoes objConfiguracoes;
		objConfiguracoes = new Configuracoes();
		objConfiguracoes.setId(rs.getInt("layout_configuracoes_id"));
		objConfiguracoes.setNomecampo(rs
				.getString("layout_configuracoes_nome_campo"));
		objConfiguracoes.setObrigatoriedade(rs
				.getString("layout_configuracoes_obrigatoriedade"));
		objConfiguracoes.setPesquisa(rs
				.getString("layout_configuracoes_pesquisa"));
		objConfiguracoes.setTamanho(rs
				.getInt("layout_configuracoes_tamanho_campo"));
		objConfiguracoes.setIdLayout(rs.getInt("layout_id"));
		objConfiguracoes.setOrdem(rs.getInt("layout_configuracoes_ordem"));

		TipoCampo objTipoCampo = new TipoCampo();
		objTipoCampo.setId(rs.getInt("tipos_campo_id"));
		objTipoCampo.setDescricao(rs.getString("tipos_campo_descricao"));
		objConfiguracoes.setTipoCampo(objTipoCampo);
		return objConfiguracoes;

	}

	private void setParametrosStatment(Configuracoes objConfiguracoes,
			PreparedStatement pst) throws SQLException {
		pst.setString(1, objConfiguracoes.getNomecampo());
		pst.setString(2, objConfiguracoes.getObrigatoriedade());
		pst.setInt(3, objConfiguracoes.getTamanho());
		if (objConfiguracoes.getIdLayout() == null) {
			pst.setNull(4, java.sql.Types.INTEGER);

		} else {
			pst.setInt(4, objConfiguracoes.getIdLayout());
		}

		pst.setInt(5, objConfiguracoes.getTipoCampo().getId());
		pst.setString(6, objConfiguracoes.getPesquisa());
		pst.setInt(7, objConfiguracoes.getOrdem());
		if (objConfiguracoes.getIdRef() == null) {
			pst.setNull(8, java.sql.Types.INTEGER);

		} else {
			pst.setInt(8, objConfiguracoes.getIdRef());
		}

	}

	@Override
	public Boolean excluir(Integer id) {
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
						"Erro ao remover Configura&ccedil;&otilde;es:Muitos registros");

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return Boolean.TRUE;
	}

	@Override
	public Configuracoes pesquisarId(Integer id) {
		Configuracoes objConfig = null;
		Connection con = null;

		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterPorID");
			logger.info("obterPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				objConfig = mapper(rs);

			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objConfig;
	}

	@Override
	public Boolean excluirConfiguracoesFilho(Integer id) {

		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluirConfiguracoesFilho");
			logger.info("excluirConfiguracoesFilho:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		


		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return Boolean.TRUE;

	}
	
	public Boolean existeRegistroIdLayout(Integer id) {

		Connection con = null;
		
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("existeConfiguracoesLayoutId");
			logger.info("existeConfiguracoesLayoutId:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) 
			{
				DataSourceUtils.releaseConnection(con, dataSource);
				//existe registro com esse layout_id
				return true ;
			} else {
				DataSourceUtils.releaseConnection(con, dataSource);
				//nao existe registro com esse layout_id
				return false ;
			}

		} catch (SQLException e) {
			throw new IllegalStateException(
					"Erro ao Verificar se ja existe alguma configuracao_layout associada a um layout:" + id + "\n",
					e);

		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		

	}
	

}

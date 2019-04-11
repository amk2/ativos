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
import com.projtec.slingcontrol.persistencia.InformacoesDemandaDAO;

public class InformacoesDemandaPostgresDAO implements InformacoesDemandaDAO
{

	private final Logger logger = LoggerFactory
			.getLogger(InformacoesDemandaPostgresDAO.class);
	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource DataSourceUtils) throws IOException
	{
		dataSource = DataSourceUtils;
		querys = QueryUtilXML.obterMapSQL(InformacoesDemandaPostgresDAO.class);

	}

	public Informacoes incluir(Informacoes objInformacoesDemanda)
	{
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParametrosStatment(objInformacoesDemanda, pst);

			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt(1);
				objInformacoesDemanda.setId(id);

			} else
			{
				throw new IllegalStateException(
						"Erro ao inserir Informacoes Demanda :Nao tem chave?");
			}

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objInformacoesDemanda;
	}

	public boolean excluir(int id)
	{
		Connection con = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluir");
			logger.info("excluir:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro remover Informacoes Demanda:Muitos registros");

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return false;
	}

	public boolean excluirDemandaID(int id)
	{
		Connection con = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluirDemandaId");
			logger.info("excluirDemandaId:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			pst.executeUpdate();

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		


		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return false;
	}

	@Override
	public boolean alterar(Informacoes objInformacoesDemanda)
	{
		Connection con = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParametrosStatment(objInformacoesDemanda, pst);

			pst.setInt(4, objInformacoesDemanda.getId());

			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro alterar Informacoes Demanda :Muitos registros");

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}

	@Override
	public Informacoes pesquisarPorId(Integer id)
	{
		Connection con = null;
		Informacoes objInformacoesDemanda = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesDemandaPorID");
			logger.info("obterInformacoesDemandaPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				objInformacoesDemanda = mapper(rs);

			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objInformacoesDemanda;
	}
	
	
	@Override
	public boolean existeRegistroLayoutId(Integer id)
	{
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("existeConfiguracoesAssociada");
			logger.info("existeConfiguracoesAssociada:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
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
	public Collection<Informacoes> obterInformacoesDemanda(
			Integer idDemanda)
	{
		Informacoes objInformacoesDemanda = null;
		Collection<Informacoes> lstInformacoes = new java.util.ArrayList<Informacoes>();
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesDemanda");
			logger.info("obterInformacoesDemanda:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idDemanda);

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				objInformacoesDemanda = mapper(rs);

				lstInformacoes.add(objInformacoesDemanda);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstInformacoes;
	}

	private void setParametrosStatment(
			Informacoes objInformacoesDemanda, PreparedStatement pst)
			throws SQLException
	{
		pst.setString(1, objInformacoesDemanda.getDescricao());
		pst.setInt(2, objInformacoesDemanda.getConfiguracoesId());
		pst.setInt(3, objInformacoesDemanda.getIdTipo());
		/*
		if (objInformacoesDemanda.getIdReferencia()==null)
		{
			pst.setNull(4,java.sql.Types.INTEGER );
			
		}else
		{
			pst.setInt(4, objInformacoesDemanda.getIdReferencia());
		}
		*/

	}

	private Informacoes mapper(ResultSet rs) throws SQLException
	{

		Informacoes objInformacoesDemanda = new Informacoes();
		objInformacoesDemanda.setId(rs.getInt("informacoes_id"));
		objInformacoesDemanda.setDescricao(rs
				.getString("informacoes_descricao"));
		objInformacoesDemanda.setConfiguracoesId(rs.getInt("layout_configuracoes_id"));
		objInformacoesDemanda.setIdTipo(rs.getInt("demanda_id"));

		return objInformacoesDemanda;
	}

	@Override
	public List<Informacoes> obterInformacoesReferencia(Integer idRef)
	{
		Informacoes objInformacoesDemanda = null;
		List<Informacoes> lstInformacoes = new java.util.ArrayList<Informacoes>();
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesDemandaReferencia");
			logger.info("obterInformacoesDemandaReferencia:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idRef);

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				objInformacoesDemanda = mapper(rs);

				lstInformacoes.add(objInformacoesDemanda);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstInformacoes;
	}

}

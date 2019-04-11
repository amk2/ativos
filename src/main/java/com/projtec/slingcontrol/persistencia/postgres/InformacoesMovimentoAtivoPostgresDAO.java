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
import com.projtec.slingcontrol.persistencia.InformacoesMovimentoAtivoDAO;

public class InformacoesMovimentoAtivoPostgresDAO implements
		InformacoesMovimentoAtivoDAO
{

	private final Logger logger = LoggerFactory.getLogger(InformacoesMovimentoAtivoPostgresDAO.class);
	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource DataSourceUtils) throws IOException
	{
		dataSource = DataSourceUtils;
		querys = QueryUtilXML
				.obterMapSQL(InformacoesMovimentoAtivoPostgresDAO.class);

	}

	public Informacoes incluir(
			Informacoes objInformacoesMovimentoAtivo)
	{
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParametrosStatment(objInformacoesMovimentoAtivo, pst);

			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt(1);
				objInformacoesMovimentoAtivo.setId(id);

			} else
			{
				throw new IllegalStateException(
						"Erro ao inserir  Informacoes Movimento Ativo :Nao tem chave?");
			}

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objInformacoesMovimentoAtivo;
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
						"Erro remover  Informacoes Movimento Ativo  :Muitos registros");

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		


		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return false;
	}

	public boolean excluirAtivoID(int id)
	{
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluirMovimentoAtivoId");
			logger.info("excluirMovimentoAtivoId:" + sql);
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
	public boolean alterar(
			Informacoes objInformacoesMovimentoAtivo)
	{
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParametrosStatment(objInformacoesMovimentoAtivo, pst);

			pst.setInt(4, objInformacoesMovimentoAtivo.getId());

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
		Informacoes objInformacoesMovimentoAtivo = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesMovimentoAtivoPorID");
			logger.info("obterInformacoesMovimentoAtivoPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				objInformacoesMovimentoAtivo = mapper(rs);

			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objInformacoesMovimentoAtivo;
	}

	@Override
	public Collection<Informacoes> obterInformacoesMovimentoAtivo(
			Integer idMovimentoAtivo)
	{
		Informacoes objInformacoesMovimentoAtivo = null;
		Collection<Informacoes> lstInformacoesMovimentoAtivo = new java.util.ArrayList<Informacoes>();
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesMovimentoAtivo");
			logger.info("obterInformacoesMovimentoAtivo:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idMovimentoAtivo);

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				objInformacoesMovimentoAtivo = mapper(rs);

				lstInformacoesMovimentoAtivo.add(objInformacoesMovimentoAtivo);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstInformacoesMovimentoAtivo;
	}

	
	@Override
	public List<Informacoes> obterInformacoesReferencia(Integer id)
	{
		Informacoes objInformacoesMovimentoAtivo = null;
		List<Informacoes> lstInformacoesMovimentoAtivo = new java.util.ArrayList<Informacoes>();
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterInformacoesMovimentoAtivoRef");
			logger.info("obterInformacoesMovimentoAtivoRef:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				objInformacoesMovimentoAtivo = mapper(rs);

				lstInformacoesMovimentoAtivo.add(objInformacoesMovimentoAtivo);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstInformacoesMovimentoAtivo;
	}
	private void setParametrosStatment(
			Informacoes objInformacoesMovimentoAtivo,
			PreparedStatement pst) throws SQLException
	{
		pst.setString(1, objInformacoesMovimentoAtivo.getDescricao());
		pst.setInt(2, objInformacoesMovimentoAtivo.getConfiguracoesId());
		pst.setInt(3, objInformacoesMovimentoAtivo.getIdTipo());
		
		
		if (objInformacoesMovimentoAtivo.getReferenciaId()==null)
		{
			pst.setNull(4,java.sql.Types.INTEGER );
			
		}else
		{
			pst.setInt(4, objInformacoesMovimentoAtivo.getReferenciaId());
		}
		
	}

	private Informacoes mapper(ResultSet rs) throws SQLException
	{

		Informacoes objInformacoesMovimentoAtivo = new Informacoes();
		
		objInformacoesMovimentoAtivo.setId(rs.getInt("informacoes_id"));
		objInformacoesMovimentoAtivo.setDescricao(rs.getString("informacoes_descricao"));
		objInformacoesMovimentoAtivo.setConfiguracoesId(rs.getInt("layout_configuracoes_id"));
		objInformacoesMovimentoAtivo.setIdTipo(rs.getInt("movimento_ativo_id"));
		
		objInformacoesMovimentoAtivo.setReferenciaId(rs.getInt("informacoes_id_referencia"));

		return objInformacoesMovimentoAtivo;
	}

	

}

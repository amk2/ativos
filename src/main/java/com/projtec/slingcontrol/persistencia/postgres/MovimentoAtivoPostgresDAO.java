package com.projtec.slingcontrol.persistencia.postgres;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.MovimentoAtivo;
import com.projtec.slingcontrol.modelo.TipoMovimentoAtivo;
import com.projtec.slingcontrol.persistencia.MovimentoAtivoDAO;

public class MovimentoAtivoPostgresDAO implements MovimentoAtivoDAO
{

	private final Logger logger = LoggerFactory.getLogger(MovimentoAtivoPostgresDAO.class);
	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource dtSource) throws IOException
	{
		dataSource = dtSource;
		querys = QueryUtilXML.obterMapSQL(MovimentoAtivoPostgresDAO.class);
	}

	public MovimentoAtivo incluir(MovimentoAtivo objMovimentoAtivo)
	{
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, objMovimentoAtivo.getLayoutId().getId());
			pst.setInt(2, objMovimentoAtivo.getDemandaId().getId());
			pst.setInt(3, objMovimentoAtivo.getTipomovimento().getId());
			// setParametrosStatment(objMovimentoAtivo,pst);

			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt(1);
				objMovimentoAtivo.setId(id);

			} else
			{
				throw new IllegalStateException(
						"Erro ao inserir Movimento Ativo :Nao tem chave?");
			}

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objMovimentoAtivo;
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
						"Erro remover movimeto ativo:Muitos registros");

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
	public boolean alterar(MovimentoAtivo objMovimentoAtivo)
	{
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, objMovimentoAtivo.getLayoutId().getId());
			pst.setInt(2, objMovimentoAtivo.getDemandaId().getId());
			pst.setInt(3, objMovimentoAtivo.getTipomovimento().getId());
			pst.setInt(4, objMovimentoAtivo.getId());
			

			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro alterar Ativos :Muitos registros");

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
	public List<MovimentoAtivo> pesquisar(MovimentoAtivo movimentoAtivo)
	{
		MovimentoAtivo objMovimentoAtivo = null;
		List<MovimentoAtivo> lstMovimentoAtivo = new java.util.ArrayList<MovimentoAtivo>();
	
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = " " + 
				 " SELECT  " + 
				 " DISTINCT movimento_ativo_id, " + 
                 " m.layout_id," +
                 " demanda_id," +
                 " tipo_movimento_ativo_id" +
                 " FROM  movimento_ativo m " +
                 " left join   layout_configuracoes conf on (m.layout_id =  conf.layout_id) " + 
			     " where 1=1 " ; 
			
			if (movimentoAtivo.getDemandaId() != null
					&& movimentoAtivo.getDemandaId().getId() != 0)
			{
				sql = sql + " AND  m.demanda_id =  "
						+ movimentoAtivo.getDemandaId().getId();
			}

			if (movimentoAtivo.getLayoutId() != null && movimentoAtivo.getLayoutId().getId() != 0)
			{
				sql = sql + " AND  m.layout_id="	+ movimentoAtivo.getLayoutId().getId();
			}
			
			
			if ( movimentoAtivo.getTipomovimento() != null &&  movimentoAtivo.getTipomovimento().getId() != 0)
			{
				sql = sql + "AND  m.tipo_movimento_ativo_id="	+ movimentoAtivo.getTipomovimento().getId();
			}

			
			if (movimentoAtivo.getInformacoesMovimentoAtivo() != null)
			{
				Collection<Informacoes> lstInfo = movimentoAtivo.getInformacoesMovimentoAtivo();
					
				Informacoes info;
				Boolean maior = movimentoAtivo.getInformacoesMovimentoAtivo().size() > 0;
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
								+ " from layout_configuracoes config inner join informacoes_movimento_ativo info "
								+ " on (config.layout_configuracoes_id = info.layout_configuracoes_id and  m.movimento_ativo_id=info.movimento_ativo_id ) "
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

			logger.info("pesquisar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				objMovimentoAtivo = mapper(rs);
				lstMovimentoAtivo.add(objMovimentoAtivo);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		

		return lstMovimentoAtivo;
	}

	@Override
	public MovimentoAtivo pesquisarPorId(Integer id)
	{
		Connection con = null;
		MovimentoAtivo objMovimentoAtivo = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterMovimentoAtivoPorID");
			logger.info("obterMovimentoAtivoPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				objMovimentoAtivo = mapper(rs);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objMovimentoAtivo;
	}

	@Override
	public List<MovimentoAtivo> obterTodos()
	{
		MovimentoAtivo objMovimentoAtivo = null;
		List<MovimentoAtivo> lstMovimentoAtivo = new java.util.ArrayList<MovimentoAtivo>();
		Connection con = null;

		try
		{

			con = dataSource.getConnection();
			String sql = querys.get("obterTodos");
			logger.info("obterTodos:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				objMovimentoAtivo = mapper(rs);

				lstMovimentoAtivo.add(objMovimentoAtivo);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstMovimentoAtivo;
	}


	private MovimentoAtivo mapper(ResultSet rs) throws SQLException
	{
		MovimentoAtivo objMovimentoAtivo = new MovimentoAtivo();
		objMovimentoAtivo.setId(rs.getInt("movimento_ativo_id"));
		Layout objLayout = new Layout();
		objLayout.setId(rs.getInt("layout_id"));
		objMovimentoAtivo.setLayoutId(objLayout);
		Demandas objDemanda = new Demandas();
		objDemanda.setId(rs.getInt("demanda_id"));
		objMovimentoAtivo.setDemandaId(objDemanda);
		TipoMovimentoAtivo objTipoMovimento = new TipoMovimentoAtivo();
		objTipoMovimento.setId(rs.getInt("tipo_movimento_ativo_id"));
		objMovimentoAtivo.setTipomovimento(objTipoMovimento);

		return objMovimentoAtivo;
	}

}

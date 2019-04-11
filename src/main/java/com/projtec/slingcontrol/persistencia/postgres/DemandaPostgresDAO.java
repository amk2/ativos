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
import com.projtec.slingcontrol.modelo.StatusDemanda;
import com.projtec.slingcontrol.modelo.TipoDemanda;
import com.projtec.slingcontrol.persistencia.DemandaDAO;

public class DemandaPostgresDAO implements DemandaDAO {
	private final Logger logger = LoggerFactory
			.getLogger(DemandaPostgresDAO.class);
	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource DataSourceUtils) throws IOException {
		dataSource = DataSourceUtils;
		querys = QueryUtilXML.obterMapSQL(DemandaPostgresDAO.class);

	}

	public Demandas incluir(Demandas objDemanda) {
		Connection con = null;
		try {
			
			con = DataSourceUtils.getConnection(dataSource);
			ResultSet rs;

			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParametrosStatment(objDemanda, pst);

			pst.execute();
			sql = querys.get("idAtual");
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				objDemanda.setId(id);

			} else {
				throw new IllegalStateException(
						"Erro ao inserir Demanda :Nao tem chave?");
			}
			

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objDemanda;
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
						"Erro remover Demanda:Muitos registros");

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}

	@Override
	public boolean alterar(Demandas objDemanda) {
		Connection con = null;
		try {
			//String statusDemanda = objDemanda.getStatus();
			con = DataSourceUtils.getConnection(dataSource);


			ResultSet rs;
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParametrosStatment(objDemanda, pst);
			pst.setInt(5, objDemanda.getId());
			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro alterar Demanda :Muitos registros");

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}

	@Override
	public List<Demandas> pesquisar(Demandas demanda) {

		List<Demandas> lstDemandas = new java.util.ArrayList<Demandas>();
		Demandas objDemanda = null;
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = " "
					+ " SELECT     "
					+ "  DISTINCT demanda_id, "
					+ " d.layout_id,  "
					+ " tipo_demanda_id,  "
					+ " demanda_id_referencia, "
					+ " demanda_dt_criacao,"
					+ " sd.status_demanda_descricao,"
					+ " sd.status_demanda_id"
					+ " FROM demandas  d, status_demanda sd "
					+ "  left join   layout_configuracoes conf on (layout_id =  conf.layout_id) "
					+ " where sd.status_demanda_id = d.status_demanda_id";
			if (demanda.getTipoDemanda() != null
					&& demanda.getTipoDemanda().getId() != 0) {
				sql = sql + " AND  d.tipo_demanda_id =  "
						+ demanda.getTipoDemanda().getId();
			}

			if (demanda.getLayout() != null && demanda.getLayout().getId() != 0) {
				sql = sql + " AND  d.layout_id=" + demanda.getLayout().getId();
			}
			
			
			if (demanda.getStatusDemanda() != null && demanda.getStatusDemanda().getId() != 0) {
				sql = sql + " AND  d.status_demanda_id=" + demanda.getStatusDemanda().getId();
			}

			if (demanda.getIdPai() != null && demanda.getIdPai() != 0) {
				sql = sql + " AND  d.demanda_id_referencia="
						+ demanda.getIdPai();
			}

			// if (demanda.getStatus() != null )
			// {
			// sql = sql + " AND  d.demanda_status='" + demanda.getStatus() +
			// "'" ;
			// }

			if (demanda.getInformacoesDemanda() != null) {
				Collection<Informacoes> lstInfo = demanda
						.getInformacoesDemanda();
				Informacoes info;
				Boolean maior = demanda.getInformacoesDemanda().size() > 0;
				if (maior) {
					sql = sql + " AND (";
				}

				for (Iterator<Informacoes> iterator = lstInfo.iterator(); iterator
						.hasNext();) {
					info = iterator.next();
					info.getDescricao();
					if (!"".equals(info.getDescricao())) {

						sql = sql
								+ ""
								+ " conf.layout_configuracoes_id in (  select    "
								+ "       config.layout_configuracoes_id        "
								+ " from layout_configuracoes config inner join informacoes_demanda info "
								+ " on (config.layout_configuracoes_id = info.layout_configuracoes_id and  d.demanda_id=info.demanda_id ) "
								+ " where lower(info.informacoes_descricao) like '%"
								+ info.getDescricao() + "%' )";

						if (iterator.hasNext()) {
							sql = sql + " or ";
						}
					}

				}

				if (maior) {
					sql = sql + " )";
				}

			}

			logger.info("pesquisar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				objDemanda = mapper(rs);
				lstDemandas.add(objDemanda);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstDemandas;
	}

	@Override
	public Collection<Demandas> obterTodos() 
	{
		Demandas objDemandas = null;
		Collection<Demandas> lstDemandas = new java.util.ArrayList<Demandas>();
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterTodos");
			logger.info("obterTodos:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				objDemandas = mapper(rs);

				lstDemandas.add(objDemandas);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstDemandas;
	}
	
	
	@Override
	public void alterarStatus(Integer idDemanda, Integer idStatus) 
	{
		Connection con = null;
		try 
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = "";			
			PreparedStatement pst;
	
			con = DataSourceUtils.getConnection(dataSource);
			sql = querys.get("alterarStatusDemanda");
			logger.info("alterarStatusDemanda:" + sql);
			pst = con.prepareStatement(sql);
			
		
			pst.setInt(1, idStatus);
			pst.setInt(2, idDemanda);
			
			int qtd = pst.executeUpdate();

			if (qtd > 1)
				throw new IllegalStateException(
						"Erro alterar Demanda :Muitos registros");

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
	}

	
	
	@Override
	public List<Demandas> pesquisarDemandasMovimento(Integer origem,
			Integer destino, Integer status, Integer tpDemanda) {
		
		Demandas objDemandas = null;
		List<Demandas> lstDemandas = new java.util.ArrayList<Demandas>();
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("pesquisarDemandasMovimento");
			
			if(origem!=null && origem > 0 )
			{
				sql = sql +  " and demItem.locais_origem=" + origem ;  
			}
			
			if(destino!=null && destino > 0 )
			{
				sql = sql +  "and demItem.locais_destino=" + destino ;  
			}
			
			
			if(status!=null && status > 0 )
			{
				sql = sql +  " and dem.status_demanda_id=" + status ;  
			}
			
			
			if(tpDemanda!=null && tpDemanda > 0 )
			{
				sql = sql +  " and dem.tipo_demanda_id=" + tpDemanda ;  
			}
			
			
			logger.info("pesquisarDemandasMovimento:" + sql);
			
			PreparedStatement pst = con.prepareStatement(sql);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				objDemandas = mapper(rs);

				lstDemandas.add(objDemandas);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstDemandas;
	}

	private Demandas mapper(ResultSet rs) throws SQLException {
		Demandas objDemanda;
		objDemanda = new Demandas();
		objDemanda.setId(rs.getInt("demanda_id"));
		Layout objLayout = new Layout();
		objLayout.setId(rs.getInt("layout_id"));
		objDemanda.setLayout(objLayout);
		TipoDemanda objTipoDemanda = new TipoDemanda();
		objTipoDemanda.setId(rs.getInt("tipo_demanda_id"));
		objDemanda.setTipoDemanda(objTipoDemanda);
		objDemanda.setIdPai(rs.getInt("demanda_id_referencia"));
		StatusDemanda stsDemanda = new StatusDemanda();
		stsDemanda.setId(rs.getInt("status_demanda_id"));
		stsDemanda.setDescricao(rs.getString("status_demanda_descricao"));		
		objDemanda.setStatusDemanda(stsDemanda);
		objDemanda.setDataCadastro(rs.getDate("demanda_dt_criacao"));
		return objDemanda;
	}

	private void setParametrosStatment(Demandas objDemanda,
			PreparedStatement pst) throws SQLException {
		
		/*
		 * demanda_id_referencia, layout_id, tipo_demanda_id,status_demanda_id
		 */
		
        int idx=1; 
        
		if (objDemanda.getIdPai() == null) {
			pst.setNull(idx++, java.sql.Types.INTEGER);

		} else {
			pst.setInt(idx++, objDemanda.getIdPai());
		}
		
		pst.setInt(idx++, objDemanda.getLayout().getId());
		pst.setInt(idx++, objDemanda.getTipoDemanda().getId());
		
		if (objDemanda.getStatusDemanda() == null) {
			pst.setNull(idx++, java.sql.Types.INTEGER);
		} 
		else {
			pst.setInt(idx++, objDemanda.getStatusDemanda().getId());
		}
		
	}

	@Override
	public Demandas pesquisarPorId(Integer id) 
	{
		Demandas objDemanda = null;
		Connection con = null;

		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterDemandaPorID");
			
			logger.info("obterDemandaPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				objDemanda = mapper(rs);
			}
			
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objDemanda;
	}

	@Override
	public boolean verificaItensLocalOrigem(Integer idDemanda) 
	{
		boolean todosNosLocaisOrigem =  true; 
		Connection con = null;

		try {
			con = DataSourceUtils.getConnection(dataSource);
			//consulta verifica se todos os itens est�o no mesmo lugar de origem item e o localde origem demanda.
			String sql = querys.get("verificarItensLocalOrigem");
			
			logger.info("verificarItensLocalOrigem:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, idDemanda);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				todosNosLocaisOrigem =false;
			}
			
			
		} catch (SQLException e) 
		{
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		
		return todosNosLocaisOrigem;
	}


	

}

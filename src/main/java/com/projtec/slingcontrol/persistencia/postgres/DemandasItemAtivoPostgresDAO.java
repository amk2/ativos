package com.projtec.slingcontrol.persistencia.postgres;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.projtec.slingcontrol.infra.QueryUtilXML;
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.Demandas;
import com.projtec.slingcontrol.modelo.DemandasItemAtivo;
import com.projtec.slingcontrol.modelo.ItensAtivos;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.modelo.Local;
import com.projtec.slingcontrol.persistencia.DemandasItemAtivoDAO;

public class DemandasItemAtivoPostgresDAO implements DemandasItemAtivoDAO
{
	
	private final Logger logger = LoggerFactory.getLogger(DemandasItemAtivoPostgresDAO.class);

	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource DataSourceUtils) throws IOException
	{
		dataSource = DataSourceUtils;
		querys = QueryUtilXML.obterMapSQL(DemandasItemAtivoPostgresDAO.class);
	}

	@Override
	public List<DemandasItemAtivo> obterItensAtivoDemanda(Integer idDemanda) 
	{
		DemandasItemAtivo demandasItemAtivo = null;
		List<DemandasItemAtivo> lstDemandasItemAtivos = new java.util.ArrayList<DemandasItemAtivo>();
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterItensAtivoDemanda");
			logger.info("obterItensAtivoDemanda:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idDemanda);
			
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				demandasItemAtivo = mapper(rs);

				lstDemandasItemAtivos.add(demandasItemAtivo);
			}
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstDemandasItemAtivos;
	}
	
	


	private DemandasItemAtivo mapper(ResultSet rs) throws SQLException 
	{
		DemandasItemAtivo demandaItemAtivo;
		/**
		 * demItem.demanda_id, atv.ativos_id, la.layout_nome,la.layout_id,
	       demItem.itens_ativo_id,itens.itens_ativo_identificacao,
	       demItem.locais_destino, locDestino.locais_nome, 
	       locais_origem,locOrigem.locais_nome
		 */
		
		demandaItemAtivo = new DemandasItemAtivo();
		
		demandaItemAtivo.setIdDemanda(rs.getInt("demanda_id"));
		ItensAtivos itemAtivos = new ItensAtivos();
		Ativos atv = new Ativos();
		atv.setId(rs.getInt("ativos_id"));
		Layout lay = new Layout();
		lay.setId(rs.getInt("layout_id"));
		lay.setNome(rs.getString("layout_nome"));
		atv.setLayout(lay);
		itemAtivos.setAtivo(atv);
		itemAtivos.setItensAtivosId(rs.getInt("itens_ativo_id"));
		itemAtivos.setIdentificacao(rs.getString("itens_ativo_identificacao"));		
		Local local = new Local();
		local.setId(rs.getInt("localAtual"));
		local.setNome(rs.getString("localAtualNome"));
		itemAtivos.setLocal(local);
		demandaItemAtivo.setItemAtivos(itemAtivos);
		
		Local localOrigem = new Local();
		localOrigem.setId(rs.getInt("locais_origem"));
		localOrigem.setNome(rs.getString("origNome"));
		demandaItemAtivo.setLocalOrigem(localOrigem);
		Local localDestino = new Local();
		localDestino.setId(rs.getInt("locais_destino"));
		localDestino.setNome(rs.getString("destNome"));
		demandaItemAtivo.setLocalDestino(localDestino);
		
		return demandaItemAtivo;
	}

	@Override
	public void incluir(DemandasItemAtivo demandasItemAtv) {
		
		Connection con = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("adicionar");
			logger.info("adicionar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			  /* demanda_id, itens_ativo_id, locais_destino, locais_origem)*/

			pst.setInt(1, demandasItemAtv.getIdDemanda());			
			pst.setInt(2, demandasItemAtv.getItemAtivos().getItensAtivosId());		
			
			
			if (demandasItemAtv.getLocalDestino()!=null  && demandasItemAtv.getLocalDestino().getId()!=null )
			{
				pst.setInt(3, demandasItemAtv.getLocalDestino().getId());
				
			}else
			{
				pst.setNull(3, java.sql.Types.INTEGER);
			}
			
			
			if(demandasItemAtv.getLocalOrigem()!=null && demandasItemAtv.getLocalOrigem().getId()!=null)
			{
				pst.setInt(4, demandasItemAtv.getLocalOrigem().getId());	
			}else
			{
				
				pst.setNull(4, java.sql.Types.INTEGER);

			}
				
		
			pst.execute();
			

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		
		
	}

	@Override
	public void excluirItensDemanda(Integer demandasItemAtvID) {
		
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluirPorDemanda");
			logger.info("excluirPorDemanda:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, demandasItemAtvID);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
	}

	@Override
	public void excluirItemTroca(Integer idDemanda, Integer idItemAnterior) 
	{
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluirItemTroca");
			logger.info("excluirItemTroca:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idDemanda);
			pst.setInt(2, idItemAnterior);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		

		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
	}

	@Override
	public void incluirItensDemandaTroca(Integer idDemanda,	Integer idItemAtual, Integer idItemAnterior , String observacao)
    {
		Connection con = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("incluirItensDemandaTroca");
			logger.info("incluirItensDemandaTroca:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setInt(1, idDemanda);			
			pst.setInt(2, idItemAtual);		
			pst.setInt(3, idItemAnterior);
			pst.setString(4, observacao);
			
			pst.execute();
			

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		
	}

	@Override
	public Boolean existeItemDemanda(Integer idDemanda, Integer idItem) 
	{
		Boolean existe = false;
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("existeItemDemanda");
			logger.info("existeItemDemanda:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, idDemanda);
			pst.setInt(2, idItem);
			
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				existe = true;
			}else
			{
				existe= false;
			}
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return existe;
	}

	@Override
	public void incluirDemandaItemAtivo(Integer id, Integer itensAtivosId) {

		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("incluirDemandaItemAtivo");
			logger.info("incluirDemandaItemAtivo:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setInt(1, id);			
			pst.setInt(2, itensAtivosId);		
			
			pst.execute();
			

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}
		
		
		
		
	}

	@Override
	public Boolean pesquisarPossivelFinalizacao(Integer id) {

		Boolean existe = false;
		Connection con = null;

		try {

			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("verificarSePodeFinalizar");
			logger.info("verificarSePodeFinalizar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				existe = true;
			}else
			{
				existe= false;
			}
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());		
		} finally {

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return existe;

	
	}

}

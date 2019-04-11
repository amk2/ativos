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
import com.projtec.slingcontrol.modelo.Ativos;
import com.projtec.slingcontrol.modelo.AtivosNo;
import com.projtec.slingcontrol.modelo.Informacoes;
import com.projtec.slingcontrol.modelo.Layout;
import com.projtec.slingcontrol.persistencia.AtivosDAO;

public class AtivosPostgresDAO extends BasePostgresDAO implements AtivosDAO
{

	private final Logger logger = LoggerFactory.getLogger(AtivosPostgresDAO.class);

	private DataSource dataSource;
	private Map<String, String> querys;

	@Autowired
	public void init(DataSource DataSourceUtils) throws IOException
	{
		dataSource = DataSourceUtils;
		querys = QueryUtilXML.obterMapSQL(AtivosPostgresDAO.class);
	}

	private void setParametrosStatment(Ativos objAtivos, PreparedStatement pst)
			throws SQLException
	{
		pst.setInt(1, objAtivos.getLayout().getId());
	}

	public Ativos incluir(Ativos objAtivos)
	{
		Connection con = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			
			String sql = querys.get("adicionar");
			String sqlId = querys.get("idAtual");
			logger.info("adicionar:" + sql);

						
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, objAtivos.getLayout().getId());
			pst.execute();
			
			pst = con.prepareStatement(sqlId);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				int id = rs.getInt(1);
				objAtivos.setId(id);

			} else
			{
				throw new IllegalStateException(
						"Erro ao inserir Ativos :N&atilde;o tem chave?");
			}

		} catch (SQLException e)
		{
				throw new IllegalStateException(e.getMessage());		

		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objAtivos;
	}
	
	public boolean incluirDep(Integer idAtivo, Collection<Integer>  lstDependencias)
	{
		Connection con = null;

		try
		{

			Integer aux ;
			con = DataSourceUtils.getConnection(dataSource);
			
			
			String sqlDep = querys.get("adicionarDependencia");
			logger.info("adicionarDependencias:" + sqlDep);
						
			PreparedStatement pst = con.prepareStatement(sqlDep);
		
			for (Iterator<Integer> iterator = lstDependencias.iterator(); iterator.hasNext();) 
			{
				aux = iterator.next();
				pst.setInt(1, idAtivo);
				pst.setInt(2, aux);
				pst.execute();
			}

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return true ;
		
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
						"Erro remover Ativos:Muitos registros");

		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		

		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return true;
	}
	
	public boolean excluirDep(int id)
	{
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("excluirDependencias");
			logger.info("excluirDependencias:" + sql);
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

		return true;
	}

	@Override
	public boolean alterar(Ativos objAtivos)
	{
		Connection con = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("alterar");
			logger.info("alterar:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			setParametrosStatment(objAtivos, pst);
			pst.setInt(2, objAtivos.getId());
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
	public List<Ativos> pesquisar(Ativos ativos)
	{
		List<Ativos> ativosList = new java.util.ArrayList<Ativos>();
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			
			StringBuffer sql = new StringBuffer();
			
			sql.append(" select DISTINCT atv.ativos_id, lay.layout_id, lay.layout_descricao ");                                                                                     
			sql.append("   from ativos atv ");  
			sql.append("   join layouts lay on atv.layout_id = lay.layout_id ");
			sql.append("  where 1 = 1 ");

			if (ativos.getLayout() != null && ativos.getLayout().getId() != 0) {
				sql.append(" and  atv.layout_id = " + ativos.getLayout().getId());
			}

			if (ativos.getInformacoes() != null) {
				
				List<Informacoes> informacoesList = new ArrayList<Informacoes>(ativos.getInformacoes());
				String sqlComplementar = getComplementoQuerieParaConsultaComCamposDinamicos(
						"atv", "ativos_id", "informacoes_ativo", informacoesList);
				sql.append(sqlComplementar);
				 
			}

			logger.info("pesquisar:" + sql.toString());
			
			PreparedStatement pst = con.prepareStatement(sql.toString());

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				
				Ativos objAtivos = mapper(rs);
				ativosList.add(objAtivos);
				
			}
			
		} 
		catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} 
		finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return ativosList;
		
	}

	
	
		
	
	@Override
	public Ativos pesquisarPorId(Integer id)
	{

		Ativos objAtivos = null;
		Connection con = null;

		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterAtivosPorID");
			logger.info("obterAtivosPorID:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			pst.setInt(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				objAtivos = mapper(rs);

			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return objAtivos;
	}
	
	@Override
	public List<Integer> pesquisarPaiPorIdFilho(Integer id)
	{
		List<Integer> dep = new ArrayList<Integer>()  ;
		Connection con = null;
		try
		{
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterPaiPorIdFilho");
			logger.info("obterPaiPorIdFilho" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			while (rs.next())
			{
				dep.add(rs.getInt("ativos_id_pai"));
			}
				
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
			return dep ;
		}

	}
	
	
	@Override
	public List<Ativos> pesquisar(String criterio)
	{
		
		Ativos objAtivos = null;
		List<Ativos> lstAtivos = new java.util.ArrayList<Ativos>();
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			
			String sql = querys.get("pesquisarCriterio");
			

			logger.info("pesquisarCriterio:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, "%" + criterio + "%"  );
			pst.setString(2, "%" + criterio + "%"  );
			pst.setString(3, "%" + criterio + "%"  );
			

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				objAtivos = mapper(rs);

				lstAtivos.add(objAtivos);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstAtivos;
	}

	

	@Override
	public List<Ativos> obterTodos()
	{

		Ativos objAtivos = null;
		List<Ativos> lstAtivos = new java.util.ArrayList<Ativos>();
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
				objAtivos = mapper(rs);

				lstAtivos.add(objAtivos);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstAtivos;
	}

	private Ativos mapper(ResultSet rs) throws SQLException
	{
		Ativos objAtivos;
		objAtivos = new Ativos();
		objAtivos.setId(rs.getInt("ativos_id"));
		Layout objLayout = new Layout();
		objLayout.setId(rs.getInt("layout_id"));		
		objAtivos.setLayout(objLayout);
		return objAtivos;
	}
	
	private AtivosNo mapperAtivoNo(ResultSet rs) throws SQLException
	{
		AtivosNo objAtivoNo = new AtivosNo();
		objAtivoNo.setFilho(rs.getInt("ativos_id_filho"));
		objAtivoNo.setPai(rs.getInt("ativos_id_pai"));		
		return objAtivoNo;
	}

	@Override
	public List<AtivosNo> pesquisarFilhoPorIdPai(Integer id, List<AtivosNo> lstPaiFilho) {
		AtivosNo objAtivoNo = null ;
		List<AtivosNo> lstAux = new ArrayList<AtivosNo>();
		
		Connection con = null;
		
		try
		{
			
			logger.info("obtendo filho do id" + id);
			con = DataSourceUtils.getConnection(dataSource);
			String sql = querys.get("obterFilhosPorIdPai");
			logger.info("obterFilhosPorIdPai" + sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			
			//se esse pai nao tiver nenhum filho
			if (!rs.next())
				//retorna uma lista null
				return lstPaiFilho ;
	

			//mapei todos os filhos de um nivel
			do 
			{
				objAtivoNo = mapperAtivoNo(rs);
				logger.info("Inserindo na lista no com id pai e id filho" + objAtivoNo.getPai() + objAtivoNo.getFilho());
				lstAux.add(objAtivoNo);
				lstPaiFilho.add(objAtivoNo);
			} while (rs.next());
			
			Iterator<AtivosNo> itr = lstAux.iterator() ;
			while (itr.hasNext())
			{
				objAtivoNo = itr.next();
				//filho = pai 
				logger.info("interator de recursividade com id" + objAtivoNo.getFilho());
				this.pesquisarFilhoPorIdPai(objAtivoNo.getFilho(),lstPaiFilho);
			}
				
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{
			DataSourceUtils.releaseConnection(con, dataSource);
		}
		return null;
	}

	@Override
	public List<Ativos> pesquisarAtivosEstoque(Integer idEstoque, String codIdentificacao, Ativos atv) 
	{
	 	   
		Ativos objAtivos = null;
		List<Ativos> lstAtivos = new java.util.ArrayList<Ativos>();
		Connection con = null;

		try
		{

			con = DataSourceUtils.getConnection(dataSource);
			String sql = " "
				    +"  select a.ativos_id , a.layout_id ,i.locais_id ,count(i.itens_ativo_id) as qtd, i.itens_ativo_identificacao, l.locais_identificacao  " 
				    +" from  itens_ativo i " 
				    +"  inner join locais  l on ( i.locais_id=l.locais_id ) "
				    +"  inner join estoque e on(  e.estoque_id=l.estoque_id) " 
				    +"  inner join ativos  a on ( a.ativos_id=i.ativos_id)   " 				    
				    +"  where 1=1 " ; 
			
			
			if (idEstoque!=null)
			{
				sql = sql +   " and e.estoque_id=" +  idEstoque;
			}
			
			if (codIdentificacao!=null && !codIdentificacao.isEmpty())
			{
				
				sql = sql +   "  and lower(l.locais_identificacao)  like '%" +  codIdentificacao.toLowerCase()  + "%'";
				
				//sql = sql +   "  and lower(i.itens_ativo_identificacao)  like '%" +  codIdentificacao.toLowerCase()  + "%'";
			}
			
			if(atv!=null && atv.getLayout()!=null && atv.getLayout().getId() > 0)
			{
				sql = sql +   "  and a.layout_id="  + atv.getLayout().getId() ;
			}
			
			if(atv!=null && atv.getId()!=null && atv.getId() > 0)
			{
				sql = sql +   "  and a.ativos_id="  + atv.getId() ;
			}
	        
			if (atv.getInformacoes() != null) {
				
				List<Informacoes> informacoesList = new ArrayList<Informacoes>(atv.getInformacoes());
				String sqlComplementar = getComplementoQuerieParaConsultaComCamposDinamicos(
						"a", "ativos_id", "informacoes_ativo", informacoesList);
				sql = sql +  sqlComplementar ;
				 
			}
			
			sql = sql +   "   group by   a.ativos_id , a.layout_id , i.locais_id, i.itens_ativo_identificacao , l.locais_identificacao "  ;
			
			logger.info("pesquisar estoque:" + sql);
			PreparedStatement pst = con.prepareStatement(sql);

			ResultSet rs = pst.executeQuery();

			while (rs.next())
			{
				
				objAtivos = mapper(rs);
				objAtivos.setQtdItens(rs.getInt("qtd"));
				objAtivos.setLocalID(rs.getInt("locais_id"));
				objAtivos.setItens_ativo_identificacaodoSQL(rs.getString("itens_ativo_identificacao"));
				lstAtivos.add(objAtivos);
			}
		} catch (SQLException e)
		{
			throw new IllegalStateException(e.getMessage());		
		} finally
		{

			DataSourceUtils.releaseConnection(con, dataSource);
		}

		return lstAtivos;		
		
	}


}

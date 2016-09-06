package br.com.controlefinanceiro.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.model.CategoriaFinanca;
import br.com.controlefinanceiro.model.Financa;
import br.com.controlefinanceiro.model.Usuario;

@Stateless
public class FinancaDAO extends AbstractDAO<Financa>{

	public List<Financa> buscarFinancasPorCategoria(CategoriaFinanca categoria, Usuario usuarioLogado) {
		try {
			String sql = "SELECT financa FROM Financa financa WHERE financa.categoriaFinanca.id = :idCategoria AND financa.idUsuario = :idUsuario";
			Query query = em.createQuery(sql);
			query.setParameter("idCategoria", categoria.getId());
			query.setParameter("idUsuario", usuarioLogado.getId());
			return (List<Financa>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Financa> buscarFinancasDoUsuario(Usuario usuarioLogado) {
		try {
			String sql = "SELECT financa FROM Financa financa WHERE financa.idUsuario = :idUsuario";
			Query query = em.createQuery(sql);
			query.setParameter("idUsuario", usuarioLogado.getId());
			return (List<Financa>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}


}

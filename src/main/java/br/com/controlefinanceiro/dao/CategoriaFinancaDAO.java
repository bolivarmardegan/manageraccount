package br.com.controlefinanceiro.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.model.CategoriaFinanca;
import br.com.controlefinanceiro.model.Usuario;

@Stateless
public class CategoriaFinancaDAO extends AbstractDAO<CategoriaFinanca>{

	public CategoriaFinanca buscarCategoriaPorNome(String nome, Usuario usuarioLogado) {
		String sql = "SELECT categoria FROM CategoriaFinanca categoria WHERE categoria.nome = :nome AND categoria.idUsuario = :idUsuario";
		Query query = em.createQuery(sql);
		query.setParameter("nome", nome);
		query.setParameter("idUsuario", usuarioLogado.getId());
		return (CategoriaFinanca) query.getSingleResult();
	}

	public List<CategoriaFinanca> buscarCategoriasDoUsuario(Usuario usuarioLogado) {
			try {
				String sql = "SELECT categoria FROM CategoriaFinanca categoria WHERE categoria.idUsuario = :idUsuario";
				Query query = em.createQuery(sql);
				query.setParameter("idUsuario", usuarioLogado.getId());
				return (List<CategoriaFinanca>) query.getResultList();
			} catch (Exception e) {
				return null;
			}
	}

}

package br.com.controlefinanceiro.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.Plano;

@Stateless
public class PlanoDAO extends AbstractDAO<Plano>{
	
	public List<Menu> buscarMenusDoPlano(Plano plano){
		String sql = "SELECT plano.menuList FROM Plano plano where plano.id = :idPlano";
		Query query = em.createQuery(sql);
		query.setParameter("idPlano", plano.getId());
		return query.getResultList();
	}

	public List<Plano> buscarPlanoPorPerfil(Integer idPerfil) {
		String sql = "SELECT plano FROM Plano plano WHERE plano.perfil.id = :idPerfil";
		Query query = em.createQuery(sql);
		query.setParameter("idPerfil", idPerfil);
		return query.getResultList();
	}

}

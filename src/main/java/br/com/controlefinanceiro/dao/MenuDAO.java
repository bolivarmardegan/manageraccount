package br.com.controlefinanceiro.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.PermissoesUsuario;
import br.com.controlefinanceiro.model.Plano;

@Stateless
public class MenuDAO extends AbstractDAO<Menu>{
	
	public Menu buscarMenuPorNome(Menu menu){
		String sql = "SELECT t FROM " + getNomeClasse() + " t WHERE t.nome = :nome" ;
		Query query = em.createQuery(sql);
		query.setParameter("nome", menu.getNome());
		Menu menuAchado = (Menu)query.getResultList().get(0);
		return menuAchado;
	}
	
	public Menu buscarMenuPorString(String nomeMenu){
		String sql = "SELECT t FROM " + getNomeClasse() + " t WHERE t.nome = " + nomeMenu;
		Query query = em.createQuery(sql);
//		query.setParameter("nome", nomeMenu);
		Menu menuAchado = (Menu)query.getResultList().get(0);
		return menuAchado;
	}
	
	public List<Plano> buscarPlanosDoMenu(Menu menu){
		String sql = "SELECT menu.planoList FROM Menu menu WHERE menu.id = :idMenu";
		Query query = em.createQuery(sql);
		query.setParameter("idMenu", menu.getId());
		return query.getResultList();
	}
	
	public List<Menu> buscarMenuPorPermissao(PermissoesUsuario permissao){
		String sql = "SELECT menu FROM Menu menu WHERE menu.permissao.id = :permissaoId";
		Query query = em.createQuery(sql);
		query.setParameter("permissaoId", permissao.getId());
		return query.getResultList();
	}
}

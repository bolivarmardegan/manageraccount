package br.com.controlefinanceiro.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.SubMenus;

@Stateless
public class SubMenusDAO extends AbstractDAO<SubMenus>{
	
	public List<SubMenus> buscarSubMenusPorMenu(Menu menuId){
		String sql = "SELECT subMenu FROM SubMenus subMenu where menu.id = "+menuId.getId();
		Query query = em.createQuery(sql);
		return query.getResultList();
	}

}

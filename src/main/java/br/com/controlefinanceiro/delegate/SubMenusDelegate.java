package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.SubMenusDAO;
import br.com.controlefinanceiro.model.SubMenus;

@Stateless
public class SubMenusDelegate extends AbstractDelegate<SubMenus>{

	@Inject
	private SubMenusDAO subMenusDAO;

	@Override
	public AbstractDAO<SubMenus> getDaoInstance() {
		return this.subMenusDAO;
	}

}

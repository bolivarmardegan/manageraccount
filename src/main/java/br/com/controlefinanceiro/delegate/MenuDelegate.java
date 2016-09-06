package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.MenuDAO;
import br.com.controlefinanceiro.model.Menu;

@Stateless
public class MenuDelegate extends AbstractDelegate<Menu>{

	@Inject
	private MenuDAO menuDAO;

	@Override
	public AbstractDAO<Menu> getDaoInstance() {
		return this.menuDAO;
	}

}

package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.TiposMenusDAO;
import br.com.controlefinanceiro.model.TiposMenus;

@Stateless
public class TiposMenusDelegate extends AbstractDelegate<TiposMenus>{

	@Inject
	private TiposMenusDAO tiposMenuDAO;;

	@Override
	public AbstractDAO<TiposMenus> getDaoInstance() {
		return this.tiposMenuDAO;
	}

}

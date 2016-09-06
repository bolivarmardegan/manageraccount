package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.PlanoDAO;
import br.com.controlefinanceiro.model.Plano;

@Stateless
public class PlanoDelegate extends AbstractDelegate<Plano>{

	@Inject
	private PlanoDAO planoDAO;

	@Override
	public AbstractDAO<Plano> getDaoInstance() {
		return this.planoDAO;
	}

}

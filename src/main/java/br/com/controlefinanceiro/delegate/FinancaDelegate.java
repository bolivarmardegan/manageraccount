package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.FinancaDAO;
import br.com.controlefinanceiro.model.Financa;

@Stateless
public class FinancaDelegate extends AbstractDelegate<Financa>{

	@Inject
	private FinancaDAO financaDAO;

	@Override
	public AbstractDAO<Financa> getDaoInstance() {
		return this.financaDAO;
	}

}

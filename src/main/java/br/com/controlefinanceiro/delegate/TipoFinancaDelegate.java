package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.TipoFinancaDAO;
import br.com.controlefinanceiro.model.TipoFinanca;

@Stateless
public class TipoFinancaDelegate extends AbstractDelegate<TipoFinanca>{

	@Inject
	private TipoFinancaDAO tipoFinancaDAO;

	@Override
	public AbstractDAO<TipoFinanca> getDaoInstance() {
		return this.tipoFinancaDAO;
	}

}

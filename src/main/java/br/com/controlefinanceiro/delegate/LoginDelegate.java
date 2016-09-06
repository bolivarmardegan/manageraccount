package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.LoginDAO;
import br.com.controlefinanceiro.model.Login;

@Stateless
public class LoginDelegate extends AbstractDelegate<Login>{

	@Inject
	private LoginDAO loginDAO;

	@Override
	public AbstractDAO<Login> getDaoInstance() {
		return this.loginDAO;
	}

}

package br.com.controlefinanceiro.delegate;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.EmailDAO;
import br.com.controlefinanceiro.model.Email;

@Stateless
public class EmailDelegate extends AbstractDelegate<Email>{

	@Inject
	private EmailDAO emailDAO;

	@Override
	public AbstractDAO<Email> getDaoInstance() {
		return this.emailDAO;
	}

}

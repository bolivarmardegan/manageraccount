package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.PermissoesUsuarioDAO;
import br.com.controlefinanceiro.model.PermissoesUsuario;

@Stateless
public class PermissoesUsuarioDelegate extends AbstractDelegate<PermissoesUsuario>{

	@Inject
	private PermissoesUsuarioDAO permissoesUsuarioDAO;

	@Override
	public AbstractDAO<PermissoesUsuario> getDaoInstance() {
		return this.permissoesUsuarioDAO;
	}

}

package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.UsuarioDAO;
import br.com.controlefinanceiro.model.Usuario;

@Stateless
public class UsuarioDelegate extends AbstractDelegate<Usuario>{

	@Inject
	private UsuarioDAO usuarioDAO;

	@Override
	public AbstractDAO<Usuario> getDaoInstance() {
		return this.usuarioDAO;
	}

}

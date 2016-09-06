package br.com.controlefinanceiro.delegate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.PerfilDAO;
import br.com.controlefinanceiro.model.Perfil;

@Stateless
public class PerfilDelegate extends AbstractDelegate<Perfil>{

	@Inject
	private PerfilDAO perfilDAO;

	@Override
	public AbstractDAO<Perfil> getDaoInstance() {
		return this.perfilDAO;
	}

}

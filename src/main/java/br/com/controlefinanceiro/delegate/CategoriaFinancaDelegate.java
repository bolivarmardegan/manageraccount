package br.com.controlefinanceiro.delegate;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.dao.CategoriaFinancaDAO;
import br.com.controlefinanceiro.model.CategoriaFinanca;
import br.com.controlefinanceiro.model.Usuario;

@Stateless
public class CategoriaFinancaDelegate extends AbstractDelegate<CategoriaFinanca>{

	@Inject
	private CategoriaFinancaDAO categoriaFinancaDAO;

	@Override
	public AbstractDAO<CategoriaFinanca> getDaoInstance() {
		return this.categoriaFinancaDAO;
	}

	

}

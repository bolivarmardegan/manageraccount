package br.com.controlefinanceiro.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.core.AbstractManagedBean;
import br.com.controlefinanceiro.delegate.PermissoesUsuarioDelegate;
import br.com.controlefinanceiro.model.PermissoesUsuario;
import br.com.controlefinanceiro.util.Constants;

@Named
@SessionScoped
public class PermissoesUsuarioMBean extends AbstractManagedBean<PermissoesUsuario> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7859761088353035765L;
	@Inject
	private PermissoesUsuarioDelegate permissoesUsuarioDelegate;
	@Inject
	private PermissoesUsuario permissoesUsuario;
	private Integer fluxoDePagina;
	private List<PermissoesUsuario> listaPermissoesUsuarios;
	
	@Override
	public AbstractDelegate<PermissoesUsuario> getDelegateInstance() {
		return this.permissoesUsuarioDelegate;
	}

	@PostConstruct
	public void init() {
		this.listaPermissoesUsuarios = new ArrayList<PermissoesUsuario>();
		this.listaPermissoesUsuarios = this.permissoesUsuarioDelegate.buscarTodos();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	public void salvar(){
		this.permissoesUsuarioDelegate.inserir(this.permissoesUsuario);
		this.permissoesUsuario = new PermissoesUsuario();
		this.listaPermissoesUsuarios = this.permissoesUsuarioDelegate.buscarTodos();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	public void alterar(){
		this.permissoesUsuarioDelegate.alterar(this.permissoesUsuario);
		this.permissoesUsuario = new PermissoesUsuario();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	public void excluir(){
		this.permissoesUsuarioDelegate.deletar(this.permissoesUsuario);
		this.listaPermissoesUsuarios = this.permissoesUsuarioDelegate.buscarTodos();
	}
	
	public void carregarEdit(){
		this.setFluxoDePagina(Constants.ALTERACAO);
	}
	
	public Integer getFluxoDePagina() {
		return fluxoDePagina;
	}
	
	public void setFluxoDePagina(Integer fluxoDePagina) {
		this.fluxoDePagina = fluxoDePagina;
	}

	public PermissoesUsuarioDelegate getPermissoesUsuarioDelegate() {
		return permissoesUsuarioDelegate;
	}

	public void setPermissoesUsuarioDelegate(PermissoesUsuarioDelegate permissoesUsuarioDelegate) {
		this.permissoesUsuarioDelegate = permissoesUsuarioDelegate;
	}

	public PermissoesUsuario getPermissoesUsuario() {
		return permissoesUsuario;
	}

	public void setPermissoesUsuario(PermissoesUsuario permissoesUsuario) {
		this.permissoesUsuario = permissoesUsuario;
	}

	public List<PermissoesUsuario> getListaPermissoesUsuarios() {
		return listaPermissoesUsuarios;
	}

	public void setListaPermissoesUsuarios(List<PermissoesUsuario> listaPermissoesUsuarios) {
		this.listaPermissoesUsuarios = listaPermissoesUsuarios;
	}

}

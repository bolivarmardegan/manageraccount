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
import br.com.controlefinanceiro.delegate.PerfilDelegate;
import br.com.controlefinanceiro.model.Perfil;
import br.com.controlefinanceiro.util.Constants;

@Named
@SessionScoped
public class PerfilMBean extends AbstractManagedBean<Perfil> implements Serializable{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 6968865120383084941L;
	@Inject
	private PerfilDelegate perfilDelegate;
	@Inject
	private Perfil perfil;
	private Integer fluxoDePagina; 
	private List<Perfil> listaPerfil;
	
	@Override
	public AbstractDelegate<Perfil> getDelegateInstance() {
		return perfilDelegate;
	}

	@PostConstruct
	public void init() {
		this.setFluxoDePagina(Constants.INCLUSAO);
		this.listaPerfil = new ArrayList<Perfil>();
		this.listaPerfil = perfilDelegate.buscarTodos();
	}
	
	public void salvar(){
		this.perfilDelegate.inserir(this.perfil);
		this.listaPerfil = perfilDelegate.buscarTodos();
		this.perfil = new Perfil();
	}
	
	public void excluir(){
		this.perfilDelegate.deletar(this.perfil);
		this.listaPerfil = perfilDelegate.buscarTodos();
		this.perfil = new Perfil();
	}
	
	public void alterar(){
		this.setFluxoDePagina(Constants.INCLUSAO);
		this.perfilDelegate.alterar(this.perfil);
		this.listaPerfil = perfilDelegate.buscarTodos();
		this.perfil = new Perfil();
	}
	
	public void carregarEdit(){
			this.setFluxoDePagina(Constants.ALTERACAO);
	}
	
	public Perfil getPerfil() {
		return perfil;
	}
	
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	public Integer getFluxoDePagina() {
		return fluxoDePagina;
	}
	
	public void setFluxoDePagina(Integer fluxoDePagina) {
		this.fluxoDePagina = fluxoDePagina;
	}
	
	public List<Perfil> getListaPerfil() {
		return listaPerfil;
	}
	
	public void setListaPerfil(List<Perfil> listaPerfil) {
		this.listaPerfil = listaPerfil;
	}

}

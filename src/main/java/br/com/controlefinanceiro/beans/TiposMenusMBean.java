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
import br.com.controlefinanceiro.delegate.TiposMenusDelegate;
import br.com.controlefinanceiro.model.TiposMenus;
import br.com.controlefinanceiro.util.Constants;

@Named
@SessionScoped
public class TiposMenusMBean extends AbstractManagedBean<TiposMenus> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4230170662722524897L;
	@Inject
	private TiposMenusDelegate tiposMenusDelegate;
	@Inject
	private TiposMenus tiposMenus;
	private List<TiposMenus> tiposMenusList;
	private Integer fluxoDePagina;

	@Override
	public AbstractDelegate<TiposMenus> getDelegateInstance() {
		return this.tiposMenusDelegate;
	}

	@PostConstruct
	public void init() {
		this.tiposMenusList = new ArrayList<TiposMenus>();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	
	public void carregarTiposMenusEdit(){
		this.setFluxoDePagina(Constants.ALTERACAO);
	}
	
	public void excluir(){
		this.tiposMenusDelegate.deletar(this.tiposMenus);
		this.tiposMenusList = this.tiposMenusDelegate.buscarTodos();
	}
	
	public void salvar(){
		this.tiposMenusDelegate.inserir(this.tiposMenus);
		this.tiposMenus = new TiposMenus();
		this.tiposMenusList = this.tiposMenusDelegate.buscarTodos();
	}
	public void editar(){
		this.tiposMenusDelegate.alterar(this.tiposMenus);
		this.tiposMenusList = this.tiposMenusDelegate.buscarTodos();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	public TiposMenus getTiposMenus() {
		return tiposMenus;
	}

	public void setTiposMenus(TiposMenus tiposMenus) {
		this.tiposMenus = tiposMenus;
	}

	public List<TiposMenus> getTiposMenusList() {
		return tiposMenusList;
	}

	public void setTiposMenusList(List<TiposMenus> tiposMenusList) {
		this.tiposMenusList = tiposMenusList;
	}
	
	
	public Integer getFluxoDePagina() {
		return fluxoDePagina;
	}
	
	public void setFluxoDePagina(Integer fluxoDePagina) {
		this.fluxoDePagina = fluxoDePagina;
	}
	
	

}

















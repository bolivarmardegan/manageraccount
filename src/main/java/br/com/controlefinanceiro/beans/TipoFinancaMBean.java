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
import br.com.controlefinanceiro.delegate.TipoFinancaDelegate;
import br.com.controlefinanceiro.model.TipoFinanca;
import br.com.controlefinanceiro.util.Constants;

@Named
@SessionScoped
public class TipoFinancaMBean extends AbstractManagedBean<TipoFinanca> implements Serializable{


	private static final long serialVersionUID = -6313912561949926782L;
	@Inject
	private TipoFinancaDelegate tipoFinancaDelegate;
	@Inject
	private TipoFinanca tipoFinanca;
	private List<TipoFinanca> tipoFinancaList;
	private Integer fluxoDePagina;

	@Override
	public AbstractDelegate<TipoFinanca> getDelegateInstance() {
		return this.tipoFinancaDelegate;
	}

	@PostConstruct
	public void init() {
		this.tipoFinancaList = new ArrayList<TipoFinanca>();
		this.tipoFinancaList = this.tipoFinancaDelegate.buscarTodos();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	public void carregarTipos(){
		this.setFluxoDePagina(Constants.ALTERACAO);
	}
	
	public void excluir(){
		this.tipoFinancaDelegate.deletar(this.tipoFinanca);
		this.tipoFinancaList = this.tipoFinancaDelegate.buscarTodos();
	}
	
	public void salvar(){
		this.tipoFinancaDelegate.inserir(this.tipoFinanca);
		this.tipoFinanca = new TipoFinanca();
		this.tipoFinancaList = this.tipoFinancaDelegate.buscarTodos();
	}
	public void editar(){
		this.tipoFinancaDelegate.alterar(this.tipoFinanca);
		this.tipoFinanca = new TipoFinanca();
		this.tipoFinancaList = this.tipoFinancaDelegate.buscarTodos();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	

	public TipoFinanca getTipoFinanca() {
		return tipoFinanca;
	}

	public void setTipoFinanca(TipoFinanca tipoFinanca) {
		this.tipoFinanca = tipoFinanca;
	}
	
	public List<TipoFinanca> getTipoFinancaList() {
		return tipoFinancaList;
	}
	
	public Integer getFluxoDePagina() {
		return fluxoDePagina;
	}
	
	public void setFluxoDePagina(Integer fluxoDePagina) {
		this.fluxoDePagina = fluxoDePagina;
	}
	
	
	

}

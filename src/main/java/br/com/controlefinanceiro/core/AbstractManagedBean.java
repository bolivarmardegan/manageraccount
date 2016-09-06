package br.com.controlefinanceiro.core;
import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.util.*;
//import br.com.ppraonline.Helper.UsuarioSessionControler;
//import br.com.ppraonline.model.Empresa;
//import br.com.ppraonline.model.Usuario;
//import br.com.ppraonline.util.PagesUrl;

public abstract class AbstractManagedBean<T> {
	private T bean;

	private Integer tipoOperacao;

	protected Session session = new Session();

	public abstract AbstractDelegate<T> getDelegateInstance();

	public abstract void init();
	
	

	public T getBean() {

		return bean;
	}

	public AbstractManagedBean() {
		super();
	}

	public void setBean(T bean) {
		this.bean = bean;
	}

	public void inserir() {
		setTipoOperacao(Constants.INCLUSAO);
		getDelegateInstance().inserir(getBean());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Inclusão realizada com sucesso", "INFO"));
	}

	public void alterar() {
		setTipoOperacao(Constants.ALTERACAO);
		getDelegateInstance().alterar(getBean());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Alteração realizada com sucesso", "INFO"));
	}

	public void deletar() {
		setTipoOperacao(Constants.EXCLUSAO);
		getDelegateInstance().deletar(getBean());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Exclusão realizada com sucesso", "INFO"));
	}

	public Integer getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(Integer tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	

	protected Usuario getUsuarioLogado() {

		// EmpregadoCaixa empregadoLogado = (EmpregadoCaixa)
		// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Empregado");
		//
		Usuario usuario = new Usuario();
		// usuario.setId(empregadoLogado.getId());
		// usuario.setNome(empregadoLogado.getMatricula());
		// usuario.setIdUnidade(empregadoLogado.getUnidade().getId());
		return usuario;
	}

	public void redirectToPage(String pagename, boolean redirect) {
		String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		url += pagename;
		if (redirect)
			url += "?faces-redirect=true";
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logOut() {
		redirectToPage(PagesUrl.LOGIN.getUrl(), true);

	}

	public String getContextPath() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}
	
	public FacesContext getCurretInstance(){
		return FacesContext.getCurrentInstance();
	}

}

















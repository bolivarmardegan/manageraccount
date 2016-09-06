package br.com.controlefinanceiro.core;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;


public class Session {


	
	public void addObjeto(String id, Object object) {
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.getExternalContext().getSessionMap().put(id, object);
	}

	public Object getObjecto(String id) {
		FacesContext fc = FacesContext.getCurrentInstance();
		return fc.getExternalContext().getSessionMap().get(id);
	}

	private ExternalContext currentExternalContext() {
		if (FacesContext.getCurrentInstance() == null) {
			throw new RuntimeException("O FacesContext não pode ser chamado fora de uma requisição HTTP");
		} else {
			return FacesContext.getCurrentInstance().getExternalContext();
		}
	}

	public FacesContext getCurrentInstance() {
		return FacesContext.getCurrentInstance();
	}

	public RequestContext getRequestContext() {
		return RequestContext.getCurrentInstance();
	}

	public void redirectToPage(String url) {
		try {
			this.currentExternalContext().redirect(url);
		} catch (IOException e) {
			throw new RuntimeException("Não foi possível acessar a página " + url);
		}
	}

	public String getContextPath() {
		return currentExternalContext().getRequestContextPath();
	}

	public void encerrarSessao() {
		currentExternalContext().invalidateSession();
	}

	public Object getAttribute(String nome) {
		return currentExternalContext().getSessionMap().get(nome);
	}

	public void setAttribute(String nome, Object valor) {
		currentExternalContext().getSessionMap().put(nome, valor);
	}

	public void addMessageInfo(String msg, String param) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, param);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addMessageError(String msg) {
		addMessageError(msg, null);
	}

	public void addMessageError(String msg, String param) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, param);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}

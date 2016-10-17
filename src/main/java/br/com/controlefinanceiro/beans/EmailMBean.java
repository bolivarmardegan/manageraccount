package br.com.controlefinanceiro.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.PostActivate;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.controlefinanceiro.Helper.UsuarioSessionControler;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.core.AbstractManagedBean;
import br.com.controlefinanceiro.delegate.EmailDelegate;
import br.com.controlefinanceiro.model.Email;
import br.com.controlefinanceiro.service.EmailService;
import br.com.controlefinanceiro.util.PagesUrl;

@Named
@SessionScoped
public class EmailMBean extends AbstractManagedBean<Email> implements Serializable{

	@Inject
	private EmailDelegate emailDelegate;
	@Inject
	private EmailService emailService;
	@Inject
	private Email email;
	@Inject
	private UsuarioSessionControler usuSession;
	
	@Override
	public AbstractDelegate<Email> getDelegateInstance() {
		
		return this.emailDelegate;
	}

	@PostConstruct
	public void init() {
		email.setTitulo("FeedBack");
		this.email.setReceptor("bolivar.android@gmail.com");
		this.email.setEmailUsuario(this.usuSession.getUsuarioLogado().getEmail());
		
	}
	
	public void cancelar(){
		this.email = new Email();
		this.redirectToPage(PagesUrl.INDEX.getUrl(), true);
	}
	
	
	public void enviarEmail(){
		this.emailService.sendEmail(this.email);
	}
	
	
	public Email getEmail() {
		return email;
	}
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	
	
	
	
	

}

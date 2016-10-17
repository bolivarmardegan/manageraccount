package br.com.controlefinanceiro.service;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.event.MethodExpressionActionListener;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.controlefinanceiro.model.Email;

@Stateless
@LocalBean
public class EmailService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6396605937508236450L;
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	//@Resource(lookup = "java:jboss/mail/controle-inanceiro") //Nome do Recurso que criamos no Wildfly
	
	
	private Session mailSession;
	
	public EmailService() {
		InitialContext ic;
		
		try {
			ic = new InitialContext();
			System.out.println("Vou tentar enviar o email....");
			Session mail = (Session)ic.lookup("java:jboss/mail/financeiro");
			
			this.mailSession = mail;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
	
	}
	
	@Asynchronous //Metodo Assíncrono para que a aplicação continue normalmente sem ficar bloqueada até que o email seja enviado    
	public void sendEmail(Email email) {
//		String to, String from, String subject, String content
	        log.info("Email enviado por " + email.getEmailUsuario() + " para " + email.getReceptor() +" : " + email.getMensagem());
	        try {
	           //Criação de uma mensagem simples
	            Message message = new MimeMessage(mailSession);
	            //Cabeçalho do Email
	            message.setFrom(new InternetAddress(email.getEmailUsuario()));
	            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.getReceptor()));            
	            message.setSubject(email.getTitulo());
	            //Corpo do email
	            message.setText(email.getMensagem());

	            //Envio da mensagem            
	            Transport.send(message);
	            log.debug("Email enviado");
	        } catch (MessagingException e) {
	            log.error("Erro a enviar o email : " + e.getMessage());        }
	    }
}

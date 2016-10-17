package br.com.controlefinanceiro.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tabela500_email")
public class Email implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1074120894871194288L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_EMAIL")
	private Integer id;
	@Column(name="ID_USUARIO")
	private Integer idUsuario;
	@Column(name="EMAIL_USER")
	private String emailUsuario;
	@Column(name="TITULO")
	private String titulo;
	@Column(name="MENSAGEM")
	private String mensagem;
	@Column(name="RECEPTOR_EMAIL")
	private String receptor;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getEmailUsuario() {
		return emailUsuario;
	}
	
	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getReceptor() {
		return receptor;
	}
	
	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}

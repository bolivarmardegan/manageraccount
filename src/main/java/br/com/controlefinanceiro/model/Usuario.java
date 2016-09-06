package br.com.controlefinanceiro.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="tabela008_usuario")
public class Usuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8457005095192745012L;
	
	@Id
	@Column(name="ID_USUARIO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer Id;
	@Column(name="NOME_USUARIO")
	private String nome;
	@Column(name="LOGIN_USUARIO")
	private String login;
	@Column(name="SENHA_USUARIO")
	private String senha;
	@Column(name="STATUS_USUARIO")
	private Boolean status;
	@ManyToOne
	@JoinTable(name = "tabela010_planos_usuarios", joinColumns = @javax.persistence.JoinColumn(name = "id_plano"), 
	inverseJoinColumns = @javax.persistence.JoinColumn(name = "id_usuario"))
	private Plano plano = new Plano();
	@ManyToOne
	@JoinColumn(name = "ID_PERFIL_USUARIO")
	private Perfil perfil = new Perfil();
	@Column(name="SOBRENOME_USUARIO")
	private String sobrenome;
	@NotEmpty
	@Pattern(regexp = ".+@.+\\.[a-z]+",message="Email inválido.")
	@Column(name="EMAIL_USUARIO")
	private String email;
	@Column(name="CPF_USUARIO")
	private String cpf;
	@Column(name="RG_USUARIO")
	private String rg;
		
	@ManyToOne
	@JoinColumn(name = "ID_PERMISSOES_USUARIO")
	private PermissoesUsuario permissoesUsuario = new PermissoesUsuario();
	
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public Perfil getPerfil() {
		return perfil;
	}
	
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	public Plano getPlano() {
		return plano;
	}
	
	public void setPlano(Plano plano) {
		this.plano = plano;
	}
	
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getRg() {
		return rg;
	}
	public void setRg(String rg) {
		this.rg = rg;
	}
	
	public PermissoesUsuario getPermissoesUsuario() {
		return permissoesUsuario;
	}
	
	public void setPermissoesUsuario(PermissoesUsuario permissoesUsuario) {
		this.permissoesUsuario = permissoesUsuario;
	}

}

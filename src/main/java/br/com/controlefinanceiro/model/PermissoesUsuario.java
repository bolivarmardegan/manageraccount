package br.com.controlefinanceiro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="tabela004_permissoes_usuario")
public class PermissoesUsuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6964203427351393687L;
	
	
	@Id
	@Column(name="ID_PERMISSOES_USUARIO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="NOME_PERMISSAO")
	private String nome;
	@Column(name="DESCRICAO_PERMISSAO")
	@OneToMany(mappedBy="permissoesUsuario")
	private List<Usuario> usuariosList = new ArrayList<Usuario>();
	private String descricao;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public List<Usuario> getUsuariosList() {
		return usuariosList;
	}
	
	public void setUsuariosList(List<Usuario> usuariosList) {
		this.usuariosList = usuariosList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}

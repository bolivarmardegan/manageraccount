package br.com.controlefinanceiro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="categoria_financa")
public class CategoriaFinanca implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7809683155263032904L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_CATEGORIA")
	private Integer id;
	@Column(name="NOME_CATEGORIA")
	private String nome;
	@OneToMany(mappedBy="categoriaFinanca")
	private List<Financa> financas = new ArrayList<Financa>();
	@Column(name="ID_USUARIO")
	private Integer idUsuario;
	
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
	public Integer getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

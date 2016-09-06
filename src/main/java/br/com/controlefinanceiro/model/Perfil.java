package br.com.controlefinanceiro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="tabela003_perfil")
public class Perfil implements Serializable{
	
	private static final long serialVersionUID = -2269344189655473014L;
	@Id
	@Column(name="ID_PERFIL")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="NOME_PERFIL")
	private String nome;
	
	@OneToMany(mappedBy="perfil", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Usuario> usuariosList;
	
	@OneToMany(mappedBy="tipoMenu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Menu> menuList;
	
	@Column(name="DESCRICAO_PERFIL")
	private String descricao;
	
	@OneToMany(mappedBy="perfil")
	private List<Plano> planoList = new ArrayList<Plano>();
	
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
	
	public List<Plano> getPlanoList() {
		return planoList;
	}
	
	public void setPlanoList(List<Plano> planoList) {
		this.planoList = planoList;
	}
	
	public List<Menu> getMenuList() {
		return menuList;
	}
	
	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

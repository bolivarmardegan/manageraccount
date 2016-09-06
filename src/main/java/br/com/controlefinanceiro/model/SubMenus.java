package br.com.controlefinanceiro.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="tabela006_submenus")
public class SubMenus implements Serializable{
	
	
	private static final long serialVersionUID = 7750541450743520871L;
	/**
	 * 
	 */
	
	@Id
	@Column(name="ID_SUBMENU")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="NOME_SUBMENU")
	private String nome;
	@ManyToOne
	@JoinColumn(nullable=false, name="ID_MENU")
	private Menu menu = new Menu();
	@Column(name="URL_SUBMENU")
	private String url;
	
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
	
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}

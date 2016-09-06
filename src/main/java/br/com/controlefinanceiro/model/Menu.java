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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tabela001_menu")
public class Menu implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 834988787341658648L;
	
	@Id
	@Column(name="ID_MENU")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="NOME_MENU")
	private String nome;
	@Column(name="STATUS_MENU")
	private boolean status;
	@OneToMany(mappedBy="menu")
	private List<SubMenus> subMenusList = new ArrayList<SubMenus>();
	@ManyToMany(mappedBy="menuList")
	private List<Plano> planoList = new ArrayList<Plano>();
	@ManyToOne
	@JoinColumn(name = "ID_TIPO_MENU")
	private TiposMenus tipoMenu;
	
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public List<Plano> getPlanoList() {
		return planoList;
	}
	public void setPlanoList(List<Plano> planoList) {
		this.planoList = planoList;
	}
	
	public TiposMenus getTipoMenu() {
		return tipoMenu;
	}
	
	public void setTipoMenu(TiposMenus tipoMenu) {
		this.tipoMenu = tipoMenu;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

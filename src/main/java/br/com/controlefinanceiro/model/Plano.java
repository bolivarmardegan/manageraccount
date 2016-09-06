package br.com.controlefinanceiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="tabela005_planos")
public class Plano implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5015079997065997143L;

	@Id
	@Column(name="ID_PLANO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="NOME_PLANO")
	private String nome;
	@Column(name="STATUS_PLANO")
	private boolean status;
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tabela011_planos_menus", joinColumns = @javax.persistence.JoinColumn(name = "id_plano"), 
    inverseJoinColumns = @javax.persistence.JoinColumn(name = "id_menu"))
	private List<Menu> menuList = new ArrayList<Menu>();
	
	@OneToMany(mappedBy="plano")
	private List<Usuario> usuariosList = new ArrayList<Usuario>();
	
	@ManyToOne
	@JoinColumn(nullable=false,name="ID_PERFIL")
	private Perfil perfil = new Perfil();
	
	
	@Column(name="VALOR_PLANO")
	private BigDecimal valorPlano;
	
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

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}

	public BigDecimal getValorPlano() {
		if(this.valorPlano == null)
			this.valorPlano = new BigDecimal("0");
		return valorPlano;
	}
	
	public void setValorPlano(BigDecimal valorPlano) {
		this.valorPlano = valorPlano;
	}
	
	
	public Perfil getPerfil() {
		return perfil;
	}
	
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}




















package br.com.controlefinanceiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="tabela010_financa")
public class Financa implements Serializable, Comparable<Financa>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -224145750341412185L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_FINANCA")
	private Integer id;
	@Column(name="NOME_FINANCA")
	private String nome;
	@ManyToOne
	@JoinColumn(nullable=false, name="ID_CATEGORIA_FINANCA")
	private CategoriaFinanca categoriaFinanca = new CategoriaFinanca();
	@Column(name="VALOR_FINANCA")
	private BigDecimal valor;
	@Column(name="DATA_VENCIMENTO")
	@Temporal(TemporalType.DATE)
	private Calendar dataVencimento;
	@Column(name="TIPO_FINANCA")
	private String tipoFinanca;
//	@Column(name="PAGA")
//	private boolean paga;
//	@Column(name="VENCIDA")
//	private boolean vencida;
	@Column(name="ID_USUARIO")
	private Integer idUsuario;
	
	

	public Financa(Integer id, String nome, CategoriaFinanca categoriaFinanca, BigDecimal valor,
			Calendar dataVencimento, String tipoFinanca, Integer idUsuario) {
		this.id = id;
		this.nome = nome;
		this.categoriaFinanca = categoriaFinanca;
		this.valor = valor;
		this.dataVencimento = dataVencimento;
		this.tipoFinanca = tipoFinanca;
//		this.paga = paga;
//		this.vencida = vencida;
		this.idUsuario = idUsuario;
	}


	public Financa() {
	}
	
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
	public CategoriaFinanca getCategoriaFinanca() {
		return categoriaFinanca;
	}
	public void setCategoriaFinanca(CategoriaFinanca categoriaFinanca) {
		this.categoriaFinanca = categoriaFinanca;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Calendar getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Calendar dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public String getTipoFinanca() {
		return tipoFinanca;
	}
	public void setTipoFinanca(String tipoFinanca) {
		this.tipoFinanca = tipoFinanca;
	}
//	public boolean isVencida() {
//		return vencida;
//	}
//	public void setVencida(boolean vencida) {
//		this.vencida = vencida;
//	}
	
	public Integer getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
//	public boolean isPaga() {
//		return paga;
//	}
//	
//	public void setPaga(boolean paga) {
//		this.paga = paga;
//	}
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	


	@Override
	public int compareTo(Financa financa) {
		return this.getId().compareTo(financa.getId());
	}
	
}




















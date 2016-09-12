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
	@Column(name="PAGA")
	private boolean paga;
	@Column(name="VENCIDA")
	private boolean vencida;
	@Column(name="ID_USUARIO")
	private Integer idUsuario;
	
	

	public Financa(Integer id, String nome, CategoriaFinanca categoriaFinanca, BigDecimal valor,
			Calendar dataVencimento, String tipoFinanca, boolean paga, boolean vencida, Integer idUsuario) {
		this.id = id;
		this.nome = nome;
		this.categoriaFinanca = categoriaFinanca;
		this.valor = valor;
		this.dataVencimento = dataVencimento;
		this.tipoFinanca = tipoFinanca;
		this.paga = paga;
		this.vencida = vencida;
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
	public boolean isVencida() {
		return vencida;
	}
	public void setVencida(boolean vencida) {
		this.vencida = vencida;
	}
	
	public Integer getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public boolean isPaga() {
		return paga;
	}
	
	public void setPaga(boolean paga) {
		this.paga = paga;
	}
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoriaFinanca == null) ? 0 : categoriaFinanca.hashCode());
		result = prime * result + ((dataVencimento == null) ? 0 : dataVencimento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + (paga ? 1231 : 1237);
		result = prime * result + ((tipoFinanca == null) ? 0 : tipoFinanca.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + (vencida ? 1231 : 1237);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Financa other = (Financa) obj;
		if (categoriaFinanca == null) {
			if (other.categoriaFinanca != null)
				return false;
		} else if (!categoriaFinanca.equals(other.categoriaFinanca))
			return false;
		if (dataVencimento == null) {
			if (other.dataVencimento != null)
				return false;
		} else if (!dataVencimento.equals(other.dataVencimento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (paga != other.paga)
			return false;
		if (tipoFinanca == null) {
			if (other.tipoFinanca != null)
				return false;
		} else if (!tipoFinanca.equals(other.tipoFinanca))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		if (vencida != other.vencida)
			return false;
		return true;
	}


	@Override
	public int compareTo(Financa financa) {
		return this.getId().compareTo(financa.getId());
	}
	
}




















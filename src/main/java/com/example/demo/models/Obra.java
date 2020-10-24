package com.example.demo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "obra")
public class Obra implements Serializable {

	private static final long serialVersionUID = 1734218624394347228L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "imagem", nullable = false)
	private String imagem;
	
	@Column(name = "data_publicacao", nullable = false)
	private Date dataPublicacao;
	
	@Column(name = "data_exposicao", nullable = false)
	private Date dataExposicao;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(name = "autor_obra",
	    joinColumns = { @JoinColumn(name = "obra_id") },
	    inverseJoinColumns = { @JoinColumn(name = "autor_id") })
	private List<Autor> autor = new ArrayList<Autor>();
	
	
	public Obra() {
		// TODO Auto-generated constructor stub
	}

	public Obra(String nome, String descricao, String imagem, Date dataPublicacao, Date dataExposicao) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.imagem = imagem;
		this.dataPublicacao = dataPublicacao;
		this.dataExposicao = dataExposicao;
	}

	public Obra(Long id, String nome, String descricao, String imagem, Date dataPublicacao, Date dataExposicao) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.imagem = imagem;
		this.dataPublicacao = dataPublicacao;
		this.dataExposicao = dataExposicao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public Date getDataExposicao() {
		return dataExposicao;
	}

	public void setDataExposicao(Date dataExposicao) {
		this.dataExposicao = dataExposicao;
	}

	@JsonIgnore
	public List<Autor> getAutor() {
		return autor;
	}

	public void setAutor(List<Autor> autor) {
		this.autor = autor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Obra other = (Obra) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Obra [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", imagem=" + imagem
				+ ", dataPublicacao=" + dataPublicacao + ", dataExposicao=" + dataExposicao + "]";
	}
	
}

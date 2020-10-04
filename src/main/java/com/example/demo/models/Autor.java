package com.example.demo.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

import com.example.demo.enums.SexoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "autor")
public class Autor implements Serializable{
	
	private static final long serialVersionUID = 8061343947930368980L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "sexo", nullable = false)
	private SexoEnum sexo;
	
	@Column(name = "email", unique=true)
	private String email;
	
	@Column(name = "data_nascimento", nullable = false)
	private Date dataNascimento;
	
	@Column(name = "paisOrigem", nullable = false)
	private String paisOrigem;
	
	@Column(name = "cpf", unique=true)
	private String cpf;
	
	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
	@JoinTable(name = "autor_obra",
            joinColumns = { @JoinColumn(name = "autor_id") },
            inverseJoinColumns = { @JoinColumn(name = "obra_id") })
    private Set<Obra> obra = new HashSet<>();
	
	public Autor() {
		// TODO Auto-generated constructor stub
	}

	public Autor(String nome, SexoEnum sexo, String email, Date dataNascimento, String paisOrigem, String cpf) {
		super();
		this.nome = nome;
		this.sexo = sexo;
		this.email = email;
		this.dataNascimento = dataNascimento;
		this.paisOrigem = paisOrigem;
		this.cpf = cpf;
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

	public SexoEnum getSexo() {
		return sexo;
	}

	public void setSexo(SexoEnum sexo) {
		this.sexo = sexo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getPaisOrigem() {
		return paisOrigem;
	}

	public void setPaisOrigem(String paisOrigem) {
		this.paisOrigem = paisOrigem;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	@JsonIgnore
	public Set<Obra> getObra() {
		return obra;
	}

	public void setObra(Set<Obra> obra) {
		this.obra = obra;
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
		Autor other = (Autor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Autor [id=" + id + ", nome=" + nome + ", sexo=" + sexo + ", email=" + email + ", dataNascimento="
				+ dataNascimento + ", paisOrigem=" + paisOrigem + ", cpf=" + cpf + "]";
	}
	
	
}

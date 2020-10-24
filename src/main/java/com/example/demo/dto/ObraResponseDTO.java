package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.demo.models.Autor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString 
public class ObraResponseDTO {
	
	private Optional<Long> id = Optional.empty();
	private String nome;
	private String descricao;
	private String imagem;
	private Date dataPublicacao;
	private Date dataExposicao;	
	private List<Autor> autor = new ArrayList<Autor>();
	
	public ObraResponseDTO() {
		// TODO Auto-generated constructor stub
	}

	public ObraResponseDTO(Optional<Long> id, String nome, String descricao, String imagem, Date dataPublicacao,
			Date dataExposicao, List<Autor> autorId) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.imagem = imagem;
		this.dataPublicacao = dataPublicacao;
		this.dataExposicao = dataExposicao;
		this.autor = autorId;
	}
	
	public ObraResponseDTO(String nome, String descricao, String imagem, Date dataPublicacao,
			Date dataExposicao, List<Autor> autorId) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.imagem = imagem;
		this.dataPublicacao = dataPublicacao;
		this.dataExposicao = dataExposicao;
		this.autor = autorId;
	}
	
}

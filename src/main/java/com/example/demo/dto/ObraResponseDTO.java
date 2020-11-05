package com.example.demo.dto;

import java.util.ArrayList;
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
	private String dataPublicacao;
	private String dataExposicao;	
	private List<Autor> autor = new ArrayList<Autor>();
	
}

package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString 
public class ObraRequestDTO {
	
	private Optional<Long> id = Optional.empty();
	
	@NotBlank(message = "Nome deve ser informado. ")
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres. ")
	private String nome;
	
	@NotBlank(message = "Descricao deve ser informado. ")
	@Length(min = 3, max = 240, message = "Descricao deve conter entre 3 e 240 caracteres. ")
	private String descricao;
	
	@NotBlank(message = "Imagem deve ser informado. ")
	private String imagem;
		
	private String dataPublicacao;
	
	private String dataExposicao;
	
	private List<Long> autorId = new ArrayList<>();
	
}

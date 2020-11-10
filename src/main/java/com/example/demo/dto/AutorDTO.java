package com.example.demo.dto;

import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString 
public class AutorDTO {
	
	private Optional<Long> id = Optional.empty();
	
	@NotBlank(message = "Nome deve ser informado. ")
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres. ")
	private String nome;
	
	@NotBlank(message = "sexo deve ser informado. ")
	private String sexo;	
	
	private String email;
	
	@NotBlank(message = "dataNascimento deve ser informado. ")
	private String dataNascimento;
	
	@NotBlank(message = "O Pais de Origem deve ser informado. ")
	private String paisOrigem;
	
	private String cpf;
	
}

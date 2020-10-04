package com.example.demo.dto;

import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//https://projectlombok.org/features/ToString


@Getter
@Setter
@ToString 
public class AutorDTO {
	
	private Optional<Long> id = Optional.empty();
	
	@NotBlank(message = "Nome não pode ser vazio.")
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
	private String nome;
	
	@NotBlank(message = "sexo não pode ser vazio.")
	private String sexo;	
	
	private String email;
	private Date dataNascimento;
	private String paisOrigem;
	private String cpf;

}

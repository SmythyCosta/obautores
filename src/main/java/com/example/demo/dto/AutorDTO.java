package com.example.demo.dto;

import java.util.Date;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//https://projectlombok.org/features/ToString


@Getter
@Setter
@ToString 
public class AutorDTO {
	
	private Optional<Long> id = Optional.empty();
	private String nome;	
	private String sexo;	
	private String email;
	private Date dataNascimento;
	private String paisOrigem;
	private String cpf;

}

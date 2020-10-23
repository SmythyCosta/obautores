package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.example.demo.models.Obra;

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
	
	@NotBlank(message = "sexo não pode ser vazio. ")
	private String sexo;	
	
	private String email;
	
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@NotBlank(message = "O Pais de Origem deve ser informado. ")
	private String paisOrigem;
	
	private String cpf;
	
	private List<Obra> obra = new ArrayList<Obra>();

}

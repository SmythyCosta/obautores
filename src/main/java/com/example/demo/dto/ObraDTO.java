package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.example.demo.models.Autor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString 
public class ObraDTO {
	
	private Optional<Long> id = Optional.empty();
	
	@NotBlank(message = "Nome deve ser informado. ")
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres. ")
	private String nome;
	
	@NotBlank(message = "Nome deve ser informado. ")
	@Length(min = 3, max = 240, message = "Nome deve conter entre 3 e 240 caracteres. ")
	private String descricao;
	
	@NotBlank()
	private String imagem;
		
	@Temporal(TemporalType.DATE)
	private Date dataPublicacao;
	
	@Temporal(TemporalType.DATE)
	private Date dataExposicao;
	
	private List<Autor> autor = new ArrayList<Autor>();
	

}

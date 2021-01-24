package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
public class Autor2 {
		
	@Id
	private Long id;
	
	private String nome;

	private String cpf;
	
	
	
}

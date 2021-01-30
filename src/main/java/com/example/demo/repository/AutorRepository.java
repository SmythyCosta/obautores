package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Autor;


public interface AutorRepository extends JpaRepository<Autor, Long>{
	
	Autor findByCpf(String cpf);
	
	Autor findByEmail(String email);
	
	Optional<Autor> findByNome(String nome);

}

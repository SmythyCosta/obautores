package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Obra;


public interface ObraRepository extends JpaRepository<Obra, Long>{
	
	Optional<Obra> findByNome(String nome);

}


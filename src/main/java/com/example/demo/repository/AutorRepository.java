package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.Autor;
import com.example.demo.models.Obra;


public interface AutorRepository extends JpaRepository<Autor, Long>{
	
	Autor findByCpf(String cpf);
	Autor findByEmail(String email);
	Optional<Autor> findByNome(String nome);
	
	@Query("select a from Autor a JOIN a.obra o WHERE o.id = ?1")
	List<Autor> findAutoresByIdObra(Long idObra); 
	
	
	//findByObraId
	//(Long publisherId)
	
	
	//SELECT c FROM Autor c JOIN c.obra sl WHERE sl.id = :idObra
	//@Query(value = "SELECT au FROM Autor au WHERE au.id = ?1", nativeQuery=true)
	//@Query(value = "SELECT c FROM Autor c JOIN c.obra WHERE c.obra.id = :idObra", nativeQuery=true)
	
	//@Query(value = "SELECT au FROM Autor au WHERE au.id = ?1", nativeQuery=true)
	//List<Autor> findAutoresByIdObra(Long idObra);
}

package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.Autor;


public interface AutorRepository extends JpaRepository<Autor, Long>{
	
	Autor findByCpf(String cpf);
	
	Autor findByEmail(String email);
	
	Optional<Autor> findByNome(String nome);
	
	@Query("select a from Autor a JOIN a.obra o WHERE o.id = ?1")
	List<Autor> findAutoresByIdObra(Long idObra); 
	
	@Query(value = "SELECT a.id, a.nome, a.cpf "
 			+ " FROM autor a ", nativeQuery = true)
	Page<IAutor> list2(Pageable pageable); 

}

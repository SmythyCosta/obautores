package com.example.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.demo.dto.AutorDTO;

public interface IAutorService<T> {

	/**
	 * Retorna uma lista paginada.
	 * 
	 * @param pageRequest
	 * @return Page<T>
	 */
	Page<AutorDTO> listarTodos(PageRequest pageRequest);
	
	/**
	 * Persiste um Obj na base de dados.
	 * 
	 * @param id
	 * @return Obj
	 */
	T persistir(T t);
	
	
	/**
	 * Retorna um Obj por ID.
	 * 
	 * @param id
	 * @return Optional<Entity>
	 */
	Optional<T> buscarPorId(Long id);
	
	/**
	 * Retorna um Obj por nome.
	 * 
	 * @param nome
	 * @return Optional<Entity>
	 */
	Optional<T> buscarPorNome(String nome);
	
	/**
	 * Deleta um Obj base de dados.
	 * 
	 * @param id
	 */
	void remover(Long id);

}
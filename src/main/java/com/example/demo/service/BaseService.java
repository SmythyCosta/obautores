package com.example.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface BaseService<T> {

	/**
	 * Retorna uma lista paginada.
	 * 
	 * @param pageRequest
	 * @return Page<T>
	 */
	Page<T> listarTodos(PageRequest pageRequest);
	
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
	 * @return Optional<Lancamento>
	 */
	Optional<T> buscarPorId(Long id);
	

	//Optional<T> buscarPorEmail(String email);
	
	/**
	 * Deleta um Obj base de dados.
	 * 
	 * @param id
	 */
	void remover(Long id);

}
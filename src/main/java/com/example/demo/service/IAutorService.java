package com.example.demo.service;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.AutorDTO;
import com.example.demo.exception.BusinessException;

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
	AutorDTO persistir(AutorDTO dto, BindingResult result) throws BusinessException, ParseException;
	
	
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
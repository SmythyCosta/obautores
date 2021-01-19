package com.example.demo.service;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.AutorDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;

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
	 * @throws javassist.NotFoundException 
	 */
	AutorDTO persistir(AutorDTO dto, BindingResult result) throws BusinessException, NotFoundException, ParseException, javassist.NotFoundException;
	
	
	/**
	 * Retorna um Obj por ID.
	 * 
	 * @param id
	 * @return AutorDTO
	 * @throws javassist.NotFoundException
	 * @throws BusinessException 
	 */
	AutorDTO buscarPorId(Long id) throws NotFoundException;
	
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
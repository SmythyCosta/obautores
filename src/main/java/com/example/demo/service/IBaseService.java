package com.example.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.ObraRequestDTO;
import com.example.demo.dto.ObraResponseDTO;

public interface IBaseService<T> {

	/**
	 * Retorna uma lista paginada.
	 * 
	 * @param pageRequest
	 * @return Page<T>
	 */
	Page<ObraResponseDTO> listarTodos(PageRequest pageRequest);
	
	/**
	 * Persiste um Obj na base de dados.
	 * 
	 * @param id
	 * @return Obj
	 */
	ObraResponseDTO persistir(ObraRequestDTO dto, BindingResult result);
	
	
	/**
	 * Retorna um Obj por ID.
	 * 
	 * @param id
	 * @return Optional<Entity>
	 */
	ObraResponseDTO searchByIdWithException(Long id);
	
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
	void remove(Long id);

}
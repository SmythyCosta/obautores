package com.example.demo.service;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.ObraRequestDTO;
import com.example.demo.dto.ObraResponseDTO;
import com.example.demo.models.Obra;

public interface IWorkService {

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
	ObraResponseDTO persistir(ObraRequestDTO dto, BindingResult result) throws ParseException;
	
	
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
	Optional<Obra> buscarPorNome(String nome);
	
	/**
	 * Deleta um Obj base de dados.
	 * 
	 * @param id
	 */
	void remove(Long id);

}
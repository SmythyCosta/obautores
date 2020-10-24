package com.example.demo.controllers;

import java.text.ParseException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ObraRequestDTO;
import com.example.demo.dto.ObraResponseDTO;
import com.example.demo.models.Autor;
import com.example.demo.models.Obra;
import com.example.demo.response.Response;
import com.example.demo.service.imp.AutorService;
import com.example.demo.service.imp.ObraService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;




@RestController
@RequestMapping(value="/api/obra")
@CrossOrigin(origins = "*")
@Api(value="API ObraControlle")
public class ObraControlle {
	
	private static final Logger log = LoggerFactory.getLogger(ObraControlle.class);
	
	@Autowired
	private ObraService obraService;
	
	@Autowired
	private AutorService autorService;
	
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	@ApiOperation(value="listar Obras")
	@GetMapping(value = "/listarPorPaginacao")
	public ResponseEntity<Response<Page<ObraResponseDTO>>> listarObras(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		log.info("Listagem de Obra, página: {}", pag);
		Response<Page<ObraResponseDTO>> response = new Response<Page<ObraResponseDTO>>();
		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.ASC, ord);
		
		Page<Obra> o = this.obraService.listarTodos(pageRequest);
		Page<ObraResponseDTO> dto = o.map(a -> this.parserToDTO(a));
		
		response.setData(dto);
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Listar Obra Por ID")
	@GetMapping(value = { "/{id}" })
	public ResponseEntity<Response<ObraResponseDTO>> buscarObraPorId(@PathVariable long id) {
		
		log.info("Buscando Autor por ID: {}", id);
		Response<ObraResponseDTO> response = new Response<ObraResponseDTO>();
		Optional<Obra> entity = this.obraService.buscarPorId(id);
		
		if (!entity.isPresent()) {			
			log.info("Obra não encontrado para o ID: {}", id);
			response.getErrors().add("Obra não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.parserToDTO(entity.get()));
		return ResponseEntity.ok(response);		
	}
	
	@ApiOperation(value="Criar nova Obra")
	@PostMapping()
	public ResponseEntity<Response<ObraResponseDTO>> criarNovaObra(@Valid @RequestBody ObraRequestDTO dto) {
		
		log.info("criando nova abra: {}", dto.toString());
		Response<ObraResponseDTO> response = new Response<ObraResponseDTO>();
		response.setData(this.parserToDTO(this.obraService.persistir(this.parserToEntity(dto))));
		return ResponseEntity.ok(response);
	}
	
	
	@ApiOperation(value="Alterar Obra por ID")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<ObraResponseDTO>> atualizaObra(@PathVariable long id, @Valid @RequestBody ObraRequestDTO dto, BindingResult result) throws ParseException {
	
		log.info("Atualizando Obra: {}", dto.toString());
		Response<ObraResponseDTO> response = new Response<ObraResponseDTO>();
		
		dto.setId(Optional.of(id));
		//ValidaObra(dto, result);
		
		Obra entity = this.parserToEntity(dto);
		
		if (result.hasErrors()) {
			log.error("Erro validando Obra: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		entity = this.obraService.persistir(entity);
		response.setData(this.parserToDTO(entity));
		return ResponseEntity.ok(response);
	}

	@ApiOperation(value="Deletar Obra Por ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> deletarObra(@PathVariable("id") Long id) {
		
		log.info("Removendo Autor: {}", id);
		Response<String> response = new Response<String>();
		Optional<Obra> entity = this.obraService.buscarPorId(id);
		
		if (!entity.isPresent()) {
			log.info("Erro ao remover Obra devido ID: {} ser inválido. ", id);
			response.getErrors().add("Erro ao remover Obra. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		this.obraService.remover(id);
		return ResponseEntity.ok(new Response<String>());		
	}	
	
	private Obra parserToEntity(ObraRequestDTO dto) {
		
		Obra entity = new Obra();
		
		if (dto.getId().isPresent()) {		
			entity.setId(dto.getId().get());
		}
		entity.setNome(dto.getNome());
		entity.setDescricao(dto.getDescricao());
		entity.setImagem(dto.getImagem());
		entity.setDataPublicacao(dto.getDataPublicacao());
		entity.setDataExposicao(dto.getDataExposicao());;
		
		dto.getAutorId().forEach(autorRequest -> {
			Optional<Autor> out = autorService.buscarPorId(autorRequest);
			if (out.isPresent()) {
				entity.getAutor().add(out.get());
			}			
		});
						
		return entity;
	}
	
	private ObraResponseDTO parserToDTO(Obra entity) {
		
		ObraResponseDTO dto = new ObraResponseDTO();
		
		dto.setId(Optional.of(entity.getId()));
		dto.setNome(entity.getNome());
		dto.setDescricao(entity.getDescricao());
		dto.setImagem(entity.getImagem());
		dto.setDataPublicacao(entity.getDataPublicacao());
		dto.setDataExposicao(entity.getDataExposicao());
		dto.getAutor().addAll(entity.getAutor());

		return dto;
	}
	
}

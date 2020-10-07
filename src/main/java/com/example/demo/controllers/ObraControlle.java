package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ObraDTO;
import com.example.demo.models.Obra;
import com.example.demo.repository.ObraRepository;
import com.example.demo.response.Response;
import com.example.demo.service.imp.ObraService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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

import com.example.demo.dto.AutorDTO;
import com.example.demo.enums.PaisEnum;
import com.example.demo.enums.SexoEnum;
import com.example.demo.models.Autor;
import com.example.demo.repository.AutorRepository;
import com.example.demo.response.Response;
import com.example.demo.service.imp.AutorService;
import com.example.demo.util.CpfUtil;
import com.example.demo.util.DataUtil;
import com.example.demo.util.EmailUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;




@RestController
@RequestMapping(value="/api/obra")
@CrossOrigin(origins = "*")
@Api(value="API ObraControlle")
public class ObraControlle {
	
	private static final Logger log = LoggerFactory.getLogger(ObraControlle.class);

	@Autowired
	private ObraRepository r;
	
	@Autowired
	private ObraService s;
	
	@Autowired
	private AutorService as;
	
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	
	@ApiOperation(value="Listar todos")
	@GetMapping()
	public List<Obra> listarTodos() {
		return r.findAll();
	}
	
	@GetMapping(value = "/listarPorPaginacao")
	public ResponseEntity<Response<Page<ObraDTO>>> listarPorPaginacao(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		log.info("Listagem de Obra, página: {}", pag);
		Response<Page<ObraDTO>> response = new Response<Page<ObraDTO>>();

		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.ASC, ord);
		
		Page<Obra> o = this.s.listarTodos(pageRequest);
		Page<ObraDTO> dto = o.map(a -> this.parserToDTO(a));
		

		response.setData(dto);
		return ResponseEntity.ok(response);
	}
	
	
	@ApiOperation(value="Criar novo")
	@PostMapping()
	public ResponseEntity<Response<ObraDTO>> criarNovo(@Valid @RequestBody ObraDTO dto) {
		
		log.info("criando nova abra: {}", dto.toString());
		Response<ObraDTO> response = new Response<ObraDTO>();

		response.setData(this.parserToDTO(this.s.persistir(this.parserToEntity(dto))));

		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Listar Por ID")
	@GetMapping(value = { "/{id}" })
	public Optional<Obra> encontrarPorId(@PathVariable long id) {
		return r.findById(id);
	}
	
	private Obra parserToEntity(ObraDTO dto) {
		
		Obra entity = new Obra();		
		if (dto.getId().isPresent()) {		
			entity.setId(dto.getId().get());
		}
		entity.setNome(dto.getNome());
		entity.setDescricao(dto.getDescricao());
		entity.setImagem(dto.getImagem());
		entity.setDataPublicacao(dto.getDataPublicacao());
		entity.setDataExposicao(dto.getDataExposicao());;
		entity.setAutor(dto.getAutor());
				
		return entity;
	}
	
	private ObraDTO parserToDTO(Obra entity) {
		ObraDTO dto = new ObraDTO();
		dto.setId(Optional.of(entity.getId()));
		dto.setNome(entity.getNome());
		dto.setDescricao(entity.getDescricao());
		dto.setImagem(entity.getImagem());
		dto.setDataPublicacao(entity.getDataPublicacao());
		dto.setDataExposicao(entity.getDataExposicao());
		//dto.setAutor(this.getAutores(entity.getId()));
		dto.setAutor(dto.getAutor());

		return dto;
	}
	
	private List<Autor> getAutores(Long idObra){
		return as.buscarAutoresPorObra(idObra);
	}
	
}

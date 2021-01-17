package com.example.demo.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.example.demo.exception.BusinessException;
import com.example.demo.models.Autor;
import com.example.demo.response.Response;
import com.example.demo.service.imp.AutorService;
import com.example.demo.util.CpfUtil;
import com.example.demo.util.DataUtil;
import com.example.demo.util.EmailUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/api/autor")
@CrossOrigin(origins = "*")
@Api(value="API AutorControlle")
public class AutorControlle extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(AutorControlle.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private AutorService autorService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
		
	@ApiOperation(value="listar Atores com Paginação")
	@GetMapping(value = "/listarAtores")
	public ResponseEntity<Response<Page<AutorDTO>>> listarAtores(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		log.info("Listagem de Autores, página: {}", pag);
		Response<Page<AutorDTO>> response = new Response<Page<AutorDTO>>();
		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.ASC, ord);
		Page<AutorDTO> responseAutor = this.autorService.listarTodos(pageRequest);

		response.setData(responseAutor);
		return ResponseEntity.ok(response);
	}
	
	
	@ApiOperation(value="Criar novo Autor")
	@PostMapping()
	public ResponseEntity<Response<AutorDTO>> criarNovoAutor(@Valid @RequestBody AutorDTO objDTO, BindingResult result) 
		throws BusinessException, ParseException  {
		
		log.info("criando novo autor: {}", objDTO.toString());
		Response<AutorDTO> response = new Response<AutorDTO>();

		response.setData(this.autorService.persistir(objDTO, result));
		return ResponseEntity.ok(response);
	}
	
	/** 
	@ApiOperation(value="Alterar Autor por ID")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<AutorDTO>> atualizaAutor(@PathVariable long id, @Valid @RequestBody AutorDTO dto, BindingResult result) throws ParseException {
		
		log.info("Atualizando lançamento: {}", dto.toString());
		Response<AutorDTO> response = new Response<AutorDTO>();
		
		dto.setId(Optional.of(id));
		ValidateAutor(dto, result);		
		
		Autor entity = this.parserToEntity(dto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando Autor: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		entity = this.autorService.persistir(entity);
		response.setData(this.parserToDTO(entity));
		return ResponseEntity.ok(response);
	}
	*/
	
	@ApiOperation(value="buscar Autor por ID")
	@GetMapping(value = { "/{id}" })
	public ResponseEntity<Response<AutorDTO>> buscarPorId(@PathVariable("id") long id) {
		
		log.info("Buscando Autor por ID: {}", id);
		Response<AutorDTO> response = new Response<AutorDTO>();
		Optional<Autor> entity = this.autorService.buscarPorId(id);
		
		if (!entity.isPresent()) {
			log.info("Autor não encontrado para o ID: {}", id);
			response.getErrors().add("Autor não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.parserToDTO(entity.get()));
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Deletar Por ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> deletar(@PathVariable("id") Long id) {
		
		log.info("Removendo Autor: {}", id);
		Response<String> response = new Response<String>();
		Optional<Autor> entity = this.autorService.buscarPorId(id);
		
		if (!entity.isPresent()) {
			log.info("Erro ao remover Autor devido ID: {} ser inválido. ", id);
			response.getErrors().add("Erro ao remover Autor. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		this.autorService.remover(id);
		return ResponseEntity.ok(new Response<String>());		
	}

	private void ValidateAutor(AutorDTO objDTO, BindingResult result) {
		
		boolean checkEmail = true;
		boolean checkCPF = true;
		boolean checkNome = true;
		
		if (!"".equals(objDTO.getEmail().trim())) {
			if (EmailUtil.isValidEmail(objDTO.getEmail())) {
				result.addError(new ObjectError("Email", "Email inválido. "));
			}
		}
		
		if (objDTO.getPaisOrigem().equalsIgnoreCase(PaisEnum.BRASIL.toString())) {
			objDTO.setCpf(CpfUtil.displaysOnlyNumbers(objDTO.getCpf()));
			if (!CpfUtil.valida(objDTO.getCpf())){
				result.addError(new ObjectError("CPF", "Cpf inválido. "));
			}
		} else {
			objDTO.setCpf("");
			checkCPF = false;
		}
		
		// ================================
		// Validando Email e Cpf na edição 
		// ================================
		if (objDTO.getId().isPresent()) {
				
			Optional<Autor> entity = this.autorService.buscarPorId(objDTO.getId().get());	
			if (entity.isPresent()) {
				if (entity.get().getEmail().equalsIgnoreCase(objDTO.getEmail())) {
					checkEmail = false;
				}				
				if (entity.get().getCpf().equalsIgnoreCase(objDTO.getCpf())) {
					checkCPF = false;
				}
				if (entity.get().getNome().equalsIgnoreCase(objDTO.getNome())) {
					checkNome = false;
				}
			}
		}
		
		if (objDTO.getDataNascimento() != null) {
			if (!DataUtil.isDateValid(objDTO.getDataNascimento())) {
				result.addError(new ObjectError("Datas", "DataNascimento Inválida. "));
			}
		}
				
		if (checkEmail) {
			this.autorService.buscarPorEmail(objDTO.getEmail()).ifPresent(
					e -> result.addError(new ObjectError("Email", "Email já existente. "))
			);		
		}
			
		if (checkCPF) {
			this.autorService.buscarPorCpf(objDTO.getCpf()).ifPresent(
					f -> result.addError(new ObjectError("CPF", "CPF já existente. "))
			);
		}
		
		if (checkNome) {
			this.autorService.buscarPorNome(objDTO.getNome()).ifPresent(
					g -> result.addError(new ObjectError("Nome", "Nome já existente. "))
			);
		}

		if (!EnumUtils.isValidEnum(SexoEnum.class, objDTO.getSexo())) {
			result.addError(new ObjectError("sexo", "Tipo de sexo inválido. "));
		}
				
		return;
	}
		
	private Autor parserToEntity(AutorDTO dto, BindingResult result) throws ParseException {
		
		Autor entity = new Autor();
		
		if (dto.getId().isPresent()) {		
			entity.setId(dto.getId().get());
		}
		entity.setNome(dto.getNome());
		entity.setEmail(dto.getEmail());
		entity.setDataNascimento(this.dateFormat.parse(dto.getDataNascimento()));
		entity.setPaisOrigem(dto.getPaisOrigem());
		entity.setCpf(dto.getCpf());
		entity.setSexo(SexoEnum.valueOf(dto.getSexo()));
		return entity;
	}
	
	private AutorDTO parserToDTO(Autor entity) {
		AutorDTO dto = new AutorDTO();
		dto.setId(Optional.of(entity.getId()));
		dto.setNome(entity.getNome());
		dto.setSexo(entity.getSexo().toString());
		dto.setEmail(entity.getEmail());
		dto.setDataNascimento(this.dateFormat.format(entity.getDataNascimento()));
		dto.setPaisOrigem(entity.getPaisOrigem());
		dto.setCpf(entity.getCpf());
		
		return dto;
	}
}

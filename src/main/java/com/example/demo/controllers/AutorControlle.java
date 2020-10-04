package com.example.demo.controllers;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AutorDTO;
import com.example.demo.enums.SexoEnum;
import com.example.demo.models.Autor;
import com.example.demo.repository.AutorRepository;
import com.example.demo.response.Response;
import com.example.demo.service.imp.AutorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/api/autor")
@CrossOrigin(origins = "*")
@Api(value="API AutorControlle")
public class AutorControlle {
	
	private static final Logger log = LoggerFactory.getLogger(AutorControlle.class);

	@Autowired
	private AutorRepository r;
	
	@Autowired
	private AutorService s;
	
	@ApiOperation(value="Listar todos")
	@GetMapping()
	public List<Autor> listarTodos() {
		return r.findAll();
	}
	
	@ApiOperation(value="Listar Por ID")
	@GetMapping(value = { "/{id}" })
	public Optional<Autor> encontrarPorId(@PathVariable long id) {
		return r.findById(id);
	}
	
	@ApiOperation(value="Criar novo")
	@PostMapping()
	public ResponseEntity<Response<AutorDTO>> criarNovo(@Valid @RequestBody AutorDTO objDTO, BindingResult result) throws ParseException  {
		
		log.info("criando novo autor: {}", objDTO.toString());
		Response<AutorDTO> response = new Response<AutorDTO>();
		//validarFuncionario(objDTO, result);
		Autor autor = this.parserToEntity(objDTO, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando Autor: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Autor out = this.s.persistir(autor);
		response.setData(this.parserToDTO(out));
		
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Alterar Por ID")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<AutorDTO>> atualiza(@PathVariable long id, @Valid @RequestBody AutorDTO dto, BindingResult result) throws ParseException {
		
		log.info("Atualizando lançamento: {}", dto.toString());
		Response<AutorDTO> response = new Response<AutorDTO>();
		//validarFuncionario(lancamentoDto, result);
		
		dto.setId(Optional.of(id));
		Autor entity = this.parserToEntity(dto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando Autor: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		entity = this.s.persistir(entity);
		response.setData(this.parserToDTO(entity));
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Deletar Por ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> deletar(@PathVariable("id") Long id) {
		
		log.info("Removendo Autor: {}", id);
		Response<String> response = new Response<String>();
		Optional<Autor> entity = this.s.buscarPorId(id);
		
		if (!entity.isPresent()) {
			log.info("Erro ao remover Autor devido ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover Autor. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		this.s.remover(id);
		return ResponseEntity.ok(new Response<String>());		
	}
	
	/**
	 * Valida um funcionário, verificando se ele é existente e válido no
	 * sistema.
	 * 
	 * @param objDTO
	 * @param result
	 */
	/**
	private void validarFuncionario(AutorDTO objDTO, BindingResult result) {
		if (objDTO.getId() == null) {
			result.addError(new ObjectError("funcionario", "Funcionário não informado."));
			return;
		}

		log.info("Validando funcionário id {}: ", objDTO.getId());
		//Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(objDTO.getFuncionarioId());
		//if (!funcionario.isPresent()) {
		//	result.addError(new ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."));
		//}
		return;
	}
	*/
	
	private Autor parserToEntity(AutorDTO dto, BindingResult result) throws ParseException {
		
		Autor entity = new Autor();
		
		if (dto.getId().isPresent()) {		
			entity.setId(dto.getId().get());
		}

		entity.setNome(dto.getNome());
		entity.setEmail(dto.getEmail());
		entity.setDataNascimento(dto.getDataNascimento());
		entity.setPaisOrigem(dto.getPaisOrigem());
		entity.setCpf(dto.getCpf());
		
		if (EnumUtils.isValidEnum(SexoEnum.class, dto.getSexo())) {
			entity.setSexo(SexoEnum.valueOf(dto.getSexo()));
		} else {
			result.addError(new ObjectError("sexo", "Tipo de sexo inválido."));
		}
				
		return entity;
	}
	
	private AutorDTO parserToDTO(Autor entity) {
		AutorDTO dto = new AutorDTO();
		dto.setId(Optional.of(entity.getId()));
		dto.setNome(entity.getNome());
		dto.setSexo(entity.getSexo().toString());
		dto.setEmail(entity.getEmail());
		dto.setDataNascimento(entity.getDataNascimento());
		dto.setPaisOrigem(entity.getPaisOrigem());
		dto.setCpf(entity.getCpf());
		
		return dto;
	}
}

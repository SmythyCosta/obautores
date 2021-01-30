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

import com.example.demo.dto.AutorDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.response.Response;
import com.example.demo.service.imp.ActorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/api/actors")
@CrossOrigin(origins = "*")
@Api(value="Actor API")
public class ActorControlle extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(ActorControlle.class);
	
	@Autowired
	private ActorService actorService;
	
	@Value("${paginacao.quantityPerPage}")
	private int quantityPerPage;
		
	@ApiOperation(value="list Actors with Pagination")
	@GetMapping(value = "/list")
	public ResponseEntity<Response<Page<AutorDTO>>> listActor(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		log.info("listing Actor, page: {}", pag);
		Response<Page<AutorDTO>> response = new Response<Page<AutorDTO>>();
		PageRequest pageRequest = PageRequest.of(pag, this.quantityPerPage, Sort.Direction.ASC, ord);
		Page<AutorDTO> responseAutor = this.actorService.listarTodos(pageRequest);

		response.setData(responseAutor);
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Create new Actor")
	@PostMapping()
	public ResponseEntity<Response<AutorDTO>> createNewActor(@Valid @RequestBody AutorDTO objDTO, BindingResult result) 
			throws BusinessException, ParseException {
		
		log.info("creating new Actor: {}", objDTO.toString());
		Response<AutorDTO> response = new Response<AutorDTO>();

		response.setData(this.actorService.persistir(objDTO, result));
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Search Actor by ID")
	@GetMapping(value = { "/{id}" })
	public ResponseEntity<Response<AutorDTO>> searchActorById(@PathVariable("id") long id) throws NotFoundException {
		
		log.info("Search Actor by ID: {}", id);
		Response<AutorDTO> response = new Response<AutorDTO>();
		response.setData(this.actorService.buscarPorIdWithException(id));
		
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Update Actor by ID")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<AutorDTO>> updateActor(@PathVariable long id, @Valid @RequestBody AutorDTO dto, BindingResult result) 
					throws BusinessException, ParseException {
		
		log.info("Updating Actor: {}", dto.toString());
		Response<AutorDTO> response = new Response<AutorDTO>();
		
		dto.setId(Optional.of(id));
		response.setData(this.actorService.persistir(dto, result)); 
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Delete Actor By ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> deleteActor(@PathVariable("id") Long id) {
		log.info("Deleting Actor: {}", id);
		this.actorService.remover(id);
		return ResponseEntity.ok(new Response<String>());		
	}
	
}

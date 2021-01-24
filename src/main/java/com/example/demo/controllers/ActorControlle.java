package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AutorDTO;
import com.example.demo.models.Autor2;
import com.example.demo.repository.ActorCustomRepository;
import com.example.demo.repository.AutorRepository;
import com.example.demo.repository.IAutor;
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
	
	@Autowired
	ActorCustomRepository actorCustomRepository;
	
	@Autowired
	private AutorRepository repository;
	
	//@Value("${paginacao.quantityPerPage}")
	private int quantityPerPage = 4;
		
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
	
	
	@ApiOperation(value="list Actors with Pagination")
	@GetMapping(value = "/list2")
	public ResponseEntity<Response<Page<IAutor>>> listActor2(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		PageRequest pageRequest = PageRequest.of(pag, this.quantityPerPage, Sort.Direction.ASC, ord);
		
		Response<Page<IAutor>> response = new Response<Page<IAutor>>();
		Page<IAutor> responseAutor = this.repository.list2(pageRequest);

		response.setData(responseAutor);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/list3")
    public ResponseEntity<Response<Page<Autor2>>> listActor3(  
    		@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cpf", required = false) String cpf
    ) {
		
		PageRequest pageRequest = PageRequest.of(pag, 4, Sort.Direction.ASC, ord);
		Response<Page<Autor2>> response = new Response<Page<Autor2>>();		
		
		Page<Autor2> obras = this.actorCustomRepository.filter(pageRequest, id, nome, cpf);
		
		response.setData(obras);
		return ResponseEntity.ok(response);
    }
	
	
	
	
	
	
	/*
	@GetMapping(value = "/list3")
	public ResponseEntity<Response<List<AutorModel>>> listActor3(
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		
		Response<List<AutorModel>> response = new Response<List<AutorModel>>();
		List<AutorModel> responseAutor = this.repository.list3();

		response.setData(responseAutor);
		return ResponseEntity.ok(response);
	}
	*/
	
	
}

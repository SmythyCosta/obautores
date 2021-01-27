package com.example.demo.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.example.demo.dto.ObraRequestDTO;
import com.example.demo.dto.ObraResponseDTO;
import com.example.demo.exception.NotFoundException;
import com.example.demo.models.Autor;
import com.example.demo.models.Obra;
import com.example.demo.response.Response;
import com.example.demo.service.imp.ActorService;
import com.example.demo.service.imp.WorkService;
import com.example.demo.util.DataUtil;
import com.example.demo.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(value="/api/works")
@CrossOrigin(origins = "*")
@Api(value="API ObraControlle")
public class WorkControlle extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(WorkControlle.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private ActorService actorService;
	
	@Value("${paginacao.quantityPerPage}")
	private int qtdPorPagina;
	
	
	
	@ApiOperation(value="list Works")
	@GetMapping(value = "/list")
	public ResponseEntity<Response<Page<ObraResponseDTO>>> listWorks(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		log.info("Work Listing, página: {}", pag);
		
		Response<Page<ObraResponseDTO>> response = new Response<Page<ObraResponseDTO>>();
		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.ASC, ord);
		
		Page<ObraResponseDTO> dto = this.workService.listarTodos(pageRequest);
		
		response.setData(dto);
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="filter Works")
	@GetMapping("/filter")
    public ResponseEntity<Response<Page<ObraResponseDTO>>> filterWork(  
    		@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "descicao", required = false) String descicao
    ) {
		
		log.info("Work Filter => nome: " + nome + " descrição: " + descicao + "");
		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.ASC, ord);
		Response<Page<ObraResponseDTO>> response = new Response<Page<ObraResponseDTO>>();		
		
		Page<ObraResponseDTO> obrasDto = this.workService.filter(pageRequest, nome, descicao);
		
		response.setData(obrasDto);
		return ResponseEntity.ok(response);
    }
	
	@ApiOperation(value="Create new Work")
	@PostMapping()
	public ResponseEntity<Response<ObraResponseDTO>> criarNovaObra(@Valid @RequestBody ObraRequestDTO dto, BindingResult result) throws ParseException  {
		
		log.info("creating new Work: {}", dto.toString());
		Response<ObraResponseDTO> response = new Response<ObraResponseDTO>();
				
		ObraResponseDTO work = this.workService.persistir(dto, result);
		response.setData(work);
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Update Work by Id")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<ObraResponseDTO>> updateWork(@PathVariable long id, @Valid @RequestBody ObraRequestDTO dto, BindingResult result) throws ParseException {
	
		log.info("Updating Work: {}", dto.toString());
		Response<ObraResponseDTO> response = new Response<ObraResponseDTO>();
		
		dto.setId(Optional.of(id));			
		response.setData(this.workService.persistir(dto, result));
		
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Search Work by ID")
	@GetMapping(value = { "/{id}" })
	public ResponseEntity<Response<ObraResponseDTO>> searchWorkById(@PathVariable("id") long id) throws NotFoundException {
		
		log.info("Search Work by ID: {}", id);
		Response<ObraResponseDTO> response = new Response<ObraResponseDTO>();
		response.setData(this.workService.searchByIdWithException(id));
		
		return ResponseEntity.ok(response);
	}
	
	
	@ApiOperation(value="Delete Work By ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> deleteWork(@PathVariable("id") Long id) {
		log.info("Deleting Work: {}", id);
		this.workService.remove(id);
		return ResponseEntity.ok(new Response<String>());		
	}
	
}

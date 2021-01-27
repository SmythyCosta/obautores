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
		
		log.info("Listagem de Obra, página: {}", pag);
		
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
		
		log.info("Filtro de Obra => nome: " + nome + " descrição: " + descicao + "");
		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.ASC, ord);
		Response<Page<ObraResponseDTO>> response = new Response<Page<ObraResponseDTO>>();		
		
		Page<ObraResponseDTO> obrasDto = this.workService.filter(pageRequest, nome, descicao);
		
		response.setData(obrasDto);
		return ResponseEntity.ok(response);
    }
	
	@ApiOperation(value="Criar nova Obra")
	@PostMapping()
	public ResponseEntity<Response<ObraResponseDTO>> criarNovaObra(@Valid @RequestBody ObraRequestDTO dto, BindingResult result) throws ParseException  {
		
		log.info("criando nova abra: {}", dto.toString());
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
	
	
	/**
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
	
	
	
	

	
	
	private Obra parserToEntity(ObraRequestDTO dto) throws ParseException {
		
		Obra entity = new Obra();
		
		if (dto.getId().isPresent()) {		
			entity.setId(dto.getId().get());
		}
		entity.setNome(dto.getNome());
		entity.setDescricao(dto.getDescricao());
		entity.setImagem(dto.getImagem());		
		if (!StringUtil.isNullOrEmpty(dto.getDataPublicacao())) {
			entity.setDataPublicacao(this.dateFormat.parse(dto.getDataPublicacao()));
		}
		if (!StringUtil.isNullOrEmpty(dto.getDataExposicao())) {
			entity.setDataExposicao(this.dateFormat.parse(dto.getDataExposicao()));
		}

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
		if (entity.getDataPublicacao() != null) {
			dto.setDataPublicacao(this.dateFormat.format(entity.getDataPublicacao()));
		}
		if (entity.getDataExposicao() != null) {
			dto.setDataExposicao(this.dateFormat.format(entity.getDataExposicao()));
		}
		dto.getAutor().addAll(entity.getAutor());

		return dto;
	}
	
	private void ValidaObra(ObraRequestDTO dto, BindingResult result) {
		
		boolean checkName = true;
		
		if (dto.getAutorId().isEmpty()) {
			result.addError(new ObjectError("Autor", "Uma Obra deve possuir no mínimo um Autor. "));
		}
		
		if (StringUtil.isNullOrEmpty(dto.getDataPublicacao()) && StringUtil.isNullOrEmpty(dto.getDataExposicao()) ) {
			result.addError(new ObjectError("Datas", "DataPublicacao ou DataExposicao Devem ser preenchida. "));
		}
		
		if (!StringUtil.isNullOrEmpty(dto.getDataPublicacao())) {
			if (!DataUtil.isDateValid(dto.getDataPublicacao())) {
				result.addError(new ObjectError("Datas", "DataPublicacao Inválida. "));
			}
		}
		
		if (!StringUtil.isNullOrEmpty(dto.getDataExposicao())) {
			if (!DataUtil.isDateValid(dto.getDataExposicao())) {
				result.addError(new ObjectError("Datas", "DataExposicao Inválida. "));
			}
		}
		
		// ================================
		// Validando na edição 
		// ================================
		if (dto.getId().isPresent()) {
			Optional<Obra> entity = this.obraService.buscarPorId(dto.getId().get());	
			if (entity.isPresent()) {
				if (entity.get().getNome().equalsIgnoreCase(dto.getNome())) {
					checkName = false;
				}				
			}
		}
		
		if (checkName) {
			this.obraService.buscarPorNome(dto.getNome()).ifPresent(
					e -> result.addError(new ObjectError("Nome", "Nome já existente. "))
			);		
		}
		
		return;
	}
	*/
	
}

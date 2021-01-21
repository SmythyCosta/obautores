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

import com.example.demo.dto.ObraRequestDTO;
import com.example.demo.dto.ObraResponseDTO;
import com.example.demo.models.Autor;
import com.example.demo.models.Obra;
import com.example.demo.response.Response;
import com.example.demo.service.imp.ActorService;
import com.example.demo.service.imp.ObraService;
import com.example.demo.util.DataUtil;
import com.example.demo.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(value="/api/obra")
@CrossOrigin(origins = "*")
@Api(value="API ObraControlle")
public class ObraControlle {
	
	private static final Logger log = LoggerFactory.getLogger(ObraControlle.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private ObraService obraService;
	
	@Autowired
	private ActorService autorService;
	
	@Value("${paginacao.quantityPerPage}")
	private int qtdPorPagina;
	
	
	/**
	@ApiOperation(value="listar Obras")
	@GetMapping(value = "/listarObras")
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
	
	@ApiOperation(value="filtrando Obras")
	@GetMapping("/filtrarObras")
    public ResponseEntity<Response<Page<ObraResponseDTO>>> filtraObra(  
    		@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "descicao", required = false) String descicao
    ) {
		
		log.info("Filtro de Obra => nome: " + nome + " descrição: " + descicao + "");
		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.ASC, ord);
		Response<Page<ObraResponseDTO>> response = new Response<Page<ObraResponseDTO>>();		
		
		Page<Obra> obras = this.obraService.filtar(pageRequest, nome, descicao);
		Page<ObraResponseDTO> dto = obras.map(o -> this.parserToDTO(o));
		
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
	public ResponseEntity<Response<ObraResponseDTO>> criarNovaObra(@Valid @RequestBody ObraRequestDTO dto, BindingResult result) throws ParseException  {
		
		log.info("criando nova abra: {}", dto.toString());
		Response<ObraResponseDTO> response = new Response<ObraResponseDTO>();
		
		ValidaObra(dto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando Obra: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Obra obra = this.parserToEntity(dto);
		
		response.setData(this.parserToDTO(this.obraService.persistir(obra)));
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Alterar Obra por ID")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<ObraResponseDTO>> atualizaObra(@PathVariable long id, @Valid @RequestBody ObraRequestDTO dto, BindingResult result) throws ParseException {
	
		log.info("Atualizando Obra: {}", dto.toString());
		Response<ObraResponseDTO> response = new Response<ObraResponseDTO>();
		
		dto.setId(Optional.of(id));		
		ValidaObra(dto, result);

		if (result.hasErrors()) {
			log.error("Erro validando Obra: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Obra entity = this.parserToEntity(dto);
		
		response.setData(this.parserToDTO(this.obraService.persistir(entity)));
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

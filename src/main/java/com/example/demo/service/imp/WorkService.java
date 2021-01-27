package com.example.demo.service.imp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.example.demo.service.IWorkService;
import com.example.demo.dto.ObraRequestDTO;
import com.example.demo.dto.ObraResponseDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.models.Autor;
import com.example.demo.models.Obra;
import com.example.demo.repository.WorkCustomRepository;
import com.example.demo.repository.WorkRepository;
import com.example.demo.util.DataUtil;
import com.example.demo.util.StringUtil;

@Service
public class WorkService implements IWorkService {
	
	private static final Logger log = LoggerFactory.getLogger(WorkService.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Value("${delimiter}")
    private String delimiter;
	
	@Autowired
	private WorkRepository rep;
	
	@Autowired
	private WorkCustomRepository workCustomRepository;
	
	@Autowired
	private ActorService actorService;

	@Override
	public Page<ObraResponseDTO> listarTodos(PageRequest pageRequest) {
		log.info("Search Works PageRequest{}", pageRequest);

		Page<Obra> o = this.rep.findAll(pageRequest);
		Page<ObraResponseDTO> dto = o.map(a -> this.parserToDTO(a));
		
		return dto;
	}

	@Override
	public ObraResponseDTO persistir(ObraRequestDTO dto, BindingResult result) throws ParseException {
		
		log.info("criando nova abra: {}", dto.toString());
		
		validateWork(dto, result);
		StringBuilder sb = new StringBuilder();
		
		if (result.hasErrors()) {
			log.error("Erro validando Obra: {}", result.getAllErrors());
			result.getAllErrors()
					.forEach(error -> sb.append(error.getDefaultMessage() + this.delimiter));
			throw new BusinessException(sb.toString());
		}
		
		Obra obra = this.parserToEntity(dto, result);
		return this.parserToDTO(this.rep.save(obra));
	
	}

	@Override
	public ObraResponseDTO searchByIdWithException(Long id) {   
		log.info("Buscando um Obra pelo ID {}", id);

		StringBuilder sb = new StringBuilder();
		Optional<Obra> entity = this.rep.findById(id);

		if (!entity.isPresent()) {
			log.info("Obra não encontrado para o ID: {}", id);
			sb.append("Obra não encontrado para o id " + id + this.delimiter);
			throw new NotFoundException(sb.toString());
		}
		
		return this.parserToDTO(entity.get());
	}
	
	@Override
	public Optional<Obra> buscarPorNome(String nome) {
		log.info("Buscando uma Obra pelo nome {}", nome);
		return this.rep.findByNome(nome);
	}

	@Override
	public void remove(Long id) {
		log.info("Removendo a Obra ID {}", id);
		ObraResponseDTO dto = this.searchByIdWithException(id);
		this.rep.deleteById(dto.getId().get());		
	}
	
	public Optional<Obra> searchById(Long id) {
		log.info("Buscando um Obra pelo ID {}", id);
		return this.rep.findById(id);
	}
	
	public Page<ObraResponseDTO> filter(PageRequest pageRequest, String nome, String descricao) {
		
		log.info("Search Work's with filters {}");	
		Page<Obra> woks = this.workCustomRepository.filter(pageRequest, nome, descricao);
		Page<ObraResponseDTO> dto = woks.map(w -> this.parserToDTO(w));
		return dto;
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
	
	private Obra parserToEntity(ObraRequestDTO dto, BindingResult result) throws ParseException {
		
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
			Optional<Autor> out = this.actorService.buscarPorId(autorRequest);
			if (out.isPresent()) {
				entity.getAutor().add(out.get());
			}			
		});
						
		return entity;
	}
	
	private void validateWork(ObraRequestDTO dto, BindingResult result) {
		
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
			Optional<Obra> entity = this.searchById(dto.getId().get());
			if (entity.isPresent()) {
				if (entity.get().getNome().equalsIgnoreCase(dto.getNome())) {
					checkName = false;
				}				
			}
		}
		
		if (checkName) {
			this.buscarPorNome(dto.getNome()).ifPresent(
					e -> result.addError(new ObjectError("Nome", "Nome já existente. "))
			);		
		}
		
	}

}

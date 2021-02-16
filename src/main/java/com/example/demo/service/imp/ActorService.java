package com.example.demo.service.imp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.example.demo.dto.AutorDTO;
import com.example.demo.enums.PaisEnum;
import com.example.demo.enums.SexoEnum;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.models.Autor;
import com.example.demo.models.Obra;
import com.example.demo.repository.AutorRepository;
import com.example.demo.repository.WorkRepository;
import com.example.demo.response.Response;
import com.example.demo.service.IAutorService;
import com.example.demo.util.CpfUtil;
import com.example.demo.util.DataUtil;
import com.example.demo.util.EmailUtil;

@Service
public class ActorService implements IAutorService {
	
	private static final Logger log = LoggerFactory.getLogger(ActorService.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Value("${delimiter}")
    private String delimiter;
	
	@Autowired
	private AutorRepository repository;
	
	@Autowired
	WorkRepository workRepository;
	
	@Override
	public Page<AutorDTO> listarTodos(PageRequest pageRequest) {
		
		log.info("Buscando Autores PageRequest{}", pageRequest);
		
		Page<Autor> autores =  this.repository.findAll(pageRequest);
		Page<AutorDTO> dto = autores.map(a -> this.parserToDTO(a));
		return dto;
	}

	@Override
	public AutorDTO persistir(AutorDTO objDTO, BindingResult result) throws BusinessException, ParseException, NotFoundException {
		log.info("Persistindo AutorDTO: {}", objDTO);
		
		ValidateAutor(objDTO, result);
		StringBuilder sb = new StringBuilder();

		if (result.hasErrors()) {
			log.error("Erro validando Autor: {}", result.getAllErrors());
			result.getAllErrors()
					.forEach(error -> sb.append(error.getDefaultMessage() + this.delimiter));
			throw new BusinessException(sb.toString());
		}
		
		return this.parserToDTO(this.repository.save(this.parserToEntity(objDTO)));
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo o Autor ID {}", id);
		AutorDTO entity = this.buscarPorIdWithException(id);
		this.repository.deleteById(entity.getId().get());
	}

	@Override
	public AutorDTO buscarPorIdWithException(Long id) throws NotFoundException {
		log.info("Buscando um Autor pelo ID {}", id);

		StringBuilder sb = new StringBuilder();
		Optional<Autor> entity = this.repository.findById(id);

		if (!entity.isPresent()) {
			log.info("Autor não encontrado para o ID: {}", id);
			sb.append("Autor não encontrado para o id " + id + this.delimiter);
			throw new NotFoundException(sb.toString());
		}
		
		return this.parserToDTO(entity.get());
	}
	
	public Optional<Autor> buscarPorId(Long id) throws NotFoundException {
		log.info("Buscando um Autor pelo ID {}", id);
		return this.repository.findById(id);
	}
	
	@Override
	public Optional<Autor> buscarPorNome(String nome) {
		log.info("Buscando um Autor pelo nome {}", nome);
		return this.repository.findByNome(nome);
	}
	
	public Optional<Autor> buscarPorCpf(String cpf) {
		log.info("Buscando Autor pelo CPF {}", cpf);
		return Optional.ofNullable(this.repository.findByCpf(cpf));
	}
	
	public Optional<Autor> buscarPorEmail(String email) {
		log.info("Buscando Autor pelo email {}", email);
		return Optional.ofNullable(this.repository.findByEmail(email));
	}

	public List<Autor> buscarAutoresPorObra(Long idObra){
		log.info("Buscando Autores pelo ID Obra {}", idObra);
		
		Optional<Obra> obra = this.workRepository.findById(idObra);
		if (!obra.isPresent()) {
			log.info("Obra não encontrado para o ID: {}", idObra);
			throw new NotFoundException("Obra não encontrado");
		}
		
		return obra.get().getAutor();
	}

	private Autor parserToEntity(AutorDTO dto) throws ParseException {
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

	private void ValidateAutor(AutorDTO objDTO, BindingResult result) throws NotFoundException {
		
		boolean checkEmail = true;
		boolean checkCPF = true;
		boolean checkNome = true;
		
		if (!"".equals(objDTO.getEmail().trim()) && EmailUtil.isValidEmail(objDTO.getEmail())) {
			result.addError(new ObjectError("Email", "Email inválido. "));
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
				
			AutorDTO entity = this.buscarPorIdWithException(objDTO.getId().get());	
			if (entity != null) {
				if (entity.getEmail().equalsIgnoreCase(objDTO.getEmail())) {
					checkEmail = false;
				}				
				if (entity.getCpf().equalsIgnoreCase(objDTO.getCpf())) {
					checkCPF = false;
				}
				if (entity.getNome().equalsIgnoreCase(objDTO.getNome())) {
					checkNome = false;
				}
			}
		}
		
		if (objDTO.getDataNascimento() != null && !DataUtil.isDateValid(objDTO.getDataNascimento())) {
				result.addError(new ObjectError("Datas", "DataNascimento Inválida. "));
		}
				
		if (checkEmail) {
			this.buscarPorEmail(objDTO.getEmail()).ifPresent(
					e -> result.addError(new ObjectError("Email", "Email já existente. "))
			);		
		}
			
		if (checkCPF) {
			this.buscarPorCpf(objDTO.getCpf()).ifPresent(
					f -> result.addError(new ObjectError("CPF", "CPF já existente. "))
			);
		}
		
		if (checkNome) {
			this.buscarPorNome(objDTO.getNome()).ifPresent(
					g -> result.addError(new ObjectError("Nome", "Nome já existente. "))
			);
		}

		if (!EnumUtils.isValidEnum(SexoEnum.class, objDTO.getSexo())) {
			result.addError(new ObjectError("sexo", "Tipo de sexo inválido. "));
		}

	}

}

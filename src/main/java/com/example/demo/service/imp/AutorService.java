package com.example.demo.service.imp;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AutorDTO;
import com.example.demo.models.Autor;
import com.example.demo.repository.AutorRepository;
import com.example.demo.service.IAutorService;

@Service
public class AutorService implements IAutorService<Autor> {
	
	private static final Logger log = LoggerFactory.getLogger(AutorService.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private AutorRepository repository;

	@Override
	public Page<AutorDTO> listarTodos(PageRequest pageRequest) {
		
		log.info("Buscando Autores PageRequest{}", pageRequest);
		
		Page<Autor> autores =  this.repository.findAll(pageRequest);
		Page<AutorDTO> dto = autores.map(a -> this.parserToDTO(a));
		return dto;
	}

	@Override
	public Autor persistir(Autor t) {
		log.info("Persistindo Autor: {}", t);
		return this.repository.save(t);
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo o Autor ID {}", id);
		this.repository.deleteById(id);
		
	}

	@Override
	public Optional<Autor> buscarPorId(Long id) {
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
		return this.repository.findAutoresByIdObra (idObra);
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

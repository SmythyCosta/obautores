package com.example.demo.service.imp;

import java.text.SimpleDateFormat;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ObraResponseDTO;
import com.example.demo.models.Obra;
import com.example.demo.repository.ObraCustomRepository;
import com.example.demo.repository.ObraRepository;
import com.example.demo.response.Response;
import com.example.demo.service.IBaseService;

@Service
public class WorkService implements IBaseService<Obra> {
	
	private static final Logger log = LoggerFactory.getLogger(WorkService.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private ObraRepository rep;
	
	@Autowired
	private ObraCustomRepository obraCustomRepository;

	@Override
	public Page<ObraResponseDTO> listarTodos(PageRequest pageRequest) {
		log.info("Search Works PageRequest{}", pageRequest);

		Page<Obra> o = this.rep.findAll(pageRequest);
		Page<ObraResponseDTO> dto = o.map(a -> this.parserToDTO(a));
		
		return dto;
	}

	@Override
	public Obra persistir(Obra t) {
		log.info("Persistindo Obra: {}", t);
		return this.rep.save(t);
	}

	@Override
	public Optional<Obra> buscarPorId(Long id) {
		log.info("Buscando uma Obra pelo ID {}", id);
		return this.rep.findById(id);
	}
	
	@Override
	public Optional<Obra> buscarPorNome(String nome) {
		log.info("Buscando uma Obra pelo nome {}", nome);
		return this.rep.findByNome(nome);
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo a Obra ID {}", id);
		this.rep.deleteById(id);		
	}
	
	public Page<Obra> filtar(PageRequest pageRequest, String nome, String descricao) {
		log.info("Buscando Obras Com Filtro Customizado{}");
		return this.obraCustomRepository.find(pageRequest, nome, descricao);
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

}

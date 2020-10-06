package com.example.demo.service.imp;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.models.Obra;
import com.example.demo.repository.ObraRepository;
import com.example.demo.service.BaseService;

@Service
public class ObraService implements BaseService<Obra> {
	
	private static final Logger log = LoggerFactory.getLogger(ObraService.class);
	
	@Autowired
	private ObraRepository rep;

	@Override
	public Page<Obra> listarTodos(PageRequest pageRequest) {
		log.info("Buscando Obras PageRequest{}", pageRequest);
		return this.rep.findAll(pageRequest);
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
	public void remover(Long id) {
		log.info("Removendo a Obra ID {}", id);
		this.rep.deleteById(id);		
	}

}

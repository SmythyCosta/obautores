package com.example.demo.service.imp;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.demo.models.Autor;
import com.example.demo.repository.AutorRepository;
import com.example.demo.service.BaseService;

@Service
public class AutorService implements BaseService<Autor> {
	
	private static final Logger log = LoggerFactory.getLogger(AutorService.class);
	
	@Autowired
	private AutorRepository rep;

	@Override
	public Page<Autor> listarTodos(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Autor persistir(Autor t) {
		log.info("Persistindo Autor: {}", t);
		return this.rep.save(t);
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo o Autor ID {}", id);
		this.rep.deleteById(id);
		
	}

	@Override
	public Optional<Autor> buscarPorId(Long id) {
		log.info("Buscando um Autor pelo ID {}", id);
		return this.rep.findById(id);
	}

}
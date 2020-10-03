package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Autor;
import com.example.demo.repository.AutorRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/api/autor")
@CrossOrigin(origins = "*")
@Api(value="API AutorControlle")
public class AutorControlle {

	@Autowired
	private AutorRepository r;
	
	@ApiOperation(value="Listar todos")
	@GetMapping()
	public List<Autor> listarTodos() {
		return r.findAll();
	}
	
	@ApiOperation(value="Listar Por ID")
	@GetMapping(value = { "/{id}" })
	public Optional<Autor> encontrarPorId(@PathVariable long id) {
		return r.findById(id);
	}
	
	@ApiOperation(value="Criar novo")
	@PostMapping()
	public Autor criarNovo(@RequestBody Autor obj) {
		return r.save(obj);
	}
	
	@ApiOperation(value="Alterar Por ID")
	@PutMapping(value = "/{id}")
	public Autor atualiza(@PathVariable long id, @RequestBody @Valid Autor obj) {
		return r.save(obj);
	}
	
	@ApiOperation(value="Deletar Por ID")
	@DeleteMapping(value = "/{id}")
	public void deletar(@PathVariable long id) {
		r.deleteById(id);
	}
	
}

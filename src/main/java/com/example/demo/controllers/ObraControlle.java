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
import com.example.demo.models.Obra;
import com.example.demo.repository.AutorRepository;
import com.example.demo.repository.ObraRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/api/obra")
@CrossOrigin(origins = "*")
@Api(value="API ObraControlle")
public class ObraControlle {

	@Autowired
	private ObraRepository r;
	
	@ApiOperation(value="Listar todos")
	@GetMapping()
	public List<Obra> listarTodos() {
		return r.findAll();
	}
	
	@ApiOperation(value="Listar Por ID")
	@GetMapping(value = { "/{id}" })
	public Optional<Obra> encontrarPorId(@PathVariable long id) {
		return r.findById(id);
	}
	
	@ApiOperation(value="Criar novo")
	@PostMapping()
	public Obra criarNovo(@RequestBody Obra obj) {
		return r.save(obj);
	}
	
	@ApiOperation(value="Alterar Por ID")
	@PutMapping(value = "/{id}")
	public Obra atualiza(@PathVariable long id, @RequestBody @Valid Obra obj) {
		return r.save(obj);
	}
	
	@ApiOperation(value="Deletar Por ID")
	@DeleteMapping(value = "/{id}")
	public void deletar(@PathVariable long id) {
		r.deleteById(id);
	}
	
}

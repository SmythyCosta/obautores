package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.AutorDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mock.ActorMock;
import com.example.demo.models.Autor;
import com.example.demo.repository.AutorRepository;
import com.example.demo.service.imp.ActorService;


@RunWith(SpringRunner.class)
public class ActorServiceTest {
	
	@InjectMocks
	ActorService autorService;
	
	@MockBean
	BindingResult bindingResult;
	
	@Mock
	AutorRepository autorRepository;
	
	@Test
	public void actorPersistir_whenValidInput_thenReturnsOK() throws NotFoundException, BusinessException, ParseException {

		Optional<Autor> actorMock = Optional.ofNullable(ActorMock.buildAutor());
		
		Mockito.when(this.autorRepository.findById(1L)).thenReturn(actorMock);
		Mockito.when(this.autorRepository.save(actorMock.get())).thenReturn(actorMock.get());
				
		AutorDTO dto = autorService.persistir(ActorMock.buildAutorDTO(), bindingResult);
		assertEquals(actorMock.get().getId(), dto.getId().get());
    }


}

package com.example.demo.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.AutorDTO;
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
	
	@Captor
	private ArgumentCaptor<Long> captor;
	
	@Test
	public void actorListarTodos_whenValidInput_thenReturnsOK() throws ParseException {

		PageRequest pageRequest = PageRequest.of(0, 2, Sort.Direction.ASC, "id");
		Page<Autor> mockPage = ActorMock.mockPageAutor();
		
		Mockito.when(this.autorRepository.findAll(pageRequest)).thenReturn(mockPage);

		Page<AutorDTO> responseAutor = autorService.listarTodos(pageRequest);
		assertTrue(responseAutor.getContent().size() > 0);
    }
	
	@Test
	public void actorPersistir_whenValidInput_thenReturnsOK() throws ParseException {

		Optional<Autor> actorMock = Optional.ofNullable(ActorMock.buildAutorNational());
		
		Mockito.when(this.autorRepository.findById(1L)).thenReturn(actorMock);
		Mockito.when(this.autorRepository.save(actorMock.get())).thenReturn(actorMock.get());
				
		AutorDTO dto = autorService.persistir(ActorMock.buildAutorDTO(), bindingResult);
		assertEquals(actorMock.get().getId(), dto.getId().get());
    }
	
	@Test
	public void actorRemove_whenValidInput_thenReturnsOK() throws ParseException {

		Optional<Autor> actorMock = Optional.ofNullable(ActorMock.buildAutorNational());
		Mockito.when(this.autorRepository.findById(111L)).thenReturn(actorMock);
		
		autorService.remover(111L);
		
		Mockito.verify(autorRepository).deleteById(captor.capture());
		Long id = captor.getValue();
		
		assertNotNull(id);
    }

}

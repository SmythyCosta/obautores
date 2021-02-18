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
import com.example.demo.exception.BusinessException;
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

	public static final String ACTOR_NAME = "ROMARIO C SOUSA";
	public static final String ACTOR_CPF = "12334565433";
	
	// TODO: List Data
	
	@Test
	public void actorListarTodos_whenValidInput_thenReturnsOK() throws ParseException {

		PageRequest pageRequest = PageRequest.of(0, 2, Sort.Direction.ASC, "id");
		Page<Autor> mockPage = ActorMock.mockPageAutor();
		
		Mockito.when(this.autorRepository.findAll(pageRequest)).thenReturn(mockPage);

		Page<AutorDTO> responseAutor = autorService.listarTodos(pageRequest);
		assertTrue(responseAutor.getContent().size() > 0);
    }
	
	// TODO: Persiste Data
	
	@Test
	public void actorPersistir_whenValidInput_thenInsertActor() throws ParseException {
		
		/**
		 *   isNational
		 * */
		Optional<Autor> actorMock = Optional.ofNullable(ActorMock.buildAutorNational());
		Autor act = actorMock.get();
		
		Mockito.when(this.autorRepository.save(new Autor())).thenReturn(act);
				
		AutorDTO dto = autorService.persistir(ActorMock.buildAutorDTONationalIn(), bindingResult);
		assertEquals(actorMock.get().getId(), dto.getId().get());
		
		/**
		 *   isForeign
		 * */
		Optional<Autor> actorMock2 = Optional.ofNullable(ActorMock.buildAutorForeign());
		Autor act2 = actorMock2.get();
		
		Mockito.when(this.autorRepository.save(new Autor())).thenReturn(act2);
				
		AutorDTO dto2 = autorService.persistir(ActorMock.buildAutorDTOFreignIn(), bindingResult);
		assertEquals(actorMock2.get().getId(), dto2.getId().get());
    }
	
	@Test
	public void actorPersistir_whenValidInput_thenUpdateActor() throws ParseException {

		Optional<Autor> actorMock = Optional.ofNullable(ActorMock.buildAutorNational());
		
		Mockito.when(this.autorRepository.findById(1L)).thenReturn(actorMock);
		Mockito.when(this.autorRepository.save(actorMock.get())).thenReturn(actorMock.get());
				
		AutorDTO dto = autorService.persistir(ActorMock.buildAutorDTONationalOut(), bindingResult);
		assertEquals(actorMock.get().getId(), dto.getId().get());
    }
	
	@Test(expected = BusinessException.class)
	public void actorPersistir_whenInvalidInput_businessExceptionEmailCpf() throws BusinessException, ParseException {
		
		Mockito.lenient().when(bindingResult.hasErrors()).thenReturn(true);
		
		AutorDTO act = ActorMock.buildAutorDTONationalIn();
		act.setEmail("hhhbr11111111111111111");
		act.setCpf("123456789");
				
		autorService.persistir(act, bindingResult);		
    }
	
	// TODO: Delete Data
	
	@Test
	public void actorRemove_whenValidInput_thenReturnsOK() throws ParseException {

		Optional<Autor> actorMock = Optional.ofNullable(ActorMock.buildAutorNational());
		Mockito.when(this.autorRepository.findById(111L)).thenReturn(actorMock);
		
		autorService.remover(111L);
		
		Mockito.verify(autorRepository).deleteById(captor.capture());
		Long id = captor.getValue();
		
		assertNotNull(id);
    }

	@Test
	public void actorBuscarPorId_whenValidInput_thenReturnsOK() throws ParseException {
		Optional<Autor> actorMock = Optional.ofNullable(ActorMock.buildAutorNational());
		Mockito.when(this.autorRepository.findById(1L)).thenReturn(actorMock);

		Optional<Autor> act = autorService.buscarPorId(1L);
		assertNotNull(act.get().getId());
	}

	@Test
	public void actorBuscarPorNome_whenValidInput_thenReturnsOK() throws ParseException {
		Optional<Autor> actorMock = Optional.ofNullable(ActorMock.buildAutorNational());
		Mockito.when(this.autorRepository.findByNome(ACTOR_NAME)).thenReturn(actorMock);

		Optional<Autor> act = autorService.buscarPorNome (ACTOR_NAME);
		assertNotNull(act.get().getId());
	}

	@Test
	public void actorBuscarPorCPF_whenValidInput_thenReturnsOK() throws ParseException {
		Autor actorMock = ActorMock.buildAutorNational();
		Mockito.when(this.autorRepository.findByCpf(ACTOR_CPF)).thenReturn(actorMock);

		Optional<Autor> act = autorService.buscarPorCpf(ACTOR_CPF);
		assertNotNull(act.get().getId());
	}

}

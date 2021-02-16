package com.example.demo.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.demo.dto.AutorDTO;
import com.example.demo.enums.SexoEnum;
import com.example.demo.models.Autor;

public class ActorMock {
	
	public static Autor buildAutorNational() throws ParseException {
		Autor actor = new Autor();
		actor.setId(1L);
		actor.setNome("ROMARIO C SOUSA");
		actor.setSexo(SexoEnum.MASCULINO);
		actor.setEmail("rnkksd@cc.bb");
		actor.setDataNascimento(mockDate());
		actor.setPaisOrigem("BRASIL");
		actor.setCpf("841.104.490-49");
		
		return actor;
    }
	
	public static Autor buildAutorForeign() throws ParseException {
		Autor actor = new Autor();
		actor.setId(1L);
		actor.setNome("Corey Todd Taylor");
		actor.setSexo(SexoEnum.MASCULINO);
		actor.setEmail("ctt@spkt.ss");
		actor.setDataNascimento(mockDate());
		actor.setPaisOrigem("EUA");
		actor.setCpf("");
		
		return actor;
    }
	
	public static AutorDTO buildAutorDTONationalIn() throws ParseException {
		AutorDTO dto = new AutorDTO();
		dto.setNome("ROMARIO C SOUSA");
		dto.setSexo("MASCULINO");
		dto.setEmail("rnkksd@cc.bb");
		dto.setDataNascimento("2019-04-24 01:06:00");
		dto.setPaisOrigem("BRASIL");
		dto.setCpf("841.104.490-49");
		
		return dto;
    }
	
	public static AutorDTO buildAutorDTONationalOut() throws ParseException {
		AutorDTO dto = buildAutorDTONationalIn();
		dto.setId(Optional.of(1L));
		return dto;
    }
	
	public static AutorDTO buildAutorDTOFreignIn() throws ParseException {
		AutorDTO dto = new AutorDTO();
		dto.setNome("Corey Todd Taylor");
		dto.setSexo("MASCULINO");
		dto.setEmail("ctt@spkt.ss");
		dto.setDataNascimento("2019-04-24 01:06:00");
		dto.setPaisOrigem("EUA");
		
		return dto;
    }
	
	public static AutorDTO buildAutorDTOFreignOut() throws ParseException {
		AutorDTO dto = buildAutorDTONationalIn();
		dto.setId(Optional.of(1L));
		return dto;
    }
	
	private static Date mockDate() throws ParseException {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd"); 
		Date data = formato.parse("1980/11/23");
		return data;
	}
	
	public static Page<Autor> mockPageAutor() throws ParseException {
		
		PageRequest pageRequest = PageRequest.of(0, 2, Sort.Direction.ASC, "id");
		Autor mockOne = buildAutorNational();
		Autor mockTwo = buildAutorForeign();
		
		List<Autor>  mockListActor = new ArrayList<>();
		mockListActor.add(mockOne);
		mockListActor.add(mockTwo);
				
		return new PageImpl<>(mockListActor, pageRequest, 1);
	}

}

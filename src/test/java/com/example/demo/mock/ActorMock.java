package com.example.demo.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.example.demo.dto.AutorDTO;
import com.example.demo.enums.SexoEnum;
import com.example.demo.models.Autor;

public class ActorMock {
	
	public static Autor buildAutor() throws ParseException {
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
	
	public static AutorDTO buildAutorDTO() throws ParseException {
		AutorDTO dto = new AutorDTO();
		dto.setId(Optional.of(1L));
		dto.setNome("ROMARIO C SOUSA");
		dto.setSexo("MASCULINO");
		dto.setEmail("rnkksd@cc.bb");
		dto.setDataNascimento("2019-04-24 01:06:00");
		dto.setPaisOrigem("BRASIL");
		dto.setCpf("841.104.490-49");
		
		return dto;
    }
	
	private static Date mockDate() throws ParseException {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd"); 
		Date data = formato.parse("1980/11/23");
		return data;
	}

}

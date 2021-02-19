package com.example.demo.mock;

import com.example.demo.dto.AutorDTO;
import com.example.demo.enums.SexoEnum;
import com.example.demo.models.Autor;
import com.example.demo.models.Obra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class WorkMock {
	
	public static com.example.demo.models.Obra buildWork() throws ParseException {
		Obra o = new Obra(1L, "Work_Test", "Describe_Test", "Image", null, null);
		o.setAutor(ActorMock.buildListAutor());
		return o;
	}

}

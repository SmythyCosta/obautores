package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.demo.exception.BusinessException;

import com.example.demo.response.ErrorMessege;

public class BaseController {

	ErrorMessege errorMessege = new ErrorMessege();

    @ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handlerBusinessException(BusinessException e) {

		if(checkDelimiter(e.getMessage())) {
			long qtd = countErros(e.getMessage());
			processList(e.getMessage(), qtd);
		}

		this.errorMessege.setTimestamp(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessege);
	}

	private boolean checkDelimiter(String erros) {
		String delimiter = ";";
		if (erros != null) {
			return erros.contains(delimiter.toLowerCase());
		}
		return false;
	}

	private long countErros(String erros) {
		char delimiter = ';';
		long count = erros.chars().filter(ch -> ch == delimiter).count();
		return count;
	}

	private void processList(String message, long qtd) {
		String listErros[] = message.split(";");
		for (int i = 0; i < qtd; i++) {
			this.errorMessege.getErrors().add(listErros[i]);
		}
	}

}

package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.demo.exception.BusinessException;

import com.example.demo.response.ErrorMessegeResponse;

public class BaseController {

	ErrorMessegeResponse errorMessegeRespose = new ErrorMessegeResponse();

    @ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handlerBusinessException(BusinessException e) {
		
		String errorMessegesFull = e.getMessage();
		emptyErrorMessegeRespose();

		if(checkDelimiter(errorMessegesFull)) {
			long qtd = countErros(errorMessegesFull);
			processErrorMessegeResponse(errorMessegesFull, qtd);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessegeRespose);
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

	private void processErrorMessegeResponse(String message, long qtd) {
		String listErros[] = message.split(";");
		for (int i = 0; i < qtd; i++) {
			this.errorMessegeRespose.getErrors().add(listErros[i]);
		}

		this.errorMessegeRespose.setTimestamp(LocalDateTime.now());
	}

	private void emptyErrorMessegeRespose() {
		this.errorMessegeRespose.getErrors().clear();
	}

}

package com.example.demo.exception.impl;

import java.time.LocalDateTime;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.IHandlerBusinessException;
import com.example.demo.response.ErrorMessegeResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HandlerBusinessException implements IHandlerBusinessException {

    ErrorMessegeResponse errorMessegeRespose = new ErrorMessegeResponse();

	@Value("${delimiter}")
    private String delimiter;

	public ResponseEntity<?> handle(BusinessException e) {
		
		String errorMessegesFull = e.getMessage();
		emptyErrorMessegeRespose();

		if(checkDelimiter(errorMessegesFull)) {
			long qtd = countErros(errorMessegesFull);
			processErrorMessegeResponse(errorMessegesFull, qtd);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.errorMessegeRespose);
    }
    
    private boolean checkDelimiter(String erros) {
		if (erros != null) {
			return erros.contains(this.delimiter.toLowerCase());
		}
		return false;
	}

	private long countErros(String erros) {
		long count = erros.chars().filter(ch -> ch == this.delimiter.charAt(0)).count();
		return count;
	}

	private void processErrorMessegeResponse(String message, long qtd) {
		String listErros[] = message.split(this.delimiter);
		for (int i = 0; i < qtd; i++) {
			this.errorMessegeRespose.getErrors().add(listErros[i]);
		}

		this.errorMessegeRespose.setTimestamp(LocalDateTime.now());
	}

	private void emptyErrorMessegeRespose() {
		this.errorMessegeRespose.getErrors().clear();
	}
    
}

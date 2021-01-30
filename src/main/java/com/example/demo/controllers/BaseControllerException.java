package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.IHandlerBusinessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.response.ErrorMessegeResponse;

public class BaseControllerException {
	
	@Autowired
	IHandlerBusinessException handlerBusinessException;
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handlerBusinessException(BusinessException e) {
		return handlerBusinessException.handle(e);
	}

	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handlerNotFoundException(NotFoundException e) {
		ErrorMessegeResponse errorMessege = setErrorMessegeResponse(e.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(errorMessege);
	}
	
	private ErrorMessegeResponse setErrorMessegeResponse(String message) {
		ErrorMessegeResponse errorMessege = new ErrorMessegeResponse();
		errorMessege.setTimestamp(LocalDateTime.now());
		errorMessege.getErrors().add(message);
		return errorMessege;
	}
	
}

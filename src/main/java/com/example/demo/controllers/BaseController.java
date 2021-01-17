package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.demo.exception.BusinessException;

import com.example.demo.response.ErrorMessege;

public class BaseController {

    @ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handlerBusinessException(BusinessException e) {
		ErrorMessege errorMessege = ErrorMessege.builder()
				.dateAndTime(LocalDateTime.now())
				.menssage(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(errorMessege);
	}

}

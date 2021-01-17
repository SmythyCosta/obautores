package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.IExceptionHandlerBusinessException;

@Controller
public class BaseController {

	@Autowired
	IExceptionHandlerBusinessException exceptionHandlerBusinessException;

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handlerBusinessException(BusinessException businessException ) {
		return exceptionHandlerBusinessException.handlerBusinessException(businessException);
    };

}

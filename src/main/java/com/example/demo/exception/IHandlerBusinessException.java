package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface IHandlerBusinessException {

    ResponseEntity<?> handle(BusinessException e);

}

package com.munioz.mark.practice.infrastructure.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.munioz.mark.practice.application.dto.Result;
import com.munioz.mark.practice.domain.exceptions.DomainException;

import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class ExceptionHandlerConfig {

	@ExceptionHandler({ Exception.class })
    public ResponseEntity<Result<Object>> handleGenericException(Exception ex) {
		return ResponseEntity.internalServerError().body(Result.error(ex.getMessage()));
    }
	
	@ExceptionHandler({ DomainException.class })
    public ResponseEntity<Result<Object>> handleDomainException(DomainException ex) {
		return ResponseEntity.ok(Result.error(ex.getMessage()));
    }

}

package com.skilladmin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(Map.of("msg", ex.getMessage(),"status",HttpStatus.INTERNAL_SERVER_ERROR));

	}

	@ExceptionHandler(RuntimeException.class)
	public  ResponseEntity<Object> handle(RuntimeException e){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
				body(Map.of("msg", e.getMessage(),"status",HttpStatus.INTERNAL_SERVER_ERROR));

	}
	
}

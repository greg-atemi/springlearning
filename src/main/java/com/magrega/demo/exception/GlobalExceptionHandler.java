package com.magrega.demo.exception;

import com.magrega.demo.dto.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
          EmailAlreadyExistsException ex) {

    ErrorResponse error = new ErrorResponse(
            "EMAIL_ALREADY_EXISTS",
            ex.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
  }
}
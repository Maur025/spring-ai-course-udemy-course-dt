package com.maur025.medassistant.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(RestClientException.class)
  public ResponseEntity<String> handleResClientException(RestClientException ex) {
    log.warn("RestClientException occurred: {}", ex.getMessage());

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
      .body("External service is unavailable. Please try again later.");
  }
}

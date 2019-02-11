package com.miller.smartscheduler.controller;

import static com.miller.smartscheduler.error.CustomErrorCode.ENTITY_VALIDATION_ERROR;
import static com.miller.smartscheduler.error.CustomErrorCode.JWT_ERROR;

import com.miller.smartscheduler.error.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> validationError(MethodArgumentNotValidException ex) {

    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .findFirst()
        .orElse(ex.getMessage());

    log.error(errorMessage);

    return new ResponseEntity<>(new ErrorResponse(errorMessage, ENTITY_VALIDATION_ERROR), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SignatureException.class)
  public ResponseEntity<ErrorResponse> tokenSignatureError(SignatureException ex) {
    log.error(ex.getMessage());

    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), JWT_ERROR), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MalformedJwtException.class)
  public ResponseEntity<ErrorResponse> invalidJwtToken(MalformedJwtException ex) {
    log.error(ex.getMessage());

    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), JWT_ERROR), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ErrorResponse> tokenExpired(ExpiredJwtException ex) {
    log.error(ex.getMessage());

    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), JWT_ERROR), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UnsupportedJwtException.class)
  public ResponseEntity<ErrorResponse> unsupportedToken(UnsupportedJwtException ex) {
    log.error(ex.getMessage());

    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), JWT_ERROR), HttpStatus.UNAUTHORIZED);
  }
}

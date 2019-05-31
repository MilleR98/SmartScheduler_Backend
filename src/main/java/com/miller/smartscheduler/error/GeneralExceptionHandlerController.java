package com.miller.smartscheduler.error;

import static com.miller.smartscheduler.error.CustomErrorCode.BAD_REQUEST;
import static com.miller.smartscheduler.error.CustomErrorCode.ENTITY_NOT_FOUND;
import static com.miller.smartscheduler.error.CustomErrorCode.ENTITY_VALIDATION_ERROR;
import static com.miller.smartscheduler.error.CustomErrorCode.EVENT_TIME_CONFLICT;

import com.miller.smartscheduler.error.exception.BadRequestException;
import com.miller.smartscheduler.error.exception.ContentNotFoundException;
import com.miller.smartscheduler.error.exception.EventTimeConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandlerController {

  @ExceptionHandler(EventTimeConflictException.class)
  public ResponseEntity<ErrorResponse> eventTimeConflicts(EventTimeConflictException ex) {

    log.error(ex.getMessage());

    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), EVENT_TIME_CONFLICT), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ContentNotFoundException.class)
  public ResponseEntity<ErrorResponse> contentNotFound(ContentNotFoundException ex) {

    log.error(ex.getMessage());

    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), ENTITY_NOT_FOUND), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> badRequest(BadRequestException ex) {

    log.error(ex.getMessage());

    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), BAD_REQUEST), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> validationError(MethodArgumentNotValidException ex) {

    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .findFirst()
        .orElse(ex.getMessage());

    log.error(errorMessage);

    return new ResponseEntity<>(new ErrorResponse(errorMessage, ENTITY_VALIDATION_ERROR), HttpStatus.BAD_REQUEST);
  }
}

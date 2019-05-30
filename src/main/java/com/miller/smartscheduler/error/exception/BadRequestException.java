package com.miller.smartscheduler.error.exception;

public class BadRequestException extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException() {
  }

}

package com.miller.smartscheduler.error.exception;

public class EventTimeConflictException extends RuntimeException {

  public EventTimeConflictException(String message) {
    super(message);
  }

  public EventTimeConflictException() {
  }
}


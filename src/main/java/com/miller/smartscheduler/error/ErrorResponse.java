package com.miller.smartscheduler.error;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResponse {

  private final String errorMessage;
  private final CustomErrorCode errorCode;
}

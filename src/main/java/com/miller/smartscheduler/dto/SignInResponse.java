package com.miller.smartscheduler.dto;

import lombok.Data;

@Data
public class SignInResponse {

  private UserDetails userDetails;
  private TokenPair tokenPair;
}

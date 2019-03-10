package com.miller.smartscheduler.security.dto;

import lombok.Data;

@Data
public class SignInResponse {

  private UserDetails userDetails;
  private TokenPair tokenPair;
}

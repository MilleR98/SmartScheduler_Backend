package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.UserType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomJwtClaims {

  private final String userId;
  private final UserType userType;
}

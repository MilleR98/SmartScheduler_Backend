package com.miller.smartscheduler.security.service;

import com.miller.smartscheduler.security.model.CustomJwtClaims;

public interface TokenValidator {

  CustomJwtClaims validateJwt(String authToken);
}

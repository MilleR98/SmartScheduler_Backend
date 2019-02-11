package com.miller.smartscheduler.security;

import com.miller.smartscheduler.model.CustomJwtClaims;

public interface TokenValidator {

  CustomJwtClaims validateJwt(String authToken);
}

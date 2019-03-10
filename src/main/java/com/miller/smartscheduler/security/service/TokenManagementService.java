package com.miller.smartscheduler.security.service;

import com.miller.smartscheduler.security.dto.TokenPair;
import com.miller.smartscheduler.security.model.CustomJwtClaims;

public interface TokenManagementService {

  TokenPair generateTokenPair(CustomJwtClaims customJwtClaims);

  TokenPair refreshToken(TokenPair tokenPair);

  void logoutToken(TokenPair jwtDto);
}

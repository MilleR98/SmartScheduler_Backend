package com.miller.smartscheduler.service;

import com.miller.smartscheduler.dto.TokenPair;
import com.miller.smartscheduler.model.CustomJwtClaims;

public interface TokenManagementService {

  TokenPair generateTokenPair(CustomJwtClaims customJwtClaims);

  TokenPair refreshToken(TokenPair tokenPair);

  void logoutToken(TokenPair jwtDto);
}

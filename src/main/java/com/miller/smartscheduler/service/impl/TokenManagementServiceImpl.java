package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.dto.TokenPair;
import com.miller.smartscheduler.model.CustomJwtClaims;
import com.miller.smartscheduler.repository.TokenRepository;
import com.miller.smartscheduler.security.TokenValidator;
import com.miller.smartscheduler.service.TokenManagementService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenManagementServiceImpl implements TokenManagementService {

  private final TokenRepository tokenRepository;
  private final TokenValidator tokenValidator;
  @Value("${jwt.secret-key}")
  private String secretKey;
  @Value("${jwt.expiration-time}")
  private Long expirationTime;

  @Override
  public TokenPair generateTokenPair(CustomJwtClaims customJwtClaims) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationTime);

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] apiKeySecretBytes = Base64.getEncoder().encode(secretKey.getBytes());
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    String token = Jwts.builder().setIssuedAt(new Date()).setExpiration(expiryDate)
        .signWith(signatureAlgorithm, signingKey).claim("userClaims", customJwtClaims).compact();

    String refreshToken = RandomStringUtils.random(20, true, true);

    tokenRepository.putTokenPair(refreshToken, token);

    return new TokenPair(token, refreshToken);
  }

  @Override
  public TokenPair refreshToken(TokenPair tokenPair) {
    CustomJwtClaims authorizedUserClaims = tokenValidator.validateJwt(tokenPair.getAuthToken());

    validateRequestedJwtPair(tokenPair);

    TokenPair newTokenPair = generateTokenPair(authorizedUserClaims);
    tokenRepository.putTokenPair(newTokenPair.getRefreshToken(), newTokenPair.getAuthToken());

    return newTokenPair;
  }

  private void validateRequestedJwtPair(TokenPair tokenPair) {
    Optional<TokenPair> tokenPairOptional = tokenRepository.getTokenPair(tokenPair.getRefreshToken());
    tokenPairOptional.orElseThrow(() -> new MalformedJwtException("Invalid refresh token"));

    if (!tokenPairOptional.get().getAuthToken().equals(tokenPair.getAuthToken())) {

      throw new MalformedJwtException("Invalid token with this refresh token");
    }
  }

  @Override
  public void logoutToken(TokenPair tokenPair) {
    tokenValidator.validateJwt(tokenPair.getAuthToken());

    tokenRepository.putLogoutToken(tokenPair.getAuthToken());
  }
}

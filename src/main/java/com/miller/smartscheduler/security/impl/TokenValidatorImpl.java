package com.miller.smartscheduler.security.impl;

import com.miller.smartscheduler.model.CustomJwtClaims;
import com.miller.smartscheduler.model.type.UserType;
import com.miller.smartscheduler.repository.TokenRepository;
import com.miller.smartscheduler.security.TokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import java.util.Base64;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenValidatorImpl implements TokenValidator {

  private final TokenRepository logoutTokenRepository;
  @Value("${jwt.secret-key}")
  private String jwtSecretKey;

  @Override
  public CustomJwtClaims validateJwt(String authToken) {

    logoutTokenRepository.getLogoutToken(authToken).ifPresent(token -> {
      throw new MalformedJwtException("Token has been logout");
    });

    Jws<Claims> claimsJws = Jwts.parser()
        .setSigningKey(Base64.getEncoder().encode(jwtSecretKey.getBytes()))
        .parseClaimsJws(authToken);

    LinkedHashMap userClaims = ((LinkedHashMap) claimsJws.getBody().get("userClaims"));
    UserType userType = UserType.valueOf((String) userClaims.get("userType"));
    String userId = userClaims.get("userId").toString();

    return new CustomJwtClaims(userId, userType);
  }
}

package com.miller.smartscheduler.security.repository;

import com.miller.smartscheduler.security.dto.TokenPair;
import com.miller.smartscheduler.security.model.TokenWrapper;
import java.util.Optional;

public interface TokenRepository {

  Optional<TokenPair> getTokenPair(String refresh);

  Optional<TokenWrapper> getLogoutToken(String token);

  Optional<TokenPair> putTokenPair(String token, String refresh);

  Optional<TokenWrapper> putLogoutToken(String token);
}

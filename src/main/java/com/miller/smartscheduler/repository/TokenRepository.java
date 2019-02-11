package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.dto.TokenPair;
import com.miller.smartscheduler.model.TokenWrapper;
import java.util.Optional;

public interface TokenRepository {

  Optional<TokenPair> getTokenPair(String refresh);

  Optional<TokenWrapper> getLogoutToken(String token);

  Optional<TokenPair> putTokenPair(String token, String refresh);

  Optional<TokenWrapper> putLogoutToken(String token);
}

package com.miller.smartscheduler.repository.impl;

import static com.miller.smartscheduler.util.Constants.LOGOUT_TOKENS_CACHE_NAME;
import static com.miller.smartscheduler.util.Constants.REFRESH_TOKENS_CACHE_NAME;

import com.miller.smartscheduler.dto.TokenPair;
import com.miller.smartscheduler.model.TokenWrapper;
import com.miller.smartscheduler.repository.TokenRepository;
import java.util.Optional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * This Class is an Implementation class for LogoutTokenRepository Interface.
 *
 * @author Milpitas Communications.
 */
@Repository
public class TokenRepositoryImpl implements TokenRepository {

  @Cacheable(key = "#refresh", cacheNames = REFRESH_TOKENS_CACHE_NAME)
  @Override
  public Optional<TokenPair> getTokenPair(String refresh) {

    return Optional.empty();
  }

  @CachePut(key = "#refresh", cacheNames = REFRESH_TOKENS_CACHE_NAME)
  @Override
  public Optional<TokenPair> putTokenPair(String token, String refresh) {

    return Optional.of(new TokenPair(token, refresh));
  }

  @Cacheable(key = "#token", cacheNames = LOGOUT_TOKENS_CACHE_NAME)
  @Override
  public Optional<TokenWrapper> getLogoutToken(String token) {

    return Optional.empty();
  }

  @CachePut(key = "#token", cacheNames = LOGOUT_TOKENS_CACHE_NAME)
  @Override
  public Optional<TokenWrapper> putLogoutToken(String token) {

    return Optional.of(new TokenWrapper(token));
  }
}

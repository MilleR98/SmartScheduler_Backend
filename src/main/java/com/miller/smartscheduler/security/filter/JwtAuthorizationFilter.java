package com.miller.smartscheduler.security.filter;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.miller.smartscheduler.security.model.CustomJwtClaims;
import com.miller.smartscheduler.security.service.TokenValidator;
import com.miller.smartscheduler.security.MutableHttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private static final String AUTH_HEADER_NAME = "Authorization";
  private static final String AUTH_HEADER_PREFIX = "Bearer ";
  private final TokenValidator tokenValidator;

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    log.debug("Request: {}", httpServletRequest.getRequestURI());

    String headerValue = httpServletRequest.getHeader(AUTH_HEADER_NAME);

    if (nonNull(headerValue) && headerValue.startsWith(AUTH_HEADER_PREFIX)) {

      Optional<CustomJwtClaims> customJwtClaims = validateToken(httpServletRequest, headerValue);
      if (customJwtClaims.isPresent()) {

        MutableHttpServletRequest requestWrapper = new MutableHttpServletRequest(httpServletRequest);
        requestWrapper.putHeader("userId", customJwtClaims.get().getUserId());

        httpServletRequest = requestWrapper;
      }
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  private Optional<CustomJwtClaims> validateToken(HttpServletRequest req, String header) {
    String token = header.replace(AUTH_HEADER_PREFIX, "");

    CustomJwtClaims customJwtClaims = tokenValidator.validateJwt(token);

    if (isNull(customJwtClaims)) {

      return Optional.empty();
    }

    return Optional.of(processSuccessTokenValidation(req, customJwtClaims));
  }

  private CustomJwtClaims processSuccessTokenValidation(HttpServletRequest req, CustomJwtClaims customJwtClaims) {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    authorities.add(new SimpleGrantedAuthority(customJwtClaims.getUserType().getFullName()));

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null, authorities);
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return customJwtClaims;
  }
}

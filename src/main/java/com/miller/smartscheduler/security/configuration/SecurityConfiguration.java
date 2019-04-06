package com.miller.smartscheduler.security.configuration;

import com.miller.smartscheduler.security.filter.JwtAuthorizationFilter;
import com.miller.smartscheduler.security.service.TokenValidator;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final TokenValidator tokenValidator;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .cors().and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
        .antMatchers(HttpMethod.POST, "/error/report").permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(getAuthFilter(), BasicAuthenticationFilter.class)
        .headers().frameOptions().disable();
  }

  @Bean
  public OncePerRequestFilter getAuthFilter() {

    return new JwtAuthorizationFilter(tokenValidator);
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addExposedHeader("Authorization");
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("DELETE");
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }
}

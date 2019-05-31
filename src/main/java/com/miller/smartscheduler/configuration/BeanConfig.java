package com.miller.smartscheduler.configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {

    return builder.build();
  }

  @Bean
  public ScheduledExecutorService getNotificationScheduledExecutorService() {

    return Executors.newScheduledThreadPool(15);
  }
}

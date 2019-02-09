package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.User;
import com.miller.smartscheduler.repository.UserRepository;
import com.miller.smartscheduler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User findByEmail(String email) {

    return userRepository.findByEmail(email);
  }
}

package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.User;
import com.miller.smartscheduler.repository.UserRepository;
import com.miller.smartscheduler.service.UserService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CommonServiceImpl<User> implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    super(userRepository);
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> findByEmail(String email) {

    return Optional.ofNullable(userRepository.findByEmail(email));
  }
}

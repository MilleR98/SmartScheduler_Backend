package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.User;
import java.util.Optional;

public interface UserService extends CommonService<User> {

  Optional<User> findByEmail(String email);
}

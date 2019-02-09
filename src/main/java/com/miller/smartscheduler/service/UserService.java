package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.User;

public interface UserService {

  User findByEmail(String email);
}

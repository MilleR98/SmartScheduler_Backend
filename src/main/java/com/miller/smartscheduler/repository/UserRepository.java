package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  User findByEmail(String email);
}

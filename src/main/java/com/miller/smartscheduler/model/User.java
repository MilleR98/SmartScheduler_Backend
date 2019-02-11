package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.UserType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

  @Id
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private UserType userType;
}

package com.miller.smartscheduler.security.dto;

import static com.miller.smartscheduler.util.Constants.EMAIL_REGEX;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignUpDTO {

  @NotBlank(message = "FirstName cannot be empty")
  private String firstName;

  @NotBlank(message = "LastName cannot be empty")
  private String lastName;

  @NotBlank(message = "Phone number cannot be empty")
  private String phoneNumber;

  @NotBlank(message = "Email Cannot be blank")
  @Pattern(regexp = EMAIL_REGEX, message = "Should be valid email format")
  private String email;

  @NotBlank(message = "Password cannot be blank")
  private String password;

  @NotBlank(message = "Repeat password cannot be blank")
  private String repeatPassword;
}

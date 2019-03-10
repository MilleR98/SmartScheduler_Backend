package com.miller.smartscheduler.security.dto;

import static com.miller.smartscheduler.util.Constants.EMAIL_REGEX;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignInDTO {

  @NotBlank(message = "Email Cannot be blank")
  @Pattern(regexp = EMAIL_REGEX, message = "Should be valid email format")
  private String email;

  @NotBlank(message = "Password cannot be blank")
  private String password;
}

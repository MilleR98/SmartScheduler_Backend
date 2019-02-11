package com.miller.smartscheduler.security.impl;

import static java.lang.String.format;

import com.miller.smartscheduler.dto.SignInDTO;
import com.miller.smartscheduler.dto.SignInResponse;
import com.miller.smartscheduler.dto.SignUpDTO;
import com.miller.smartscheduler.dto.TokenPair;
import com.miller.smartscheduler.dto.UserDetails;
import com.miller.smartscheduler.error.exception.AuthException;
import com.miller.smartscheduler.error.exception.UserRegistrationException;
import com.miller.smartscheduler.model.CustomJwtClaims;
import com.miller.smartscheduler.model.User;
import com.miller.smartscheduler.model.type.UserType;
import com.miller.smartscheduler.security.CustomIdentityProvider;
import com.miller.smartscheduler.service.TokenManagementService;
import com.miller.smartscheduler.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomIdentityProviderImpl implements CustomIdentityProvider {

  private final UserService userService;
  private final TokenManagementService tokenGenerationService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public SignInResponse signIn(SignInDTO signInDTO) {

    User user = userService.findByEmail(signInDTO.getEmail())
        .orElseThrow(() -> new AuthException("Invalid email address"));

    return loginWithPassword(signInDTO, user);
  }

  private SignInResponse loginWithPassword(SignInDTO signInDTO, User user) {
    if (passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())) {

      CustomJwtClaims customJwtClaims = new CustomJwtClaims(user.getUserId(), user.getUserType());
      TokenPair tokenPair = tokenGenerationService.generateTokenPair(customJwtClaims);

      SignInResponse response = new SignInResponse();
      response.setTokenPair(tokenPair);
      UserDetails userDetails = new UserDetails();
      userDetails.setEmail(user.getEmail());
      userDetails.setFirstName(user.getFirstName());
      userDetails.setLastName(user.getLastName());

      return response;
    } else {

      throw new AuthException("Invalid password");
    }
  }

  @Override
  public void logout(TokenPair tokenPair) {

    tokenGenerationService.logoutToken(tokenPair);
  }

  @Override
  public TokenPair refresh(TokenPair tokenPair) {

    return tokenGenerationService.refreshToken(tokenPair);
  }

  @Override
  public void signUp(SignUpDTO signUpDTO) {

    userService.findByEmail(signUpDTO.getEmail())
        .ifPresent((user) -> new UserRegistrationException(format("User with email %s already exist", signUpDTO.getEmail())));

    passwordsMatchValidation(signUpDTO);

    User user = new User();
    user.setEmail(signUpDTO.getEmail());
    user.setFirstName(signUpDTO.getFirstName());
    user.setLastName(signUpDTO.getLastName());
    user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
    user.setUserType(UserType.SIMPLE_USER);

    userService.save(user);
  }

  private void passwordsMatchValidation(SignUpDTO signUpDTO) {
    boolean passwordMatch = signUpDTO.getPassword().equals(signUpDTO.getRepeatPassword());

    if (!passwordMatch) {

      throw new IllegalArgumentException("Password and confirm password does not match");
    }
  }
}

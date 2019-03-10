package com.miller.smartscheduler.security.service;

import com.miller.smartscheduler.security.dto.TokenPair;
import com.miller.smartscheduler.security.dto.SignInDTO;
import com.miller.smartscheduler.security.dto.SignInResponse;
import com.miller.smartscheduler.security.dto.SignUpDTO;

public interface CustomIdentityProvider {

  SignInResponse signIn(SignInDTO signInDTO);

  void logout(TokenPair tokenPair);

  TokenPair refresh(TokenPair tokenPair);

  void signUp(SignUpDTO signUpDTO);
}

package com.miller.smartscheduler.security;

import com.miller.smartscheduler.dto.TokenPair;
import com.miller.smartscheduler.dto.SignInDTO;
import com.miller.smartscheduler.dto.SignInResponse;
import com.miller.smartscheduler.dto.SignUpDTO;

public interface CustomIdentityProvider {

  SignInResponse signIn(SignInDTO signInDTO);

  void logout(TokenPair tokenPair);

  TokenPair refresh(TokenPair tokenPair);

  void signUp(SignUpDTO signUpDTO);
}

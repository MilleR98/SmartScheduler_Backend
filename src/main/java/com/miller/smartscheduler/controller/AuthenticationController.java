package com.miller.smartscheduler.controller;

import com.miller.smartscheduler.dto.TokenPair;
import com.miller.smartscheduler.dto.SignInDTO;
import com.miller.smartscheduler.dto.SignInResponse;
import com.miller.smartscheduler.dto.SignUpDTO;
import com.miller.smartscheduler.security.CustomIdentityProvider;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final CustomIdentityProvider identityProvider;

  @PostMapping("/sign-in")
  public ResponseEntity login(@RequestBody @Valid SignInDTO signInDTO, HttpServletResponse httpServletResponse) {
    SignInResponse signInResponse = identityProvider.signIn(signInDTO);

    httpServletResponse.setHeader("Authorization", signInResponse.getTokenPair().getAuthToken());
    httpServletResponse.setHeader("Refresh", signInResponse.getTokenPair().getRefreshToken());

    return new ResponseEntity<>(signInResponse.getUserDetails(), HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity logout(@RequestBody @Valid TokenPair tokenPair) {
    identityProvider.logout(tokenPair);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/sign-up")
  public ResponseEntity signUp(@RequestBody @Valid SignUpDTO signUpDTO) {
    identityProvider.signUp(signUpDTO);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/refresh")
  public ResponseEntity refresh(@RequestBody @Valid TokenPair tokenPair, HttpServletResponse httpServletResponse) {
    TokenPair newTokens = identityProvider.refresh(tokenPair);

    httpServletResponse.setHeader("Authorization", newTokens.getAuthToken());
    httpServletResponse.setHeader("Refresh", newTokens.getRefreshToken());

    return new ResponseEntity<>(HttpStatus.OK);
  }
}

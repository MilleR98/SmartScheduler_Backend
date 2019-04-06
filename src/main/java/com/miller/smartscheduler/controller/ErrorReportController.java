package com.miller.smartscheduler.controller;

import com.miller.smartscheduler.error.AndroidErrorReport;
import com.miller.smartscheduler.error.ErrorNotificationService;
import com.miller.smartscheduler.error.exception.AuthException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error/report")
@RequiredArgsConstructor
public class ErrorReportController {

  private final String authHeaderPrefix = "Basic ";
  private final ErrorNotificationService errorNotificationService;
  @Value("${acra.report.username-password}")
  private String webhookUsernamePasswordAuth;

  @PostMapping
  public ResponseEntity processReport(@RequestBody AndroidErrorReport errorReport, @RequestHeader("Authorization") String authHeaderValue) {

    checkReportAuth(authHeaderValue);

    errorNotificationService.sendErrorReport(errorReport);

    return new ResponseEntity(HttpStatus.OK);
  }

  private void checkReportAuth(String requestedUsernamePasswordAuth) {
    String encodedUsernamePassword = Base64.getEncoder()
        .encodeToString(webhookUsernamePasswordAuth.getBytes());

    if (!encodedUsernamePassword.equals(requestedUsernamePasswordAuth.replaceFirst(authHeaderPrefix, StringUtils.EMPTY))) {

      throw new AuthException("Report request is unauthorized");
    }
  }
}

package com.miller.smartscheduler.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmartUtils {

  public static final String SALT = "mnjd093^*'~#44&($@�ZZUKJdc..-�sww";
  public static final int SECURITY_NUMBER = 5000;
  public static final String SERVER_HOST = "http://localhost:8080";

  public static String generateSecurityCode(String email, String id, String timestamp) {
    String linkCode = email + SALT + id + timestamp;
    String sha2LinkCode = null;
    byte[] byteData = null;

    try {

      byteData = MessageDigest.getInstance("SHA-256").digest(linkCode.getBytes());

    } catch (NoSuchAlgorithmException e) {

      log.debug(e.getMessage());
    }

    StringBuilder sb = new StringBuilder();

    for (byte byteDatum : byteData) {
      sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
    }

    sha2LinkCode = sb.toString();

    return sha2LinkCode;
  }
}

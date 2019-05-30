package com.miller.smartscheduler.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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

  public static boolean isEventTimeIntersectInterval(LocalDateTime startTime1, LocalDateTime endTime1, LocalDateTime startTime2, LocalDateTime endTime2) {

    boolean isEndTimeOrStartTimeEquals = startTime1.isEqual(startTime2) || endTime1.isEqual(endTime2);

    boolean isBookedTimeIncludeGenerated = startTime2.isAfter(startTime1) && endTime2.isBefore(endTime1);

    boolean isGeneratedTimeIncludeBooked = startTime1.isAfter(startTime2) && endTime1.isBefore(endTime2);

    boolean isBookedEndTimeIntersectGenerated = !isDateInInterval(startTime1, startTime2, endTime2)
        && isDateInInterval(endTime1, startTime2, endTime2);

    boolean isBookedStartTimeIntersectGenerated = isDateInInterval(startTime1, startTime2, endTime2)
        && !isDateInInterval(endTime1, startTime2, endTime2);

    return isEndTimeOrStartTimeEquals || isBookedTimeIncludeGenerated || isGeneratedTimeIncludeBooked || isBookedEndTimeIntersectGenerated || isBookedStartTimeIntersectGenerated;
  }

  private static boolean isDateInInterval(LocalDateTime dateToCheck, LocalDateTime start, LocalDateTime end) {

    return dateToCheck.isAfter(start) && dateToCheck.isBefore(end);
  }
}

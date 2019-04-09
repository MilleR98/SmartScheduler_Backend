package com.miller.smartscheduler.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {

    return dateToConvert.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }

  public static Date convertToDate(LocalDateTime dateToConvert) {

    return Date.from(dateToConvert.atZone(ZoneId.systemDefault())
        .toInstant());
  }
}

package com.miller.smartscheduler.util;

import com.miller.smartscheduler.model.ReminderConfig;
import com.miller.smartscheduler.model.type.ReminderType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class has Notification Time calculation related operations.
 *
 * @author Milpitas Communications.
 */
public class ReminderTimeUtil {

  public static List<Long> calculateScheduledReminderTimes(LocalDateTime endDate, ReminderConfig reminderConfig) {
    List<Long> periods = new ArrayList<>();

    Duration durationBetweenEndAndReminderStart = Duration.between(reminderConfig.getReminderTime(), endDate);
    Duration durationBetweenEndAndNow = Duration.between(LocalDateTime.now().withSecond(0).withNano(0), endDate);

    periods.add(durationBetweenEndAndNow.toMinutes());

    if (reminderConfig.getReminderType().equals(ReminderType.ONE_TIME)) {

      long reminderDelay = durationBetweenEndAndNow.minus(durationBetweenEndAndReminderStart).toMinutes();
      periods.add(reminderDelay);
    } else {

      //TODO implement daily, weekly, monthly
      long step = reminderConfig.getReminderType().getMinutes();
    }

    return periods;
  }

  public static long getOneTimeReminderDelay(LocalDateTime endDate, ReminderConfig reminderConfig){

    Duration durationBetweenEndAndReminderStart = Duration.between(reminderConfig.getReminderTime(), endDate);
    Duration durationBetweenEndAndNow = Duration.between(LocalDateTime.now().withSecond(0).withNano(0), endDate);

    return durationBetweenEndAndNow.minus(durationBetweenEndAndReminderStart).toMinutes();
  }
}

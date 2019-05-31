package com.miller.smartscheduler.util;

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

  public static long getOneTimeReminderDelay(LocalDateTime endDate, LocalDateTime reminderTime) {

    Duration durationBetweenEndAndReminderStart = Duration.between(reminderTime, endDate);
    Duration durationBetweenEndAndNow = Duration.between(LocalDateTime.now().withSecond(0).withNano(0), endDate);

    return durationBetweenEndAndNow.minus(durationBetweenEndAndReminderStart).toMinutes();
  }

  public static List<Long> calculateScheduledReminderTimes(LocalDateTime endDate, LocalDateTime reminderTime, ReminderType reminderType) {
    List<Long> periods = new ArrayList<>();

    Duration durationBetweenEndAndReminderStart = Duration.between(reminderTime, endDate);
    Duration durationBetweenEndAndNow = Duration.between(LocalDateTime.now().withSecond(0).withNano(0), endDate);

    periods.add(durationBetweenEndAndNow.toMinutes());

    if (reminderType.equals(ReminderType.ONE_TIME)) {

      long reminderDelay = durationBetweenEndAndNow.minus(durationBetweenEndAndReminderStart).toMinutes();
      periods.add(reminderDelay);
    } else {

      //TODO implement daily, weekly, monthly
      long step = reminderType.getMinutes();
    }

    return periods;
  }

  public static List<Long> calculateScheduledReminderTimesForEvent(LocalDateTime eventStart) {
    List<Long> periods = new ArrayList<>();

    Duration durationBetweenStartAndNow = Duration.between(LocalDateTime.now().withSecond(0).withNano(0), eventStart);

    //TODO implement 15m, 1h, 1d before

    return periods;
  }
}

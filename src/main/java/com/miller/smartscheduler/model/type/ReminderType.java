package com.miller.smartscheduler.model.type;

public enum ReminderType {

  DAILY(24 * 60),
  ONE_TIME(0),
  WEEKLY(60 * 24 * 7),
  MONTHLY(60 * 24 * 7 * 30);

  private long minutes;

  ReminderType(long minutes) {
    this.minutes = minutes;
  }

  public long getMinutes() {
    return minutes;
  }
}

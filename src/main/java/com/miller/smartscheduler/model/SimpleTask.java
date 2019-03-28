package com.miller.smartscheduler.model;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class SimpleTask {

  private String id;
  private String name;
  private LocalDate date;
  private LocalTime startTime;
  private LocalTime endTime;
  private Boolean scheduledNotificationsOn;
  private String userId;
}

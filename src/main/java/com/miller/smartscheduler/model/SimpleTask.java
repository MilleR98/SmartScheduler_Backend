package com.miller.smartscheduler.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SimpleTask {

  private String id;
  private String title;
  private String description;
  private ReminderConfig reminderConfig;
  private LocalDateTime deadlineDate;
  private LocalDateTime createdAt = LocalDateTime.now();
  private String userId;
}

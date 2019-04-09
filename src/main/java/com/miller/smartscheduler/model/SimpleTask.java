package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.ReminderType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SimpleTask {

  private String id;
  private String title;
  private String description;
  private ReminderType reminderType;
  private LocalDateTime reminderTime;
  private LocalDateTime deadlineDate;
  private LocalDateTime createdAt = LocalDateTime.now();
  private String userId;
}

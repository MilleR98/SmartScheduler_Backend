package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.ReminderType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderConfig {

  private ReminderType reminderType;
  private LocalDateTime reminderTime;
}

package com.miller.smartscheduler.model.dto;

import com.miller.smartscheduler.model.type.ReminderType;
import com.miller.smartscheduler.model.type.SubtaskStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TaskPreviewDTO {

  private String name;
  private ReminderType reminderType;
  private LocalDateTime deadlineDate;
  private List<SubtaskStatus> subtaskStatuses;
}

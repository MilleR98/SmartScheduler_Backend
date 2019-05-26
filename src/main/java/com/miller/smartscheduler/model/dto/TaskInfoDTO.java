package com.miller.smartscheduler.model.dto;

import com.miller.smartscheduler.model.Subtask;
import com.miller.smartscheduler.model.type.ReminderType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TaskInfoDTO {

  private String id;
  private String title;
  private String description;
  private ReminderType reminderType;
  private LocalDateTime reminderTime;
  private LocalDateTime deadlineDate;
  private LocalDateTime createdAt;
  private List<Subtask> subtaskList;
}

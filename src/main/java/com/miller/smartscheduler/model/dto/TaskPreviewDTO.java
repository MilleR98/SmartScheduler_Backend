package com.miller.smartscheduler.model.dto;

import com.miller.smartscheduler.model.type.SubtaskStatus;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class TaskPreviewDTO {

  private String id;
  private String title;
  private LocalDateTime deadlineDate;
  private LocalDateTime createdAt;
  private List<SubtaskStatus> subtaskStatuses;
}

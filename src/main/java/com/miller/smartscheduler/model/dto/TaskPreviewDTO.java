package com.miller.smartscheduler.model.dto;

import com.miller.smartscheduler.model.type.SubtaskStatus;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class TaskPreviewDTO {

  private String title;
  private Date deadlineDate;
  private Date createdAt;
  private List<SubtaskStatus> subtaskStatuses;
}

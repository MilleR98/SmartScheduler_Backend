package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.SubtaskPriority;
import com.miller.smartscheduler.model.type.SubtaskStatus;
import lombok.Data;

@Data
public class Subtask {

  private String id;
  private String name;
  private SubtaskPriority priority;
  private SubtaskStatus subtaskStatus;
  private String taskId;
}

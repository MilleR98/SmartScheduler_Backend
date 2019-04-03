package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.Subtask;
import java.util.List;

public interface SubtaskService extends CommonService<Subtask> {

  List<Subtask> getSubtaskByTaskId(String taskId);

  void removeAllByTaskId(String taskId);
}

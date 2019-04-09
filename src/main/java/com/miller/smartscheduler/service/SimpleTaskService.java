package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.SimpleTask;
import com.miller.smartscheduler.model.dto.CreateTaskDTO;
import com.miller.smartscheduler.model.dto.TaskInfoDTO;
import com.miller.smartscheduler.model.dto.TaskPreviewDTO;
import java.util.List;

public interface SimpleTaskService extends CommonService<SimpleTask>{

  List<SimpleTask> getUserTasks(String userId);

  void save(CreateTaskDTO createTaskDTO, String userId);

  List<TaskPreviewDTO> getUserTaskPreviews(String userId);

  TaskInfoDTO getTaskInfo(String id);
}

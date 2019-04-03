package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.SimpleTask;
import com.miller.smartscheduler.repository.SimpleTaskRepository;
import com.miller.smartscheduler.service.SimpleTaskService;
import com.miller.smartscheduler.service.SubtaskService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SimpleTaskServiceImpl extends CommonServiceImpl<SimpleTask> implements SimpleTaskService {

  private final SimpleTaskRepository simpleTaskRepository;
  private final SubtaskService subtaskService;

  public SimpleTaskServiceImpl(SimpleTaskRepository simpleTaskRepository, SubtaskService subtaskService) {
    super(simpleTaskRepository);
    this.simpleTaskRepository = simpleTaskRepository;
    this.subtaskService = subtaskService;
  }

  @Override
  public List<SimpleTask> getUserTasks(String userId) {

    return simpleTaskRepository.findAllByUserId(userId);
  }

  @Override
  public void remove(String id) {
    super.remove(id);

    subtaskService.removeAllByTaskId(id);
  }
}

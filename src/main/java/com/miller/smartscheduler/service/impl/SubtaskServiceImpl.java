package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.Subtask;
import com.miller.smartscheduler.repository.SubtaskRepository;
import com.miller.smartscheduler.service.SubtaskService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SubtaskServiceImpl extends CommonServiceImpl<Subtask> implements SubtaskService {

  private final SubtaskRepository subtaskRepository;

  public SubtaskServiceImpl(SubtaskRepository subtaskRepository) {
    super(subtaskRepository);
    this.subtaskRepository = subtaskRepository;
  }

  @Override
  public List<Subtask> getSubtaskByTaskId(String taskId) {

    return subtaskRepository.findAllByTaskId(taskId);
  }

  @Override
  public void removeAllByTaskId(String taskId) {

    subtaskRepository.removeAllByTaskId(taskId);
  }
}

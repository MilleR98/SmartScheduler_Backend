package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.SimpleTask;
import com.miller.smartscheduler.repository.SimpleTaskRepository;
import com.miller.smartscheduler.service.SimpleTaskService;
import org.springframework.stereotype.Service;

@Service
public class SimpleTaskServiceImpl extends CommonServiceImpl<SimpleTask> implements SimpleTaskService {

  private final SimpleTaskRepository simpleTaskRepository;

  public SimpleTaskServiceImpl(SimpleTaskRepository simpleTaskRepository) {
    super(simpleTaskRepository);
    this.simpleTaskRepository = simpleTaskRepository;
  }
}

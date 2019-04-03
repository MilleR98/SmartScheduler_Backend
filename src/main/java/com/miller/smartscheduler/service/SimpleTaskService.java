package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.SimpleTask;
import java.util.List;

public interface SimpleTaskService extends CommonService<SimpleTask>{

  List<SimpleTask> getUserTasks(String userId);
}

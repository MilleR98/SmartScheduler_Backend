package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.Subtask;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubtaskRepository extends MongoRepository<Subtask, String> {

  List<Subtask> findAllByTaskId(String taskId);

  void removeAllByTaskId(String taskId);
}

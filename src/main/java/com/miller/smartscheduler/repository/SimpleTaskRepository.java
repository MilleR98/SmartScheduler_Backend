package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.SimpleTask;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SimpleTaskRepository extends MongoRepository<SimpleTask, String> {

  List<SimpleTask> findAllByUserId(String userId);
}

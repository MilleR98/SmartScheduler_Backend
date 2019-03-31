package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.SimpleTask;
import com.miller.smartscheduler.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SimpleTaskRepository extends MongoRepository<SimpleTask, String> {

}

package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.EventPoint;
import com.miller.smartscheduler.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventPointRepository extends MongoRepository<EventPoint, String> {

}

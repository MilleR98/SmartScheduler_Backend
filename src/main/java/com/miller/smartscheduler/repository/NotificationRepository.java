package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

}

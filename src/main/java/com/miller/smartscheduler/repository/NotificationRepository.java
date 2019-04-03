package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.Notification;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

  List<Notification> findAllByUserId(String userId);

  List<Notification> findAllByCreatedAtAfter(LocalDateTime localDateTime);
}

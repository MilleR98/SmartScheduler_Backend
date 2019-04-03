package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.Notification;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationService extends CommonService<Notification> {

  List<Notification> getUserNotifications(String userId);
}

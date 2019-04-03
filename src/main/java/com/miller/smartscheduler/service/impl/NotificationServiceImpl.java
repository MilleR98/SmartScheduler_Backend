package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.Notification;
import com.miller.smartscheduler.repository.NotificationRepository;
import com.miller.smartscheduler.service.NotificationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends CommonServiceImpl<Notification> implements NotificationService {

  private final NotificationRepository notificationRepository;

  public NotificationServiceImpl(NotificationRepository notificationRepository) {
    super(notificationRepository);
    this.notificationRepository = notificationRepository;
  }

  @Override
  public List<Notification> getUserNotifications(String userId) {
    return null;
  }
}

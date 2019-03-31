package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.NotificationType;
import lombok.Data;

@Data
public class Notification {

  private String id;
  private String userId;
  private String content;
  private NotificationType notificationType;
}

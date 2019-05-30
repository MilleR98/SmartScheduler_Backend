package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.NotificationType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Notification {

  private String id;
  private String userId;
  private String content;
  private String title;
  private boolean seen;
  private LocalDateTime createdAt = LocalDateTime.now();
  private NotificationType notificationType;
}

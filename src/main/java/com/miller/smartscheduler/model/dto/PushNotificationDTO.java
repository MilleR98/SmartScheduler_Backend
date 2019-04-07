package com.miller.smartscheduler.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PushNotificationDTO {

  @JsonProperty("to")
  private String recipient;
  @JsonProperty("notification")
  private PushNotificationData notificationData;
}

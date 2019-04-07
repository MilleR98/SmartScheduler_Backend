package com.miller.smartscheduler.model;

import lombok.Data;

@Data
public class UserDeviceInfo {

  private String id;
  private String userId;
  private String deviceFcmToken;
  private String deviceId;
}

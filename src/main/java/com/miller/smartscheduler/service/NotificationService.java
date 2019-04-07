package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.Notification;
import com.miller.smartscheduler.model.dto.DeviceFcmTokenDTO;
import java.util.List;

public interface NotificationService extends CommonService<Notification> {

  List<Notification> getUserNotifications(String userId);

  void registerUserDeviceFcmToken(DeviceFcmTokenDTO fcmToken, String userId);
}

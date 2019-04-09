package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.Notification;
import com.miller.smartscheduler.model.UserDeviceInfo;
import com.miller.smartscheduler.model.dto.DeviceFcmTokenDTO;
import com.miller.smartscheduler.repository.NotificationRepository;
import com.miller.smartscheduler.service.NotificationService;
import com.miller.smartscheduler.service.UserDeviceInfoService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends CommonServiceImpl<Notification> implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserDeviceInfoService userDeviceInfoService;

  public NotificationServiceImpl(NotificationRepository notificationRepository, UserDeviceInfoService userDeviceInfoService) {
    super(notificationRepository);
    this.notificationRepository = notificationRepository;
    this.userDeviceInfoService = userDeviceInfoService;
  }

  @Override
  public List<Notification> getUserNotifications(String userId) {

    return notificationRepository.findAllByUserId(userId);
  }

  @Override
  public void registerUserDeviceFcmToken(DeviceFcmTokenDTO deviceFcmTokenDTO, String userId) {

    userDeviceInfoService.findByDeviceIdAndUserId(deviceFcmTokenDTO.getDeviceId(), userId)
        .ifPresentOrElse(userDeviceInfo -> updateDeviceFcmToken(userDeviceInfo, deviceFcmTokenDTO), () -> createNewDeviceInfo(deviceFcmTokenDTO, userId));

  }

  private void updateDeviceFcmToken(UserDeviceInfo userDeviceInfo, DeviceFcmTokenDTO deviceFcmTokenDTO) {
    userDeviceInfo.setDeviceFcmToken(deviceFcmTokenDTO.getDeviceFcmToken());

    userDeviceInfoService.save(userDeviceInfo);
  }

  private void createNewDeviceInfo(DeviceFcmTokenDTO deviceFcmTokenDTO, String userId) {
    UserDeviceInfo userDeviceInfo = new UserDeviceInfo();
    userDeviceInfo.setUserId(userId);
    userDeviceInfo.setDeviceFcmToken(deviceFcmTokenDTO.getDeviceFcmToken());
    userDeviceInfo.setDeviceId(deviceFcmTokenDTO.getDeviceId());

    userDeviceInfoService.save(userDeviceInfo);
  }
}

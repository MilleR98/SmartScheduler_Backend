package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.UserDeviceInfo;
import com.miller.smartscheduler.model.dto.PushNotificationDTO;
import com.miller.smartscheduler.model.dto.PushNotificationData;
import com.miller.smartscheduler.service.FirebaseMessagingService;
import com.miller.smartscheduler.service.UserDeviceInfoService;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

  private final UserDeviceInfoService userDeviceInfoService;
  private final RestTemplate restTemplate;

  @Value("${google.cloud-messaging.server-key}")
  private String serverKey;
  @Value("${google.cloud-messaging.send-notification-url}")
  private String sendPushNotificationUrl;

  private HttpHeaders headers;

  @PostConstruct
  public void init() {
    headers = new HttpHeaders() {{
      set(AUTHORIZATION_HEADER_NAME, "key=" + serverKey);
    }};

    headers.setContentType(MediaType.APPLICATION_JSON);
  }

  @Override
  public void sendSimplePushNotification(String title, String body, String userId) {

    PushNotificationDTO pushNotificationDTO = new PushNotificationDTO();

    PushNotificationData notificationData = new PushNotificationData();
    notificationData.setBody(body);
    notificationData.setTitle(title);

    pushNotificationDTO.setNotificationData(notificationData);

    userDeviceInfoService.getAllUserDevices(userId).stream()
        .map(UserDeviceInfo::getDeviceFcmToken)
        .forEach(fcmToken -> sendNotification(fcmToken, pushNotificationDTO));
  }

  private void sendNotification(String fcmToken, PushNotificationDTO pushNotificationDTO) {
    pushNotificationDTO.setRecipient(fcmToken);

    HttpEntity<PushNotificationDTO> request = new HttpEntity<>(pushNotificationDTO, headers);

    restTemplate.postForEntity(sendPushNotificationUrl, request, Object.class);
  }
}

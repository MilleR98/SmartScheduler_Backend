package com.miller.smartscheduler.controller;

import com.miller.smartscheduler.model.Notification;
import com.miller.smartscheduler.model.dto.DeviceFcmTokenDTO;
import com.miller.smartscheduler.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public List<Notification> getUserNotifications(@RequestHeader("userId") String userId) {

    return notificationService.getUserNotifications(userId);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity removeNotification(@PathVariable("id") String id) {

    notificationService.remove(id);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping("/register-token")
  public ResponseEntity setUserNotificationToken(@RequestBody DeviceFcmTokenDTO deviceFcmTokenDTO, @RequestHeader("userId") String userId) {

    notificationService.registerUserDeviceFcmToken(deviceFcmTokenDTO, userId);

    return new ResponseEntity(HttpStatus.OK);
  }
}

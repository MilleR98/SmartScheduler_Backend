package com.miller.smartscheduler.controller;

import com.miller.smartscheduler.error.exception.ContentNotFoundException;
import com.miller.smartscheduler.model.Notification;
import com.miller.smartscheduler.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifcations")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public List<Notification> getUserNotifications(@RequestParam("userId") String userId) {

    return notificationService.getUserNotifications(userId);
  }

  @GetMapping("/{id}")
  public Notification getNotifications(@PathVariable("id") String id) {

    return notificationService.find(id).orElseThrow(ContentNotFoundException::new);
  }

  @PostMapping
  public ResponseEntity createNotifications(@RequestBody Notification notification) {

    notificationService.save(notification);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity updateNotification(@PathVariable("id") String id, @RequestBody Notification notification) {

    notificationService.update(id, notification);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity removeNotification(@PathVariable("id") String id) {

    notificationService.remove(id);

    return new ResponseEntity(HttpStatus.OK);
  }
}

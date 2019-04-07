package com.miller.smartscheduler.service;

public interface FirebaseMessagingService {

  void sendSimplePushNotification(String title, String body, String userId);
}

package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.error.exception.ContentNotFoundException;
import com.miller.smartscheduler.model.Challenge;
import com.miller.smartscheduler.model.Notification;
import com.miller.smartscheduler.model.type.ChallengeStatus;
import com.miller.smartscheduler.model.type.NotificationType;
import com.miller.smartscheduler.repository.ChallengeRepository;
import com.miller.smartscheduler.service.ChallengeService;
import com.miller.smartscheduler.service.FirebaseMessagingService;
import com.miller.smartscheduler.service.NotificationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChallengeServiceImpl extends CommonServiceImpl<Challenge> implements ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final NotificationService notificationService;
  private final FirebaseMessagingService firebaseMessagingService;

  public ChallengeServiceImpl(ChallengeRepository challengeRepository, NotificationService notificationService,
      FirebaseMessagingService firebaseMessagingService) {
    super(challengeRepository);
    this.challengeRepository = challengeRepository;
    this.notificationService = notificationService;
    this.firebaseMessagingService = firebaseMessagingService;
  }

  @Override
  public List<Challenge> findAllByUserId(String userId) {

    return challengeRepository.findAllByUserId(userId);
  }

  @Override
  public List<Challenge> findAllByUserIdAndStatus(String userId, ChallengeStatus status) {

    return challengeRepository.findAllByUserIdAndStatus(userId, status);
  }

  @Override
  public void updateStatus(String id, ChallengeStatus challengeStatus) {

    Challenge challenge = find(id).orElseThrow(ContentNotFoundException::new);
    challenge.setStatus(challengeStatus);

    firebaseMessagingService.sendSimplePushNotification("Challenge failed", "Oh no, you give up... Challenge " + challenge.getName() + " failed", challenge.getUserId());

    Notification notification = new Notification();
    notification.setContent("Oh no, you give up... Challenge " + challenge.getName() + " failed");
    notification.setTitle("Challenge failed");
    notification.setUserId(challenge.getUserId());
    notification.setNotificationType(NotificationType.INFO);

    update(id, challenge);
  }

  @Override
  public void save(Challenge object) {

    firebaseMessagingService.sendSimplePushNotification("Challenge created", "Looks like your create new challenge "+object.getName()+"! Great! keep going for 21 days. I will notify you every day at "
        + object.getRemindAt().toLocalTime(), object.getUserId());

    Notification notification = new Notification();
    notification.setContent("Looks like your create new challenge! Great keep going 21 days. I will notify you every day at " + object.getRemindAt().toLocalTime());
    notification.setTitle("Challenge created");
    notification.setUserId(object.getUserId());
    notification.setNotificationType(NotificationType.INFO);

    notificationService.save(notification);

    super.save(object);
  }
}

package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.Challenge;
import com.miller.smartscheduler.model.type.ChallengeStatus;
import java.util.List;

public interface ChallengeService extends CommonService<Challenge> {

  List<Challenge> findAllByUserId(String userId);

  List<Challenge> findAllByUserIdAndStatus(String userId, ChallengeStatus status);
}

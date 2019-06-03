package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.Challenge;
import com.miller.smartscheduler.model.type.ChallengeStatus;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChallengeRepository extends MongoRepository<Challenge, String> {

  List<Challenge> findAllByUserId(String userId);

  List<Challenge> findAllByUserIdAndStatus(String userId, ChallengeStatus status);
}

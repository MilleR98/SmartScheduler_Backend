package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChallengeRepository extends MongoRepository<Challenge, String> {

}

package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.Challenge;
import com.miller.smartscheduler.repository.ChallengeRepository;
import com.miller.smartscheduler.service.ChallengeService;
import org.springframework.stereotype.Service;

@Service
public class ChallengeServiceImpl extends CommonServiceImpl<Challenge> implements ChallengeService {

  private final ChallengeRepository challengeRepository;

  public ChallengeServiceImpl(ChallengeRepository challengeRepository) {
    super(challengeRepository);
    this.challengeRepository = challengeRepository;
  }

}

package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.ChallengeStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Challenge {

  private String id;
  private String name;
  private ChallengeStatus status;
  private LocalDateTime startTime;
  private LocalDateTime remindAt;
  private String userId;
}

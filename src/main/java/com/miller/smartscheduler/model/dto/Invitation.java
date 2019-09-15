package com.miller.smartscheduler.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Invitation {

  private String ownerEmail;
  private String memberEmail;
  private String ownerFullName;
  private String memberName;
  private String memberPermission;
  private String eventDescription;
  private String eventName;
  private String startDate;
  private String endDate;
  private String eventAddress;
  private String serverHost;
  private String eventId;
  private String timestamp;
  private String sha2LinkCode;
  private String acceptLink;
  private String declineLink;
}

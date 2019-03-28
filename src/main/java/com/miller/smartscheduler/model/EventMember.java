package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.EventMemberType;
import lombok.Data;

@Data
public class EventMember {

  private String id;
  private String memberId;
  private String eventId;
  private EventMemberType memberType;
}

package com.miller.smartscheduler.model.dto;

import com.miller.smartscheduler.model.type.EventMemberPermission;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventPreviewDTO {

  private String eventId;
  private String name;
  private Long membersCount;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private EventMemberPermission currentUserEventPermission;
}

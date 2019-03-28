package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.PointPriority;
import com.miller.smartscheduler.model.type.PointStatus;
import lombok.Data;

@Data
public class EventPoint {

  private String id;
  private String name;
  private PointPriority pointPriority;
  private PointStatus pointStatus;
  private String eventId;
}

package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.EventCategory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Event {

  private String id;
  private String name;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private boolean enableReminders;
  private String description;
  private EventCategory eventCategory;
  private String locationId;
}

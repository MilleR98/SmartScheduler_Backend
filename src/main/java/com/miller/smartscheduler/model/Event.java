package com.miller.smartscheduler.model;

import com.miller.smartscheduler.model.type.EventCategory;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class Event {

  private String id;
  private String name;
  private LocalDate date;
  private LocalTime startTime;
  private LocalTime endTime;

  private String description;
  private EventCategory eventCategory;
  private String locationId;
}

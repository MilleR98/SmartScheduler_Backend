package com.miller.smartscheduler.model.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class EventTimeConflictDTO {

  private List<EventPreviewDTO> interceptedEvents = new ArrayList<>();
}

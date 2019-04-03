package com.miller.smartscheduler.model.dto;

import com.miller.smartscheduler.model.EventLocation;
import com.miller.smartscheduler.model.type.EventCategory;
import com.miller.smartscheduler.model.type.EventMemberPermission;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class EventDTO {

  private String id;
  private String name;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String description;
  private EventCategory eventCategory;
  private EventLocation eventLocation;
  private EventMemberPermission currentUserPermission;
  private List<EventMemberDTO> memberDTOList;
}

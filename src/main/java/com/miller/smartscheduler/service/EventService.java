package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.model.dto.EventDTO;
import com.miller.smartscheduler.model.dto.EventMemberDTO;
import com.miller.smartscheduler.model.dto.EventPreviewDTO;
import com.miller.smartscheduler.model.type.EventMemberPermission;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService extends CommonService<Event> {

  List<Event> findUserEvents(String userId, LocalDateTime from, LocalDateTime to);

  EventDTO findFullEventInfo(String id);

  void saveFullEvent(EventDTO eventDTO, String userId);

  List<EventPreviewDTO> findUserEventsPreview(String userId, LocalDateTime from, LocalDateTime to);

  void inviteMemberToEvent(String eventId, EventMemberDTO eventMemberDTO);
}

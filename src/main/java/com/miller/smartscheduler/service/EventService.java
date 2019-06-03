package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.model.dto.EventDTO;
import com.miller.smartscheduler.model.dto.EventMemberDTO;
import com.miller.smartscheduler.model.dto.EventPreviewDTO;
import com.miller.smartscheduler.model.dto.EventTimeConflictDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface EventService extends CommonService<Event> {

  List<Event> findUserEvents(String userId, LocalDateTime from, LocalDateTime to);

  EventDTO findFullEventInfo(String id);

  void saveFullEvent(EventDTO eventDTO, String userId);

  List<EventPreviewDTO> findUserEventsPreview(String userId, LocalDateTime from, LocalDateTime to);

  void inviteMemberToEvent(String eventId, String ownerId, EventMemberDTO eventMemberDTO);

  String declineEventEmailInvitation(String eventId, String code, String time, String email);

  String acceptEventEmailInvitation(String eventId, String code, String time, String email);

  void notifyEventMembers(String eventId, String msgContent);

  void declineEventInvitation(String eventId, String userId);

  void acceptEventInvitation(String eventId, String userId);

  EventTimeConflictDTO eventTimeValidation(String userId, LocalDateTime startTime, LocalDateTime endTime);

  List<LocalDate> getDaysWithEvents(String userId, Month month);

  void deleteMemberEvent(String eventId, String memberId);

  void updateEvent(String id, EventDTO eventDTO);
}

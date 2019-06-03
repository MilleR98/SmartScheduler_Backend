package com.miller.smartscheduler.controller;

import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.model.dto.EventDTO;
import com.miller.smartscheduler.model.dto.EventMemberDTO;
import com.miller.smartscheduler.model.dto.EventPreviewDTO;
import com.miller.smartscheduler.model.dto.EventTimeConflictDTO;
import com.miller.smartscheduler.service.EventService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @GetMapping
  public List<Event> getUserEvents(@RequestHeader("userId") String userId,
      @RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to) {

    return eventService.findUserEvents(userId, LocalDateTime.parse(from), LocalDateTime.parse(to));
  }

  @GetMapping("/previews")
  public List<EventPreviewDTO> getUserEventsPreview(@RequestHeader("userId") String userId,
      @RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to) {

    return eventService.findUserEventsPreview(userId, LocalDateTime.parse(from), LocalDateTime.parse(to));
  }

  @GetMapping("/day-markers")
  public List<LocalDate> getDaysWithEvents(@RequestHeader("userId") String userId,
      @RequestParam(value = "month", required = false) Month month) {

    return eventService.getDaysWithEvents(userId, month);
  }

  @GetMapping("/{id}")
  public EventDTO getEventInfo(@PathVariable("id") String id) {

    return eventService.findFullEventInfo(id);
  }

  @PostMapping("/{id}/members/invite")
  public void inviteMemberToEvent(@PathVariable("id") String eventId, @RequestHeader("userId") String userId, @RequestBody EventMemberDTO eventMemberDTO) {

    eventService.inviteMemberToEvent(eventId, userId, eventMemberDTO);
  }

  @DeleteMapping("/{eventId}/members/{memberId}")
  public void deleteMemberEvent(@PathVariable("eventId") String eventId, @PathVariable("memberId") String memberId) {

    eventService.deleteMemberEvent(eventId, memberId);
  }

  @PostMapping("/email-invitation/decline")
  public String declineInvitation(@RequestParam("eventId") String eventId,
      @RequestParam("code") String code,
      @RequestParam("time") String time,
      @RequestParam("email") String email) {

    return eventService.declineEventEmailInvitation(eventId, code, time, email);
  }

  @PostMapping("/email-invitation/accept")
  public String acceptInvitation(@RequestParam("eventId") String eventId,
      @RequestParam("code") String code,
      @RequestParam("time") String time,
      @RequestParam("email") String email) {

    return eventService.acceptEventEmailInvitation(eventId, code, time, email);
  }

  @PostMapping("/{id}/invitation/decline")
  public void declineInvitation(@PathVariable("id") String eventId, @RequestHeader("userId") String userId) {

    eventService.declineEventInvitation(eventId, userId);
  }

  @PostMapping("/{id}/invitation/accept")
  public void acceptInvitation(@PathVariable("id") String eventId, @RequestHeader("userId") String userId) {

    eventService.acceptEventInvitation(eventId, userId);
  }

  @PostMapping("/check-time-conflict")
  public EventTimeConflictDTO eventTimeValidation(@RequestHeader("userId") String userId,
      @RequestParam(value = "from") LocalDateTime startTime,
      @RequestParam(value = "to") LocalDateTime endTime) {

    return eventService.eventTimeValidation(userId, startTime, endTime);
  }

  @PostMapping("/{id}/notify-members")
  public void notifyEventMembers(@PathVariable("id") String eventId,
      @RequestBody() String msgContent) {

    eventService.notifyEventMembers(eventId, msgContent);
  }

  @PostMapping
  public ResponseEntity createEvent(@RequestBody EventDTO eventDTO, @RequestHeader("userId") String userId) {

    eventService.saveFullEvent(eventDTO, userId);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity updateEvent(@PathVariable("id") String id, @RequestBody EventDTO eventDTO) {

    eventService.updateEvent(id, eventDTO);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity removeEvent(@PathVariable("id") String id) {

    eventService.remove(id);

    return new ResponseEntity(HttpStatus.OK);
  }
}

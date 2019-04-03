package com.miller.smartscheduler.controller;

import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.model.dto.EventDTO;
import com.miller.smartscheduler.model.dto.EventMemberDTO;
import com.miller.smartscheduler.model.dto.EventPreviewDTO;
import com.miller.smartscheduler.service.EventService;
import java.time.LocalDateTime;
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
      @RequestParam(value = "from", required = false) LocalDateTime from, @RequestParam(value = "to", required = false) LocalDateTime to) {

    return eventService.findUserEvents(userId, from, to);
  }

  @GetMapping("/previews")
  public List<EventPreviewDTO> getUserEventsPreview(@RequestHeader("userId") String userId,
      @RequestParam(value = "from", required = false) LocalDateTime from, @RequestParam(value = "to", required = false) LocalDateTime to) {

    return eventService.findUserEventsPreview(userId, from, to);
  }

  @GetMapping("/{id}")
  public EventDTO getEventInfo(@PathVariable("id") String id) {

    return eventService.findFullEventInfo(id);
  }

  @PostMapping("/{id}/invite")
  public void inviteMemberToEvent(@PathVariable("id") String eventId, @RequestBody EventMemberDTO eventMemberDTO) {

    eventService.inviteMemberToEvent(eventId, eventMemberDTO);
  }

  @PostMapping
  public ResponseEntity createEvent(@RequestBody EventDTO eventDTO, @RequestHeader("userId") String userId) {

    eventService.saveFullEvent(eventDTO, userId);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity updateEvent(@PathVariable("id") String id, @RequestBody Event eventDTO) {

    eventService.update(id, eventDTO);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity removeEvent(@PathVariable("id") String id) {

    eventService.remove(id);

    return new ResponseEntity(HttpStatus.OK);
  }
}

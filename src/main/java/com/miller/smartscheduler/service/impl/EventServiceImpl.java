package com.miller.smartscheduler.service.impl;

import static com.miller.smartscheduler.model.type.EventMemberPermission.OWNER;
import static java.util.Objects.nonNull;

import com.miller.smartscheduler.error.exception.ContentNotFoundException;
import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.model.EventLocation;
import com.miller.smartscheduler.model.EventMember;
import com.miller.smartscheduler.model.User;
import com.miller.smartscheduler.model.dto.EventDTO;
import com.miller.smartscheduler.model.dto.EventMemberDTO;
import com.miller.smartscheduler.model.dto.EventPreviewDTO;
import com.miller.smartscheduler.model.type.EventMemberPermission;
import com.miller.smartscheduler.model.type.UserType;
import com.miller.smartscheduler.repository.EventRepository;
import com.miller.smartscheduler.service.EventLocationService;
import com.miller.smartscheduler.service.EventMemberService;
import com.miller.smartscheduler.service.EventService;
import com.miller.smartscheduler.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl extends CommonServiceImpl<Event> implements EventService {

  private final EventRepository eventRepository;
  private final EventMemberService eventMemberService;
  private final EventLocationService eventLocationService;
  private final UserService userService;

  public EventServiceImpl(EventRepository eventRepository, EventMemberService eventMemberService, EventLocationService eventLocationService,
      UserService userService) {
    super(eventRepository);
    this.eventRepository = eventRepository;
    this.eventMemberService = eventMemberService;
    this.eventLocationService = eventLocationService;
    this.userService = userService;
  }

  @Override
  public List<Event> findUserEvents(String userId, LocalDateTime from, LocalDateTime to) {

    List<String> eventIds = eventMemberService.findAllByUserId(userId)
        .stream()
        .map(EventMember::getEventId)
        .collect(Collectors.toList());

    List<Event> eventList = eventRepository.findAllById(eventIds);

    return filterByDatetimes(from, to, eventList);
  }

  private List<Event> filterByDatetimes(LocalDateTime from, LocalDateTime to, List<Event> eventList) {

    Predicate<Event> eventPredicate;

    if (nonNull(from) && nonNull(to)) {

      eventPredicate = event -> event.getStartDate().isAfter(from) || event.getStartDate().isBefore(to);
    } else if (nonNull(from)) {

      eventPredicate = event -> event.getStartDate().isAfter(from) || event.getStartDate().isEqual(from);
    } else if (nonNull(to)) {

      eventPredicate = event -> event.getStartDate().isBefore(to) || event.getStartDate().isEqual(to);
    } else {

      return eventList;
    }

    return eventList.stream().filter(eventPredicate)
        .collect(Collectors.toList());
  }

  @Override
  public EventDTO findFullEventInfo(String id) {

    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new ContentNotFoundException("Cannot find event info"));

    EventLocation eventLocation = eventLocationService.find(event.getLocationId())
        .orElseThrow(() -> new ContentNotFoundException("Cannot find event location info"));

    List<EventMemberDTO> eventMembers = eventMemberService.findAllEventId(id).stream()
        .map(this::mapToEventMemberDTO)
        .collect(Collectors.toList());

    EventDTO eventDTO = new EventDTO();
    eventDTO.setDescription(event.getDescription());
    eventDTO.setId(event.getId());
    eventDTO.setEndDate(event.getEndDate());
    eventDTO.setStartDate(event.getStartDate());
    eventDTO.setEventCategory(event.getEventCategory());
    eventDTO.setName(event.getName());
    eventDTO.setEventLocation(eventLocation);
    eventDTO.setMemberDTOList(eventMembers);

    return eventDTO;
  }

  private EventMemberDTO mapToEventMemberDTO(EventMember eventMember) {

    EventMemberDTO eventMemberDTO = new EventMemberDTO();
    eventMemberDTO.setUserId(eventMember.getUserId());
    eventMemberDTO.setMemberPermission(eventMember.getEventMemberPermission());

    return eventMemberDTO;
  }

  @Override
  public void saveFullEvent(EventDTO eventDTO, String userId) {

    String eventLocationId = eventLocationService.saveAndReturn(eventDTO.getEventLocation()).getId();

    Event event = new Event();
    event.setDescription(eventDTO.getDescription());
    event.setName(event.getName());
    event.setEndDate(event.getEndDate());
    event.setStartDate(event.getStartDate());
    event.setEventCategory(event.getEventCategory());
    event.setLocationId(eventLocationId);
    String eventId = this.saveAndReturn(event).getId();

    EventMember eventOwner = new EventMember();
    eventOwner.setEventId(eventId);
    eventOwner.setAccepted(true);
    eventOwner.setEventMemberPermission(OWNER);
    eventOwner.setUserId(userId);
    eventMemberService.save(eventOwner);

    eventDTO.getMemberDTOList().forEach(eventMemberDTO -> inviteMemberToEvent(eventId, eventMemberDTO));
  }

  @Override
  public List<EventPreviewDTO> findUserEventsPreview(String userId, LocalDateTime from, LocalDateTime to) {

    return findUserEvents(userId, from, to).stream()
        .map(event -> mapToEventPreviewDTO(event, userId))
        .collect(Collectors.toList());
  }

  @Override
  public void inviteMemberToEvent(String eventId, EventMemberDTO eventMemberDTO) {

    Optional<User> memberOptional = userService.findByEmail(eventMemberDTO.getMemberEmail());

    String userId;

    if (memberOptional.isPresent()) {

      userId = memberOptional.get().getUserId();
    } else {

      User member = new User();
      member.setUserType(UserType.GUEST_USER);
      member.setFirstName(eventMemberDTO.getFirstName());
      member.setLastName(eventMemberDTO.getLastName());
      member.setEmail(eventMemberDTO.getMemberEmail());

      userId = userService.saveAndReturn(member).getUserId();
    }

    EventMember eventMember = new EventMember();
    eventMember.setEventId(eventId);
    eventMember.setEventMemberPermission(eventMemberDTO.getMemberPermission());
    eventMember.setUserId(userId);

    //TODO: EMAIL INVITATION
  }

  private EventPreviewDTO mapToEventPreviewDTO(Event event, String userId) {
    EventPreviewDTO eventPreviewDTO = new EventPreviewDTO();
    eventPreviewDTO.setEventId(event.getId());
    eventPreviewDTO.setEndTime(event.getEndDate());
    eventPreviewDTO.setStartTime(event.getStartDate());
    eventPreviewDTO.setName(event.getName());

    EventMemberPermission currentUserPermission = eventMemberService.findByEventAndUser(event.getId(), userId)
        .getEventMemberPermission();

    eventPreviewDTO.setCurrentUserEventPermission(currentUserPermission);

    long activeMemberCount = eventMemberService.findAllEventId(event.getId()).stream()
        .filter(eventMember -> !eventMember.getEventMemberPermission().equals(OWNER) || eventMember.isAccepted())
        .count();

    eventPreviewDTO.setMembersCount(activeMemberCount);

    return eventPreviewDTO;
  }

  @Override
  public void remove(String id) {
    super.remove(id);

    eventMemberService.removeAllByEventId(id);
  }
}

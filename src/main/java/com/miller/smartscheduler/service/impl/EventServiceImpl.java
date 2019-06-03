package com.miller.smartscheduler.service.impl;

import static com.miller.smartscheduler.model.type.EventMemberPermission.OWNER;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.miller.smartscheduler.error.exception.BadRequestException;
import com.miller.smartscheduler.error.exception.ContentNotFoundException;
import com.miller.smartscheduler.error.exception.EventTimeConflictException;
import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.model.EventLocation;
import com.miller.smartscheduler.model.EventMember;
import com.miller.smartscheduler.model.Notification;
import com.miller.smartscheduler.model.User;
import com.miller.smartscheduler.model.dto.EventDTO;
import com.miller.smartscheduler.model.dto.EventMemberDTO;
import com.miller.smartscheduler.model.dto.EventPreviewDTO;
import com.miller.smartscheduler.model.dto.EventTimeConflictDTO;
import com.miller.smartscheduler.model.type.EmailMessageType;
import com.miller.smartscheduler.model.type.EventMemberPermission;
import com.miller.smartscheduler.model.type.NotificationType;
import com.miller.smartscheduler.model.type.UserType;
import com.miller.smartscheduler.repository.EventRepository;
import com.miller.smartscheduler.service.EmailService;
import com.miller.smartscheduler.service.EventLocationService;
import com.miller.smartscheduler.service.EventMemberService;
import com.miller.smartscheduler.service.EventService;
import com.miller.smartscheduler.service.FirebaseMessagingService;
import com.miller.smartscheduler.service.InvitationService;
import com.miller.smartscheduler.service.NotificationService;
import com.miller.smartscheduler.service.UserService;
import com.miller.smartscheduler.util.SmartUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class EventServiceImpl extends CommonServiceImpl<Event> implements EventService {

  private final EventRepository eventRepository;
  private final ScheduledExecutorService executor;
  private final EventMemberService eventMemberService;
  private final EventLocationService eventLocationService;
  private final UserService userService;
  private final InvitationService invitationService;
  private final FirebaseMessagingService firebaseMessagingService;
  private final EmailService emailService;
  private final NotificationService notificationService;
  private final TemplateEngine templateEngine;

  public EventServiceImpl(EventRepository eventRepository, ScheduledExecutorService executor, EventMemberService eventMemberService,
      EventLocationService eventLocationService,
      UserService userService, InvitationService invitationService, FirebaseMessagingService firebaseMessagingService,
      EmailService emailService, NotificationService notificationService, TemplateEngine templateEngine) {
    super(eventRepository);
    this.eventRepository = eventRepository;
    this.executor = executor;
    this.eventMemberService = eventMemberService;
    this.eventLocationService = eventLocationService;
    this.userService = userService;
    this.invitationService = invitationService;
    this.firebaseMessagingService = firebaseMessagingService;
    this.emailService = emailService;
    this.notificationService = notificationService;
    this.templateEngine = templateEngine;
  }

  @Override
  public List<Event> findUserEvents(String userId, LocalDateTime from, LocalDateTime to) {

    List<String> eventIds = eventMemberService.findAllByUserId(userId)
        .stream()
        .map(EventMember::getEventId)
        .collect(Collectors.toList());

    List<Event> eventList = eventRepository.findAllByIdIn(eventIds);

    return filterByDate(from, to, eventList);
  }

  private List<Event> filterByDate(LocalDateTime from, LocalDateTime to, List<Event> eventList) {

    Predicate<Event> eventPredicate;

    if (nonNull(from) && nonNull(to)) {

      eventPredicate = event -> ((event.getStartDate().isAfter(from) || event.getStartDate().isEqual(from))
          && (event.getStartDate().isBefore(to) || event.getStartDate().isEqual(to))) ||
          ((event.getEndDate().isAfter(from) || event.getEndDate().isEqual(from))
              && (event.getEndDate().isBefore(to) || event.getEndDate().isEqual(to)));
    } else if (nonNull(from)) {

      eventPredicate = event -> event.getStartDate().isAfter(from) || event.getStartDate().isEqual(from)
          || event.getEndDate().isAfter(from) || event.getEndDate().isEqual(from);
    } else if (nonNull(to)) {

      eventPredicate = event -> event.getStartDate().isBefore(to) || event.getStartDate().isEqual(to)
          || event.getEndDate().isBefore(to) || event.getEndDate().isEqual(to);
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

    List<EventMemberDTO> eventMembers = eventMemberService.findAllByEventId(id).stream()
        .filter(eventMember -> !eventMember.getEventMemberPermission().equals(OWNER))
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
    event.setName(eventDTO.getName());
    event.setEndDate(eventDTO.getEndDate());
    event.setStartDate(eventDTO.getStartDate());
    event.setEventCategory(eventDTO.getEventCategory());
    event.setLocationId(eventLocationId);
    event.setEnableReminders(eventDTO.isEnableReminders());
    String eventId = this.saveAndReturn(event).getId();

    EventMember eventOwner = new EventMember();
    eventOwner.setEventId(eventId);
    eventOwner.setAccepted(true);
    eventOwner.setEventMemberPermission(OWNER);
    eventOwner.setUserId(userId);
    eventMemberService.save(eventOwner);

    if (event.isEnableReminders()) {
      //todo event reminders
    }

    eventDTO.getMemberDTOList().forEach(eventMemberDTO -> inviteMemberToEvent(eventId, userId, eventMemberDTO));
  }

  private void remindAboutEvent(EventDTO eventDTO, String userId) {

    EventLocation eventLocation = eventDTO.getEventLocation();
    String eventTime = eventDTO.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + " - " + eventDTO.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    String address = eventLocation.getCountry() + ", " + eventLocation.getCity() + ", " + eventLocation.getStreet() + " street, " + eventLocation.getBuildingNumber();

    firebaseMessagingService.sendSimplePushNotification(eventDTO.getName(),
        "Hey! You have an event " + eventDTO.getName() + ". Time " + eventTime + ". Location: " + address,
        userId);
  }

  @Override
  public List<EventPreviewDTO> findUserEventsPreview(String userId, LocalDateTime from, LocalDateTime to) {

    return findUserEvents(userId, from, to).stream()
        .map(event -> mapToEventPreviewDTO(event, userId))
        .collect(Collectors.toList());
  }

  @Override
  public void inviteMemberToEvent(String eventId, String ownerId, EventMemberDTO eventMemberDTO) {

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

    User eventOwner = userService.find(ownerId)
        .orElseThrow(ContentNotFoundException::new);

    Event event = find(eventId).orElseThrow(ContentNotFoundException::new);
    EventLocation eventLocation = eventLocationService.find(event.getLocationId())
        .orElseThrow(ContentNotFoundException::new);

    invitationService.sendEventInvitation(eventMemberDTO, event, eventLocation, eventOwner);

    Notification notification = new Notification();
    notification.setContent(eventOwner.getFirstName() + " " + eventOwner.getLastName() + " has invited you to join the event " + event.getName() +
        ". From " + event.getStartDate().format(ISO_LOCAL_DATE) + " to " + event.getEndDate().format(ISO_LOCAL_DATE));
    notification.setTitle("Event invitation");
    notification.setUserId(eventMember.getId());
    notification.setNotificationType(NotificationType.INVITATION);

    notificationService.save(notification);
  }

  @Override
  public String declineEventEmailInvitation(String eventId, String code, String time, String email) {

    User member = userService.findByEmail(email).orElseThrow(ContentNotFoundException::new);
    String eventMemberId = eventMemberService.findByEventAndUser(eventId, member.getUserId()).getId();
    String eventOwnerId = eventMemberService.findByEventIdAndMemberPermission(eventId, OWNER).get(0).getUserId();
    String eventName = find(eventId).orElseThrow(ContentNotFoundException::new).getName();

    String hashed = SmartUtils.generateSecurityCode(email, eventId, time);

    if (isCode(code, hashed)) {

      declineInvitation(member, eventMemberId, eventOwnerId, eventName);
    } else {

      throw new BadRequestException("Link is not active");
    }

    Context context = new Context();
    context.setVariables(new HashMap<>() {{
      put("eventName", eventName);
    }});

    return templateEngine.process("invitation-declined", context);
  }

  private void declineInvitation(User member, String eventMemberId, String eventOwnerId, String eventName) {
    eventMemberService.remove(eventMemberId);

    firebaseMessagingService.sendSimplePushNotification("Member declined invitation",
        member.getFirstName() + " " + member.getLastName() + " has declined invitation to event"
            + eventName, eventOwnerId);

    Notification notification = new Notification();
    notification.setContent(member.getFirstName() + " " + member.getLastName() + " has declined your invitation to the event " + eventName);
    notification.setTitle("Event invitation");
    notification.setUserId(eventOwnerId);
    notification.setNotificationType(NotificationType.INFO);

    notificationService.save(notification);
  }

  @Override
  public String acceptEventEmailInvitation(String eventId, String code, String time, String email) {

    User member = userService.findByEmail(email).orElseThrow(ContentNotFoundException::new);
    EventMember eventMember = eventMemberService.findByEventAndUser(eventId, member.getUserId());
    String eventOwnerId = eventMemberService.findByEventIdAndMemberPermission(eventId, OWNER).get(0).getUserId();
    Event event = find(eventId).orElseThrow(ContentNotFoundException::new);

    String hashed = SmartUtils.generateSecurityCode(email, eventId, time);

    if (isCode(code, hashed)) {

      acceptInvitation(member, eventOwnerId, eventMember, event);

    } else {

      throw new BadRequestException("Link is not active");
    }

    Context context = new Context();
    context.setVariables(new HashMap<>() {{
      put("eventName", event.getName());
    }});

    return templateEngine.process("invitation-accepted", context);
  }

  @Override
  public void notifyEventMembers(String eventId, String msgContent) {

    String eventName = find(eventId).orElseThrow(ContentNotFoundException::new).getName();
    Map<String, Object> messageDataMap = new HashMap<>();
    messageDataMap.put("content", msgContent);

    eventMemberService.findAllByEventId(eventId).stream()
        .filter(eventMember -> eventMember.isAccepted() && !eventMember.getEventMemberPermission().equals(OWNER))
        .forEach(eventMember -> notifyMember(msgContent, eventName, messageDataMap, eventMember));
  }

  private void notifyMember(String msgContent, String eventName, Map<String, Object> messageDataMap, EventMember eventMember) {
    User member = userService.find(eventMember.getUserId()).orElseThrow(ContentNotFoundException::new);
    firebaseMessagingService.sendSimplePushNotification(eventName, msgContent, eventMember.getUserId());

    messageDataMap.put("memberName", member.getFirstName());

    emailService.sendEmailMessage(EmailMessageType.ACTION_NOTIFICATION, messageDataMap, member.getEmail(), eventName);

    Notification notification = new Notification();
    notification.setContent(msgContent);
    notification.setTitle("Event information");
    notification.setUserId(eventMember.getUserId());
    notification.setNotificationType(NotificationType.INFO);

    notificationService.save(notification);
  }

  @Override
  public void declineEventInvitation(String eventId, String userId) {
    User member = userService.find(userId).orElseThrow(ContentNotFoundException::new);
    String eventOwnerId = eventMemberService.findByEventIdAndMemberPermission(eventId, OWNER).get(0).getUserId();
    String eventName = find(eventId).orElseThrow(ContentNotFoundException::new).getName();

    declineInvitation(member, userId, eventOwnerId, eventName);
  }

  @Override
  public void acceptEventInvitation(String eventId, String userId) {
    User member = userService.find(userId).orElseThrow(ContentNotFoundException::new);
    String eventOwnerId = eventMemberService.findByEventIdAndMemberPermission(eventId, OWNER).get(0).getUserId();
    EventMember eventMember = eventMemberService.findByEventAndUser(eventId, member.getUserId());
    Event event = find(eventId).orElseThrow(ContentNotFoundException::new);

    acceptInvitation(member, eventOwnerId, eventMember, event);
  }

  @Override
  public EventTimeConflictDTO eventTimeValidation(String userId, LocalDateTime startTime, LocalDateTime endTime) {

    EventTimeConflictDTO eventTimeConflictDTO = new EventTimeConflictDTO();

    List<EventPreviewDTO> eventPreview = findUserEvents(userId, null, null).stream()
        .filter(event -> SmartUtils.isEventTimeIntersectInterval(startTime, endTime, event.getStartDate(), event.getEndDate()))
        .map(event -> mapToEventPreviewDTO(event, userId))
        .collect(Collectors.toList());

    eventTimeConflictDTO.setInterceptedEvents(eventPreview);

    return eventTimeConflictDTO;
  }

  @Override
  public List<LocalDate> getDaysWithEvents(String userId, Month month) {

    List<LocalDate> daysWithEvents;

    if (isNull(month)) {

      daysWithEvents = eventRepository.findAll().stream()
          .flatMap(event -> Stream.of(event.getStartDate().toLocalDate(), event.getEndDate().toLocalDate()))
          .distinct()
          .collect(Collectors.toList());

    } else {

      YearMonth yearMonth = YearMonth.of(2019, month);
      LocalDateTime start = LocalDateTime.of(yearMonth.atDay(1), LocalTime.MIN);
      LocalDateTime end = LocalDateTime.of(yearMonth.atEndOfMonth(), LocalTime.MAX);

      daysWithEvents = eventRepository.findAllByStartDateBetween(start, end).stream()
          .flatMap(event -> Stream.of(event.getStartDate().toLocalDate(), event.getEndDate().toLocalDate()))
          .distinct()
          .collect(Collectors.toList());
    }

    return daysWithEvents;
  }

  @Override
  public void deleteMemberEvent(String eventId, String memberId) {

    eventMemberService.removeByUserIdAndEventId(memberId, eventId);
  }

  @Override
  public void updateEvent(String id, EventDTO eventDTO) {

    Event event = find(id).orElseThrow(ContentNotFoundException::new);

    eventLocationService.update(event.getLocationId(), eventDTO.getEventLocation());

    event.setDescription(eventDTO.getDescription());
    event.setName(eventDTO.getName());
    event.setEndDate(eventDTO.getEndDate());
    event.setStartDate(eventDTO.getStartDate());
    event.setEventCategory(eventDTO.getEventCategory());
    event.setEnableReminders(eventDTO.isEnableReminders());
  }

  private void acceptInvitation(User member, String eventOwnerId, EventMember eventMember, Event event) {
    if (!eventMember.isAccepted()) {

      EventTimeConflictDTO eventTimeConflictDTO = eventTimeValidation(member.getUserId(), event.getStartDate(), event.getEndDate());
      if (!eventTimeConflictDTO.getInterceptedEvents().isEmpty()) {

        throw new EventTimeConflictException("Invited event time conflicts with your existing events");
      }

      eventMember.setAccepted(true);
      eventMemberService.save(eventMember);

      firebaseMessagingService.sendSimplePushNotification("Member accept invitation",
          member.getFirstName() + " " + member.getLastName() + " has accepted invitation to event"
              + event.getName(), eventOwnerId);

      Notification notification = new Notification();
      notification.setContent(member.getFirstName() + " " + member.getLastName() + " has accepted your invitation to the event " + event.getName());
      notification.setTitle("Event invitation");
      notification.getAdditionalParameters().put("eventId", event.getId());
      notification.setUserId(eventOwnerId);
      notification.setNotificationType(NotificationType.INFO);

      notificationService.save(notification);
    }
  }

  private boolean isCode(String code, String hashed) {

    return code != null && hashed != null && hashed.equals(code);
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

    long activeMemberCount = eventMemberService.findAllByEventId(event.getId()).stream()
        .filter(eventMember -> !eventMember.getEventMemberPermission().equals(OWNER) && eventMember.isAccepted())
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

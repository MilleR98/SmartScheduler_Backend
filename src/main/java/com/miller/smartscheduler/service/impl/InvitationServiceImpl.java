package com.miller.smartscheduler.service.impl;

import static com.miller.smartscheduler.util.SmartUtils.SERVER_HOST;

import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.model.EventLocation;
import com.miller.smartscheduler.model.User;
import com.miller.smartscheduler.model.dto.EventMemberDTO;
import com.miller.smartscheduler.model.type.EmailMessageType;
import com.miller.smartscheduler.service.EmailService;
import com.miller.smartscheduler.service.InvitationService;
import com.miller.smartscheduler.util.SmartUtils;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

  private final EmailService emailService;

  @Override
  public void sendEventInvitation(EventMemberDTO eventMemberDTO, Event event, EventLocation eventLocation, User eventOwner) {

    String timestamp = String.valueOf(System.currentTimeMillis() / (Math.random() * SmartUtils.SECURITY_NUMBER));
    String sha2LinkCode = SmartUtils.generateSecurityCode(eventMemberDTO.getMemberEmail().toLowerCase(), event.getId(), timestamp);

    Map<String, Object> invitationDataMap = buildInvitationDataMap(eventMemberDTO, event, eventLocation, eventOwner);

    invitationDataMap.put("timestamp", timestamp);
    invitationDataMap.put("sha2LinkCode", sha2LinkCode);

    emailService.sendEmailMessage(EmailMessageType.EVENT_INVITATION, invitationDataMap, eventMemberDTO.getMemberEmail(), event.getName());
  }

  private Map<String, Object> buildInvitationDataMap(EventMemberDTO eventMemberDTO, Event event, EventLocation eventLocation, User eventOwner) {
    Map<String, Object> invitationDataMap = new HashMap<>();
    invitationDataMap.put("ownerEmail", eventOwner.getEmail());
    invitationDataMap.put("memberEmail", eventMemberDTO.getMemberEmail());
    invitationDataMap.put("ownerFullName", eventOwner.getFirstName() + " " + eventOwner.getLastName());
    invitationDataMap.put("memberName", eventMemberDTO.getFirstName());
    invitationDataMap.put("memberPermission", eventMemberDTO.getMemberPermission().name());
    invitationDataMap.put("eventDescription", event.getDescription());
    invitationDataMap.put("eventName", event.getName());
    invitationDataMap.put("startDate", event.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
    invitationDataMap.put("endDate", event.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
    String address = eventLocation.getCountry() + ", " + eventLocation.getCity() + ", " + eventLocation.getStreet() + " street, " + eventLocation.getBuildingNumber();
    invitationDataMap.put("eventAddress", address);
    invitationDataMap.put("serverHost", SERVER_HOST);
    invitationDataMap.put("eventId", event.getId());

    return invitationDataMap;
  }
}

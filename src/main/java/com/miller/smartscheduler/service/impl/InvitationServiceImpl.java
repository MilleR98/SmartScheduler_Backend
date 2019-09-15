package com.miller.smartscheduler.service.impl;

import static com.miller.smartscheduler.util.SmartUtils.SERVER_HOST;

import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.model.EventLocation;
import com.miller.smartscheduler.model.User;
import com.miller.smartscheduler.model.dto.EventMemberDTO;
import com.miller.smartscheduler.model.dto.Invitation;
import com.miller.smartscheduler.service.EmailService;
import com.miller.smartscheduler.service.InvitationService;
import com.miller.smartscheduler.util.SmartUtils;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

  private static final String acceptEventUrl = SERVER_HOST + "/events/email-invitation/accept";
  private static final String declineEventUrl = SERVER_HOST + "/events/email-invitation/decline";
  private final EmailService emailService;
  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  @Override
  public void sendEventInvitation(EventMemberDTO eventMemberDTO, Event event, EventLocation eventLocation, User eventOwner) {

    String timestamp = String.valueOf(System.currentTimeMillis() / (Math.random() * SmartUtils.SECURITY_NUMBER));
    String sha2LinkCode = SmartUtils.generateSecurityCode(eventMemberDTO.getMemberEmail().toLowerCase(), event.getId(), timestamp);

    String address = eventLocation.getCountry() + ", " + eventLocation.getCity() + ", " + eventLocation.getStreet() + " street, " + eventLocation.getBuildingNumber();

    String acceptLink = acceptEventUrl + "?eventId="+event.getId()+"&code="+sha2LinkCode+"&time="+timestamp+"&email="+eventMemberDTO.getMemberEmail()+"&eventName="+event.getName();
    String declineLink = declineEventUrl + "?eventId="+event.getId()+"&code="+sha2LinkCode+"&time="+timestamp+"&email="+eventMemberDTO.getMemberEmail()+"&eventName="+event.getName();
    Invitation invitation = Invitation.builder()
        .ownerEmail(eventOwner.getEmail())
        .memberEmail(eventMemberDTO.getMemberEmail())
        .ownerFullName(eventOwner.getFirstName() + " " + eventOwner.getLastName())
        .memberName(eventMemberDTO.getFirstName())
        .memberPermission(eventMemberDTO.getMemberPermission().name())
        .eventDescription(event.getDescription())
        .eventName(event.getName())
        .startDate(event.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
        .endDate(event.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
        .eventAddress(address)
        .serverHost(SERVER_HOST)
        .eventId(event.getId())
        .timestamp(timestamp)
        .sha2LinkCode(sha2LinkCode)
        .acceptLink(acceptLink)
        .declineLink(declineLink)
        .build();

    MimeMessagePreparator messagePreparator = buildMailMessage(invitation);
    mailSender.send(messagePreparator);
  }

  private MimeMessagePreparator buildMailMessage(Invitation invitation) {

    return mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
      messageHelper.setTo(invitation.getMemberEmail());
      messageHelper.setSubject(invitation.getEventName());
      String content = buildContent(invitation);
      messageHelper.setText(content, true);
    };
  }

  private String buildContent(Invitation invitation) {
    Context context = new Context();

    context.setVariable("invitation", invitation);

    return templateEngine.process("event-invitation", context);
  }
}

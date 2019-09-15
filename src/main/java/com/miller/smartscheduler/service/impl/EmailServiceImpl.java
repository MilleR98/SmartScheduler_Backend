package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.error.exception.BadRequestException;
import com.miller.smartscheduler.model.type.EmailMessageType;
import com.miller.smartscheduler.service.EmailService;
import com.sun.mail.smtp.SMTPTransport;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private static final String FROM_EMAIL = "melnyk.dev@gmail.com";
  private static final String PASSWORD = "developer1998";
  private static final String HOST = "smtp.gmail.com";
  private static final String PORT = "465";

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  private static MimeMessage getBaseMimeMessage(String to, Session session) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(FROM_EMAIL, "SmartScheduler"));
    InternetAddress[] toAddresses = {new InternetAddress(to)};
    message.setRecipients(Message.RecipientType.TO, toAddresses);

    return message;
  }

  private static void setSMTPTtransport(Session session, MimeMessage message) throws MessagingException {

    SMTPTransport transport = (SMTPTransport) session.getTransport("smtps");
    transport.connect(HOST, FROM_EMAIL, PASSWORD);
    transport.sendMessage(message, message.getAllRecipients());
    transport.close();
  }

  private static String readActivationResources(String fileName) {
    StringBuilder fileContent = new StringBuilder();
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(EmailServiceImpl.class.getClassLoader().getResourceAsStream(fileName)))) {

      String line;

      while ((line = br.readLine()) != null) {
        fileContent.append(line).append("\n");
      }

    } catch (Exception e) {
      log.debug(e.getMessage());
    }

    return fileContent.toString();
  }

  private static Properties getProperties() {
    Properties property = System.getProperties();
    property.put("mail.smtp.host", HOST);
    property.put("mail.smtp.port", PORT);
    property.put("mail.smtp.auth", "true");
    property.put("mail.smtp.starttls.enable", "true");

    return property;
  }

  @Override
  public void sendEmailMessage(EmailMessageType emailMessageType, Map<String, Object> messageDataMap, String recipientEmail, String subject) {

    Session session = Session.getDefaultInstance(getProperties());
    try {
      MimeMessage message = getBaseMimeMessage(recipientEmail, session);
      message.setSubject("Lean-Case Sharing invitation");
      message.setSentDate(new Date());

      String activationPage = readActivationResources(emailMessageType.getTemplateName());

      String formattedProjectActivationBody = StrSubstitutor.replace(activationPage, messageDataMap);

      message.setSubject(subject);
      message.setContent(formattedProjectActivationBody, "text/html");

      setSMTPTtransport(session, message);

    } catch (Exception mex) {

      throw new BadRequestException("Invitation error");
    }
  }

}

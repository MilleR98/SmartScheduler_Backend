package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.type.EmailMessageType;
import com.miller.smartscheduler.service.EmailService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  @Override
  public void sendEmailMessage(EmailMessageType emailMessageType, Map<String, Object> messageDataMap, String recipientEmail, String subject) {

    Context context = new Context();

    context.setVariables(messageDataMap);

    String templateContent = templateEngine.process(emailMessageType.getTemplateName(), context);

    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
      messageHelper.setTo(recipientEmail);
      messageHelper.setSubject(subject);
      messageHelper.setText(templateContent, true);
    };

    mailSender.send(messagePreparator);
  }
}

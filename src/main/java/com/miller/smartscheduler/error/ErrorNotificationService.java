package com.miller.smartscheduler.error;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class ErrorNotificationService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  @Value("${smartbot.error-notification.emails}")
  private String[] emailReceivers;

  @Value("${smartbot.error-notification.subject}")
  private String errorSubject;

  public void sendErrorReport(AndroidErrorReport androidErrorReport) {

    //MimeMessagePreparator messagePreparator = buildMailMessage(androidErrorReport);
    //mailSender.send(messagePreparator);
  }

  private MimeMessagePreparator buildMailMessage(AndroidErrorReport androidErrorReport) {

    return mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
      messageHelper.setTo(emailReceivers);
      messageHelper.setSubject(errorSubject);
      String content = buildContent(androidErrorReport);
      messageHelper.setText(content, true);
    };
  }

  private String buildContent(AndroidErrorReport androidErrorReport) {
    Context context = new Context();

    context.setVariable("errorReport", androidErrorReport);

    return templateEngine.process("error-message", context);
  }
}

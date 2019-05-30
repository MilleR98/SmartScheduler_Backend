package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.type.EmailMessageType;
import java.util.Map;

public interface EmailService {

  void sendEmailMessage(EmailMessageType emailMessageType, Map<String, Object> messageDataMap, String recipientEmail, String subject);
}

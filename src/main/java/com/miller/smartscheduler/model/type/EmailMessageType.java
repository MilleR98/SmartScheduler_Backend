package com.miller.smartscheduler.model.type;

public enum EmailMessageType {

  EVENT_INVITATION("templates/event-invitation"),
  ACTION_NOTIFICATION("action-notification");

  public String getTemplateName() {
    return templateName;
  }

  private String templateName;

  EmailMessageType(String templateName) {
    this.templateName = templateName;
  }
}

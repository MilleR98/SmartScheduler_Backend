package com.miller.smartscheduler.model.type;

public enum UserType {

  GUEST_USER,
  SIMPLE_USER,
  PREMIUM_USER,
  SYS_ADMIN;

  public String getFullName() {
    return "ROLE_" + this.name();
  }
}

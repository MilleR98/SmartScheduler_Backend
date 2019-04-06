package com.miller.smartscheduler.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class AndroidErrorReport {

  @JsonProperty("PHONE_MODEL")
  private String phoneModel;
  @JsonProperty("BRAND")
  private String brand;
  @JsonProperty("ANDROID_VERSION")
  private String androidVersion;
  @JsonProperty("STACK_TRACE")
  private String exceptionStackTrace;
  @JsonProperty("USER_COMMENT")
  private String userComment;
  @JsonProperty("USER_APP_START_DATE")
  private ZonedDateTime appStartDate;
  @JsonProperty("USER_CRASH_DATE")
  private ZonedDateTime appCrashDate;
}

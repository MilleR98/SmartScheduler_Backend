package com.miller.smartscheduler.model;

import lombok.Data;

@Data
public class EventLocation {

  private String id;
  private String country;
  private String city;
  private String street;
  private String buildingNumber;
}

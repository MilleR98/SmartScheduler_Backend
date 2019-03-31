package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.EventLocation;
import com.miller.smartscheduler.repository.EventLocationRepository;
import com.miller.smartscheduler.service.EventLocationService;
import org.springframework.stereotype.Service;

@Service
public class EventLocationServiceImpl extends CommonServiceImpl<EventLocation> implements EventLocationService {

  private final EventLocationRepository eventLocationRepository;

  public EventLocationServiceImpl(EventLocationRepository eventLocationRepository) {
    super(eventLocationRepository);
    this.eventLocationRepository = eventLocationRepository;
  }
}

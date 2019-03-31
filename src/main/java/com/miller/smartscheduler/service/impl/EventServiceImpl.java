package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.Event;
import com.miller.smartscheduler.repository.EventRepository;
import com.miller.smartscheduler.service.EventService;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl extends CommonServiceImpl<Event> implements EventService {

  private final EventRepository eventRepository;

  public EventServiceImpl(EventRepository eventRepository) {
    super(eventRepository);
    this.eventRepository = eventRepository;
  }
}

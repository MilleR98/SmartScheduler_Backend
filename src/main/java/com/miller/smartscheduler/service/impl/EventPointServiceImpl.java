package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.EventPoint;
import com.miller.smartscheduler.repository.EventPointRepository;
import com.miller.smartscheduler.service.EventPointService;
import org.springframework.stereotype.Service;

@Service
public class EventPointServiceImpl extends CommonServiceImpl<EventPoint> implements EventPointService {

  private final EventPointRepository eventPointRepository;

  public EventPointServiceImpl(EventPointRepository eventPointRepository) {
    super(eventPointRepository);
    this.eventPointRepository = eventPointRepository;
  }
}

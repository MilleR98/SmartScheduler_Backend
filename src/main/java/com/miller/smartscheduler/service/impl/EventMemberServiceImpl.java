package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.EventMember;
import com.miller.smartscheduler.repository.EventMemberRepository;
import com.miller.smartscheduler.service.EventMemberService;
import org.springframework.stereotype.Service;

@Service
public class EventMemberServiceImpl extends CommonServiceImpl<EventMember> implements EventMemberService {

  private final EventMemberRepository eventMemberRepository;

  public EventMemberServiceImpl(EventMemberRepository eventMemberRepository) {
    super(eventMemberRepository);
    this.eventMemberRepository = eventMemberRepository;
  }
}

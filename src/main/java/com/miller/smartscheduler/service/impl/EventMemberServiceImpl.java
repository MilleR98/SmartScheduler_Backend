package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.EventMember;
import com.miller.smartscheduler.model.type.EventMemberPermission;
import com.miller.smartscheduler.repository.EventMemberRepository;
import com.miller.smartscheduler.service.EventMemberService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventMemberServiceImpl extends CommonServiceImpl<EventMember> implements EventMemberService {

  private final EventMemberRepository eventMemberRepository;

  public EventMemberServiceImpl(EventMemberRepository eventMemberRepository) {
    super(eventMemberRepository);
    this.eventMemberRepository = eventMemberRepository;
  }

  @Override
  public List<EventMember> findAllByUserId(String memberId) {

    return eventMemberRepository.findAllByUserId(memberId);
  }

  @Override
  public List<EventMember> findAllByEventId(String eventId) {

    return eventMemberRepository.findAllByEventId(eventId);
  }

  @Override
  public void removeByUserIdAndEventId(String userId, String eventId) {

    eventMemberRepository.removeByUserIdAndEventId(userId, eventId);
  }

  @Override
  public void removeAllByEventId(String eventId) {

    eventMemberRepository.removeAllByEventId(eventId);
  }

  @Override
  public List<EventMember> findByEventIdAndMemberPermission(String eventId, EventMemberPermission eventMemberPermission) {

    return eventMemberRepository.findByEventIdAndEventMemberPermission(eventId, eventMemberPermission);
  }

  @Override
  public EventMember findByEventAndUser(String eventId, String userId) {

    return eventMemberRepository.findByEventIdAndUserId(eventId, userId);
  }
}

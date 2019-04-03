package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.EventMember;
import com.miller.smartscheduler.model.type.EventMemberPermission;
import java.util.List;

public interface EventMemberService extends CommonService<EventMember> {

  List<EventMember> findAllByUserId(String memeberId);

  List<EventMember> findAllEventId(String eventId);

  void removeAllByEventId(String eventId);

  List<EventMember> findByEventIdAndMemberPermission(String eventId, EventMemberPermission eventMemberPermission);

  EventMember findByEventAndUser(String eventId, String userId);
}
package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.EventMember;
import com.miller.smartscheduler.model.type.EventMemberPermission;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventMemberRepository extends MongoRepository<EventMember, String> {

  List<EventMember> findAllByUserId(String memeberId);

  List<EventMember> findAllByEventId(String eventId);

  void removeAllByEventId(String eventId);

  List<EventMember> findByEventIdAndEventMemberPermission(String eventId, EventMemberPermission eventMemberPermission);

  EventMember findByEventIdAndUserId(String eventId, String userId);
}

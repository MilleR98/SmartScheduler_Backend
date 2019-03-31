package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.EventMember;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventMemberRepository extends MongoRepository<EventMember, String> {

}

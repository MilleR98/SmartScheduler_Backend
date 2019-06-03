package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.Event;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

  List<Event> findAllByStartDate(LocalDateTime startDate);

  List<Event> findAllByStartDateBetween(LocalDateTime searchStartDate, LocalDateTime searchEndDate);

  List<Event> findAllByIdIn(List<String> eventIds);
}

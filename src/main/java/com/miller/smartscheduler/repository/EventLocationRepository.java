package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.EventLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventLocationRepository extends MongoRepository<EventLocation, String> {

}

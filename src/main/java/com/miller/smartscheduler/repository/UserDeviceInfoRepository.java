package com.miller.smartscheduler.repository;

import com.miller.smartscheduler.model.UserDeviceInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDeviceInfoRepository extends MongoRepository<UserDeviceInfo, String> {

  List<UserDeviceInfo> findAllByUserId(String userId);

  Optional<UserDeviceInfo> findByDeviceIdAndUserId(String deviceId, String userId);
}

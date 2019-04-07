package com.miller.smartscheduler.service;

import com.miller.smartscheduler.model.UserDeviceInfo;
import java.util.List;
import java.util.Optional;

public interface UserDeviceInfoService extends CommonService<UserDeviceInfo> {

  List<UserDeviceInfo> getAllUserDevices(String userId);

  Optional<UserDeviceInfo> findByDeviceIdAndUserId(String deviceId, String userId);
}

package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.model.UserDeviceInfo;
import com.miller.smartscheduler.repository.UserDeviceInfoRepository;
import com.miller.smartscheduler.service.UserDeviceInfoService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserDeviceInfoServiceImpl extends CommonServiceImpl<UserDeviceInfo> implements UserDeviceInfoService {

  private final UserDeviceInfoRepository userDeviceInfoRepository;

  public UserDeviceInfoServiceImpl(UserDeviceInfoRepository userDeviceInfoRepository) {
    super(userDeviceInfoRepository);
    this.userDeviceInfoRepository = userDeviceInfoRepository;
  }

  @Override
  public List<UserDeviceInfo> getAllUserDevices(String userId) {

    return userDeviceInfoRepository.findAllByUserId(userId);
  }

  @Override
  public Optional<UserDeviceInfo> findByDeviceIdAndUserId(String deviceId, String userId) {

    return userDeviceInfoRepository.findByDeviceIdAndUserId(deviceId, userId);
  }
}

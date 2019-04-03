package com.miller.smartscheduler.service;

import java.util.List;
import java.util.Optional;

public interface CommonService<T> {

  Optional<T> find(String id);

  List<T> findAll();

  void save(T object);

  void update(String id, T object);

  T saveAndReturn(T object);

  void remove(String id);
}

package com.miller.smartscheduler.service;

import java.util.List;
import java.util.Optional;

public interface CommonService<T> {

  Optional<T> find(String id);

  T getOrFail(String id);

  List<T> findAll();

  void save(T object);

  T saveAndReturn(T object);

  void remove(T object);
}

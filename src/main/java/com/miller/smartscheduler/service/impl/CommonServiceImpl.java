package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.service.CommonService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;

@RequiredArgsConstructor
public class CommonServiceImpl<T> implements CommonService<T> {

  private final MongoRepository<T, String> mongoRepository;

  @Override
  public Optional<T> find(String id) {

    return mongoRepository.findById(id);
  }

  @Override
  public List<T> findAll() {

    return mongoRepository.findAll();
  }

  @Override
  public void save(T object) {

    mongoRepository.save(object);
  }

  @Override
  public T saveAndReturn(T object) {

    return mongoRepository.save(object);
  }

  @Override
  public void remove(T object) {

    mongoRepository.delete(object);
  }
}

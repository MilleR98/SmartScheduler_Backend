package com.miller.smartscheduler.controller;

import com.miller.smartscheduler.error.exception.ContentNotFoundException;
import com.miller.smartscheduler.model.SimpleTask;
import com.miller.smartscheduler.model.Subtask;
import com.miller.smartscheduler.model.dto.CreateTaskDTO;
import com.miller.smartscheduler.model.dto.TaskInfoDTO;
import com.miller.smartscheduler.model.dto.TaskPreviewDTO;
import com.miller.smartscheduler.service.SimpleTaskService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class SimpleTaskController {

  private final SimpleTaskService simpleTaskService;

  @GetMapping
  public List<SimpleTask> getUserTasks(@RequestHeader("userId") String userId) {

    return simpleTaskService.getUserTasks(userId);
  }

  @GetMapping("/previews")
  public List<TaskPreviewDTO> getUserTaskPreviews(@RequestHeader("userId") String userId) {

    return simpleTaskService.getUserTaskPreviews(userId);
  }

  @GetMapping("/{id}")
  public TaskInfoDTO getTaskInfo(@PathVariable("id") String id) {

    return simpleTaskService.getTaskInfo(id);
  }

  @PostMapping
  public ResponseEntity createTask(@RequestBody CreateTaskDTO createTaskDTO, @RequestHeader("userId") String userId) {

    simpleTaskService.save(createTaskDTO, userId);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity updateTask(@PathVariable("id") String id, @RequestBody SimpleTask simpleTask) {

    simpleTaskService.update(id, simpleTask);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity removeTask(@PathVariable("id") String id) {

    simpleTaskService.remove(id);

    return new ResponseEntity(HttpStatus.OK);
  }
}

package com.miller.smartscheduler.controller;

import com.miller.smartscheduler.error.exception.ContentNotFoundException;
import com.miller.smartscheduler.model.SimpleTask;
import com.miller.smartscheduler.model.Subtask;
import com.miller.smartscheduler.service.SubtaskService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subtasks")
@RequiredArgsConstructor
public class SubtaskController {

  private final SubtaskService subtaskService;

  @GetMapping
  public List<Subtask> getTaskSubtasks(@RequestParam("taskId") String taskId) {

    return subtaskService.getSubtaskByTaskId(taskId);
  }

  @GetMapping("/{id}")
  public Subtask getSubtasksInfo(@PathVariable("id") String id) {

    return subtaskService.find(id).orElseThrow(ContentNotFoundException::new);
  }

  @PostMapping
  public ResponseEntity createTask(@RequestBody Subtask simpleTask) {

    subtaskService.save(simpleTask);

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity updateSubtask(@PathVariable("id") String id, @RequestBody Subtask subtask) {

    subtaskService.update(id, subtask);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity removeSubtask(@PathVariable("id") String id) {

    subtaskService.remove(id);

    return new ResponseEntity(HttpStatus.OK);
  }
}

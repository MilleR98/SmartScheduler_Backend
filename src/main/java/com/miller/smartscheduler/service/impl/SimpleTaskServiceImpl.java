package com.miller.smartscheduler.service.impl;

import static com.miller.smartscheduler.util.DateUtil.convertToDate;
import static com.miller.smartscheduler.util.DateUtil.convertToLocalDateTime;

import com.miller.smartscheduler.model.SimpleTask;
import com.miller.smartscheduler.model.Subtask;
import com.miller.smartscheduler.model.dto.CreateTaskDTO;
import com.miller.smartscheduler.model.dto.TaskPreviewDTO;
import com.miller.smartscheduler.model.type.SubtaskStatus;
import com.miller.smartscheduler.repository.SimpleTaskRepository;
import com.miller.smartscheduler.service.FirebaseMessagingService;
import com.miller.smartscheduler.service.SimpleTaskService;
import com.miller.smartscheduler.service.SubtaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SimpleTaskServiceImpl extends CommonServiceImpl<SimpleTask> implements SimpleTaskService {

  private final SimpleTaskRepository simpleTaskRepository;
  private final SubtaskService subtaskService;
  private final FirebaseMessagingService firebaseMessagingService;

  public SimpleTaskServiceImpl(SimpleTaskRepository simpleTaskRepository, SubtaskService subtaskService,
      FirebaseMessagingService firebaseMessagingService) {
    super(simpleTaskRepository);
    this.simpleTaskRepository = simpleTaskRepository;
    this.subtaskService = subtaskService;
    this.firebaseMessagingService = firebaseMessagingService;
  }

  @Override
  public List<SimpleTask> getUserTasks(String userId) {

    return simpleTaskRepository.findAllByUserId(userId);
  }

  @Override
  public void save(CreateTaskDTO createTaskDTO, String userId) {

    LocalDateTime deadlineDate = convertToLocalDateTime(createTaskDTO.getDeadlineDate());
    LocalDateTime reminderTime = convertToLocalDateTime(createTaskDTO.getReminderTime());

    SimpleTask simpleTask = new SimpleTask();
    simpleTask.setUserId(userId);
    simpleTask.setDeadlineDate(deadlineDate);
    simpleTask.setReminderTime(reminderTime);
    simpleTask.setReminderType(createTaskDTO.getReminderType());
    simpleTask.setTitle(createTaskDTO.getTitle());
    simpleTask.setDescription(createTaskDTO.getDescription());

    String taskId = saveAndReturn(simpleTask).getId();

    createTaskDTO.getSubtaskList().forEach(subtask -> {
      subtask.setTaskId(taskId);
      subtaskService.save(subtask);
    });

    firebaseMessagingService.sendSimplePushNotification("Action successfull",
        "New task '" + simpleTask.getTitle() + "' created. Good luck with progress", userId);
  }

  @Override
  public List<TaskPreviewDTO> getUserTaskPreviews(String userId) {

    return getUserTasks(userId).stream()
        .map(this::mapToPreview)
        .collect(Collectors.toList());
  }

  private TaskPreviewDTO mapToPreview(SimpleTask simpleTask) {
    TaskPreviewDTO taskPreviewDTO = new TaskPreviewDTO();
    taskPreviewDTO.setDeadlineDate(convertToDate(simpleTask.getDeadlineDate()));
    taskPreviewDTO.setCreatedAt(convertToDate(simpleTask.getCreatedAt()));
    taskPreviewDTO.setTitle(simpleTask.getTitle());

    List<SubtaskStatus> subtaskStatuses = subtaskService.getSubtaskByTaskId(simpleTask.getId()).stream()
        .map(Subtask::getSubtaskStatus)
        .collect(Collectors.toList());

    taskPreviewDTO.setSubtaskStatuses(subtaskStatuses);

    return taskPreviewDTO;
  }

  @Override
  public void remove(String id) {
    super.remove(id);

    subtaskService.removeAllByTaskId(id);
  }
}

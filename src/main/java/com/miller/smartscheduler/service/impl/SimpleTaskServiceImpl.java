package com.miller.smartscheduler.service.impl;

import com.miller.smartscheduler.error.exception.ContentNotFoundException;
import com.miller.smartscheduler.model.ReminderConfig;
import com.miller.smartscheduler.model.SimpleTask;
import com.miller.smartscheduler.model.Subtask;
import com.miller.smartscheduler.model.dto.CreateTaskDTO;
import com.miller.smartscheduler.model.dto.TaskInfoDTO;
import com.miller.smartscheduler.model.dto.TaskPreviewDTO;
import com.miller.smartscheduler.model.type.SubtaskStatus;
import com.miller.smartscheduler.repository.SimpleTaskRepository;
import com.miller.smartscheduler.service.FirebaseMessagingService;
import com.miller.smartscheduler.service.SimpleTaskService;
import com.miller.smartscheduler.service.SubtaskService;
import com.miller.smartscheduler.util.ReminderTimeUtil;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SimpleTaskServiceImpl extends CommonServiceImpl<SimpleTask> implements SimpleTaskService {

  private final SimpleTaskRepository simpleTaskRepository;
  private final ScheduledExecutorService executor;
  private final SubtaskService subtaskService;
  private final FirebaseMessagingService firebaseMessagingService;

  public SimpleTaskServiceImpl(SimpleTaskRepository simpleTaskRepository, ScheduledExecutorService executor, SubtaskService subtaskService,
      FirebaseMessagingService firebaseMessagingService) {
    super(simpleTaskRepository);
    this.simpleTaskRepository = simpleTaskRepository;
    this.executor = executor;
    this.subtaskService = subtaskService;
    this.firebaseMessagingService = firebaseMessagingService;
  }

  @Override
  public List<SimpleTask> getUserTasks(String userId) {

    return simpleTaskRepository.findAllByUserId(userId);
  }

  @Override
  public void save(CreateTaskDTO createTaskDTO, String userId) {

    SimpleTask simpleTask = new SimpleTask();
    simpleTask.setUserId(userId);
    simpleTask.setDeadlineDate(createTaskDTO.getDeadlineDate());
    simpleTask.setReminderConfig(new ReminderConfig(createTaskDTO.getReminderType(), createTaskDTO.getReminderTime()));
    simpleTask.setTitle(createTaskDTO.getTitle());
    simpleTask.setDescription(createTaskDTO.getDescription());

    String taskId = saveAndReturn(simpleTask).getId();

    createTaskDTO.getSubtaskList().forEach(subtask -> {
      subtask.setTaskId(taskId);
      subtaskService.save(subtask);
    });

    List<Long> notificationTimeValues = ReminderTimeUtil.calculateScheduledReminderTimes(simpleTask.getDeadlineDate(), simpleTask.getReminderConfig());

    notificationTimeValues.forEach(reminderDelay ->
        executor.schedule(() -> remindAboutTask(simpleTask), reminderDelay, TimeUnit.MINUTES));
  }

  private void remindAboutTask(SimpleTask simpleTask) {

    String notificationBody;
    if (simpleTask.getDeadlineDate().isEqual(simpleTask.getReminderConfig().getReminderTime())) {

      notificationBody = "Hey! Deadline for task " + simpleTask.getTitle();
    } else {

      notificationBody = "Hey! You have a task " + simpleTask.getTitle() + ". Deadline: " + simpleTask.getDeadlineDate();
    }

    firebaseMessagingService.sendSimplePushNotification(simpleTask.getTitle(),
        notificationBody,
        simpleTask.getUserId());
  }

  @Override
  public List<TaskPreviewDTO> getUserTaskPreviews(String userId) {

    return getUserTasks(userId).stream()
        .map(this::mapToPreview)
        .collect(Collectors.toList());
  }

  @Override
  public TaskInfoDTO getTaskInfo(String id) {
    SimpleTask simpleTask = find(id).orElseThrow(() -> new ContentNotFoundException("Canot found task info"));
    List<Subtask> subtaskList = subtaskService.getSubtaskByTaskId(simpleTask.getId());

    TaskInfoDTO taskInfo = new TaskInfoDTO();
    taskInfo.setDeadlineDate(simpleTask.getDeadlineDate().withSecond(0).withNano(0));
    taskInfo.setCreatedAt(simpleTask.getCreatedAt().withSecond(0).withNano(0));
    taskInfo.setReminderTime(simpleTask.getCreatedAt().withSecond(0).withNano(0));
    taskInfo.setReminderType(simpleTask.getReminderConfig().getReminderType());
    taskInfo.setReminderTime(simpleTask.getReminderConfig().getReminderTime());
    taskInfo.setId(simpleTask.getId());
    taskInfo.setTitle(simpleTask.getTitle());
    taskInfo.setDescription(simpleTask.getDescription());
    taskInfo.setSubtaskList(subtaskList);

    return taskInfo;
  }

  private TaskPreviewDTO mapToPreview(SimpleTask simpleTask) {
    TaskPreviewDTO taskPreviewDTO = new TaskPreviewDTO();
    taskPreviewDTO.setDeadlineDate(simpleTask.getDeadlineDate().withSecond(0).withNano(0));
    taskPreviewDTO.setCreatedAt(simpleTask.getCreatedAt().withSecond(0).withNano(0));
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

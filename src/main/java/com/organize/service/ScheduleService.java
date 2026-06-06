package com.organize.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.organize.dto.request.ScheduleTaskRequest;
import com.organize.dto.response.ConflictTaskResponse;
import com.organize.dto.response.ScheduleResult;
import com.organize.dto.response.TaskResponse;
import com.organize.entity.Goal;
import com.organize.entity.Task;
import com.organize.exception.InvalidTaskScheduleException;
import com.organize.exception.TaskNotFoundException;
import com.organize.repository.GoalRepository;
import com.organize.repository.TaskRepository;
import com.organize.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    
    private final TaskRepository taskRepository;
    private final GoalRepository goalRepository;
    
    private String getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser().getId();
        
    }

    private Task getTaskByIdAndUserId(String taskId, String userId) {

        return taskRepository.findByIdAndUserId(taskId, userId)
                                .orElseThrow(
                                    () -> new TaskNotFoundException("Task not found")
                                );
    }

    private void validateScheduleRequest(ScheduleTaskRequest request) {

        if(request.getScheduledStart() == null || request.getScheduledEnd() == null) {
            throw new InvalidTaskScheduleException("Shcedule start and end are required");
        }

        if(!request.getScheduledEnd().isAfter(request.getScheduledStart())) {
            throw new InvalidTaskScheduleException("Scheduled end must be after scheduled start");
        }
    }

    private List<ConflictTaskResponse> findConflictingTasks(String userId, 
                                        String currentTaskId, 
                                        LocalDateTime start, 
                                        LocalDateTime end) {

        List<Task> scheduledTasks = taskRepository.findByUserIdAndScheduledStartIsNotNull(userId);

        List<ConflictTaskResponse> conflicts = new ArrayList<>();

        for(Task task : scheduledTasks) {
            if(task.getId().equals(currentTaskId)) {
                continue;
            }

            boolean overlaps = start.isBefore(task.getScheduledEnd()) && end.isAfter(task.getScheduledStart());

            if(overlaps) {
                conflicts.add(mapToConflictResponse(task));
            }
        }

        return conflicts;
    }

    private String getGoalTitle(String goalId) {

        return goalRepository.findById(goalId)
                .map(Goal::getTitle)
                .orElse("Unknown Goal");
    }

    private ConflictTaskResponse mapToConflictResponse(Task task) {
        
        return new ConflictTaskResponse(task.getId(), 
                                        task.getTitle(), 
                                        task.getScheduledStart(), 
                                        task.getScheduledEnd());
    }

    private TaskResponse mapToTaskResponse(Task task, String goalTitle) {

        return new TaskResponse(
                task.getId(),
                task.getGoalId(),
                goalTitle,
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getEstimatedDurationMinutes(),
                task.getScheduledStart(),
                task.getScheduledEnd(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }



    public ScheduleResult scheduleTask(String taskId, ScheduleTaskRequest request) {

        String userId = getCurrentUserId();

        Task task = getTaskByIdAndUserId(taskId, userId);

        validateScheduleRequest(request);

        List<ConflictTaskResponse> conflicts = findConflictingTasks(
                                                    userId,
                                                    taskId,
                                                    request.getScheduledStart(),
                                                    request.getScheduledEnd()
                                                );

        if (!conflicts.isEmpty()) {

            return new ScheduleResult(
                    false,
                    null,
                    conflicts,
                    "Schedule conflict detected"
            );
        }

        task.setScheduledStart(request.getScheduledStart());

        task.setScheduledEnd(request.getScheduledEnd());

        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        String goalTitle = getGoalTitle(savedTask.getGoalId());

        return new ScheduleResult(
                true,
                mapToTaskResponse(
                        savedTask,
                        goalTitle
                ),
                Collections.emptyList(),
                "Task scheduled successfully"
        );
    }


    public ScheduleResult forceScheduleTask(String taskId, ScheduleTaskRequest request) {

        String userId = getCurrentUserId();

        Task task = getTaskByIdAndUserId(taskId, userId);

        validateScheduleRequest(request);

        task.setScheduledStart(request.getScheduledStart());

        task.setScheduledEnd(request.getScheduledEnd());

        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        String goalTitle = getGoalTitle(savedTask.getGoalId());

        return new ScheduleResult(
                true,
                mapToTaskResponse(
                        savedTask,
                        goalTitle
                ),
                Collections.emptyList(),
                "Task scheduled successfully"
        );
    }

    public List<TaskResponse> getTasksByDate(LocalDate date) {

        String userId = getCurrentUserId();

        LocalDateTime startOfDay = date.atStartOfDay();

        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Task> scheduledTasks = taskRepository.findByUserIdAndScheduledStartIsNotNull(userId);

        return scheduledTasks.stream()
                .filter(task ->
                        task.getScheduledStart().isBefore(endOfDay)
                        &&
                        task.getScheduledEnd().isAfter(startOfDay)
                )
                .sorted(
                        Comparator.comparing(
                                Task::getScheduledStart
                        )
                )
                .map(task -> {

                    String goalTitle =
                            getGoalTitle(
                                    task.getGoalId()
                            );

                    return mapToTaskResponse(
                            task,
                            goalTitle
                    );
                })
                .toList();
    }



    public List<TaskResponse> getUnscheduledTasks() {

        String userId = getCurrentUserId();

        List<Task> tasks = taskRepository.findByUserIdAndScheduledStartIsNull(userId);

        return tasks.stream()
                .map(task -> {
                    String goalTitle =
                            getGoalTitle(
                                    task.getGoalId()
                            );
                    return mapToTaskResponse(
                            task,
                            goalTitle
                    );
                })
                .toList();
    }
}

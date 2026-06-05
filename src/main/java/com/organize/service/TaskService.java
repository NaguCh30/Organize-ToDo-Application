package com.organize.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.organize.dto.request.CreateTaskRequest;
import com.organize.dto.request.UpdateTaskRequest;
import com.organize.dto.response.TaskResponse;
import com.organize.entity.Goal;
import com.organize.entity.Task;
import com.organize.entity.TaskPriority;
import com.organize.entity.TaskStatus;
import com.organize.exception.DuplicateTaskException;
import com.organize.exception.InvalidTaskScheduleException;
import com.organize.exception.TaskNotFoundException;
import com.organize.repository.TaskRepository;
import com.organize.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository taskRepository;
    
    private final GoalService goalService;

    private String getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser().getId();
        
    }

    private String normalizeTitle(String title) {
        title = title.strip();
        title = title.replaceAll("\\s+", " ");
        return title;
    }

    private TaskResponse mapToResponse(Task task, String goalTitle) {
        return new TaskResponse(
            task.getId(),
            task.getGoalId(),
            goalTitle,
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getScheduledStart(),
            task.getScheduledEnd(),
            task.getCreatedAt(),
            task.getUpdatedAt()
        );
    }

    private Task getTaskByIdAndUserId(String taskId, String userId) {

        return taskRepository.findByIdAndUserId(taskId, userId)
                        .orElseThrow(
                            () -> new TaskNotFoundException("Task not found")
                        );
    }



    public TaskResponse createTask(String goalId, CreateTaskRequest request) {

        String userId = getCurrentUserId();

        Goal goal = goalService.getGoalEntityByIdAndUserId(goalId, userId);

        String normalizedTitle = normalizeTitle(request.getTitle());

        if (taskRepository.existsByGoalIdAndTitleIgnoreCase(
                                                goalId,
                                                normalizedTitle
                                             )
            ) {
            throw new DuplicateTaskException("Task title already exists in this goal");
        }

        if (request.getScheduledStart() != null
                && request.getScheduledEnd() != null
                && request.getScheduledEnd()
                        .isBefore(request.getScheduledStart())) {

            throw new InvalidTaskScheduleException(
                    "Scheduled end must be after scheduled start"
            );
        }

        Task task = new Task();

        task.setUserId(userId);
        task.setGoalId(goalId);

        task.setTitle(normalizedTitle);
        task.setDescription(request.getDescription());

        task.setStatus(TaskStatus.NOT_STARTED);

        task.setPriority(
                request.getPriority() != null
                        ? request.getPriority()
                        : TaskPriority.MEDIUM
        );

        task.setScheduledStart(request.getScheduledStart());

        task.setScheduledEnd(request.getScheduledEnd());

        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        return mapToResponse(
                savedTask,
                goal.getTitle()
        );
    }



    public List<TaskResponse> getTasksByGoal(String goalId) {

        String userId = getCurrentUserId();

        Goal goal = goalService.getGoalEntityByIdAndUserId(goalId, userId);

        List<Task> tasks = taskRepository.findByGoalIdAndUserId(goalId, userId);

        return tasks.stream()
                .map(task -> mapToResponse(task, goal.getTitle()))
                .toList();
    }



    public TaskResponse getTaskById(String taskId) {

        String userId = getCurrentUserId();

        Task task = getTaskByIdAndUserId(taskId, userId);

        Goal goal = goalService.getGoalEntityByIdAndUserId(task.getGoalId(), userId);

        return mapToResponse(task, goal.getTitle());
    }



    public TaskResponse updateTask(String taskId, UpdateTaskRequest request) {

        String userId = getCurrentUserId();

        Task task = getTaskByIdAndUserId(taskId, userId);

        TaskStatus oldStatus = task.getStatus();

        Goal goal = goalService.getGoalEntityByIdAndUserId(task.getGoalId(), userId);

        if (request.getTitle() != null) {

            String normalizedTitle = normalizeTitle(request.getTitle());

            if (!normalizedTitle.equalsIgnoreCase(task.getTitle()) 
                    && taskRepository.existsByGoalIdAndTitleIgnoreCase(task.getGoalId(), normalizedTitle)) {

                throw new DuplicateTaskException("Task title already exists in this goal");
            }

            task.setTitle(normalizedTitle);
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        if (request.getScheduledStart() != null) {
            task.setScheduledStart(request.getScheduledStart());
        }

        if (request.getScheduledEnd() != null) {
            task.setScheduledEnd(request.getScheduledEnd());
        }

        if (task.getScheduledStart() != null
                && task.getScheduledEnd() != null
                && task.getScheduledEnd()
                        .isBefore(task.getScheduledStart())) {

            throw new InvalidTaskScheduleException("Scheduled end must be after scheduled start");
        }

        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);

        TaskStatus newStatus = updatedTask.getStatus();

        if(oldStatus != newStatus) {
            goalService.recalculateGoalStatus(task.getGoalId(), userId);
        }

        return mapToResponse(updatedTask, goal.getTitle());
    }



    public void deleteTask(String taskId) {

        String userId = getCurrentUserId();

        Task task = getTaskByIdAndUserId(taskId, userId);

        String goalId = task.getGoalId();

        taskRepository.delete(task);

        goalService.recalculateGoalStatus(goalId, userId);
    }

    public void deleteTasksByGoal(String goalId) {

        String userId = getCurrentUserId();

        taskRepository.deleteByGoalIdAndUserId(goalId, userId);
    }

}

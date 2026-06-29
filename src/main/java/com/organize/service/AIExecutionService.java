package com.organize.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.organize.dto.ai.AIAssistantResponse;
import com.organize.dto.ai.AITaskDTO;
import com.organize.dto.request.CreateGoalRequest;
import com.organize.dto.request.CreateTaskRequest;
import com.organize.dto.request.UpdateGoalRequest;
import com.organize.dto.response.GoalResponse;
import com.organize.dto.response.TaskResponse;
import com.organize.entity.Goal;
import com.organize.entity.Task;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIExecutionService {

    private final GoalService goalService;
    private final TaskService taskService;

    public Object execute(AIAssistantResponse response) {

        if (response.getAction() == null) {
            return response;
        }

        return switch (response.getAction()) {

            case CREATE_GOAL -> createGoal(response);

            case CREATE_TASK -> createTask(response);

            case UPDATE_GOAL -> updateGoal(response);

            case DELETE_TASK -> deleteTask(response);

            case DELETE_GOAL -> deleteGoal(response);

            default -> Map.of(
                    "message",
                    "Action not implemented yet."
            );
        };
    }

    private Object createGoal(AIAssistantResponse response) {

        CreateGoalRequest goalRequest = new CreateGoalRequest();
        goalRequest.setTitle(response.getGoal().getTitle());
        goalRequest.setDescription(response.getGoal().getDescription());

        GoalResponse createdGoal = goalService.createGoal(goalRequest);

        List<TaskResponse> createdTasks = new ArrayList<>();

        if (response.getTasks() != null) {

            for (AITaskDTO aiTask : response.getTasks()) {

                CreateTaskRequest taskRequest = new CreateTaskRequest();

                taskRequest.setTitle(aiTask.getTaskTitle());
                taskRequest.setDescription(aiTask.getDescription());
                taskRequest.setPriority(aiTask.getPriority());
                taskRequest.setEstimatedDurationMinutes(aiTask.getEstimatedDurationMinutes());

                createdTasks.add(
                        taskService.createTask(createdGoal.getId(), taskRequest)
                );
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("goal", createdGoal);
        result.put("tasks", createdTasks);

        return result;
    }

    private Object createTask(AIAssistantResponse response) {

        Goal goal = goalService.getGoalByTitle(response.getGoal().getTitle());

        AITaskDTO aiTask = response.getTasks().get(0);

        CreateTaskRequest request = new CreateTaskRequest();

        request.setTitle(aiTask.getTaskTitle());
        request.setDescription(aiTask.getDescription());
        request.setPriority(aiTask.getPriority());
        request.setEstimatedDurationMinutes(aiTask.getEstimatedDurationMinutes());

        return taskService.createTask(goal.getId(), request);
    }

    private Object updateGoal(AIAssistantResponse response) {

        Goal goal = goalService.getGoalByTitle(response.getGoal().getTitle());

        UpdateGoalRequest request = new UpdateGoalRequest();

        if (response.getGoal().getNewTitle() != null
                && !response.getGoal().getNewTitle().isBlank()) {

            request.setTitle(response.getGoal().getNewTitle());
        }

        if (response.getGoal().getDescription() != null
                && !response.getGoal().getDescription().isBlank()) {

            request.setDescription(response.getGoal().getDescription());
        }

        return goalService.updateGoal(goal.getId(), request);
    }

    private Object deleteTask(AIAssistantResponse response) {

        AITaskDTO aiTask = response.getTasks().get(0);

        Task task = taskService.getTaskByTitle(aiTask.getTaskTitle());

        taskService.deleteTask(task.getId());

        return Map.of(
                "message", "Task deleted successfully"
        );
    }

    private Object deleteGoal(AIAssistantResponse response) {

        Goal goal = goalService.getGoalByTitle(response.getGoal().getTitle());

        goalService.deleteGoal(goal.getId());

        return Map.of(
                "message", "Goal deleted successfully"
        );
    }
}
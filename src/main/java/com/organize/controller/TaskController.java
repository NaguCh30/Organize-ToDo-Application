package com.organize.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.organize.dto.request.CreateTaskRequest;
import com.organize.dto.request.UpdateTaskRequest;
import com.organize.dto.response.TaskResponse;
import com.organize.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {
    
    private final TaskService taskService;

    @PostMapping("/goals/{goalId}/tasks")
    public ResponseEntity<TaskResponse> createTask(
                                @PathVariable String goalId, 
                                @Valid @RequestBody CreateTaskRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(goalId, request));
    }

    @GetMapping("/goals/{goalId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksByGoal(@PathVariable String goalId) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getTasksByGoal(goalId));
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable String taskId) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskById(taskId));
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable String taskId, 
                                                    @Valid @RequestBody UpdateTaskRequest request) {
                                    
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(taskId, request));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String taskId) {
       
        taskService.deleteTask(taskId);

        return ResponseEntity.ok("Task deleted successfully");
    }
}

package com.organize.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.organize.dto.request.ScheduleTaskRequest;
import com.organize.dto.response.ScheduleResult;
import com.organize.dto.response.TaskResponse;
import com.organize.service.ScheduleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;

    @PutMapping("/schedule/tasks/{taskId}")
    public ResponseEntity<ScheduleResult> scheduleTask(
            @PathVariable String taskId,
            @RequestBody @Valid ScheduleTaskRequest request
    ) {
        return ResponseEntity.ok(
                scheduleService.scheduleTask(
                        taskId,
                        request
                )
        );
    }


    @PutMapping("/schedule/tasks/{taskId}/force")
    public ResponseEntity<ScheduleResult> forceScheduleTask(
            @PathVariable String taskId,
            @RequestBody @Valid ScheduleTaskRequest request
    ) {
        return ResponseEntity.ok(
                scheduleService.forceScheduleTask(
                        taskId,
                        request
                )
        );
    }

    @GetMapping("/schedule/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksByDate(
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(
                scheduleService.getTasksByDate(date)
        );
    }

    @GetMapping("/tasks/unscheduled")
    public ResponseEntity<List<TaskResponse>>
    getUnscheduledTasks() {

        return ResponseEntity.ok(
                scheduleService.getUnscheduledTasks()
        );
    }
}

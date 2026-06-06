package com.organize.dto.response;

import java.time.LocalDateTime;

import com.organize.entity.TaskPriority;
import com.organize.entity.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    

    private String id;

    private String goalId;

    private String goalTitle;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private Integer estimatedDurationMinutes;

    private LocalDateTime scheduledStart;

    private LocalDateTime scheduledEnd;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

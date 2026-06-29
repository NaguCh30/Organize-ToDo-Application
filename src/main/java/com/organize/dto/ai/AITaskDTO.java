package com.organize.dto.ai;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.organize.entity.TaskPriority;
import com.organize.entity.TaskStatus;
import lombok.Data;

@Data
public class AITaskDTO {

    private String goalTitle;

    // Existing task
    private String taskTitle;

    // New task title (used for UPDATE_TASK)
    private String newTaskTitle;

    private String description;

    @JsonSetter(nulls = Nulls.SKIP)
    private TaskPriority priority;

    @JsonSetter(nulls = Nulls.SKIP)
    private TaskStatus status;

    private Integer estimatedDurationMinutes;
}
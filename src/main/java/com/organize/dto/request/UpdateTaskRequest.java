package com.organize.dto.request;

import java.time.LocalDateTime;

import com.organize.entity.TaskPriority;
import com.organize.entity.TaskStatus;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {
    
    @Size(max = 100)
    private String title;
    
    @Size(max = 1000)
    private String description;

    private TaskPriority priority;

    private TaskStatus status;

    private LocalDateTime scheduledStart;

    private LocalDateTime scheduledEnd;
}

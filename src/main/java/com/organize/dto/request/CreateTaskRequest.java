package com.organize.dto.request;

import java.time.LocalDateTime;

import com.organize.entity.TaskPriority;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    
    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    private TaskPriority priority;

    //Added estimated time for task for futire usage
    @Min(1)
    private Integer estimatedDurationMinutes;

    //Below two fields are removed from task update and task create requests
    // and moved to the schedule service as scheduling is seperate operation

    //private LocalDateTime scheduledStart;

    //private LocalDateTime scheduledEnd;
}

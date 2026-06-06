package com.organize.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResult {
    
    private boolean success;

    private TaskResponse task;

    private List<ConflictTaskResponse> conflictingTasks;

    private String message;
}

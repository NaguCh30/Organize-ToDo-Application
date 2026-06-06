package com.organize.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConflictTaskResponse {
    
    private String taskId;
    
    private String title;

    private LocalDateTime scheduledStart;

    private LocalDateTime scheduledEnd;

}

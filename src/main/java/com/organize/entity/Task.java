package com.organize.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task extends BaseEntity {
    
    private String userId;
    
    private String goalId;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDateTime scheduledStart;

    private LocalDateTime scheduledEnd;
}

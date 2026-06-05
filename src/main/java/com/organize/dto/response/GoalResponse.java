package com.organize.dto.response;

import java.time.LocalDateTime;

import com.organize.entity.GoalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponse {
    
    private String id;

    private String title;

    private String description;

    private GoalStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

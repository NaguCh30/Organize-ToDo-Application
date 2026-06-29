package com.organize.dto.ai;

import lombok.Data;

@Data
public class AIGoalDTO {

    // Existing goal
    private String title;

    // New goal title (used for UPDATE_GOAL)
    private String newTitle;

    // New description
    private String description;
}
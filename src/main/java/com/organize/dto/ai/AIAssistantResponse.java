package com.organize.dto.ai;

import java.util.List;

import lombok.Data;

@Data
public class AIAssistantResponse {

    private AIAction action;

    private AIGoalDTO goal;

    private List<AITaskDTO> tasks;

    private String aiMessage;

    private Object result;

}
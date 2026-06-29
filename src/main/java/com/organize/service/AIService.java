package com.organize.service;

import com.organize.dto.ai.AIAssistantRequest;
import com.organize.dto.ai.AIAssistantResponse;

public interface AIService {

    AIAssistantResponse process(AIAssistantRequest request);

}
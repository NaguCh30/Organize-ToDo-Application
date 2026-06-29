package com.organize.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.organize.dto.ai.AIAssistantRequest;
import com.organize.dto.ai.AIAssistantResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroqAIService implements AIService {

    private final RestClient restClient;
    private final PromptService promptService;
    private final ObjectMapper objectMapper;
    private final AIExecutionService aiExecutionService;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.model}")
    private String model;

    @Override
    public AIAssistantResponse process(AIAssistantRequest request) {

        String prompt = promptService.buildAssistantPrompt(request.getMessage());

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role",
                                "system",
                                "content",
                                "You are an AI assistant that always returns valid JSON."
                        ),
                        Map.of(
                                "role",
                                "user",
                                "content",
                                prompt
                        )
                ),
                "temperature", 0.2,
                "response_format", Map.of("type", "json_object")
        );

        try {

            Map<?, ?> response = restClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            List<?> choices = (List<?>) response.get("choices");

            if (choices == null || choices.isEmpty()) {
                throw new RuntimeException("No response received from Groq.");
            }

            Map<?, ?> choice = (Map<?, ?>) choices.get(0);

            Map<?, ?> message = (Map<?, ?>) choice.get("message");

            String json = (String) message.get("content");

            AIAssistantResponse aiResponse =
                    objectMapper.readValue(json, AIAssistantResponse.class);

            Object executionResult = aiExecutionService.execute(aiResponse);

            aiResponse.setResult(executionResult);

            return aiResponse;

        } catch (Exception e) {

            throw new RuntimeException("Groq API Error: " + e.getMessage(), e);

        }
    }
}
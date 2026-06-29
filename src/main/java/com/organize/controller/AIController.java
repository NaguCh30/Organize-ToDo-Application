package com.organize.controller;

import com.organize.dto.ai.AIAssistantRequest;
import com.organize.dto.ai.AIAssistantResponse;
import com.organize.service.AIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/assistant")
    public ResponseEntity<AIAssistantResponse> assistant(
            @Valid @RequestBody AIAssistantRequest request
    ) {
        return ResponseEntity.ok(aiService.process(request));
    }
}
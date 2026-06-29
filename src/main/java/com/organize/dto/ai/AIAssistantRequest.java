package com.organize.dto.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AIAssistantRequest {

    @NotBlank
    private String message;

}
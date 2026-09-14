package com.maur025.medassistant.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank(message = "prompt must not be null") String prompt,
                          String model)
{

}

package com.maur025.medassistant.controller;

import com.maur025.medassistant.dto.ChatRequest;
import com.maur025.medassistant.service.AssistantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AssistantService assistantService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody ChatRequest request)
    {
        return ResponseEntity.ok(assistantService.chat(request.prompt(), request.model()));
    }

    @PostMapping(value = "/stream",
                 produces = MediaType.TEXT_EVENT_STREAM_VALUE + "; charset=UTF-8")
    public Flux<String> chatStream(@RequestBody ChatRequest request)
    {
        return assistantService.chatStream(request.prompt(), request.model());
    }

    @PostMapping(value = "/explain")
    public ResponseEntity<String> explainCondition(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(
            assistantService.explainCondition(request.prompt(), request.model()));
    }

    @PostMapping(value = "/symptoms")
    public ResponseEntity<String> analyzeSymptoms(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(
            assistantService.analyzeSymptoms(request.prompt(), request.model()));
    }

    @PostMapping(value = "/diagnose")
    public ResponseEntity<String> diagnoseWithReasoning(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(
            assistantService.diagnoseWithReasoning(request.prompt(), request.model()));
    }

    @PostMapping(value = "/consult")
    public ResponseEntity<String> consultReasoning(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(assistantService.consult(request.prompt(), request.model()));
    }
}

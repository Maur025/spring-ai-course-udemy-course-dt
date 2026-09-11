package com.maur025.medassistant.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping
    public String chat(@RequestBody String prompt) {
        return chatClient.prompt(prompt)
            .call()
            .content();
    }

    @PostMapping(value = "/stream",
                 produces = MediaType.TEXT_EVENT_STREAM_VALUE + "; charset=UTF-8")
    public Flux<String> chatStream(@RequestBody String prompt) {
        return chatClient.prompt(prompt)
            .stream()
            .content();
    }
}

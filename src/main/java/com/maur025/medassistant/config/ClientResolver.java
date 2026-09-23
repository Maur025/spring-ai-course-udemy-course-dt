package com.maur025.medassistant.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ClientResolver {

  private final ChatClient geminiClient;
  private final ChatClient ollamaClient;
  private final ChatClient openAiClient;

  public ClientResolver(@Qualifier("geminiClient") ChatClient geminiClient,
    @Qualifier("ollamaClient") ChatClient ollamaClient,
    @Qualifier("openAiClient") ChatClient openAiClient)
  {
    this.geminiClient = geminiClient;
    this.ollamaClient = ollamaClient;
    this.openAiClient = openAiClient;
  }

  public ChatClient resolve(String model) {
    if (model == null) {
      return geminiClient;
    }

    return switch (model.toLowerCase()) {
      case "ollama" -> ollamaClient;
      case "gemini" -> geminiClient;
      case "openai" -> openAiClient;
      default -> null;
    };
  }
}

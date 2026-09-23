package com.maur025.medassistant.config;

import com.maur025.medassistant.tools.AppointmentSearchTool;
import com.maur025.medassistant.tools.DoctorInfoTool;
import com.maur025.medassistant.tools.PatientInfoTool;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class AssistantConfig {

//    @Bean
//    ChatClient chatClient(ChatClient.Builder builder) {
//        return builder.build();
//    }

  private final AppointmentSearchTool appointmentSearchTool;
  private final DoctorInfoTool doctorInfoTool;
  private final PatientInfoTool patientInfoTool;

  @Value("classpath:prompts/system-prompt.st")
  private Resource systemPromptResource;

  @Bean("geminiClient")
  ChatClient geminiClient(GoogleGenAiChatModel chatModel) throws IOException {
    return ChatClient.builder(chatModel)
      .defaultSystem(getSystemPrompt())
      .defaultTools(appointmentSearchTool, doctorInfoTool, patientInfoTool)
      .build();
  }

  @Bean("ollamaClient")
  ChatClient ollamaClient(OllamaChatModel chatModel) throws IOException {
    return ChatClient.builder(chatModel)
      .defaultSystem(getSystemPrompt())
      .defaultTools(appointmentSearchTool, doctorInfoTool, patientInfoTool)
      .build();
  }

  @Bean("openAiClient")
  public ChatClient openAiClient(OpenAiChatModel chatModel) throws IOException {
    return ChatClient.builder(chatModel)
      .defaultSystem(getSystemPrompt())
      .defaultTools(appointmentSearchTool, doctorInfoTool, patientInfoTool)
      .build();
  }

  private String getSystemPrompt() throws IOException {
    return systemPromptResource.getContentAsString(StandardCharsets.UTF_8)
      .replace(
        "{currentDate}", LocalDate.now()
          .toString()
      );
  }
}

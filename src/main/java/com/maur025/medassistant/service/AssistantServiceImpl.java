package com.maur025.medassistant.service;

import com.maur025.medassistant.config.ClientResolver;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssistantServiceImpl implements AssistantService {

  private final ClientResolver clientResolver;

  @Value("classpath:prompts/explain-condition.st")
  private Resource explainConditionPrompt;

  @Value("classpath:prompts/symptom-analysis.st")
  private Resource symptomAnalysisPrompt;

  @Value("classpath:prompts/diagnosis-cot.st")
  private Resource diagnosisCotPrompt;

  @Value("classpath:prompts/consultation.st")
  private Resource consultationPrompt;


  private PromptTemplate explainConditionTemplate;
  private PromptTemplate symptomAnalysisTemplate;
  private PromptTemplate diagnosisCotTemplate;
  private PromptTemplate consultationTemplate;

  @PostConstruct
  void init() {
    explainConditionTemplate = new PromptTemplate(explainConditionPrompt);
    symptomAnalysisTemplate = new PromptTemplate(symptomAnalysisPrompt);
    diagnosisCotTemplate = new PromptTemplate(diagnosisCotPrompt);
    consultationTemplate = PromptTemplate.builder()
      .resource(consultationPrompt)
      .build();
  }

  @Override
  public String chat(String prompt, String model, Long userId) {
    log.info("chat request - model: {}", model);

    return clientResolver.resolve(model)
      .prompt(prompt)
      .toolContext(Map.of("userId", userId))
      .call()
      .content();
  }

  @Override
  public Flux<String> chatStream(String prompt, String model) {
    log.info("chat stream request - model: {}", model);

    return clientResolver.resolve(model)
      .prompt(prompt)
      .stream()
      .content();
  }

  @Override
  public String explainCondition(String condition, String model) {
    log.info("Explain request - condition: {}, model: {}", condition, model);

    String message = explainConditionTemplate.render(Map.of("condition", condition));

    return clientResolver.resolve(model)
      .prompt(message)
      .call()
      .content();
  }

  @Override
  public String analyzeSymptoms(String symptoms, String model) {
    log.info("Analysis de symptoms - symptoms: {}, model: {}", symptoms, model);

    String message = symptomAnalysisTemplate.render(Map.of("sintomas", symptoms));

    return clientResolver.resolve(model)
      .prompt(message)
      .call()
      .content();
  }

  @Override
  public String diagnoseWithReasoning(String symptoms, String model) {
    log.info("diagnose with reasoning - symptoms: {}, model: {}", symptoms, model);

    String message = diagnosisCotTemplate.render(Map.of("sintomas", symptoms));

    return clientResolver.resolve(model)
      .prompt(message)
      .call()
      .content();
  }

  @Override
  public String consult(String query, String model) {
    log.info("Consult request - query: {}, model: {}", query, model);

    String message = consultationTemplate.render(Map.of("consulta", query));

    return clientResolver.resolve(model)
      .prompt(message)
      .call()
      .content();
  }
}

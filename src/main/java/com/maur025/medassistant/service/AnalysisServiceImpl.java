package com.maur025.medassistant.service;

import com.maur025.medassistant.config.ClientResolver;
import com.maur025.medassistant.dto.analysis.ConditionSummary;
import com.maur025.medassistant.dto.analysis.QueryClassification;
import com.maur025.medassistant.dto.analysis.SymptomAnalysis;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

  private final ClientResolver clientResolver;

  @Value("classpath:prompts/structured-analysis.st")
  private Resource structuredAnalysisPrompt;

  private PromptTemplate structuredAnalysisTemplate;

  @PostConstruct
  void init() {
    structuredAnalysisTemplate = PromptTemplate.builder()
      .resource(structuredAnalysisPrompt)
      .build();
  }

  @Override
  public ConditionSummary summarizeCondition(String condition, String model) {
    log.info("Summarizing condition: {} using model: {}", condition, model);
/*
    var converter = new BeanOutputConverter<>(ConditionSummary.class);

    String format = converter.getFormat();
    log.info("Instrucciones de formato generadas:\n{}", format);

    String prompt = """
      Proporciona un resumen médico educativo sobre: %s
      
      %s
      """.formatted(condition, format);

    String jsonResponse = resolveClient(model).prompt(prompt)
      .call()
      .content();

    log.info("Respuesta JSON recibida:\n{}", jsonResponse);

    assert jsonResponse != null;

    return converter.convert(jsonResponse);*/

    return clientResolver.resolve(model)
      .prompt()
      .user("Proporciona un resumen médico educativo sobre: " + condition)
      .call()
      .entity(ConditionSummary.class);
  }

  @Override
  public List<ConditionSummary> listRelatedConditions(String symptoms, String model) {
    log.info("Listado de condiciones relacionadas - modelo: {}", model);

    return clientResolver.resolve(model)
      .prompt()
      .user("Listá las 3 condiciones médicas más probables " + "para estos síntomas: " + symptoms)
      .call()
      .entity(new ParameterizedTypeReference<>() {
      });
  }

  @Override
  public SymptomAnalysis analyzeSymptoms(String symptoms, String model) {
    log.info("Análisis de síntomas - modelo: {}", model);

    String message = structuredAnalysisTemplate.render(Map.of("sintomas", symptoms));

    return clientResolver.resolve(model)
      .prompt()
      .user(message)
      .call()
      .entity(SymptomAnalysis.class);
  }

  @Override
  public QueryClassification classifyQuery(String query, String model) {
    log.info("Clasificación de consulta - modelo: {}", model);

    return clientResolver.resolve(model)
      .prompt()
      .user("Clasificá la siguiente consulta de un paciente. "
        + "Determiná que tipo de consulta es y explicá brevemente por qué.\n\n"
        + "Consulta del paciente: \"" + query + "\"")
      .call()
      .entity(QueryClassification.class);
  }
}

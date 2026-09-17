package com.maur025.medassistant.service;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AssistantServiceImpl implements AssistantService {

    private final ChatClient geminiClient;
    private final ChatClient ollamaClient;

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

    public AssistantServiceImpl(@Qualifier("geminiClient") ChatClient geminiClient,
        @Qualifier("ollamaClient") ChatClient ollamaClient)
    {
        this.geminiClient = geminiClient;
        this.ollamaClient = ollamaClient;
    }

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
    public String chat(String prompt, String model) {
        log.info("chat request - model: {}", model);

        return resolveClient(model).prompt(prompt)
            .call()
            .content();
    }

    @Override
    public Flux<String> chatStream(String prompt, String model) {
        log.info("chat stream request - model: {}", model);

        return resolveClient(model).prompt(prompt)
            .stream()
            .content();
    }

    @Override
    public String explainCondition(String condition, String model) {
        log.info("Explain request - condition: {}, model: {}", condition, model);

        String message = explainConditionTemplate.render(Map.of("condition", condition));

        return resolveClient(model).prompt(message)
            .call()
            .content();
    }

    @Override
    public String analyzeSymptoms(String symptoms, String model) {
        log.info("Analysis de symptoms - symptoms: {}, model: {}", symptoms, model);

        String message = symptomAnalysisTemplate.render(Map.of("sintomas", symptoms));

        return resolveClient(model).prompt(message)
            .call()
            .content();
    }

    @Override
    public String diagnoseWithReasoning(String symptoms, String model) {
        log.info("diagnose with reasoning - symptoms: {}, model: {}", symptoms, model);

        String message = diagnosisCotTemplate.render(Map.of("sintomas", symptoms));

        return resolveClient(model).prompt(message)
            .call()
            .content();
    }

    @Override
    public String consult(String query, String model) {
        log.info("Consult request - query: {}, model: {}", query, model);

        String message = consultationTemplate.render(Map.of("consulta", query));

        return resolveClient(model).prompt(message)
            .call()
            .content();
    }

    private ChatClient resolveClient(String model) {
        return "ollama".equalsIgnoreCase(model) ? ollamaClient : geminiClient;
    }
}

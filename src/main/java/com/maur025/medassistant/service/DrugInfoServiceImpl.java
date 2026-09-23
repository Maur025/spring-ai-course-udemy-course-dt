package com.maur025.medassistant.service;

import com.maur025.medassistant.dto.DrugInfo;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrugInfoServiceImpl implements DrugInfoService {

  private static final int MAX_FIELD_LENGTH = 500;
  private final RestClient openFdaClient;

  @Override
  public DrugInfo getDrugInfo(String drugName) {
    log.info("Fetching drug information for: {}", drugName);

    var response = fetchFromOpenFda(drugName);

    return mapToDrugInfo(response, drugName);
  }

  private String extractFirst(Map<?, ?> map, String key) {
    if (!(map.get(key) instanceof List<?> list) || list.isEmpty()) {
      return "No disponible";
    }

    var value = list.getFirst()
      .toString();

    return value.length() > MAX_FIELD_LENGTH ? value.substring(0, MAX_FIELD_LENGTH) + "..." : value;
  }

  private Map<String, Object> fetchFromOpenFda(String drugName) {
    return openFdaClient.get()
      .uri("/drug/label.json?search=openfda.generic_name:{name}&limit=1", drugName)
      .retrieve()
      .body(new ParameterizedTypeReference<>() {
      });
  }

  private DrugInfo mapToDrugInfo(Map<String, Object> response, String drugName) {
    if (!(response.get("results") instanceof List<?> results) || response.isEmpty()) {
      log.info("No se encontraron resultados para el medicamento: {}", drugName);
      return null;
    }

    if (!(results.getFirst() instanceof Map<?, ?> result)) {
      log.info("No se encontraron resultados para el medicamento: {}", drugName);
      return null;
    }

    var openfda = result.get("openfda") instanceof Map<?, ?> m ? m : Map.of();

    return new DrugInfo(
      extractFirst(openfda, "brand_name"), extractFirst(openfda, "generic_name"),
      extractFirst(result, "purpose"), extractFirst(result, "warnings"),
      extractFirst(result, "dosage_and_administration")
    );
  }
}

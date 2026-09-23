package com.maur025.medassistant.tools;

import com.maur025.medassistant.dto.PatientInfo;
import com.maur025.medassistant.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientInfoTool {

  private final PatientService patientService;

  @Tool(
    description = "Consultar el historial clínico de un paciente por su ID. Usar cuando el usuario pregunte por su historial médico, alergías o condiciones.")
  public PatientInfo getPatientInfo(
    @ToolParam(description = "ID numérico del paciente") Long patientId, ToolContext context)
  {
    log.info("Fetching patient info for patientId: {}", patientId);

    Long userId = (Long) context.getContext()
      .get("userId");

    log.info("User ID from context: {}", userId);

    if (!patientId.equals(userId)) {
      log.warn("Access denied: User ID {} is trying to access patient ID {}", userId, patientId);
      return null;
    }

    return patientService.getPatientInfo(patientId);
  }
}

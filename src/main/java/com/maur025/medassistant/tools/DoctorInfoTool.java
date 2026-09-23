package com.maur025.medassistant.tools;

import com.maur025.medassistant.dto.DoctorInfo;
import com.maur025.medassistant.service.DoctorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DoctorInfoTool {

  private final DoctorService doctorService;

  @Tool(
    description = "Busca información de un médico por nombre o especialidad. Usar cuando el usuario pregunte por datos de un doctor, quién atiende una especialidad, o información de contacto de un médico.")
  public List<DoctorInfo> searchDoctors(@ToolParam(
    description = "Solo el apellido del médico, o el nombre del médico, o el nombre de la especialidad, sin titulos como Dr. o Dra. ni espacios en blanco. Solo acepta una palabra.") String query)
  {
    log.info("Searching for doctors with query: {}", query);

    return doctorService.searchDoctors(query);
  }

}

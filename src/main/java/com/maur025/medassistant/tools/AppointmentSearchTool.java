package com.maur025.medassistant.tools;

import com.maur025.medassistant.dto.AppointmentInfo;
import com.maur025.medassistant.service.AppointmentService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentSearchTool {

  private final AppointmentService appointmentService;

  @Tool(
    description = "Buscar turnos médicos disponibles para una especialidad y fecha. Usar cuando el usuario pregunte por disponibilidad de turnos o citas médicas.")
  public List<AppointmentInfo> searchAppointments(@ToolParam(
      description = "Especialidad médica, por ejemplo: cardiología, pediatría, dermatología.") String specialty,
    @ToolParam(description = "Fecha de la cita formato yyyy-MM-dd") String date)
  {
    log.info("Searching for available appointments for specialty: {} on date: {}", specialty, date);

    return appointmentService.findAvailableAppointments(specialty, LocalDate.parse(date));
  }
}

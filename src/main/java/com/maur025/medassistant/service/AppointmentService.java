package com.maur025.medassistant.service;

import com.maur025.medassistant.dto.AppointmentInfo;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

  List<AppointmentInfo> findAvailableAppointments(String specialty, LocalDate date);
}

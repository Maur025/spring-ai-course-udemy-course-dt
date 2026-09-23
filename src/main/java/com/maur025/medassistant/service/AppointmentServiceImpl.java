package com.maur025.medassistant.service;

import com.maur025.medassistant.dto.AppointmentInfo;
import com.maur025.medassistant.model.Appointment;
import com.maur025.medassistant.model.Doctor;
import com.maur025.medassistant.repository.AppointmentRepository;
import com.maur025.medassistant.repository.DoctorRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

  private final DoctorRepository doctorRepository;
  private final AppointmentRepository appointmentRepository;

  public List<AppointmentInfo> findAvailableAppointments(String specialty, LocalDate date) {
    log.info("Searching for available appointments for specialty: {} on date: {}", specialty, date);

    var doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);

    if (doctors.isEmpty()) {
      log.info("No doctors found for specialty: {}", specialty);
      return List.of();
    }

    var doctorNames = buildDoctorNameMap(doctors);
    var doctorIds = new ArrayList<>(doctorNames.keySet());
    var appointments = appointmentRepository.findByDoctorIdInAndDateAndAvailableTrue(
      doctorIds, date);

    return toAppointmentInfoList(appointments, doctorNames, specialty);
  }

  private Map<Long, String> buildDoctorNameMap(List<Doctor> doctors) {
    return doctors.stream()
      .collect(Collectors.toMap(
        Doctor::getId,
        doctor -> doctor.getFirstName() + " " + doctor.getLastName()
      ));
  }


  private List<AppointmentInfo> toAppointmentInfoList(List<Appointment> appointments,
    Map<Long, String> doctorNames, String specialty)
  {
    return appointments.stream()
      .map(appointment -> new AppointmentInfo(
        doctorNames.get(appointment.getDoctorId()), specialty, appointment.getDate()
        .toString(), appointment.getStartTime()
        .toString()
      ))
      .toList();
  }
}

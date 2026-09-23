package com.maur025.medassistant.repository;

import com.maur025.medassistant.model.Appointment;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  List<Appointment> findByDoctorIdInAndDateAndAvailableTrue(List<Long> doctorIds, LocalDate date);


}

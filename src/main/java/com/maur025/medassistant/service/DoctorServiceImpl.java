package com.maur025.medassistant.service;

import com.maur025.medassistant.dto.DoctorInfo;
import com.maur025.medassistant.model.Doctor;
import com.maur025.medassistant.repository.DoctorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {

  private final DoctorRepository doctorRepository;

  public List<DoctorInfo> searchDoctors(String query) {
    return doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSpecialtyContainingIgnoreCase(
        query, query, query)
      .stream()
      .map(this::toDoctorInfo)
      .toList();
  }

  private DoctorInfo toDoctorInfo(Doctor doctor) {
    return new DoctorInfo(
      doctor.getFirstName(), doctor.getLastName(), doctor.getSpecialty(), doctor.getLicenseNumber(),
      doctor.getPhone(), doctor.getOffice()
    );
  }
}

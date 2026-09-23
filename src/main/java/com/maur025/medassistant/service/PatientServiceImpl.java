package com.maur025.medassistant.service;

import com.maur025.medassistant.dto.PatientInfo;
import com.maur025.medassistant.model.Patient;
import com.maur025.medassistant.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

  private final PatientRepository patientRepository;

  @Override
  public PatientInfo getPatientInfo(Long patientId) {
    log.info("Fetching patient info for patientId: {}", patientId);

    return patientRepository.findById(patientId)
      .map(this::toPatientInfo)
      .orElse(null);
  }

  private PatientInfo toPatientInfo(Patient patient) {
    return new PatientInfo(
      patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth(),
      patient.getAllergies(), patient.getConditions()
    );
  }
}

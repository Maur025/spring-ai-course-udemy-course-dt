package com.maur025.medassistant.service;

import com.maur025.medassistant.dto.PatientInfo;

public interface PatientService {

  PatientInfo getPatientInfo(Long patientId);
}

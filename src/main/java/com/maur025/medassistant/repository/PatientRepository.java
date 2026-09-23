package com.maur025.medassistant.repository;

import com.maur025.medassistant.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}

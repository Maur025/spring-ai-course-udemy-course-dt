package com.maur025.medassistant.repository;

import com.maur025.medassistant.model.Doctor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

  List<Doctor> findBySpecialtyIgnoreCase(String specialty);

  List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSpecialtyContainingIgnoreCase(
    String firstName, String lastName, String specialty);
}

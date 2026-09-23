package com.maur025.medassistant.dto;

import java.time.LocalDate;

public record PatientInfo(String firstName, String lastName, LocalDate dateOfBirth,
                          String allergies, String conditions)
{

}

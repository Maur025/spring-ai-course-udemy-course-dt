package com.maur025.medassistant.service;

import com.maur025.medassistant.dto.DoctorInfo;
import java.util.List;

public interface DoctorService {

  List<DoctorInfo> searchDoctors(String query);
}

package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SCTechnicianService {

    SCTechnicianResponseDTO createTechnician(SCTechnicianCreateDTO createDTO);

    SCTechnicianResponseDTO getTechnicianById(String id);

    SCTechnicianResponseDTO updateTechnician(String id, SCTechnicianUpdateDTO updateDTO);

    void deleteTechnician(String id);

    Page<SCTechnicianListDTO> getAllTechnicians(Pageable pageable);

    Page<SCTechnicianListDTO> searchTechnicians(String keyword, Pageable pageable);

    Page<SCTechnicianListDTO> getTechniciansByBranch(String branchOffice, Pageable pageable);

    Page<SCTechnicianListDTO> getTechniciansBySpecialty(String specialty, Pageable pageable);

    SCTechnicianResponseDTO updatePassword(String id, String newPassword);

    public SCTechnicianResponseDTO getTechnicianByUserId(Long userId);
}

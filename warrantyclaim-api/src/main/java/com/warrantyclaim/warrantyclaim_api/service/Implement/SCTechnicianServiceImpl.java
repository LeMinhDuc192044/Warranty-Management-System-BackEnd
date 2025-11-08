package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.SCTechnician;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;

import com.warrantyclaim.warrantyclaim_api.repository.SCTechnicianRepository;
import com.warrantyclaim.warrantyclaim_api.service.SCTechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class SCTechnicianServiceImpl implements SCTechnicianService {

    @Autowired
    private SCTechnicianRepository technicianRepository;

    @Override
    public SCTechnicianResponseDTO createTechnician(SCTechnicianCreateDTO createDTO) {
        // Validate email uniqueness


        // Create new technician
        SCTechnician technician = new SCTechnician();
        technician.setId(generateTechnicianId());
        technician.setUserId(createDTO.getUserId());
        technician.setName(createDTO.getName());
        technician.setEmail(createDTO.getEmail());
        technician.setPhoneNumber(createDTO.getPhoneNumber());
        technician.setDateOfBirth(createDTO.getDateOfBirth());
        technician.setPassword(createDTO.getPassword()); // Should be encrypted in real app
        technician.setBranchOffice(createDTO.getBranchOffice());
        technician.setSpecialty(createDTO.getSpecialty());

        SCTechnician saved = technicianRepository.save(technician);
        return mapToResponseDTO(saved);
    }

    @Override
    public SCTechnicianResponseDTO getTechnicianById(String id) {
        SCTechnician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + id));
        return mapToResponseDTO(technician);
    }

    @Override
    public SCTechnicianResponseDTO getTechnicianByUserId(Long userId) {
        SCTechnician technician = technicianRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with userId: " + userId));
        return mapToResponseDTO(technician);
    }

    @Override
    public SCTechnicianResponseDTO updateTechnician(String id, SCTechnicianUpdateDTO updateDTO) {
        SCTechnician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + id));

        // Check email uniqueness if changed


        // Check userId uniqueness if changed


        // Update other fields
        if (updateDTO.getName() != null) {
            technician.setName(updateDTO.getName());
        }
        if (updateDTO.getPhoneNumber() != null) {
            technician.setPhoneNumber(updateDTO.getPhoneNumber());
        }
        if (updateDTO.getDateOfBirth() != null) {
            technician.setDateOfBirth(updateDTO.getDateOfBirth());
        }
        if (updateDTO.getBranchOffice() != null) {
            technician.setBranchOffice(updateDTO.getBranchOffice());
        }
        if (updateDTO.getSpecialty() != null) {
            technician.setSpecialty(updateDTO.getSpecialty());
        }

        SCTechnician updated = technicianRepository.save(technician);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteTechnician(String id) {
        if (!technicianRepository.existsById(id)) {
            throw new ResourceNotFoundException("Technician not found with id: " + id);
        }
        technicianRepository.deleteById(id);
    }

    @Override
    public Page<SCTechnicianListDTO> getAllTechnicians(Pageable pageable) {
        Page<SCTechnician> technicians = technicianRepository.findAll(pageable);
        return technicians.map(this::mapToListDTO);
    }

    @Override
    public Page<SCTechnicianListDTO> searchTechnicians(String keyword, Pageable pageable) {
        Page<SCTechnician> technicians = technicianRepository.searchTechnicians(keyword, pageable);
        return technicians.map(this::mapToListDTO);
    }

    @Override
    public Page<SCTechnicianListDTO> getTechniciansByBranch(String branchOffice, Pageable pageable) {
        Page<SCTechnician> technicians = technicianRepository.findByBranchOffice(branchOffice, pageable);
        return technicians.map(this::mapToListDTO);
    }

    @Override
    public Page<SCTechnicianListDTO> getTechniciansBySpecialty(String specialty, Pageable pageable) {
        Page<SCTechnician> technicians = technicianRepository.findBySpecialty(specialty, pageable);
        return technicians.map(this::mapToListDTO);
    }

    @Override
    public SCTechnicianResponseDTO updatePassword(String id, String newPassword) {
        SCTechnician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + id));

        technician.setPassword(newPassword); // Should be encrypted in real app
        SCTechnician updated = technicianRepository.save(technician);
        return mapToResponseDTO(updated);
    }

    // Helper methods
    private String generateTechnicianId() {
        return "SCT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private SCTechnicianResponseDTO mapToResponseDTO(SCTechnician technician) {
        SCTechnicianResponseDTO dto = new SCTechnicianResponseDTO();
        dto.setId(technician.getId());
        dto.setUserId(technician.getUserId());
        dto.setName(technician.getName());
        dto.setEmail(technician.getEmail());
        dto.setPhoneNumber(technician.getPhoneNumber());
        dto.setDateOfBirth(technician.getDateOfBirth());
        dto.setBranchOffice(technician.getBranchOffice());
        dto.setSpecialty(technician.getSpecialty());
        return dto;
    }

    private SCTechnicianListDTO mapToListDTO(SCTechnician technician) {
        SCTechnicianListDTO dto = new SCTechnicianListDTO();
        dto.setId(technician.getId());
        dto.setName(technician.getName());
        dto.setEmail(technician.getEmail());
        dto.setPhoneNumber(technician.getPhoneNumber());
        dto.setBranchOffice(technician.getBranchOffice());
        dto.setSpecialty(technician.getSpecialty());
        return dto;
    }
}
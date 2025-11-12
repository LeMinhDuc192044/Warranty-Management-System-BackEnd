package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.TechnicianWorkStatsDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WorkCompletionRequestDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WorkResultResponseDTO;
import com.warrantyclaim.warrantyclaim_api.entity.SCTechnician;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyClaim;
import com.warrantyclaim.warrantyclaim_api.entity.WorkResult;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.repository.SCTechnicianRepository;
import com.warrantyclaim.warrantyclaim_api.repository.WarrantyClaimRepository;
import com.warrantyclaim.warrantyclaim_api.repository.WorkResultRepository;
import com.warrantyclaim.warrantyclaim_api.service.WorkResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkResultServiceImpl implements WorkResultService {

    private final WorkResultRepository workResultRepository;
    private final WarrantyClaimRepository warrantyClaimRepository;
    private final SCTechnicianRepository scTechnicianRepository;

    @Override
    @Transactional
    public WorkResultResponseDTO completeWork(WorkCompletionRequestDTO request) {
        // Validate claim exists
        WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Claim not found with ID: " + request.getClaimId()));

        // Check if work result already exists
        if (workResultRepository.existsByClaimId(request.getClaimId())) {
            throw new IllegalStateException("Work result already exists for claim: " + request.getClaimId());
        }

        // Create WorkResult
        WorkResult workResult = new WorkResult();
        workResult.setClaimId(request.getClaimId());
        workResult.setPartsUsed(request.getPartsUsed() != null ? String.join(",", request.getPartsUsed()) : "");
        workResult.setSerialNumbers(
                request.getSerialNumbers() != null ? String.join(",", request.getSerialNumbers()) : "");
        workResult.setCompletionNotes(request.getCompletionNotes());
        workResult.setReturnDate(request.getReturnDate());
        workResult.setCompletedAt(LocalDateTime.now());
        workResult.setCompletedByTechnicianId(request.getCompletedByTechnicianId());
        workResult.setWorkDurationHours(request.getWorkDurationHours());

        // Save WorkResult
        WorkResult savedResult = workResultRepository.save(workResult);

        // Update claim status to COMPLETED
        claim.setStatus(WarrantyClaimStatus.COMPLETED);
        warrantyClaimRepository.save(claim);

        // Convert to DTO and return
        return convertToDTO(savedResult);
    }

    @Override
    public WorkResultResponseDTO getWorkResultByClaimId(String claimId) {
        WorkResult workResult = workResultRepository.findByClaimId(claimId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Work result not found for claim: " + claimId));
        return convertToDTO(workResult);
    }

    @Override
    public List<WorkResultResponseDTO> getWorkResultsByTechnician(String technicianId) {
        List<WorkResult> workResults = workResultRepository.findByCompletedByTechnicianId(technicianId);
        return workResults.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkResultResponseDTO updateCustomerRating(String claimId, Integer rating) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        WorkResult workResult = workResultRepository.findByClaimId(claimId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Work result not found for claim: " + claimId));

        workResult.setCustomerSatisfactionRating(rating);
        WorkResult updated = workResultRepository.save(workResult);

        return convertToDTO(updated);
    }

    @Override
    public TechnicianWorkStatsDTO getTechnicianWorkStats(String technicianId) {
        // Get technician info
        SCTechnician technician = scTechnicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Technician not found with ID: " + technicianId));

        // Get all work results
        List<WorkResult> workResults = workResultRepository.findByCompletedByTechnicianId(technicianId);

        // Calculate stats
        int totalWorks = workResults.size();
        Integer totalHours = workResultRepository.getTotalWorkHoursByTechnician(technicianId);
        Double avgRating = workResultRepository.getAverageRatingByTechnician(technicianId);

        // Count high-rated works (>= 4 stars)
        int highRatedWorks = (int) workResults.stream()
                .filter(w -> w.getCustomerSatisfactionRating() != null &&
                        w.getCustomerSatisfactionRating() >= 4)
                .count();

        return TechnicianWorkStatsDTO.builder()
                .technicianId(technicianId)
                .technicianName(technician.getName())
                .totalWorksCompleted(totalWorks)
                .totalWorkHours(totalHours != null ? totalHours : 0)
                .averageRating(avgRating != null ? avgRating : 0.0)
                .highRatedWorks(highRatedWorks)
                .build();
    }

    /**
     * Convert WorkResult entity to DTO
     */
    private WorkResultResponseDTO convertToDTO(WorkResult workResult) {
        WorkResultResponseDTO dto = new WorkResultResponseDTO();
        dto.setClaimId(workResult.getClaimId());

        // Parse comma-separated strings to lists
        if (workResult.getPartsUsed() != null && !workResult.getPartsUsed().isEmpty()) {
            dto.setPartsUsed(Arrays.asList(workResult.getPartsUsed().split(",")));
        }

        if (workResult.getSerialNumbers() != null && !workResult.getSerialNumbers().isEmpty()) {
            dto.setSerialNumbers(Arrays.asList(workResult.getSerialNumbers().split(",")));
        }

        dto.setCompletionNotes(workResult.getCompletionNotes());
        dto.setReturnDate(workResult.getReturnDate());
        dto.setCompletedAt(workResult.getCompletedAt());
        dto.setCompletedByTechnicianId(workResult.getCompletedByTechnicianId());
        dto.setWorkDurationHours(workResult.getWorkDurationHours());
        dto.setCustomerSatisfactionRating(workResult.getCustomerSatisfactionRating());

        // Get technician name if available
        if (workResult.getCompletedByTechnicianId() != null) {
            scTechnicianRepository.findById(workResult.getCompletedByTechnicianId())
                    .ifPresent(tech -> dto.setTechnicianName(tech.getName()));
        }

        // Get claim info if available
        if (workResult.getWarrantyClaim() != null) {
            WarrantyClaim claim = workResult.getWarrantyClaim();
            dto.setCustomerName(claim.getCustomerName());

            if (claim.getVehicle() != null) {
                dto.setVehicleVIN(claim.getVehicle().getId());
                dto.setVehicleName(claim.getVehicle().getName());
            }
        }

        return dto;
    }
}

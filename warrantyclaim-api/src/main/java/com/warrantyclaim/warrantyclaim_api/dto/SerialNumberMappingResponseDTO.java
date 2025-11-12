package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SerialNumberMappingResponseDTO {
    private String serialNumber;
    private String partId;
    private String partName;
    private String vehicleVIN;
    private String claimId;
    private LocalDateTime mappingDate;
    private String mappedByTechnicianId;
    private String technicianName;
    private String notes;
    private Integer durabilityPercentage;
}

package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SerialNumberMappingRequestDTO {
    private String serialNumber;
    private String partId;
    private String vehicleVIN;
    private String claimId;
    private String mappedByTechnicianId;
    private String notes;
    private Integer durabilityPercentage; // 0-100%
}

package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.ServiceCampaignsStatus;
import com.warrantyclaim.warrantyclaim_api.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCampaignsResponseDTO {

    private String id;

    private String typeName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String requiredParts;
    private Specialty specialty;
    private String description;

    private ServiceCampaignsStatus status;

    private Boolean notificationSent;

    private List<VehicleTypeInfoDTO> vehicleTypes;
    private List<TechnicianBasicDTO> technicians;

}
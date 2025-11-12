package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.ServiceCampaignsStatus;
import com.warrantyclaim.warrantyclaim_api.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCampaignsListDTO {

    private String campaignsId;
    private String campaignsTypeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String requiredParts;
    private String description;
    private ServiceCampaignsStatus status;
    private Boolean notificationSent;
    private Specialty specialty;

    // Count of relationships
    private Integer vehicleTypeCount;
    private Integer technicianCount;

    // Số xe đã sửa - tính từ reports hoặc technician progress
    private Integer completedVehicles;
}

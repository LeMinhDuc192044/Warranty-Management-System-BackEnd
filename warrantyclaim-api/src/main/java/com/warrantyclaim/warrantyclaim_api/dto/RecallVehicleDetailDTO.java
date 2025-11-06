package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecallVehicleDetailDTO {
    // Recall info
    private String recallId;
    private String recallTitle;
    private String recallDescription;
    private String recallStatus;

    // Vehicle info
    private String vehicleId;
    private String vehicleVin;
    private String vehicleModel;
    private String vehicleBrand;
    private Integer vehicleYear;
    private String vehicleStatus;

    // Relationship info
    private String recallVehicleStatus;  // Status của RecallElectricVehicle

    // Additional info (optional)
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;
}

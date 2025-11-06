package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecallVehicleStatusResponseDTO {
    private String recallId;
    private String vehicleId;
    private String status;
    private String vehicleVin;
    private String vehicleModel;
    private String message;
}
package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricVehicleResponseDTO {

    private String id;  // VIN
    private String name;
    private Float totalKm;
    private String picture;
    private LocalDate purchaseDate;
    private String owner;
    private String phoneNumber;
    private String email;
    private VehicleStatus status;

    // Nested vehicle type info
    private VehicleTypeInfoDTO vehicleType;

}

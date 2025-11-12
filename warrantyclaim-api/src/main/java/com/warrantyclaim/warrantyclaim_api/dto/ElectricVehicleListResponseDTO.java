package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricVehicleListResponseDTO {
    private String id; // VIN
    private String name;
    private Float totalKm;
    private String owner;
    private String phoneNumber;
    private String email;
    private String picture;
    private LocalDate purchaseDate;
    private VehicleStatus status;
    private com.warrantyclaim.warrantyclaim_api.enums.UsageType usageType;
    private LocalDate returnDate;

    // Simplified vehicle type info
    private String vehicleTypeId;
    private String modelName;
    private List<SparePartInfoScDTO> sparePartInfoScDTOList;
}

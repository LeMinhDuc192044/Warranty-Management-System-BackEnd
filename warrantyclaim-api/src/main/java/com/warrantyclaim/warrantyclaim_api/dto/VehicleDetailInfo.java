package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicleType;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetailInfo {
    private String vehicleId;
    private String vehicleName;
    private Float totalKm;
    private String owner;
    private String phoneNumber;
    private String email;
    private VehicleStatus status;
    private com.warrantyclaim.warrantyclaim_api.enums.UsageType usageType;
    private java.time.LocalDate purchaseDate;
    private String vehicleTypeName;
    private String modelName;
    private ElectricVehicleType electricVehicleType;
}

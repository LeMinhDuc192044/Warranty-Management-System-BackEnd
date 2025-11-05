package com.warrantyclaim.warrantyclaim_api.dto;

import jakarta.validation.constraints.NotBlank;

import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricVehicleUpdateRequestDTO {

    private String name;

    private Float totalKm;

    private LocalDate productionDate;

    private String owner;

    @Pattern(regexp = "^[0-9]{10,20}$", message = "Phone number must be 10-20 digits")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    private VehicleStatus status;

    private String vehicleTypeId;
}


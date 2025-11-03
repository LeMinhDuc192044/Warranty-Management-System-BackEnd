package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicleType;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleCreateDTO {

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN format")
    @NotBlank(message = "Vehicle must have a VIN")
    private String vehicleId;

    @NotBlank(message = "Vehicle must have an name")
    private String vehicleName;

    @NotNull(message = "Vehicle must have traveled history to apply Warranty Policy!!")
    @PositiveOrZero(message = "Total Km must be 0 or more")
    private float totalKm;

    @NotBlank(message = "Vehicle must have an owner")
    private String owner;

    @Email(message = "Invalid email Format!!!")
    @NotBlank(message = "Email can not be blank!!!")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @NotBlank(message = "Vehicle owner must have a phone number for contact information")
    private String phoneNumber;

    @NotNull(message = "Must have purchase date to apply Warranty Policy!")
    private LocalDate purchaseDate;

    private VehicleStatus status;

    @NotBlank(message = "Vehicle must have a type")
    private String electricVehicleTypeId;

}

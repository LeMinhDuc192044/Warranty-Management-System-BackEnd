package com.warrantyclaim.warrantyclaim_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartsRequestCreateDTO {
//    @NotBlank(message = "Part number is required")
//    private String partNumber;

    @NotBlank(message = "Part name is required")
    private String partName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Request date is required")
    private LocalDate requestDate;

    private LocalDate deliveryDate;

    @NotBlank(message = "Part type ID is required")
    private String partTypeId;

    @NotBlank(message = "VIN is required")
    private String vin;

    @NotBlank(message = "Staff ID is required")
    private String requestedByStaffId;

    @NotBlank(message = "Branch office is required")
    private String branchOffice;
}

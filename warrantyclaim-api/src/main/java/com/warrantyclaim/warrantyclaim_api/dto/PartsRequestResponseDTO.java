package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.PartsRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartsRequestResponseDTO {
    private String id;
//    private String partNumber;
    private String partName; // part number ko can thiet
    private Integer quantity;
    private LocalDate requestDate;
    private PartsRequestStatus status;
    private LocalDate deliveryDate;

    // Nested objects
    private PartTypeInfoDTO partType;
    private VehicleBasicInfoDTO vehicle;

//    private StaffInfoDTO staff;
}

package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.PartsRequestStatus;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartsRequestUpdateDTO {
//    private String partNumber;

    private String partName;

    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    private LocalDate requestDate;

    private PartsRequestStatus status;

    private LocalDate deliveryDate;

    private String partTypeId;

    private String vin;

    private String requestedByStaffId;

    private String branchOffice;
}

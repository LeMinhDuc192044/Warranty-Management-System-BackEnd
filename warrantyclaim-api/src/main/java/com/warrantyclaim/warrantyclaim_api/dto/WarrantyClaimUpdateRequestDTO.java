package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyClaimUpdateRequestDTO {
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String customerName;

    @Pattern(regexp = "^[0-9]{10,20}$", message = "Phone number must be 10-20 digits")
    private String customerPhone;

    @Size(max = 100, message = "Issue description must not exceed 100 characters")
    private String issueDescription;

    @Pattern(regexp = "^(PENDING|IN_PROGRESS|APPROVED|REJECTED|COMPLETED)$",
            message = "Status must be one of: PENDING, IN_PROGRESS, APPROVED, REJECTED, COMPLETED")
    private WarrantyClaimStatus status;

    private LocalDate ClaimDate;

    @Email(message = "Invalid email format")
    private String email;

    private OfficeBranch officeBranch;



    private String RequiredParts;

    private String electricVehicleId;

    private String Vehicle_VIN_ID;
}

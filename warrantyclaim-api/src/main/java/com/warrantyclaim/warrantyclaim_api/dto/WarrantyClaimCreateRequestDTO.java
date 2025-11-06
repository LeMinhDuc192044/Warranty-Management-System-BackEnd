package com.warrantyclaim.warrantyclaim_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyClaimCreateRequestDTO { // this information for creating a Warranty Claim

    @NotBlank(message = "Require issue description to fix car!!!")
    private String issueDescription;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate claimDate;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    // @NotBlank(message = "Require Picture for verification!!")
    // private String picture;

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String customerName;

    @NotNull(message = "Office Branch is required!!!")
    private OfficeBranch officeBranch;

    private String requiredPart;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;

    private Long createdByUserId; // ID của SC_STAFF tạo claim

}
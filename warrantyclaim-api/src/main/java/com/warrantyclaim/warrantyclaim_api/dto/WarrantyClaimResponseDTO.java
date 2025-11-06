package com.warrantyclaim.warrantyclaim_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyClaimResponseDTO {
    private String claimId;
    private String customerName;
    private String customerPhone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate claimDate;

    private String issueDescription;
    private WarrantyClaimStatus status;
    private String email;
    private String requiredPart;
    private OfficeBranch officeBranch;
    private String rejectionReason;
    private Long createdByUserId;

    // Nested objects for related entities
    private VehicleBasicInfoDTO vehicle;

    private StaffBasicInfoDTO assignedStaff;
}
